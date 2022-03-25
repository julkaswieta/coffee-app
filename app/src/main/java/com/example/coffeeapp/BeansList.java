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

import com.example.coffeeapp.db.BeanDB;
import com.example.coffeeapp.db.RecipeDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BeansList extends AppCompatActivity {
    static ArrayList<Bean> beansList = new ArrayList<>();
    static List<BeanDB> beansFromDB;

    private BottomNavigationView bottomBar;
    private RecyclerView beansRecView;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beans_list);

        initViews();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.beans_menu);
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
     * @param item menu item selected
     * @return if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_option) {
            Intent newBeansIntent = new Intent(this, AddBeans.class);
            startActivity(newBeansIntent);
            return true;
        }
        return false;
    }


    /**
     * Initialises the whole layout
     */
    private void initViews() {
        //initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_beans);
        toolbar = findViewById(R.id.bl_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        beansRecView = findViewById(R.id.beans_rec_view);


        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Beans");

        initBottomBar();
        loadBeans();
    }

    /**
     * Defines behaviour for the bottom bar
     */
    private void initBottomBar() {
        // set a listener for the bottom bar and signify that we're in the bean screen
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.beans_menu:
                        return true;
                    case R.id.recipes_menu:
                        Intent recipesIntent = new Intent(BeansList.this, RecipesList.class);
                        startActivity(recipesIntent);
                        return true;
                    case R.id.home_menu:
                        Intent homeIntent = new Intent(BeansList.this, MainActivity.class);
                        startActivity(homeIntent);
                        return true;
                    case R.id.coffee_log_menu:
                        Intent coffeeLogIntent = new Intent(BeansList.this, CoffeeLog.class);
                        startActivity(coffeeLogIntent);
                        return true;
                    case R.id.map_menu:
                        return true;
                    default:
                        Toast.makeText(BeansList.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.beans_menu);
    }


    private void loadBeans() {
        beansList.add(new Bean("Vanilla & Hazelnut", "Agifa"));
        beansList.add(new Bean("Brazylia Santos", "Agifa"));

        // create a recipe RecView adapter and pass it to the RecView
        BeansRecViewAdapter adapter = new BeansRecViewAdapter(this);
        adapter.setBeans(beansList);
        beansRecView.setAdapter(adapter);
        // set layout manager for the RecView - display the items linearly
        beansRecView.setLayoutManager(new LinearLayoutManager(this));
    }
}