<!DOCTYPE html>

<html>
<head>
	<?php include 'header.php'; ?>
	<title>Scarlet Taxi - Available Rides</title>
	<script>
		function mainPage()
		{
			window.location = '/main.php';   
		}
	</script>
</head>

<body>

<?php
	include 'cookie.php';

	if(empty($_COOKIE['USER_LOGGED_IN'])){
		echo"<h2>Please log in to view this page.</h2>";
	} else if (isset($_COOKIE['USER_LOGGED_IN'])){
		include 'loggedInNav.php';
	}
?>


<div class="container">
<h1>Available Rides</h1>
	

	<!--Query for Rides-->

	<?php
		require_once('../htdocs-include/credentials.php');
		$con= mysql_connect($host, $username, $password);
		// Check connection
		if (!$con){
  			die('Could not connect: ' . mysql_error());
  		}
		$db_selected = mysql_select_db($database, $con);
		if (!$db_selected){
  			die ("Can\'t use test_db : " . mysql_error());
  		}


		if(isset($_POST['origins'])){//Grab campus of origin
			$orgCamp = $_POST['origins'];
			if(!is_numeric($orgCamp)){
				if($orgCamp == "busch"){
					$orgCamp = 2;
				}else if($orgCamp == "cac"){
					$orgCamp = 4;
				}else if($orgCamp == "cdoug"){
					$orgCamp = 5;
				}else{
					$orgCamp = 3;
				}	
			}
		}
		if(isset($_POST['destinations'])){//Grab campus of destination
			$destCamp = $_POST['destinations'];
			if(!is_numeric($destCamp)){
				if($destCamp == "busch"){
					$destCamp = 2;
				}else if($destCamp == "cac"){
					$destCamp = 4;
				}else if($destCamp == "cdoug"){
					$destCamp = 5;
				}else{
					$destCamp = 3;
				}	
			}
		}
	
		if(isset($_POST['origins']) && !(isset($_POST['destinations'])) && !(isset($_POST['departureTimes']))){//Display rides by origin
			$org = $_POST['origins'];
			if(is_numeric($org)){
				$result = mysql_query("SELECT * FROM rides WHERE origin = '$org' AND availableSeats > 0 ORDER BY departure", $con);
			}else{
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE r.origin = l.locationID AND l.campus = '$orgCamp' AND availableSeats > 0 ORDER BY departure", $con);
			}
		}else if(isset($_POST['destinations']) && !(isset($_POST['origins'])) && !(isset($_POST['departureTimes']))){//Display rides by destination
			$dest = $_POST['destinations'];
			if(is_numeric($dest)){
				$result = mysql_query("SELECT * FROM rides WHERE destination = '$dest' ORDER BY departure", $con);
			}else{
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE r.destination = l.locationID AND l.campus = '$destCamp' AND availableSeats > 0 ORDER BY departure", $con);		
			}
		}else if(isset($_POST['departureTimes']) && !(isset($_POST['origins'])) && !(isset($_POST['destinations']))){//Display rides by departure time
			$depTime = $_POST['departureTimes'];
			$result = mysql_query("SELECT * FROM rides WHERE SUBSTRING(departure, 1, 2) = '$depTime' AND availableSeats > 0 ORDER BY departure", $con);
		}else if(isset($_POST['destinations']) && isset($_POST['origins']) && !(isset($_POST['departureTimes']))){//Display rides by origin and destination
			$dest = $_POST['destinations'];
			$org = $_POST['origins'];
			
			if(!is_numeric($dest) && is_numeric($org)){//Campus destination, specific origin
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE r.origin ='$org' AND r.destination = l.locationID AND l.campus = '$destCamp' AND availableSeats > 0 ORDER BY departure", $con);	
			}else if(!is_numeric($org) && is_numeric($dest)){//Campus origin, specific destination
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE r.destination ='$dest' AND r.origin = l.locationID AND l.campus = '$orgCamp' AND availableSeats > 0 ORDER BY departure", $con);
			
			}else if(!is_numeric($org) && !is_numeric($dest)){//Campus origin and destination 
				$result = mysql_query("SELECT * FROM rides r, locations a, locations b WHERE (a.locationID = r.origin AND a.campus = '$orgCamp') AND (b.locationID = r.destination AND b.campus = '$destCamp') AND availableSeats > 0 ORDER BY departure", $con);
			}else{//Specific origin and Destination
				$result = mysql_query("SELECT * FROM rides WHERE destination = '$dest' AND origin = '$org' AND availableSeats > 0 ORDER BY departure", $con);
			}
		}else if(!(isset($_POST['destinations'])) && isset($_POST['origins']) && isset($_POST['departureTimes'])){//Display rides by origin and departure time
			$depTime = $_POST['departureTimes'];
			$org = $_POST['origins'];
			if(is_numeric($org)){
				$result = mysql_query("SELECT * FROM rides WHERE SUBSTRING(departure, 1, 2) = '$depTime' AND origin = '$org' AND availableSeats > 0 ORDER BY departure", $con);
			}else{
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE (r.origin = l.locationID AND l.campus = '$orgCamp') AND (SUBSTRING(departure, 1, 2) = '$depTime') AND availableSeats > 0 ORDER BY departure", $con);
			}
			
		}else if(isset($_POST['destinations']) && !(isset($_POST['origins'])) && isset($_POST['departureTimes'])){//Display rides by destination and departure time
			$depTime = $_POST['departureTimes'];
			$dest = $_POST['destinations'];
			if(is_numeric($dest)){
				$result = mysql_query("SELECT * FROM rides WHERE SUBSTRING(departure, 1, 2) = '$depTime' AND destination = '$dest' AND availableSeats > 0 ORDER BY departure", $con);
			}else{
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE (r.destination = l.locationID AND l.campus = '$destCamp') AND (SUBSTRING(departure, 1, 2) = '$depTime') AND availableSeats > 0 ORDER BY departure", $con);
			}
			
		}else if(isset($_POST['destinations']) && isset($_POST['origins']) && isset($_POST['departureTimes'])){//Display rides by origin, destination, and departure time
			$depTime = $_POST['departureTimes'];
			$dest = $_POST['destinations'];
			$org = $_POST['origins'];

			if(!is_numeric($dest) && is_numeric($org)){//Campus destination, specific origin
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE r.origin ='$org' AND r.destination = l.locationID AND l.campus = '$destCamp' AND SUBSTRING(departure, 1, 2) = '$depTime' AND availableSeats > 0 ORDER BY departure", $con);	
			}else if(!is_numeric($org) && is_numeric($dest)){//Campus origin, specific destination
				$result = mysql_query("SELECT * FROM rides r, locations l WHERE r.destination ='$dest' AND r.origin = l.locationID AND l.campus = '$orgCamp' AND SUBSTRING(departure, 1, 2) = '$depTime' AND availableSeats > 0 ORDER BY departure", $con);
			
			}else if(!is_numeric($org) && !is_numeric($dest)){//Campus origin and destination 
				$result = mysql_query("SELECT * FROM rides r, locations a, locations b WHERE (a.locationID = r.origin AND a.campus = '$orgCamp') AND (b.locationID = r.destination AND b.campus = '$destCamp') AND SUBSTRING(departure, 1, 2) = '$depTime' AND availableSeats > 0 ORDER BY departure", $con);
			}else{//Specific origin and Destination
				$result = mysql_query("SELECT * FROM rides WHERE SUBSTRING(departure, 1, 2) = '$depTime' AND destination = '$dest' AND origin = '$org' AND availableSeats > 0 ORDER BY departure", $con);
			}			
			
		}else if(!(isset($_POST['origins'])) && !(isset($_POST['destinations'])) && !(isset($_POST['departureTimes']))){//Display all rides
			$result=mysql_query("SELECT * FROM rides WHERE availableSeats > 0 ORDER BY departure", $con);
		}
	?>

	<form name="searchRides" onsubmit="searchAvailableRides.php" method="post">
	FROM: <select name="origins">
		<option value="" disabled="disabled" selected="selected">
		<?php
			if(!(isset($_POST['origins']))){
				echo "Rides by Origin";
			}else{
				if($org == "busch"){
					echo "Busch Campus";
				}else if($org == "cac"){
					echo "College Ave Campus";
				}else if($org == "cdoug"){
					echo "Cook/Douglas Campus";
				}else if($org == "livi"){
					echo "Livingston Campus";
				}else{	
					$getOrg = mysql_query("SELECT * FROM locations WHERE locationID = '$org'", $con);
					$originName = mysql_fetch_array($getOrg);
					echo $originName['locationName'];
				}
			}
		?>
		</option>
		<option value="" disabled="disabled">------Campuses------</option>
		<option value="busch">Busch Campus</option>
		<option value="cac">College Ave Campus</option>
		<option value="cdoug">Cook/Douglas Campus</option>
		<option value="livi">Livingston Campus</option>
		<option value="" disabled="disabled">-----Parking Lots-----</option>
		<option value="8">Busch - Parking Lot 51</option>
		<option value="10">Busch - Parking Lot 51A</option>
		<option value="12">Busch - Parking Lot 64</option>
		<option value="9">CAC - Parking Lot 8</option>
		<option value="7">CAC - Parking Lot 30</option>
		<option value="13">CAC - College Ave Parking Deck</option>
		<option value="14">CAC - New Brunswick Train Station</option>
		<option value="11">Cook/Douglas - Parking Lot 86</option>
		<option value="4">Cook/Douglas - Parking Lot 97</option>
		<option value="15">Cook/Douglas - RU Public Safety Building</option>
		<option value="6">Livingston - Lot 101</option>
		<option value="5">Livingston - Lot 103</option>
		<option value="3">Livingston - Lot 105</option>
	
	</select> 
	TO: <select name="destinations">
		<option value="" disabled="disabled" selected="selected">
		<?php
			if(!(isset($_POST['destinations']))){
				echo "Rides by Destination";
			}else{
				if($dest == "busch"){
					echo "Busch Campus";
				}else if($dest == "cac"){
					echo "College Ave Campus";
				}else if($dest == "cdoug"){
					echo "Cook/Douglas Campus";
				}else if($dest == "livi"){
					echo "Livingston Campus";
				}else{	
					$getDest = mysql_query("SELECT * FROM locations WHERE locationID = '$dest'", $con);
					$destName = mysql_fetch_array($getDest);
					echo $destName['locationName'];
				}
			}
		?>
		</option>
		<option value="" disabled="disabled">------Campuses------</option>
		<option value="busch">Busch Campus</option>
		<option value="cac">College Ave Campus</option>
		<option value="cdoug">Cook/Douglas Campus</option>
		<option value="livi">Livingston Campus</option>
		<option value="" disabled="disabled">-----Parking Lots-----</option>
		<option value="8">Busch - Parking Lot 51</option>
		<option value="10">Busch - Parking Lot 51A</option>
		<option value="12">Busch - Parking Lot 64</option>
		<option value="9">CAC - Parking Lot 8</option>
		<option value="7">CAC - Parking Lot 30</option>
		<option value="13">CAC - College Ave Parking Deck</option>
		<option value="14">CAC - New Brunswick Train Station</option>
		<option value="11">Cook/Douglas - Parking Lot 86</option>
		<option value="4">Cook/Douglas - Parking Lot 97</option>
		<option value="15">Cook/Douglas - RU Public Safety Building</option>
		<option value="6">Livingston - Lot 101</option>
		<option value="5">Livingston - Lot 103</option>
		<option value="3">Livingston - Lot 105</option>
	
	</select> 
	AT: <select name="departureTimes">
		<option value="" disabled="disabled" selected="selected">
		<?php
			if(!(isset($_POST['departureTimes']))){
				echo "Rides by Departure Time";
			}else{
				$getDepTime = $_POST['departureTimes'];
				if($getDepTime == "00"){
					echo "12:00 AM - 12:59 AM";
				}else if($getDepTime == "01"){
					echo "1:00 AM - 1:59 AM";
				}else if($getDepTime == "02"){
					echo "2:00 AM - 2:59 AM";
				}else if($getDepTime == "03"){
					echo "3:00 AM - 3:59 AM";
				}else if($getDepTime == "04"){
					echo "4:00 AM - 4:59 AM";
				}else if($getDepTime == "05"){
					echo "5:00 AM - 5:59 AM";
				}else if($getDepTime == "06"){
					echo "6:00 AM - 6:59 AM";
				}else if($getDepTime == "07"){
					echo "7:00 AM - 7:59 AM";
				}else if($getDepTime == "08"){
					echo "8:00 AM - 8:59 AM";
				}else if($getDepTime == "09"){
					echo "9:00 AM - 9:59 AM";
				}else if($getDepTime == "10"){
					echo "10:00 AM - 10:59 AM";
				}else if($getDepTime == "11"){
					echo "11:00 AM - 11:59 AM";
				}else if($getDepTime == "12"){
					echo "12:00 PM - 12:59 PM";
				}else if($getDepTime == "13"){
					echo "1:00 PM - 1:59 PM";
				}else if($getDepTime == "14"){
					echo "2:00 PM - 2:59 PM";
				}else if($getDepTime == "15"){
					echo "3:00 PM - 3:59 PM";
				}else if($getDepTime == "16"){
					echo "4:00 PM - 4:59 PM";
				}else if($getDepTime == "17"){
					echo "5:00 PM - 5:59 PM";
				}else if($getDepTime == "18"){
					echo "6:00 PM - 6:59 PM";
				}else if($getDepTime == "19"){
					echo "7:00 PM - 7:59 PM";
				}else if($getDepTime == "20"){
					echo "8:00 PM - 8:59 PM";
				}else if($getDepTime == "21"){
					echo "9:00 PM - 9:59 PM";
				}else if($getDepTime == "22"){
					echo "10:00 PM - 10:59 PM";
				}else{
					echo "11:00 PM - 11:59 PM";
				}				
			}
		?>
		</option>
		<option value="00">12:00 AM - 12:59 AM</option>
		<option value="01">1:00 AM - 1:59 AM</option>
		<option value="02">2:00 AM - 2:59 AM</option>
		<option value="03">3:00 AM - 3:59 AM</option>
		<option value="04">4:00 AM - 4:59 AM</option>
		<option value="05">5:00 AM - 5:59 AM</option>
		<option value="06">6:00 AM - 6:59 AM</option>
		<option value="07">7:00 AM - 7:59 AM</option>
		<option value="08">8:00 AM - 8:59 AM</option>
		<option value="09">9:00 AM - 9:59 AM</option>
		<option value="10">10:00 AM - 10:59 AM</option>
		<option value="11">11:00 AM - 11:59 AM</option>
		<option value="12">12:00 PM - 12:59 PM</option>
		<option value="13">1:00 PM - 1:59 PM</option>
		<option value="14">2:00 PM - 2:59 PM</option>
		<option value="15">3:00 PM - 3:59 PM</option>
		<option value="16">4:00 PM - 4:59 PM</option>
		<option value="17">5:00 PM - 5:59 PM</option>
		<option value="18">6:00 PM - 6:59 PM</option>
		<option value="19">7:00 PM - 7:59 PM</option>
		<option value="20">8:00 PM - 8:59 PM</option>
		<option value="21">9:00 PM - 9:59 PM</option>
		<option value="22">10:00 PM - 10:59 PM</option>
		<option value="23">11:00 PM - 11:59 PM</option>
	</select>
	<button class='btn btn-default type='submit'>Search</button> <button class="btn btn-default" type="button" onclick="mainPage()">All Rides</button> 
	</form><br>
	<!--Display Rides in Table-->

	<div>
	<table class="table" style="overflow:auto"> 
	<tr>
	<td>Ride</td>
	<td>Origin</td>
	<td>Destination</td>
	<td>Departure Time</td>
	<td>Open Seats</td>
	<td>Driver</td>
	<td>Request for Ride</td>
	</tr>
	<?php
	$i = 1;
	while($row = mysql_fetch_array($result)){?>
	<tr><form action="requestForRide.php" method="post" name="<?php echo $row['rideID']; ?>">
	<td><?php echo $i; ?><input type="text" style="display:none" name="rideId" value="<?php echo $row['rideID']; ?>"></td>
	<td><input size="30" type="text" name="origin" style="text-align:center;border: none;" value="<?php 
		$origin = $row['origin'];
		$resultThree=mysql_query("SELECT * FROM locations WHERE locationID = '$origin'", $con);
		$locationRowTwo = mysql_fetch_array($resultThree);

		echo $locationRowTwo['locationName'];
	?>" readonly></td>
	<td><input size="30" type="text" name="destination" style="text-align:center;border: none;" value="<?php 
		$destination = $row['destination'];
		$resultTwo=mysql_query("SELECT * FROM locations WHERE locationID = '$destination'", $con);
		$locationRow = mysql_fetch_array($resultTwo);
		echo $locationRow['locationName'];
	?>" readonly></td>
	<td><input type="text" name="departure" style="text-align:center;border: none;" value="<?php 
		if(substr($row['departure'], 0, 2) > 12){
			$hour = substr($row['departure'], 0, 2) - 12;
			echo $hour . substr($row['departure'], 2, 3) . " PM"; 
		}else if(substr($row['departure'], 0, 2) == 0){
			echo "12" . substr($row['departure'], 2, 3) . " AM"; 			
		}else if(substr($row['departure'], 0, 2) < 10){
			echo substr($row['departure'], 1, 4) . " AM"; 			
		}else{
			echo substr($row['departure'], 0, 5) . " AM"; 
		}
		
	?>" readonly></td>
	<td><input size="9" type="text" name="availableSeats" style="text-align:center;border: none;" value="<?php echo $row['availableSeats']; ?>" readonly></td>
	<td><input type="text" name="driver" style="text-align:center;border: none;" value="<?php 
		$car = $row['car'];
		$resultFive=mysql_query("SELECT * FROM cars WHERE carID = '$car'", $con);
		$driver = mysql_fetch_array($resultFive);
		$driverID = $driver['driver'];

		$resultSix = mysql_query("SELECT * FROM users WHERE userID = '$driverID'", $con);
		$driverName = mysql_fetch_array($resultSix);
		
		echo $driverName['userName'];

	?>" readonly></td>
	<td><?php 
		$ride = $row['rideID'];
		$resultFour=mysql_query("SELECT * FROM requests WHERE ride = '$ride'", $con);
		
		$carId = $row['car'];
		$checkUser = mysql_query("SELECT * FROM cars WHERE carID = '$carId'", $con);
		$driver = mysql_fetch_array($checkUser);

		$j = 0;

		while($userRow = mysql_fetch_array($resultFour)){
			if($userRow['requestingUser'] == $_COOKIE['USER_LOGGED_IN']){
				echo "Request Sent";
				$j++;
			}
		}
		if($j == 0){
			if($driver['driver'] == $_COOKIE['USER_LOGGED_IN']){
				echo "Your Posted Ride";
			}else{
				echo "<button class='btn btn-default type='submit'>Request Ride</button>";
			}
		}
	?>
	</form>
        </td>
	</tr>
	<?php 
		$i++;} 
		if($i == 1){
			echo "<h2> No results found </h2>";
		}
	?>
	</div>
	<?php mysql_close($con); ?>
</div>

</body>
</html>
