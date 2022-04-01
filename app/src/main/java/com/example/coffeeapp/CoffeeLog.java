package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.loadBeans;
import static com.example.coffeeapp.RecipesList.loadRecipes;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDrinkDB;
import com.example.coffeeapp.db.ShopDrinkDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * CoffeeLog activity that displays a list of all drinks in a chronological order and loads drinks from the database
 * Logic for coffeelog activity
 */
public class CoffeeLog extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 91;
    static ArrayList<Drink> drinksList = new ArrayList<>();
    static List<RecipeDrinkDB> recipeDrinksDB = new ArrayList<>();
    static List<ShopDrinkDB> shopDrinksDB = new ArrayList<>();

    private BottomNavigationView bottomBar;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private MaterialCardView noDrinksCard;
    private DrinksRecViewAdapter adapter;
    private RecyclerView drinksRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_log);
        initViews();

        CoffeeDatabase db = CoffeeDatabase.getDatabase(this);
        loadBeans(db);
        loadRecipes(db);
        loadDrinks(db);
        initRecView();
        adapter.notifyDataSetChanged();
        if (drinksList.size() < 1) {
            noDrinksCard.setVisibility(View.VISIBLE);
        } else {
            noDrinksCard.setVisibility(View.GONE);
        }
    }

    /**
     * Initialises the whole layout
     */
    private void initViews() {
        //initialise attributes
        bottomBar = findViewById(R.id.bottom_bar_beans);
        toolbar = findViewById(R.id.cc_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        noDrinksCard = findViewById(R.id.no_drinks_info);
        drinksRecView = findViewById(R.id.drinks_rec_view);

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
                switch (item.getItemId()) {
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
                        // check if the user gave permission to use location
                        if (ContextCompat.checkSelfPermission(CoffeeLog.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Intent cafeFinderIntent = new Intent(CoffeeLog.this, CafeFinder.class);
                            startActivity(cafeFinderIntent);
                        } else {
                            ActivityCompat.requestPermissions(CoffeeLog.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                        }
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
     * Loads the drinks from the database to the local list
     *
     * @param db database to read
     */
    public static void loadDrinks(CoffeeDatabase db) {
        recipeDrinksDB = db.recipeDrinkDao().getAllRecipeDrinks();
        // if no drinks in the app, add all from the db
        if (drinksList.size() < 1) {
            for (RecipeDrinkDB drinkDB : recipeDrinksDB) {
                drinksList.add(createRecipeDrinkFromDatabase(drinkDB));
            }
        }
        // else, add only the ones that are not already there
        else {
            for (RecipeDrinkDB drinkDB : recipeDrinksDB) {
                boolean found = false;
                for (Drink drink : drinksList) {
                    if (drinkDB.drinkId == drink.getId()) {
                        if (drink.getClass() == DrinkFromRecipe.class) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    drinksList.add(createRecipeDrinkFromDatabase(drinkDB));
                }
            }
        }
        shopDrinksDB = db.shopDrinkDao().getAllShopDrinks();
        if (drinksList.size() < 1) {
            for (ShopDrinkDB drinkDB : shopDrinksDB) {
                drinksList.add(createShopDrinkFromDatabase(drinkDB));
            }
        } else {
            for (ShopDrinkDB drinkDB : shopDrinksDB) {
                boolean found = false;
                for (Drink drink : drinksList) {
                    if (drinkDB.drinkId == drink.getId()) {
                        if (drink.getClass() == DrinkFromShop.class) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    drinksList.add(createShopDrinkFromDatabase(drinkDB));
                }
            }
        }
    }

    /**
     * Initialises the drinks recycler view in a grid layout
     */
    private void initRecView() {
        adapter = new DrinksRecViewAdapter(this);
        adapter.setDrinks(drinksList);
        drinksRecView.setAdapter(adapter);
        drinksRecView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    /**
     * Creates a Drink object from the RecipeDrinkDB object read from the db
     *
     * @param drinkDB drink from recipe from the db
     * @return local Drink object
     */
    private static Drink createRecipeDrinkFromDatabase(RecipeDrinkDB drinkDB) {
        DrinkFromRecipe drink = new DrinkFromRecipe();
        drink.setId(drinkDB.drinkId);
        drink.setDateAdded(drinkDB.dateAdded);
        drink.setDrinkName(drinkDB.drinkName);
        drink.setRecipeName(drinkDB.recipeName);
        drink.setBeansUsed(drinkDB.beansUsed);
        drink.setAmountOfCoffeeUsed(drinkDB.amountOfCoffeeUsed);
        drink.setPrepMethodUsed(drinkDB.prepMethodUsed);
        drink.setExtrasUsed(drinkDB.extrasUsed);
        drink.setDrinkPhoto(drinkDB.drinkPhoto);
        return drink;
    }

    /**
     * Creates a Drink object from the ShopDrinkDB object read from the db
     *
     * @param drinkDB drink from shop from the db
     * @return local Drink object
     */
    private static Drink createShopDrinkFromDatabase(ShopDrinkDB drinkDB) {
        DrinkFromShop drink = new DrinkFromShop();
        drink.setId(drinkDB.drinkId);
        drink.setDateAdded(drinkDB.dateAdded);
        drink.setDrinkName(drinkDB.drinkName);
        drink.setPrice(drinkDB.price);
        drink.setCurrency(drinkDB.currency);
        drink.setSize(drinkDB.size);
        drink.setRating(drinkDB.rating);
        drink.setDrinkNotes(drinkDB.drinkNotes);
        drink.setShopName(drinkDB.shopName);
        drink.setShopAddress(drinkDB.shopAddress);
        drink.setDrinkPhoto(drinkDB.drinkPhoto);
        return drink;
    }

    /**
     * check if location permissions have been granted, and launch the map if so
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
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
     *
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

    /**
     * sets the bottom bar icon selected
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        bottomBar.setSelectedItemId(R.id.coffee_log_menu);
    }

    /**
     * Refreshes the drinks list and makes sure it's ordered from latest
     */
    @Override
    protected void onResume() {
        adapter.setDrinks(drinksList);
        adapter.notifyDataSetChanged();
        if (drinksList.size() < 1) {
            noDrinksCard.setVisibility(View.VISIBLE);
        } else {
            noDrinksCard.setVisibility(View.GONE);
        }
        super.onResume();
    }
}