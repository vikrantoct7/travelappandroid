
DROP TABLE IF EXISTS `aasv_city`;
CREATE TABLE `aasv_city` (
  `CITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITY` varchar(30) NOT NULL,
  PRIMARY KEY (`CITYID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


/*!40000 ALTER TABLE `aasv_city` DISABLE KEYS */;
INSERT INTO `aasv_city` (`CITYID`,`CITY`) VALUES 
 (1,'PUNE');
/*!40000 ALTER TABLE `aasv_city` ENABLE KEYS */;



DROP TABLE IF EXISTS `aasv_citylocalities`;
CREATE TABLE `aasv_citylocalities` (
  `LOCALITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `CITYID` int(10) unsigned NOT NULL,
  `LOCALITY` varchar(30) NOT NULL,
  PRIMARY KEY (`LOCALITYID`),
  KEY `FK_aasv_citylocalities_cityid` (`CITYID`),
  CONSTRAINT `FK_aasv_citylocalities_cityid` FOREIGN KEY (`CITYID`) REFERENCES `aasv_city` (`CITYID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;



/*!40000 ALTER TABLE `aasv_citylocalities` DISABLE KEYS */;
INSERT INTO `aasv_citylocalities` (`LOCALITYID`,`CITYID`,`LOCALITY`) VALUES 
 (3,1,'Kothrud'),
 (4,1,'MG Road'),
 (5,1,'Railway Station'),
 (6,1,'Airport'),
 (7,1,'Swargate'),
 (8,1,'Hadapsar'),
 (9,1,'Kharadi'),
 (10,1,'Shivaji Nagar Bus  Depot');
/*!40000 ALTER TABLE `aasv_citylocalities` ENABLE KEYS */;




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
  `ISCONFIRMED` bit(1) NOT NULL,
  `CONFIRMDAT` datetime DEFAULT NULL,
  `ISDELETED` bit(1) NOT NULL,
  `DALETEDDAT` datetime DEFAULT NULL,
  PRIMARY KEY (`TRAVELID`),
  KEY `FK_aasv_travel_userid` (`USERID`),
  CONSTRAINT `FK_aasv_travel_userid` FOREIGN KEY (`USERID`) REFERENCES `aasv_user` (`USERID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;





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




DROP TABLE IF EXISTS `aasv_travelmode`;
CREATE TABLE `aasv_travelmode` (
  `TMODE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(30) NOT NULL,
  `NOFPASSENGER` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`TMODE`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;



/*!40000 ALTER TABLE `aasv_travelmode` DISABLE KEYS */;
INSERT INTO `aasv_travelmode` (`TMODE`,`TYPE`,`NOFPASSENGER`) VALUES 
 (1,'AUTO',3),
 (2,'TAXI',4);
/*!40000 ALTER TABLE `aasv_travelmode` ENABLE KEYS */;




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


DROP PROCEDURE IF EXISTS `aasv_city_getall`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_city_getall`()
BEGIN
      SELECT CITYID,CITY FROM aasv_city;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `aasv_travelmode_getall`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_travelmode_getall`()
BEGIN
      SELECT TMODE,TYPE,NOFPASSENGER FROM aasv_travelmode;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `aasv_travelplan_insert`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_travelplan_insert`(
USERID INT,
CURRLOCATION VARCHAR(30),
STARTLOCATION VARCHAR(30),
ENDLOCATION VARCHAR(30),
TRAVELTIME VARCHAR(15),
TRAVELMODE VARCHAR(15),
NOOFPASSENGER TINYINT)
BEGIN
     INSERT INTO aasv_travel(USERID,CURRLOCATION,STARTLOCATION,ENDLOCATION,TRAVELTIME,TRAVELMODE,NOOFPASSENGER,DATCRETRAVEL,ISCONFIRMED,ISDELETED)
       VALUES(USERID,CURRLOCATION,STARTLOCATION,ENDLOCATION,TRAVELTIME,TRAVELMODE,NOOFPASSENGER,NOW(),0,0);
     SELECT @ROWCOUNT;       
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `aasv_travel_archives`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_travel_archives`()
BEGIN

      DECLARE TRAVELDETID INT;
      DECLARE SELTRAVELID INT;
      DECLARE not_found_travel INT DEFAULT 0;

      -- declare cursor to fetch all trevel which are passed by last three hours
      DEClARE travel_cursor CURSOR FOR
      SELECT TRAVELID FROM aasv_travel where DATCRETRAVEL<= DATE_SUB(now(),INTERVAL 3 HOUR) AND ISDELETED=0;

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


DROP PROCEDURE IF EXISTS `aasv_user_getloginuserdata`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_user_getloginuserdata`(
MOBILENO VARCHAR(11)
)
BEGIN
    /*SELECT USERID,UFNAME,ULNAME,GENDER,AGE,UCONTACTNO FROM aasv_user WHERE  ULOGIN=LOGIN  AND UPASSWORD=USERPASSWORD;*/
    SELECT USERID,UFNAME,ULNAME,GENDER,AGE,UCONTACTNO,CITYID FROM aasv_user WHERE  UCONTACTNO=MOBILENO;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `aasv_user_getusercitylocalites`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_user_getusercitylocalites`(LOGINUSERID INT)
BEGIN
      SELECT LOCALITYID,LOCALITY FROM aasv_citylocalities
      INNER JOIN aasv_city ON aasv_city.CITYID=aasv_citylocalities.CITYID
      AND EXISTS(SELECT 1 FROM aasv_user WHERE aasv_user.USERID=LOGINUSERID
      AND aasv_user.CITYID=aasv_city.CITYID);
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `aasv_user_insert`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_user_insert`(FNAME VARCHAR(20),
LNAME VARCHAR(20),
CITYNAME VARCHAR(20),
SEX SMALLINT,
AGE SMALLINT,
MOBILENO VARCHAR(11))
BEGIN

DECLARE GETCITYID INT;
SET GETCITYID= (SELECT CITYID FROM aasv_city WHERE city=CITYNAME);

INSERT INTO aasv_user(UFNAME,ULNAME,GENDER,AGE,UCONTACTNO,DATCREUSER,CITYID)
VALUES(FNAME,LNAME,SEX,AGE,MOBILENO,NOW(),GETCITYID);

CALL aasv_user_getloginuserdata(MOBILENO);

END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `aasv_user_travelconfirm`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_user_travelconfirm`(
CURUSERTRAVELID INT,USERTRAVELID INT,CURUSERID INT,TRAVELERUSERID INT)
BEGIN
      UPDATE aasv_travel SET  ISCONFIRMED=1,CONFIRMDAT=NOW() WHERE TRAVELID=CURUSERTRAVELID;
      UPDATE aasv_travel SET  ISCONFIRMED=1,CONFIRMDAT=NOW() WHERE TRAVELID=USERTRAVELID;
      INSERT INTO aasv_travelconfirmdet(TRAVELID,MAPEDTRAVELID,CONFIRMEDDAT,ARCHIVE,ARCHIVEDATE,USERID,MAPEDUSERID)
      VALUES(CURUSERTRAVELID,USERTRAVELID,NOW(),0,NULL,CURUSERID,TRAVELERUSERID);
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `aasv_user_traveldelete`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_user_traveldelete`(SELTRAVELID INT)
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

DROP PROCEDURE IF EXISTS `aasv_user_travelmatch`;

DELIMITER $$

/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE  PROCEDURE `aasv_user_travelmatch`(LOGINUSERID INT)
BEGIN

      /*Delete travels which are passed in last three hours*/
      CALL aasv_travel_archives();
       
      SELECT otherusers.TRAVELID,matchusers.USERID,matchusers.UCONTACTNO,otherusers.TRAVELID,matchusers.UFNAME ,matchusers.ULNAME,matchusers.GENDER,matchusers.AGE,matchusers.UCONTACTNO,
        otherusers.CURRLOCATION,otherusers.STARTLOCATION,otherusers.ENDLOCATION,otherusers.STARTLOCATION,otherusers.NOOFPASSENGER,otherusers.TRAVELMODE,otherusers.TRAVELTIME,
      otherusers.USERID=LOGINUSERID AS ISSELFPLAN, otherusers.ISCONFIRMED
      FROM aasv_travel otherusers
      INNER JOIN aasv_user matchusers ON matchusers.USERID=otherusers.USERID
      WHERE otherusers.ISDELETED=0 AND EXISTS(SELECT 1 FROM aasv_travel connecteduser
            WHERE  connecteduser.ISDELETED=0 AND connecteduser.CURRLOCATION=otherusers.CURRLOCATION AND
        connecteduser.ENDLOCATION =otherusers.ENDLOCATION AND
      connecteduser.USERID=LOGINUSERID ANd connecteduser.NOOFPASSENGER +otherusers.NOOFPASSENGER
       <= (SELECT NOFPASSENGER FROM aasv_travelmode WHERE aasv_travelmode.TYPE =otherusers.TRAVELMODE))
      ORDER BY ISSELFPLAN DESC,ISCONFIRMED DESC;
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

DELIMITER ;