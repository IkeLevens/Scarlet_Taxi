<!DOCTYPE html>
<html>

<head>
	<?php include 'header.php'; ?>
	<script>
		function mainPage()
		{
			window.location = '/main.php';   
		}
	</script>
</head>

<body>

<?php

include "cookie.php";

if(empty($_COOKIE['USER_LOGGED_IN'])){
	echo"<h2>Please log in to view this page.</h2>";
} else if (isset($_COOKIE['USER_LOGGED_IN'])){
	include 'loggedInNav.php';
}

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

$userId = $_COOKIE['USER_LOGGED_IN'];
$rideId = $_POST['rideID'];
$timeSent = date('H:i:s');
$comment = $_POST['commentText'];

mysql_query("INSERT INTO requests (requestingUser, ride, sent, requestComment, requestStatus)
VALUES ('$userId', '$rideId', '$timeSent', '$comment', 'p')", $con) or die ('Error updating database: '.mysql_error());

//Create new notification for request
mysql_query("INSERT INTO requestNotifications (request, notificationType)
VALUES (LAST_INSERT_ID(), 'R')", $con) or die ('Error updating database: '.mysql_error());


mysql_close($con);
?>

<div class="container">
<h1> Request Successfully Sent! </h1>
<br>
<form action"searchAvailableRides.php">
	<button class="btn btn-primary" type="button" onclick="mainPage()">Back to Main Page</button>
</form>
</div>

</body>
</head>
