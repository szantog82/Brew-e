package com.szantog.brew_e;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

public class OrderLocalRepository {
    private AppDatabase db;
    private LiveData<List<OrderedItem>> orderedList = new MutableLiveData<>();

    public OrderLocalRepository(Application application) {
        db = Room.databaseBuilder(application, AppDatabase.class, "brew-e-db").build();
    }

    public LiveData<List<OrderedItem>> getAllOrderedItems() {
        return db.savedOrderItemDao().getAllOrders();
    }

    public void insertOrderedItem(OrderedItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.savedOrderItemDao().insertAll(item));
    }
}
