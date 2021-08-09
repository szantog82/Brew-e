package com.szantog.brew_e.ui;

import android.app.Application;

import com.szantog.brew_e.clients.brewe.RetrofitClient;
import com.szantog.brew_e.clients.brewe.dtos.AuthResponse;
import com.szantog.brew_e.clients.brewe.dtos.UserRequest;
import com.szantog.brew_e.data.OrderLocalRepository;
import com.szantog.brew_e.data.entities.OrderedItem;
import com.szantog.brew_e.domain.DrinkItem;
import com.szantog.brew_e.domain.User;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {

    private OrderLocalRepository orderLocalRepository;
    private final MutableLiveData<User> userData = new MutableLiveData<>();
    private final MutableLiveData<String> session_id = new MutableLiveData<>();
    private final MutableLiveData<String> login_token = new MutableLiveData<>();
    private final MutableLiveData<List<DrinkItem>> bucket = new MutableLiveData<>();

    private final MutableLiveData<Integer> clickedButton = new MutableLiveData<>();

    public MainViewModel(Application application) {
        super(application);
        orderLocalRepository = new OrderLocalRepository(application);
    }

    public void setClickedButtonId(int buttonId) {
        this.clickedButton.setValue(buttonId);
    }

    public LiveData<Integer> getClickedButtonId() {
        return clickedButton;
    }

    public void setBucketForOrder(List<DrinkItem> bucket) {
        this.bucket.setValue(bucket);
    }

    public LiveData<List<DrinkItem>> getBucketForOrder() {
        return bucket;
    }

    public void setRegisterUserData(User u) {
        userData.setValue(u);
    }

    public LiveData<String> getSessionId() {
        return session_id;
    }

    public LiveData<String> getLoginToken() {
        return login_token;
    }

    public LiveData<List<OrderedItem>> getAllOrderedItems() {
        return orderLocalRepository.getAllOrderedItems();
    }

    public void insertOrderedItem(OrderedItem item) {
        orderLocalRepository.insertOrderedItem(item);
    }

    public void loginUser(String login, String password) {
        Call<AuthResponse> call = RetrofitClient.getInstance().loginUser(login, password);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                AuthResponse authResponse = response.body();
                if (authResponse.getSession_id().length() > 0) {
                    testConnection(authResponse.getSession_id());
                    MainViewModel.this.session_id.setValue(authResponse.getSession_id());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
            }
        });
    }

    public void testConnection(String session_id) {
        Call<UserRequest> call = RetrofitClient.getInstance().testUserConnection("PHPSESSID=" + session_id);
        call.enqueue(new Callback<UserRequest>() {
            @Override
            public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                UserRequest userRequest = response.body();
                if (userRequest != null) {
                    login_token.setValue(userRequest.getLogin_token());
                    userData.setValue(userRequest.convertToUser());
                }
            }

            @Override
            public void onFailure(Call<UserRequest> call, Throwable t) {
            }
        });
    }

    public void logoutUser(String session_id) {
        Call<Void> call = RetrofitClient.getInstance().logoutUser("PHPSESSID=" + session_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                userData.postValue(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void uploadFirebaseToken(String session_id, String firebaseToken) {
        Call<Void> call = RetrofitClient.getInstance().uploadFirbaseToken("PHPSESSID=" + session_id, firebaseToken);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public LiveData<User> getUser() {
        return userData;
    }

}
