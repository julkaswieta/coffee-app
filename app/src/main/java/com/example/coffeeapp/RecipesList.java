package com.example.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.coffeeapp.db.RecipeDB;
import com.example.coffeeapp.db.RecipesDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class RecipesList extends AppCompatActivity {
    static ArrayList<Recipe> recipesList = new ArrayList<>();
    static List<RecipeDB> recipesFromDB;

    private BottomNavigationView bottomBar;
    private RecyclerView recipesRecView;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        initViews();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.recipes_menu);
    }

    /**
     * Adds the menu options to the toolbar
     * @param menu layout file
     */
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

    private Recipe createRecipeFromDatabase(RecipeDB rDB) {
        Recipe recipe = new Recipe();
        recipe.setId(rDB.recipeId);
        recipe.setName(rDB.name);
        recipe.setDateAdded(rDB.dateAdded);
        // TODO: fix beans - will need to query beansList for ID
        // recipe.setBeansUsed();
        recipe.setAmountOfCoffee(rDB.amountOfCoffee);
        recipe.setMethodOfBrewing(rDB.methodOfBrewing);
        recipe.setBrewingTime(rDB.brewingTime);
        recipe.setBoughtGround(rDB.boughtGround);
        recipe.setGrindScale(rDB.grindScale);
        recipe.setGrindNotes(rDB.grindNotes);
        recipe.setMilk(rDB.milk);
        recipe.setSyrup(rDB.syrup);
        recipe.setSugar(rDB.sugar);
        recipe.setRating(rDB.rating);
        recipe.setNotes(rDB.notes);
        return recipe;
    }

    /**
     * Defines behaviour for the bottom bar
     */
    private void initBottomBar() {
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

    /**
     * Initialises the whole layout
     */
    private void initViews() {
        // initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_recipes);
        recipesRecView = findViewById(R.id.recipes_rec_view);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Recipes");

        // initialise the bottom bar
        initBottomBar();

        // initialise recipes RecView + load recipes from DB
        loadRecipes();
    }

    /**
     * Creates the Recycler View for recipes and loads recipes from the Database
     */
    private void loadRecipes() {
        // get persisted recipes
        RecipesDatabase db = RecipesDatabase.getDatabase(this.getApplicationContext());
        recipesFromDB = db.recipeDao().getAllRecipes();
        if(recipesList.size() < 1) {
            for(RecipeDB rDB : recipesFromDB) {
                recipesList.add(createRecipeFromDatabase(rDB));
            }
        }
        else {
            for (RecipeDB rDB : recipesFromDB) {
                boolean found = false;
                for (Recipe r : recipesList) {
                    // TODO: check if the id is not already in the list, if not add to list for display
                    if(rDB.recipeId == r.getId()) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    recipesList.add(createRecipeFromDatabase(rDB));
                }
            }
        }

        // create a recipe RecView adapter and pass it to the RecView
        RecipesRecViewAdapter adapter = new RecipesRecViewAdapter(this);
        adapter.setRecipes(recipesList);
        recipesRecView.setAdapter(adapter);
        // set layout manager for the RecView - display the items linearly
        recipesRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}