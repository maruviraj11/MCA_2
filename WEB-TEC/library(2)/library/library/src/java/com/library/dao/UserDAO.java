package com.library.dao;

import com.library.model.User;
import com.library.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User getUserById(int id) {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updatePassword(int id, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newPassword);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByEmailAndPassword(String email, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean addUser(User user) {
        String query = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteLibrarian(int id) {
        String query = "DELETE FROM users WHERE id = ? AND role = 'librarian'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
             ps.setInt(1, id);
             return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getLibrarianEmailById(int id) {
        String query = "SELECT email FROM users WHERE id = ? AND role = 'librarian'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateLibrarian(int id, String email, String password) {
        boolean hasEmail = email != null && !email.trim().isEmpty();
        boolean hasPassword = password != null && !password.trim().isEmpty();
        if (!hasEmail && !hasPassword) return false;

        String query;
        if (hasEmail && hasPassword) {
            query = "UPDATE users SET email = ?, password = ? WHERE id = ? AND role = 'librarian'";
        } else if (hasEmail) {
            query = "UPDATE users SET email = ? WHERE id = ? AND role = 'librarian'";
        } else {
            query = "UPDATE users SET password = ? WHERE id = ? AND role = 'librarian'";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            int idx = 1;
            if (hasEmail) ps.setString(idx++, email.trim());
            if (hasPassword) ps.setString(idx++, password);
            ps.setInt(idx, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ? AND role = 'user'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserEmailById(int id) {
        String query = "SELECT email FROM users WHERE id = ? AND role = 'user'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(int id, String email, String password) {
        boolean hasEmail = email != null && !email.trim().isEmpty();
        boolean hasPassword = password != null && !password.trim().isEmpty();
        if (!hasEmail && !hasPassword) return false;

        String query;
        if (hasEmail && hasPassword) {
            query = "UPDATE users SET email = ?, password = ? WHERE id = ? AND role = 'user'";
        } else if (hasEmail) {
            query = "UPDATE users SET email = ? WHERE id = ? AND role = 'user'";
        } else {
            query = "UPDATE users SET password = ? WHERE id = ? AND role = 'user'";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            int idx = 1;
            if (hasEmail) ps.setString(idx++, email.trim());
            if (hasPassword) ps.setString(idx++, password);
            ps.setInt(idx, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllLibrarians() {
        List<User> librarians = new ArrayList<>();
        String query = "SELECT id, email FROM users WHERE role = 'librarian'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
             
             while (rs.next()) {
                 User user = new User();
                 user.setId(rs.getInt("id"));
                 user.setEmail(rs.getString("email"));
                 librarians.add(user);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return librarians;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, email FROM users WHERE role = 'user' ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean checkEmailExists(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
             ps.setString(1, email);
             try (ResultSet rs = ps.executeQuery()) {
                 return rs.next();
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
