<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());

//$cid = $_GET["cid"];
$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
$radioId = $json["radioId"];
$data = $json["data"];
$cid = $json["cid"];

if($radioId == 1) {
    $strSQL = "SELECT `ID`, `NAME`, `SMS_TARGETNUMBER`, `STATUS`, `CODEKEY` FROM `personal` WHERE NAME LIKE ";
    $strSQL = $strSQL . "'%" . $data ."%'";
    $strSQL = $strSQL . " AND CID = " . "'" . $cid ."'";

}
if($radioId == 2) {
    $strSQL = "SELECT `ID`, `NAME`, `SMS_TARGETNUMBER`, `STATUS`, `CODEKEY` FROM `personal` WHERE SMS_TARGETNUMBER = ";
    $strSQL = $strSQL . "'" . $data ."'";
    $strSQL = $strSQL . " AND CID = " . "'" . $cid ."'";
}
if($radioId == 3) {
    $strSQL = "SELECT `ID`, `NAME`, `SMS_TARGETNUMBER`, `STATUS`, `CODEKEY` FROM `personal` WHERE CODEKEY = ";
    $strSQL = $strSQL . "'" . $data ."'";
    $strSQL = $strSQL . " AND CID = " . "'" . $cid ."'";
}

mysqli_real_query($connection, $strSQL);



$arr = array();
if ($res = mysqli_store_result($connection)) {
    while ($row = mysqli_fetch_assoc($res)) {
		$arr[] = $row;		
    }
}
mysqli_close($connection);

if (count($arr) > 0) {
    $obj = array('arr' => $arr);
    echo json_encode($obj);
}
else {
    echo "{\"arr\":[]}" ;
}

// if (count($arr) > 0) {
//     $obj = array('id' => $arr[0], 'name' => $arr[1], 'sms_targetnumber' => $arr[2], 'status' => $arr[3], 'codekey' => $arr[4]);
//     echo json_encode($obj);
// }
// else {
//     echo "{\"arr\":[]}" ;
// }

?>