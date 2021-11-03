<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());

//$cid = $_GET["cid"];
//$inputJSON = file_get_contents('php://input');
//$json = json_decode($inputJSON, TRUE);
//mysqli_query($connection, "SET NAMES utf8");
//$name = $json["college"];
//$login = $json["login"];
//$password = $json["password"];

$arr = array();
$response = "SELECT `CID`, `NAME` FROM `colleges`";

mysqli_real_query($connection, $response);
if ($res = mysqli_store_result($connection)) {
    while ($row = mysqli_fetch_row($res)) {
			$arr[] = $row;
    }
}
mysqli_close($connection);

$obj = array('arr' => $arr);
echo json_encode($obj);
?>