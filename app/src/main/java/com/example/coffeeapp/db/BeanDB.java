package com.example.coffeeapp.db;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "beans")
public class BeanDB {
    @PrimaryKey
    public int beansId;
    public Date dateAdded;
    public String name;
    public String roaster;
    public boolean isFlavoured;
    public String flavour;
    public String isBlend;
    public int degreeOfRoast;
    public boolean isDecaf;
    public String urlToShop;
    public float costPerKg;
    public String currency;
    public float rating;
    public String notes;
    public Bitmap image;
}
