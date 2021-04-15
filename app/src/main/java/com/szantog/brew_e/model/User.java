package com.szantog.brew_e.model;

public class User {

    private int id;
    private String login;
    private String password;
    private String email;
    private String family_name;
    private String first_name;
    private int postalcode;
    private String country;
    private String city;
    private String street;

    public User() {

    }

    public User(int id, String login, String email, String family_name, String first_name, int postalcode, String country, String city, String street) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.family_name = family_name;
        this.first_name = first_name;
        this.postalcode = postalcode;
        this.country = country;
        this.city = city;
        this.street = street;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
