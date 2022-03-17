package com.example.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;

public class RecipesList extends AppCompatActivity {
    private BottomNavigationView bottomBar;
    private RecyclerView recipesRecView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        // initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_recipes);
        recipesRecView = findViewById(R.id.recipes_rec_view);

        // initialise the ArrayList of recipes
        ArrayList<Recipe> recipes = new ArrayList<>();

        // set some test Recipe objects to test teh RecView
        recipes.add(new Recipe(new Date(), "Cold Brew"));
        recipes.add(new Recipe(new Date(), "Oat Latte"));
        recipes.add(new Recipe(new Date(), "Cappuccino"));
        recipes.add(new Recipe(new Date(), "Americano with soy milk"));

        // create a recipe RecView adapter and pass it to the RecView
        RecipesRecViewAdapter adapter = new RecipesRecViewAdapter();
        adapter.setRecipes(recipes);
        recipesRecView.setAdapter(adapter);
        // set layout manager for the RecView - display the items linearly
        recipesRecView.setLayoutManager(new LinearLayoutManager(this));


        // set a listener for the bottom bar and signify that we're in the recipes screen
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beans_menu:
                        Toast.makeText(RecipesList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent beansIntent = new Intent(RecipesList.this, BeansList.class);
                        startActivity(beansIntent);
                        return true;
                    case R.id.recipes_menu:
                        Toast.makeText(RecipesList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.home_menu:
                        Toast.makeText(RecipesList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(RecipesList.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.coffee_log_menu:
                        Toast.makeText(RecipesList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent logIntent = new Intent(RecipesList.this, CoffeeLog.class);
                        startActivity(logIntent);
                        return true;
                    case R.id.map_menu:
                        Toast.makeText(RecipesList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(RecipesList.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.recipes_menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.recipes_menu);
    }
}