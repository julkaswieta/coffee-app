package com.example.coffeeapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "beans")
public class BeanDB {
    @PrimaryKey
    public int beansId;
    public String name;
    public String roaster;
    public boolean isFlavoured;
    public String flavour;
    public boolean isBlend;
    public int degreeOfRoast;
    public boolean isDecaf;
    public String urlToShop;
    public float costPerKg;
    public float rating;
}
