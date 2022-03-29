package com.example.coffeeapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDrinkDao {
    @Insert
    void insertRecipeDrink(RecipeDrinkDB... recipe_drinks);

    @Delete
    void deleteRecipeDrink(RecipeDrinkDB recipe_drinks);

    @Update
    void updateRecipeDrink(RecipeDrinkDB... recipe_drinks);

    @Query("SELECT COUNT(*) FROM recipe_drinks")
    int getRecipeDrinksCount();

    @Query("SELECT * FROM recipe_drinks")
    List<RecipeDrinkDB> getAllRecipeDrinks();

    @Query("SELECT * FROM recipe_drinks WHERE drinkId = :id")
    RecipeDrinkDB getRecipeDrinkById(int id);

    @Query("SELECT MAX(drinkId) FROM recipe_drinks")
    int getBiggestRecipeDrinkId();
}
