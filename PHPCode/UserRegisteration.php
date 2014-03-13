<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	$UFNAME = $jsonInput->UFNAME;
	$ULNAME = $jsonInput->ULNAME;
	//$ULOGIN = $jsonInput->ULOGIN;
	$GENDER = $jsonInput->GENDER;
	$AGE = $jsonInput->AGE;
	//$UPASSWORD = $jsonInput->UPASSWORD;
	$UCONTACTNO = $jsonInput->UCONTACTNO;
	//$count = Count_Record('aasv_user','USERID','ULOGIN="'.$ULOGIN.'"');
	$count = Count_Record('aasv_user','USERID','UCONTACTNO="'.$UCONTACTNO.'"');
	if($count <= 0)
	{
		$sql =$db->Query("CALL aasv_user_insert('".$UFNAME."','".$ULNAME."','".$GENDER."','".$AGE."','".$UCONTACTNO."')");
		$result['RESULT'] = 'OK';
		while($row = mysql_fetch_array($sql)){
			$result['USERDATA'] = $row;
		}
	}
	else
	{
		$result['RESULT'] = 'KO';
		//Error code to define record already exist
		$result['ERRORCODE']="102";
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