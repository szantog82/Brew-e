package com.szantog.brew_e;

public class DrinkItemForUpload {

    private int item_id;
    private int item_count;
    private int shop_id;

    public DrinkItemForUpload(int item_id, int item_count, int shop_id) {
        this.item_id = item_id;
        this.item_count = item_count;
        this.shop_id = shop_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }
}
