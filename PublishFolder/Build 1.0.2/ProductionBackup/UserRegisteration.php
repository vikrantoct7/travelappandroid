<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	$UFNAME = $jsonInput->UFNAME;
	$ULNAME = $jsonInput->ULNAME;
	$CITY = $jsonInput->CITY;
	$GENDER = $jsonInput->GENDER;
	$AGE = $jsonInput->AGE;
	$UCONTACTNO = $jsonInput->UCONTACTNO;
	$count = Count_Record('aasv_user','USERID','UCONTACTNO="'.$UCONTACTNO.'"');
	if($count <= 0)
	{
		$sql =$db->Query("CALL aasv_user_insert('".$UFNAME."','".$ULNAME."','".$CITY."','".$GENDER."','".$AGE."','".$UCONTACTNO."')");
		$result['RESULT'] = 'OK';
		while($row = mysql_fetch_array($sql)){
			$result['USERDATA'] = $row;
		}
		$db->FreeResult();
	}
	else
	{
		$result['RESULT'] = 'KO';
		//Error code to define record already exist
		$result['ERRORCODE']='102';
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