<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	$LOCALITYNAME = $jsonInput->LOCALITYNAME;
	$CITYNAME = $jsonInput->CITYNAME;
	$count = Count_Record('aasv_citylocalities','LOCALITYID','LOCALITY="'.$LOCALITYNAME.'"');
	if($count <= 0)
	{
		$queryResult =$db->Query("CALL aasv_citylocalities_insert('".$LOCALITYNAME."','".$CITYNAME."')");
		if(mysql_num_rows($queryResult) > 0)
		{
			$result['RESULT'] = 'OK';
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