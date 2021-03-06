package com.szantog.brew_e.data;

import com.szantog.brew_e.data.entities.OrderedItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {OrderedItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract SavedOrderItemDao savedOrderItemDao();
}
