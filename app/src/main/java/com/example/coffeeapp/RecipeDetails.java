package com.example.coffeeapp;

import static com.example.coffeeapp.RecipesList.recipesList;
import static com.example.coffeeapp.RecipesRecViewAdapter.RECIPE_ID_KEY;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import java.time.LocalTime;

public class RecipeDetails extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Recipe recipe;
    private TextView txtName;
    private TextView txtDateAdded;
    private TextView txtBeans;
    private TextView txtAmountCoffee;
    private TextView txtPrepMethod;
    private TextView txtBrewTime;
    private CheckBox chckBoughtGround;
    private TextView txtGrindScale;
    private TextView txtGrindNotes;
    private TextView txtExtras;
    private TextView txtNotes;
    private RatingBar ratingBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        // initialise all views in the layout
        initViews();
        // load the recipe to display
        loadRecipeData();
    }

    /**
     * Initialises the whole layout
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initViews() {
        //initialise attributes
        toolbar = findViewById(R.id.rec_details_toolbar);
        toolbarTitle = findViewById(R.id.edit_toolbar_title);
        txtName = findViewById(R.id.rec_details_name);
        txtDateAdded = findViewById(R.id.rec_details_date_added);
        txtBeans = findViewById(R.id.rec_details_beans_used);
        txtAmountCoffee = findViewById(R.id.rec_details_coffee_amount);
        txtPrepMethod = findViewById(R.id.rec_details_prep_method);
        txtBrewTime = findViewById(R.id.rec_details_brew_time);
        chckBoughtGround = findViewById(R.id.rec_details_checkbox);
        txtGrindScale = findViewById(R.id.rec_details_grind_scale);
        txtGrindNotes = findViewById(R.id.rec_details_grind_notes);
        txtExtras = findViewById(R.id.rec_details_extras);
        txtNotes = findViewById(R.id.rec_details_notes);
        ratingBar = findViewById(R.id.rec_details_rating_bar);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Recipe details");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    /**
     * Loads the recipe data into appropriate views
     */
    private void loadRecipeData() {
        // get the recipe from the incoming intent
        Intent incomingRecipe = getIntent();
        if(incomingRecipe != null) {
            int recipeId = incomingRecipe.getIntExtra(RECIPE_ID_KEY, -1);
            for(Recipe r : recipesList) {
                if(r.getId() == recipeId) {
                    recipe = r;
                    break;
                }
            }
        }

        // fill in any views that have values
        // mandatory fields
        txtName.setText(recipe.getName());
        DateFormat df = new DateFormat();
        txtDateAdded.setText(df.format("yyyy-MM-dd", recipe.getDateAdded()));
        if(recipe.getBeansUsed() != null) {
            txtBeans.setText(recipe.getBeansUsed().getName());
        }
        txtPrepMethod.setText(recipe.getMethodOfBrewing());
        // optional fields
        if(recipe.getAmountOfCoffee() != 0) {
            txtAmountCoffee.setText(String.valueOf(recipe.getAmountOfCoffee()));
        }
        if(!recipe.getBrewingTime().equals(LocalTime.of(0, 0))) {
            txtBrewTime.setText(recipe.getBrewingTime().toString());
        }
        if(recipe.isBoughtGround()) {
            chckBoughtGround.setChecked(true);
        }
        if(recipe.getGrindScale() != 0) {
            txtGrindScale.setText("Grind scale: " + String.valueOf(recipe.getGrindScale()));
        }
        if(!recipe.getGrindNotes().isEmpty() && recipe.getGrindNotes() != null) {
            txtGrindNotes.setText(recipe.getGrindNotes());
        }
        String extras = "";
        boolean extrasExist = false;
        if(recipe.getMilk() != null) {
            extrasExist = true;
            extras += "Milk ";
            extras += recipe.getMilk();
            extras += "\n";
        }
        if(recipe.getSyrup() != null) {
            extras += "Syrup ";
            extrasExist = true;
            extras += recipe.getSyrup();
            extras += "\n";
        }
        if(recipe.getSugar() != null) {
            extras += "Sugar ";
            extrasExist = true;
            extras += recipe.getSugar();
        }
        if(extrasExist) {
            txtExtras.setText(extras);
        }
        if(recipe.getRating() != 0) {
            ratingBar.setRating(recipe.getRating());
        }
        if(recipe.getNotes() != null) {
            if(!recipe.getNotes().isEmpty()) {
                txtNotes.setText(recipe.getNotes());
            }
        }
    }

    /**
     * handles the top back button
     */
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Adds the menu options to the toolbar
     * @param menu layout file
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    /**
     * Gets the user to the edit (add) recipe activity to add a new recipe
     * @param item  menu item pressed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_option) {
            Intent editRecipeIntent = new Intent(this, AddRecipe.class);
            editRecipeIntent.putExtra(RECIPE_ID_KEY, recipe.getId());
            startActivity(editRecipeIntent);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        loadRecipeData();
        super.onResume();
    }
}