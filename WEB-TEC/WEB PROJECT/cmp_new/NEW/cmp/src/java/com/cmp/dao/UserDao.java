package com.cmp.dao;

import com.cmp.config.AppConfig;
import com.cmp.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDao {

    public void createComplaint(int userId, Integer classId, String scope, String title, String description,
            String attachmentName, String attachmentPath, String attachmentType) {
        try {
            createComplaintInternal(userId, classId, scope, title, description, attachmentName, attachmentPath, attachmentType, true);
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof java.sql.SQLSyntaxErrorException) {
                createComplaintInternal(userId, classId, scope, title, description, null, null, null, false);
                return;
            }
            throw ex;
        }
    }

    private void createComplaintInternal(int userId, Integer classId, String scope, String title, String description,
            String attachmentName, String attachmentPath, String attachmentType, boolean includeAttachmentFields) {
        try {
            Connection con = DBUtil.getConnection();
            validateDuplicateComplaint(con, userId, scope, title, description);
            PreparedStatement deptPs = con.prepareStatement("SELECT department_id FROM users WHERE id = ?");
            deptPs.setInt(1, userId);
            ResultSet deptRs = deptPs.executeQuery();
            int departmentId = 0;
            if (deptRs.next()) {
                departmentId = deptRs.getInt(1);
            }
            deptRs.close();
            deptPs.close();
            if (departmentId == 0) {
                con.close();
                throw new RuntimeException("Your account is not assigned to any department.");
            }

            PreparedStatement clerkPs = con.prepareStatement("SELECT clerk_user_id FROM departments WHERE id = ?");
            clerkPs.setInt(1, departmentId);
            ResultSet clerkRs = clerkPs.executeQuery();
            int clerkId = 0;
            if (clerkRs.next()) {
                clerkId = clerkRs.getInt(1);
            }
            clerkRs.close();
            clerkPs.close();
            if (clerkId == 0) {
                con.close();
                throw new RuntimeException("No clerk is assigned to your department yet. Please contact admin.");
            }

            PreparedStatement ps = con.prepareStatement(includeAttachmentFields
                    ? "INSERT INTO complaints(raised_by, department_id, class_id, assigned_clerk_id, complaint_scope, title, description, attachment_name, attachment_path, attachment_type, status, clerk_seen, created_at, updated_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PENDING', 0, NOW(), NOW())"
                    : "INSERT INTO complaints(raised_by, department_id, class_id, assigned_clerk_id, complaint_scope, title, description, status, clerk_seen, created_at, updated_at) VALUES(?, ?, ?, ?, ?, ?, ?, 'PENDING', 0, NOW(), NOW())");
            ps.setInt(1, userId);
            ps.setInt(2, departmentId);
            if (classId == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, classId.intValue());
            }
            if (clerkId == 0) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, clerkId);
            }
            ps.setString(5, scope);
            ps.setString(6, title);
            ps.setString(7, description);
            if (includeAttachmentFields) {
                ps.setString(8, attachmentName);
                ps.setString(9, attachmentPath);
                ps.setString(10, attachmentType);
            }
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to save complaint.", ex);
        }
    }

    public List<Map> getComplaintHistory(int userId) {
        try {
            return getComplaintHistoryInternal(userId, true);
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof java.sql.SQLSyntaxErrorException) {
                return getComplaintHistoryInternal(userId, false);
            }
            throw ex;
        }
    }

    private List<Map> getComplaintHistoryInternal(int userId, boolean includeAttachmentFields) {
        List<Map> list = new ArrayList<Map>();
        String sql = "SELECT c.id, c.title, c.description, c.status, c.complaint_scope, c.created_at, c.updated_at, c.clerk_remarks, "
                + (includeAttachmentFields ? "c.attachment_name, c.attachment_path, c.attachment_type, " : "")
                + "TIMESTAMPDIFF(MINUTE, c.created_at, NOW()) AS age_minutes, "
                + "(SELECT COUNT(*) FROM notifications n WHERE n.complaint_id = c.id) AS clerk_action_count "
                + "FROM complaints c WHERE c.raised_by = ? ORDER BY c.created_at DESC";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("id", rs.getInt("id"));
                row.put("title", rs.getString("title"));
                row.put("description", rs.getString("description"));
                row.put("status", rs.getString("status"));
                row.put("complaintScope", rs.getString("complaint_scope"));
                row.put("createdAt", rs.getString("created_at"));
                row.put("updatedAt", rs.getString("updated_at"));
                row.put("clerkRemarks", rs.getString("clerk_remarks"));
                if (includeAttachmentFields) {
                    row.put("attachmentName", rs.getString("attachment_name"));
                    row.put("attachmentPath", rs.getString("attachment_path"));
                    row.put("attachmentType", rs.getString("attachment_type"));
                } else {
                    row.put("attachmentName", null);
                    row.put("attachmentPath", null);
                    row.put("attachmentType", null);
                }
                boolean withinTime = rs.getInt("age_minutes") <= AppConfig.COMPLAINT_EDIT_WINDOW_MINUTES;
                boolean rejected = "REJECTED".equals(rs.getString("status"));
                row.put("canEdit", Boolean.valueOf(withinTime && rejected));
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load history.", ex);
        }
        return list;
    }

    public void updateComplaint(int complaintId, int userId, String title, String description) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE complaints SET title = ?, description = ?, updated_at = NOW(), clerk_seen = 0 "
                    + "WHERE id = ? AND raised_by = ? "
                    + "AND TIMESTAMPDIFF(MINUTE, created_at, NOW()) <= ? "
                    + "AND status = 'REJECTED'");
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setInt(3, complaintId);
            ps.setInt(4, userId);
            ps.setInt(5, AppConfig.COMPLAINT_EDIT_WINDOW_MINUTES);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to update complaint.", ex);
        }
    }

    public int getUserNotificationCount(int userId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0");
            ps.setInt(1, userId);
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
            throw new RuntimeException("Unable to load notifications.", ex);
        }
    }

    public List<Map> getRecentNotifications(int userId) {
        List<Map> list = new ArrayList<Map>();
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT message, is_read, created_at FROM notifications WHERE user_id = ? ORDER BY created_at DESC LIMIT 6");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("message", rs.getString("message"));
                row.put("isRead", Boolean.valueOf(rs.getInt("is_read") == 1));
                row.put("createdAt", rs.getString("created_at"));
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load recent notifications.", ex);
        }
        return list;
    }

    public void markUserNotificationsRead(int userId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE notifications SET is_read = 1 WHERE user_id = ?");
            ps.setInt(1, userId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to mark notifications.", ex);
        }
    }

    public void clearComplaintHistory(int userId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM complaints WHERE raised_by = ?");
            ps.setInt(1, userId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear complaint history.", ex);
        }
    }

    private void validateDuplicateComplaint(Connection con, int userId, String scope, String title, String description) throws Exception {
        PreparedStatement ps = con.prepareStatement(
                "SELECT id FROM complaints WHERE raised_by = ? AND complaint_scope = ? AND title = ? "
                + "AND description = ? AND status <> 'SOLVED' LIMIT 1");
        ps.setInt(1, userId);
        ps.setString(2, scope);
        ps.setString(3, title);
        ps.setString(4, description);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();
        if (exists) {
            throw new RuntimeException("This complaint is already submitted. Update the rejected complaint instead of creating the same one again.");
        }
    }
}
