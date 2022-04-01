package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.loadBeans;
import static com.example.coffeeapp.CoffeeLog.drinksList;
import static com.example.coffeeapp.CoffeeLog.loadDrinks;
import static com.example.coffeeapp.RecipesList.loadRecipes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.coffeeapp.db.CoffeeDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Calendar;
import java.util.Date;

/**
 * Home screen - main activity
 * loads all beans, recipes and drinks to provide statistics on them
 * Logic for mainActivity
 */
public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 90;
    private BottomNavigationView bottomBar;
    private TextView recipeCount, beansCount;
    private TextView todayCount, weekCount, monthCount, totalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        loadStats();
    }

    /**
     * Initialises the whole layout
     */
    private void initViews() {
        //initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_main);
        recipeCount = findViewById(R.id.home_recipe_count);
        beansCount = findViewById(R.id.home_beans_count);
        todayCount = findViewById(R.id.today_count);
        weekCount = findViewById(R.id.week_count);
        monthCount = findViewById(R.id.month_count);
        totalCount = findViewById(R.id.total_count);

        initBottomBar();
    }

    /**
     * Loads all components from the database and updates statistical data displayed
     */
    private void loadStats() {
        CoffeeDatabase db = CoffeeDatabase.getDatabase(this);
        int recCount = db.recipeDao().getRecipeCount();
        if(recCount > 0) {
            recipeCount.setText(String.valueOf(recCount));
            recipeCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }
        int bnCount = db.beanDao().getBeansCount();
        if(bnCount > 0) {
            beansCount.setText(String.valueOf(bnCount));
            beansCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }

        loadDrinks(db);
        int today_count = 0;
        int week_count = 0;
        int month_count = 0;
        int total_count = 0;
        Calendar today = Calendar.getInstance();
        Calendar drinkDate;
        for(Drink drink : drinksList) {
            drinkDate = Calendar.getInstance();
            drinkDate.setTime(drink.getDateAdded());
            // count all drinks
            total_count++;
            // count today's drinks
            if(today.get(Calendar.YEAR) == drinkDate.get(Calendar.YEAR)
                    && today.get(Calendar.MONTH) == drinkDate.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) == drinkDate.get(Calendar.DAY_OF_MONTH)) {
                today_count++;
            }
            if(today.get(Calendar.YEAR) == drinkDate.get(Calendar.YEAR)
                    && today.get(Calendar.WEEK_OF_YEAR) == drinkDate.get(Calendar.WEEK_OF_YEAR)) {
                week_count++;
            }
            if(today.get(Calendar.YEAR) == drinkDate.get(Calendar.YEAR)
                    && today.get(Calendar.MONTH) == drinkDate.get(Calendar.MONTH)) {
                month_count++;
            }
        }

        todayCount.setText(String.valueOf(today_count));
        weekCount.setText(String.valueOf(week_count));
        monthCount.setText(String.valueOf(month_count));
        totalCount.setText(String.valueOf(total_count));
    }

    /**
     * Initialises the bottom bar
     */
    private void initBottomBar() {
        // set a listener for the bottom bar and signify that we're in the home screen
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.beans_menu:
                    Intent beansIntent = new Intent(MainActivity.this, BeansList.class);
                    startActivity(beansIntent);
                    return true;
                case R.id.recipes_menu:
                    Intent recipesIntent = new Intent(MainActivity.this, RecipesList.class);
                    startActivity(recipesIntent);
                    return true;
                case R.id.home_menu:
                    return true;
                case R.id.coffee_log_menu:
                    Intent coffeeLogIntent = new Intent(MainActivity.this, CoffeeLog.class);
                    startActivity(coffeeLogIntent);
                    return true;
                case R.id.map_menu:
                    // check if the user gave permission to use location
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent cafeFinderIntent = new Intent(this, CafeFinder.class);
                        startActivity(cafeFinderIntent);
                    }
                    else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    }

                    return true;
                default:
                    Toast.makeText(MainActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        bottomBar.setSelectedItemId(R.id.home_menu);
    }

    /**
     * check if location permissions have been granted, and launch the map if so
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cafeFinderIntent = new Intent(this, CafeFinder.class);
                startActivity(cafeFinderIntent);
            } else {
                Toast.makeText(this, "Access to location denied. Allow it from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Resets the bottom bar tab selected
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.home_menu);
    }

    /**
     * Reloads the statistics
     */
    @Override
    protected void onResume() {
        loadStats();
        super.onResume();
    }
}
