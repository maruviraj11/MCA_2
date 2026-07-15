package com.cmp.listener;

import com.cmp.config.AppConfig;
import com.cmp.util.DBUtil;
import com.cmp.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppBootstrapListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        ensureDefaultAdmin();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }

    private void ensureDefaultAdmin() {
        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement checkPs = con.prepareStatement("SELECT id FROM users WHERE role = 'ADMIN' LIMIT 1");
            ResultSet rs = checkPs.executeQuery();
            boolean exists = rs.next();
            rs.close();
            checkPs.close();
            if (!exists) {
                PreparedStatement insertPs = con.prepareStatement(
                        "INSERT INTO users(full_name, email, password_hash, role, must_change_password, is_active) VALUES(?, ?, ?, 'ADMIN', 1, 1)");
                insertPs.setString(1, AppConfig.DEFAULT_ADMIN_NAME);
                insertPs.setString(2, AppConfig.DEFAULT_ADMIN_EMAIL);
                insertPs.setString(3, PasswordUtil.hash(AppConfig.DEFAULT_ADMIN_PASSWORD));
                insertPs.executeUpdate();
                insertPs.close();
            }
            con.close();
        } catch (Exception ex) {
            System.out.println("Default admin seed skipped: " + ex.getMessage());
        }
    }
}
