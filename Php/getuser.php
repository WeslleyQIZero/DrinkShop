<?php

	require_once 'db_functions.php';
	$db = new DB_Functions();

	/*
	 * Endpoint : https://<domain>/drinkshop/getuser.php
	 * Method : POST
	 * Params : phone
	 * Result : JSON
	 */

	$response = array();
	if(isset($_POST['phone']))
	{
		$phone = $_POST['phone'];

			//Create new user
		$user = $db->getUserInformation($phone);
		if($user) {

			$response["phone"] = $user["Phone"];
			$response["name"] = $user["Name"];
			$response["birthdate"] = $user["Birthdate"];
			$response["address"] = $user["Address"];
			echo json_encode($response);
		}else {

			$response["error_msg"] = "User does not exist";
			echo json_encode($response);
		}
	}
	else {
		$response["error_msg"] = "Required parameter (phone,name,birthdate,address) is missing!";
		echo json_encode($response);
	}

?>