<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	$USERID = $jsonInput->USERID;
	$CURRLOCATION = $jsonInput->CURRLOCATION;
	$STARTLOCATION = $jsonInput->STARTLOCATION;
	$ENDLOCATION = $jsonInput->ENDLOCATION;
	$TRAVELTIME = $jsonInput->TRAVELTIME;
	$TRAVELMODE = $jsonInput->TRAVELMODE;
	$NOOFPASSENGER = $jsonInput->NOOFPASSENGER;
	$queryResult = $db->Query("CALL aasv_travelplan_insert('".$USERID."','".$CURRLOCATION."','".$STARTLOCATION."','".$ENDLOCATION."','".$TRAVELTIME."','".$TRAVELMODE."','".$NOOFPASSENGER."')");
	if(mysql_num_rows($queryResult) > 0)
	{
		$result['RESULT'] = 'OK';
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