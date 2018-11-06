package com.example.olgun.drinkshop.Retrofit;

import com.example.olgun.drinkshop.Model.CheckUserResponse;
import com.example.olgun.drinkshop.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public abstract class IDrinkShopAPI {

    @FormUrlEncoded
    @POST("checkuser.php")
    abstract Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    abstract Call<User> registerNewUser(@Field("phone") String phone,
                                        @Field("name") String name,
                                        @Field("address") String address,
                                        @Field("birthdate") String birthdate);
}
