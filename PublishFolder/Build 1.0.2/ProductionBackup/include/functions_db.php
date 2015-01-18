<?php
///********************************************************///
/**** This function use to count total number of record in 
database.***/
///********************************************************///

function Count_Record($table, $ctr_field, $condn = ''){
	global $db;
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select count(".$ctr_field.") as tot_rec from ".$table." ".$condn; //echo $query;
	
	$db -> Query($query);
	$tot_rec = $db -> getResult(0, 'tot_rec');
	$db -> FreeResult();
	return $tot_rec;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch field name in database***/
///********************************************************///
function Get_Images($table, $field, $condn){
	global $_conf_vars, $db;
	$img_arr = array();
	if(!empty($condn)) $condn = " where ".$condn;
	$db -> Query("Select ".$field." from ".prefix($table)." ".$condn);
	if($db -> NumRow() > 0){
		while($row = $db -> getAssoc()) $img_arr[] = $row;
	}
	$db -> FreeResult();
	return $img_arr;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch one column  value 
in database***/
///********************************************************///
function Retrieve_Field($table, $field, $condn = ''){
	global $db, $_conf_vars;
	$val = '';
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select ".$field." from ".$table." ".$condn; //\\\echo "<br /><br />".$query;
	$db -> Query($query);
	if($db -> NumRow() > 0){ $row = $db -> getData(); $val = StripSlash($row[0]); }
	$db -> FreeResult();
	return $val;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch one column  value 
in database***/
///********************************************************///
function Show_List($query, $name, $sel_val = '', $select = '', $attrib = ''){
	global $_conf_vars, $db;
	$list = '<select name="'.$name.'" id="'.$name.'" '.$attrib.'>';
	if(!empty($select)) $list .= chr(13).'<option value="">'.$select.'</option>'.chr(13);
	$db -> Query($query);
	if($db -> NumRow() > 0){
		while($row = $db -> getData()){
			if(!is_array($sel_val) && $row[0] == $sel_val) $selected = 'selected="selected"';
			elseif(is_array($sel_val) && in_array($row[0], $sel_val)) $selected = 'selected="selected"';
			else $selected = '';
			$list .= '<option value="'.$row[0].'" '.$selected.'>'.StripSlash($row[1]).'</option>'.chr(13);
		}
	}
	$list .= '</select>';
	$db -> FreeResult();
	return $list;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch single row in database***/
///********************************************************///
function Get_Assoc($table, $fields, $condn = ''){
	global $db, $_conf_vars;
	$row = array();
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select ".$fields." from ".$table." ".$condn; //=echo $query."<br /><br />";

	$db -> Query($query);
	if($db -> NumRow() > 0){ $row = $db -> getAssoc(); }
	$db -> FreeResult();
	return $row;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch  records in array 
format in database***/
///********************************************************///
function Get_Array($table, $fields, $condn = ''){
	global $db, $_conf_vars;
	$row = array();
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select ".$fields." from ".$table." ".$condn;
	$db -> Query($query); //echo $query;echo '<br/>';
	if($db -> NumRow() > 0){ while($rs = $db -> getData()) $row[] = $rs[0]; }
	$db -> FreeResult();
	return $row;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch an records in array 
format in database***/
///********************************************************///
function Get_An_Array($table, $field, $condn = ''){
	global $db, $_conf_vars;
	$row = array();
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select ".$field." from ".$table." ".$condn; //echo $query;
	$db -> Query($query);
	if($db -> NumRow() > 0){ while($rs = $db -> getData()) $row[] = $rs[0]; }
	$db -> FreeResult();
	return $row;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch records in object 
format in database***/
///********************************************************///
function Get_Records($table, $fields, $condn = ''){
	global $db, $_conf_vars;
	$row = array();
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select ".$fields." from ".$table." ".$condn; //echo "<br /><br />".$query;
	$db -> Query($query); 
	if($db -> NumRow() > 0){ while($rs = $db -> getAssoc()) $row[] = $rs; }
	$db -> FreeResult();
	return $row;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch records in array format  
format in database for smarty***/
///********************************************************///
function Smarty_Arr($table, $fields, $condn = ''){
	global $db, $_conf_vars;
	$row = array();
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select ".$fields." from ".$table." ".$condn; //echo $query;
	$db -> Query($query);
	if($db -> NumRow() > 0){ while($rs = $db -> getData()) $row[$rs[0]] = $rs[1]; }
	$db -> FreeResult();
	return $row;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to Generate OrderNo  ***/
///********************************************************///
function Generate_OrderNo($user_id){
	global $db;
	$order_no = randomPrefix(12);
	$ord_arr = Get_An_Array(prefix("user_payment"), "order_no");
	if(!empty($ord_arr)){
		while(in_array($order_no, $ord_arr)) $order_no = $user_id.randomPrefix(8);
	}
	return $order_no;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to field name in database also assign 
 in array ***/
///********************************************************///
function Declare_Variable($table, $condn = ''){
	global $db;
	$arr = array();
	if(!isEmpty($condn)) $condn = " and ".$condn;
	$db -> Query("Select * from ".$table." ".$condn." limit 0, 1");
	for($f=0; $f < $db -> NumField(); $f++){
		$field_name = $db -> GetField($f);
		$field_name = $field_name -> name;
		$arr[$field_name] = "";
	}
	$db -> FreeResult();
	return $arr;
}
///********************************************************///
///End
///********************************************************///


///********************************************************///
/****This function use to check valid user name without space ***/
///********************************************************///
function Valid_Username($username){
	global $db, $_conf_vars;
	$char_arr = array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_');
	$flag = 1;
	for($s=0; $s < strlen($username); $s++){
		$char = $username.substr($s, 1);
		if(!in_array(+$char, $char_arr)){//echo $char."<br />";
			$flag = 0;
			break;
		}
	}
	if($flag == 0) return false;
	else return true;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to check user login or not ***/
///********************************************************///
function checkUserLogin(){
	if(isset($_SESSION["USERNAME"]) && !isEmpty($_SESSION["USERNAME"])){
		$user_id = Retrieve_Field(prefix("user"), "user_id", "user_email = '".$_SESSION['USERNAME']."' and user_status = '1'");
		return $user_id;
	}else{ return false; }
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to return user id if user is logged in ***/
///********************************************************///
function isLogin($next_page = ''){
	global $_conf_vars, $db;
	$clt_id = checkUserLogin();
	if(isEmpty($clt_id)){
		if(!isEmpty($next_page)) $_SESSION['NEXT_PAGE'] = $next_page;
		Redirect_To($_conf_vars['USER']."/".$_conf_vars['LOGIN'].$_conf_vars['FILE_EXTN']);
	}
	else return $clt_id;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to fetch specific field name in database***/
///********************************************************///
function Specified_Field($table, $field, $condn = ''){
	global $db, $_conf_vars;
	$val = '';
	if(!empty($condn)) $condn = " where ".$condn;
	$query = "Select ".$field." from ".$table." ".$condn; #echo "<br /><br />".$query;
	//echo $query;
	$db -> Query($query);
	if($db -> NumRow() > 0)
	{
		for($i=0;$i<$db -> NumRow();$i++)
		{
			$resultSet[]  = $db -> getData(); 
		}

	}
	$db -> FreeResult();
	return $resultSet;
}
///********************************************************///
///End
///********************************************************///


///********************************************************///
/****This function use to fetch category in database***/
///********************************************************///
function Get_category()
{
	global $db, $_conf_vars;
	$cat_arr = Get_Records(prefix('category').' c ' , 'c.cat_id,c.cat_name',  "  parent_id = '0' ");
	if(!empty($cat_arr)){
		for($cc=0; $cc < sizeof($cat_arr); $cc++)
		{
			$cat_arr[$cc]['sub_arr'] = Get_Records(prefix('category'),'cat_name, cat_id',"parent_id = '".$cat_arr[$cc]['cat_id']."' order by cat_id");
		}
	}
	return $cat_arr;
}
///********************************************************///
///End
///********************************************************///


///********************************************************///
/****This function use to update record in database***/
///********************************************************///
function Update_Data($table, $query, $cond){
	global $db;
	if($cond !='')
	{
		$sql_query = "UPDATE ".$table." set ".$query." where ".$cond;
		if($db -> Query($sql_query))
			return true;
		else
			return false;
	}
	return false;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to insert record in database***/
///********************************************************///
function Insert_Data($table, $field){
	global $db;
	$status ='';
	$sql_query = "INSERT INTO ".$table." set ".$field;
	if($db -> Query($sql_query))
		$status = "true";
	else
		$status = "false";

	return $status;
}
///********************************************************///
///End
///********************************************************///

///********************************************************///
/****This function use to delete record in database***/
///********************************************************///
function Delete_Data($table,$cond){
	global $db;
	if($cond !='')
	{
		$sql_query = "DELETE FROM ".$table." where ".$cond;
		//echo "<br/>";
		
		if($db -> Query($sql_query))
			return true;
		else
			return false;
	}
	return false;
}
///********************************************************///
///End
///********************************************************///
?>
