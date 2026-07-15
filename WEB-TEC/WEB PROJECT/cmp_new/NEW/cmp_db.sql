-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 31, 2026 at 06:28 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cmp_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `classes`
--

CREATE TABLE `classes` (
  `id` int(11) NOT NULL,
  `department_id` int(11) NOT NULL,
  `class_name` varchar(120) NOT NULL,
  `expected_strength` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `complaints`
--

CREATE TABLE `complaints` (
  `id` int(11) NOT NULL,
  `raised_by` int(11) NOT NULL,
  `department_id` int(11) NOT NULL,
  `class_id` int(11) DEFAULT NULL,
  `assigned_clerk_id` int(11) DEFAULT NULL,
  `complaint_scope` varchar(20) NOT NULL,
  `title` varchar(160) NOT NULL,
  `description` text NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'PENDING',
  `clerk_remarks` varchar(255) DEFAULT NULL,
  `clerk_seen` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `departments`
--

CREATE TABLE `departments` (
  `id` int(11) NOT NULL,
  `name` varchar(120) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `hod_user_id` int(11) DEFAULT NULL,
  `clerk_user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `complaint_id` int(11) NOT NULL,
  `message` varchar(255) NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(120) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL,
  `department_id` int(11) DEFAULT NULL,
  `class_id` int(11) DEFAULT NULL,
  `must_change_password` tinyint(1) NOT NULL DEFAULT 1,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `email`, `password_hash`, `role`, `department_id`, `class_id`, `must_change_password`, `is_active`, `created_at`) VALUES
(1, 'System Admin', 'maruviraj11@gmail.com', 'Vir@j8347218105', 'ADMIN', NULL, NULL, 1, 1, '2026-03-29 03:59:35'),
(3, 'viraj maru', 'maruviraj12@gmail.com', 'viraj@111', 'Admin', NULL, NULL, 0, 1, '2026-03-29 04:14:45'),
(4, 'System Admin', 'admin@cmp.local', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'ADMIN', NULL, NULL, 0, 1, '2026-03-29 04:20:55'),
(24, 'Mr.Chintan Sarvaiya', 'chintan11@gmail.com', '2ca878be87fe59f95349e0beb9c12e54b8bb03ab7495289ccdee5b3a72a0cf44', 'STAFF', NULL, NULL, 1, 1, '2026-03-29 05:58:04'),
(25, 'VIRAJ MARU', 'maruviraj22@gmail.com', '6e4c97e2146f302eef281ff3c2e542b4f6d50eb2d8d9c91ab75581aec14738a0', 'CR', NULL, NULL, 1, 1, '2026-03-29 05:58:21'),
(26, 'Dr.Alkesh Bakotiya', 'absoftware2828@gmail.com', 'f4d151b72f9785e91f9900e4cd1ad549e0353ab65689bf1abb7c3710d2af8ff1', 'CR', NULL, NULL, 1, 1, '2026-03-29 06:09:35'),
(27, 'alkesh', 'alkesh@gmail.com', 'b71c77be6a446f61ce79c594d5ac9cd7d00a12c562570d448a818279cf891ad2', 'STUDENT', NULL, NULL, 1, 1, '2026-03-29 06:10:53'),
(28, 'ANIL', 'anil@gmail.com', '31576ac4c0c4b2082ee9a35242f6fd02d301969c8ce55ce1b8659a641b957f72', 'STUDENT', NULL, NULL, 1, 1, '2026-03-29 06:12:23'),
(29, 'Mr.jitendra', 'jitendra12@gmail.com', '05b813800b7d965e4226a91428cc27806f6099c322f7d681658d659adaf38820', 'STAFF', NULL, NULL, 1, 1, '2026-03-29 07:57:16'),
(30, 'jignesh', 'jignesh@gmail.com', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'CR', NULL, NULL, 0, 1, '2026-03-29 08:04:03'),
(32, 'ab', 'ab@gmail.com', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'STUDENT', NULL, NULL, 0, 1, '2026-03-29 08:43:28'),
(33, 'jadu', 'jadu@gmail.com', 'c5be82b9762ef4e95cddfbd58b4db92ba8092b1db9b847cc9ce8ccbeaae50d03', 'STUDENT', NULL, NULL, 1, 1, '2026-03-29 10:26:51'),
(34, 'jadu', 'dib@gmail.com', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'STUDENT', NULL, NULL, 0, 1, '2026-03-29 10:27:09'),
(60, 'Viraj Maru', 'maruviraj00@gmail.com', '9ce03b231d2fb143c174b01b8a9a1eeffa7a7098e7a9f3b7ff6a1909c9d8a356', 'STUDENT', NULL, NULL, 1, 1, '2026-03-31 03:41:14'),
(61, 'Alkesh Bakotiya', 'alkeshbakotiya7@gmail.com', 'b57cbc9996dd647df3ef57b2908d05c22aa46562d8d9cc2c323a5a15b4469d98', 'STUDENT', NULL, NULL, 1, 1, '2026-03-31 03:41:59'),
(62, 'Viraj Agrawat', 'virajagravat@gmail.com', '1fdea5492593f1b9cec39cee369cb3af1912e1177272e63e267f7b2d150b5e91', 'STUDENT', NULL, NULL, 1, 1, '2026-03-31 03:42:25'),
(63, 'aryan kumbhani', 'aryankumbhani@gmail.com', '0a87acad935166eced65c0e5b261114eaa2ec72b0c8a30aa7c2cd1d03f78a507', 'STUDENT', NULL, NULL, 1, 1, '2026-03-31 03:44:08'),
(64, 'harsh makwana', 'harshmakwana@gmail.com', '374fdba0dd5ed0c52ac234722f40e2b87cd7213d6ab33003805d8e1425f149ba', 'STUDENT', NULL, NULL, 1, 1, '2026-03-31 03:44:11'),
(65, 'mayur vaja', 'mayurvaja@gmail.com', '5b072407f38bbe332b4512471f0b05f174edd080a60f0e04ad6cb1479de12dcb', 'STUDENT', NULL, NULL, 1, 1, '2026-03-31 03:44:12'),
(66, 'nikunj mevada', 'niikunjmevada@gmail.com', 'df117d558a877a39a84a87c42633ed5f0dad25da8e2094134df2a07abba0873b', 'STUDENT', NULL, NULL, 1, 1, '2026-03-31 03:44:13');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `classes`
--
ALTER TABLE `classes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_classes_department` (`department_id`);

--
-- Indexes for table `complaints`
--
ALTER TABLE `complaints`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_complaints_user` (`raised_by`),
  ADD KEY `fk_complaints_department` (`department_id`),
  ADD KEY `fk_complaints_class` (`class_id`),
  ADD KEY `fk_complaints_clerk` (`assigned_clerk_id`);

--
-- Indexes for table `departments`
--
ALTER TABLE `departments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `fk_departments_hod` (`hod_user_id`),
  ADD KEY `fk_departments_clerk` (`clerk_user_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_notifications_user` (`user_id`),
  ADD KEY `fk_notifications_complaint` (`complaint_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_users_department` (`department_id`),
  ADD KEY `fk_users_class` (`class_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `classes`
--
ALTER TABLE `classes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `complaints`
--
ALTER TABLE `complaints`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT for table `departments`
--
ALTER TABLE `departments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `classes`
--
ALTER TABLE `classes`
  ADD CONSTRAINT `fk_classes_department` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `complaints`
--
ALTER TABLE `complaints`
  ADD CONSTRAINT `fk_complaints_class` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_complaints_clerk` FOREIGN KEY (`assigned_clerk_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_complaints_department` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_complaints_user` FOREIGN KEY (`raised_by`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `departments`
--
ALTER TABLE `departments`
  ADD CONSTRAINT `fk_departments_clerk` FOREIGN KEY (`clerk_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_departments_hod` FOREIGN KEY (`hod_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `fk_notifications_complaint` FOREIGN KEY (`complaint_id`) REFERENCES `complaints` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_notifications_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `fk_users_class` FOREIGN KEY (`class_id`) REFERENCES `classes` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_users_department` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
