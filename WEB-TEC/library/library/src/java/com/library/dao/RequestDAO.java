package com.library.dao;

import com.library.model.BookRequest;
import com.library.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public boolean createRequest(int userId, int bookId) {
        // Check if book is available first
        String duplicateQuery = "SELECT 1 FROM requests WHERE user_id = ? AND book_id = ? AND status IN ('pending','issued') LIMIT 1";
        String checkQuery = "SELECT available FROM books WHERE id = ?";
        String insertQuery = "INSERT INTO requests (user_id, book_id, status) VALUES (?, ?, 'pending')";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Prevent multiple active requests for the same book by the same user
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
                ResultSet rs = checkPs.executeQuery();
                if (rs.next() && rs.getInt("available") > 0) {
                    try (PreparedStatement insertPs = conn.prepareStatement(insertQuery)) {
                        insertPs.setInt(1, userId);
                        insertPs.setInt(2, bookId);
                        int rows = insertPs.executeUpdate();
                        if(rows > 0) {
                            conn.commit();
                            return true;
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
        String query = "SELECT r.*, b.title as book_title FROM requests r " +
                       "JOIN books b ON r.book_id = b.id WHERE r.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookRequest req = new BookRequest();
                req.setId(rs.getInt("id"));
                req.setUserId(rs.getInt("user_id"));
                req.setBookId(rs.getInt("book_id"));
                req.setRequestDate(rs.getTimestamp("request_date"));
                req.setStatus(rs.getString("status"));
                req.setBookTitle(rs.getString("book_title"));
                requests.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public List<BookRequest> getAllRequests() {
        List<BookRequest> requests = new ArrayList<>();
        String query = "SELECT r.*, u.email, b.title as book_title FROM requests r " +
                       "JOIN users u ON r.user_id = u.id " +
                       "JOIN books b ON r.book_id = b.id ORDER BY r.status, r.request_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                BookRequest req = new BookRequest();
                req.setId(rs.getInt("id"));
                req.setUserId(rs.getInt("user_id"));
                req.setBookId(rs.getInt("book_id"));
                req.setRequestDate(rs.getTimestamp("request_date"));
                req.setStatus(rs.getString("status"));
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
        String updateReqQuery = "UPDATE requests SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement reqPs = conn.prepareStatement(updateReqQuery)) {
                reqPs.setString(1, status);
                reqPs.setInt(2, requestId);
                int rows = reqPs.executeUpdate();
                
                if (rows > 0) {
                    if (status.equals("issued")) {
                        // Decrease availability
                        BookDAO bookDao = new BookDAO();
                        if(!bookDao.updateBookAvailability(bookId, -1)) {
                            conn.rollback();
                            return false;
                        }
                    } else if (status.equals("returned")) {
                        // Increase availability
                        BookDAO bookDao = new BookDAO();
                        if(!bookDao.updateBookAvailability(bookId, 1)) {
                            conn.rollback();
                            return false;
                        }
                    }
                    // rejected doesn't change availability
                    conn.commit();
                    return true;
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
}
