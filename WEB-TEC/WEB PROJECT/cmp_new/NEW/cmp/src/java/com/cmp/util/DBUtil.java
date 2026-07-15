package com.cmp.util;

import com.cmp.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBUtil {

    static {
        try {
            Class.forName(AppConfig.DB_DRIVER);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("MySQL JDBC Driver not found.", ex);
        }
    }

    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(AppConfig.DB_URL, AppConfig.DB_USER, AppConfig.DB_PASSWORD);
        } catch (SQLException ex) {
            throw new SQLException("Database connection failed for user '" + AppConfig.DB_USER
                    + "' on URL " + AppConfig.DB_URL + ". Check MySQL username/password and database name.", ex);
        }
    }
}
