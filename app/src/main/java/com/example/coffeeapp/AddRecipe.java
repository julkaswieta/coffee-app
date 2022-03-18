package com.example.coffeeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Add new recipe");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // add options to the Beans Spinner

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

    // goes back to the recipes screen when back button pressed
    public boolean onSupportNavigateUp() {
        // TODO: add dialogue asking to confirm discard (back button) - if I can do that
        onBackPressed();
        return true;
    }

    private Recipe createRecipe() {
        Recipe recipe = new Recipe();
        // TODO: check if name, beans and method are specified
        if(recipeName.toString().isEmpty() || beansSpinner.getSelectedItem() == null) {
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
            recipe.setName(recipeName.toString());
        }
        return recipe;
    }


}