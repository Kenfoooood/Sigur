<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());


$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
//$group = $_GET["group"];
$group = $json["group"];
mysqli_query($connection, "SET NAMES utf8");

$strSQL = "SELECT `ID` FROM `personal` WHERE NAME = ";
$strSQL = $strSQL . "'" . $group ."'";

mysqli_real_query($connection, $strSQL);
$arr = NULL;
if ($res = mysqli_store_result($connection)) {
    while ($row = mysqli_fetch_row($res)) {
		$arr = $row;
    }
}

mysqli_close($connection);
$obj = array('arr' => $arr);
echo json_encode($obj);
?>