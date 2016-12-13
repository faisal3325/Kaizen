<?php
if($_SERVER['REQUEST_METHOD'] == 'POST' ) {

  $servername = "localhost";
$username = "nagpurst_kaizen";
$password = "faisalkhan123";
$dbname = "nagpurst_location";

//create connection
$connection = new mysqli($servername, $username, $password, $dbname);
//check connection
if($connection->connect_error)
    die("Connection failed: ".$conn->connect_error);

    $lati = $_POST['ilat'];
    $lngi = $_POST['ilng'];
    $soundi = $_POST['isound'];

    // mysql inserting a new row
    $result = "INSERT INTO location (lat, lng, sound) VALUES ('$lati', '$lngi', '$soundi')";

    if ($connection->query($result) === TRUE)	echo 1;
    else	echo 0;//"Error: " . $sql . "<br>" . $conn->error;
    $connection->close;
}
?>
