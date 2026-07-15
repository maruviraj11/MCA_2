drop database if exists library;
CREATE DATABASE IF NOT EXISTS library;
USE library;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'librarian', 'user') NOT NULL
);

CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    available INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    issue_date DATETIME NULL,
    due_date DATE NULL,
    return_date DATETIME NULL,
    fine_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    fine_paid TINYINT(1) NOT NULL DEFAULT 0,
    fine_paid_date DATETIME NULL,
    status ENUM('pending', 'issued', 'returned', 'rejected') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

CREATE INDEX idx_requests_user_status ON requests(user_id, status);
CREATE INDEX idx_requests_status_due ON requests(status, due_date);

CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    user_id INT NOT NULL,
    provider ENUM('razorpay') NOT NULL DEFAULT 'razorpay',
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    amount_paise INT NOT NULL,
    order_id VARCHAR(64) NOT NULL UNIQUE,
    payment_id VARCHAR(64) NULL,
    signature VARCHAR(128) NULL,
    status ENUM('created','paid','failed') NOT NULL DEFAULT 'created',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at DATETIME NULL,
    FOREIGN KEY (request_id) REFERENCES requests(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_payments_request ON payments(request_id);
CREATE INDEX idx_payments_user ON payments(user_id);

-- Insert a default admin user (password: admin123)
-- WARNING: Use simple passwords only for a demonstrative simple project!
INSERT IGNORE INTO users (email, password, role) VALUES ('admin@library.com', 'admin123', 'admin');

-- Optional demo accounts
INSERT IGNORE INTO users (email, password, role) VALUES ('librarian@library.com', 'librarian123', 'librarian');
INSERT IGNORE INTO users (email, password, role) VALUES ('user@library.com', 'user12345', 'user');

-- Optional demo books
INSERT IGNORE INTO books (title, author, category, quantity, available) VALUES
('Clean Code', 'Robert C. Martin', 'Programming', 5, 5),
('Head First Java', 'Kathy Sierra', 'Programming', 4, 4),
('The Alchemist', 'Paulo Coelho', 'Fiction', 3, 3),
('Database System Concepts', 'Abraham Silberschatz', 'Database', 2, 2);
