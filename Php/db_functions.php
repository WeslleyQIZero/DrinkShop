<?php

class DB_Functions{

	private $conn;

	function __construct()
	{
		require_once 'db_connect.php';
		$db = new DB_Connect();
		$this->conn = $db->connect();
	}

	function __destruct()
	{
		//TODO: Implement __destruct() method.
	}

	/*
	 * Check user exists
	 * return true/false
	 */
	function checkExistsUser($phone)
	{	

		/* Because of we want to secure database, we are going to use **PDO** for PHP*/

		//The SQL statement as following will be executed whenever this function is called
		$stmt = $this->conn->prepare("SELECT * FROM User WHERE Phone=?");
		$stmt->bind_param("s",$phone); //"s" defines the parameter number,so if there was "ss" means two parameter mus be passed to this function (bind_param) which is PDO function
		$stmt->execute(); //will execute this PDO function
		$stmt->store_result(); //the result will be stored as well

		if($stmt->num_rows > 0){

			$stmt->close();
			return true;
		}else {
			$stmt->close();
			return false;
		}
	}

	/*
	 * Register new user
	 * return User object if user was created
	 * return error message if have exception
	 */
	public function registerNewUser($phone,$name,$birthdate,$address)
	{
		//The SQL statement as following will be executed whenever this function is called
		$stmt = $this->conn->prepare("INSERT INTO User(Phone,Name,Birthdate,Address) VALUES(?,?,?,?)");
		$stmt->bind_param("ssss",$phone,$name,$birthdate,$address);
		$result = $stmt->execute();
		$stmt->store_result();

		if($result) {
			$stmt = $this->conn->prepare("SELECT * FROM User WHERE Phone = ?");
			$stmt->bind_param("s",$phone);
			$stmt->execute();
			$user = $stmt->get_result()->fetch_assoc(); 

			/*fetch_assoc(); = we want to get all the information of the user like 

			  Phone: ....
			  Name: .....
			  Birthday: ...
			  Address: ....

			*/

			$stmt->close();
			return $user;
		}else
			return false;
	}

	/*
	 * Get User Information
	 * return User object if user was created
	 * return false if user is not exists
	 */
	public function getUserInformation($phone) {

		$stmt = $this->conn->prepare("SELECT * FROM User WHERE Phone=?");
		$stmt->bind_param("s",$phone);

		if($stmt->execute()) {

			$user = $stmt->get_result()->fetch_assoc();
			$stmt->close();

			return $user;
		}
		else
			return NULL;
	}
}