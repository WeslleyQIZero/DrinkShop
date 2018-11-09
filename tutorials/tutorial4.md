# Log In Account

In this tutorial, we are going to implement **Log In** functionality for app user.

## PHP - (getuser.php)

To control the **email** and **password** fields in the application, we have to send a request to an endpoint to check whether provided **emaill** and **passwords** are matching on a user.

So, lets create a php file called **getuser.php** and then add the following lines to this file.

~~~~

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

~~~~

Basically, whenever a request comes to **localhost/drinkshop/php/getuser.php** with **email** and **password** attributes then logically it will be checked whether a user has those attributes. If so, then as response the **user** informations will be returned as in **JSON** format.

## HTTP API Resolver - IDrinkShopAPI

In the **IDrinkShopAPI** interface, we have to create another **method** to send a request to the **localhost/drinkshop/php/getuser.php** whenever it has been called.

So, lets add the following lines to this file.

~~~~

    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("email") String email,
                                  @Field("password") String password);

~~~~

## Log In 

In the **MainActivity** class we need to write a function to call the **HTTP API Resolver** for to send request to the **localhost/drinkshop/php/getuser.php**.

So lets add the following function to this class.

~~~~

    private void logIn() {

        final MaterialEditText edt_email = (MaterialEditText)findViewById(R.id.edt_email);
        final MaterialEditText edt_password = (MaterialEditText)findViewById(R.id.edt_password);

        if(TextUtils.isEmpty(edt_email.getText().toString())) {
            Toast.makeText(MainActivity.this,"Please enter your Email",Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(edt_password.getText().toString())) {
            Toast.makeText(MainActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }

        final SpotsDialog watingDialog = new SpotsDialog(MainActivity.this);
        watingDialog.show();
        watingDialog.setMessage("Please waiting...");

        mService.getUserInformation(edt_email.getText().toString(),edt_password.getText().toString()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                User user = response.body();
                watingDialog.dismiss();

                if(TextUtils.isEmpty(user.getError_msg())) {
                    Common.currentUser = response.body();
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    finish(); //will close this activity
                } else {
                    Toast.makeText(MainActivity.this,"Email or password is wrong!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Connection", t.toString());
            }
        });
    }

~~~~

## Attach Log In Function To Button

Up to here, we have created **php api file**, **HTTP API Resolver Interface** and **Log In** function. So we have to attach this function to a button's click events to make sure that whenever the button is clicked then this function will be called.

So, In the **MainActivity** lets add the following lines to main method.

~~~~

    Button btn_continue;
    TextView btn_register;

    IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getAPI();
		
		/* This is what we've added now */
        btn_continue = (Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        btn_register = (TextView)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });

    }
~~~~
