package com.example.coffeeapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalTime;
import java.util.Date;

@Entity(tableName = "recipes")
public class RecipeDB {
    @PrimaryKey
    public int recipeId;
    public Date dateAdded;
    public String name;
    public int beansUsedId;
    public float amountOfCoffee;
    public String methodOfBrewing;
    public LocalTime brewingTime;
    public boolean boughtGround;
    public int grindScale;
    public String grindNotes;
    public String milk;
    public String syrup;
    public String sugar;
    public float rating;
    public String notes;
}
