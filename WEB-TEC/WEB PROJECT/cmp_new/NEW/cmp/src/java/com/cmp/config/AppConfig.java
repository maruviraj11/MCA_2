package com.cmp.config;

public final class AppConfig {

    public static final String DB_URL = "jdbc:mysql://localhost:3306/cmp_db?useSSL=false&serverTimezone=Asia/Kolkata";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    public static final String DEFAULT_ADMIN_NAME = "System Admin";
    public static final String DEFAULT_ADMIN_EMAIL = "admin@cmp.local";
    public static final String DEFAULT_ADMIN_PASSWORD = "Admin@123";

    // SMTP mail sending:
    // 1. Set MAIL_ENABLED to true
    // 2. Put your Gmail address in MAIL_FROM and SMTP_USERNAME
    // 3. Put Gmail App Password in SMTP_PASSWORD
    public static final boolean MAIL_ENABLED = true;
    public static final String MAIL_FROM = "maruviraj11@gmail.com";
    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final String SMTP_PORT = "587";
    public static final String SMTP_USERNAME = "maruviraj11@gmail.com";
    public static final String SMTP_PASSWORD = "ksaa ofoo omor mlgq";
    public static final boolean SMTP_TLS = true;
    // For old JRE truststore issues (PKIX), keep true in local/dev only.
    public static final boolean SMTP_TRUST_ALL_CERTS = true;
    public static final int COMPLAINT_EDIT_WINDOW_MINUTES = 30;

    private AppConfig() {
    }
}
