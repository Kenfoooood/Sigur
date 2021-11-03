<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());


$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
$cid = $json["cid"];
$cmd = $json["cmd"];
$id = $json["id"];
$name = $json["name"];
$sms_targetnumber = $json["sms_targetnumber"];
$status = $json["status"];
$codekey = $json["codekey"];


mysqli_query($connection, "SET NAMES utf8");

$strSQL = "UPDATE `personal` SET `NAME` = ";
$strSQL = $strSQL . "'" . $name . "', ";
$strSQL = $strSQL . "`STATUS` = '" . $status. "', ";
$strSQL = $strSQL . "`SMS_TARGETNUMBER` = '". $sms_targetnumber . "', ";
$strSQL = $strSQL . "`CODEKEY` = '" . $codekey. "' ";
$strSQL = $strSQL . " WHERE ID = '" . $id. "' AND ";
$strSQL = $strSQL . "CID = '". $cid. "'";

echo $strSQL; 
 
mysqli_real_query($connection, $strSQL);
$status2 = "2";
if (strcmp($status, "AVAILABLE") == 0) {
    $status2 = "1";
}


$data = array('id' => $id, 'name' => base64_encode($name), 'sms_targetnumber' => $sms_targetnumber, 'status' => $status2, 'codekey' => $codekey);

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