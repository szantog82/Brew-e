package com.szantog.brew_e.ui.ordermenu;

import android.util.Log;

import com.szantog.brew_e.clients.brewe.RetrofitClient;
import com.szantog.brew_e.domain.DrinkItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderMenuViewModel extends ViewModel {

    private final MutableLiveData<List<DrinkItem>> drinkItems = new MutableLiveData<>();

    public void downloadMenuItems(int shopId) {
        Call<List<DrinkItem>> call = RetrofitClient.getInstance().getDrinkMenu(shopId);
        call.enqueue(new Callback<List<DrinkItem>>() {
            @Override
            public void onResponse(Call<List<DrinkItem>> call, Response<List<DrinkItem>> response) {
                drinkItems.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DrinkItem>> call, Throwable t) {
                Log.e("downloadMenu failure", t.getMessage());
            }
        });
    }

    public LiveData<List<DrinkItem>> getDrinkMenuItems() {
        return drinkItems;
    }
}
