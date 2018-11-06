# Setup Project & PHP Backend

In this tutorial, we are going to learn how to intialize our project in the **Android Studio** and we will create **php** backend as well.

## Initialize Project

Create a new project in the **Android Studio** and choose **API 21: Android 5.0 (Lollipop)**.

## Libraries

We are going to install some external libraries to our application, So lets add the following lines to the **Gradle Scripts/build.gradle (Module: app)**.

~~~~

    implementation 'com.android.support:cardview-v7:27.1.1'
    implementation 'com.android.support:design:27.1.1'
    implementation 'com.rengwuxian.materialedittext:library:2.1.4'
    implementation 'com.szagurskii:patternedtextwatcher:0.5.0'
    implementation 'com.github.d-max:spots-dialog:0.7@aar'
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'

~~~~

## MySQL

Now, we need to create a database for our **app**. So lets go to the **localhost/phpMyAdmin** which is the web tool of the **MySQL** that i am going to create database.

If you are using another tool to create database its all fine.

Create a new **Database** in the **phpMyAdmin** called as **DrinkShop**.

Then create a table called **user** and make sure that the table columns are looks like as below.

~~~~


| Name      | Type    | Length/Values |
| --------- | ------- | ------------- |
| Phone     | VARCHAR | 20            |
| Name      | VARCHAR | 50            |
| Birthdate | DATE    |               |
| Address   | TEXT    |               |


Make sure that **Phone** column is a primary key but not autoincrement.

~~~~

## Web Services

In this tutorial, we are going to write 2 method for our **web services**

1. Check exists User
2. Register New User

## Web Services (config.php)

To being structured and following OOP principles, We are going to create different files and folders for this project.

So, lets create a folder called **DrinkShop** and add a file called **config.php** to your **xampp** or **wampp** server or If you are using **Linux** and using **apache2** serverthen create those in the **var/www** folder.

For the **config.php** we have to write our database credentials, So lets add the following lines to this folder. I've explained every single line code as well.

~~~~

<?php

	/*
	 * Database Config Information
	 */

define("DB_HOST","localhost");
define("DB_USER","your_database_user_name");
define("DB_PASSWORD","your_database_user_password");
define("DB_DATABAse","DrinkShop");

?>


~~~~

## Web Services (db_connect.php)

The another file that we are going to create is **db_connect.php** file which we'll be used as **DB_Connect** class. So we will just create an instance (object) from this class and we will be connected to the **database**.

So, lets add the following lines to this file.

~~~~

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

~~~~

## Web Services (db_functions.php)

Now, we are going to write some functions for our **db_connect.php**. So lets create another file called as **db_functions.php** and add the following lines.

~~~~

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
        $stmt = $this->conn->prepare("INSERT INTO User(Phone,Name,Birthday,Address) VALUES(?,?,?,?)");
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
}

~~~~

## Web Services (checkuser.php)

Lets create a php file called **checkuser.php** to check whether the userphone is already existed in the database or not.

~~~~

<?php

    require_once 'db_functions.php';
    $db = new DB_Functions();

    /*
     * Endpoint : https://<domain>/drinkshop/checkuser.php
     * Method : POST
     * Params : phone
     * Result : JSON
     */

    $response = array();
    if(isset($_POST['phone']))
    {
        $phone = $_POST['phone'];
        if($db->checkExistsUser($phone)){

            $response["exists"] = TRUE;
            echo json_encode($response);

        } else {

            $response["exists"] = FALSE;
            echo json_encode($response);
        }
    }
    else {
        $response["error_msg"] = "Required parameter (phone) is missing!";
        echo json_encode($response);
    }

?>

~~~~

## Model CheckUserResponse in **Android**

Now, we are going to create a data model for this **checkuser.php** response in android.

So, lets create a folder in the **app/java/com.example.username.projectname** as called as **Model**.

Then in this folder create a **java** file called as **CheckUserResponse** and add the following lines.

~~~~

public class CheckUserResponse {

    private boolean exists;
    private String error_msg;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}

~~~~

## Web Services (register.php)

Lets create a php file called **register.php** to check whether the userphone is already existed in the database or not then if it is not existed then create the user.

