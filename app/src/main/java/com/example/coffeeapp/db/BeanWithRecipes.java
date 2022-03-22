package com.example.coffeeapp.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;

public class BeanWithRecipes {
    @Embedded private Bean bean;
    @Relation(
            parentColumn = "beansId",
            entityColumn = "beansUsedId"
    )
    public ArrayList<Recipe> recipes;
}
