<!DOCTYPE html>
<html>
	<head>
		<title>Scarlet Taxi</title>
		<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <meta charset="utf-8">
	</head>
	
	<?php
		$userID = 1;
		
		#$con = mysqli_connect('cs431-5.cs.rutgers.edu', 'csuser', 'csc1b7c2');
		
		#$row = mysqli_fetch_array(mysqli_query("SELECT * FROM users WHERE userID = " + $userID));
		
		$row = array("userID"=>"1", "memberName"=>"Andy Krohg", "userName"=>"andy", "memberPassword"=>"pass", "email"=>"andykrohg@gmail.com", "address"=>"199", "mobileNumber"=>"9083101493", 
			"receiveEmailNotification"=>"1", "receiveSMSNotification"=>"1");

	?>
	
	<script>
		function checkBoxes()
		{
			document.getElementById("emailNotification").checked=<?php echo $row['receiveEmailNotification'];?> ==1;
			document.getElementById("smsNotification").checked=<?php echo $row['receiveSMSNotification'];?> ==1;
		}
		
        function checkModificationForm() {
                name = $("#inputName").val();
                userName = $("#inputUsername").val();
                email = $("#inputEmail").val();
                mobile = $("#inputMobile").val();
                password = $("#inputPassword").val();
                checkPassword = $("#confirmPassword").val();
                
                console.log(name);
                console.log(userName);
                
                if(name.length < 2) {
                        $("#name-group").addClass("has-error");
                               $("#nameError").text("* Name must be at least 2 characters long.");
                } else {
                        $("#name-group").removeClass("has-error");
                        $("#nameError").text("");
                }
                
                if(userName.length < 6) {
                        $("#username-group").addClass("has-error");
                        $("#usernameError").text("* Username must be at least 6 characters long.");
                } else {
                        $("#username-group").removeClass("has-error");
                        $("#usernameError").text("");
                }
                
                if(checkEmail(email) == false) {
                        $("#email-group").addClass("has-error");
                        $("#emailError").text("* Email is not valid.");
                } else {
                        $("email-group").removeClass("has-error");
                        $("#emailError").text("");
                }
                
                if(checkMobile(mobile) == false) {
                        $("#mobile-group").addClass("has-error");
                        $("#mobileError").text("* Mobile number must be 10 digits of format XXXXXXXXXX.");
                } else {
                        $("#mobile-group").removeClass("has-error");
                        $("#mobileError").text("");
                }
                
                if(password != checkPassword) {
                        $("#password-group").addClass("has-error");
                        $("#confirmPassword-group").addClass("has-error");
                        $("#passwordError").text("* Passwords do not match.");
                } else if (password.length < 12 || password.length > 21 ) {
                           $("#password-group").addClass("has-error");
                           $("#confirmPassword-group").addClass("has-error");
                           $("#passwordError").text("* Passwords must be between 12 and 21 characters long.");
                } else {
                        $("#password-group").removeClass("has-error");
                        $("#confirmPassword-group").removeClass("has-error");
                        $("#passwordError").text("");
                }         
                
        }
        
        function checkMobile(mobile) {
                var phoneno = /^\d{10}$/;
                if(mobile.match(phoneno)) {
                        return true;
                       } else {
                    return false;
                }
        }
        
        function checkEmail(email) {
                var emailRegEx = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                if(email.match(emailRegEx)) {
                        return true;
                } else {
                        return false;
                }
        }

    </script>
	
	<body onLoad = "checkBoxes()">
        <div class="container">
            <h1>Modify Account Settings</h1>
            <br>
			
            <form class="form-horizontal" role="form">

                <div id="name-group" class="form-group">
                    <label for="inputName" class="col-sm-2 control-label">Name</label>
                    <div class="col-sm-2">
                        <input type="text" class="form-control" id="inputName" value = "<?php print $row['memberName'];?>">
                    </div>
                    <p id="nameError"></p>
                </div>

                <div id="username-group" class="form-group">
                    <label for="inputUsername" class="col-sm-2 control-label">Username</label>
                    <div class="col-sm-2">
                        <input type="text" class="form-control" id="inputUsername" value=<?php print $row['userName'];?>>
                    </div>
                    <p id="usernameError"></p>
                </div>

                <div id="email-group" class="form-group">
                    <label for="inputEmail" class="col-sm-2 control-label">Email</label>
                    <div class="col-sm-2">
                        <input type="email" class="form-control" id="inputEmail" value=<?php print $row['email'];?>>
                    </div>
                    <p id="emailError"></p>
                </div>

                <div id="mobile-group" class="form-group">
                    <label for="inputMobile" class="col-sm-2 control-label">Mobile Number</label>
                    <div class="col-sm-2">
                        <input type="tel" class="form-control" id="inputMobile" value=<?php print $row['mobileNumber'];?>>
                    </div>
                    <p id="mobileError"></p>
                </div>

                <div id="password-group" class="form-group">
                    <label for="inputPassword" class="col-sm-2 control-label">Password</label>
                    <div class="col-sm-2">
                        <input type="password" class="form-control" id="inputPassword" value=<?php print $row['memberPassword'];?>>
                    </div>
                    <p id="passwordError"></p>
                </div>

                <div id="confirmPassword-group" class="form-group">
                    <label for="confirmPassword" class="col-sm-2 control-label">Confirm Password</label>
                    <div class="col-sm-2">
                        <input type="password" class="form-control" id="confirmPassword" value=<?php print $row['memberPassword'];?>>
                    </div>
					<br>
                </div>
				
				<div id="alert-group" class = "form-group">
					<label for="alerts" class="col-sm-2 control-label">Alert Type</label>
					<div class="col-sm-2" id="alerts">
						<input type="checkbox" class="form-control" id="emailNotification">Email
						<input type="checkbox" class="form-control" id="smsNotification">SMS
					</div>
				</div>
                <br>
                <div class="col-sm-2 col-sm-offset-1">
                    <button onclick="checkModificationForm()" type="button" class="btn btn-success btn-block">Save</button>
					<button onclick="cancel" type = "button" class = "btn btn-success btn-block">Cancel</button>
                </div>
				
            </form>
        </div>
    </body>
</html>