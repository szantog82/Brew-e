package com.szantog.brew_e.viewmodel;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szantog.brew_e.model.BlogItem;
import com.szantog.brew_e.model.CoffeeShop;
import com.szantog.brew_e.model.DrinkItem;
import com.szantog.brew_e.model.DrinkItemForUpload;
import com.szantog.brew_e.model.ResponseModel;
import com.szantog.brew_e.model.User;
import com.szantog.brew_e.model.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitListViewModel extends ViewModel {

    private final MutableLiveData<Boolean> is_Up_Downloading = new MutableLiveData<>();

    private final MutableLiveData<User> userData = new MutableLiveData<>();
    private final MutableLiveData<String> session_id = new MutableLiveData<>();
    private final MutableLiveData<List<CoffeeShop>> coffeeShopList = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedShopId = new MutableLiveData<>();
    private final MutableLiveData<List<DrinkItem>> drinkItems = new MutableLiveData<>();
    private final MutableLiveData<List<BlogItem>> blogItems = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isOrderUploadSuccess = new MutableLiveData<>();
    private final MutableLiveData<ResponseModel> registrationResponse = new MutableLiveData<>();

    public LiveData<Boolean> isDownloading() {
        return is_Up_Downloading;
    }

    public void logoutUser(String session_id) {
        is_Up_Downloading.setValue(true);
        Call<Void> call = RetrofitClient.getInstance().logoutUser("PHPSESSID=" + session_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                userData.postValue(null);
                is_Up_Downloading.postValue(false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                is_Up_Downloading.setValue(false);
            }
        });
    }

    public LiveData<Integer> getSelectedShopId() {
        return selectedShopId;
    }

    public LiveData<User> getUser() {
        return userData;
    }

    public LiveData<String> getSessionId() {
        return session_id;
    }

    public void loginUser(String login, String password) {
        is_Up_Downloading.setValue(true);
        Call<ResponseModel> call = RetrofitClient.getInstance().loginUser(login, password);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                is_Up_Downloading.postValue(false);
                ResponseModel responseModel = response.body();
                if (responseModel.getSession_id().length() > 0) {
                    testConnection(responseModel.getSession_id());
                    RetrofitListViewModel.this.session_id.setValue(responseModel.getSession_id());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                is_Up_Downloading.postValue(false);
            }
        });
    }

    public void uploadUserRegistration(User user) {
        is_Up_Downloading.setValue(true);
        Gson gson = new GsonBuilder().setLenient().create();
        String json_string = gson.toJson(user);
        Call<ResponseModel> call = RetrofitClient.getInstance().registerUser(json_string);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel r = response.body();
                is_Up_Downloading.postValue(false);
                registrationResponse.postValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                is_Up_Downloading.postValue(false);
            }
        });
    }

    public LiveData<ResponseModel> getRegistrationresponse() {
        return registrationResponse;
    }

    public void testConnection(String session_id) {
        is_Up_Downloading.setValue(true);
        Call<User> call = RetrofitClient.getInstance().testUserConnection("PHPSESSID=" + session_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                is_Up_Downloading.postValue(false);
                User user = response.body();
                if (user != null) {
                    userData.setValue(user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                is_Up_Downloading.postValue(false);
            }
        });
    }

    public LiveData<List<CoffeeShop>> getCoffeeShopList() {
        return coffeeShopList;
    }

    public void downloadShopList() {
        if (coffeeShopList.getValue() == null || coffeeShopList.getValue().size() == 0) {
            is_Up_Downloading.setValue(true);
            Call<List<CoffeeShop>> call = RetrofitClient.getInstance().getCoffeeShops();
            call.enqueue(new Callback<List<CoffeeShop>>() {
                @Override
                public void onResponse(Call<List<CoffeeShop>> call, Response<List<CoffeeShop>> response) {
                    coffeeShopList.postValue(response.body());
                    is_Up_Downloading.postValue(false);

                }

                @Override
                public void onFailure(Call<List<CoffeeShop>> call, Throwable t) {
                    Log.e("downloadShopsForMap", t.getMessage());
                    is_Up_Downloading.postValue(false);
                }
            });
        }
    }

    public void setSelectedShopId(int shop_id) {
        selectedShopId.setValue(shop_id);
    }

    public void downloadMenuItems() {
        int shop_id = selectedShopId.getValue();
        is_Up_Downloading.setValue(true);
        Call<List<DrinkItem>> call = RetrofitClient.getInstance().getDrinkMenu(shop_id);
        call.enqueue(new Callback<List<DrinkItem>>() {
            @Override
            public void onResponse(Call<List<DrinkItem>> call, Response<List<DrinkItem>> response) {
                is_Up_Downloading.postValue(false);
                drinkItems.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DrinkItem>> call, Throwable t) {
                is_Up_Downloading.postValue(false);
                Log.e("downloadMenu failure", t.getMessage());
            }
        });
    }

    public LiveData<List<DrinkItem>> getDrinkMenuItems() {
        return drinkItems;
    }

    public void downloadBlogs() {
        int shop_id = selectedShopId.getValue();
        is_Up_Downloading.setValue(true);
        Call<List<BlogItem>> call = RetrofitClient.getInstance().getBlogs(shop_id);
        call.enqueue(new Callback<List<BlogItem>>() {
            @Override
            public void onResponse(Call<List<BlogItem>> call, Response<List<BlogItem>> response) {
                is_Up_Downloading.postValue(false);
                blogItems.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<BlogItem>> call, Throwable t) {
                is_Up_Downloading.postValue(false);
                Log.e("downloadBlog failure", t.getMessage());
            }
        });
    }

    public LiveData<List<BlogItem>> getBlogs() {
        return blogItems;
    }

    public void uploadOrder(String session_id, List<DrinkItem> bucket) {
        is_Up_Downloading.setValue(true);
        List<DrinkItemForUpload> bucketForUpload = new ArrayList<>();

        //convert DrinkItem to DrinkItemForUpload
        for (DrinkItem item : bucket) {
            boolean found = false;
            for (DrinkItemForUpload o : bucketForUpload) {
                if (o.getItem_id() == item.getId()) {
                    found = true;
                    o.setItem_count((o.getItem_count() + 1));
                    break;
                }
            }
            if (!found) {
                bucketForUpload.add(new DrinkItemForUpload(item.getId(), 1, item.getShop_id()));
            }
        }
        Gson gson = new GsonBuilder().setLenient().create();
        String json_string = gson.toJson(bucketForUpload);
        Call<ResponseModel> call = RetrofitClient.getInstance().uploadOrder("PHPSESSID=" + session_id, json_string);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                is_Up_Downloading.postValue(false);
                ResponseModel r = response.body();
                if (r.getSuccess() == 1) {
                    isOrderUploadSuccess.postValue(true);
                } else {
                    isOrderUploadSuccess.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                is_Up_Downloading.postValue(false);
                isOrderUploadSuccess.postValue(false);
                Log.e("uploadOrder", t.getMessage());

            }
        });
    }

    public LiveData<Boolean> isUploadSuccess() {
        return isOrderUploadSuccess;
    }
}
