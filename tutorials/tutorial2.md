# Register Account

In this tutorial, we are going to learn how to add a functionality to the android app and make sure that users will be registered succesfully.

## Layout (register_layout.xml)

Lets create a layout called **register_layout.xml** in the **res/layout** folder. So we can design our register page under this file.

Then, add the following xml to it.

~~~~

<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardElevation="16dp"
    app:cardCornerRadius="8dp"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    
    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        
        <LinearLayout
            android:orientation="vertical"
            android:layout_margin="16dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <com.rengwuxian.materialedittext.MaterialEditText
                android:id="@+id/edt_phone"
                android:hint="Phone"
                android:textSize="18sp"
                android:inputType="text"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

            <com.rengwuxian.materialedittext.MaterialEditText
                android:id="@+id/edt_name"
                android:hint="Name"
                android:textSize="18sp"
                android:inputType="text"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

            <com.rengwuxian.materialedittext.MaterialEditText
                android:id="@+id/edt_address"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Address"
                android:inputType="text"
                android:textSize="18sp"
                app:met_floatingLabel="highlight" />

            <com.rengwuxian.materialedittext.MaterialEditText
                android:id="@+id/edt_birthday"
                android:hint="Birthdate (YYYY-MM-DD)"
                android:textSize="20sp"
                android:digits="1234567890-"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

        </LinearLayout>

        <Button
            android:id="@+id/btn_register"
            android:text="Continue"
            android:background="@color/colorPrimaryDark"
            android:layout_alignParentBottom="true"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

    </LinearLayout>
</android.support.v7.widget.CardView>

~~~~

## Activity (MainActivity)

In the **MainActivity** we have to add a **setOnClickListener** to make sure that, whenever the button that we defined in the **activity_main.xml** which is the first layout page of our application will show the **register** layout that we've created just before that.

Lets add the following lines to this file.

~~~~

    Button btn_continue;

    IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getAPI(); //will create a retrofit object, that we are going to use to send request to the our **BASE_URL**.

        btn_continue = (Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog(); //will call the **showRegisterDialog()** function.
            }
        });
    }

~~~~

## Custom Function (ShowRegisterDialog)

In our **MainActivity** we have to create a function (method) called as **showRegisterDialog()** to show the **register** layout.

Lets add the following function to this file.

~~~~

    private void showRegisterDialog() {
		
		//We are going to create a alert builder which will be used to show alert dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //To Define Alert Dialog Title
        builder.setTitle("REGISTER");

        LayoutInflater inflater = this.getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);

        final MaterialEditText edt_phone = (MaterialEditText)register_layout.findViewById(R.id.edt_phone);
        final MaterialEditText edt_name = (MaterialEditText)register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_address = (MaterialEditText)register_layout.findViewById(R.id.edt_address);
        final MaterialEditText edt_birthday = (MaterialEditText)register_layout.findViewById(R.id.edt_birthday);

        Button btn_register = (Button)register_layout.findViewById(R.id.btn_register);

        edt_birthday.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        builder.setView(register_layout);
        builder.show();

        //To Create Alert Dialog object by using builder to create it
        final AlertDialog dialog = builder.create();

        //Event -> Whenever register button is clicked then this will be worked
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Will close it
                dialog.dismiss();

                if(TextUtils.isEmpty(edt_phone.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your phone", Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_address.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please enter your address",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_birthday.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please enter your birthdate",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_name.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please enter your name",Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("Phone",edt_phone.toString());
                Log.d("Name",edt_name.toString());
                Log.d("Address",edt_address.toString());
                Log.d("Birthday",edt_birthday.toString());
				
				//Will show Please waiting message 
                final SpotsDialog watingDialog = new SpotsDialog(MainActivity.this);
                watingDialog.show();
                watingDialog.setMessage("Please waiting...");
				
				//will send a request to the url/checkuser.php with the parameters .php?phone
                mService.checkUserExists(edt_phone.getText().toString()).enqueue(new Callback<CheckUserResponse>() {
                    @Override
                    public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {

                        CheckUserResponse userResponse = response.body();

                        if(userResponse.isExists()) {

                            //If User already exists, just start new Activity
                            watingDialog.dismiss();
                            Log.d("User","User is Existed");
                        }
                        else {
                            Log.d("User","User is Not Existed. So, can create a new one");

                            //If phone is not existed in the database, call the register function
                            register(edt_phone.getText().toString(),edt_name.getText().toString(),edt_address.getText().toString(),edt_birthday.getText().toString());
                            watingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                        watingDialog.dismiss();
                        Log.d("Connection",t.toString());
                    }
                });

            }

            //Close Dialog
        });

        //dialog.show();
    }

~~~~

## Custom Function (Register)

The Register function is used to send request to the related web service to register the user.

So, lets add the following lines to **MainActivity** class.

~~~~

    private void register(String phone, String name, String address, String birthday) {

        Log.d("Phone",phone.toString());
        Log.d("Name",name.toString());
        Log.d("Address",address.toString());
        Log.d("Birthday",birthday.toString());

        mService.registerNewUser(phone,name,address,birthday)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User user = response.body();

                        //Log.d("User",user.getError_msg().toString());
						
						//If user.getError_msg() is empty that means we do not have any error, and we have created the user successfully.
                        if(TextUtils.isEmpty(user.getError_msg())) {
                            Toast.makeText(MainActivity.this,"User register succesfully",Toast.LENGTH_SHORT).show();
                            //Start new activity
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Connection", t.toString());
                }
        });
    }

~~~~

Whenever this function is called, it will send request to the **url/register.php?phone=sameple&name=sample&address=sample&birthday=sample**. The response will be converted to the **User** object which means the **retrofit** automatically will convert the returned **JSON** to the model. This is one of the power of to use retrofit. Because we do not need to parse JSON.

