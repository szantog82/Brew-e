package com.szantog.brew_e.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "order")
public class OrderedItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;

    public String name;

    public int price;

    public String shop_name;


}
