<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	//$ULOGIN = $jsonInput->ULOGIN;
	//$UPASSWORD = $jsonInput->UPASSWORD;
	$UMOBILENO = $jsonInput->UMOBILENO;
	//$queryResult = $db->Query("CALL aasv_user_getloginuserdata('".$ULOGIN."','".$UPASSWORD."')");
	$queryResult = $db->Query("CALL aasv_user_getloginuserdata('".$UMOBILENO."')");
	if(mysql_num_rows($queryResult) > 0)
	{
		$result['RESULT'] = 'OK';
		while($row = mysql_fetch_array($queryResult)){
			$result['USERDATA'] = $row;
		}
	}
	else
	{
		$result['RESULT'] = 'KO';
		//Error code to define record is not exist
		$result['ERRORCODE']="101";
	}
	$db->FreeResult();
}
catch(Exception $e)
{
   $result['RESULT'] = 'KO';
   //Error code to define technical error
   $result['ERRORCODE']="103";
   $result['ERRMSG']=$e->getMessage();
}
echo json_encode($result);
?>