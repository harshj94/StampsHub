<?php

/**
 PHP API for Login, Register, Changepassword, Resetpassword Requests and for Email Notifications.
 **/

if (isset($_POST['tag']) && $_POST['tag'] != '') 
{
    // Get tag
    $tag = $_POST['tag'];

    // Include Database handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();
	
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    // check for tag type
    if ($tag == 'login') 
	{
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];

        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        
    
		if ($user != false) 
		{
            // user found
            // echo json with success = 1
			
        $response["success"] = 1;
	    $response["user"]["utype"] = $user["utype"];
		$response["user"]["first_name"] = $user["first_name"];
		$response["user"]["last_name"] = $user["last_name"];
		$response["user"]["email_id"] = $user["email_id"];
		$response["user"]["user_gender"] = $user["user_gender"];
		$response["user"]["phone_number"] = $user["phone_number"];
		$response["user"]["date_of_birth"] = $user["date_of_birth"];
                $response["user"]["uid"] = $user["unique_id"];
		$response["user"]["created_at"] = $user["created_at"];
            
            echo json_encode($response);
        } 
		else 
		{
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";

            echo json_encode($response);
        }
    } 
	
	
	
	else if ($tag == 'chgpass')
	{
		$email = $_POST['email'];
		$newpassword = $_POST['newpas'];
		$hash = $db->hashSSHA($newpassword);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
		$subject = "Change Password Notification";
        $message = "Hello User,\n\nYour Password is sucessfully changed.\n\nRegards,\StampsHub Team.";
        $from = "harsh@stampshubdemo.site50.net";
		$headers = "From:" . $from;
		if ($db->isUserExisted($email))
		{
			$user = $db->forgotPassword($email, $encrypted_password, $salt);
			if ($user) 
			{
				$response["success"] = 1;
				mail($email,$subject,$message,$headers);
				echo json_encode($response);
			}
			else 
			{
				$response["error"] = 1;
				echo json_encode($response);
			}

            // user is already existed - error response           
        } 
		else 
		{
            $response["error"] = 2;
            $response["error_msg"] = "User not exist";
			echo json_encode($response);

		}
	}

	
	else if ($tag == 'forpass')
	{
		$forgotpassword = $_POST['forgotpassword'];
		$randomcode = $db->random_string();
		$hash = $db->hashSSHA($randomcode);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
		$subject = "Password Recovery";
		$message = "Hello User,\n\nYour Password is sucessfully changed. Your new Password is $randomcode . Login with your new Password and change it in the User Panel.\n\nRegards,\nStampsHub Team.";
		$from = "harsh@stampshubdemo.site50.net";
		$headers = "From:" . $from;
		if ($db->isUserExisted($forgotpassword)) 
		{
			$user = $db->forgotPassword($forgotpassword, $encrypted_password, $salt);
			if ($user)
			{
				$response["success"] = 1;
				mail($forgotpassword,$subject,$message,$headers);
				echo json_encode($response);
			}
			else 
			{
				$response["error"] = 1;
				echo json_encode($response);
			}
            // user is already existed - error response
        } 
		else 
		{
			$response["error"] = 2;
            $response["error_msg"] = "User not exist";
			echo json_encode($response);
		}

	}

	else if ($tag == 'register') 
	{
        // Request type is Register new user
		$utype = $_POST['utype'];
        $first_name = $_POST['first_name'];
		$last_name = $_POST['last_name'];
        $email_id = $_POST['email_id'];
		$user_gender = $_POST['user_gender'];
		$phone_number = $_POST['phone_number'];
		$date_of_birth = $_POST['date_of_birth'];
		$security_question = $_POST['security_question'];
		$security_answer = $_POST['security_answer'];
        $user_password = $_POST['user_password'];    
		
		$subject = "Registration";
		$message = "Hello $first_name,\n\nYou have sucessfully registered to our service.\n\nRegards,\nStampsHub.";
		$from = "harsh@stampshubdemo.site50.net";
		$headers = "From:" . $from;

        // check if user is already existed
        if ($db->isUserExisted($email_id)) 
		{
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } 
		else if(!$db->validEmail($email_id))
		{
            $response["error"] = 3;
            $response["error_msg"] = "Invalid Email Id";
            echo json_encode($response);             
		}
		else 
		{
            // store user
            $user = $db->storeUser($utype,$first_name,$last_name,$email_id,$user_gender,$phone_number,$date_of_birth,$security_question,$security_answer,$user_password);
            if ($user) 
			{
                // user stored successfully
				$response["success"] = 1;
				$response["user"]["utype"] = $user["utype"];
				$response["user"]["first_name"] = $user["first_name"];
				$response["user"]["last_name"] = $user["last_name"];
				$response["user"]["email_id"] = $user["email_id"];
				$response["user"]["user_gender"] = $user["user_gender"];
				$response["user"]["phone_number"] = $user["phone_number"];
				$response["user"]["date_of_birth"] = $user["date_of_birth"];
                $response["user"]["uid"] = $user["unique_id"];
				$response["user"]["created_at"] = $user["created_at"];
				mail($email_id,$subject,$message,$headers);
            
                echo json_encode($response);
            } 
			else 
			{
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "JSON Error occured in Registartion";
                echo json_encode($response);
            }
        }
    } 
	
	else if ($tag == 'registerbiz') 
	{
        // Request type is Register new user
		$utype = $_POST['utype'];
		$business_name = $_POST['business_name'];
		$email_id = $_POST['email_id'];
		$address1 = $_POST['address1'];
		$address2 = $_POST['address2'];
		$address3 = $_POST['address3'];
		$country = $_POST['country'];
		$postcode = $_POST['postcode'];
		$security_question = $_POST['security_question'];
		$security_answer = $_POST['security_answer'];
		$password = $_POST['password'];
           
		$subject = "Registration";
		$message = "Hello $business_name,\n\nYou have sucessfully registered to our service.\n\nRegards,\nStampsHub.";
		$from = "harsh@stampshubdemo.site50.net";
		$headers = "From:" . $from;

        // check if user is already existed
        if ($db->isUserExistedbiz($email_id)) 
		{
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } 
		else if(!$db->validEmail($email_id))
		{
            $response["error"] = 3;
            $response["error_msg"] = "Invalid Email Id";
            echo json_encode($response);             
		}
		else 
		{
            // store user
            $user = $db->storeUserbiz($utype,$business_name,$email_id,$address1,$address2,$address3,$country,$postcode,$security_question,$security_answer,$password);
            if ($user) 
			{
                // user stored successfully
				$response["success"] = 1;
				$response["user"]["utype"] = $user["utype"];
				$response["user"]["business_name"] = $user["business_name"];
				$response["user"]["email_id"] = $user["email_id"];
				$response["user"]["address1"] = $user["address1"];
				$response["user"]["address2"] = $user["address2"];
				$response["user"]["address3"] = $user["address3"];
				$response["user"]["country"] = $user["country"];
                $response["user"]["postcode"] = $user["postcode"];
				$response["user"]["uid"] = $user["unique_id"];
				$response["user"]["created_at"] = $user["created_at"];
				mail($email_id,$subject,$message,$headers);
            
                echo json_encode($response);
            } 
			else 
			{
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "JSON Error occured in Registartion";
                echo json_encode($response);
            }
        }
    }
	
	
	else 
	{
		$response["error"] = 3;
		$response["error_msg"] = "JSON ERROR";
		echo json_encode($response);
    }
} 

else 
{
    echo "StampsHub Login API";
}
?>