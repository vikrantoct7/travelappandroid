<?php // CONFIG FILE
define("DB_HOST","localhost");
define("DB_USER","root");
define("DB_PASSWORD","sa");
define("DB_DATABASE","mindyourtravel");	
ob_start();
//session_start();
// DB VARIABLES
$conf["PREFIX"]="tbl_";
class DB{
	var $cn;
	var $result;
	function DB(){
		$DbHost=DB_HOST;
		$DbUserName=DB_USER;
		$DbPassword=DB_PASSWORD;
		$DbName=DB_DATABASE;
		$this->cn=@mysql_connect($DbHost,$DbUserName,$DbPassword);
		mysql_select_db($DbName,$this->cn) or Site_Error("Configuration");
	}
	function Limit($offset){mysql_data_seek($this->result,$offset) or die(display_error(mysql_error(), mysql_errno(), $sql));}
	function InsertId(){return mysql_insert_id($this->cn);}
	function Query($sql){//echo $sql."<br /><br />";
		 $this->result=mysql_query($sql,$this->cn);
		 if(!$this->result)
		 {
			throw new Exception(mysql_error());
		 }
	return $this->result;
	}
	function NumRow(){return mysql_num_rows($this->result);}
	function getData(){ if($rs=mysql_fetch_array($this->result)) return($rs); else return(false); }
	function getObject(){ if($rs=mysql_fetch_object($this->result)) return($rs); else  return(false); }
	function getAssoc(){ if($rs=mysql_fetch_assoc($this->result)) return($rs); else  return(false); }
	function getResult($pos, $field){return mysql_result($this->result,$pos,$field);}
	function loadObjectList( $key='' ) {
		if(!($cur = $this->result)) return null;
		$array = array();
		while($row = mysql_fetch_object($cur)){
			if($key) $array[$row->$key] = $row;
			else $array[] = $row;
		}
		mysql_free_result( $cur );
		return $array;
	}
	function FreeResult(){if(isset($this->result)) mysql_free_result($this->result);}
	function Close(){
		if($this->cn) mysql_close($this->cn);
	}
	function __destruct(){
		if($this->cn) mysql_close($this->cn);
	}
	function GetField($p){return mysql_fetch_field($this->result, $p);}
	function NumField(){return mysql_num_fields($this->result);}
}
function display_error($error, $error_num, $query){
	$query='';
	if($query) {// Safify query
		$query = preg_replace("/([0-9a-f]){32}/", "********************************", $query); // Hides all hashes
		$query_str = "$query";
	}
	echo '<font style="font-size: 11px; font-family: tahoma"><hr noshade color="#ECECEC"/> 
		&nbsp;&nbsp;<font color="#FF0000"><strong>ERROR OCCURED: </strong></font> <strong>'.$error.'</strong></font>
		<hr noshade color="#ECECEC"/>';
	exit();
}

class XMLParser{
    private $xml;
    public function __construct($xmlString='default_xml_string'){
        if(!is_string($xmlString)){
            throw new Exception('Invalid XML string.');
        }
        // read XML string
        if(!$this->xml=simplexml_load_string($xmlString)){
            throw new Exception('Error reading XML string.');
        }
    }
    // fetch specific nodes according to node name
    public function fetchNodes($nodeName){
        if(!$nodeName){
            throw new Exception('Invalid node name.');
        }
        $nodes=array();
        foreach($this->xml as $node){
            $nodes[]=$node->$nodeName;
        }
        return $nodes;
    }
    // fetch all nodes as array of objects
    public function fetchNodesAsObjects(){
        $nodes=array();
        foreach($this->xml as $node){
            $nodes[]=$node;
        }
        return $nodes;
    }
    // count nodes of type $nodeName
    public function countNodes($nodeName){
        if(!$nodeName){
            throw new Exception('Invalid node name.');
        }
        $nodeCounter=0;
        foreach($this->xml as $node){
            $nodeCounter++;
        }
        return $nodeCounter;
    }
}

// DEFINE VARIABLES
$db=new DB();
global $db;
