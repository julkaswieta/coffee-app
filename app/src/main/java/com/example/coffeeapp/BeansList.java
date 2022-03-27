package com.example.coffeeapp;

import static com.example.coffeeapp.RecipesList.loadRecipes;

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
import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BeansList extends AppCompatActivity {
    static ArrayList<Bean> beansList = new ArrayList<>();
    static List<BeanDB> beansFromDB;

    BeansRecViewAdapter adapter;
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
        CoffeeDatabase db = CoffeeDatabase.getDatabase(this.getApplicationContext());
        loadBeans(db);
        initRecView();
        loadRecipes(db);
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

    private void initRecView() {
        // create a recipe RecView adapter and pass it to the RecView
        adapter = new BeansRecViewAdapter(this);
        adapter.setBeans(beansList);
        beansRecView.setAdapter(adapter);
        // set layout manager for the RecView - display the items linearly
        beansRecView.setLayoutManager(new LinearLayoutManager(this));
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
        return b;
    }

    /**
     * refresh beans list every time activity is resumed
     */
    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}