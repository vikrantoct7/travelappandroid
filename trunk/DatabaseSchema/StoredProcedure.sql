﻿--
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