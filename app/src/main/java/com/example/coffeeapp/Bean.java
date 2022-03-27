package com.example.coffeeapp;

import java.util.Date;

public class Bean {
    public static int idCounter = 0;

    private int id;
    private Date dateAdded;
    private String name;
    private String roaster;
    private boolean isFlavoured;
    private String flavour;
    private boolean isBlend;    // true for blend, false for single origin
    private int degreeOfRoast;  // on a scale to 1-5
    private boolean isDecaf;
    private String photo;
    private String urlToShop;
    private float costPerKg;
    private String currency;
    private float rating;
    private String notes;

    public Bean() {}

    public Bean(String name, String roaster) {
        this.name = name;
        this.roaster = roaster;
    }

    public static int nextId() {
        return ++idCounter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoaster() {
        return roaster;
    }

    public void setRoaster(String roaster) {
        this.roaster = roaster;
    }

    public boolean isFlavoured() {
        return isFlavoured;
    }

    public void setFlavoured(boolean flavoured) {
        isFlavoured = flavoured;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public boolean isBlend() {
        return isBlend;
    }

    public void setBlend(boolean blend) {
        isBlend = blend;
    }

    public int getDegreeOfRoast() {
        return degreeOfRoast;
    }

    public void setDegreeOfRoast(int degreeOfRoast) {
        this.degreeOfRoast = degreeOfRoast;
    }

    public boolean isDecaf() {
        return isDecaf;
    }

    public void setDecaf(boolean decaf) {
        isDecaf = decaf;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUrlToShop() {
        return urlToShop;
    }

    public void setUrlToShop(String urlToShop) {
        this.urlToShop = urlToShop;
    }

    public float getCostPerKg() {
        return costPerKg;
    }

    public void setCostPerKg(float costPerKg) {
        this.costPerKg = costPerKg;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return name + ", " + roaster;
    }
}
