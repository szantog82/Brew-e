package com.szantog.brew_e.ui;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szantog.brew_e.clients.brewe.RetrofitClient;
import com.szantog.brew_e.clients.brewe.dtos.AuthResponse;
import com.szantog.brew_e.clients.brewe.dtos.DrinkItemRequest;
import com.szantog.brew_e.domain.DrinkItem;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendOrderViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isOrderUploadSuccess = new MutableLiveData<>();

    public void uploadOrder(String session_id, List<DrinkItem> bucket) {
        List<DrinkItemRequest> bucketForUpload = new ArrayList<>();

        //convert DrinkItem to DrinkItemForUpload
        for (DrinkItem item : bucket) {
            boolean found = false;
            for (DrinkItemRequest o : bucketForUpload) {
                if (o.getItem_id() == item.getId()) {
                    found = true;
                    o.setItem_count((o.getItem_count() + 1));
                    break;
                }
            }
            if (!found) {
                bucketForUpload.add(new DrinkItemRequest(item.getId(), 1, item.getShop_id()));
            }
        }
        Gson gson = new GsonBuilder().setLenient().create();
        String json_string = gson.toJson(bucketForUpload);
        Call<AuthResponse> call = RetrofitClient.getInstance().uploadOrder("PHPSESSID=" + session_id, json_string);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                AuthResponse r = response.body();
                if (r != null && r.getSuccess() == 1) {
                    isOrderUploadSuccess.postValue(true);
                } else {
                    isOrderUploadSuccess.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                isOrderUploadSuccess.postValue(false);
                Log.e("uploadOrder", t.getMessage());

            }
        });
    }

    public LiveData<Boolean> isUploadSuccess() {
        return isOrderUploadSuccess;
    }
}
