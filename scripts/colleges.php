<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());

//$cid = $_GET["cid"];
$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
mysqli_query($connection, "SET NAMES utf8");
$name = $json["college"];
$login = $json["login"];
$password = $json["password"];
$sec_login = $json["sec_login"];
$sec_password = $json["sec_password"];



$arr = array();
$strSQL1 = "INSERT INTO `colleges` (`NAME`, `LOGIN`, `PASSWORD`, `SEC_LOGIN`, `SEC_PASSWORD`) VALUES (";
$strSQL1 = $strSQL1 . "'" .$name ."', ";
$strSQL1 = $strSQL1 . "'" .$login ."', ";
$strSQL1 = $strSQL1 . "'" .$password ."', ";
$strSQL1 = $strSQL1 . "'" .$sec_login ."', ";
$strSQL1 = $strSQL1 . "'" .$sec_password ."')";
mysqli_query($connection, $strSQL1);


mysqli_close($connection);

?>
