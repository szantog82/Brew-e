package com.szantog.brew_e.viewmodel;

import com.szantog.brew_e.model.DrinkItem;
import com.szantog.brew_e.model.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final MutableLiveData<List<DrinkItem>> bucket = new MutableLiveData<>();

    private final MutableLiveData<Integer> clickedButton = new MutableLiveData<>();

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
        user.setValue(u);
    }

    public LiveData<User> getRegisterUserData() {
        return user;
    }

}
