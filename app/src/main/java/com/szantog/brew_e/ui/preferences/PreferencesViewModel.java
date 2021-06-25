package com.szantog.brew_e.ui.preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szantog.brew_e.clients.brewe.RetrofitClient;
import com.szantog.brew_e.domain.User;

import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreferencesViewModel extends ViewModel {

    public void uploadUpdatedUserData(String session_id, User user) {
        Gson gson = new GsonBuilder().setLenient().create();
        String json_string = gson.toJson(user);
        Call<Void> call = RetrofitClient.getInstance().modifyUser("PHPSESSID=" + session_id, json_string);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}
