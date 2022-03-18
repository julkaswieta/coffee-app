package com.example.coffeeapp;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class that deals with recipes added by the user
 */
public class Recipe {
    private Date dateAdded;
    private String name;
    private Bean beansUsed;
    private String methodOfBrewing;
    private String brewingNotes;
    private boolean boughtGround;
    private int grindScale;
    private int brewingTime;
    private ArrayList<String> additions;
    // e.g. milk + kind, temp, amount, steamed or not
    // syrups/sauce + flavour, brand, amount
    // sugar - white/brown + amount
    private int rating;
    private String photo;

    // default empty constructor
    public Recipe(){}

    public Recipe(Date dateAdded, String name) {
        this.dateAdded = dateAdded;
        this.name = name;
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

    public Bean getBeansUsed() {
        return beansUsed;
    }

    public void setBeansUsed(Bean beansUsed) {
        this.beansUsed = beansUsed;
    }

    public String getMethodOfBrewing() {
        return methodOfBrewing;
    }

    public void setMethodOfBrewing(String methodOfBrewing) {
        this.methodOfBrewing = methodOfBrewing;
    }

    public String getBrewingNotes() {
        return brewingNotes;
    }

    public void setBrewingNotes(String brewingNotes) {
        this.brewingNotes = brewingNotes;
    }

    public boolean isBoughtGround() {
        return boughtGround;
    }

    public void setBoughtGround(boolean boughtGround) {
        this.boughtGround = boughtGround;
    }

    public int getGrindScale() {
        return grindScale;
    }

    public void setGrindScale(int grindScale) {
        this.grindScale = grindScale;
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public void setBrewingTime(int brewingTime) {
        this.brewingTime = brewingTime;
    }

    public ArrayList<String> getAdditions() {
        return additions;
    }

    public void setAdditions(ArrayList<String> additions) {
        this.additions = additions;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "dateAdded='" + dateAdded + '\'' +
                ", name='" + name + '\'' +
                ", beansUsed=" + beansUsed +
                ", methodOfBrewing='" + methodOfBrewing + '\'' +
                ", brewingNotes='" + brewingNotes + '\'' +
                ", boughtGround=" + boughtGround +
                ", grindScale=" + grindScale +
                ", brewingTime=" + brewingTime +
                ", additions=" + additions +
                ", rating=" + rating +
                ", photo='" + photo + '\'' +
                '}';
    }
}
