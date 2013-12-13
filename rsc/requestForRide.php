<!DOCTYPE html>

<html>
<head>
    <?php include 'header.php'; ?>
    <title>Scarlet Taxi - Post a Ride</title>

<script type="text/javascript">

function mainPage()
{
    window.location = '/main.php';   
}

</script>

    <style>
        .form{
            text-align: left;
        }	
    </style>

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
        <div class="col-md-10 col-md-offset-3">
            <div class="form">
            <h1>Request for Ride</h1>
                <form name="requestForRide" id="requestForRide" action="requestRide.php" method="post" >
                    <input type="text" style="display:none" value="<?php echo $_POST['rideId']; ?>" name="rideID">
                    DRIVER: <?php echo $_POST['driver']; ?><br>
                    ORIGIN: <?php echo $_POST['origin']; ?><br>
                    DESTINATION: <?php echo $_POST['destination']; ?><br>
                    DEPARTURE TIME: <?php echo $_POST['departure']; ?><br>
                    REMAINING SEATS: <?php echo $_POST['availableSeats']; ?><br>
                    <h3>Comment for the Driver</h3>
                    <textarea name="commentText" rows="4" cols="50" form="requestForRide"></textArea>
                    <br>
                    <br>
                    <input class="btn btn-primary" type="submit" value="Submit"> <button class="btn btn-warning" type="button" onclick="mainPage()">Cancel</button> 
                </form>
            </div>
        </div>
    </div>

</body>
</html>
