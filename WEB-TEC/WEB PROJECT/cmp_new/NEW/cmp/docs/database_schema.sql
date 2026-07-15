CREATE DATABASE IF NOT EXISTS cmp_db;
USE cmp_db;

CREATE TABLE IF NOT EXISTS departments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL UNIQUE,
    description VARCHAR(255),
    hod_user_id INT NULL,
    clerk_user_id INT NULL
);

CREATE TABLE IF NOT EXISTS classes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    department_id INT NOT NULL,
    class_name VARCHAR(120) NOT NULL,
    expected_strength INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    department_id INT NULL,
    class_id INT NULL,
    must_change_password TINYINT(1) NOT NULL DEFAULT 1,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS complaints (
    id INT PRIMARY KEY AUTO_INCREMENT,
    raised_by INT NOT NULL,
    department_id INT NOT NULL,
    class_id INT NULL,
    assigned_clerk_id INT NULL,
    complaint_scope VARCHAR(20) NOT NULL,
    title VARCHAR(160) NOT NULL,
    description TEXT NOT NULL,
    attachment_name VARCHAR(255),
    attachment_path VARCHAR(255),
    attachment_type VARCHAR(120),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    clerk_remarks VARCHAR(255),
    clerk_seen TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    complaint_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE departments
    ADD CONSTRAINT fk_departments_hod FOREIGN KEY (hod_user_id) REFERENCES users(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_departments_clerk FOREIGN KEY (clerk_user_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE classes
    ADD CONSTRAINT fk_classes_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE;

ALTER TABLE users
    ADD CONSTRAINT fk_users_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_users_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE SET NULL;

ALTER TABLE complaints
    ADD CONSTRAINT fk_complaints_user FOREIGN KEY (raised_by) REFERENCES users(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_complaints_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_complaints_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_complaints_clerk FOREIGN KEY (assigned_clerk_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE notifications
    ADD CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_notifications_complaint FOREIGN KEY (complaint_id) REFERENCES complaints(id) ON DELETE CASCADE;

INSERT INTO users (full_name, email, password_hash, role, must_change_password, is_active)
SELECT 'System Admin', 'admin@cmp.local', 'e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7', 'ADMIN', 1, 1
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@cmp.local');

ALTER TABLE complaints ADD COLUMN IF NOT EXISTS attachment_name VARCHAR(255) NULL;
ALTER TABLE complaints ADD COLUMN IF NOT EXISTS attachment_path VARCHAR(255) NULL;
ALTER TABLE complaints ADD COLUMN IF NOT EXISTS attachment_type VARCHAR(120) NULL;
