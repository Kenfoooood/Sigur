<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());

$cid = $_GET["cid"];

mysqli_query($connection, "SET NAMES utf8");


$response = "SELECT `ID` FROM `personal` WHERE CID = ";
$response = $response . "'" .$cid . "'";
$response = $response . "order by id desc limit 1";

$arr = "0";
mysqli_real_query($connection, $response);
if ($res = mysqli_store_result($connection)) {
    while ($row = mysqli_fetch_row($res)) {
			$arr = $row[0];
    }
}
mysqli_close($connection);

echo $arr;

?>