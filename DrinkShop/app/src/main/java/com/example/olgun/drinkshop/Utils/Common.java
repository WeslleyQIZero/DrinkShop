package com.example.olgun.drinkshop.Utils;

import com.example.olgun.drinkshop.Model.User;
import com.example.olgun.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.olgun.drinkshop.Retrofit.RetrofitClient;

public class Common {
    //In Emulator, localhost = 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2/drinkshop/php/";

    public static User currentUser = null;

    public static IDrinkShopAPI getAPI() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}
