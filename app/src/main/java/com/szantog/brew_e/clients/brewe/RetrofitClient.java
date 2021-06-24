package com.szantog.brew_e.clients.brewe;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitApi instance = null;

    private static RetrofitApi getRetrofitApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        return retrofitApi;
    }

    public static synchronized RetrofitApi getInstance() {
        if (instance == null) {
            instance = getRetrofitApi();
        }
        return instance;
    }

}
