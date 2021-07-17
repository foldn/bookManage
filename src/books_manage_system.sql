/*
SQLyog Enterprise v12.09 (64 bit)
MySQL - 8.0.21 : Database - book_manage_system
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`book_manage_system` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `book_manage_system`;

/*Table structure for table `books` */

DROP TABLE IF EXISTS `books`;

CREATE TABLE `books` (
  `books_id` varchar(20) NOT NULL,
  `books_name` varchar(20) NOT NULL,
  `books_author` varchar(20) NOT NULL,
  `books_type` varchar(20) NOT NULL,
  `books_price` double(10,2) NOT NULL,
  `books_isborrowed` tinyint(1) NOT NULL,
  `books_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`books_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `books` */

insert  into `books`(`books_id`,`books_name`,`books_author`,`books_type`,`books_price`,`books_isborrowed`,`books_date`)
values ('20180901','时间简史','史蒂芬.霍金','科技类',98.00,0,'2021-04-03 19:25:19')
,('20180903','你坏','大冰','青春文学',38.80,0,'2021-04-03 19:25:19')
,('20180904','阿弥陀佛么么哒','大冰','青春文学',38.80,1,'2021-04-03 19:25:19')
,('20180906','他们最善良','大冰','青春文学',38.80,0,'2021-04-03 19:25:19')
,('20180911','乖，摸摸头','大冰','青春文学',38.60,0,'2021-04-03 19:25:19')
,('20180915','天工开物','宋应星','综合性',78.00,1,'2021-04-03 19:25:19')
,('20180916','荣格','尼采','哲学',58.00,1,'2021-04-03 19:25:19')
,('20180917','Java从入门到精通','国家863中部软件孵化器','技术类',98.00,0,'2021-04-03 19:25:19')
,('20180918','狂人日记','鲁迅','文学类',62.00,1,'2021-04-03 19:25:19')
,('20180919','走遍中国','阿纲','人文自然',36.90,0,'2021-04-03 19:25:19')
,('20180920','呐喊','鲁迅','文学类',67.00,0,'2021-04-03 19:25:19');

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `users_id` int NOT NULL AUTO_INCREMENT,
  `users_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `users_account` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `users_password` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `users_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_start` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`users_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `users` */

insert  into `users`(`users_id`,`users_name`,`users_account`,`users_password`,`users_type`,`user_start`)
values (1,'卡莎','123','123','管理员','2021-04-04 12:42:23'),(2,'霞','123','123','游客','2021-04-04 12:43:29');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
