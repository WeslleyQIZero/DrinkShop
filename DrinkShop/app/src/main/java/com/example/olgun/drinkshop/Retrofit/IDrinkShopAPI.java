package com.example.olgun.drinkshop.Retrofit;

import com.example.olgun.drinkshop.Model.CheckUserResponse;
import com.example.olgun.drinkshop.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IDrinkShopAPI {

    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("email") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("name") String name,
                               @Field("surname") String surname,
                               @Field("address") String address,
                               @Field("email") String email,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("email") String email,
                                  @Field("password") String password);
}
