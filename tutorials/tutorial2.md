# Login & Register Account

In this tutorial, we are going to learn how to add a functionality to the android app and make sure that users will be registered succesfully and also can be logged in as well.

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
            android:layout_margin="20dp"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <com.rengwuxian.materialedittext.MaterialEditText
                app:met_minCharacters="2"
                android:id="@+id/edt_name"
                android:hint="Name"
                android:textSize="20sp"
                android:inputType="text"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

            <com.rengwuxian.materialedittext.MaterialEditText
                app:met_minCharacters="2"
                android:id="@+id/edt_surname"
                android:hint="Surname"
                android:textSize="20sp"
                android:inputType="text"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

            <com.rengwuxian.materialedittext.MaterialEditText
                app:met_minCharacters="10"
                android:id="@+id/edt_address"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Address"
                android:inputType="text"
                android:textSize="20sp"
                app:met_floatingLabel="highlight" />

            <com.rengwuxian.materialedittext.MaterialEditText
                app:met_minCharacters="4"
                android:id="@+id/edt_email"
                android:hint="Email"
                android:textSize="20sp"
                android:inputType="textEmailAddress"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

            <com.rengwuxian.materialedittext.MaterialEditText
                app:met_minCharacters="8"
                android:id="@+id/edt_password"
                android:hint="Password"
                android:textSize="20sp"
                android:inputType="textPassword"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

            <com.rengwuxian.materialedittext.MaterialEditText
                app:met_minCharacters="8"
                android:id="@+id/edt_confirm"
                android:hint="Confirm Password"
                android:textSize="20sp"
                android:inputType="textPassword"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:met_floatingLabel="highlight"/>

        </LinearLayout>

        <Button
            android:textSize="18sp"
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
    TextView btn_register;

    IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getAPI(); //will create a retrofit object, that we are going to use to send request to the our **BASE_URL**.
        
        ....

        btn_register = (TextView)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });
    }

~~~~

## Custom Function (ShowRegisterDialog)

In our **MainActivity** we have to create a function (method) called as **showRegisterDialog()** to show the **register** layout.

Lets add the following function to this class.

~~~~

    private void showRegisterDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        //To Define Alert Dialog Title
        builder.setTitle("REGISTER");

        LayoutInflater inflater = this.getLayoutInflater();
        View register_layout = inflater.inflate(R.layout.register_layout, null);

        final MaterialEditText edt_name = (MaterialEditText)register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_surname = (MaterialEditText)register_layout.findViewById(R.id.edt_surname);
        final MaterialEditText edt_address = (MaterialEditText)register_layout.findViewById(R.id.edt_address);
        final MaterialEditText edt_email = (MaterialEditText)register_layout.findViewById(R.id.edt_email);
        final MaterialEditText edt_password = (MaterialEditText)register_layout.findViewById(R.id.edt_password);
        final MaterialEditText edt_confirm = (MaterialEditText)register_layout.findViewById(R.id.edt_confirm);

        Button btn_register = (Button)register_layout.findViewById(R.id.btn_register);

        //edt_birthday.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        builder.setView(register_layout);
        builder.show();

        //To Create Alert Dialog object
        final AlertDialog dialog = builder.create();

        //Event
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Will close it
                dialog.dismiss();

                if(TextUtils.isEmpty(edt_name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_surname.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please enter your Surname",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_address.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please enter your Address",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_email.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please enter your Email",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_password.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please enter your Password",Toast.LENGTH_SHORT).show();
                    return;
                } else if(TextUtils.isEmpty(edt_confirm.getText().toString())) {
                    Toast.makeText(MainActivity.this,"Please confirm Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!edt_password.getText().toString().equalsIgnoreCase(edt_confirm.getText().toString())){
                    Toast.makeText(MainActivity.this, "Password & Confirm Password does not match",Toast.LENGTH_SHORT).show();
                    return;
                }

                final SpotsDialog watingDialog = new SpotsDialog(MainActivity.this);
                watingDialog.show();
                watingDialog.setMessage("Please waiting...");

                mService.checkUserExists(edt_email.getText().toString()).enqueue(new Callback<CheckUserResponse>() {
                    @Override
                    public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {

                        CheckUserResponse userResponse = response.body();

                        if(userResponse.isExists()) {

                            //If User already exists, just start new Activity
                            watingDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Email already is used",Toast.LENGTH_SHORT).show();
                            Log.d("User","User is Existed");
                        }
                        else {
                            Log.d("User","User is Not Existed. So, can create a new one");
                            register(edt_name.getText().toString(),edt_surname.getText().toString(),edt_address.getText().toString(),edt_email.getText().toString(),edt_password.getText().toString());
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

  private void register(String name, String surname, String address, String email, String password) {

        /*Log.d("Phone",phone.toString());
        Log.d("Name",name.toString());
        Log.d("Address",address.toString());
        Log.d("Birthday",birthday.toString());*/

        mService.registerNewUser(name,surname,address,email,password)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User user = response.body();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Connection", t.toString());
                }
        });
    }


~~~~

Whenever this function is called, it will send request to the **url/register.php**. The response will be converted to the **User** object which means the **retrofit** automatically will convert the **JSON** whatever returned to the model. This is one of the power of to use retrofit. Because of that we do not need to parse JSON.

