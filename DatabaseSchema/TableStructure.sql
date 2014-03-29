DROP TABLE IF EXISTS `aasv_city`;
CREATE TABLE `aasv_city` (
  `CITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITY` varchar(30) NOT NULL,
  PRIMARY KEY (`CITYID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `aasv_citylocalities`;
CREATE TABLE `aasv_citylocalities` (
  `LOCALITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITYID` int(10) unsigned NOT NULL,
  `LOCALITY` varchar(30) NOT NULL,
  PRIMARY KEY (`LOCALITYID`),
  KEY `FK_aasv_citylocalities_cityid` (`CITYID`),
  CONSTRAINT `FK_aasv_citylocalities_cityid` FOREIGN KEY (`CITYID`) REFERENCES `aasv_city` (`CITYID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `aasv_user`;
CREATE TABLE `aasv_user` (
  `USERID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `UFNAME` varchar(20) NOT NULL,
  `ULNAME` varchar(20) NOT NULL,
  `ULOGIN` varchar(15) DEFAULT NULL,
  `GENDER` smallint(5) unsigned NOT NULL,
  `AGE` smallint(5) unsigned NOT NULL,
  `UPASSWORD` varchar(15) DEFAULT NULL,
  `UCONTACTNO` varchar(11) NOT NULL,
  `DATCREUSER` datetime NOT NULL,
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1 COMMENT='User table';


DROP TABLE IF EXISTS `aasv_travel`;
CREATE TABLE `aasv_travel` (
  `TRAVELID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USERID` int(10) unsigned NOT NULL,
  `CURRLOCATION` varchar(30) NOT NULL,
  `STARTLOCATION` varchar(30) NOT NULL,
  `ENDLOCATION` varchar(30) NOT NULL,
  `TRAVELTIME` varchar(15) NOT NULL,
  `TRAVELMODE` varchar(15) NOT NULL,
  `NOOFPASSENGER` tinyint(3) unsigned NOT NULL,
  `DATCRETRAVEL` datetime NOT NULL,
  PRIMARY KEY (`TRAVELID`),
  KEY `FK_aasv_travel_userid` (`USERID`),
  CONSTRAINT `FK_aasv_travel_userid` FOREIGN KEY (`USERID`) REFERENCES `aasv_user` (`USERID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `aasv_travelmode`;
CREATE TABLE `aasv_travelmode` (
  `TMODE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(30) NOT NULL,
  `NOFPASSENGER` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`TMODE`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `aasv_travelconfirmdet`;
CREATE TABLE `aasv_travelconfirmdet` (
  `DETIDSYS` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TRAVELID` int(10) unsigned NOT NULL,
  `MAPEDTRAVELID` int(10) unsigned NOT NULL,
  `CONFIRMEDDAT` datetime NOT NULL,
  `ARCHIVE` bit(1) NOT NULL,
  `ARCHIVEDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`DETIDSYS`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;