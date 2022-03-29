package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.loadBeans;
import static com.example.coffeeapp.RecipesList.loadRecipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDrinkDB;
import com.example.coffeeapp.db.ShopDrinkDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CoffeeLog extends AppCompatActivity {
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
        if(drinksList.size() < 1) {
            noDrinksCard.setVisibility(View.VISIBLE);
        }
        else {
            noDrinksCard.setVisibility(View.GONE);
        }
    }

    private static void loadDrinks(CoffeeDatabase db) {
        recipeDrinksDB = db.recipeDrinkDao().getAllRecipeDrinks();
        if(drinksList.size() < 1) {
            for (RecipeDrinkDB drinkDB : recipeDrinksDB) {
                drinksList.add(createRecipeDrinkFromDatabase(drinkDB));
            }
        }
        else{
            for (RecipeDrinkDB drinkDB : recipeDrinksDB) {
                boolean found = false;
                for(Drink drink : drinksList) {
                    if(drinkDB.drinkId == drink.getId()) {
                        if(drink.getClass() == DrinkFromRecipe.class) {
                            found = true;
                            break;
                        }
                    }
                }
                if(!found) {
                    drinksList.add(createRecipeDrinkFromDatabase(drinkDB));
                }
            }
        }

        shopDrinksDB = db.shopDrinkDao().getAllShopDrinks();
        if(drinksList.size() < 1) {
            for(ShopDrinkDB drinkDB : shopDrinksDB) {
                drinksList.add(createShopDrinkFromDatabase(drinkDB));
            }
        }
        else {
            for(ShopDrinkDB drinkDB : shopDrinksDB) {
                boolean found = false;
                for(Drink drink : drinksList) {
                    if(drinkDB.drinkId == drink.getId()) {
                        if (drink.getClass() == DrinkFromShop.class) {
                            found = true;
                            break;
                        }
                    }
                }
                if(!found) {
                    drinksList.add(createShopDrinkFromDatabase(drinkDB));
                }
            }
        }
    }

    private void initRecView() {
        adapter = new DrinksRecViewAdapter(this);
        adapter.setDrinks(drinksList);
        drinksRecView.setAdapter(adapter);
        drinksRecView.setLayoutManager(new GridLayoutManager(this, 2));
    }

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

    @Override
    protected void onResume() {
        adapter.setDrinks(drinksList);
        adapter.notifyDataSetChanged();
        if(drinksList.size() < 1) {
           noDrinksCard.setVisibility(View.VISIBLE);
        }
        else {
            noDrinksCard.setVisibility(View.GONE);
        }
        super.onResume();
    }
}