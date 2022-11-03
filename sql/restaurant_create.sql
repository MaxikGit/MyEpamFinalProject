SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema restaurantDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `restaurantDB` DEFAULT CHARACTER SET utf8;
USE `restaurantDB`;

-- -----------------------------------------------------
-- Table `restaurantDB`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `restaurantDB`.`role`;

CREATE TABLE IF NOT EXISTS `restaurantDB`.`role`
(
    `id`   INT AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
);


-- -----------------------------------------------------
-- Table `restaurantDB`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `restaurantDB`.`user`;

CREATE TABLE IF NOT EXISTS `restaurantDB`.`user`
(
    `id`        INT AUTO_INCREMENT,
    `email`     VARCHAR(255) UNIQUE NOT NULL,
    `name`      VARCHAR(16)         NOT NULL,
    `last_name` VARCHAR(32)         NOT NULL,
    `password`  VARCHAR(32)         NOT NULL,
    `details`   VARCHAR(255) DEFAULT 'none',
    `role_id`   INT                 NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    INDEX `fk_user_role_idx` (`role_id` ASC) VISIBLE,
    CONSTRAINT `fk_user_role`
        FOREIGN KEY (`role_id`)
            REFERENCES `restaurantDB`.`role` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table `restaurantDB`.`status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `restaurantDB`.`status`;

CREATE TABLE IF NOT EXISTS `restaurantDB`.`status`
(
    `id`      INT AUTO_INCREMENT,
    `name`    VARCHAR(50) NOT NULL,
    `details` VARCHAR(45) DEFAULT 'none',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
);


-- -----------------------------------------------------
-- Table `restaurantDB`.`custom`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `restaurantDB`.`custom`;

CREATE TABLE IF NOT EXISTS `restaurantDB`.`custom`
(
    `id`          INT NOT NULL AUTO_INCREMENT,
    `cost`        DECIMAL(15, 2) UNSIGNED DEFAULT '0.00',
    `create_time` TIMESTAMP              DEFAULT CURRENT_TIMESTAMP,
    `user_id`     INT NOT NULL,
    `status_id`   INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_custom_user1_idx` (`user_id` ASC) VISIBLE,
    INDEX `fk_custom_status1_idx` (`status_id` ASC) VISIBLE,
    CONSTRAINT `fk_custom_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `restaurantDB`.`user` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT `fk_custom_status1`
        FOREIGN KEY (`status_id`)
            REFERENCES `restaurantDB`.`status` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table `restaurantDB`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `restaurantDB`.`category`;

CREATE TABLE IF NOT EXISTS `restaurantDB`.`category`
(
    `id`   int NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) UNIQUE NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `category_name_idx` (`name` ASC) VISIBLE,
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE
);


-- -----------------------------------------------------
-- Table `restaurantDB`.`dish`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `restaurantDB`.`dish`;

CREATE TABLE IF NOT EXISTS `restaurantDB`.`dish`
(
    `id`          INT AUTO_INCREMENT,
    `name`        VARCHAR(255) UNIQUE NOT NULL,
    `price`       DECIMAL(5, 2) UNSIGNED ZEROFILL,
    `details`     VARCHAR(255) DEFAULT 'none',
    `category_id` INT                 NOT NULL,
    `image_path`  VARCHAR(255),
    PRIMARY KEY (`id`),
    INDEX `fk_dish_category_idx` (`category_id` ASC) INVISIBLE,
    INDEX `dish_price_idx` (`price` ASC) VISIBLE,
    UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
    FULLTEXT INDEX `dish_name_idx` (`name`) VISIBLE,
    CONSTRAINT `fk_dish_category1`
        FOREIGN KEY (`category_id`)
            REFERENCES `restaurantDB`.`category` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);


-- -----------------------------------------------------
-- Table `restaurantDB`.`custom_has_dish`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `restaurantDB`.`custom_has_dish`;

CREATE TABLE IF NOT EXISTS `restaurantDB`.`custom_has_dish`
(
    `custom_id` INT                    NOT NULL,
    `dish_id`   INT                    NOT NULL,
    `count`     INT UNSIGNED           NULL,
    `price`     DECIMAL(5, 2) UNSIGNED NULL,
    PRIMARY KEY (`custom_id`, `dish_id`),
    INDEX `fk_custom_has_dish_dish1_idx` (`dish_id` ASC) VISIBLE,
    INDEX `fk_custom_has_dish_custom1_idx` (`custom_id` ASC) VISIBLE,
    CONSTRAINT `fk_custom_has_dish_custom1`
        FOREIGN KEY (`custom_id`)
            REFERENCES `restaurantDB`.`custom` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_custom_has_dish_dish1`
        FOREIGN KEY (`dish_id`)
            REFERENCES `restaurantDB`.`dish` (`id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
