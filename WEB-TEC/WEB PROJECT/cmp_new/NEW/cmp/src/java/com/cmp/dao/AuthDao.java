package com.cmp.dao;

import com.cmp.util.DBUtil;
import com.cmp.util.PasswordUtil;
import com.cmp.util.ValidationUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class AuthDao {

    public Map authenticate(String email, String password) {
        if (!ValidationUtil.isValidEmail(email)) {
            return null;
        }
        email = email.trim().toLowerCase();
        if (password != null) {
            password = password.trim();
        }
        String sql = "SELECT u.id, u.full_name, u.email, u.role, u.department_id, u.class_id, "
                + "u.must_change_password, d.name AS department_name, c.class_name "
                + "FROM users u "
                + "LEFT JOIN departments d ON d.id = u.department_id "
                + "LEFT JOIN classes c ON c.id = u.class_id "
                + "WHERE u.email = ? AND u.is_active = 1";
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (PasswordUtil.matches(password, getPasswordHash(rs.getInt("id"), con))) {
                    Map user = new HashMap();
                    user.put("id", rs.getInt("id"));
                    user.put("fullName", rs.getString("full_name"));
                    user.put("email", rs.getString("email"));
                    user.put("role", rs.getString("role"));
                    user.put("departmentId", rs.getObject("department_id"));
                    user.put("departmentName", rs.getString("department_name"));
                    user.put("classId", rs.getObject("class_id"));
                    user.put("className", rs.getString("class_name"));
                    user.put("mustChangePassword", rs.getBoolean("must_change_password"));
                    rs.close();
                    ps.close();
                    con.close();
                    return user;
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Login failed.", ex);
        }
        return null;
    }

    private String getPasswordHash(int userId, Connection con) throws Exception {
        PreparedStatement ps = con.prepareStatement("SELECT password_hash FROM users WHERE id = ?");
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        String value = "";
        if (rs.next()) {
            value = rs.getString(1);
        }
        rs.close();
        ps.close();
        return value;
    }

    public void changePassword(int userId, String newPassword) {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET password_hash = ?, must_change_password = 0 WHERE id = ?");
            ps.setString(1, PasswordUtil.hash(newPassword));
            ps.setInt(2, userId);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to change password.", ex);
        }
    }
}
