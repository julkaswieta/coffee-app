package com.example.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;

public class RecipesList extends AppCompatActivity {
    static ArrayList<Recipe> recipesList = new ArrayList<>();

    private BottomNavigationView bottomBar;
    private RecyclerView recipesRecView;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        // initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_recipes);
        recipesRecView = findViewById(R.id.recipes_rec_view);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Recipes");


        // set some test Recipe objects to test teh RecView
        recipesList.add(new Recipe(new Date(), "Cold Brew"));
        recipesList.add(new Recipe(new Date(), "Soy latte"));
        recipesList.add(new Recipe(new Date(), "Cappuccino"));
        recipesList.add(new Recipe(new Date(), "Americano with soy milk"));


        // create a recipe RecView adapter and pass it to the RecView
        RecipesRecViewAdapter adapter = new RecipesRecViewAdapter(this);
        adapter.setRecipes(recipesList);
        recipesRecView.setAdapter(adapter);
        // set layout manager for the RecView - display the items linearly
        recipesRecView.setLayoutManager(new LinearLayoutManager(this));


        // set a listener for the bottom bar and signify that we're in the recipes screen
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beans_menu:
                        Intent beansIntent = new Intent(RecipesList.this, BeansList.class);
                        startActivity(beansIntent);
                        return true;
                    case R.id.recipes_menu:
                        return true;
                    case R.id.home_menu:
                        Intent homeIntent = new Intent(RecipesList.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.coffee_log_menu:
                        Intent logIntent = new Intent(RecipesList.this, CoffeeLog.class);
                        startActivity(logIntent);
                        return true;
                    case R.id.map_menu:
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

    // Adds the menu options to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    // Gets the user to the add recipe activity to add a new recipe
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_option) {
            Intent newRecipeIntent = new Intent(this, AddRecipe.class);
            startActivity(newRecipeIntent);
            return true;
        }
        return false;
    }
}