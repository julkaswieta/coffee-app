package com.example.coffeeapp;

import static com.example.coffeeapp.BeansList.loadBeans;
import static com.example.coffeeapp.CoffeeLog.drinksList;
import static com.example.coffeeapp.CoffeeLog.recipeDrinksDB;
import static com.example.coffeeapp.CoffeeLog.shopDrinksDB;
import static com.example.coffeeapp.DrinkFromShop.idCounterS;
import static com.example.coffeeapp.DrinkFromRecipe.idCounterR;
import static com.example.coffeeapp.RecipesList.loadRecipes;
import static com.example.coffeeapp.RecipesList.recipesList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeapp.db.CoffeeDatabase;
import com.example.coffeeapp.db.RecipeDrinkDB;
import com.example.coffeeapp.db.ShopDrinkDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AddDrink extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 6;
    private static final int CAMERA_PERMISSION_CODE = 7;
    private static final int GALLERY_PERMISSION_CODE = 8;
    private static final int FROM_GALLERY = 100;
    private static final int FROM_CAMERA = 101;

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private MaterialAlertDialogBuilder backDialogBuilder;
    private MaterialAlertDialogBuilder saveDialogBuilder;
    private MaterialAlertDialogBuilder photoDialogBuilder;
    private AlertDialog backDialog;
    private AlertDialog saveDialog;
    private AlertDialog photoDialog;
    private RadioGroup recipeSourceGroup;
    private ConstraintLayout storeView;
    private ConstraintLayout recipeView;
    private Button saveButton;
    private Button cancelButton;
    private ImageButton addPhotoButton;
    private Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    private int photoSource;
    private Uri imageUri;
    private Bitmap photo;

    // views for storeView
    private EditText txtDrinkNameS;
    private EditText txtPriceS;
    private Spinner currencySpinnerS;
    private Spinner sizeSpinnerS;
    private RatingBar ratingS;
    private EditText txtShopNameS;
    private EditText txtShopAddressS;
    private EditText txtNotesS;
    private ArrayList<String> currencies = new ArrayList<>(Arrays.asList("£", "€", "zł"));
    private ArrayList<String> sizes = new ArrayList<>(Arrays.asList("Unknown", "Small", "Medium", "Large"));

    // views for recipeView
    private EditText txtDrinkNameR;
    private Spinner recipeSpinnerR;
    private Button addRecipeBtnR;
    private TextView txtRecipeNameR;
    private TextView txtBeansUsedR;
    private TextView txtCoffeeAmountR;
    private TextView txtPrepMethodR;
    private TextView txtExtrasR;
    private ArrayAdapter<Recipe> recipeArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drink);

        initViews();
    }

    /**
     * Initialises the whole layout
     */
    private void initViews() {
        toolbar = findViewById(R.id.ad_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        backDialogBuilder = new MaterialAlertDialogBuilder(this);
        saveDialogBuilder = new MaterialAlertDialogBuilder(this);
        photoDialogBuilder = new MaterialAlertDialogBuilder(this);
        recipeSourceGroup = findViewById(R.id.drink_source_group);
        storeView = findViewById(R.id.from_shop_tab);
        recipeView = findViewById(R.id.from_recipe_tab);
        saveButton = findViewById(R.id.ad_save_button);
        cancelButton = findViewById(R.id.ad_cancel_button);
        addPhotoButton = findViewById(R.id.ad_add_photo_btn);
        //store view
        txtDrinkNameS = findViewById(R.id.ad_drink_name);
        txtPriceS = findViewById(R.id.ad_price);
        currencySpinnerS = findViewById(R.id.ad_currency_spinner);
        sizeSpinnerS = findViewById(R.id.ad_size_spinner);
        ratingS = findViewById(R.id.ad_rating_bar);
        txtShopNameS = findViewById(R.id.ad_shop_name);
        txtShopAddressS = findViewById(R.id.ad_shop_address);
        txtNotesS = findViewById(R.id.ad_notes);
        // recipe view
        txtDrinkNameR = findViewById(R.id.ad_rec_drink_name);
        recipeSpinnerR = findViewById(R.id.ad_rec_spinner);
        addRecipeBtnR = findViewById(R.id.ad_new_rec_btn);
        txtRecipeNameR = findViewById(R.id.ad_rec_name);
        txtBeansUsedR = findViewById(R.id.ad_rec_beans);
        txtCoffeeAmountR = findViewById(R.id.ad_rec_coffee_amount);
        txtPrepMethodR = findViewById(R.id.ad_rec_prep_method);
        txtExtrasR = findViewById(R.id.ad_rec_extras);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Add new drink");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initListeners();

        // fill the spinner with currencies
        ArrayAdapter<String> currencyAdapter =
                new ArrayAdapter<>(this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        currencies);
        currencySpinnerS.setAdapter(currencyAdapter);
        // fill the spinner with sizes
        ArrayAdapter<String> sizesAdapter =
                new ArrayAdapter<>(this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        sizes);
        sizeSpinnerS.setAdapter(sizesAdapter);
        // fill the spinner with recipes
        CoffeeDatabase db = CoffeeDatabase.getDatabase(this);
        loadBeans(db);
        loadRecipes(db);
        recipeArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, recipesList);
        recipeSpinnerR.setAdapter(recipeArrayAdapter);
    }

    private void initListeners() {
        recipeSourceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioGroup.getCheckedRadioButtonId() == R.id.ad_shop_rb) {
                    storeView.setVisibility(View.VISIBLE);
                    recipeView.setVisibility(View.GONE);
                }
                else if(radioGroup.getCheckedRadioButtonId() == R.id.ad_recipe_rb) {
                    storeView.setVisibility(View.GONE);
                    recipeView.setVisibility(View.VISIBLE);
                }
            }
        });

        addRecipeBtnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newRecipeIntent = new Intent(AddDrink.this, AddRecipe.class);
                startActivity(newRecipeIntent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recipeSpinnerR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = (Recipe) adapterView.getItemAtPosition(i);
                txtRecipeNameR.setText(recipe.getName());
                txtBeansUsedR.setText(recipe.getBeansUsed().toString());
                txtCoffeeAmountR.setText("Amount of coffee: "+ String.valueOf(recipe.getAmountOfCoffee()) + " grams");
                txtPrepMethodR.setText("Method: " + recipe.getMethodOfBrewing());
                String extras = "";
                if(recipe.getMilk() != null) {
                    extras += ("Milk: " + recipe.getMilk() + "\n");
                }
                if(recipe.getSyrup() != null) {
                    extras += ("Syrup: " + recipe.getSyrup() + "\n");
                }
                if(recipe.getSugar() != null) {
                    extras += ("Sugar: " + recipe.getSugar() + "\n");
                }
                txtExtrasR.setText("Extras:\n" + extras);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // onClickListener for the Save button - create a new recipe object
        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // check which option is chosen
                if(recipeSourceGroup.getCheckedRadioButtonId() == R.id.ad_shop_rb) {
                    // check if name is specified
                    if(txtDrinkNameS.getText().toString().isEmpty() || txtShopNameS.getText().toString().isEmpty()) {
                        saveDialog = saveDialogBuilder.create();
                        saveDialogBuilder
                                .setTitle(R.string.dialog_check_title)
                                .setMessage(R.string.dialog_check_mandatory_drink)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveDialog.dismiss();
                                    }
                                }).show();
                    }
                    else {
                        createShopDrink();
                        finish();
                    }

                }
                else if(recipeSourceGroup.getCheckedRadioButtonId() == R.id.ad_recipe_rb) {
                    if (txtDrinkNameR.getText().toString().isEmpty() || recipeSpinnerR.getSelectedItem() == null) {
                        saveDialog = saveDialogBuilder.create();
                        saveDialogBuilder
                                .setTitle(R.string.dialog_check_title)
                                .setMessage(R.string.dialog_check_mandatory_drink_rec)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        saveDialog.dismiss();
                                    }
                                }).show();
                    }
                    else {
                        createRecipeDrink();
                        finish();
                    }
                }
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialog = photoDialogBuilder.create();
                photoDialogBuilder
                        .setTitle(R.string.dialog_camera_gallery)
                        .setNegativeButton("Open camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // check if the user has granted permissions to use the camera - if yes, open it, if no, ask for them
                                if (ContextCompat.checkSelfPermission(AddDrink.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                                    photoSource = FROM_CAMERA;
                                } else {
                                    ActivityCompat.requestPermissions(AddDrink.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                                }

                            }
                        })
                        .setPositiveButton("Open gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // launches the gallery
                                if (ContextCompat.checkSelfPermission(AddDrink.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                                    photoSource = FROM_GALLERY;
                                } else {
                                    ActivityCompat.requestPermissions(AddDrink.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                                }


                            }
                        }).show();
            }
        });
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
        backDialog = backDialogBuilder.create();
        backDialogBuilder
                .setTitle(R.string.dialog_discard_draft)
                .setMessage(R.string.dialog_discard_drink_message)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        backDialog.dismiss();
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
     * Creates and persists a DrinkFromShop object
     */
    public void createShopDrink() {
        DrinkFromShop drink = new DrinkFromShop();
        CoffeeDatabase db = CoffeeDatabase.getDatabase(AddDrink.this.getApplicationContext());
        if(db.shopDrinkDao().getShopDrinksCount() > 0) {
            idCounterS = db.shopDrinkDao().getBiggestShopDrinkId();
        }
        else {
            idCounterS = 0;
        }
        drink.setId(DrinkFromShop.nextId());
        drink.setDateAdded(new Date());
        drink.setDrinkName(txtDrinkNameS.getText().toString());
        if(!txtPriceS.getText().toString().isEmpty()) {
            drink.setPrice(Float.parseFloat(txtPriceS.getText().toString()));
            drink.setCurrency(currencySpinnerS.getSelectedItem().toString());
        }
        drink.setSize(sizeSpinnerS.getSelectedItem().toString());
        drink.setRating(ratingS.getRating());
        drink.setShopName(txtShopNameS.getText().toString());
        drink.setShopAddress(txtShopAddressS.getText().toString());
        drink.setDrinkNotes(txtNotesS.getText().toString());
        try{
            if (photoSource == FROM_GALLERY) {
                drink.setDrinkPhoto(getBitmapFromUri(imageUri));
            } else if (photoSource == FROM_CAMERA) {
                drink.setDrinkPhoto(photo);
            }
        } catch (IOException ex) {
            Log.e(this.getClass().toString(), "Couldn't save image");
        }
        drinksList.add(drink);

        ShopDrinkDB drinkDB = new ShopDrinkDB();
        drinkDB.drinkId = drink.getId();
        drinkDB.dateAdded = drink.getDateAdded();
        drinkDB.drinkName = drink.getDrinkName();
        drinkDB.price = drink.getPrice();
        drinkDB.currency = drink.getCurrency();
        drinkDB.size = drink.getSize();
        drinkDB.rating = drink.getRating();
        drinkDB.shopName = drink.getShopName();
        drinkDB.shopAddress = drink.getShopAddress();
        drinkDB.drinkNotes = drink.getDrinkNotes();
        if(drink.getDrinkPhoto() != null) {
            drinkDB.drinkPhoto = drink.getDrinkPhoto();
        }
        else {
            drinkDB.drinkPhoto = null;
        }
        shopDrinksDB.add(drinkDB);
        db.shopDrinkDao().insertShopDrink(drinkDB);
        Toast.makeText(this, "Drink " + drink.getDrinkName() + " saved.", Toast.LENGTH_SHORT).show();
    }

    private void createRecipeDrink() {
        DrinkFromRecipe drink = new DrinkFromRecipe();
        CoffeeDatabase db = CoffeeDatabase.getDatabase(AddDrink.this.getApplicationContext());
        if(db.recipeDrinkDao().getRecipeDrinksCount() > 0) {
            idCounterR = db.recipeDrinkDao().getBiggestRecipeDrinkId();
        }
        else {
            idCounterR = 0;
        }
        drink.setId(DrinkFromRecipe.nextId());
        drink.setDateAdded(new Date());
        drink.setDrinkName(txtDrinkNameR.getText().toString());
        Recipe recipeUsed = (Recipe)recipeSpinnerR.getSelectedItem();
        drink.setRecipeUsed(recipeUsed);
        drink.setRecipeName(recipeUsed.getName());
        drink.setBeansUsed(recipeUsed.getBeansUsed().getName() + ", " + recipeUsed.getBeansUsed().getRoaster());
        drink.setAmountOfCoffeeUsed(recipeUsed.getAmountOfCoffee());
        drink.setPrepMethodUsed(recipeUsed.getMethodOfBrewing());
        drink.setExtrasUsed(recipeUsed.getMilk() + "\n" + recipeUsed.getSyrup() + "\n" + recipeUsed.getSugar());
        try{
            if (photoSource == FROM_GALLERY) {
                drink.setDrinkPhoto(getBitmapFromUri(imageUri));
            } else if (photoSource == FROM_CAMERA) {
                drink.setDrinkPhoto(photo);
            }
        } catch (IOException ex) {
            Log.e(this.getClass().toString(), "Couldn't save image");
        }
        drinksList.add(drink);

        RecipeDrinkDB drinkDB = new RecipeDrinkDB();
        drinkDB.drinkId = drink.getId();
        drinkDB.dateAdded = drink.getDateAdded();
        drinkDB.drinkName = drink.getDrinkName();
        drinkDB.recipeUsedId = drink.getRecipeUsed().getId();
        drinkDB.recipeName = drink.getRecipeName();
        drinkDB.beansUsed = drink.getBeansUsed();
        drinkDB.amountOfCoffeeUsed = drink.getAmountOfCoffeeUsed();
        drinkDB.prepMethodUsed = drink.getPrepMethodUsed();
        drinkDB.extrasUsed = drink.getExtrasUsed();
        if(drink.getDrinkPhoto() != null) {
            drinkDB.drinkPhoto = drink.getDrinkPhoto();
        }
        else {
            drinkDB.drinkPhoto = null;
        }
        recipeDrinksDB.add(drinkDB);
        db.recipeDrinkDao().insertRecipeDrink(drinkDB);
        Toast.makeText(this, "Drink " + drink.getDrinkName() + " saved.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Create a Bitmap from the supplied URI
     * @param uri   URI of an image
     * @return      Bitmap for an image
     * @throws IOException
     */
    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * Handles results from intents launched - gallery & camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                Uri imageChosen = data.getData();
                addPhotoButton.setImageURI(imageChosen);
                imageUri = imageChosen;
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                if (!data.getExtras().isEmpty()) {
                    photo = (Bitmap) data.getExtras().get("data");
                    addPhotoButton.setImageBitmap(photo);
                }
            }
        }
    }

    /**
     * check if Camera or gallery access permission is granted, if so, open the camera/gallery
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                photoSource = FROM_CAMERA;
            } else {
                Toast.makeText(this, "Access to camera denied. Allow it from settings.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                photoSource = FROM_GALLERY;
            } else {
                Toast.makeText(this, "Access to gallery denied. Allow it from settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        recipeArrayAdapter.notifyDataSetChanged();
        super.onResume();
    }
}