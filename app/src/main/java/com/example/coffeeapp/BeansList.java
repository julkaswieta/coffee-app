package com.example.coffeeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BeansList extends AppCompatActivity {
    static ArrayList<Bean> beansList = new ArrayList<>();

    private BottomNavigationView bottomBar;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beans_list);

        //initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_beans);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Beans");

        // set a listener for the bottom bar and signify that we're in the bean screen
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beans_menu:
                        Toast.makeText(BeansList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.recipes_menu:
                        Toast.makeText(BeansList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent recipesIntent = new Intent(BeansList.this, RecipesList.class);
                        startActivity(recipesIntent);
                        return true;
                    case R.id.home_menu:
                        Toast.makeText(BeansList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent homeIntent = new Intent(BeansList.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.coffee_log_menu:
                        Toast.makeText(BeansList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        Intent coffeeLogIntent = new Intent(BeansList.this, CoffeeLog.class);
                        startActivity(coffeeLogIntent);
                        return true;
                    case R.id.map_menu:
                        Toast.makeText(BeansList.this, item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(BeansList.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.beans_menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.beans_menu);
    }
}