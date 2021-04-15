package com.szantog.brew_e.model;

public class CoffeeShop {
    private int id;
    private String name;
    private String description;
    private String email;
    private String city;
    private int postalcode;
    private String street;
    private String tax_num;
    private String lat_coord;
    private String lon_coord;

    public CoffeeShop(int id, String name, String description, String email, String city, int postalcode, String street, String tax_num, String lat_coord, String lon_coord) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.email = email;
        this.city = city;
        this.postalcode = postalcode;
        this.street = street;
        this.tax_num = tax_num;
        this.lat_coord = lat_coord;
        this.lon_coord = lon_coord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTax_num() {
        return tax_num;
    }

    public void setTax_num(String tax_num) {
        this.tax_num = tax_num;
    }

    public String getLat_coord() {
        return lat_coord;
    }

    public void setLat_coord(String lat_coord) {
        this.lat_coord = lat_coord;
    }

    public String getLon_coord() {
        return lon_coord;
    }

    public void setLon_coord(String lon_coord) {
        this.lon_coord = lon_coord;
    }
}




