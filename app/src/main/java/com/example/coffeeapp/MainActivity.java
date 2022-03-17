package com.example.coffeeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomBar;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_main);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Home");

        // set a listener for the bottom bar and signify that we're in the home screen
        bottomBar.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.beans_menu:
                    Toast.makeText(MainActivity.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                    Intent beansIntent = new Intent(MainActivity.this, BeansList.class);
                    startActivity(beansIntent);
                    return true;
                case R.id.recipes_menu:
                    Toast.makeText(MainActivity.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                    Intent recipesIntent = new Intent(MainActivity.this, RecipesList.class);
                    startActivity(recipesIntent);
                    return true;
                case R.id.home_menu:
                    return true;
                case R.id.coffee_log_menu:
                    Toast.makeText(MainActivity.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                    Intent coffeLogIntent = new Intent(MainActivity.this, CoffeeLog.class);
                    startActivity(coffeLogIntent);
                    return true;
                case R.id.map_menu:
                    Toast.makeText(MainActivity.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
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
