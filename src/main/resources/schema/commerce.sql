-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 22, 2022 at 12:30 AM
-- Server version: 10.4.22-MariaDB


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `commerce`
--
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `commerce` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `commerce`;

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
CREATE TABLE IF NOT EXISTS `accounts`
(
    `staff_id`    int(11)      NOT NULL,
    `role_id`     int(11)      NOT NULL,
    `email`       varchar(255) NOT NULL,
    `password`    varchar(255) NOT NULL,
    `create_date` datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update` timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`staff_id`, `role_id`),
    UNIQUE KEY `UC_Account_Email` (`email`),
    KEY `FK_Account_Role` (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
CREATE TABLE IF NOT EXISTS `brands`
(
    `brand_id`      int(11)      NOT NULL AUTO_INCREMENT,
    `brand_name`    varchar(100) NOT NULL,
    `brand_picture` varchar(100) NOT NULL,
    `create_date`   datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update`   timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`brand_id`),
    UNIQUE KEY `UC_Brand_Name` (`brand_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories`
(
    `category_id`   int(11)      NOT NULL AUTO_INCREMENT,
    `category_name` varchar(100) NOT NULL,
    `create_date`   datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update`   timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`category_id`),
    UNIQUE KEY `UC_Category_Name` (`category_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE IF NOT EXISTS `orders`
(
    `order_id`    int(11)      NOT NULL AUTO_INCREMENT,
    `staff_id`    int(11)      NOT NULL,
    `store_id`    int(11)      NOT NULL,
    `order_name`  varchar(255) NOT NULL,
    `create_date` datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update` timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    UNIQUE KEY `UC_Order_Name` (`order_name`),
    PRIMARY KEY (`order_id`),
    KEY `FK_Order_Store` (`store_id`),
    KEY `FK_Order_Staff` (`staff_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE IF NOT EXISTS `order_item`
(
    `order_item_id` int(11)   NOT NULL AUTO_INCREMENT,
    `order_id`      int(11)   NOT NULL,
    `product_id`    int(11)   NOT NULL,
    `quantity`      int(11)   NOT NULL CHECK (`quantity` >= 0),
    `price`         int(11)   NOT NULL CHECK (`price` >= 0),
    `create_date`   datetime  NOT NULL DEFAULT current_timestamp(),
    `last_update`   timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`order_item_id`, `order_id`, `product_id`),
    KEY `FK_Order_Item_Order` (`order_id`),
    KEY `FK_Order_Item_Product` (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products`
(
    `product_id`      int(11)      NOT NULL AUTO_INCREMENT,
    `product_qr_code` varchar(100) NOT NULL,
    `product_name`    varchar(100) NOT NULL,
    `product_picture` varchar(100) NOT NULL,
    `product_price`   int(11)      NOT NULL CHECK (`product_price` >= 0),
    `brand_id`        int(11)      NOT NULL,
    `category_id`     int(11)      NOT NULL,
    `create_date`     datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update`     timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`product_id`),
    UNIQUE KEY `UC_Product_Name` (`product_name`),
    UNIQUE KEY `UC_Product_QrCode` (`product_qr_code`),
    KEY `FK_Product_Brand` (`brand_id`) USING BTREE,
    KEY `FK_Product_Category` (`category_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `product_supplier`
--

DROP TABLE IF EXISTS `product_supplier`;
CREATE TABLE IF NOT EXISTS `product_supplier`
(
    `product_id`  int(11)   NOT NULL,
    `supplier_id` int(11)   NOT NULL,
    `create_date` datetime  NOT NULL DEFAULT current_timestamp(),
    `last_update` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`product_id`, `supplier_id`),
    KEY `FK_Product_Supplier_Product` (`product_id`),
    KEY `FK_Product_Supplier_Supplier` (`supplier_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles`
(
    `role_id`     int(11)      NOT NULL AUTO_INCREMENT,
    `role_name`   varchar(100) NOT NULL,
    `create_date` datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update` timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`role_id`),
    UNIQUE KEY `UC_Role_Name` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `staffs`
--

DROP TABLE IF EXISTS `staffs`;
CREATE TABLE IF NOT EXISTS `staffs`
(
    `staff_id`      int(11)      NOT NULL AUTO_INCREMENT,
    `first_name`    varchar(100) NOT NULL,
    `last_name`     varchar(100) NOT NULL,
    `date_of_birth` date         NOT NULL,
    `phone`         varchar(100) NOT NULL,
    `hire_date`     date         NOT NULL,
    `store_id`      int(11)      NOT NULL,
    `create_date`   datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update`   timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`staff_id`),
    UNIQUE KEY `UC_Staff_Phone` (`phone`),
    KEY `FK_Staff_Store` (`store_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `stocks`
--

DROP TABLE IF EXISTS `stocks`;
CREATE TABLE IF NOT EXISTS `stocks`
(
    `product_id`  int(11)   NOT NULL,
    `store_id`    int(11)   NOT NULL,
    `quantity`    int(11)   NOT NULL CHECK (`quantity` >= 0),
    `create_date` datetime  NOT NULL DEFAULT current_timestamp(),
    `last_update` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`product_id`, `store_id`),
    KEY `FK_Stock_Product` (`product_id`),
    KEY `FK_Stock_Store` (`store_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `stores`
--

DROP TABLE IF EXISTS `stores`;
CREATE TABLE IF NOT EXISTS `stores`
(
    `store_id`    int(11)      NOT NULL AUTO_INCREMENT,
    `store_name`  varchar(100) NOT NULL,
    `phone`       varchar(100) NOT NULL,
    `email`       varchar(255) NOT NULL,
    `address`     varchar(100) NOT NULL,
    `city`        varchar(100) NOT NULL,
    `state`       varchar(100) NOT NULL,
    `zip_code`    varchar(100) NOT NULL,
    `create_date` datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update` timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`store_id`),
    UNIQUE KEY `UC_Store_Name` (`store_name`),
    UNIQUE KEY `UC_Store_Email` (`email`),
    UNIQUE KEY `UC_Store_Phone` (`phone`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE IF NOT EXISTS `suppliers`
(
    `supplier_id`  int(11)      NOT NULL AUTO_INCREMENT,
    `company_name` varchar(100) NOT NULL,
    `phone`        varchar(100)          DEFAULT NULL,
    `fax`          varchar(100)          DEFAULT NULL,
    `address`      varchar(100) NOT NULL,
    `city`         varchar(100) NOT NULL,
    `state`        varchar(100) NOT NULL,
    `zip_code`     varchar(100) NOT NULL,
    `create_date`  datetime     NOT NULL DEFAULT current_timestamp(),
    `last_update`  timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    PRIMARY KEY (`supplier_id`),
    UNIQUE KEY `UC_Supplier_Name` (`company_name`),
    UNIQUE KEY `UC_Supplier_Phone` (`phone`),
    UNIQUE KEY `UC_Supplier_Fax` (`fax`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
    ADD CONSTRAINT `FK_Account_Role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT `FK_Account_staff` FOREIGN KEY (`staff_id`) REFERENCES `staffs` (`staff_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
    ADD CONSTRAINT `FK_Order_Staff` FOREIGN KEY (`staff_id`) REFERENCES `staffs` (`staff_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT `FK_Order_Store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `order_item`
--
ALTER TABLE `order_item`
    ADD CONSTRAINT `FK_Order_Item_Order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT `FK_Order_Item_Product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `products`
--
ALTER TABLE `products`
    ADD CONSTRAINT `FK_ProductBrand` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT `FK_ProductCategory` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `product_supplier`
--
ALTER TABLE `product_supplier`
    ADD CONSTRAINT `FK_Product_Supplier_Product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT `FK_Product_Supplier_Supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `staffs`
--
ALTER TABLE `staffs`
    ADD CONSTRAINT `FK_Staff_Store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Constraints for table `stocks`
--
ALTER TABLE `stocks`
    ADD CONSTRAINT `FK_Stock_Product` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT `FK_Stock_Store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;


SET FOREIGN_KEY_CHECKS = 1;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;