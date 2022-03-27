package com.example.coffeeapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insertRecipe(RecipeDB... recipes);

    @Delete
    void deleteRecipe(RecipeDB recipeDB);

    @Update
    void updateRecipe(RecipeDB... recipes);

    @Query("SELECT * FROM recipes")
    List<RecipeDB> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE recipeId = :id")
    RecipeDB getRecipeById(int id);

    @Query("SELECT MAX(recipeId) FROM recipes")
    int getBiggestRecipeId();

    /*@Transaction
    @Query("SELECT * FROM beans")
    ArrayList<BeanWithRecipes> getBeansWithRecipes();
     */
}
