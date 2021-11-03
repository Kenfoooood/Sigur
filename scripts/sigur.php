<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());


$cid = $_GET["cid"];
$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
$json = $json["name"];
mysqli_query($connection, "SET NAMES utf8");
foreach ($json as $value) {
	
	$id = $value['id'];
	$parent_id = $value['parent_id'];
	$type = $value['type'];
	$name = $value['name'];
	$desc = $value['description'];
	$sms_targetnumber = $value['sms_targetnumber'];
	$status = $value['status'];
	$codekey = $value['codekey'];
	
	$strSQL = "INSERT INTO `personal` (`CID`, `ID`, `PARENT_ID`, `TYPE`, `NAME`, `DESCRIPTION`, `SMS_TARGETNUMBER`, ";
	$strSQL = $strSQL . "`STATUS`, `CODEKEY`) VALUES (";
	$strSQL = $strSQL . "'" .$cid ."', ";
	$strSQL = $strSQL . "'" .$id ."', ";
	$strSQL = $strSQL . "'" .$parent_id ."', ";
	$strSQL = $strSQL . "'" .$type ."', ";
	$strSQL = $strSQL . "'" .$name ."', ";
	$strSQL = $strSQL . "'" .$desc ."', ";
	$strSQL = $strSQL . "'" .$sms_targetnumber ."', ";
	$strSQL = $strSQL . "'" .$status ."', ";
	$strSQL = $strSQL . "'" .$codekey ."')";

	mysqli_query($connection, $strSQL);
}

//$codekey = "CAST(" .$value['codekey']. " AS BINARY(8))";
//$strSQL = $strSQL .$codekey .")";

mysqli_close($connection);
?>
