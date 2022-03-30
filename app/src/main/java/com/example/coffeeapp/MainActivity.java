package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.loadBeans;
import static com.example.coffeeapp.CoffeeLog.drinksList;
import static com.example.coffeeapp.CoffeeLog.loadDrinks;
import static com.example.coffeeapp.RecipesList.loadRecipes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.coffeeapp.db.CoffeeDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
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
                    return true;
                default:
                    Toast.makeText(MainActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        bottomBar.setSelectedItemId(R.id.home_menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.home_menu);
    }

    @Override
    protected void onResume() {
        loadStats();
        super.onResume();
    }
}