~~~~

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
    if(isset($_POST['phone']) &&
       isset($_POST['name']) &&
       isset($_POST['birthday']) &&
       isset($_POST['address']))
    {
        $phone = $_POST['phone'];
        $name = $_POST['name'];
        $birthday = $_POST['birthday'];
        $address = $_POST['address'];

        if($db->checkExistsUser($phone)){

            $response["exists"] = "User already existed with ".$phone;
            echo json_encode($response);

        } else {

            //Create new user
            $user = $db->registerNewUser($phone,$name,$birthday,$address);
            if($user) {

                $response["phone"] = $user["Phone"];
                $response["name"] = $user["Name"];
                $response["birthdate"] = $user["Birthdate"];
                $response["address"] = $user["Address"];
                echo json_encode($response);
            }else {

                $response["error_msg"] = "Unkown error occured in registeration";
            }
        }
    }
    else {
        $response["error_msg"] = "Required parameter (phone,name,birthdate,address) is missing!";
        echo json_encode($response);
    }

?>

~~~~

## Model User in **Android**

Lets create a class in the **app/com.example.username.projectname/Model** folder as **User** and add the following lines.

~~~~

public class User {

    private String phone;
    private String address;
    private String name;
    private String birthdate;
    private String error_msg; //It will be empty if user return object

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}


~~~~

## Retrofit Interface (IDrinkShopAPI)

Now we have to set up **Retrofit** for our android app to send HTTP request to the server. It is a very simple **HTTP Client** library for Android and Java.

Create a folder called **Retrofit** and in this folder create a **Java Interface** called as **IDrinkShopAPI**.

Then, add the following lines.

~~~~

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public abstract class IDrinkShopAPI {
    
    /* We will send **POST** request, so we have to encode the URL */

    @FormUrlEncoded
    @POST("checkuser.php") //URL to send request            //Field will be send
    abstract Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    abstract Call<User> registerNewUser(@Field("phone") String phone,
                                        @Field("name") String name,
                                        @Field("address") String address,
                                        @Field("birthdate") String birthdate);
}

~~~~

## Retrofit Client (RetrofitClient)

Now, we have to create an **Client Class** to use to send request to the urls. So lets create a **Java Class** called **RetrofitClient** and add the following lines.

~~~~

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {

        if(retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl) //the URL will send request
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}

~~~~

**Library Definition: **

1.  GSON - [Link](https://github.com/google/gson)

Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Gson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of.

There are a few open-source projects that can convert Java objects to JSON. However, most of them require that you place Java annotations in your classes; something that you can not do if you do not have access to the source-code. Most also do not fully support the use of Java Generics. Gson considers both of these as very important design goals.

Goals

* Provide simple toJson() and fromJson() methods to convert Java objects to JSON and vice-versa
* Allow pre-existing unmodifiable objects to be converted to and from JSON
* Extensive support of Java Generics
* Allow custom representations for objects
* Support arbitrarily complex objects (with deep inheritance hierarchies and extensive use of generic types)

## Utils (Common)

Now, lets create a folder called **Utils** then create a **Java Class** in this folder as **Common**. We are going to use this folder for general purpose requirements and also we are going to use this **common** class for common requirements like **API Endpoints**.

So lets add the following lines to this file.

~~~~

import com.example.olgun.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.olgun.drinkshop.Retrofit.RetrofitClient;

public class Common {
    //In Emulator, localhost = 10.2.2.2
    private static final String BASE_URL = "http://localhost/drinkshop/";
    
    //This one, will run **RetrofitClient** class and **getClient** function in this file.    
    private static IDrinkShopAPI getApi() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}

~~~~

## Assets

Now, we are going to create the **UI** of this app. So firstly lets get the assets from - [Link](./Assets) folder and put them to your **drawable** folder in your application by following the steps.

### Download and Install Android Drawable Importer

1. Go to File/Settings/Plugins
2. Search A Plugin Called **Android Drawable Importer**
3. If Not Found Click To **Search Repository**
4. Click To Install
5. Restart

### Create drawables

1. Right Click to **res/drawable** folder
2. Select **Batch Drawable Import**
3. Click to **+**
4. Select **drink_shop_bg.jpg** and **bg.jpg**

And you will see that, In the **res/drawable** folder automatically this plugin will generated image.

## Layout (activity_main.xml)

Now, lets customize our **activity_main.xml** file for UI. So lets add the following lines to this file.

~~~~

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@drawable/drink_shop_bg"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/btn_continue"
        android:text="Continue"
        android:background="@color/colorPrimaryDark"
        android:layout_alignParentBottom="true"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <!-- android:layout_alignParentBottom="true" PUTS THE BUTTON TO THE BOTTOM -->

</RelativeLayout>

~~~~

## Styles

In the **values/style.xml** lets make some changes to not show **ActionBar**. So lets change the **style** to the **parent="Theme.AppCompact.Light.NoActionBar"**

## Colors

In **values/colors.xml** change colors to:

1. **colorPrimary** to **#d4e7e1**
2. **colorPrimaryDark** to **#a3b6ae**
3. **colorAccent** to **#1e1e1e*