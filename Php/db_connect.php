<?php

class DB_Connect {

	private $conn;

	public function connect() {

		require_once 'config.php'; //will call this file once not in every page
		$this->conn = new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE); //will connect to the database using the credentials that we have created in the **config.php** file.
		return $this->conn;
	}
}

?>