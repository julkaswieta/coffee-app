package com.example.coffeeapp;

import android.graphics.Bitmap;

import java.util.Date;

public abstract class Drink {
    private int id;
    private String drinkName;
    private Date dateAdded;
    private Bitmap drinkPhoto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Bitmap getDrinkPhoto() {
        return drinkPhoto;
    }

    public void setDrinkPhoto(Bitmap drinkPhoto) {
        this.drinkPhoto = drinkPhoto;
    }
}
