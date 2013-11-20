<?php

require_once('../htdocs-include/credentials.php');

$con= mysqli_connect($host, $username, $password, $database);

// Check connection
        if (mysqli_connect_errno($con))
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$origin = $_POST['pickUpLoc'];
$destination = $_POST['destAddress'];
$departure = $_POST['departure'];
$seatsTaken = $_POST['openSeats'];

mysqli_query($con,"INSERT INTO rides (car, origin, destination, toCampus, departure, seatsTaken)
VALUES (1, '$origin', '$destination', 1, '$departure', '$seatsTaken')");

        mysqli_close($con);
?>
