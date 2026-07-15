package com.cmp.dao;

import com.cmp.transport.MailTransport;
import com.cmp.util.DBUtil;
import com.cmp.util.PasswordUtil;
import com.cmp.util.ValidationUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDao {

    public List<Map> getDepartments() {
        List<Map> list = new ArrayList<Map>();
        String sql = "SELECT d.id, d.name, d.description, d.hod_user_id, d.clerk_user_id, "
                + "hod.full_name AS hod_name, clerk.full_name AS clerk_name "
                + "FROM departments d "
                + "LEFT JOIN users hod ON hod.id = d.hod_user_id "
                + "LEFT JOIN users clerk ON clerk.id = d.clerk_user_id ORDER BY d.name";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("id", rs.getInt("id"));
                row.put("name", rs.getString("name"));
                row.put("description", rs.getString("description"));
                row.put("hodUserId", rs.getObject("hod_user_id"));
                row.put("clerkUserId", rs.getObject("clerk_user_id"));
                row.put("hodName", rs.getString("hod_name"));
                row.put("clerkName", rs.getString("clerk_name"));
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load departments.", ex);
        }
        return list;
    }

    public List<Map> getUsersByRole(String role) {
        List<Map> list = new ArrayList<Map>();
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT u.id, u.full_name, u.email, d.name AS department_name "
                    + "FROM users u "
                    + "LEFT JOIN departments d ON d.id = u.department_id "
                    + "WHERE u.role = ? ORDER BY u.full_name");
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("id", rs.getInt("id"));
                row.put("fullName", rs.getString("full_name"));
                row.put("email", rs.getString("email"));
                row.put("departmentName", rs.getString("department_name"));
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load users.", ex);
        }
        return list;
    }

    public List<Map> getRecentAdminActivities() {
        List<Map> list = new ArrayList<Map>();
        try {
            Connection con = DBUtil.getConnection();

            PreparedStatement deptPs = con.prepareStatement(
                    "SELECT id, name, description FROM departments ORDER BY id DESC LIMIT 3");
            ResultSet deptRs = deptPs.executeQuery();
            while (deptRs.next()) {
                Map row = new HashMap();
                row.put("title", "Department created: " + deptRs.getString("name"));
                row.put("detail", deptRs.getString("description") == null || deptRs.getString("description").trim().length() == 0
                        ? "Department is available for HOD and clerk assignment."
                        : deptRs.getString("description"));
                row.put("createdAt", "Department ID " + deptRs.getInt("id"));
                list.add(row);
            }
            deptRs.close();
            deptPs.close();

            PreparedStatement userPs = con.prepareStatement(
                    "SELECT full_name, role, created_at FROM users WHERE role IN ('HOD', 'CLERK') ORDER BY created_at DESC LIMIT 3");
            ResultSet userRs = userPs.executeQuery();
            while (userRs.next()) {
                Map row = new HashMap();
                row.put("title", userRs.getString("role") + " account created");
                row.put("detail", userRs.getString("full_name"));
                row.put("createdAt", userRs.getString("created_at"));
                list.add(row);
            }
            userRs.close();
            userPs.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load admin activities.", ex);
        }
        return list;
    }

    public int getTotalComplaintCount() {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM complaints");
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
            throw new RuntimeException("Unable to load complaint count.", ex);
        }
    }

    public int getPendingComplaintCount() {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM complaints WHERE status IN ('PENDING', 'ASSIGNED')");
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
            throw new RuntimeException("Unable to load pending complaint count.", ex);
        }
    }

    public void saveDepartment(String name, String description, String hodId, String clerkId) {
        try {
            name = name == null ? "" : name.trim();
            description = description == null ? "" : description.trim();
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO departments(name, description, hod_user_id, clerk_user_id) VALUES(?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, description);
            if (hodId == null || hodId.trim().length() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, Integer.parseInt(hodId));
            }
            if (clerkId == null || clerkId.trim().length() == 0) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, Integer.parseInt(clerkId));
            }
            ps.executeUpdate();
            ps.close();
            con.close();
            refreshDepartmentAssignments();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to save department.", ex);
        }
    }

    public String saveRoleUser(String fullName, String email, String role, String departmentId) {
        String plainPassword = PasswordUtil.generatePassword();
        try {
            fullName = fullName == null ? "" : fullName.trim();
            email = ValidationUtil.normalizeEmailOrThrow(email);
            validateUniqueEmail(email, null);
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(full_name, email, password_hash, role, department_id, must_change_password, is_active) VALUES(?, ?, ?, ?, ?, 1, 1)");
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.hash(plainPassword));
            ps.setString(4, role);
            if (departmentId == null || departmentId.trim().length() == 0) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, Integer.parseInt(departmentId));
            }
            ps.executeUpdate();
            ps.close();
            con.close();

            MailTransport.sendMail(
                    email,
                    "Your " + role + " account login details",
                    plainPassword,
                    fullName,
                    role
            );

            return plainPassword;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to save role user.", ex);
        }
    }

    public void updateDepartment(int departmentId, String name, String description, String hodId, String clerkId) {
        try {
            name = name == null ? "" : name.trim();
            description = description == null ? "" : description.trim();
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE departments SET name = ?, description = ?, hod_user_id = ?, clerk_user_id = ? WHERE id = ?");
            ps.setString(1, name);
            ps.setString(2, description);
            if (hodId == null || hodId.trim().length() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, Integer.parseInt(hodId));
            }
            if (clerkId == null || clerkId.trim().length() == 0) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, Integer.parseInt(clerkId));
            }
            ps.setInt(5, departmentId);
            ps.executeUpdate();
            ps.close();
            con.close();
            refreshDepartmentAssignments();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to update department.", ex);
        }
    }

    public void updateRoleUser(int userId, String role, String fullName, String email) {
        try {
            fullName = fullName == null ? "" : fullName.trim();
            email = ValidationUtil.normalizeEmailOrThrow(email);
            validateUniqueEmail(email, Integer.valueOf(userId));
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET full_name = ?, email = ? WHERE id = ? AND role = ?");
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setInt(3, userId);
            ps.setString(4, role);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to update " + role + ".", ex);
        }
    }

    public void assignDepartment(int departmentId, String hodId, String clerkId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE departments SET hod_user_id = ?, clerk_user_id = ? WHERE id = ?");
            if (hodId == null || hodId.trim().length() == 0) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, Integer.parseInt(hodId));
            }
            if (clerkId == null || clerkId.trim().length() == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, Integer.parseInt(clerkId));
            }
            ps.setInt(3, departmentId);
            ps.executeUpdate();
            ps.close();
            con.close();
            refreshDepartmentAssignments();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to assign department users.", ex);
        }
    }

    public void deleteDepartment(int departmentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement clearPs = con.prepareStatement(
                    "UPDATE departments SET hod_user_id = NULL, clerk_user_id = NULL WHERE id = ?");
            clearPs.setInt(1, departmentId);
            clearPs.executeUpdate();
            clearPs.close();

            PreparedStatement deletePs = con.prepareStatement("DELETE FROM departments WHERE id = ?");
            deletePs.setInt(1, departmentId);
            deletePs.executeUpdate();
            deletePs.close();
            con.close();
            refreshDepartmentAssignments();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to delete department.", ex);
        }
    }

    public void deleteRoleUser(int userId, String role) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement unassignPs;
            if ("HOD".equals(role)) {
                unassignPs = con.prepareStatement("UPDATE departments SET hod_user_id = NULL WHERE hod_user_id = ?");
            } else {
                unassignPs = con.prepareStatement("UPDATE departments SET clerk_user_id = NULL WHERE clerk_user_id = ?");
            }
            unassignPs.setInt(1, userId);
            unassignPs.executeUpdate();
            unassignPs.close();

            PreparedStatement deletePs = con.prepareStatement("DELETE FROM users WHERE id = ? AND role = ?");
            deletePs.setInt(1, userId);
            deletePs.setString(2, role);
            deletePs.executeUpdate();
            deletePs.close();
            con.close();
            refreshDepartmentAssignments();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to delete " + role + ".", ex);
        }
    }

    public void clearAllDepartments() {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM departments");
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear departments.", ex);
        }
    }

    public void clearRoleUsers(String role) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE role = ?");
            ps.setString(1, role);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear " + role + " records.", ex);
        }
    }

    private void refreshDepartmentAssignments() throws Exception {
        Connection con = DBUtil.getConnection();
        PreparedStatement resetPs = con.prepareStatement("UPDATE users SET department_id = NULL WHERE role IN ('HOD', 'CLERK')");
        resetPs.executeUpdate();
        resetPs.close();
        PreparedStatement hodPs = con.prepareStatement(
                "UPDATE users u JOIN departments d ON d.hod_user_id = u.id SET u.department_id = d.id WHERE u.role = 'HOD'");
        hodPs.executeUpdate();
        hodPs.close();
        PreparedStatement clerkPs = con.prepareStatement(
                "UPDATE users u JOIN departments d ON d.clerk_user_id = u.id SET u.department_id = d.id WHERE u.role = 'CLERK'");
        clerkPs.executeUpdate();
        clerkPs.close();
        con.close();
    }

    private void validateUniqueEmail(String email, Integer excludeUserId) throws Exception {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps;
        if (excludeUserId == null) {
            ps = con.prepareStatement("SELECT id FROM users WHERE email = ?");
            ps.setString(1, email);
        } else {
            ps = con.prepareStatement("SELECT id FROM users WHERE email = ? AND id <> ?");
            ps.setString(1, email);
            ps.setInt(2, excludeUserId.intValue());
        }
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();
        con.close();
        if (exists) {
            throw new RuntimeException("This email is already used. Please enter a different email.");
        }
    }
}
