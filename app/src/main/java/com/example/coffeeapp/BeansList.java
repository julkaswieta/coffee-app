package com.example.coffeeapp;

import static com.example.coffeeapp.RecipesList.loadRecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.coffeeapp.db.BeanDB;
import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Resposible for the BeansList activity and loading beans from the database
 */
public class BeansList extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 90;
    static ArrayList<Bean> beansList = new ArrayList<>();
    static List<BeanDB> beansFromDB;

    BeansRecViewAdapter adapter;
    private BottomNavigationView bottomBar;
    private RecyclerView beansRecView;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private MaterialCardView noBeansCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beans_list);

        initViews();
        CoffeeDatabase db = CoffeeDatabase.getDatabase(this.getApplicationContext());
        loadBeans(db);
        initRecView();
        adapter.notifyDataSetChanged();
        if(beansList.size() < 1) {
            noBeansCard.setVisibility(View.VISIBLE);
        }
        else {
            noBeansCard.setVisibility(View.GONE);
        }
        loadRecipes(db);
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
        noBeansCard = findViewById(R.id.no_beans_info);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Beans");

        initBottomBar();
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
                        // check if the user gave permission to use location
                        if(ContextCompat.checkSelfPermission(BeansList.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Intent cafeFinderIntent = new Intent(BeansList.this, CafeFinder.class);
                            startActivity(cafeFinderIntent);
                        }
                        else {
                            ActivityCompat.requestPermissions(BeansList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
                        return true;
                    default:
                        Toast.makeText(BeansList.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.beans_menu);
    }

    /**
     * Initialises the beans recycler view
     */
    private void initRecView() {
        // create a recipe RecView adapter and pass it to the RecView
        adapter = new BeansRecViewAdapter(this);
        adapter.setBeans(beansList);
        beansRecView.setAdapter(adapter);
        // set layout manager for the RecView - display the items linearly
        beansRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Creates the Recycler View for beans and loads beans from the Database
     */
    public static void loadBeans(CoffeeDatabase db) {
        // get persisted bean
        beansFromDB = db.beanDao().getAllBeans();
        if(beansList.size() < 1) {
            for(BeanDB bDB : beansFromDB) {
                beansList.add(createBeanFromDB(bDB));
            }
        }
        else {
            for (BeanDB bDB : beansFromDB) {
                boolean found = false;
                for (Bean b : beansList) {
                    // TODO: check if the id is not already in the list, if not add to list for display
                    if(bDB.beansId == b.getId()) {
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    beansList.add(createBeanFromDB(bDB));
                }
            }
        }
    }

    /**
     * Creates a new Bean object from the supplied BeanDB object
     * @param bDB   Bean from the DB
     * @return      local Bean object
     */
    private static Bean createBeanFromDB(BeanDB bDB) {
        Bean b = new Bean();
        b.setName(bDB.name);
        b.setId(bDB.beansId);
        b.setDateAdded(bDB.dateAdded);
        b.setRoaster(bDB.roaster);
        b.setDegreeOfRoast(bDB.degreeOfRoast);
        b.setDecaf(bDB.isDecaf);
        b.setFlavoured(bDB.isFlavoured);
        b.setFlavour(bDB.flavour);
        b.setBlend(bDB.isBlend);
        b.setUrlToShop(bDB.urlToShop);
        b.setCostPerKg(bDB.costPerKg);
        b.setCurrency(bDB.currency);
        b.setRating(bDB.rating);
        b.setNotes(bDB.notes);
        b.setPhoto(bDB.image);
        return b;
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
     * Gets the user to the add beans activity to add a new beans
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
     * Resets the bottom bar to the correct tab selected
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.beans_menu);
    }

    /**
     * refresh beans list every time activity is resumed
     */
    @Override
    protected void onResume() {
        adapter.setBeans(beansList);
        adapter.notifyDataSetChanged();
        if(beansList.size() < 1) {
            noBeansCard.setVisibility(View.VISIBLE);
        }
        else {
            noBeansCard.setVisibility(View.GONE);
        }
        super.onResume();
    }
}