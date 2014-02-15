DELIMITER $$

-- -----------------------------------------------------------------------------

DROP PROCEDURE IF EXISTS `mindyourtravel`.`aasv_user_insert` $$
/*!50003 SET @TEMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `aasv_user_insert`(FNAME VARCHAR(20),
LNAME VARCHAR(20),
LOGIN VARCHAR(15),
SEX BIT,
AGE SMALLINT,
UPASSWORD VARCHAR(15),
CONTACTNO VARCHAR(11))
BEGIN

      INSERT INTO aasv_user(UFNAME,ULNAME,ULOGIN,GENDER,AGE,UPASSWORD,UCONTACTNO,DATCREUSER,DATMODUSER,ISACTIVE)
       VALUES(FNAME,LNAME,LOGIN,SEX,AGE,UPASSWORD,CONTACTNO,NOW(),NOW(),0);
END $$
/*!50003 SET SESSION SQL_MODE=@TEMP_SQL_MODE */  $$

-- -----------------------------------------------------------------------------

DELIMITER ;