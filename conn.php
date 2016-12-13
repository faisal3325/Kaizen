<?php
$servername = "localhost";
$username = "nagpurst_kaizen";
$password = "faisalkhan123";
$dbname = "nagpurst_location";

//create connection
$conn = new mysqli($servername, $username, $password, $dbname);
//check connection
if($conn->connect_error)
    die("Connection failed: ".$conn->connect_error);
?>
