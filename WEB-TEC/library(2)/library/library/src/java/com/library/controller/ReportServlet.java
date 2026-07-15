package com.library.controller;

import com.library.model.User;
import com.library.util.DBConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ReportServlet extends HttpServlet {

    private static final DateTimeFormatter CSV_TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter CSV_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");
        if (user == null || (!"admin".equals(user.getRole()) && !"librarian".equals(user.getRole()))) {
            response.sendRedirect("index.jsp");
            return;
        }

        String type = request.getParameter("type");
        if (type == null) type = "issued";
        type = type.trim().toLowerCase();
        if (!("issued".equals(type) || "overdue".equals(type) || "fines".equals(type))) {
            type = "issued";
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "library_" + type + "_report_" + timestamp + ".csv";

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (PrintWriter out = response.getWriter()) {
            if ("issued".equals(type)) {
                writeIssued(out, false);
            } else if ("overdue".equals(type)) {
                writeIssued(out, true);
            } else {
                writeFines(out);
            }
        }
    }

    private void writeIssued(PrintWriter out, boolean overdueOnly) {
        String query =
                "SELECT r.id AS request_id, u.email AS user_email, b.title AS book_title, r.issue_date, r.due_date " +
                "FROM requests r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN books b ON r.book_id = b.id " +
                "WHERE r.status = 'issued' " +
                (overdueOnly ? "AND r.due_date IS NOT NULL AND r.due_date < CURDATE() " : "") +
                "ORDER BY r.due_date ASC, r.issue_date ASC";

        out.println("request_id,user_email,book_title,issue_date,due_date");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.print(rs.getInt("request_id"));
                out.print(",");
                out.print(csv(rs.getString("user_email")));
                out.print(",");
                out.print(csv(rs.getString("book_title")));
                out.print(",");
                out.print(csv(fmtTs(rs.getTimestamp("issue_date"))));
                out.print(",");
                out.println(csv(fmtDate(rs.getDate("due_date"))));
            }
        } catch (SQLException e) {
            out.println();
            out.println("error," + csv("Failed to generate report: " + e.getMessage()));
        }
    }

    private void writeFines(PrintWriter out) {
        String query =
                "SELECT r.id AS request_id, u.email AS user_email, b.title AS book_title, r.return_date, r.fine_amount, r.fine_paid, r.fine_paid_date, " +
                "(SELECT COALESCE(SUM(p.amount_paise),0) FROM payments p WHERE p.request_id = r.id AND p.status = 'paid') AS paid_paise " +
                "FROM requests r " +
                "JOIN users u ON r.user_id = u.id " +
                "JOIN books b ON r.book_id = b.id " +
                "WHERE r.status = 'returned' AND r.fine_amount > 0 " +
                "ORDER BY r.return_date DESC";

        out.println("request_id,user_email,book_title,return_date,fine_amount,paid_amount,due_amount,fine_paid,fine_paid_date");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.print(rs.getInt("request_id"));
                out.print(",");
                out.print(csv(rs.getString("user_email")));
                out.print(",");
                out.print(csv(rs.getString("book_title")));
                out.print(",");
                out.print(csv(fmtTs(rs.getTimestamp("return_date"))));
                out.print(",");
                java.math.BigDecimal fine = rs.getBigDecimal("fine_amount");
                if (fine == null) fine = java.math.BigDecimal.ZERO;

                int paidPaise = rs.getInt("paid_paise");
                java.math.BigDecimal paid = java.math.BigDecimal.valueOf(paidPaise).divide(new java.math.BigDecimal("100"));
                java.math.BigDecimal due = fine.subtract(paid);
                if (due.compareTo(java.math.BigDecimal.ZERO) < 0) due = java.math.BigDecimal.ZERO;

                out.print(fine);
                out.print(",");
                out.print(paid);
                out.print(",");
                out.print(due);
                out.print(",");
                out.print(rs.getInt("fine_paid") == 1 ? "yes" : "no");
                out.print(",");
                out.println(csv(fmtTs(rs.getTimestamp("fine_paid_date"))));
            }
        } catch (SQLException e) {
            out.println();
            out.println("error," + csv("Failed to generate report: " + e.getMessage()));
        }
    }

    private String fmtTs(Timestamp ts) {
        if (ts == null) return "";
        return CSV_TS.format(ts.toLocalDateTime());
    }

    private String fmtDate(java.sql.Date d) {
        if (d == null) return "";
        return CSV_DATE.format(d.toLocalDate());
    }

    private String csv(String value) {
        if (value == null) return "\"\"";
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}
