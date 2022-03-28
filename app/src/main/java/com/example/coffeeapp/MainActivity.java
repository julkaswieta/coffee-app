package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.loadBeans;
import static com.example.coffeeapp.RecipesList.loadRecipes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomBar;
    private TextView recipeCount;
    private TextView beansCount;

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
}
