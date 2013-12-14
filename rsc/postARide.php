<!DOCTYPE html>

<html>
	<head>
		<title>Scarlet Taxi - Post a Ride</title>
		<?php include 'header.php'; ?>
		<script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>
		<style>

			#left_container {
				text-align: left;
				float:left;
                margin:20px;
		margin-left:100px;
                padding:20px;
                position: relative;
                z-index: 10;
                background-color:white;
                border: 2px solid #222222;
                border-radius:25px;
			}
			#map_wrapper {
				float: left;
				height: 100%;
				width: 100%;
				position: fixed;
			}
			#map_canvas {
				width: 100%;
				height: 100%;
			}
		</style>
		<?php
				require_once('../htdocs-include/credentials.php');
				$conn = mysql_connect($host, $username, $password);
				$db_selected = mysql_select_db($database, $conn);
				
				$query = "SELECT * FROM Google_Maps_View";
				$result = mysql_query($query);
				$locations = array();
				while($r = mysql_fetch_assoc($result)) {
					$locations[] = $r;
				}

				$userID = $_COOKIE['USER_LOGGED_IN'];
				$checkCar = mysql_query("SELECT * FROM cars WHERE driver = '$userID'", $conn);
				$numCar = mysql_num_rows($checkCar);
	
		?>
		<script type="text/javascript">
			function infoWindowButton_pickupInput(locationName) {
				destinationInput = $("#destAddress").val();
				pickUpInput = $("#pickUpLoc").val();
				console.log(destinationInput);
				console.log(pickUpInput);
				if(destinationInput === locationName) {
					console.log("infoWindowButton_pickupInput if statement shot");
					$("#destAddress").val("");
				}
				$("#pickUpLoc").val(locationName);
			}

			function infoWindowButton_destinationInput(locationName) {
				destinationInput = $("#destAddress").val();
				pickUpInput = $("#pickUpLoc").val();
				console.log(destinationInput);
				console.log(pickUpInput);
				if(locationName === pickUpInput) {
					console.log("infoWindowButton_destinationInput if statement");
					$("#pickUpLoc").val("");
				}
				$("#destAddress").val(locationName);
			}

			function validateForm()
			{
			  var x=document.forms["postRideForm"]["pickUpLoc"].value;
				if (x==null || x=="" || x == "Rider Pickup Location")
				{
					alert("Please choose a rider pickup location.");
					return false;
				}
				x=document.forms["postRideForm"]["destAddress"].value;
				if (x==null || x=="" || x == "Destination Address")
				{
					alert("Please choose a destination address.");
					return false;
				}
				x=document.forms["postRideForm"]["openSeats"].value;
			        var y = parseInt(x);
				if (x==null || x=="" || x == "Number of Open Seats" || y <= 0)
				{
					alert("Please choose a valid number of open seats in your car.");
					return false;
				}
				x=document.forms["postRideForm"]["chooseCar"].value;
				if (x=="" || x=="Choose Car")
				{
					alert("Please choose the car you are driving.");
					return false;
				}
				
				return true;
			}

			function mainPage()
			{
					window.location = '/main.php';   
			}
			
			function initialize() {
				var locations = <?php echo json_encode($locations); ?>;
				var mapOptions = {
					zoom: 14,
					center: new google.maps.LatLng(40.503659, -74.445211),
					mapTypeId: google.maps.MapTypeId.ROADMAP,
					panControl: true,
					zoomControl: true,
					mapTypeControl: true,
					scaleControl: true,
					streetViewControl: true,
					overviewMapControl:true
				};
				
				var map = new google.maps.Map(document.getElementById('map_canvas'),
				mapOptions);
				
				var infowindow = new google.maps.InfoWindow();
				
				var marker, i;
				
				for(i = 0; i < locations.length; i++) {
					marker = new google.maps.Marker({
	
						position: new google.maps.LatLng(locations[i]['xCoordinate'], locations[i]['yCoordinate']),
						map:map
					});
				
					google.maps.event.addListener(marker, 'click', (function(marker, i) {
						return function() {
						locationName = locations[i]['locationName'];
						infowindow.setContent('<center><h4>' + locations[i]['locationName'] + '</h4>' +
												'<h5>Campus: ' + locations[i]['campus'] + '</h5>' +
												'<h5>' + locations[i]['streetAddress'] + ', ' + locations[i]['city'] + ', ' + locations[i]['zipCode'] + '</h5>' +
												'<button type="button" class="btn btn-primary" onclick="infoWindowButton_pickupInput(locationName);">Add to Pickup</button><p></p>' +
												'<button type="button" class="btn btn-success" onclick="infoWindowButton_destinationInput(locationName);">Add to Destination</button></center>');
						infowindow.open(map, marker);
						}
					})(marker, i));
				}
			}

			google.maps.event.addDomListener(window, 'load', initialize);

			function checkCar(){
				var checkCar = <?php echo json_encode($numCar); ?>;
				if(checkCar == 0){
					alert("Please add a car to your account before posting a ride.");
					window.location = '/accountModification.php';
				}
			}
		</script>
	</head>
	<body onload="checkCar()">
	<div id="left_container">
		<?php
			include 'cookie.php';

			if(empty($_COOKIE['USER_LOGGED_IN'])){
				echo"<h2>Please log in to view this page.</h2>";
			} else if (isset($_COOKIE['USER_LOGGED_IN'])){
				include 'loggedInNav.php';
			}
		?>
			<h1>Post a Ride</h1>
			<form name="postRideForm" action="postRide.php" method="post" onsubmit="return validateForm()" >
                <h3>Rider Pickup Location</h3>
                <input type="text" class="form-control" id="pickUpLoc" name="pickUpLoc" value="Rider Pickup Location" style="width:250px" readonly>
				<h3>Destination Address</h3>
				<input type="text" class="form-control" id="destAddress" name="destAddress" value="Destination Address" style="width:250px" readonly>
				<h3>Departure Time</h3> 
				<select name="departureHour">
					<option value="01">1</option>
					<option value="02">2</option>
					<option value="03">3</option>
					<option value="04">4</option>
					<option value="05">5</option>
					<option value="06">6</option>
					<option value="07">7</option>
					<option value="08">8</option>
					<option value="09">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
				</select>
				 : 
				<select name="departureMin">
					<option value="00">00</option>
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
					<option value="24">24</option>
					<option value="25">25</option>
					<option value="26">26</option>
					<option value="27">27</option>
					<option value="28">28</option>
					<option value="29">29</option>
					<option value="30">30</option>
					<option value="31">31</option>
					<option value="32">32</option>
					<option value="33">33</option>
					<option value="34">34</option>
					<option value="35">35</option>
					<option value="36">36</option>
					<option value="37">37</option>
					<option value="38">38</option>
					<option value="39">39</option>
					<option value="40">40</option>
					<option value="41">41</option>
					<option value="42">42</option>
					<option value="43">43</option>
					<option value="44">44</option>
					<option value="45">45</option>
					<option value="46">46</option>
					<option value="47">47</option>
					<option value="48">48</option>
					<option value="49">49</option>
					<option value="50">50</option>
					<option value="51">51</option>
					<option value="52">52</option>
					<option value="53">53</option>
					<option value="54">54</option>
					<option value="55">55</option>
					<option value="56">56</option>
					<option value="57">57</option>
					<option value="58">58</option>
					<option value="59">59</option>
				</select>
				<select name="AMorPM">
					<option value="PM">PM</option>
					<option value="AM">AM</option>
				</select><br>
				<h3>Car</h3>
				<select name="chooseCar">
					<?php $openSeats = 1; ?>
					<option value="" selected="selected" disabled="disabled">Choose Car</option>
					<?php 
						$cars = mysql_query("SELECT * FROM cars WHERE driver = '$userID'");
						while ($row = mysql_fetch_array($cars)){
							if($openSeats < $row['seats']){
								$openSeats = $row['seats'];
							}
					?>
					<option value="<?php echo $row['carID']; ?>"><?php echo $row['nickname']; ?></option>
					<?php } ?>
				</select><br>
				<h3>Number of Open Seats</h3>
				<select name="openSeats">
					<option value="" selected="selected" disabled="disabled">Choose Number of Seats</option>
					<?php
						$j = 1;
						while($j < $openSeats){ ?>
					<option value="<?php echo $j; ?>"><?php echo $j; ?></option>
					<?php $j++; } ?>	
				</select><br><br>
				<button type="submit" class="btn btn-success" value="Submit">
					<i class="glyphicon glyphicon-pushpin"></i>
					Submit
				</button>
				<button type="button" class="btn btn-warning" onclick="mainPage()">
					<i class="glyphicon glyphicon-remove"></i>
					Cancel
				</button> 
			</form>
		</div>
		<div id="map_wrapper">
			<div id="map_canvas" class="mapping"></div>
		</div>
	</body>
</html>
