package com.szantog.brew_e.domain;

public class DrinkItem {

    private int id;
    private String item_group;
    private String item_name;
    private int item_price;
    private String item_image;
    private int shop_id;
    private String shop_name;

    public DrinkItem(int id, String item_group, String item_name, int item_price, String item_image, int shop_id, String shop_name) {
        this.id = id;
        this.item_group = item_group;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_image = item_image;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_group() {
        return item_group;
    }

    public void setItem_group(String item_group) {
        this.item_group = item_group;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
