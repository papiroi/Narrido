-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: narridodb
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `access_codes`
--

DROP TABLE IF EXISTS `access_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access_codes` (
  `codeid` int(11) NOT NULL AUTO_INCREMENT,
  `access_code` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `userid` int(11) DEFAULT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`codeid`),
  UNIQUE KEY `access_code_unique` (`access_code`),
  KEY `fk_access_codes_users1_idx` (`userid`),
  KEY `fk_access_codes_user_group1_idx` (`group_id`),
  CONSTRAINT `fk_access_codes_user_group1` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_access_codes_users1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `access_codes`
--

LOCK TABLES `access_codes` WRITE;
/*!40000 ALTER TABLE `access_codes` DISABLE KEYS */;
INSERT INTO `access_codes` VALUES (1,'95N6Z-9A3CA','it-staff',2,1),(2,'V9HJZ-NW4XR','it-staff',3,1),(3,'RQID4-9YKVS','property-supply',5,1),(4,'W1XM6-JYUTN','it-staff',4,1),(5,'6K35M-6RN4T','dept-head',6,2),(6,'ROFAV-FR22P','faculty',7,3),(7,'NA6NP-RX3UV','faculty',8,3),(8,'BD0PV-FU4DD','faculty',9,3),(9,'NX9CE-MY2I0','faculty',10,3),(10,'L6EEZ-58ODC','student',NULL,4),(11,'QJ8F0-23AR8','student',NULL,5),(12,'W5D5T-3IIY7','student',NULL,6);
/*!40000 ALTER TABLE `access_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `access_codes_group`
--

DROP TABLE IF EXISTS `access_codes_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access_codes_group` (
  `codeid` int(11) NOT NULL,
  `code` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`codeid`),
  UNIQUE KEY `code_UNIQUE` (`code`),
  KEY `fk_access_codes_group_user_group1_idx` (`group_id`),
  CONSTRAINT `fk_access_codes_group_user_group1` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `access_codes_group`
--

LOCK TABLES `access_codes_group` WRITE;
/*!40000 ALTER TABLE `access_codes_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `access_codes_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_trail`
--

DROP TABLE IF EXISTS `audit_trail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_trail` (
  `trailid` int(11) NOT NULL AUTO_INCREMENT,
  `who` int(11) NOT NULL,
  `description` varchar(256) NOT NULL,
  `date_done` datetime NOT NULL,
  PRIMARY KEY (`trailid`),
  KEY `fk_audit_trail_users1_idx` (`who`),
  KEY `time` (`date_done`),
  CONSTRAINT `fk_audit_trail_users1` FOREIGN KEY (`who`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_trail`
--

LOCK TABLES `audit_trail` WRITE;
/*!40000 ALTER TABLE `audit_trail` DISABLE KEYS */;
INSERT INTO `audit_trail` VALUES (1,5,'Added new PC: CL-01 PC-01 at Computer Laboratory 1','2017-05-07 06:25:19'),(2,5,'Added new PC: CL-01 PC-02 at Computer Laboratory 1','2017-05-07 06:26:09'),(3,5,'Added new PC: CL-01 PC-03 at Computer Laboratory 1','2017-05-07 06:27:25'),(4,5,'Added new PC: CL-01 PC-04 at Computer Laboratory 1','2017-05-07 06:29:43'),(5,5,'Added new PC: CL-01 PC-05 at Computer Laboratory 1','2017-05-07 06:30:28'),(6,2,'Added monitoring entry for Computer Laboratory 1','2017-05-07 06:35:31'),(7,2,'Added monitoring entry for Computer Laboratory 1','2017-05-07 06:41:05'),(8,2,'Added monitoring entry for Computer Laboratory 1','2017-05-07 06:42:15'),(9,2,'Added monitoring entry for Computer Laboratory 1','2017-05-07 06:42:50'),(10,2,'Added monitoring entry for Computer Laboratory 1','2017-05-07 06:43:56'),(11,3,'Updated issue # 1: Walang wampserver at mysql connector(resolved)','2017-05-07 06:51:26'),(12,3,'Updated issue # 2: Walang wampserver at mysql connector(pending)','2017-05-07 06:52:23'),(13,2,'Updated issue # 3: slow running(pending)','2017-05-07 06:53:58'),(14,4,'Updated issue # 4: cannot boot windows(resolved)','2017-05-07 06:55:05'),(15,4,'Updated issue # 5: blue screen at startup(resolved)','2017-05-07 06:55:31');
/*!40000 ALTER TABLE `audit_trail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `commentid` int(11) NOT NULL AUTO_INCREMENT,
  `comment_content` varchar(160) NOT NULL,
  `userid` int(11) NOT NULL,
  `postid` int(11) NOT NULL,
  PRIMARY KEY (`commentid`),
  KEY `fk_comments_users1_idx` (`userid`),
  KEY `fk_comments_posts1_idx` (`postid`),
  CONSTRAINT `fk_comments_posts1` FOREIGN KEY (`postid`) REFERENCES `posts` (`postid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_users1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_monitoring`
--

DROP TABLE IF EXISTS `daily_monitoring`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `daily_monitoring` (
  `monitoringid` int(11) NOT NULL AUTO_INCREMENT,
  `labid` int(11) NOT NULL,
  `date_time` datetime NOT NULL,
  `instructor` varchar(128) NOT NULL,
  `course_section` varchar(128) NOT NULL,
  `system_unit` varchar(128) DEFAULT 'OK',
  `monitor` varchar(128) DEFAULT 'OK',
  `keyboard` varchar(128) DEFAULT 'OK',
  `mouse` varchar(128) DEFAULT 'OK',
  `avr` varchar(128) DEFAULT 'OK',
  `remarks` varchar(160) NOT NULL,
  `comment` varchar(160) NOT NULL,
  `userid` int(11) NOT NULL,
  PRIMARY KEY (`monitoringid`),
  KEY `fk_daily_monitoring_labs1_idx` (`labid`),
  KEY `fk_daily_monitoring_users1_idx` (`userid`),
  CONSTRAINT `fk_daily_monitoring_labs1` FOREIGN KEY (`labid`) REFERENCES `labs` (`labid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_daily_monitoring_users1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_monitoring`
--

LOCK TABLES `daily_monitoring` WRITE;
/*!40000 ALTER TABLE `daily_monitoring` DISABLE KEYS */;
INSERT INTO `daily_monitoring` VALUES (1,1,'2017-05-07 06:35:31','Grace S. Ibañez','BSCS 3A','OK','OK','OK','OK','OK','none','clean',2),(2,1,'2017-05-07 06:41:05','Sophia Poblete','BSIT 3E','OK','OK','OK','OK','OK','leftover chocolate drink at pc-03','chairs not arranged',2),(3,1,'2017-05-07 06:42:15','Jade Lopez','AB Journ 2A','OK','OK','OK','OK','OK','clean','ok',2),(4,1,'2017-05-07 06:42:50','Liezel Lagat','BSEd 1A','OK','OK','OK','OK','OK','ok','ok',2),(5,1,'2017-05-07 06:43:56','Roselyn Anne Fernandez Cuenca','BS Med Tech 3A','OK','OK','PC-03','OK','OK','pc-03 keyboard keys not arranged','no footsocks',2);
/*!40000 ALTER TABLE `daily_monitoring` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file` (
  `fileid` int(11) NOT NULL AUTO_INCREMENT,
  `file_url` varchar(512) NOT NULL,
  `file_name` varchar(256) NOT NULL,
  `userid` int(11) NOT NULL,
  `group_id` int(11) DEFAULT NULL,
  `postid` int(11) DEFAULT NULL,
  `date_uploaded` datetime NOT NULL,
  `file_type` varchar(45) DEFAULT 'file',
  PRIMARY KEY (`fileid`),
  KEY `fk_file_users1_idx` (`userid`),
  KEY `fk_file_user_group1_idx` (`group_id`),
  KEY `fk_file_posts1_idx` (`postid`),
  CONSTRAINT `fk_file_posts1` FOREIGN KEY (`postid`) REFERENCES `posts` (`postid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_file_user_group1` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_file_users1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (1,'http://localhost:8080/files/qr/PC-01 Computer Laboratory 1.png','PC-01 Computer Laboratory 1.png',5,NULL,NULL,'2017-05-07 06:25:19','qr'),(2,'http://localhost:8080/files/qr/PC-02 Computer Laboratory 1.png','PC-02 Computer Laboratory 1.png',5,NULL,NULL,'2017-05-07 06:26:09','qr'),(3,'http://localhost:8080/files/qr/PC-03 Computer Laboratory 1.png','PC-03 Computer Laboratory 1.png',5,NULL,NULL,'2017-05-07 06:27:25','qr'),(4,'http://localhost:8080/files/qr/PC-04 Computer Laboratory 1.png','PC-04 Computer Laboratory 1.png',5,NULL,NULL,'2017-05-07 06:29:43','qr'),(5,'http://localhost:8080/files/qr/PC-05 Computer Laboratory 1.png','PC-05 Computer Laboratory 1.png',5,NULL,NULL,'2017-05-07 06:30:28','qr'),(6,'http://localhost:8080/files/reports/PC Report Computer Laboratory 1 May 7 2017 0630.pdf','PC Report Computer Laboratory 1 May 7 2017 0630.pdf',5,NULL,NULL,'2017-05-07 06:30:53','report'),(7,'http://localhost:8080/files/reports/Monitoring Report Computer Laboratory 1 May 7 2017 0644.pdf','Monitoring Report Computer Laboratory 1 May 7 2017 0644.pdf',2,NULL,NULL,'2017-05-07 06:44:14','report'),(8,'http://localhost:8080/files/reports/Job Summary May 7 2017 0652.pdf','Job Summary May 7 2017 0652.pdf',3,NULL,NULL,'2017-05-07 06:52:45','report'),(9,'http://localhost:8080/files/reports/Job Summary May 7 2017 0655.pdf','Job Summary May 7 2017 0655.pdf',4,NULL,NULL,'2017-05-07 06:55:37','report');
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gadjets`
--

DROP TABLE IF EXISTS `gadjets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gadjets` (
  `gadjetsid` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(160) NOT NULL,
  `type` varchar(45) NOT NULL,
  `date_acquired` date DEFAULT NULL,
  `serialno` varchar(128) DEFAULT NULL,
  `property_number` varchar(128) DEFAULT NULL,
  `unit_value` double DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `mr` varchar(128) DEFAULT NULL,
  `re_mr` varchar(128) DEFAULT NULL,
  `pcid` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  PRIMARY KEY (`gadjetsid`),
  KEY `fk_gadjets_pc1_idx` (`pcid`),
  KEY `fk_gadjets_labs1_idx` (`labid`),
  CONSTRAINT `fk_gadjets_labs1` FOREIGN KEY (`labid`) REFERENCES `labs` (`labid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_gadjets_pc1` FOREIGN KEY (`pcid`) REFERENCES `pc` (`pcid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gadjets`
--

LOCK TABLES `gadjets` WRITE;
/*!40000 ALTER TABLE `gadjets` DISABLE KEYS */;
/*!40000 ALTER TABLE `gadjets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_pending`
--

DROP TABLE IF EXISTS `group_pending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_pending` (
  `pendingid` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`pendingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_pending`
--

LOCK TABLES `group_pending` WRITE;
/*!40000 ALTER TABLE `group_pending` DISABLE KEYS */;
/*!40000 ALTER TABLE `group_pending` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobs` (
  `jobid` int(11) NOT NULL AUTO_INCREMENT,
  `date_reported` datetime NOT NULL,
  `remarks` varchar(128) DEFAULT NULL,
  `report` varchar(256) NOT NULL,
  `findings` varchar(256) DEFAULT NULL,
  `application` varchar(128) DEFAULT NULL,
  `userid_handled_by` int(11) DEFAULT NULL,
  `date_resolved` datetime DEFAULT NULL,
  `userid_reported_by` int(11) NOT NULL,
  `status` varchar(64) DEFAULT NULL,
  `pcid` int(11) NOT NULL,
  PRIMARY KEY (`jobid`),
  KEY `fk_jobs_users1_idx` (`userid_handled_by`),
  KEY `fk_jobs_users2_idx` (`userid_reported_by`),
  KEY `date_reported` (`date_reported`),
  KEY `fk_jobs_pc1_idx` (`pcid`),
  CONSTRAINT `fk_jobs_pc1` FOREIGN KEY (`pcid`) REFERENCES `pc` (`pcid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_jobs_users1` FOREIGN KEY (`userid_handled_by`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_jobs_users2` FOREIGN KEY (`userid_reported_by`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobs`
--

LOCK TABLES `jobs` WRITE;
/*!40000 ALTER TABLE `jobs` DISABLE KEYS */;
INSERT INTO `jobs` VALUES (1,'2017-05-07 06:46:04',NULL,'Walang wampserver at mysql connector','no wampserver installed','installed wampserver',3,'2017-05-07 06:51:26',9,'resolved',1),(2,'2017-05-07 06:46:13',NULL,'Walang wampserver at mysql connector','no wampserver and mysql connector installed','installed wampserver & mysql connector',3,NULL,9,'pending',2),(3,'2017-05-07 06:49:04',NULL,'slow running','hdd has bad sectors','for further inspection',2,NULL,8,'pending',3),(4,'2017-05-07 06:49:57',NULL,'cannot boot windows','corrupted os','reformatted hdd',4,'2017-05-07 06:55:05',7,'resolved',4),(5,'2017-05-07 06:50:32',NULL,'blue screen at startup','corrupted os','formatted hard drive',4,'2017-05-07 06:55:31',7,'resolved',5);
/*!40000 ALTER TABLE `jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labs`
--

DROP TABLE IF EXISTS `labs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labs` (
  `labid` int(11) NOT NULL AUTO_INCREMENT,
  `lab_description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`labid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labs`
--

LOCK TABLES `labs` WRITE;
/*!40000 ALTER TABLE `labs` DISABLE KEYS */;
INSERT INTO `labs` VALUES (1,'Computer Laboratory 1'),(2,'Computer Laboratory 2'),(3,'Computer Laboratory 3'),(4,'Computer Laboratory 4'),(5,'Computer Laboratory 5'),(6,'Computer Laboratory 6'),(7,'Computer Laboratory 7'),(8,'Computer Laboratory 8'),(9,'Computer Laboratory 9');
/*!40000 ALTER TABLE `labs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `par`
--

DROP TABLE IF EXISTS `par`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `par` (
  `parid` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`parid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `par`
--

LOCK TABLES `par` WRITE;
/*!40000 ALTER TABLE `par` DISABLE KEYS */;
/*!40000 ALTER TABLE `par` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pc`
--

DROP TABLE IF EXISTS `pc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pc` (
  `pcid` int(11) NOT NULL AUTO_INCREMENT,
  `pc_number` varchar(45) DEFAULT NULL,
  `pc_name` varchar(45) NOT NULL,
  `pc_description` varchar(512) DEFAULT NULL,
  `date_acquired` date DEFAULT NULL,
  `serialno` varchar(45) DEFAULT NULL,
  `property_number` varchar(128) DEFAULT NULL,
  `unit_value` double DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `labid` int(11) NOT NULL,
  `date_info_updated` date DEFAULT NULL,
  `mr` varchar(128) DEFAULT NULL,
  `re_mr` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`pcid`),
  KEY `fk_pc_labs_idx` (`labid`),
  CONSTRAINT `fk_pc_labs` FOREIGN KEY (`labid`) REFERENCES `labs` (`labid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pc`
--

LOCK TABLES `pc` WRITE;
/*!40000 ALTER TABLE `pc` DISABLE KEYS */;
INSERT INTO `pc` VALUES (1,'CL-01 PC-01','PC-01','Acer Veriton\nCPU: Core i7 6500K @ 3.80GHz 3M Cache\nRAM: Kingston 8GB RAM\nHDD: Seagate 40TB SSD\nAVR: Excell','2017-05-07','xxxxxxyyyyyy524420','53123',30000,'WORKING',1,NULL,'Mildred Apostol','Bryan Balaga'),(2,'CL-01 PC-02','PC-02','Acer Veriton CPU: Core i7 6500K @ 3.80GHz 3M Cache RAM: Kingston 8GB RAM HDD: Seagate 40TB SSD AVR: Excell','2017-05-07','xxxxxxyyyyyy524421','53124',30000,'WORKING',1,NULL,'Mildred Apostol','Bryan Balaga'),(3,'CL-01 PC-03','PC-03','Acer Veriton CPU: Core i7 6500K @ 3.80GHz 3M Cache RAM: Kingston 8GB RAM HDD: Seagate 40TB SSD AVR: Excell','2017-05-07','xxxxxxyyyyyy524422','53125',30000,'WORKING',1,NULL,'Mildred Apostol','Bryan Balaga'),(4,'CL-01 PC-04','PC-04','Acer Veriton CPU: Core i7 6500K @ 3.80GHz 3M Cache RAM: Kingston 8GB RAM HDD: Seagate 40TB SSD AVR: Excell','2017-05-07','xxxxxxyyyyyy524423','53126',30000,'WORKING',1,NULL,'Mildred Apostol','Bryan Balaga'),(5,'CL-01 PC-05','PC-05','Acer Veriton CPU: Core i7 6500K @ 3.80GHz 3M Cache RAM: Kingston 8GB RAM HDD: Seagate 40TB SSD AVR: Excell','2017-05-07','xxxxxxyyyyyy524424','53127',30000,'WORKING',1,NULL,'Mildred Apostol','Bryan Balaga');
/*!40000 ALTER TABLE `pc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pc_install`
--

DROP TABLE IF EXISTS `pc_install`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pc_install` (
  `pcid` int(11) NOT NULL,
  `pc_install_data` mediumtext,
  PRIMARY KEY (`pcid`),
  KEY `fk_pc_install_pc1_idx` (`pcid`),
  CONSTRAINT `fk_pc_install_pc1` FOREIGN KEY (`pcid`) REFERENCES `pc` (`pcid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pc_install`
--

LOCK TABLES `pc_install` WRITE;
/*!40000 ALTER TABLE `pc_install` DISABLE KEYS */;
/*!40000 ALTER TABLE `pc_install` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pc_trail`
--

DROP TABLE IF EXISTS `pc_trail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pc_trail` (
  `pcid` int(11) NOT NULL,
  `action` varchar(256) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `userid` int(11) NOT NULL,
  PRIMARY KEY (`pcid`),
  KEY `fk_pc_trail_pc1_idx` (`pcid`),
  KEY `fk_pc_trail_users1_idx` (`userid`),
  CONSTRAINT `fk_pc_trail_pc1` FOREIGN KEY (`pcid`) REFERENCES `pc` (`pcid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_pc_trail_users1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pc_trail`
--

LOCK TABLES `pc_trail` WRITE;
/*!40000 ALTER TABLE `pc_trail` DISABLE KEYS */;
/*!40000 ALTER TABLE `pc_trail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
  `postid` int(11) NOT NULL AUTO_INCREMENT,
  `post_content` varchar(160) DEFAULT NULL,
  `userid` int(11) NOT NULL,
  `group_id` int(11) NOT NULL,
  `date_posted` datetime NOT NULL,
  PRIMARY KEY (`postid`),
  KEY `fk_posts_users1_idx` (`userid`),
  KEY `fk_posts_user_group1_idx` (`group_id`),
  KEY `datez` (`date_posted`),
  CONSTRAINT `fk_posts_user_group1` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_posts_users1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `remr`
--

DROP TABLE IF EXISTS `remr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `remr` (
  `remrid` int(11) NOT NULL AUTO_INCREMENT,
  `recipient` varchar(160) DEFAULT NULL,
  `article` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`remrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remr`
--

LOCK TABLES `remr` WRITE;
/*!40000 ALTER TABLE `remr` DISABLE KEYS */;
/*!40000 ALTER TABLE `remr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group`
--

DROP TABLE IF EXISTS `user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `group_name` varchar(256) NOT NULL,
  `userid_owner` int(11) NOT NULL,
  PRIMARY KEY (`group_id`),
  KEY `fk_user_group_users1_idx` (`userid_owner`),
  CONSTRAINT `fk_user_group_users1` FOREIGN KEY (`userid_owner`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group`
--

LOCK TABLES `user_group` WRITE;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
INSERT INTO `user_group` VALUES (1,'it-staff','Computer Technician Unit',1),(2,'dept-head','Department Heads @ CvSU-Imus',1),(3,'faculty','Department of IT',6),(4,'student','Thesis 1 BSIT 3A',7),(5,'student','Thesis 1 & MIS BSIT 3D',7),(6,'student','Operating System BSCS 3A',7);
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group_members`
--

DROP TABLE IF EXISTS `user_group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group_members` (
  `membershipid` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `confirmed` tinyint(1) NOT NULL DEFAULT '0',
  `codeid` int(11) NOT NULL,
  PRIMARY KEY (`membershipid`),
  UNIQUE KEY `unique` (`group_id`,`userid`),
  KEY `fk_user_group_has_users_users1_idx` (`userid`),
  KEY `fk_user_group_has_users_user_group1_idx` (`group_id`),
  KEY `fk_user_group_members_access_codes1_idx` (`codeid`),
  CONSTRAINT `fk_user_group_has_users_user_group1` FOREIGN KEY (`group_id`) REFERENCES `user_group` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_has_users_users1` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_members_access_codes1` FOREIGN KEY (`codeid`) REFERENCES `access_codes` (`codeid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group_members`
--

LOCK TABLES `user_group_members` WRITE;
/*!40000 ALTER TABLE `user_group_members` DISABLE KEYS */;
INSERT INTO `user_group_members` VALUES (1,1,2,1,1),(2,1,3,1,2),(3,1,4,1,4),(4,1,5,1,3),(5,2,6,1,5),(6,3,7,1,6),(7,3,8,1,7),(8,3,9,1,8),(9,3,10,1,9),(10,6,11,1,12),(11,6,12,1,12),(12,6,13,1,12),(13,6,14,1,12),(14,6,15,1,12),(15,6,16,1,12);
/*!40000 ALTER TABLE `user_group_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_group_pending`
--

DROP TABLE IF EXISTS `user_group_pending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_group_pending` (
  `user_group_group_id` int(11) NOT NULL,
  `users_userid` int(11) NOT NULL,
  PRIMARY KEY (`user_group_group_id`,`users_userid`),
  KEY `fk_user_group_has_users_users2_idx` (`users_userid`),
  KEY `fk_user_group_has_users_user_group2_idx` (`user_group_group_id`),
  CONSTRAINT `fk_user_group_has_users_user_group2` FOREIGN KEY (`user_group_group_id`) REFERENCES `user_group` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_has_users_users2` FOREIGN KEY (`users_userid`) REFERENCES `users` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_group_pending`
--

LOCK TABLES `user_group_pending` WRITE;
/*!40000 ALTER TABLE `user_group_pending` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_group_pending` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `id_number` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` varchar(64) NOT NULL,
  `salt` varchar(64) NOT NULL,
  `type` varchar(45) NOT NULL,
  `first_name` varchar(64) NOT NULL,
  `middle_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  `address` varchar(512) NOT NULL,
  `is_confirmed` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `id_number_UNIQUE` (`id_number`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,201412345,'liane','1b36dd17762e28a1d417dc9c9eaf842341a62f7697e294c3e7ee751584e37','a424ee53cb4d7be9ca1b250aadf4f4a7d1ac927bfd38dda4fbbd9a3a2b170','mis-officer','Liane Vina','G.','Ocampo','debug',1),(2,201400000,'bryan','5eaf5c31af3975c1109914a5541051e3037b72fa15624ed304aab5a97fef036','bf723972f53da490113bc870bc5e1cb2702f7d1881914652413fd86451abd','it-staff','Bryan','V.','Balaga','debug',1),(3,201400001,'arren','305ccab7abca4201bbd2c22dbcec2910374a439dcab4ad1811aec179e1177b','302117cd37c3baaef1c9a9406a527bdda5e6c961baa52daf36093e9981c86','it-staff','Arren','Jakob','Miranda','debug',1),(4,201400002,'kyle','a198bf43f2c23f636f5326e1676f9e1d856c90849c1688ededa49117f2fa7','bd43d1c94402957e06569f2e98795f86d84b84e44b8fd8b8fd79ef39d8536','it-staff','Kyle','Kyle','Chua','debug',1),(5,201410000,'nelia','3fae223335a91d1bea7913bc5a3da9119fe0711796bc598ac84f4dfcc563a1b','d2931d238640f8895fe35f3235a5221642dc803f18fc6a57c1631a21f41be9','property-supply','Nelia','G.','Ocampo','debug',1),(6,201420000,'rosalina','61764c59deda7828fab586bf6b596aead93999bdd8f1c28393743ff79ac6a674','a69caca0ef71cb89646bafcc4515cf64e13fe5cbabdc1585434905f713034','dept-head','Rosalina','D.','Lacuesta','debug',1),(7,201430000,'milleth','ad99f1bcb2080184a82adfc7042f716285b3680ce486db6c15cc1c0fb20a090','3ed06b9f6f88ea258cfaebe2983312bc3cad9f47e677093b2485de6ff2','faculty','Milleth','M.','Bautista','debug',1),(8,201430001,'jona','96cd8c96573c2df1f248fc9066d95656236e541b7dde2479a31a2489dbee8','6c55791bcff2687e613fb7cfa3e29b72a071bcfd75342c40ee60ad7e19b041f0','faculty','Jonalyn','Reyes','De Dios','debug',1),(9,201430002,'may','b213b2b2469ff85995f8af8e2bdae744a830a06458d1adc0998c4a2033fc36','eed7863c4dc16bde060f68e3871cb35f683ab14a6b6a90d1cc3cdf235f','faculty','May','Sarreal','Aguilar','debug',1),(10,201430003,'joven','8f2cdf513ec7a44f64604a49e8c06dc1dba4e8c3164907232d7f52c692a6290','a93a2b578880b23a71915144371a6f74944564364f17e5428c1a897fdddbed','faculty','Joven','S.','Rios','debug',1),(11,201440000,'princess','2878e86d7820833954dc7583d8370183f2247aa3a97a6de2df3a6bf242ad341','7368abd321bcee722078ee1b5daf9a9aea7aad58ffcec272a1eeb72282d30','student','Princess Meliza','Garcia','Narrido','debug',1),(12,20140001,'mariejane','a92f68202f95892ff0bd8741a97b8d014dbceb0c5768664b9f8cc3fa4b8bdbf','3648e4941436f2dd489833e5d57b22eb884d78c02c62e03043be79e73e57e37c','student','Marie Jane','Mosende','Herbas','debug',1),(13,20140002,'charmaine','d6ba144884b459449fa4cd3ddcc5e275dfebfc5de6b26e41c3efb1a93dc326','d9649eae8322daf3dc78fa16e8f5d6763ac385d9b211403d4c8baa85aa57a5','student','Charmaine','Reynoso','Mantillas','debug',1),(14,201440002,'alyssa','2c967fb6bdbd94b4b238a33417a45d55a19f27cffef7d4bc54983db858891d','c0abbecc133688adf06c626eb534cf7830fdeda42121388a719d1fef964565a','student','Alyssa Mae','Remoroza','Santillan','debug',1),(15,201440003,'ahrold','587d82f5d936de540dcdd77d8994bca2a9a6e98e7bda33b0381897e07a78b8','70206d7f6be1a61a14f685290ad9ec893c0cf3d63927b880ac379046fb3978','student','John Ahrol','Calinga','Paraon','debug',1),(16,201440004,'karen','e8d7aa13142edb5847fd5ca02267f837d4a9d0464c6b62ad906b60d6a9be6','7738a5e37c4739ae18390855e45fc548a26bfdcbf913fd0918359e6ec84c','student','Jessca-Karen','Tagalicud','Adolfo','debug',1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-07  6:57:09
