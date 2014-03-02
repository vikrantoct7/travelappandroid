<?php
include "include/config.php";
include "include/functions_db.php";

$result=array();
try
{
	
	$jsonInput = json_decode(filter_var(file_get_contents('php://input'), FILTER_UNSAFE_RAW)); 
	$DATATYPE = $jsonInput->DATATYPE;
	if ($DATATYPE =='TRAVELMODE')
	{
		$queryResult = $db->Query("CALL aasv_travelmode_getall()");
		if(mysql_num_rows($queryResult) > 0)
		{
			$result['RESULT'] = 'OK';
			$resultSet= array();
			while($row = mysql_fetch_array($queryResult)){
				$resultSet[]=$row;
			}
			$result['DATAARRAY'] = $resultSet;
		}
		else
		{
			$result['RESULT'] = 'KO';
			//Error code to define record is not exist
			$result['ERRORCODE']="101";
		}
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