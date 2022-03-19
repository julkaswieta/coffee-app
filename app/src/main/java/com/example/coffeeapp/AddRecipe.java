package com.example.coffeeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class AddRecipe extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private EditText recipeName;
    private Spinner beansSpinner;
    private Button btnAddBeans;
    private Button cancelButton;
    private Button saveButton;
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog dialog;
    private NumberPicker gramPicker1;
    private NumberPicker gramPicker2;
    private NumberPicker hourPicker;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // initialise attributes
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recipeName = findViewById(R.id.inputRecipeName);
        beansSpinner = findViewById(R.id.beansSpinner);
        btnAddBeans = findViewById(R.id.btnAddBeans);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        dialogBuilder = new MaterialAlertDialogBuilder(this);
        gramPicker1 = findViewById(R.id.gram_picker1);
        gramPicker2 = findViewById(R.id.gram_picker2);
        hourPicker = findViewById(R.id.hour_picker);
        minutesPicker = findViewById(R.id.minutes_picker);
        secondsPicker = findViewById(R.id.seconds_picker);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Add new recipe");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // add options to the Beans Spinner
        ArrayList<String> beans = new ArrayList<>();
        beans.add("Brazylia Santos, Agifa");
        beans.add("Vanilla&Hazelnut, Agifa");
        // create the adapter for the spinner
        ArrayAdapter<String> beansAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, beans);
        beansSpinner.setAdapter(beansAdapter);
        // TODO: add a hint to the spinner instead of displaying the first item

        // setup the number pickers for selecting number of grams and brewing time
        gramPicker1.setMinValue(0);
        gramPicker1.setMaxValue(99);
        gramPicker2.setMinValue(0);
        gramPicker2.setMaxValue(9);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(24);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);

        // onClickListener for the Cancel button - check if the user is sure to go back
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Display a dialog to confirm the user wants to discard the new recipe
                dialog = dialogBuilder.create();
                dialogBuilder
                        .setTitle(R.string.dialog_discard_draft)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).show();
            }
        });

        // onClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: create a new Recipe object and assign all attributes
                createRecipe();
                // TODO: display a snackbar/toast that the recipe has been created
            }
        });
    }

    // goes back to the recipes screen when the top back button pressed
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Recipe createRecipe() {
        Recipe recipe = new Recipe();
        // TODO: check if name, beans and method are specified
        if(recipeName.getText().toString().isEmpty() || beansSpinner.getSelectedItem() == null) {
            dialog = dialogBuilder.create();
            dialogBuilder
                    .setTitle(R.string.dialog_check_mandatory)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    }).show();
        }
        // proceed with creating the recipe
        else {
            recipe.setName(recipeName.getText().toString());
            Toast.makeText(this, "Recipe " + recipe.getName() + " saved", Toast.LENGTH_SHORT).show();
        }
        return recipe;
    }

    // Don't let the user just quit without saving or confirming to leave
    @Override
    public void onBackPressed() {
        dialog = dialogBuilder.create();
        dialogBuilder
                .setTitle(R.string.dialog_discard_draft)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }
}