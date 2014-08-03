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
-- Create schema mindyourtravel
--

--
-- Definition of table `aasv_city`
--

DROP TABLE IF EXISTS `aasv_city`;
CREATE TABLE `aasv_city` (
  `CITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITY` varchar(30) NOT NULL,
  PRIMARY KEY (`CITYID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `aasv_city`
--


--
-- Definition of table `aasv_citylocalities`
--

DROP TABLE IF EXISTS `aasv_citylocalities`;
CREATE TABLE `aasv_citylocalities` (
  `LOCALITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITYID` int(10) unsigned NOT NULL,
  `LOCALITY` varchar(30) NOT NULL,
  PRIMARY KEY (`LOCALITYID`),
  KEY `FK_aasv_citylocalities_cityid` (`CITYID`),
  CONSTRAINT `FK_aasv_citylocalities_cityid` FOREIGN KEY (`CITYID`) REFERENCES `aasv_city` (`CITYID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `aasv_citylocalities`
--



--
-- Definition of table `aasv_travel`
--

DROP TABLE IF EXISTS `aasv_travel`;
CREATE TABLE `aasv_travel` (
  `TRAVELID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USERID` int(10) unsigned NOT NULL,
  `CURRLOCATION` varchar(30) NOT NULL,
  `STARTLOCATION` varchar(30) NOT NULL,
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
  KEY `FK_aasv_travel_userid` (`USERID`),
  CONSTRAINT `FK_aasv_travel_userid` FOREIGN KEY (`USERID`) REFERENCES `aasv_user` (`USERID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `aasv_travel`
--


--
-- Definition of table `aasv_travelconfirmdet`
--

DROP TABLE IF EXISTS `aasv_travelconfirmdet`;
CREATE TABLE `aasv_travelconfirmdet` (
  `DETIDSYS` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TRAVELID` int(10) unsigned NOT NULL,
  `MAPEDTRAVELID` int(10) unsigned NOT NULL,
  `CONFIRMEDDAT` datetime NOT NULL,
  `ARCHIVE` bit(1) NOT NULL,
  `ARCHIVEDATE` datetime DEFAULT NULL,
  `USERID` int(10) unsigned NOT NULL,
  `MAPEDUSERID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`DETIDSYS`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `aasv_travelconfirmdet`
--

--
-- Definition of table `aasv_travelmode`
--

DROP TABLE IF EXISTS `aasv_travelmode`;
CREATE TABLE `aasv_travelmode` (
  `TMODE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(30) NOT NULL,
  `NOFPASSENGER` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`TMODE`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `aasv_travelmode`
--

/*!40000 ALTER TABLE `aasv_travelmode` DISABLE KEYS */;
INSERT INTO `aasv_travelmode` (`TMODE`,`TYPE`,`NOFPASSENGER`) VALUES 
 (1,'AUTO',3),
 (2,'TAXI',4),
 (3,'PRIVATE VECHICLE',4),
 (4,'CAB',4);
/*!40000 ALTER TABLE `aasv_travelmode` ENABLE KEYS */;


--
-- Definition of table `aasv_user`
--

DROP TABLE IF EXISTS `aasv_user`;
CREATE TABLE `aasv_user` (
  `USERID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `UFNAME` varchar(20) NOT NULL,
  `ULNAME` varchar(20) NOT NULL,
  `GENDER` smallint(5) unsigned NOT NULL,
  `AGE` smallint(5) unsigned NOT NULL,
  `UCONTACTNO` varchar(11) NOT NULL,
  `DATCREUSER` datetime NOT NULL,
  `CITYID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`USERID`),
  KEY `FK_aasv_user__city_cityid` (`CITYID`),
  CONSTRAINT `FK_aasv_user__city_cityid` FOREIGN KEY (`CITYID`) REFERENCES `aasv_city` (`CITYID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1 COMMENT='User table';

--
-- Dumping data for table `aasv_user`
--

