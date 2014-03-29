<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	$CURUSERTRAVELID = $jsonInput->CURUSERTRAVELID;
	$USERTRAVELID = $jsonInput->USERTRAVELID;
	$CONUSERID = $jsonInput->CONUSERID;
	
	$queryResult = $db->Query("CALL aasv_user_travelconfirm('".$CURUSERTRAVELID."','".$USERTRAVELID."')");
	$db->ReConnect();
	$db->Query("CALL aasv_user_travelmatch('".$CONUSERID."')");
	$result['TRAVELLIST'] = $db->loadObjectList();
	if(count($result)>0)
	{
		$result['RESULT'] = 'OK';
	}
	else
	{
		$result['RESULT'] = 'KO';
		//Error code to define record is not exist
		$result['ERRORCODE']="101";
	}
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