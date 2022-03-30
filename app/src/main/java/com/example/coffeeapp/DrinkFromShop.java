package com.example.coffeeapp;

public class DrinkFromShop extends Drink {
    public static int idCounterS = 0;

    private float price;
    private String currency;
    private String size;
    private float rating;
    private String drinkNotes;
    private String shopName;
    private String shopAddress;

    /**
     * returns the next available id
     * @return
     */
    public static int nextId() {
        return ++idCounterS;
    }

    /**
     * returns the drink price
     * @return  price
     */
    public float getPrice() {
        return price;
    }

    /**
     * sets the drink price
     * @param price
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * returns the currecny of the price
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * sets the currency of the price
     * @param currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * return the sizeof the drink
     * @return
     */
    public String getSize() {
        return size;
    }

    /**
     * sets the size of the drink
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * returns rhe rating of the drink
     * @return
     */
    public float getRating() {
        return rating;
    }

    /**
     * sets the drink rating
     * @param rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * returns notes on thedrink
     * @return
     */
    public String getDrinkNotes() {
        return drinkNotes;
    }

    /**
     * sets notes on the dirnk
     * @param drinkNotes
     */
    public void setDrinkNotes(String drinkNotes) {
        this.drinkNotes = drinkNotes;
    }

    /**
     * returns the coffee shop name
     * @return
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * sets the coffee shop name
     * @param shopName
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * returns the coffee shop address
     * @return
     */
    public String getShopAddress() {
        return shopAddress;
    }

    /**
     * sets the coffee shop address
     * @param shopAddress
     */
    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
}
