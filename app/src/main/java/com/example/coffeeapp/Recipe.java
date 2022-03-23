package com.example.coffeeapp;

import android.graphics.Bitmap;
import android.net.Uri;

import java.time.LocalTime;
import java.util.Date;

/**
 * Class that deals with recipes added by the user
 */
public class Recipe {
    private Date dateAdded;
    private String name;
    private Bean beansUsed;
    private float amountOfCoffee;
    private String methodOfBrewing;
    private LocalTime brewingTime;
    private boolean boughtGround;
    private int grindScale;
    private String grindNotes;
    private String milk;
    private String syrup;
    private String sugar;
    // e.g. milk + kind, temp, amount, steamed or not
    // syrups/sauce + flavour, brand, amount
    // sugar - white/brown + amount
    private int rating; // scale from 0-5
    private Uri photoUri;
    private Bitmap photo;
    private String notes;

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

    public float getAmountOfCoffee() {
        return amountOfCoffee;
    }

    public void setAmountOfCoffee(float amountOfCoffee) {
        this.amountOfCoffee = amountOfCoffee;
    }

    public String getMethodOfBrewing() {
        return methodOfBrewing;
    }

    public void setMethodOfBrewing(String methodOfBrewing) {
        this.methodOfBrewing = methodOfBrewing;
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

    public LocalTime getBrewingTime() {
        return brewingTime;
    }

    public void setBrewingTime(LocalTime brewingTime) {
        this.brewingTime = brewingTime;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public String getGrindNotes() {
        return grindNotes;
    }

    public void setGrindNotes(String grindNotes) {
        this.grindNotes = grindNotes;
    }

    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public String getSyrup() {
        return syrup;
    }

    public void setSyrup(String syrup) {
        this.syrup = syrup;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "dateAdded=" + dateAdded +
                ", name='" + name + '\'' +
                ", beansUsed=" + beansUsed +
                ", amountOfCoffee=" + amountOfCoffee +
                ", methodOfBrewing='" + methodOfBrewing + '\'' +
                ", boughtGround=" + boughtGround +
                ", grindScale=" + grindScale +
                ", grindNotes='" + grindNotes + '\'' +
                ", brewingTime=" + brewingTime +
                ", milk='" + milk + '\'' +
                ", syrup='" + syrup + '\'' +
                ", sugar='" + sugar + '\'' +
                ", rating=" + rating +
                ", photo='" + photoUri + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
