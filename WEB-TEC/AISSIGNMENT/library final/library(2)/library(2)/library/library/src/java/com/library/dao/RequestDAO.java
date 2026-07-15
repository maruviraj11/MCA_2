package com.library.dao;

import com.library.model.BookRequest;
import com.library.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    private static final int BORROW_LIMIT = 3;
    private static final int LOAN_DAYS = 14;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5.00");

    public boolean createRequest(int userId, int bookId) {
        // Prevent multiple active requests for the same book + enforce max issued books limit
        String duplicateQuery = "SELECT 1 FROM requests WHERE user_id = ? AND book_id = ? AND status IN ('pending','issued') LIMIT 1";
        String borrowLimitQuery = "SELECT COUNT(*) AS c FROM requests WHERE user_id = ? AND status = 'issued'";
        String checkQuery = "SELECT available FROM books WHERE id = ?";
        String insertQuery = "INSERT INTO requests (user_id, book_id, status) VALUES (?, ?, 'pending')";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement limitPs = conn.prepareStatement(borrowLimitQuery)) {
                limitPs.setInt(1, userId);
                try (ResultSet limitRs = limitPs.executeQuery()) {
                    if (limitRs.next() && limitRs.getInt("c") >= BORROW_LIMIT) {
                        conn.rollback();
                        return false;
                    }
                }
            }
            
            try (PreparedStatement dupPs = conn.prepareStatement(duplicateQuery)) {
                dupPs.setInt(1, userId);
                dupPs.setInt(2, bookId);
                try (ResultSet dupRs = dupPs.executeQuery()) {
                    if (dupRs.next()) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement checkPs = conn.prepareStatement(checkQuery)) {
                checkPs.setInt(1, bookId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt("available") > 0) {
                        try (PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                            insertPs.setInt(1, userId);
                            insertPs.setInt(2, bookId);
                            int rows = insertPs.executeUpdate();
                            if (rows > 0) {
                                conn.commit();
                                return true;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<BookRequest> getRequestsByUser(int userId) {
        List<BookRequest> requests = new ArrayList<>();
        String query = "SELECT r.*, b.title as book_title, " +
                "(SELECT COALESCE(SUM(p.amount_paise),0) FROM payments p WHERE p.request_id = r.id AND p.status = 'paid') AS paid_paise " +
                "FROM requests r " +
                "JOIN books b ON r.book_id = b.id WHERE r.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookRequest req = extractRequestFromResultSet(rs);
                    req.setBookTitle(rs.getString("book_title"));
                    requests.add(req);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<BookRequest> getAllRequests() {
        List<BookRequest> requests = new ArrayList<>();
        String query = "SELECT r.*, u.email, b.title as book_title, " +
                "(SELECT COALESCE(SUM(p.amount_paise),0) FROM payments p WHERE p.request_id = r.id AND p.status = 'paid') AS paid_paise " +
                "FROM requests r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN books b ON r.book_id = b.id ORDER BY r.status, r.request_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                BookRequest req = extractRequestFromResultSet(rs);
                req.setUserName(rs.getString("email"));
                req.setBookTitle(rs.getString("book_title"));
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public boolean updateRequestStatus(int requestId, String status, int bookId) {
        return updateRequestStatus(requestId, status, bookId, null);
    }

    public boolean updateRequestStatus(int requestId, String status, int bookId, Date dueDateOverride) {
        if (status == null) return false;
        status = status.trim().toLowerCase();
        if (!("issued".equals(status) || "returned".equals(status) || "rejected".equals(status))) return false;

        String currentQuery = "SELECT status, due_date FROM requests WHERE id = ? FOR UPDATE";

        String issueDefaultQuery = "UPDATE requests SET status = 'issued', issue_date = NOW(), due_date = DATE_ADD(CURDATE(), INTERVAL ? DAY), " +
                "return_date = NULL, fine_amount = 0, fine_paid = 0, fine_paid_date = NULL " +
                "WHERE id = ? AND status = 'pending'";
        String issueCustomQuery = "UPDATE requests SET status = 'issued', issue_date = NOW(), due_date = ?, " +
                "return_date = NULL, fine_amount = 0, fine_paid = 0, fine_paid_date = NULL " +
                "WHERE id = ? AND status = 'pending'";
        String rejectRequestQuery = "UPDATE requests SET status = 'rejected' WHERE id = ? AND status = 'pending'";
        String returnRequestQuery = "UPDATE requests SET status = 'returned', return_date = NOW(), fine_amount = ?, fine_paid = ?, fine_paid_date = ? " +
                "WHERE id = ? AND status = 'issued'";
        String paidSumQuery = "SELECT COALESCE(SUM(amount_paise),0) AS s FROM payments WHERE request_id = ? AND status = 'paid'";

        String decreaseAvailabilityQuery = "UPDATE books SET available = available - 1 WHERE id = ? AND available > 0";
        String increaseAvailabilityQuery = "UPDATE books SET available = available + 1 WHERE id = ? AND available < quantity";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            String currentStatus = null;
            Date dueDate = null;
            try (PreparedStatement curPs = conn.prepareStatement(currentQuery)) {
                curPs.setInt(1, requestId);
                try (ResultSet curRs = curPs.executeQuery()) {
                    if (curRs.next()) {
                        currentStatus = curRs.getString("status");
                        dueDate = curRs.getDate("due_date");
                    }
                }
            }

            if (currentStatus == null) {
                conn.rollback();
                return false;
            }

            if ("issued".equals(status)) {
                if (!"pending".equalsIgnoreCase(currentStatus)) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement decPs = conn.prepareStatement(decreaseAvailabilityQuery)) {
                    decPs.setInt(1, bookId);
                    if (decPs.executeUpdate() <= 0) {
                        conn.rollback();
                        return false;
                    }
                }

                if (dueDateOverride != null) {
                    try (PreparedStatement issuePs = conn.prepareStatement(issueCustomQuery)) {
                        issuePs.setDate(1, dueDateOverride);
                        issuePs.setInt(2, requestId);
                        if (issuePs.executeUpdate() <= 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                } else {
                    try (PreparedStatement issuePs = conn.prepareStatement(issueDefaultQuery)) {
                        issuePs.setInt(1, LOAN_DAYS);
                        issuePs.setInt(2, requestId);
                        if (issuePs.executeUpdate() <= 0) {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                conn.commit();
                return true;
            }

            if ("rejected".equals(status)) {
                if (!"pending".equalsIgnoreCase(currentStatus)) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement rejPs = conn.prepareStatement(rejectRequestQuery)) {
                    rejPs.setInt(1, requestId);
                    if (rejPs.executeUpdate() <= 0) {
                        conn.rollback();
                        return false;
                    }
                }

                conn.commit();
                return true;
            }

            if ("returned".equals(status)) {
                if (!"issued".equalsIgnoreCase(currentStatus)) {
                    conn.rollback();
                    return false;
                }

                BigDecimal fine = calculateFine(dueDate, new Timestamp(System.currentTimeMillis()));
                boolean finePaid = fine.compareTo(BigDecimal.ZERO) == 0;
                if (!finePaid) {
                    int paidPaise = 0;
                    try (PreparedStatement paidPs = conn.prepareStatement(paidSumQuery)) {
                        paidPs.setInt(1, requestId);
                        try (ResultSet prs = paidPs.executeQuery()) {
                            if (prs.next()) paidPaise = prs.getInt("s");
                        }
                    }
                    int finePaise = fine.multiply(new BigDecimal("100")).intValue();
                    finePaid = paidPaise >= finePaise;
                }

                try (PreparedStatement retPs = conn.prepareStatement(returnRequestQuery)) {
                    retPs.setBigDecimal(1, fine);
                    retPs.setInt(2, finePaid ? 1 : 0);
                    retPs.setTimestamp(3, finePaid ? new Timestamp(System.currentTimeMillis()) : null);
                    retPs.setInt(4, requestId);
                    if (retPs.executeUpdate() <= 0) {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement incPs = conn.prepareStatement(increaseAvailabilityQuery)) {
                    incPs.setInt(1, bookId);
                    incPs.executeUpdate();
                }

                conn.commit();
                return true;
            }

            conn.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markFinePaid(int requestId) {
        String query = "UPDATE requests SET fine_paid = 1, fine_paid_date = NOW() " +
                "WHERE id = ? AND status = 'returned' AND fine_amount > 0 AND fine_paid = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, requestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BookRequest getRequestById(int requestId) {
        String query = "SELECT * FROM requests WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return extractRequestFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean returnBookByUser(int requestId, int userId, Timestamp returnAt) {
        if (returnAt == null) return false;

        String currentQuery = "SELECT status, due_date, book_id FROM requests WHERE id = ? AND user_id = ? FOR UPDATE";
        String returnRequestQuery = "UPDATE requests SET status = 'returned', return_date = ?, fine_amount = ?, fine_paid = ?, fine_paid_date = ? " +
                "WHERE id = ? AND user_id = ? AND status = 'issued'";
        String increaseAvailabilityQuery = "UPDATE books SET available = available + 1 WHERE id = ? AND available < quantity";
        String paidSumQuery = "SELECT COALESCE(SUM(amount_paise),0) AS s FROM payments WHERE request_id = ? AND status = 'paid'";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            String currentStatus = null;
            Date dueDate = null;
            Integer bookId = null;
            try (PreparedStatement curPs = conn.prepareStatement(currentQuery)) {
                curPs.setInt(1, requestId);
                curPs.setInt(2, userId);
                try (ResultSet curRs = curPs.executeQuery()) {
                    if (curRs.next()) {
                        currentStatus = curRs.getString("status");
                        dueDate = curRs.getDate("due_date");
                        bookId = curRs.getInt("book_id");
                    }
                }
            }

            if (currentStatus == null || !"issued".equalsIgnoreCase(currentStatus) || bookId == null) {
                conn.rollback();
                return false;
            }

            BigDecimal fine = calculateFine(dueDate, returnAt);
            boolean finePaid = fine.compareTo(BigDecimal.ZERO) == 0;
            if (!finePaid) {
                int paidPaise = 0;
                try (PreparedStatement paidPs = conn.prepareStatement(paidSumQuery)) {
                    paidPs.setInt(1, requestId);
                    try (ResultSet prs = paidPs.executeQuery()) {
                        if (prs.next()) paidPaise = prs.getInt("s");
                    }
                }
                int finePaise = fine.multiply(new BigDecimal("100")).intValue();
                finePaid = paidPaise >= finePaise;
            }

            try (PreparedStatement retPs = conn.prepareStatement(returnRequestQuery)) {
                retPs.setTimestamp(1, returnAt);
                retPs.setBigDecimal(2, fine);
                retPs.setInt(3, finePaid ? 1 : 0);
                retPs.setTimestamp(4, finePaid ? new Timestamp(System.currentTimeMillis()) : null);
                retPs.setInt(5, requestId);
                retPs.setInt(6, userId);
                if (retPs.executeUpdate() <= 0) {
                    conn.rollback();
                    return false;
                }
            }

            try (PreparedStatement incPs = conn.prepareStatement(increaseAvailabilityQuery)) {
                incPs.setInt(1, bookId.intValue());
                incPs.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private BookRequest extractRequestFromResultSet(ResultSet rs) throws SQLException {
        BookRequest req = new BookRequest();
        req.setId(rs.getInt("id"));
        req.setUserId(rs.getInt("user_id"));
        req.setBookId(rs.getInt("book_id"));
        req.setRequestDate(rs.getTimestamp("request_date"));
        req.setIssueDate(rs.getTimestamp("issue_date"));
        req.setDueDate(rs.getDate("due_date"));
        req.setReturnDate(rs.getTimestamp("return_date"));
        BigDecimal dbFine = rs.getBigDecimal("fine_amount");
        req.setFineAmount(dbFine);
        req.setFinePaid(rs.getInt("fine_paid") == 1);
        req.setFinePaidDate(rs.getTimestamp("fine_paid_date"));
        String status = rs.getString("status");
        req.setStatus(status);

        try {
            int paidPaise = rs.getInt("paid_paise");
            BigDecimal paid = BigDecimal.valueOf(paidPaise).divide(new BigDecimal("100"));
            req.setPaidAmount(paid);
        } catch (SQLException ignored) {
            req.setPaidAmount(BigDecimal.ZERO);
        }

        if ("issued".equalsIgnoreCase(status)) {
            BigDecimal est = calculateFine(req.getDueDate(), new Timestamp(System.currentTimeMillis()));
            req.setFineAmount(est);
        }
        return req;
    }

    private BigDecimal calculateFine(Date dueDate, Timestamp now) {
        if (dueDate == null) return BigDecimal.ZERO;
        java.time.LocalDate due = dueDate.toLocalDate();
        java.time.LocalDate today = now.toLocalDateTime().toLocalDate();
        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(due, today);
        if (daysLate <= 0) return BigDecimal.ZERO;
        return FINE_PER_DAY.multiply(BigDecimal.valueOf(daysLate));
    }
}
