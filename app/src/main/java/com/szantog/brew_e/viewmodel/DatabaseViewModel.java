package com.szantog.brew_e.viewmodel;

import android.app.Application;

import com.szantog.brew_e.OrderLocalRepository;
import com.szantog.brew_e.model.OrderedItem;

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
