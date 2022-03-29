package com.example.coffeeapp;

public class DrinkFromShop extends Drink {
    private float price;
    private String currency;
    private String size;
    private float rating;
    private String drinkNotes;
    private String shopName;
    private String shopAddress;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDrinkNotes() {
        return drinkNotes;
    }

    public void setDrinkNotes(String drinkNotes) {
        this.drinkNotes = drinkNotes;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
}
