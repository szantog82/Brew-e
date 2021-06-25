package com.szantog.brew_e.ui.browse;

import android.util.Log;
import android.view.View;

import com.szantog.brew_e.clients.brewe.RetrofitClient;
import com.szantog.brew_e.domain.CoffeeShop;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowseViewModel extends ViewModel {

    private final MutableLiveData<List<CoffeeShop>> coffeeShopList = new MutableLiveData<>();

    public LiveData<List<CoffeeShop>> getCoffeeShopList() {
        return coffeeShopList;
    }

    public void downloadShopList() {
        if (coffeeShopList.getValue() == null || coffeeShopList.getValue().size() == 0) {
            Call<List<CoffeeShop>> call = RetrofitClient.getInstance().getCoffeeShops();
            call.enqueue(new Callback<List<CoffeeShop>>() {
                @Override
                public void onResponse(Call<List<CoffeeShop>> call, Response<List<CoffeeShop>> response) {
                    coffeeShopList.postValue(response.body());

                }

                @Override
                public void onFailure(Call<List<CoffeeShop>> call, Throwable t) {
                    Log.e("downloadShopsForMap", t.getMessage());
                }
            });
        }
    }
}
