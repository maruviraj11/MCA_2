package com.library.dao;

import com.library.model.Payment;
import com.library.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PaymentDAO {

    public Payment createPayment(int requestId, int userId, int amountPaise, String currency, String provider, String orderId) {
        String query = "INSERT INTO payments (request_id, user_id, amount_paise, currency, provider, order_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'created')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, requestId);
            ps.setInt(2, userId);
            ps.setInt(3, amountPaise);
            ps.setString(4, currency);
            ps.setString(5, provider);
            ps.setString(6, orderId);
            int rows = ps.executeUpdate();
            if (rows <= 0) return null;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) return null;
                int id = keys.getInt(1);
                Payment p = new Payment();
                p.setId(id);
                p.setRequestId(requestId);
                p.setUserId(userId);
                p.setAmountPaise(amountPaise);
                p.setCurrency(currency);
                p.setProvider(provider);
                p.setOrderId(orderId);
                p.setStatus("created");
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Payment getByOrderId(String orderId) {
        String query = "SELECT * FROM payments WHERE order_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return extract(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int sumPaidPaiseByRequest(int requestId) {
        String query = "SELECT COALESCE(SUM(amount_paise),0) AS s FROM payments WHERE request_id = ? AND status = 'paid'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0;
                return rs.getInt("s");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean markPaid(int paymentDbId, String paymentId, String signature) {
        String query = "UPDATE payments SET status = 'paid', payment_id = ?, signature = ?, paid_at = NOW() " +
                "WHERE id = ? AND status = 'created'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, paymentId);
            ps.setString(2, signature);
            ps.setInt(3, paymentDbId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markFailed(int paymentDbId) {
        String query = "UPDATE payments SET status = 'failed' WHERE id = ? AND status = 'created'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, paymentDbId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Payment extract(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getInt("id"));
        p.setRequestId(rs.getInt("request_id"));
        p.setUserId(rs.getInt("user_id"));
        p.setProvider(rs.getString("provider"));
        p.setCurrency(rs.getString("currency"));
        p.setAmountPaise(rs.getInt("amount_paise"));
        p.setOrderId(rs.getString("order_id"));
        p.setPaymentId(rs.getString("payment_id"));
        p.setSignature(rs.getString("signature"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setPaidAt(rs.getTimestamp("paid_at"));
        return p;
    }
}
