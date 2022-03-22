package com.example.coffeeapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insertRecipe(Recipe... recipes);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE recipeId = :id")
    Recipe getRecipeById(int id);

    /*@Transaction
    @Query("SELECT * FROM beans")
    ArrayList<BeanWithRecipes> getBeansWithRecipes();
     */
}
