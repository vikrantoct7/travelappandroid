<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	$LOGGEDINUSERID = $jsonInput->LOGGEDINUSERID;
	$STARTLOCATIONCITY = $jsonInput->STARTLOCATIONCITY;
	$ENDLOCATIONCITY = $jsonInput->ENDLOCATIONCITY;
	$db->Query("CALL aasv_travelmode_getall()");
	$result['TRAVELMODE'] = $db->loadObjectList();
	$db->ReConnect();
	$db->Query("CALL aasv_user_getusercitylocalites('".$LOGGEDINUSERID."','".$STARTLOCATIONCITY."')");
	$result['STARTCITYLOCALITES'] = $db->loadObjectList();
	
	if($STARTLOCATIONCITY != $ENDLOCATIONCITY)
	{
		$db->ReConnect();
		$db->Query("CALL aasv_user_getusercitylocalites('".$LOGGEDINUSERID."','".$ENDLOCATIONCITY."')");
		$result['ENDCITYLOCALITES'] = $db->loadObjectList();
	}
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