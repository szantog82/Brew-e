package com.szantog.brew_e;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitApi {
    public String BASE_URL = "http://www.szantog82.nhely.hu";

    @GET("get_shops")
    Call<List<CoffeeShop>> getCoffeeShops();

    @GET("get_drink_menu/{shop_id}")
    Call<List<DrinkMenu>> getDrinkMenu(@Path("shop_id") int shop_id);

}
