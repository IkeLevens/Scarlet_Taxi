<?php

require_once('../htdocs-include/credentials.php');

$con= mysql_connect($host, $username, $password);

// Check connection
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }


$db_selected = mysql_select_db($database, $con);

if (!$db_selected)
  {
  die ("Can\'t use test_db : " . mysql_error());
  }

$origin = $_POST['pickUpLoc'];
$destination = $_POST['destAddress'];
$departure = $_POST['departure'];
$seatsTaken = $_POST['openSeats'];

mysql_query("INSERT INTO rides (car, origin, destination, toCampus, departure, seatsTaken)
VALUES (1, '$origin', '$destination', 1, '$departure', '$seatsTaken')", $con) or die ('Error updating database: '.mysql_error());

echo "Added a new ride to the database.";

mysql_close($con);
?>
