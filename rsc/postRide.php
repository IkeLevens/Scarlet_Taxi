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

		if($_POST['AMorPM'] == "PM"){
			if($_POST['departureHour'] != "12"){
				$departHour = $_POST['departureHour'] + 12;
			}else{
				$departHour = $_POST['departureHour'];	
			}	
		}else{
			$departHour = $_POST['departureHour'];
		}	

		$car = $_POST['chooseCar'];
		$origin = $_POST['pickUpLoc'];
		$destination = $_POST['destAddress'];
		$departure = $departHour . $_POST['departureMin'] . "00";
		$availableSeats = $_POST['openSeats'];

		$originNum = mysql_query("SELECT locationID FROM locations WHERE locationName = '$origin'", $con);
		$orgID = mysql_fetch_array($originNum);
		$oID = $orgID['locationID'];

		$destinationNum = mysql_query("SELECT locationID FROM locations WHERE locationName = '$destination'", $con);
		$destID = mysql_fetch_array($destinationNum);
		$dID = $destID['locationID'];

		mysql_query("INSERT INTO rides (car, origin, destination, toCampus, departure, availableSeats)
		VALUES ($car, '$oID', '$dID', 1, '$departure', '$availableSeats')", $con) or die ('Error updating database: '.mysql_error());


		mysql_close($con);
		?>
		
		<?php 
			include 'cookie.php';
			if(empty($_COOKIE['USER_LOGGED_IN'])) {
				include 'loggedOutNav.php';

			} else if (isset($_COOKIE['USER_LOGGED_IN'])) { 
				include 'loggedInNav.php';
			
			} 
		?>
		<?php include 'footer.php'; ?>
		<div class="container">
			<h1>Ride Added Successfully!</h1>
			<br>
			<button class="btn btn-primary" type="button" onclick="mainPage()">Back to Main Page</button>
</form>
		</div>
	</body>
</html>