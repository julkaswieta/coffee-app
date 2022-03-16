package com.example.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoffeeLog extends AppCompatActivity {
    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_log);

        // create the bottom bar, set a listener and signify that we're in the home screen
        bottomBar = findViewById(R.id.bottom_bar_log);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beans_menu:
                        Toast.makeText(CoffeeLog.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent beansIntent = new Intent(CoffeeLog.this, BeansList.class);
                        startActivity(beansIntent);
                        return true;
                    case R.id.recipes_menu:
                        Toast.makeText(CoffeeLog.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent recipesIntent = new Intent(CoffeeLog.this, RecipesList.class);
                        startActivity(recipesIntent);
                        return true;
                    case R.id.home_menu:
                        Toast.makeText(CoffeeLog.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(CoffeeLog.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.coffee_log_menu:
                        Toast.makeText(CoffeeLog.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.map_menu:
                        Toast.makeText(CoffeeLog.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(CoffeeLog.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.coffee_log_menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.coffee_log_menu);
    }
}