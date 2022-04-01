package com.example.coffeeapp;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Represents a generic drink object
 */
public abstract class Drink {
    private int id;
    private String drinkName;
    private Date dateAdded;
    private Bitmap drinkPhoto;

    /**
     * returns the drink's id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * sets the id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the drink name
     * @return  name
     */
    public String getDrinkName() {
        return drinkName;
    }

    /**
     * seta a new drink name
     * @param drinkName new name
     */
    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    /**
     * returns the date drink was added
     * @return  date
     */
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * sets a new date
     * @param dateAdded date
     */
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * returns a drink photo
     * @return  Bitmap photo
     */
    public Bitmap getDrinkPhoto() {
        return drinkPhoto;
    }

    /**
     * sets a new drink photo
     * @param drinkPhoto new photo
     */
    public void setDrinkPhoto(Bitmap drinkPhoto) {
        this.drinkPhoto = drinkPhoto;
    }
}
