package com.example.olgun.drinkshop;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olgun.drinkshop.Model.CheckUserResponse;
import com.example.olgun.drinkshop.Model.User;
import com.example.olgun.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.olgun.drinkshop.Utils.Common;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btn_continue;
    TextView btn_register;

    IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = Common.getAPI();

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

                        //Log.d("User",user.getError_msg().toString());

                        if(TextUtils.isEmpty(user.getError_msg())) {
                            Toast.makeText(MainActivity.this,"User register succesfully",Toast.LENGTH_SHORT).show();
                            //Start new activity
                            //Assign json result to currentUser
                            Common.currentUser = response.body();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            finish(); //will close this activity
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("Connection", t.toString());
                }
        });
    }
}
