CREATE DATABASE `mindyourtravel` /*!40100 DEFAULT CHARACTER SET latin1 */;


DROP TABLE IF EXISTS `mindyourtravel`.`aasv_user`;
CREATE TABLE  `mindyourtravel`.`aasv_user` (
  `USERID` int(11) NOT NULL AUTO_INCREMENT,
  `UFNAME` varchar(20) NOT NULL,
  `ULNAME` varchar(20) NOT NULL,
  `ULOGIN` varchar(15) NOT NULL,
  `GENDER` bit(1) NOT NULL DEFAULT b'0',
  `AGE` smallint(5) unsigned NOT NULL,
  `UPASSWORD` varchar(15) NOT NULL,
  `UCONTACTNO` varchar(11) NOT NULL,
  `DATCREUSER` datetime NOT NULL,
  `DATMODUSER` datetime NOT NULL,
  `ISACTIVE` bit(1) NOT NULL,
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1 COMMENT='User table';