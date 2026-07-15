package com.cmp.dao;

import com.cmp.transport.MailTransport;
import com.cmp.util.DBUtil;
import com.cmp.util.PasswordUtil;
import com.cmp.util.ValidationUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HodDao {

    public List<Map> getClasses(int departmentId) {
        List<Map> list = new ArrayList<Map>();
        String sql = "SELECT c.id, c.class_name, c.expected_strength, "
                + "(SELECT COUNT(*) FROM users u WHERE u.class_id = c.id AND u.role IN ('STUDENT', 'CR')) AS student_count "
                + "FROM classes c WHERE c.department_id = ? ORDER BY c.class_name";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("id", rs.getInt("id"));
                row.put("className", rs.getString("class_name"));
                row.put("expectedStrength", rs.getInt("expected_strength"));
                row.put("studentCount", rs.getInt("student_count"));
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load classes.", ex);
        }
        return list;
    }

    public List<Map> getStudents(int departmentId) {
        return getUsersByDepartmentRoles(departmentId, "('STUDENT', 'CR')");
    }

    public List<Map> getStaff(int departmentId) {
        return getUsersByDepartmentRoles(departmentId, "('STAFF')");
    }

    public int getActiveComplaintCount(int departmentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM complaints WHERE department_id = ? AND status IN ('PENDING', 'ASSIGNED')");
            ps.setInt(1, departmentId);
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
            throw new RuntimeException("Unable to load active complaint count.", ex);
        }
    }

    public Map getComplaintStatusCounts(int departmentId) {
        Map counts = new HashMap();
        counts.put("PENDING", Integer.valueOf(0));
        counts.put("ASSIGNED", Integer.valueOf(0));
        counts.put("REJECTED", Integer.valueOf(0));
        counts.put("SOLVED", Integer.valueOf(0));
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT status, COUNT(*) AS total FROM complaints WHERE department_id = ? GROUP BY status");
            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                counts.put(rs.getString("status"), Integer.valueOf(rs.getInt("total")));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load complaint status counts.", ex);
        }
        return counts;
    }

    public List<Map> getStudentsByClass(int departmentId, int classId) {
        List<Map> list = new ArrayList<Map>();
        String sql = "SELECT u.id, u.full_name, u.email, u.role, c.class_name "
                + "FROM users u "
                + "JOIN classes c ON c.id = u.class_id "
                + "WHERE u.department_id = ? AND u.class_id = ? AND u.role IN ('STUDENT', 'CR') "
                + "ORDER BY u.full_name";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, departmentId);
            ps.setInt(2, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("id", rs.getInt("id"));
                row.put("fullName", rs.getString("full_name"));
                row.put("email", rs.getString("email"));
                row.put("role", rs.getString("role"));
                row.put("className", rs.getString("class_name"));
                list.add(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to load class students.", ex);
        }
        return list;
    }

    public List<Map> getRecentHodActivities(int departmentId) {
        List<Map> list = new ArrayList<Map>();
        try {
            Connection con = DBUtil.getConnection();

            PreparedStatement classPs = con.prepareStatement(
                    "SELECT class_name, expected_strength, id FROM classes WHERE department_id = ? ORDER BY id DESC LIMIT 3");
            classPs.setInt(1, departmentId);
            ResultSet classRs = classPs.executeQuery();
            while (classRs.next()) {
                Map row = new HashMap();
                row.put("title", "Class available: " + classRs.getString("class_name"));
                row.put("detail", "Expected students: " + classRs.getInt("expected_strength"));
                row.put("createdAt", "Class ID " + classRs.getInt("id"));
                list.add(row);
            }
            classRs.close();
            classPs.close();

            PreparedStatement userPs = con.prepareStatement(
                    "SELECT full_name, role, created_at FROM users WHERE department_id = ? AND role IN ('STUDENT', 'CR', 'STAFF') ORDER BY created_at DESC LIMIT 3");
            userPs.setInt(1, departmentId);
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
            throw new RuntimeException("Unable to load HOD activities.", ex);
        }
        return list;
    }

    private List<Map> getUsersByDepartmentRoles(int departmentId, String roleSql) {
        List<Map> list = new ArrayList<Map>();
        String sql = "SELECT u.id, u.full_name, u.email, u.role, c.class_name FROM users u "
                + "LEFT JOIN classes c ON c.id = u.class_id WHERE u.department_id = ? AND u.role IN " + roleSql
                + " ORDER BY u.full_name";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, departmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map row = new HashMap();
                row.put("id", rs.getInt("id"));
                row.put("fullName", rs.getString("full_name"));
                row.put("email", rs.getString("email"));
                row.put("role", rs.getString("role"));
                row.put("className", rs.getString("class_name"));
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

    public void saveClass(int departmentId, String className, int expectedStrength) {
        try {
            className = className == null ? "" : className.trim();
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO classes(department_id, class_name, expected_strength) VALUES(?, ?, ?)");
            ps.setInt(1, departmentId);
            ps.setString(2, className);
            ps.setInt(3, expectedStrength);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to save class.", ex);
        }
    }

    public String saveStudent(String fullName, String email, int departmentId, int classId) {
        return saveDepartmentUser(fullName, email, "STUDENT", departmentId, classId);
    }

    public List saveStudentsBulk(List studentRows, int departmentId, int classId) {
        List createdUsers = new ArrayList();
        validateBulkStudentCapacity(studentRows.size(), classId, departmentId);
        for (int i = 0; i < studentRows.size(); i++) {
            Map row = (Map) studentRows.get(i);
            String password = saveStudent(String.valueOf(row.get("fullName")),
                    String.valueOf(row.get("email")), departmentId, classId);
            Map created = new HashMap();
            created.put("fullName", row.get("fullName"));
            created.put("email", row.get("email"));
            created.put("password", password);
            createdUsers.add(created);
        }
        return createdUsers;
    }

    public String saveStaff(String fullName, String email, int departmentId) {
        return saveDepartmentUser(fullName, email, "STAFF", departmentId, null);
    }

    private String saveDepartmentUser(String fullName, String email, String role, int departmentId, Integer classId) {
        String plainPassword = PasswordUtil.generatePassword();
        try {
            fullName = fullName == null ? "" : fullName.trim();
            email = ValidationUtil.normalizeEmailOrThrow(email);
            validateUniqueEmail(email, null);
            if (classId != null) {
                validateClassCapacity(classId.intValue(), departmentId, null);
            }
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(full_name, email, password_hash, role, department_id, class_id, must_change_password, is_active) VALUES(?, ?, ?, ?, ?, ?, 1, 1)");
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, PasswordUtil.hash(plainPassword));
            ps.setString(4, role);
            ps.setInt(5, departmentId);
            if (classId == null) {
                ps.setNull(6, java.sql.Types.INTEGER);
            } else {
                ps.setInt(6, classId.intValue());
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
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public void updateClass(int classId, int departmentId, String className, int expectedStrength) {
        try {
            className = className == null ? "" : className.trim();
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE classes SET class_name = ?, expected_strength = ? WHERE id = ? AND department_id = ?");
            ps.setString(1, className);
            ps.setInt(2, expectedStrength);
            ps.setInt(3, classId);
            ps.setInt(4, departmentId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to update class.", ex);
        }
    }

    public void deleteClass(int classId, int departmentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement clearPs = con.prepareStatement(
                    "UPDATE users SET class_id = NULL, role = CASE WHEN role = 'CR' THEN 'STUDENT' ELSE role END "
                    + "WHERE class_id = ? AND department_id = ?");
            clearPs.setInt(1, classId);
            clearPs.setInt(2, departmentId);
            clearPs.executeUpdate();
            clearPs.close();

            PreparedStatement deletePs = con.prepareStatement(
                    "DELETE FROM classes WHERE id = ? AND department_id = ?");
            deletePs.setInt(1, classId);
            deletePs.setInt(2, departmentId);
            deletePs.executeUpdate();
            deletePs.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to delete class.", ex);
        }
    }

    public void updateStudent(int userId, int departmentId, String fullName, String email, int classId) {
        validateStudentClassLocked(userId, classId);
        updateDepartmentUser(userId, departmentId, fullName, email, classId, true, "student");
    }

    public void updateStaff(int userId, int departmentId, String fullName, String email) {
        updateDepartmentUser(userId, departmentId, fullName, email, null, false, "staff");
    }

    public void deleteDepartmentUser(int userId, int departmentId, String allowedRoleGroup) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps;
            if ("STUDENT_GROUP".equals(allowedRoleGroup)) {
                ps = con.prepareStatement("DELETE FROM users WHERE id = ? AND department_id = ? AND role IN ('STUDENT', 'CR')");
            } else {
                ps = con.prepareStatement("DELETE FROM users WHERE id = ? AND department_id = ? AND role = 'STAFF'");
            }
            ps.setInt(1, userId);
            ps.setInt(2, departmentId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to delete user.", ex);
        }
    }

    public void assignCr(int studentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement currentClassPs = con.prepareStatement("SELECT class_id, role FROM users WHERE id = ?");
            currentClassPs.setInt(1, studentId);
            ResultSet rs = currentClassPs.executeQuery();
            int classId = 0;
            String currentRole = "";
            if (rs.next()) {
                classId = rs.getInt(1);
                currentRole = rs.getString(2);
            }
            rs.close();
            currentClassPs.close();
            if ("CR".equals(currentRole)) {
                con.close();
                return;
            }
            PreparedStatement countPs = con.prepareStatement("SELECT COUNT(*) FROM users WHERE class_id = ? AND role = 'CR'");
            countPs.setInt(1, classId);
            ResultSet countRs = countPs.executeQuery();
            int crCount = 0;
            if (countRs.next()) {
                crCount = countRs.getInt(1);
            }
            countRs.close();
            countPs.close();
            if (crCount >= 2) {
                con.close();
                throw new RuntimeException("Only 2 CR can be assigned in one class.");
            }
            PreparedStatement promotePs = con.prepareStatement("UPDATE users SET role = 'CR' WHERE id = ?");
            promotePs.setInt(1, studentId);
            promotePs.executeUpdate();
            promotePs.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to assign CR.", ex);
        }
    }

    public void removeCr(int studentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET role = 'STUDENT' WHERE id = ? AND role = 'CR'");
            ps.setInt(1, studentId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to remove CR.", ex);
        }
    }

    public void clearAllClasses(int departmentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM classes WHERE department_id = ?");
            ps.setInt(1, departmentId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear classes.", ex);
        }
    }

    public void clearAllStudents(int departmentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM users WHERE department_id = ? AND role IN ('STUDENT', 'CR')");
            ps.setInt(1, departmentId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear students.", ex);
        }
    }

    public void clearAllStaff(int departmentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM users WHERE department_id = ? AND role = 'STAFF'");
            ps.setInt(1, departmentId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to clear staff members.", ex);
        }
    }

    private void updateDepartmentUser(int userId, int departmentId, String fullName, String email, Integer classId,
            boolean includeClass, String label) {
        try {
            fullName = fullName == null ? "" : fullName.trim();
            email = ValidationUtil.normalizeEmailOrThrow(email);
            validateUniqueEmail(email, Integer.valueOf(userId));
            if (includeClass) {
                validateClassCapacity(classId.intValue(), departmentId, Integer.valueOf(userId));
            }
            Connection con = DBUtil.getConnection();
            PreparedStatement ps;
            if (includeClass) {
                ps = con.prepareStatement(
                        "UPDATE users SET full_name = ?, email = ?, class_id = ? WHERE id = ? AND department_id = ? AND role IN ('STUDENT', 'CR')");
                ps.setString(1, fullName);
                ps.setString(2, email);
                ps.setInt(3, classId.intValue());
                ps.setInt(4, userId);
                ps.setInt(5, departmentId);
            } else {
                ps = con.prepareStatement(
                        "UPDATE users SET full_name = ?, email = ? WHERE id = ? AND department_id = ? AND role = 'STAFF'");
                ps.setString(1, fullName);
                ps.setString(2, email);
                ps.setInt(3, userId);
                ps.setInt(4, departmentId);
            }
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to update " + label + ".", ex);
        }
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

    private void validateClassCapacity(int classId, int departmentId, Integer excludeUserId) throws Exception {
        Connection con = DBUtil.getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT expected_strength, "
                + "(SELECT COUNT(*) FROM users WHERE class_id = c.id AND role IN ('STUDENT', 'CR')"
                + (excludeUserId == null ? "" : " AND id <> ?")
                + ") AS current_count "
                + "FROM classes c WHERE c.id = ? AND c.department_id = ?");
        int index = 1;
        if (excludeUserId != null) {
            ps.setInt(index++, excludeUserId.intValue());
        }
        ps.setInt(index++, classId);
        ps.setInt(index, departmentId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            rs.close();
            ps.close();
            con.close();
            throw new RuntimeException("Selected class is invalid for this department.");
        }
        int expected = rs.getInt("expected_strength");
        int current = rs.getInt("current_count");
        rs.close();
        ps.close();
        con.close();
        if (current >= expected) {
            throw new RuntimeException("This class is full. You cannot add more students to it.");
        }
    }

    private void validateStudentClassLocked(int userId, int newClassId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT class_id FROM users WHERE id = ? AND role IN ('STUDENT', 'CR')");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int currentClassId = rs.getInt(1);
                if (currentClassId > 0 && currentClassId != newClassId) {
                    rs.close();
                    ps.close();
                    con.close();
                    throw new RuntimeException("Student is already assigned to another class and cannot be moved.");
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to validate student class assignment.", ex);
        }
    }

    private void validateBulkStudentCapacity(int incomingCount, int classId, int departmentId) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT expected_strength, "
                    + "(SELECT COUNT(*) FROM users WHERE class_id = c.id AND role IN ('STUDENT', 'CR')) AS current_count "
                    + "FROM classes c WHERE c.id = ? AND c.department_id = ?");
            ps.setInt(1, classId);
            ps.setInt(2, departmentId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                con.close();
                throw new RuntimeException("Selected class is invalid for this department.");
            }
            int expected = rs.getInt("expected_strength");
            int current = rs.getInt("current_count");
            rs.close();
            ps.close();
            con.close();
            if (current + incomingCount > expected) {
                throw new RuntimeException("Only " + (expected - current) + " seats are available in this class.");
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to validate bulk student capacity.", ex);
        }
    }
}
