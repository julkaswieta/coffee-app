package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.beansList;
import static com.example.coffeeapp.BeansRecViewAdapter.BEANS_ID_KEY;
import static com.example.coffeeapp.RecipesList.recipesList;
import static com.example.coffeeapp.RecipesRecViewAdapter.RECIPE_ID_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class BeanDetails extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Bean beans;
    private TextView txtName;
    private TextView txtDate;
    private TextView txtRoaster;
    private TextView txtDegreeOfRoast;
    private TextView txtBeansSpecs;
    private TextView txtStoreUrl;
    private TextView txtPrice;
    private TextView txtNotes;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_details);

        initViews();
        loadBeansData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.bd_toolbar);
        toolbarTitle = findViewById(R.id.edit_toolbar_title);
        txtName = findViewById(R.id.bd_beans_name);
        txtDate = findViewById(R.id.bd_date_added);
        txtRoaster = findViewById(R.id.bd_roaster_name);
        txtDegreeOfRoast = findViewById(R.id.bd_degree_of_roast);
        txtBeansSpecs = findViewById(R.id.bd_beans_specs);
        txtStoreUrl = findViewById(R.id.bd_url);
        txtPrice = findViewById(R.id.bd_price);
        txtNotes = findViewById(R.id.bd_notes);
        ratingBar = findViewById(R.id.bd_rating_bar);
        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Recipe details");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadBeansData() {
        // get the recipe from the incoming intent
        Intent incomingBeans = getIntent();
        if(incomingBeans != null) {
            int beansId = incomingBeans.getIntExtra(BEANS_ID_KEY, -1);
            for(Bean b : beansList) {
                if(b.getId() == beansId) {
                    beans = b;
                    break;
                }
            }
        }
        txtName.setText(beans.getName());
        DateFormat df = new DateFormat();
        txtDate.setText(df.format("yyyy-MM-dd", beans.getDateAdded()));
        txtRoaster.setText(beans.getRoaster());
        if(beans.getDegreeOfRoast() != 0) {
            txtDegreeOfRoast.setText("Degree of roast: " + beans.getDegreeOfRoast());
        }
        String specs = "";
        if(beans.isDecaf()) {
            specs += "Decaffeinated.\n";
        }
        else {
            specs += "Not decaf.\n";
        }
        if(beans.isFlavoured()) {
            specs += (beans.getFlavour() + " flavoured.\n");
        }
        specs += beans.isBlend();
        txtBeansSpecs.setText(specs);
        if(!beans.getUrlToShop().isEmpty()) {
            txtStoreUrl.setText(beans.getUrlToShop());
        }
        if(beans.getCostPerKg() != 0) {
            txtPrice.setText(String.valueOf(beans.getCostPerKg()) + " " + beans.getCurrency());
        }
        if(beans.getRating() != 0) {
            ratingBar.setRating(beans.getRating());
        }
        if(!beans.getNotes().isEmpty()) {
            txtNotes.setText(beans.getNotes());
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
            Intent editBeansIntent = new Intent(this, AddBeans.class);
            editBeansIntent.putExtra(BEANS_ID_KEY, beans.getId());
            startActivity(editBeansIntent);
            return true;
        }
        return false;
    }
}