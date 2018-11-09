<?php

	require_once 'db_functions.php';
	$db = new DB_Functions();

	/*
	 * Endpoint : https://<domain>/drinkshop/register.php
	 * Method : POST
	 * Params : phone, name, birthday, address
	 * Result : JSON
	 */

	$response = array();
	if(isset($_POST['name']) &&
	   isset($_POST['surname']) &&
	   isset($_POST['address']) &&
	   isset($_POST['email']) &&
	   isset($_POST['password']))
	{
		$name = $_POST['name'];
		$surname = $_POST['surname'];
		$address = $_POST['address'];
		$email = $_POST['email'];
		$password = $_POST['password'];

		if($db->checkExistsUser($email)){

			$response["exists"] = "User already existed with ".$email;
			echo json_encode($response);

		} else {

			//Create new user
			$user = $db->registerNewUser($name,$surname,$address,$email,$password);
			if($user) {

				$response["name"] = $user["Name"];
				$response["surname"] = $user["Surname"];
				$response["address"] = $user["Address"];
				$response["email"] = $user["Email"];
				echo json_encode($response);
			}else {

				$response["error_msg"] = "Unkown error occured in registeration";
			}
		}
	}
	else {
		$response["error_msg"] = "Required parameter (name,surname,address,email,password) is missing!";
		echo json_encode($response);
	}

?>