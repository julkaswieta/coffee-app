package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.beansList;
import static com.example.coffeeapp.BeansList.loadBeans;
import static com.example.coffeeapp.BeansRecViewAdapter.BEANS_ID_KEY;
import static com.example.coffeeapp.Recipe.idCounter;
import static com.example.coffeeapp.RecipesList.recipesFromDB;
import static com.example.coffeeapp.RecipesList.recipesList;
import static com.example.coffeeapp.RecipesRecViewAdapter.RECIPE_ID_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import java.io.FileDescriptor;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;

public class AddRecipe extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 6;
    private static final int CAMERA_PERMISSION_CODE = 7;
    private static final int GALLERY_PERMISSION_CODE = 8;
    private static final int FROM_GALLERY = 100;
    private static final int FROM_CAMERA = 101;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private EditText recipeName;
    private EditText prepMethod;
    private Spinner beansSpinner;
    private Button btnAddBeans;
    private Button cancelButton;
    private Button saveButton;
    private MaterialAlertDialogBuilder backDialogBuilder;
    private MaterialAlertDialogBuilder saveDialogBuilder;
    private MaterialAlertDialogBuilder photoDialogBuilder;
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
    private RatingBar rating;
    private EditText notes;
    private ImageButton btnAddPhoto;
    private Uri imageUri;
    private Bitmap photo;
    private int photoSource;
    private Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    ArrayAdapter<Bean> beansAdapter;
    private Recipe incomingRecipe;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        initViews();
        checkAndLoadEditData();
    }

    /**
     * Initialises the whole layout
     */
    private void initViews() {
        // initialise attributes
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recipeName = findViewById(R.id.inputRecipeName);
        prepMethod = findViewById(R.id.inputPrepMethod);
        beansSpinner = findViewById(R.id.beansSpinner);
        btnAddBeans = findViewById(R.id.btnAddBeans);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        backDialogBuilder = new MaterialAlertDialogBuilder(this);
        saveDialogBuilder = new MaterialAlertDialogBuilder(this);
        photoDialogBuilder = new MaterialAlertDialogBuilder(this);
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
        rating = findViewById(R.id.ratingBarAR);
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
        // create the adapter for the spinner

        CoffeeDatabase db = CoffeeDatabase.getDatabase(this.getApplicationContext());
        loadBeans(db);
        beansAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, beansList);
        beansSpinner.setAdapter(beansAdapter);

        // setup the number pickers for selecting number of grams and brewing time
        gramPicker1.setMinValue(0);
        gramPicker1.setMaxValue(99);
        gramPicker2.setMinValue(0);
        gramPicker2.setMaxValue(9);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);

        initListeners();
    }

    /**
     * Defines all buttons' behaviours
     */
    private void initListeners() {
        // onClickListener for the Cancel button - check if the user is sure to go back
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // onClickListener for the Save button - create a new recipe object
        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // check if name, beans and method are specified
                if(recipeName.getText().toString().isEmpty() || beansSpinner.getSelectedItem() == null || prepMethod.getText().toString().isEmpty()) {
                    dialogSave = saveDialogBuilder.create();
                    saveDialogBuilder
                            .setTitle(R.string.dialog_check_title)
                            .setMessage(R.string.dialog_check_mandatory)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogSave.dismiss();
                                }
                            }).show();
                }
                else {
                    createRecipe();
                    finish();
                }
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPhoto = photoDialogBuilder.create();
                photoDialogBuilder
                        .setTitle(R.string.dialog_camera_gallery)
                        .setNegativeButton("Open camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // check if the user has granted permissions to use the camera - if yes, open it, if no, ask for them
                                if(ContextCompat.checkSelfPermission(AddRecipe.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                                    photoSource = FROM_CAMERA;
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
                                if(ContextCompat.checkSelfPermission(AddRecipe.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                                    photoSource = FROM_GALLERY;
                                }
                                else {
                                    ActivityCompat.requestPermissions(AddRecipe.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                                }


                            }
                        }).show();
            }
        });

        btnAddBeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openAddBeans = new Intent(AddRecipe.this, AddBeans.class);
                startActivityForResult(openAddBeans, 90);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkAndLoadEditData() {
        // check if the activity is open to add or edit
        Intent incomingRecipeIntent = getIntent();
        if(incomingRecipeIntent != null) {
            int recipeId = incomingRecipeIntent.getIntExtra(RECIPE_ID_KEY, -1);
            if(recipeId != -1) {
                for(Recipe r : recipesList) {
                    if(r.getId() == recipeId) {
                        incomingRecipe = r;
                        toolbarTitle.setText("Edit recipe");
                        break;
                    }
                }
            }
        }
        if(incomingRecipe != null) {
            // TODO: display the stuff in appropriate boxes
            recipeName.setText(incomingRecipe.getName());
            for(int i = 0; i < beansList.size(); i++) {
                if(beansList.get(i).getId() == incomingRecipe.getBeansUsed().getId()) {
                    beansSpinner.setSelection(i);
                    break;
                }
            }
            if(incomingRecipe.getAmountOfCoffee() != 0) {
                String temp = String.valueOf(incomingRecipe.getAmountOfCoffee());
                String[] tempArray = temp.split("[.]");
                gramPicker1.setValue(Integer.valueOf(tempArray[0]));
                gramPicker2.setValue(Integer.valueOf(tempArray[1]));
            }
            prepMethod.setText(incomingRecipe.getMethodOfBrewing());
            if(!incomingRecipe.getBrewingTime().equals(LocalTime.of(0, 0))) {
                String temp = incomingRecipe.getBrewingTime().toString();
                String[] tempArray = temp.split(":");
                hourPicker.setValue(Integer.valueOf(tempArray[0]));
                minutesPicker.setValue(Integer.valueOf(tempArray[1]));
                if(tempArray.length > 2) {
                    secondsPicker.setValue(Integer.valueOf(tempArray[2]));
                }
            }
            boughtGround.setChecked(incomingRecipe.isBoughtGround());
            if(incomingRecipe.getGrindScale() != 0) {
                grindScale.setValue(incomingRecipe.getGrindScale());
            }
            if(!incomingRecipe.getGrindNotes().isEmpty()) {
                grindNotes.setText(incomingRecipe.getGrindNotes());
            }
            if(incomingRecipe.getMilk() != null) {
                if(!incomingRecipe.getMilk().isEmpty()) {
                    milk.setChecked(true);
                    try {
                        String[] tempArray = incomingRecipe.getMilk().split(",");
                        milkKind.setText(tempArray[0]);
                        milkAmount.setText(tempArray[1]);
                    }
                    catch (Exception ex) {
                        Log.e(this.getClass().toString(), "Couldn't load milk data properly.");
                    }
                }
            }
            if(incomingRecipe.getSyrup() != null) {
                if(!incomingRecipe.getSyrup().isEmpty()) {
                    syrup.setChecked(true);
                    try {
                        String[] tempArray = incomingRecipe.getSyrup().split(",");
                        syrupFlavour.setText(tempArray[0]);
                        syrupAmount.setText(tempArray[1]);
                    }
                    catch (Exception ex) {
                        Log.e(this.getClass().toString(), "Couldn't load syrup data properly.");
                    }
                }
            }
            if(incomingRecipe.getSugar() != null) {
                if(!incomingRecipe.getSugar().isEmpty()) {
                    sugar.setChecked(true);
                    try {
                        String[] tempArray = incomingRecipe.getSugar().split(",");
                        sugarKind.setText(tempArray[0]);
                        sugarAmount.setText(tempArray[1]);
                    }
                    catch (Exception ex) {
                        Log.e(this.getClass().toString(), "Couldn't load sugar data properly.");
                    }
                }
            }
            if(incomingRecipe.getRating() != 0) {
                rating.setRating(incomingRecipe.getRating());
            }
            if(!incomingRecipe.getNotes().isEmpty()) {
                notes.setText(incomingRecipe.getNotes());
            }
        }
    }

    /*
     * Handles the top back button
     */
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Handles back button presses - asks to confirm to leave
     */
    @Override
    public void onBackPressed() {
        dialogBack = backDialogBuilder.create();
        backDialogBuilder
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

    /**
     * handles results from intents launched - gallery & camera
     */
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
                    photo = (Bitmap) data.getExtras().get("data");
                    btnAddPhoto.setImageBitmap(photo);
                }
            }
        }
    }

    /**
     * check if Camera or gallery access permission is granted, if so, open the camera/gallery
     * @param requestCode   request code passed when launching intent
     * @param permissions   permissions to check
     * @param grantResults  checked permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                photoSource = FROM_CAMERA;
            }
            else{
                Toast.makeText(this, "Access to camera denied. Allow it from settings.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == GALLERY_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                photoSource =FROM_GALLERY;
            }
            else{
                Toast.makeText(this, "Access to gallery denied. Allow it from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Create a Bitmap from the supplied URI
     * @param uri   URI of an image
     * @return      Bitmap for an image
     * @throws IOException
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * Gathers all data from the views and assigns it to the recipe object. If name, beans and method not specified, displays a dialog.
     * @return Recipe object created
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createRecipe() {
        if(incomingRecipe != null) {
            incomingRecipe.setName(recipeName.getText().toString());
            incomingRecipe.setBeansUsed((Bean)beansSpinner.getSelectedItem());
            String tempAmount = Integer.toString(gramPicker1.getValue()) + "." + Integer.toString(gramPicker2.getValue());
            float amount = Float.parseFloat(tempAmount);
            incomingRecipe.setAmountOfCoffee(amount);
            incomingRecipe.setMethodOfBrewing(prepMethod.getText().toString());
            incomingRecipe.setBrewingTime(LocalTime.of(hourPicker.getValue(), minutesPicker.getValue(), secondsPicker.getValue()));
            incomingRecipe.setBoughtGround(boughtGround.isChecked());
            incomingRecipe.setGrindScale((int) grindScale.getValue());
            incomingRecipe.setGrindNotes(grindNotes.getText().toString());
            if (milk.isChecked()) {
                incomingRecipe.setMilk(milkKind.getText().toString() + ", " + milkAmount.getText().toString());
            }
            if (syrup.isChecked()) {
                incomingRecipe.setSyrup(syrupFlavour.getText().toString() + ", " + syrupAmount.getText().toString());
            }
            if (sugar.isChecked()) {
                incomingRecipe.setSugar(sugarKind.getText().toString() + ", " + sugarAmount.getText().toString());
            }

            incomingRecipe.setRating(rating.getRating());
            incomingRecipe.setNotes(notes.getText().toString());
            /*try {
                if (photoSource == FROM_GALLERY) {
                    incomingRecipe.setPhoto(getBitmapFromUri(imageUri));
                } else if (photoSource == FROM_CAMERA) {
                    recipe.setPhoto(photo);
                }
            } catch (IOException ex) {
                Log.e(this.getClass().toString(), "Couldn't save image");
            }
             */
        }
        else {
            Recipe recipe = new Recipe();
            // get the biggest available recipeId so that the new ones are not replicated
            CoffeeDatabase db = CoffeeDatabase.getDatabase(AddRecipe.this.getApplicationContext());
            if (db.recipeDao().getAllRecipes().size() > 0) {
                idCounter = db.recipeDao().getBiggestRecipeId();
            } else {
                idCounter = 0;
            }
            recipe.setId(Recipe.nextId());
            recipe.setName(recipeName.getText().toString());
            recipe.setDateAdded(new Date());
            recipe.setBeansUsed((Bean)beansSpinner.getSelectedItem());
            String tempAmount = Integer.toString(gramPicker1.getValue()) + "." + Integer.toString(gramPicker2.getValue());
            float amount = Float.parseFloat(tempAmount);
            recipe.setAmountOfCoffee(amount);
            recipe.setMethodOfBrewing(prepMethod.getText().toString());
            recipe.setBrewingTime(LocalTime.of(hourPicker.getValue(), minutesPicker.getValue(), secondsPicker.getValue()));
            recipe.setBoughtGround(boughtGround.isChecked());
            recipe.setGrindScale((int) grindScale.getValue());
            recipe.setGrindNotes(grindNotes.getText().toString());
            if (milk.isChecked()) {
                recipe.setMilk(milkKind.getText().toString() + ", " + milkAmount.getText().toString());
            }
            if (syrup.isChecked()) {
                recipe.setSyrup(syrupFlavour.getText().toString() + ", " + syrupAmount.getText().toString());
            }
            if (sugar.isChecked()) {
                recipe.setSugar(sugarKind.getText().toString() + ", " + sugarAmount.getText().toString());
            }
            recipe.setRating(rating.getRating());
            recipe.setNotes(notes.getText().toString());
            try {
                if (photoSource == FROM_GALLERY) {
                    recipe.setPhoto(getBitmapFromUri(imageUri));
                } else if (photoSource == FROM_CAMERA) {
                    recipe.setPhoto(photo);
                }
            } catch (IOException ex) {
                Log.e(this.getClass().toString(), "Couldn't save image");
            }
            recipesList.add(recipe);

            // persist the recipe
            RecipeDB recipeDB = new RecipeDB();
            recipeDB.recipeId = recipe.getId();
            recipeDB.name = recipe.getName();
            recipeDB.dateAdded = recipe.getDateAdded();
            recipeDB.beansUsedId = recipe.getBeansUsed().getId();
            recipeDB.amountOfCoffee = recipe.getAmountOfCoffee();
            recipeDB.methodOfBrewing = recipe.getMethodOfBrewing();
            recipeDB.brewingTime = recipe.getBrewingTime();
            recipeDB.boughtGround = recipe.isBoughtGround();
            recipeDB.grindScale = recipe.getGrindScale();
            recipeDB.grindNotes = recipe.getGrindNotes();
            recipeDB.milk = recipe.getMilk();
            recipeDB.syrup = recipe.getSyrup();
            recipeDB.sugar = recipe.getSugar();
            recipeDB.rating = recipe.getRating();
            recipeDB.notes = recipe.getNotes();

            recipesFromDB.add(recipeDB);
            db.recipeDao().insertRecipe(recipeDB);
            Toast.makeText(this, "Recipe " + recipe.getName() + " saved", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onResume() {
        beansAdapter.notifyDataSetChanged();
        super.onResume();
    }
}