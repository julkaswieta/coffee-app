package com.example.coffeeapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "beans")
public class Bean {
    @PrimaryKey(autoGenerate = true)
    private int beansId;
    private String name;
    private String roaster;
    private boolean isFlavoured;
    private String flavour;
    private boolean isBlend;
    private int degreeOfRoast;
    private boolean isDecaf;
    private String urlToShop;
    private float costPerKg;
}
