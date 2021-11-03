<?php
$connection =mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());


/*$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
$cid = $json["cid"];
echo $cid;*/
$cid = $_GET["cid"];

$strSQL = "SELECT CID, CMD, DATA FROM `commands` WHERE CID = ". $cid;

$arr = array();


mysqli_real_query($connection, $strSQL);
$res = NULL;
if ($res = mysqli_store_result($connection)) {
    while ($row = mysqli_fetch_row($res)) {
		$arr[] = $row;
    }
}

$delete = "DELETE FROM `commands` WHERE CID = ". $cid;
mysqli_query($connection, $delete);
mysqli_close($connection);
$obj = array('arr' => $arr);
echo json_encode($obj);





?>