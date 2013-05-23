<?php

// response json
use Application\Model\Rpc;

$json = array();

/**
 * Registering a user device
 * Store reg id in users table
 */
if (isset($_POST["name"]) && isset($_POST["email"]) && isset($_POST["regId"])) {
	//file_put_contents ( "elislog.txt" , "Register");
	
	
    $name = $_POST["name"];
    $email = $_POST["email"];
    $gcm_regid = $_POST["regId"]; // GCM Registration ID
    // Store user details in db
    include_once '../Rpc.php';
    include_once './GCM.php';

    //file_put_contents ( "elislog.txt" , "Register: ".$name." , ".$gcm_regid);
    
    $rpc = new Rpc($serviceLocator);
    $gcm = new GCM();

   //file_put_contents ( "elislog.txt" , "RCP: ".$name." , ".$gcm_regid);
    
    $res = $rpc->registerUser($name, $gcm_regid);

    $registatoin_ids = array($gcm_regid);
    $message = array("product" => "shirt");

    $result = $gcm->send_notification($registatoin_ids, $message);

    echo $result;
} else {
    // user details missing
}
?>
