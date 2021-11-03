<?php
$connection = mysqli_connect("localhost", "awork1vm_sigur", "0oS5mT*Z", "awork1vm_sigur") or die (mysqli_connect_error ());

//$cid = $_GET["cid"];
$inputJSON = file_get_contents('php://input');
$json = json_decode($inputJSON, TRUE);
mysqli_query($connection, "SET NAMES utf8");
//$name = $json["college"];
$cid = $json["cid"];
$login = $json["login"];
$password = $json["password"];



$arr = array();
$response = "SELECT `CID`, `LOGIN`, `PASSWORD`, `SEC_LOGIN`, `SEC_PASSWORD` FROM `colleges` WHERE CID = ";
$response = $response . "'". $cid ."' AND (";
$response = $response . "LOGIN = " . "'". $login ."' AND ";
$response = $response . "PASSWORD = " . "'". $password ."' OR ";
$response = $response . "SEC_LOGIN = " . "'". $login ."' AND ";
$response = $response . "SEC_PASSWORD = " . "'". $password ."')";


mysqli_real_query($connection, $response);
$res = NULL;
if ($res = mysqli_store_result($connection)) {
    while ($row = mysqli_fetch_row($res)) {
			$arr[] = $row;
    }
}
mysqli_close($connection);
// $obj = array('arr' => $arr);
// echo json_encode($obj);
$empty = 0;
if(count($arr) > 0) {
    if (strcmp($arr[0][1], $login) == 0 && strcmp($arr[0][2], $password) == 0) {
        $empty = 1;
        echo 2;
        exit;
    }
    if (strcmp($arr[0][3], $login) == 0 && strcmp($arr[0][4], $password) == 0) {
        $empty = 1;
        echo 1;
    }
    if ($empty == 0) {
        echo 0;
    }
}

else {
    echo 0;
}


// if(count($arr) > 0) {
//     if (strcmp($arr[0][1], $login) != 0) {
//         echo 1;
//     }
//     else {
//         echo 2;
//     }
// }
// else {
//     echo 0;
// }
?>