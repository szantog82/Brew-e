package com.szantog.brew_e;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DatabaseViewModel extends AndroidViewModel {

    private OrderLocalRepository orderLocalRepository;

    public DatabaseViewModel(Application application) {
        super(application);
        orderLocalRepository = new OrderLocalRepository(application);
    }

    public LiveData<List<OrderedItem>> getAllOrderedItems() {
        return orderLocalRepository.getAllOrderedItems();
    }

    public void insertOrderedItem(OrderedItem item) {
        orderLocalRepository.insertOrderedItem(item);
    }
}