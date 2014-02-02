<?php
// Prevent caching.
//header('Cache-Control: no-cache, must-revalidate');
//header('Expires: Mon, 01 Jan 1996 00:00:00 GMT');
// The JSON standard MIME header.
//header('Content-type: application/json');
// This ID parameter is sent by our javascript client.
$name = $_POST['name'];
$age = $_POST['age'];
$place = $_POST['place'];
$marks = $_POST['marks'];
// Here's some data that we want to send via JSON.
// We'll include the $id parameter so that we
// can show that it has been passed in correctly.
// You can send whatever data you like.
//$data = array("Name", $id);
$arr = array('name' => $name, 'age' => $age, 'place' => $place, 'marks' => $marks);
// Send the data.
echo json_encode($arr);
?>
