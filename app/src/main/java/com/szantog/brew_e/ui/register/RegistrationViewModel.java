package com.szantog.brew_e.ui.register;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szantog.brew_e.domain.BlogItem;
import com.szantog.brew_e.domain.CoffeeShop;
import com.szantog.brew_e.domain.DrinkItem;
import com.szantog.brew_e.clients.brewe.dtos.DrinkItemRequest;
import com.szantog.brew_e.clients.brewe.dtos.AuthResponse;
import com.szantog.brew_e.domain.User;
import com.szantog.brew_e.clients.brewe.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel extends ViewModel {

    private final MutableLiveData<AuthResponse> registrationResponse = new MutableLiveData<>();


    public void uploadUserRegistration(User user) {
        Gson gson = new GsonBuilder().setLenient().create();
        String json_string = gson.toJson(user);
        Call<AuthResponse> call = RetrofitClient.getInstance().registerUser(json_string);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                AuthResponse r = response.body();
                registrationResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
            }
        });
    }

    public LiveData<AuthResponse> getRegistrationresponse() {
        return registrationResponse;
    }


}
