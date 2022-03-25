package com.example.coffeeapp;

public class Bean {
    public static int idCounter = 0;

    private int id;
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

    public Bean() {}

    public Bean(String name, String roaster) {
        this.name = name;
        this.roaster = roaster;
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

    @Override
    public String toString() {
        return name + ", " + roaster;
    }
}
