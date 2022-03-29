package com.example.coffeeapp.db;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "shop_drinks")
public class ShopDrinkDB {
    @PrimaryKey
    public int drinkId;
    public Date dateAdded;
    public String drinkName;
    public float price;
    public String currency;
    public String size;
    public float rating;
    public String drinkNotes;
    public String shopName;
    public String shopAddress;
    public Bitmap drinkPhoto;
}
