<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());

//$cid = $_GET["cid"];
$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
mysqli_query($connection, "SET NAMES utf8");
$parent_id = $value['parent_id'];
$name = $value['name'];


$strSQL = "SELECT `ID`,`NAME`, `SMS_TARGETNUMBER`, `STATUS`, `CODEKEY` FROM `personal` WHERE PARENT_ID = ";
$strSQL = $strSQL . "'" . $parent_id ."', ";
$strSQL = $strSQL . "NAME = ";
$strSQL = $strSQL . "'" . $name ."'";

mysqli_real_query($connection, $strSQL);
$arr = array();
if ($res = mysqli_store_result($connection)) {
    while ($row = mysqli_fetch_row($res)) {
		for ($i = 0; $i < mysqli_num_fields($res); $i++) {
			$arr[$i] = $row[$i];		
		}
    }
}
mysqli_close($connection);
$obj = array('id' => $arr[0], 'name' => $arr[1], 'sms_targetnumber' => $arr[2], 'status' => $arr[3], 'codekey' => $arr[4]);
echo json_encode($obj);
?>