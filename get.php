<?php
if($_SERVER['REQUEST_METHOD'] == 'GET' ) {

  include_once "conn.php";
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $sound = $_POST['sound'];

    //mysql fetching data
    $sql = "SELECT lat, lng, sound FROM location";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            echo "Lat: " . $row["lat"]. " - Lng: " . $row["lng"]. " - Sound: " . $row["sound"]. "<br>";
        }
    } else {
        echo "0 results";
    }
    $conn->close
}
?>
