package com.example.coffeeapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insertRecipe(RecipeDB... recipeDBS);

    @Delete
    void deleteRecipe(RecipeDB recipeDB);

    @Query("SELECT * FROM recipes")
    List<RecipeDB> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE recipeId = :id")
    RecipeDB getRecipeById(int id);

    /*@Transaction
    @Query("SELECT * FROM beans")
    ArrayList<BeanWithRecipes> getBeansWithRecipes();
     */
}
