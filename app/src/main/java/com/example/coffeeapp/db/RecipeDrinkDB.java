package com.example.coffeeapp.db;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "recipe_drinks")
public class RecipeDrinkDB {
    @PrimaryKey
    public int drinkId;
    public Date dateAdded;
    public String drinkName;
    public int recipeUsedId;
    public String recipeName;
    public String beansUsed;
    public float amountOfCoffeeUsed;
    public String prepMethodUsed;
    public String extrasUsed;
    public String drinkNotes;
    public Bitmap drinkPhoto;
}
