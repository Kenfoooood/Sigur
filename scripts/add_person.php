<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());


$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
$cid = $json["cid"];
$cmd = $json["cmd"];
$parent_id = $json["parent_id"];
$name = $json["name"];

mysqli_query($connection, "SET NAMES utf8");

$data = array('parent_id' => $parent_id, 'name' => base64_encode($name));

$strSQL = "INSERT INTO `commands` (`cid`, `cmd`, `data`) VALUES (";
$strSQL = $strSQL . "'" . $cid ."', ";
$strSQL = $strSQL . "'" . $cmd ."', ";
$strSQL = $strSQL . "'" . json_encode($data) ."')";


mysqli_real_query($connection, $strSQL);
echo "ok";
// $res = NULL;
// if ($res = mysqli_store_result($connection)) {
//     while ($row = mysqli_fetch_row($res)) {
// 		$arr[] = $row;
//     }
// }
mysqli_close($connection);
?>