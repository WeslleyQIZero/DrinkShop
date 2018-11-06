package com.example.olgun.drinkshop.Utils;

import com.example.olgun.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.olgun.drinkshop.Retrofit.RetrofitClient;

public class Common {
    //In Emulator, localhost = 10.2.2.2
    private static final String BASE_URL = "http://localhost/drinkshop/";

    private static IDrinkShopAPI getApi() {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}
