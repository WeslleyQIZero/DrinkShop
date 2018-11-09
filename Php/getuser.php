<?php

	require_once 'db_functions.php';
	$db = new DB_Functions();

	/*
	 * Endpoint : https://<domain>/drinkshop/getuser.php
	 * Method : POST
	 * Params : email
	 * Result : JSON
	 */

	$response = array();
	if(isset($_POST['email']) && isset($_POST['password']))
	{
		$email = $_POST['email'];
		$password = $_POST['password'];

			//Create new user
		$user = $db->getUserInformation($email,$password);
		if($user) {

			$response["name"] = $user["Name"];
			$response["surname"] = $user["Surname"];
			$response["address"] = $user["Address"];
			$response["email"] = $user["Email"];
			echo json_encode($response);
		}else {

			$response["error_msg"] = "User does not exist";
			echo json_encode($response);
		}
	}
	else {
		$response["error_msg"] = "Required parameter (email,password) is missing!";
		echo json_encode($response);
	}

?>