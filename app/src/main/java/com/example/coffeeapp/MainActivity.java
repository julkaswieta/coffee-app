package com.example.coffeeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create the bottom bar, set a listener and signify that we're in the home screen
        bottomBar = findViewById(R.id.bottom_bar_main);
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
