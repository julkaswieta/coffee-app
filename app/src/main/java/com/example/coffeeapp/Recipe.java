package com.example.coffeeapp;

import android.graphics.Bitmap;
import android.net.Uri;

import java.time.LocalTime;
import java.util.Date;

/**
 * Class that deals with recipes added by the user
 */
public class Recipe {
    public static int idCounter = 0;

    private int id;
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
    private float rating; // scale from 0-5
    private Uri photoUri;
    private Bitmap photo;
    private String notes;


    /**
     * returns the next available id
     * @return id
     */
    public static int nextId() {
        return ++idCounter;
    }

    // default empty constructor
    public Recipe(){
    }

    // constructor with date added and recipe name
    public Recipe(Date dateAdded, String name) {
        this.dateAdded = dateAdded;
        this.name = name;
    }

    /**
     * returns the recipe id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * sets the recipe id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the date added
     * @return
     */
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * sets the date added
     * @param dateAdded
     */
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * returns recipe name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * sets recipe name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns beans used to preap recipe
     * @return
     */
    public Bean getBeansUsed() {
        return beansUsed;
    }

    /**
     * sets the beans used to prep recipe
     * @param beansUsed
     */
    public void setBeansUsed(Bean beansUsed) {
        this.beansUsed = beansUsed;
    }

    /**
     * returns amount of coffeee in grams used to prep recipe
     * @return
     */
    public float getAmountOfCoffee() {
        return amountOfCoffee;
    }

    /**
     * sets amount of coffeee in grams used to prep recipe
     * @param amountOfCoffee
     */
    public void setAmountOfCoffee(float amountOfCoffee) {
        this.amountOfCoffee = amountOfCoffee;
    }

    /**
     * returns recipe preparation method
     * @return
     */
    public String getMethodOfBrewing() {
        return methodOfBrewing;
    }

    /**
     * sets recipe prep method
     * @param methodOfBrewing
     */
    public void setMethodOfBrewing(String methodOfBrewing) {
        this.methodOfBrewing = methodOfBrewing;
    }

    /**
     * determined whether the beans used were bought ground or not
     * @return
     */
    public boolean isBoughtGround() {
        return boughtGround;
    }

    /**
     * sets whether the beans used were bought ground or not
     * @param boughtGround
     */
    public void setBoughtGround(boolean boughtGround) {
        this.boughtGround = boughtGround;
    }

    /**
     * returns how well the beans are ground
     * @return
     */
    public int getGrindScale() {
        return grindScale;
    }

    /**
     * sets how well the beans are grouns
     * @param grindScale
     */
    public void setGrindScale(int grindScale) {
        this.grindScale = grindScale;
    }

    /**
     * returns the length of brewing
     * @return
     */
    public LocalTime getBrewingTime() {
        return brewingTime;
    }

    /**
     * sets the length of brewing
     * @param brewingTime
     */
    public void setBrewingTime(LocalTime brewingTime) {
        this.brewingTime = brewingTime;
    }

    /**
     * returns recipe rating
     * @return
     */
    public float getRating() {
        return rating;
    }

    /**
     * sets recipe rating
     * @param rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * returns photo uri
     * @return
     */
    public Uri getPhotoUri() {
        return photoUri;
    }

    /**
     * sets photo uri
     * @param photoUri
     */
    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    /**
     * returns grind notes
     * @return
     */
    public String getGrindNotes() {
        return grindNotes;
    }

    /**
     * sets grind notes
     * @param grindNotes
     */
    public void setGrindNotes(String grindNotes) {
        this.grindNotes = grindNotes;
    }

    /**
     * returns the kind and amount of milk used
     * @return
     */
    public String getMilk() {
        return milk;
    }

    /**
     * sets the kind and amount of milk used
     * @param milk
     */
    public void setMilk(String milk) {
        this.milk = milk;
    }

    /**
     * returns the kind and amount of syrup used
     * @return
     */
    public String getSyrup() {
        return syrup;
    }

    /**
     * sets the kind and amount of syrup used
     * @param syrup
     */
    public void setSyrup(String syrup) {
        this.syrup = syrup;
    }

    /**
     * returns the kind and amount of sugar used
     * @return
     */
    public String getSugar() {
        return sugar;
    }

    /**
     * sets the kind and amount of sugar used
     * @param sugar
     */
    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    /**
     * returns recipe notes
     * @return
     */
    public String getNotes() {
        return notes;
    }

    /**
     * sets recipe notes
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * returns recipe Bitmap photo
     * @return
     */
    public Bitmap getPhoto() {
        return photo;
    }

    /**
     * sets recipe Bitmap photo
     * @param photo
     */
    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    /**
     * recipe string representation - name + beans used
     * @return
     */
    @Override
    public String toString() {
        return name + " with " + beansUsed.getName();
    }
}
