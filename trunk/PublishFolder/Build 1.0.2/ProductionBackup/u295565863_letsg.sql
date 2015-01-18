-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.4.1-beta-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema u295565863_letsg
--

CREATE DATABASE IF NOT EXISTS u295565863_letsg;
USE u295565863_letsg;

--
-- Table structure for table `aasv_city`
--

CREATE TABLE IF NOT EXISTS `aasv_city` (
  `CITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITY` varchar(30) NOT NULL,
  PRIMARY KEY (`CITYID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=42 ;

--
-- Dumping data for table `aasv_city`
--

INSERT INTO `aasv_city` (`CITYID`, `CITY`) VALUES
(1, 'PUNE'),
(3, 'Noida'),
(4, 'Ghaziabad'),
(5, 'Irvine'),
(6, 'Costa Mesa'),
(7, 'Bhopal'),
(8, 'Indore'),
(9, 'Dhamtari'),
(10, 'Bilaspur'),
(11, 'Bangalore'),
(12, 'Balaghat'),
(13, 'Gondia'),
(14, 'Gurgaon'),
(15, 'New Delhi'),
(16, 'Udaipur'),
(17, 'Croydon'),
(18, 'East Croydon'),
(19, 'Coombe Road'),
(20, 'Basingstoke'),
(21, 'Union Street'),
(22, 'Newport Beach'),
(23, 'Newark'),
(24, 'New York'),
(25, 'Mumbai'),
(26, 'Nashik'),
(27, 'Kota'),
(28, 'Bengaluru'),
(29, 'Junnar'),
(30, 'Shirur'),
(31, 'Huntington Beach'),
(32, 'Ajmere Gate'),
(33, 'Gwalior'),
(34, 'Wardha'),
(35, 'Nagpur'),
(36, 'Secunderabad'),
(37, 'Safidon'),
(38, 'Thane'),
(39, 'Bharka Para'),
(40, 'Raipur'),
(41, 'Agra');

-- --------------------------------------------------------

--
-- Table structure for table `aasv_citylocalities`
--

CREATE TABLE IF NOT EXISTS `aasv_citylocalities` (
  `LOCALITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITYID` int(10) unsigned NOT NULL,
  `LOCALITY` varchar(30) NOT NULL,
  PRIMARY KEY (`LOCALITYID`),
  KEY `FK_aasv_citylocalities_cityid` (`CITYID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=94 ;

--
-- Dumping data for table `aasv_citylocalities`
--

INSERT INTO `aasv_citylocalities` (`LOCALITYID`, `CITYID`, `LOCALITY`) VALUES
(1, 1, 'Kothrud'),
(2, 1, 'Magarpatta City'),
(3, 3, 'Sector 64'),
(4, 4, 'Vasundhara'),
(5, 4, 'Crossings Republik'),
(6, 1, 'MG Road'),
(7, 4, 'Indirapuram'),
(8, 5, 'Irvine'),
(9, 6, 'Costa Mesa'),
(10, 1, 'Pune Junction Railway Station'),
(11, 1, 'Kharadi'),
(12, 1, 'Swargate Bus Depot'),
(13, 7, 'Bhopal'),
(14, 8, 'Indore'),
(15, 7, 'Minal Residency'),
(16, 9, 'Dhamtari'),
(17, 10, 'Bilaspur'),
(18, 1, 'Hinjewadi Phase 3'),
(19, 8, 'Bhavarkua'),
(20, 8, 'Navlakha Bus Station'),
(21, 1, 'Balgandharv'),
(22, 11, 'Marathahalli'),
(23, 11, 'Indra Nagar'),
(24, 1, 'Katraj'),
(25, 1, 'Hinjewadi'),
(26, 12, 'Balaghat'),
(27, 13, 'Gondia'),
(28, 14, 'Sector 18'),
(29, 3, 'Noida City Centre'),
(30, 1, 'Airport'),
(31, 16, 'Udaipur'),
(32, 14, 'Sector 14'),
(33, 18, 'East Croydon'),
(34, 19, 'Coombe Road'),
(35, 15, 'IP Extension'),
(36, 3, 'Sector 93'),
(37, 15, 'Dwarka Sector 6'),
(38, 1, 'Lakshmi Road Chowk'),
(39, 21, 'Union Street'),
(40, 22, 'Newport Beach'),
(41, 24, 'New York'),
(42, 25, 'Mumbai'),
(43, 26, 'Nashik'),
(44, 23, 'Newark'),
(45, 3, 'Noida'),
(46, 1, 'Mundhwa'),
(47, 29, 'Junnar'),
(48, 30, 'Shirur'),
(49, 31, 'Huntington Beach'),
(50, 32, 'New Delhi'),
(51, 3, 'Noida Extension'),
(52, 11, 'J P Nagar 5 Phase'),
(53, 11, 'Manayata Tech Park'),
(54, 33, 'Sindhi Colony'),
(55, 33, 'Gwalior Junction'),
(56, 34, 'Wardha'),
(57, 35, 'Nagpur'),
(58, 34, 'Ramnagar'),
(59, 36, 'Secunderabad'),
(60, 1, 'Warje'),
(61, 1, 'Sinhagad Road'),
(62, 3, 'Sector 62'),
(63, 15, 'Indira Gandhi International Ai'),
(64, 1, 'Mangalwar Peth'),
(65, 1, 'Prabhat Road'),
(66, 1, 'Bhandarkar Road'),
(67, 38, 'Rutu Park'),
(68, 38, 'Thane West'),
(69, 1, 'Dhanori'),
(70, 1, 'Shukrawar Peth'),
(71, 1, 'Wagholi'),
(72, 11, 'Bangalore'),
(73, 11, 'Kormangala'),
(74, 1, 'Dhayari'),
(75, 28, 'Electronic City Phase 1 Bus St'),
(76, 28, 'Electronics City Phase 1'),
(77, 28, 'Koramangala'),
(78, 25, 'Mumbai Airport'),
(79, 25, 'Santacruz East'),
(80, 25, 'Airport Road Metro Station'),
(81, 25, 'Malad West'),
(82, 28, 'Electronic City Phase II'),
(83, 35, 'Ramna Maroti Nagar'),
(84, 39, 'Rajnandgaon'),
(85, 40, 'Raipur'),
(86, 4, 'Crossing Republic'),
(87, 28, 'Electronic City'),
(88, 25, 'Vakola Church'),
(89, 25, 'Vakola'),
(90, 25, 'Vakola Bridge'),
(91, 25, 'Vakola Masjid'),
(92, 41, 'Kamla Nagar'),
(93, 41, 'Khandari');
/*!40000 ALTER TABLE `aasv_citylocalities` ENABLE KEYS */;
-- --------------------------------------------------------

--
-- Table structure for table `aasv_travel`
--

CREATE TABLE IF NOT EXISTS `aasv_travel` (
  `TRAVELID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USERID` int(10) unsigned NOT NULL,
  `CURRLOCATION` varchar(30) NOT NULL,
  `STARTLOCATION` varchar(50) NOT NULL,
  `ENDLOCATION` varchar(30) NOT NULL,
  `TRAVELTIME` varchar(15) NOT NULL,
  `TRAVELMODE` varchar(30) NOT NULL,
  `NOOFPASSENGER` tinyint(3) unsigned NOT NULL,
  `DATCRETRAVEL` datetime NOT NULL,
  `ISCONFIRMED` tinyint(3) unsigned NOT NULL,
  `CONFIRMDAT` datetime DEFAULT NULL,
  `ISDELETED` bit(1) NOT NULL,
  `DALETEDDAT` datetime DEFAULT NULL,
  PRIMARY KEY (`TRAVELID`),
  KEY `FK_aasv_travel_userid` (`USERID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `aasv_travelconfirmdet`
--

CREATE TABLE IF NOT EXISTS `aasv_travelconfirmdet` (
  `DETIDSYS` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TRAVELID` int(10) unsigned NOT NULL,
  `MAPEDTRAVELID` int(10) unsigned NOT NULL,
  `CONFIRMEDDAT` datetime NOT NULL,
  `ARCHIVE` bit(1) NOT NULL,
  `ARCHIVEDATE` datetime DEFAULT NULL,
  `USERID` int(10) unsigned NOT NULL,
  `MAPEDUSERID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`DETIDSYS`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


--
-- Table structure for table `aasv_travelmode`
--

CREATE TABLE IF NOT EXISTS `aasv_travelmode` (
  `TMODE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(30) NOT NULL,
  `NOFPASSENGER` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`TMODE`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `aasv_travelmode`
--

INSERT INTO `aasv_travelmode` (`TMODE`, `TYPE`, `NOFPASSENGER`) VALUES
(1, 'AUTO', 3),
(2, 'TAXI', 4),
(3, 'PRIVATE VECHICLE', 4),
(4, 'CAB', 4);

-- --------------------------------------------------------

--
-- Table structure for table `aasv_user`
--

CREATE TABLE IF NOT EXISTS `aasv_user` (
  `USERID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `UFNAME` varchar(20) NOT NULL,
  `ULNAME` varchar(20) NOT NULL,
  `GENDER` smallint(5) unsigned NOT NULL,
  `AGE` smallint(5) unsigned NOT NULL,
  `UCONTACTNO` varchar(11) NOT NULL,
  `DATCREUSER` datetime NOT NULL,
  `CITYID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`USERID`),
  KEY `FK_aasv_user__city_cityid` (`CITYID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COMMENT='User table' AUTO_INCREMENT=49 ;

--
-- Dumping data for table `aasv_user`
--

INSERT INTO `aasv_user` (`USERID`, `UFNAME`, `ULNAME`, `GENDER`, `AGE`, `UCONTACTNO`, `DATCREUSER`, `CITYID`) VALUES
(1, 'Shweta', 'S', 2, 30, '8055612125', '2014-09-16 16:48:47', 1),
(2, 'sachin', 'bajpai', 1, 34, '9494339959', '2014-09-16 18:08:04', 5),
(3, 'ram', 'balr', 1, 37, '8103418361', '2014-09-16 20:02:27', 8),
(4, 'Deepali ', 'Gupta ', 2, 35, '4430040564', '2014-09-16 21:07:54', 17),
(5, 'durgadas', 'haldar', 1, 30, '9711393630', '2014-09-17 00:25:15', 15),
(6, 'Mohit', 'Singh', 1, 35, '9818319464', '2014-09-17 02:35:21', 15),
(7, 'kushagra', 'rajpoot', 1, 24, '9818011696', '2014-09-17 04:28:59', 3),
(8, 'Amit', 'Shroti', 1, 35, '9850824449', '2014-09-17 04:52:33', 1),
(9, 'ash', 'saka', 1, 35, '+447414515', '2014-09-17 06:48:37', 20),
(10, 'chanchal', 'verma', 1, 35, '1980318826', '2014-09-17 15:09:04', 23),
(11, 'Vikrant', 'Jain', 1, 34, '9891109568', '2014-09-17 15:54:50', 4),
(12, 'Boudh Priya', 'Samrat', 1, 25, '8983533437', '2014-09-17 20:32:12', 1),
(13, 'v ', 'thapa', 1, 44, '9463440086', '2014-09-18 08:48:51', 24),
(14, 'Atul', 'M', 1, 38, '9890945190', '2014-09-18 10:11:30', 1),
(15, 'suchint', 'kotia', 1, 37, '9405030056', '2014-09-18 16:00:31', 27),
(16, 'Manish', 'Kumar', 1, 30, '9891109501', '2014-09-18 16:27:25', 1),
(17, 'Srinivasan', 'Shanmugam', 1, 28, '8105705640', '2014-09-18 16:27:43', 28),
(18, 'Sandeep', 'Seth', 1, 38, '8782442332', '2014-09-18 16:31:14', 1),
(19, 'Kunal', 'Rawat', 1, 24, '7864736212', '2014-09-18 16:42:11', 1),
(20, 'Neha', 'Dutta', 2, 30, '8438823632', '2014-09-18 16:46:33', 1),
(21, 'sharad', 'Sahu', 1, 36, '9923400948', '2014-09-18 18:04:45', 1),
(22, 'Ashish', 'Sharma', 1, 37, '9420684421', '2014-09-21 15:58:16', 1),
(23, 'sangram', 'naykodi', 1, 28, '8421082168', '2014-09-24 07:03:49', 1),
(24, 'sheikh', 'imran', 1, 26, '9818595614', '2014-09-27 03:27:59', 3),
(25, 'Ashish ', 'jain ', 1, 36, '8861877555', '2014-09-28 08:05:26', 11),
(26, 'amol', 'sikchi', 1, 32, '9923345252', '2014-09-28 08:32:47', 1),
(27, 'shirish', 'kota', 1, 28, '9665057547', '2014-09-30 14:56:23', 1),
(28, 'Rajesh', 'Sharma', 1, 40, '9423120313', '2014-10-02 14:32:53', 34),
(29, 'murali krishna', ' akula', 1, 30, '9866028521', '2014-10-06 10:44:57', 1),
(30, 'tushar', 'shinde', 1, 22, '9404434273', '2014-10-30 13:37:41', 35),
(31, 'Ankit', 'goyal', 1, 22, '9896898072', '2014-11-02 13:45:20', 37),
(32, 'rutuja', 'tipare', 2, 25, '9975417077', '2014-11-04 11:48:36', 1),
(33, 'rutuja', 'tipare', 2, 24, '9975417078', '2014-11-04 11:57:23', 1),
(34, 'deep', 'shukla', 1, 28, '9270859348', '2014-11-08 12:32:03', 1),
(35, 'parul', 'sharma', 2, 27, '9881369030', '2014-11-08 13:13:47', 1),
(36, 'Ritesh', 'Sinha', 1, 29, '9448974423', '2014-11-20 18:20:45', 11),
(37, 'Karan', 'Bhatija', 1, 27, '9820973979', '2014-11-29 17:43:01', 25),
(38, 'Akanksha', 'Pathak', 2, 34, '9823886394', '2014-12-15 09:28:09', 35),
(39, 'Ritesh', 'Sharma', 1, 40, '8103395586', '2014-12-27 11:52:07', 9),
(40, 'Hemraj', 'Sharma', 1, 64, '9425822178', '2014-12-31 15:10:26', 1),
(41, 'Prafull', 'Kothari', 1, 35, '9302293023', '2015-01-01 13:56:04', 9),
(42, 'siddharth', 'chitnis', 1, 30, '9890040168', '2015-01-05 16:35:12', 1),
(43, 'Mazhar', 'Mhate', 1, 27, '9902893243', '2015-01-10 16:55:01', 25),
(44, 'Atul', 'Sikchi', 1, 32, '9623007765', '2015-01-11 13:12:53', 1),
(45, 'karan', 'bhatija', 1, 27, '9833720176', '2015-01-13 16:23:51', 25),
(46, 'Tapan', 'Sinha', 1, 34, '9810527643', '2015-01-15 10:37:28', 3),
(47, 'piyush ', 'jain', 1, 18, '8979407774', '2015-01-17 12:02:01', 4),
(48, 'sangram', 'naykodi', 1, 28, '7276250212', '2015-01-17 12:38:25', 1);


--
-- Definition of procedure `aasv_citylocalities_insert`
--

DROP PROCEDURE IF EXISTS `aasv_citylocalities_insert`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_citylocalities_insert`(LOCALITYNAME VARCHAR(30),
CITYNAME VARCHAR(30))
BEGIN

DECLARE GETCITYID INT;

   SET GETCITYID= (SELECT CITYID FROM aasv_city WHERE city=CITYNAME);

   IF GETCITYID IS NULL THEN
           INSERT INTO aasv_city (CITY) VALUES(CITYNAME);
           SET GETCITYID= (SELECT CITYID FROM aasv_city WHERE city=CITYNAME);
   END IF;


   INSERT INTO aasv_citylocalities(CITYID,LOCALITY)
        VALUES(GETCITYID,LOCALITYNAME);

   SELECT @ROWCOUNT;

END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_city_getall`
--

DROP PROCEDURE IF EXISTS `aasv_city_getall`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_city_getall`()
BEGIN
      SELECT CITYID,CITY FROM aasv_city ORDER BY CITY;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_travelmode_getall`
--

DROP PROCEDURE IF EXISTS `aasv_travelmode_getall`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_travelmode_getall`()
BEGIN
      SELECT TMODE,TYPE,NOFPASSENGER FROM aasv_travelmode;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_travelplan_insert`
--

DROP PROCEDURE IF EXISTS `aasv_travelplan_insert`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_travelplan_insert`(
USERID INT,
CURRLOCATION VARCHAR(30),
STARTLOCATION VARCHAR(30),
ENDLOCATION VARCHAR(30),
TRAVELTIME VARCHAR(15),
TRAVELMODE VARCHAR(30),
NOOFPASSENGER TINYINT)
BEGIN
     INSERT INTO aasv_travel(USERID,CURRLOCATION,STARTLOCATION,ENDLOCATION,TRAVELTIME,TRAVELMODE,NOOFPASSENGER,DATCRETRAVEL,ISCONFIRMED,ISDELETED)
       VALUES(USERID,CURRLOCATION,STARTLOCATION,ENDLOCATION,TRAVELTIME,TRAVELMODE,NOOFPASSENGER,NOW(),0,0);
     SELECT @ROWCOUNT;       
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_travel_archives`
--

DROP PROCEDURE IF EXISTS `aasv_travel_archives`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_travel_archives`()
BEGIN

      DECLARE TRAVELDETID INT;
      DECLARE SELTRAVELID INT;
      DECLARE not_found_travel INT DEFAULT 0;

      -- declare cursor to fetch all trevel which are passed by last three hours
      DEClARE travel_cursor CURSOR FOR
      SELECT TRAVELID FROM aasv_travel where DATCRETRAVEL<= DATE_SUB(now(),INTERVAL 2 HOUR) AND ISDELETED=0;

      DECLARE CONTINUE HANDLER FOR NOT FOUND SET not_found_travel = 1;
      OPEN travel_cursor;
      SET not_found_travel = 0;
      travel_loop : LOOP
               FETCH travel_cursor INTO SELTRAVELID;
               IF not_found_travel THEN
                        CLOSE travel_cursor;
                        LEAVE travel_loop;
               END IF;



              UPDATE aasv_travel SET  ISDELETED=1,DALETEDDAT=NOW() WHERE TRAVELID=SELTRAVELID;

              SET TRAVELDETID =(SELECT  DETIDSYS FROM aasv_travelconfirmdet WHERE MAPEDTRAVELID=SELTRAVELID AND ARCHIVE=0
                               UNION
                               SELECT DETIDSYS FROM aasv_travelconfirmdet WHERE TRAVELID=SELTRAVELID AND ARCHIVE=0);

              UPDATE aasv_travel SET  ISCONFIRMED=0,CONFIRMDAT=NULL
              WHERE TRAVELID=(SELECT MAPEDTRAVELID FROM aasv_travelconfirmdet WHERE DETIDSYS=TRAVELDETID  AND TRAVELID=SELTRAVELID
                              UNION
                              SELECT TRAVELID FROM aasv_travelconfirmdet WHERE DETIDSYS=TRAVELDETID AND MAPEDTRAVELID=SELTRAVELID);

              UPDATE aasv_travelconfirmdet SET ARCHIVE=1 , ARCHIVEDATE=NOW() WHERE  DETIDSYS=TRAVELDETID;
      END LOOP;

END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_user_getloginuserdata`
--

DROP PROCEDURE IF EXISTS `aasv_user_getloginuserdata`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_user_getloginuserdata`(
/*LOGIN VARCHAR(15),USERPASSWORD VARCHAR(15)*/
MOBILENO VARCHAR(11)
)
BEGIN
    /*SELECT USERID,UFNAME,ULNAME,GENDER,AGE,UCONTACTNO FROM aasv_user WHERE  ULOGIN=LOGIN  AND UPASSWORD=USERPASSWORD;*/
    SELECT USERID,UFNAME,ULNAME,GENDER,AGE,UCONTACTNO,aasv_city.CITYID,CITY AS USERCITY
            FROM aasv_user INNER JOIN aasv_city ON aasv_city.CITYID=aasv_user.CITYID
     WHERE  UCONTACTNO=MOBILENO;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_user_getusercitylocalites`
--

DROP PROCEDURE IF EXISTS `aasv_user_getusercitylocalites`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_user_getusercitylocalites`(LOGINUSERID INT,GPSLOCCITY VARCHAR(50))
BEGIN

IF LENGTH(GPSLOCCITY) = 0 THEN
      SELECT LOCALITYID,LOCALITY FROM aasv_citylocalities
      INNER JOIN aasv_city ON aasv_city.CITYID=aasv_citylocalities.CITYID
      AND EXISTS(SELECT 1 FROM aasv_user WHERE aasv_user.USERID=LOGINUSERID
      AND aasv_user.CITYID=aasv_city.CITYID)
      ORDER BY LOCALITY;
ELSE
      SELECT LOCALITYID,LOCALITY FROM aasv_citylocalities
      INNER JOIN aasv_city ON aasv_city.CITYID=aasv_citylocalities.CITYID
      WHERE  aasv_city.CITY=GPSLOCCITY
      ORDER BY LOCALITY;
END IF;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_user_insert`
--

DROP PROCEDURE IF EXISTS `aasv_user_insert`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_user_insert`(FNAME VARCHAR(20),
LNAME VARCHAR(20),
CITYNAME VARCHAR(20),
SEX SMALLINT,
AGE SMALLINT,
MOBILENO VARCHAR(11))
BEGIN

DECLARE GETCITYID INT;
SET GETCITYID= (SELECT CITYID FROM aasv_city WHERE city=CITYNAME);
IF GETCITYID IS NULL THEN
   INSERT INTO aasv_city (CITY) VALUES(CITYNAME);
   SET GETCITYID= (SELECT CITYID FROM aasv_city WHERE city=CITYNAME);
END IF;

INSERT INTO aasv_user(UFNAME,ULNAME,GENDER,AGE,UCONTACTNO,DATCREUSER,CITYID)
VALUES(FNAME,LNAME,SEX,AGE,MOBILENO,NOW(),GETCITYID);

CALL aasv_user_getloginuserdata(MOBILENO);

END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_user_travelconfirm`
--

DROP PROCEDURE IF EXISTS `aasv_user_travelconfirm`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_user_travelconfirm`(
CURUSERTRAVELID INT,USERTRAVELID INT,CURUSERID INT,TRAVELERUSERID INT)
BEGIN
      UPDATE aasv_travel SET  ISCONFIRMED=1,CONFIRMDAT=NOW() WHERE TRAVELID=CURUSERTRAVELID;
      UPDATE aasv_travel SET  ISCONFIRMED=1,CONFIRMDAT=NOW() WHERE TRAVELID=USERTRAVELID;
      INSERT INTO aasv_travelconfirmdet(TRAVELID,MAPEDTRAVELID,CONFIRMEDDAT,ARCHIVE,ARCHIVEDATE,USERID,MAPEDUSERID)
      VALUES(CURUSERTRAVELID,USERTRAVELID,NOW(),0,NULL,CURUSERID,TRAVELERUSERID);
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_user_traveldelete`
--

DROP PROCEDURE IF EXISTS `aasv_user_traveldelete`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_user_traveldelete`(SELTRAVELID INT)
BEGIN
      DECLARE TRAVELDETID INT;
      UPDATE aasv_travel SET  ISDELETED=1,DALETEDDAT=NOW() WHERE TRAVELID=SELTRAVELID;

      SET TRAVELDETID =(SELECT  DETIDSYS FROM aasv_travelconfirmdet WHERE MAPEDTRAVELID=SELTRAVELID AND ARCHIVE=0
                       UNION
                       SELECT DETIDSYS FROM aasv_travelconfirmdet WHERE TRAVELID=SELTRAVELID AND ARCHIVE=0);

      UPDATE aasv_travel SET  ISCONFIRMED=0,CONFIRMDAT=NULL
      WHERE TRAVELID=(SELECT MAPEDTRAVELID FROM aasv_travelconfirmdet WHERE DETIDSYS=TRAVELDETID  AND TRAVELID=SELTRAVELID
                      UNION
                      SELECT TRAVELID FROM aasv_travelconfirmdet WHERE DETIDSYS=TRAVELDETID AND MAPEDTRAVELID=SELTRAVELID);

      UPDATE aasv_travelconfirmdet SET ARCHIVE=1 , ARCHIVEDATE=NOW() WHERE  DETIDSYS=TRAVELDETID;
 
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;

--
-- Definition of procedure `aasv_user_travelmatch`
--

DROP PROCEDURE IF EXISTS `aasv_user_travelmatch`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE PROCEDURE `aasv_user_travelmatch`(LOGINUSERID INT)
BEGIN

      /*Delete travels which are passed in last three hours*/
      CALL aasv_travel_archives();

      SELECT otherusers.TRAVELID,matchusers.USERID,matchusers.UCONTACTNO,otherusers.TRAVELID,matchusers.UFNAME ,matchusers.ULNAME,matchusers.GENDER,matchusers.AGE,matchusers.UCONTACTNO,
        otherusers.CURRLOCATION,otherusers.STARTLOCATION,otherusers.ENDLOCATION,otherusers.STARTLOCATION,otherusers.NOOFPASSENGER,otherusers.TRAVELMODE,otherusers.TRAVELTIME
        ,otherusers.USERID=LOGINUSERID AS ISSELFPLAN,
        (SELECT 1 FROM aasv_travelconfirmdet confirmdet WHERE ARCHIVE=0 AND (confirmdet.TRAVELID=connecteduser.TRAVELID
         AND confirmdet.MAPEDTRAVELID=otherusers.TRAVELID) OR (confirmdet.TRAVELID=otherusers.TRAVELID
         AND confirmdet.MAPEDTRAVELID=connecteduser.TRAVELID) ) AS CONFIRMEDTO,
         otherusers.ISCONFIRMED

      FROM aasv_travel otherusers
      INNER JOIN aasv_travel connecteduser ON
            connecteduser.CURRLOCATION=otherusers.CURRLOCATION
            /*AND onnecteduser.ENDLOCATION =otherusers.ENDLOCATION */
            AND connecteduser.USERID=LOGINUSERID
            /*AND otherusers.ISCONFIRMED<= connecteduser.ISCONFIRMED*/
            ANd (connecteduser.TRAVELID=otherusers.TRAVELID  OR connecteduser.NOOFPASSENGER +otherusers.NOOFPASSENGER
       <= (SELECT NOFPASSENGER FROM aasv_travelmode WHERE aasv_travelmode.TYPE =otherusers.TRAVELMODE))
      INNER JOIN aasv_user matchusers ON matchusers.USERID=otherusers.USERID
      WHERE otherusers.ISDELETED=0 AND connecteduser.ISDELETED=0  AND EXISTS(SELECT 1 FROM aasv_travel WHERE ISDELETED=0 AND USERID=LOGINUSERID)
      ORDER BY ISSELFPLAN DESC,CONFIRMEDTO DESC;

	  
	  
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;