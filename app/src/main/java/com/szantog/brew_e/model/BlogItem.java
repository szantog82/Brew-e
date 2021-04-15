package com.szantog.brew_e.model;

public class BlogItem {

    private int id;
    private String title;
    private String text;
    private long upload_date;
    private String shop_name;

    public BlogItem(int id, String title, String text, long upload_date, String shop_name) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.upload_date = upload_date;
        this.shop_name = shop_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(long upload_date) {
        this.upload_date = upload_date;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
