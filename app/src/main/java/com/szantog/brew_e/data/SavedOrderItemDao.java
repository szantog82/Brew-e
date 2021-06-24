package com.szantog.brew_e.data;

import com.szantog.brew_e.data.entities.OrderedItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SavedOrderItemDao {

    @Query("select * from `order` order by date desc")
    LiveData<List<OrderedItem>> getAllOrders();

    @Insert
    void insertAll(OrderedItem... localOrders);
}
