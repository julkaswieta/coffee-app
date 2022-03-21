package com.example.coffeeapp;

import static com.example.coffeeapp.RecipesList.recipesList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Date;

public class AddRecipe extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 6;
    private static final int CAMERA_PERMISSION_CODE = 7;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private EditText recipeName;
    private EditText prepMethod;
    private Spinner beansSpinner;
    private Button btnAddBeans;
    private Button cancelButton;
    private Button saveButton;
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog dialogBack;
    private AlertDialog dialogSave;
    private AlertDialog dialogPhoto;
    private NumberPicker gramPicker1;
    private NumberPicker gramPicker2;
    private NumberPicker hourPicker;
    private NumberPicker minutesPicker;
    private NumberPicker secondsPicker;
    private CheckBox boughtGround;
    private Slider grindScale;
    private EditText grindNotes;
    private CheckBox milk;
    private CheckBox syrup;
    private CheckBox sugar;
    private EditText milkKind;
    private EditText milkAmount;
    private EditText syrupFlavour;
    private EditText syrupAmount;
    private EditText sugarKind;
    private EditText sugarAmount;
    private Slider rating;
    private EditText notes;
    private ImageButton btnAddPhoto;
    private Uri imageUri;
    private Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // initialise attributes
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recipeName = findViewById(R.id.inputRecipeName);
        prepMethod = findViewById(R.id.inputPrepMethod);
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
        boughtGround = findViewById(R.id.chckGround);
        grindScale = findViewById(R.id.grindSlider);
        grindNotes = findViewById(R.id.inputGrindNotes);
        milk = findViewById(R.id.chckMilk);
        syrup = findViewById(R.id.chckSyrup);
        sugar = findViewById(R.id.chckSugar);
        milkKind = findViewById(R.id.txtMilkKind);
        milkAmount = findViewById(R.id.txtMilkAmount);
        syrupFlavour = findViewById(R.id.txtSyrupFlavour);
        syrupAmount = findViewById(R.id.txtSyrupAmount);
        sugarKind = findViewById(R.id.txtSugarKind);
        sugarAmount = findViewById(R.id.txtSugarAmount);
        rating = findViewById(R.id.ratingSlider);
        notes = findViewById(R.id.inputNotes);
        btnAddPhoto = findViewById(R.id.add_photo_btn);

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
                onBackPressed();
            }
        });

        // onClickListener for the Save button - create a new recipe object
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = createRecipe();
                recipesList.add(recipe);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPhoto = dialogBuilder.create();
                dialogBuilder
                        .setTitle(R.string.dialog_camera_gallery)
                        .setNegativeButton("Open camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // check if the user has granted permissions to use the camera - if yes, open it, if no, ask for them
                                if(ContextCompat.checkSelfPermission(AddRecipe.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                                }
                                else {
                                    ActivityCompat.requestPermissions(AddRecipe.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                                }

                            }
                        })
                        .setPositiveButton("Open gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // launches the gallery
                                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(openGallery, GALLERY_REQUEST_CODE);

                            }
                        }).show();
            }
        });
    }

    // handles the top back button
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Gathers all data from the views and assigns it to the recipe object. If name, beans and method not specified, displays a dialog.
     * @return Recipe object created
     */
    private Recipe createRecipe() {
        Recipe recipe = new Recipe();
        // check if name, beans and method are specified
        if(recipeName.getText().toString().isEmpty() || beansSpinner.getSelectedItem() == null || prepMethod.getText().toString().isEmpty()) {
            dialogSave = dialogBuilder.create();
            dialogBuilder
                    .setTitle(R.string.dialog_check_title)
                    .setMessage(R.string.dialog_check_mandatory)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogBack.dismiss();
                        }
                    }).show();
        }
        // proceed with creating the recipe
        else {
            recipe.setName(recipeName.getText().toString());
            recipe.setDateAdded(new Date());
            // TODO: fix set beans to work properly
            recipe.setBeansUsed(new Bean(beansSpinner.getSelectedItem().toString().split(",")[0], beansSpinner.getSelectedItem().toString().split(",")[1]));
            String tempAmount = Integer.toString(gramPicker1.getValue()) + "." + Integer.toString(gramPicker2.getValue());
            float amount= Float.parseFloat(tempAmount);
            recipe.setAmountOfCoffee(amount);
            recipe.setMethodOfBrewing(prepMethod.getText().toString());
            recipe.setBrewingTime(Integer.toString(hourPicker.getValue()) + "," + Integer.toString(minutesPicker.getValue()) + "," + Integer.toString(secondsPicker.getValue()));
            recipe.setBoughtGround(boughtGround.isChecked());
            recipe.setGrindScale((int)grindScale.getValue());
            recipe.setGrindNotes(grindNotes.getText().toString());
            if(milk.isChecked()) { recipe.setMilk(milkKind.getText().toString() + "," + milkAmount.getText().toString()); }
            if(syrup.isChecked()) { recipe.setSyrup(syrupFlavour.getText().toString() + "," + syrupAmount.getText().toString()); }
            if(sugar.isChecked()) { recipe.setSugar(sugarKind.getText().toString() + "," + sugarAmount.getText().toString()); }
            recipe.setRating((int)rating.getValue());
            recipe.setNotes(notes.getText().toString());
            recipe.setPhotoUri(imageUri);
            Toast.makeText(this, "Recipe " + recipe.getName() + " saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        return recipe;
    }

    // Don't let the user just quit without saving or confirming to leave
    @Override
    public void onBackPressed() {
        dialogBack = dialogBuilder.create();
        dialogBuilder
                .setTitle(R.string.dialog_discard_draft)
                .setMessage(R.string.dialog_discard_message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogBack.dismiss();
                    }
                })
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    // handles results from intents launched - gallery & camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if(requestCode == GALLERY_REQUEST_CODE) {
                Uri imageChosen = data.getData();
                btnAddPhoto.setImageURI(imageChosen);
                imageUri = imageChosen;
            }
            else if(requestCode == CAMERA_REQUEST_CODE) {
                if(!data.getExtras().isEmpty()) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    btnAddPhoto.setImageBitmap(photo);
                }
            }
        }
    }

    // check if Camera permission is granted, if so, open the camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
            }
            else{
                Toast.makeText(this, "Access to camera denied. Allow it from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}