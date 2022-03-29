package com.example.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoffeeLog extends AppCompatActivity {
    private BottomNavigationView bottomBar;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_log);

        initViews();
    }

    /**
     * Initialises the whole layout
     */
    private void initViews() {
        //initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_beans);
        toolbar = findViewById(R.id.cc_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Your Coffee Cups");

        initBottomBar();
    }

    /**
     * Initialises the bottom bar
     */
    private void initBottomBar() {
        // create the bottom bar, set a listener and signify that we're in the coffee log screen
        bottomBar = findViewById(R.id.bottom_bar_log);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beans_menu:
                        Intent beansIntent = new Intent(CoffeeLog.this, BeansList.class);
                        startActivity(beansIntent);
                        return true;
                    case R.id.recipes_menu:
                        Intent recipesIntent = new Intent(CoffeeLog.this, RecipesList.class);
                        startActivity(recipesIntent);
                        return true;
                    case R.id.home_menu:
                        Intent homeIntent = new Intent(CoffeeLog.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.coffee_log_menu:
                        return true;
                    case R.id.map_menu:
                        return true;
                    default:
                        Toast.makeText(CoffeeLog.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.coffee_log_menu);
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
     * Gets the user to the add drink activity to add a new drink
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_option) {
            Intent newDrinkIntent = new Intent(this, AddDrink.class);
            startActivity(newDrinkIntent);
            return true;
        }
        return false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.coffee_log_menu);
    }
}