-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: restaurantdb
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `category_name_idx` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (3,'BBQ Food'),(4,'Drinks'),(2,'Meat & Fish'),(1,'Салат');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom`
--

DROP TABLE IF EXISTS `custom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cost` decimal(15,2) unsigned DEFAULT '0.00',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int NOT NULL,
  `status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_custom_user1_idx` (`user_id`),
  KEY `fk_custom_status1_idx` (`status_id`),
  CONSTRAINT `fk_custom_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_custom_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom`
--

LOCK TABLES `custom` WRITE;
/*!40000 ALTER TABLE `custom` DISABLE KEYS */;
INSERT INTO `custom` VALUES (18,1411.90,'2022-09-19 17:11:22',13,2),(19,377.00,'2022-09-19 17:14:17',3,2),(20,541.20,'2022-09-19 17:16:54',3,2),(21,1657.05,'2022-09-19 17:20:11',9,2),(22,352.50,'2022-09-19 17:22:14',1,2);
/*!40000 ALTER TABLE `custom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `custom_has_dish`
--

DROP TABLE IF EXISTS `custom_has_dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `custom_has_dish` (
  `custom_id` int NOT NULL,
  `dish_id` int NOT NULL,
  `count` int unsigned DEFAULT NULL,
  `price` decimal(5,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`custom_id`,`dish_id`),
  KEY `fk_custom_has_dish_dish1_idx` (`dish_id`),
  KEY `fk_custom_has_dish_custom1_idx` (`custom_id`),
  CONSTRAINT `fk_custom_has_dish_custom1` FOREIGN KEY (`custom_id`) REFERENCES `custom` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_custom_has_dish_dish1` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `custom_has_dish`
--

LOCK TABLES `custom_has_dish` WRITE;
/*!40000 ALTER TABLE `custom_has_dish` DISABLE KEYS */;
INSERT INTO `custom_has_dish` VALUES (18,8,2,130.20),(18,9,2,225.50),(18,10,2,320.10),(18,14,3,20.10),(19,1,1,70.30),(19,2,1,50.20),(19,3,1,20.50),(19,4,1,170.50),(19,12,1,10.00),(19,13,1,55.50),(20,5,1,50.50),(20,6,1,220.10),(20,7,2,270.25),(20,12,2,10.00),(21,4,1,170.50),(21,5,2,50.50),(21,6,2,220.10),(21,10,1,320.10),(21,11,1,70.25),(21,13,10,55.50),(22,1,1,70.30),(22,2,1,50.20),(22,3,3,20.50),(22,4,1,170.50);
/*!40000 ALTER TABLE `custom_has_dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish`
--

DROP TABLE IF EXISTS `dish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dish` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` decimal(5,2) unsigned zerofill DEFAULT NULL,
  `details` varchar(255) DEFAULT 'none',
  `category_id` int NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_dish_category_idx` (`category_id`) /*!80000 INVISIBLE */,
  KEY `dish_price_idx` (`price`),
  FULLTEXT KEY `dish_name_idx` (`name`),
  CONSTRAINT `fk_dish_category1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish`
--

LOCK TABLES `dish` WRITE;
/*!40000 ALTER TABLE `dish` DISABLE KEYS */;
INSERT INTO `dish` VALUES (1,'Шуба',070.30,'none',1,'salad_fur_coat.webp'),(2,'Bulgarian',050.20,'none',1,'bulgarian.webp'),(3,'Grilled chicken with charred pineapple salad',020.50,'none',1,'grilled_chicken_with_charred_pineapple_salad.webp'),(4,'Печеня',170.50,'none',2,'zharkoe.webp'),(5,'Fried tune',050.50,'none',2,'fried_tune_new.webp'),(6,'Perfect pork belly',220.10,'none',2,'perfect_pork_belly.webp'),(7,'Gunpowder lamb',270.25,'none',2,'gunpowder_lamb.webp'),(8,'BBQ british ribs',130.20,'none',3,'bbq_british_ribs.webp'),(9,'Шашлик з ошийка',225.50,'none',3,'shashlyk_z_oshijka.webp'),(10,'Butterflied prawn skewers',320.10,'none',3,'butterflied_prawn_skewers.webp'),(11,'Whole burnt aubergine',070.25,'none',3,'whole_burnt_aubergine.webp'),(12,'Вода',010.00,'none',4,'bottle_of_water.webp'),(13,'Vodka Khortytsa',055.50,'none',4,'khortitsa.webp'),(14,'Coca-Cola',020.10,'none',4,'bottle_of_coca_cola.webp'),(15,'Whisky Jameson',070.25,'none',4,'jameson.webp');
/*!40000 ALTER TABLE `dish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (2,'client'),(1,'manager');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `details` varchar(45) DEFAULT 'none',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'receiving','none'),(2,'cooking','none'),(3,'delivery & payment','none'),(4,'completed','none');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(16) NOT NULL,
  `last_name` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `details` varchar(255) DEFAULT 'none',
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_user_role_idx` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'klimenko@gmail.com','Demon','Klimenko','Klimenko','none',1),(2,'kulpaka@gmail.com','Major','Kulpaka','Kulpaka','none',2),(3,'ivanov@gmail.com','Ivan','Ivanko','Ivanko','none',2),(4,'petrenko@gmail.com','Petro','Petrenko','Petrenko','none',2),(5,'козюлько@gmail.com','Козел','Козюлько','Козюлько','none',2),(6,'бозюлько@gmail.com','Бозел','Бозюлько','Бозюлько','none',2),(7,'жозюлько@gmail.com','Жозел','Жозюлько','Жозюлько','none',2),(8,'Гозюлько@gmail.com','Гозел','Гозюлько','Гозюлько','none',2),(9,'vasylko@gmail.com','Vasya','Vasylko','Vasylko','none',2),(10,'murzilko@gmail.com','Murza','Murzilko','Murzilko','none',2),(11,'mohito@gmail.com','Оля','Мохіто','mohito','none',2),(12,'gggyyy@gmail.com','Саша','fddsd','1234','none',2),(13,'gghh@gmail.com','какашка','какашка','5566','none',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'restaurantdb'
--

--
-- Dumping routines for database 'restaurantdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-09-19 21:06:42
