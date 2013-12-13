<!DOCTYPE html>
<html>
	<head>
		<title>Scarlet Taxi - Sent Requests</title>
		<?php include 'header.php'; ?>
	</head>
	<body>
		<?php 
			if(empty($_COOKIE['USER_LOGGED_IN'])) {
				include 'loggedOutNav.php';
			}  else if (isset($_COOKIE['USER_LOGGED_IN'])) { 
				include 'loggedInNav.php';
			}
		?>
		<div class="container">
		<h1>Sent Requests</h1>
		<br>
			<?php
				$userID = $_COOKIE['USER_LOGGED_IN'];
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

				if(isset($_POST['requestId'])){//Cancel the request
					$req = $_POST['requestId'];
					
					$res = mysql_query("SELECT * FROM requests WHERE requestID = '$req'", $con);
					$status = mysql_fetch_array($res);
					
					if($status['requestStatus'] == 'a'){//If request is approved

						//Create new notification for request
						mysql_query("INSERT INTO requestNotifications (request, notificationType)
						VALUES ($req, 'C')", $con) or die ('Error updating database: '.mysql_error());

						//Increase Available Seats in Ride
						$rideId = $status['ride'];
						$resu = mysql_query("SELECT * FROM rides WHERE rideID = '$rideId'", $con);
						$seats = mysql_fetch_array($resu);
						$availSeats = $seats['availableSeats'];
						$availSeats = $availSeats + 1;
						
						mysql_query("UPDATE rides SET availableSeats='$availSeats' WHERE rideID = '$rideId'", $con) or die ('Error updating database: '.mysql_error());
					}

					//Delete the request
					mysql_query("DELETE FROM requestNotifications WHERE request = '$req' AND notificationtype != 'C'", $con);
					mysql_query("DELETE FROM requests WHERE requestID = '$req'", $con);
				}
				
				$query = "SELECT * FROM Sent_Requests_View5 WHERE requestingUser = '$userID' ORDER BY departure";
				$result = mysql_query($query);
                		$num = mysql_num_rows($result);
				if($num == 0){ echo"<h4>You have not sent any requests</h4>";}else{ 
			?>
			<div class="container">
				<table name="sentRequests" class="table" id="sentRequests">
				<tr>
					<td><h4>Request</h4></td>
					<td><h4>Origin</h4></td>
					<td><h4>Destination</h4></td>
					<td><h4>Departure Time</h4></td>
					<td><h4>Request Comment</h4></td>
					<td><h4>Car Info</h4></td>
					<td><h4>Request Status</h4></td>
					<td><h4>Cancel Request</h4></td>
				</tr>
				<?php
					$i=0;
					while ($i < $num) {
						$f0=mysql_result($result,$i,"requestID");
						$check = mysql_query("SELECT * FROM requestNotifications WHERE request = '$f0' AND notificationtype = 'C'", $con);
						$numRows = mysql_num_rows($check);
			
						if($numRows == 0){
							$f1=mysql_result($result,$i,"origin");
							$f2=mysql_result($result,$i,"destination");
							$f3=mysql_result($result,$i,"departure");
							$f4=mysql_result($result,$i,"requestComment");
							$f5=mysql_result($result,$i,"requestStatus");
							$f6=mysql_result($result,$i,"driver");
							$f7=mysql_result($result,$i,"make");
							$f8=mysql_result($result,$i,"color");
							$f9=mysql_result($result,$i,"style");
							$f10=mysql_result($result,$i,"seats");
						
				?>
						<tr><form action="sentRequests.php" method="post" name="requests">
							<input type="text" style="display: none" name="requestId" value="<?php echo $f0; ?>">
							<td>
								<p style="text-align:center;border: none;"><?php echo $i + 1; ?></p>
							</td>
							<td>
								<p style="text-align:center;border: none;"><?php echo $f1; ?></p>
							</td>
							<td>
								<p style="text-align:center;border: none;"><?php echo $f2; ?></p>
							</td>
							<td>
								<p style="text-align:center;border: none;"><?php
									if(substr($f3, 0, 2) > 12){
										$hour = substr($f3, 0, 2) - 12;
										echo $hour . substr($f3, 2, 3) . " PM"; 
									}else if(substr($f3, 0, 2) == 0){
										echo "12" . substr($f3, 2, 3) . " AM"; 			
									}else if(substr($f3, 0, 2) < 10){
										echo substr($f3, 1, 4) . " AM"; 			
									}else{
										echo substr($f3, 0, 5) . " AM"; 
									}

								?></p>
							</td>

							<td>
								<p style="text-align:center;border: none;"><?php echo $f4; ?></p>
							</td>
							<td>
								<?php 
									echo "Driver: " . $f6 . "<br>";
									echo "Color: " . $f8 . "<br>";
									echo "Make: " . $f7 . "<br>";
									echo "Style: " . $f9 . "<br>";
								?>
							</td>
							<td>
								<p style="text-align:center;border: none;">
									<?php
										if($f10 == '-1'){ 
											echo '<button class="btn btn-danger btn-block" disabled>
											<i class="glyphicon glyphicon-remove"></i> Ride Canceled</button>'; 

										} else if ($f5 == 'p') { echo '<button type="button" class="btn btn-warning" disabled>
																<i class="glyphicon glyphicon-transfer"></i>
																Pending
															</button>'; }
										else if ($f5 == 'a') { echo '<button type="button" class="btn btn-success btn-block" disabled>
																	 <i class="glyphicon glyphicon-ok"></i>
																		Approved
																	 </button>'; }
										else if ($f5 == 'd') { echo '<button type="button" class="btn btn-error btn-block" disabled>
																		<i class="glyphicon glyphicon-remove"></i>
																		Denied
																	</button>'; }																				?>
								</p>
							</td>
							<td><!--Cancel Request-->
								<?php 
								if($f10 == '-1'){ 
								?>	
									<button type="submit" class="btn btn-default">Delete</button>
								<?php }else{ ?>
									<button type="submit" class="btn btn-default">Cancel</button>
								<?php } ?>
							</td></form>
						</tr>
				<?php
					}$i++;}}
				?>

			</div>
			
		</div>
		
	</body>
</html>
