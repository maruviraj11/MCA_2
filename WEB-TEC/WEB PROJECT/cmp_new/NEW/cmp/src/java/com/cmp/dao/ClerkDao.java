package com.cmp.dao;

import com.cmp.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClerkDao {

    public List<Map> getComplaints(int clerkId) {
        try {
            return getComplaintsInternal(clerkId, true, false);
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof java.sql.SQLSyntaxErrorException) {
                return getComplaintsInternal(clerkId, false, false);
            }
            throw ex;
        }
    }

    public List<Map> getResolvedComplaints(int clerkId) {
        try {
            return getComplaintsInternal(clerkId, true, true);
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof java.sql.SQLSyntaxErrorException) {
                return getComplaintsInternal(clerkId, false, true);
            }
            throw ex;
        }
    }

    private List<Map> getComplaintsInternal(int clerkId, boolean includeAttachmentFields, boolean historyMode) {
        List<Map> list = new ArrayList<Map>();
        String sql = "SELECT c.id, c.title, c.description, c.status, c.complaint_scope, c.created_at, "
                + (includeAttachmentFields ? "c.attachment_name, c.attachment_path, c.attachment_type, " : "")
                + "u.full_name AS raised_by, u.role AS raised_role, cl.class_name "
                + "FROM complaints c "
                + "JOIN users u ON u.id = c.raised_by "
                + "LEFT JOIN classes cl ON cl.id = c.class_id "
                + "WHERE c.assigned_clerk_id = ? "
                + (historyMode ? "AND c.status = 'SOLVED' " : "AND c.status <> 'SOLVED' ")
                + "ORDER BY c.created_at DESC";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, clerkId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("id", rs.getInt("id"));
                row.put("title", rs.getString("title"));
                row.put("description", rs.getString("description"));
                row.put("status", rs.getString("status"));
                row.put("complaintScope", rs.getString("complaint_scope"));
                row.put("createdAt", rs.getString("created_at"));
                row.put("raisedBy", rs.getString("raised_by"));
                row.put("raisedRole", rs.getString("raised_role"));
                row.put("className", rs.getString("class_name"));
                if (includeAttachmentFields) {
                    row.put("attachmentName", rs.getString("attachment_name"));
                    row.put("attachmentPath", rs.getString("attachment_path"));
                    row.put("attachmentType", rs.getString("attachment_type"));
                } else {
                    row.put("attachmentName", null);
                    row.put("attachmentPath", null);
                    row.put("attachmentType", null);
                }
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load complaints.", ex);
        }
        return list;
    }

    public void updateStatus(int complaintId, String status, String remarks) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement currentPs = con.prepareStatement(
                    "SELECT status FROM complaints WHERE id = ?");
            currentPs.setInt(1, complaintId);
            ResultSet currentRs = currentPs.executeQuery();
            String currentStatus = null;
            if (currentRs.next()) {
                currentStatus = currentRs.getString("status");
            }
            currentRs.close();
            currentPs.close();
            if (currentStatus == null) {
                con.close();
                throw new RuntimeException("Complaint not found.");
            }
            if ("SOLVED".equals(currentStatus)) {
                con.close();
                throw new RuntimeException("Solved complaint is already moved to history and cannot be changed again.");
            }
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE complaints SET status = ?, clerk_remarks = ?, updated_at = NOW() WHERE id = ?");
            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setInt(3, complaintId);
            ps.executeUpdate();
            ps.close();
            if (!status.equals(currentStatus)) {
                PreparedStatement notifyPs = con.prepareStatement(
                        "INSERT INTO notifications(user_id, complaint_id, message, is_read, created_at) "
                        + "SELECT raised_by, id, CONCAT('Complaint status changed to ', ?), 0, NOW() FROM complaints WHERE id = ?");
                notifyPs.setString(1, status);
                notifyPs.setInt(2, complaintId);
                notifyPs.executeUpdate();
                notifyPs.close();
            }
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to update complaint status.", ex);
        }
    }

    public int getUnreadNotificationCount(int clerkId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM complaints WHERE assigned_clerk_id = ? AND clerk_seen = 0");
            ps.setInt(1, clerkId);
            ResultSet rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            ps.close();
            con.close();
            return count;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load notification count.", ex);
        }
    }

    public List<Map> getRecentComplaintAlerts(int clerkId) {
        List<Map> list = new ArrayList<Map>();
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT title, status, created_at FROM complaints WHERE assigned_clerk_id = ? ORDER BY created_at DESC LIMIT 6");
            ps.setInt(1, clerkId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("title", rs.getString("title"));
                row.put("status", rs.getString("status"));
                row.put("createdAt", rs.getString("created_at"));
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load clerk alerts.", ex);
        }
        return list;
    }

    public void markClerkSeen(int clerkId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE complaints SET clerk_seen = 1 WHERE assigned_clerk_id = ?");
            ps.setInt(1, clerkId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear clerk notifications.", ex);
        }
    }

    public void clearActiveComplaints(int clerkId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM complaints WHERE assigned_clerk_id = ? AND status <> 'SOLVED'");
            ps.setInt(1, clerkId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear active complaints.", ex);
        }
    }

    public void clearResolvedHistory(int clerkId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM complaints WHERE assigned_clerk_id = ? AND status = 'SOLVED'");
            ps.setInt(1, clerkId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear resolved history.", ex);
        }
    }
}
