package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.beansList;
import static com.example.coffeeapp.BeansList.loadBeans;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Logic for RecipesList activity
 * Displays a list of all recipes added and loads recipes from the database
 */
public class RecipesList extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 90;
    static ArrayList<Recipe> recipesList = new ArrayList<>();
    static List<RecipeDB> recipesFromDB;

    RecipesRecViewAdapter adapter;
    private BottomNavigationView bottomBar;
    private RecyclerView recipesRecView;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private MaterialCardView noRecipesCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        initViews();

        CoffeeDatabase db = CoffeeDatabase.getDatabase(this.getApplicationContext());
        // initialise recipes RecView + load recipes from DB
        loadBeans(db);
        loadRecipes(db);
        initRecView();
        adapter.notifyDataSetChanged();
        if (recipesList.size() < 1) {
            noRecipesCard.setVisibility(View.VISIBLE);
        } else {
            noRecipesCard.setVisibility(View.GONE);
        }
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
        noRecipesCard = findViewById(R.id.no_recipes_info);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Recipes");

        // initialise the bottom bar
        initBottomBar();
    }

    /**
     * Defines behaviour for the bottom bar
     */
    private void initBottomBar() {
        // set a listener for the bottom bar and signify that we're in the recipes screen
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
                        // check if the user gave permission to use location
                        if (ContextCompat.checkSelfPermission(RecipesList.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Intent cafeFinderIntent = new Intent(RecipesList.this, CafeFinder.class);
                            startActivity(cafeFinderIntent);
                        } else {
                            ActivityCompat.requestPermissions(RecipesList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
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
     * Initialises the recipes recycler view
     */
    private void initRecView() {
        // create a recipe RecView adapter and pass it to the RecView
        adapter = new RecipesRecViewAdapter(this);
        adapter.setRecipes(recipesList);
        recipesRecView.setAdapter(adapter);
        // set layout manager for the RecView - display the items linearly
        recipesRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Creates the Recycler View for recipes and loads recipes from the Database
     */
    public static void loadRecipes(CoffeeDatabase db) {
        // get persisted recipes
        recipesFromDB = db.recipeDao().getAllRecipes();
        if (recipesList.size() < 1) {
            for (RecipeDB rDB : recipesFromDB) {
                recipesList.add(createRecipeFromDatabase(rDB));
            }
        } else {
            for (RecipeDB rDB : recipesFromDB) {
                boolean found = false;
                for (Recipe r : recipesList) {
                    if (rDB.recipeId == r.getId()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    recipesList.add(createRecipeFromDatabase(rDB));
                }
            }
        }
    }

    /**
     * Creates a local Recipe objecy from the cupplied RecipeDB object frm the DB
     *
     * @param rDB recipe object from the db
     * @return local Recipe object
     */
    private static Recipe createRecipeFromDatabase(RecipeDB rDB) {
        Recipe recipe = new Recipe();
        recipe.setId(rDB.recipeId);
        recipe.setName(rDB.name);
        recipe.setDateAdded(rDB.dateAdded);
        for (Bean b : beansList) {
            if (b.getId() == rDB.beansUsedId) {
                recipe.setBeansUsed(b);
                break;
            }
        }
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
        recipe.setPhoto(rDB.image);
        return recipe;
    }

    /**
     * check if location permissions have been granted, and launch the map if so
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cafeFinderIntent = new Intent(this, CafeFinder.class);
                startActivity(cafeFinderIntent);
            } else {
                Toast.makeText(this, "Access to location denied. Allow it from settings.", Toast.LENGTH_SHORT).show();
            }
        }
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

    /**
     * Gets the user to the add recipe activity to add a new recipe
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_option) {
            Intent newRecipeIntent = new Intent(this, AddRecipe.class);
            startActivity(newRecipeIntent);
            return true;
        }
        return false;
    }

    /**
     * Resets the bottom bar tab selected
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.recipes_menu);
    }

    /**
     * Refreshes the recipes list and reorders it from latest
     */
    @Override
    protected void onResume() {
        adapter.setRecipes(recipesList);
        adapter.notifyDataSetChanged();
        if (recipesList.size() < 1) {
            noRecipesCard.setVisibility(View.VISIBLE);
        } else {
            noRecipesCard.setVisibility(View.GONE);
        }
        super.onResume();
    }
}