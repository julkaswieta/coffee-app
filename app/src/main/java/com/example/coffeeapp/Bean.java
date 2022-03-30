package com.example.coffeeapp;

import android.graphics.Bitmap;

import java.util.Date;

public class Bean {
    public static int idCounter = 0;

    private int id;
    private Date dateAdded;
    private String name;
    private String roaster;
    private boolean isFlavoured;
    private String flavour;
    private String isBlend;
    private int degreeOfRoast;  // on a scale to 1-5
    private boolean isDecaf;
    private Bitmap photo;
    private String urlToShop;
    private float costPerKg;
    private String currency;
    private float rating;
    private String notes;

    // default emppty constructor
    public Bean() {}

    // constructor with beans name and roaster
    public Bean(String name, String roaster) {
        this.name = name;
        this.roaster = roaster;
    }

    /**
     * returns the next available id
     * @return  id
     */
    public static int nextId() {
        return ++idCounter;
    }

    /**
     * Returns bean's id
     * @return  id
     */
    public int getId() {
        return id;
    }

    /**
     * sets the bean's id
     * @param id    new id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the date the beans were added at
     * @return  Date object
     */
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * Sets the bean's added date
     * @param dateAdded new date to set
     */
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * Returns bean's name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets bean's name
     * @param name  new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the bean's roaster
     * @return  roaster name
     */
    public String getRoaster() {
        return roaster;
    }

    /**
     * sets the roaster to a new value
     * @param roaster   new roaster to set
     */
    public void setRoaster(String roaster) {
        this.roaster = roaster;
    }

    /**
     * returns whether the beans are flavoured or not
     * @return  true for flavoured, false for not
     */
    public boolean isFlavoured() {
        return isFlavoured;
    }

    /**
     * determines whether beans are flavoured
     * @param flavoured true or false
     */
    public void setFlavoured(boolean flavoured) {
        isFlavoured = flavoured;
    }

    /**
     * returns the bean's flavour
     * @return flavour
     */
    public String getFlavour() {
        return flavour;
    }

    /**
     * Sets he ban's flavour to new value
     * @param flavour new flavour
     */
    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    /**
     * returns the orgin of the beans - unknown, blend or single
     * @return origin
     */
    public String isBlend() {
        return isBlend;
    }

    /**
     * sets the origin to new value
     * @param blend origin
     */
    public void setBlend(String blend) {
        isBlend = blend;
    }

    /**
     * returns the bean's degree of roast
     * @return  degree of roast
     */
    public int getDegreeOfRoast() {
        return degreeOfRoast;
    }

    /**
     * sets the degree of roast to a new value
     * @param degreeOfRoast new degree
     */
    public void setDegreeOfRoast(int degreeOfRoast) {
        this.degreeOfRoast = degreeOfRoast;
    }

    /**
     * determined whether the bean is decaf
     * @return  true for decaf
     */
    public boolean isDecaf() {
        return isDecaf;
    }

    /**
     * sets whether the bean is decaf
     * @param decaf true or false
     */
    public void setDecaf(boolean decaf) {
        isDecaf = decaf;
    }

    /**
     * returns the bean's Bitmap photo
     * @return  Bitmap
     */
    public Bitmap getPhoto() {
        return photo;
    }

    /**
     * sets the bean's photo to a new value
     * @param photo new Bitmap
     */
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    /**
     * returns the url to bean shop
     * @return url
     */
    public String getUrlToShop() {
        return urlToShop;
    }

    /**
     * sets the url
     * @param urlToShop url
     */
    public void setUrlToShop(String urlToShop) {
        this.urlToShop = urlToShop;
    }

    /**
     * returns the bean's cost per kg
     * @return  price
     */
    public float getCostPerKg() {
        return costPerKg;
    }

    /**
     * sets the cost
     * @param costPerKg price per kg
     */
    public void setCostPerKg(float costPerKg) {
        this.costPerKg = costPerKg;
    }

    /**
     * returns the bean's rating
     * @return rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * sets the rating
     * @param rating    new rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * returns the currency bean's were bought with
     * @return  currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * sets the currency
     * @param currency  currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * return notes on thebean
     * @return  notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * sets the notes
     * @param notes notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * string representation of the bean
     * @return  string with name and roaster
     */
    @Override
    public String toString() {
        return name + ", " + roaster;
    }
}
