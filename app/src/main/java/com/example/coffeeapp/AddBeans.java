package com.example.coffeeapp;

import static com.example.coffeeapp.Bean.idCounter;
import static com.example.coffeeapp.BeansList.beansFromDB;
import static com.example.coffeeapp.BeansList.beansList;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeapp.db.BeanDB;
import com.example.coffeeapp.db.CoffeeDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Date;

public class AddBeans extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 6;
    private static final int CAMERA_PERMISSION_CODE = 7;
    private static final int GALLERY_PERMISSION_CODE = 8;
    private static final int FROM_GALLERY = 100;
    private static final int FROM_CAMERA = 101;

    // toolbar
    private Toolbar toolbar;
    private TextView toolbarTitle;
    // dialogs
    private MaterialAlertDialogBuilder backDialogBuilder;
    private MaterialAlertDialogBuilder photoDialogBuilder;
    private MaterialAlertDialogBuilder saveDialogBuilder;
    private AlertDialog backDialog;
    private AlertDialog photoDialog;
    private AlertDialog saveDialog;
    // bottom buttons
    private Button cancelButton;
    private Button saveButton;
    // photo capture
    private ImageButton addPhotoButton;
    private Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    private int photoSource;
    private Uri imageUri;
    private Bitmap photo;
    // views
    private EditText txtBeansName;
    private EditText txtRoasterName;
    private Slider sliderDegreeOfRoast;
    private CheckBox checkIsDecaf;
    private CheckBox checkIsFlavoured;
    private EditText txtFlavour;
    private RadioGroup blendOriginGroup;
    private RadioButton blendOriginButton;
    private EditText txtShopUrl;
    private EditText txtPrice;
    private Spinner currencySpinner;
    private RatingBar ratingBar;
    private EditText txtNotes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beans);

        initViews();
    }

    /**
     * Initialises the whole layout
     */
    private void initViews() {
        toolbar = findViewById(R.id.ab_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        backDialogBuilder = new MaterialAlertDialogBuilder(this);
        photoDialogBuilder = new MaterialAlertDialogBuilder(this);
        saveDialogBuilder = new MaterialAlertDialogBuilder(this);
        cancelButton = findViewById(R.id.ab_cancel_button);
        saveButton = findViewById(R.id.ab_save_button);
        addPhotoButton = findViewById(R.id.ab_add_photo_btn);
        txtBeansName = findViewById(R.id.ab_beans_name);
        txtRoasterName = findViewById(R.id.ab_roaster_name);
        sliderDegreeOfRoast = findViewById(R.id.ab_roast_degree_slider);
        checkIsDecaf = findViewById(R.id.ab_check_decaf);
        checkIsFlavoured = findViewById(R.id.ab_check_flavour);
        txtFlavour = findViewById(R.id.ab_flavour);
        blendOriginGroup = findViewById(R.id.ab_blend_origin_group);
        txtShopUrl = findViewById(R.id.ab_link_to_store);
        txtPrice = findViewById(R.id.ab_cost);
        currencySpinner = findViewById(R.id.ab_currency_spinner);
        ratingBar = findViewById(R.id.ab_rating_bar);
        txtNotes = findViewById(R.id.ab_notes);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Add new beans");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // fill the spinner with currencies
        ArrayList<String> currencies = new ArrayList<>();
        currencies.add("£");
        currencies.add("€");
        currencies.add("zł");
        ArrayAdapter<String> currencyAdapter =
                new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                currencies);
        currencySpinner.setAdapter(currencyAdapter);

        initListeners();
    }

    /**
     * Defines all buttons' behaviours
     */
    private void initListeners() {
        // enable or disable flavour text box
        checkIsFlavoured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIsFlavoured.isChecked()) {
                    txtFlavour.setEnabled(true);
                }
                else {
                    txtFlavour.setEnabled(false);
                }
            }
        });

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
                if(txtBeansName.getText().toString().isEmpty() || txtRoasterName.getText().toString().isEmpty()) {
                    saveDialog = saveDialogBuilder.create();
                    saveDialogBuilder
                            .setTitle(R.string.dialog_check_title)
                            .setMessage(R.string.dialog_check_mandatory_beans)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    saveDialog.dismiss();
                                }
                            }).show();
                }
                else {
                    createBeans();
                    finish();
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
                                if(ContextCompat.checkSelfPermission(AddBeans.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                                    photoSource = FROM_CAMERA;
                                }
                                else {
                                    ActivityCompat.requestPermissions(AddBeans.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                                }

                            }
                        })
                        .setPositiveButton("Open gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // launches the gallery
                                if(ContextCompat.checkSelfPermission(AddBeans.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                                    photoSource = FROM_GALLERY;
                                }
                                else {
                                    ActivityCompat.requestPermissions(AddBeans.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                                }


                            }
                        }).show();
            }
        });
    }

    /**
     * Creates a Beans object and persists it in the DB
     */
    private void createBeans() {
        // TODO: gather data and create bean object + store in a db
        Bean bean = new Bean();
        // get the biggest latest bean id
        CoffeeDatabase db = CoffeeDatabase.getDatabase(AddBeans.this.getApplicationContext());
        if (db.beanDao().getAllBeans().size() > 0) {
            idCounter = db.beanDao().getBiggestBeansId();
        } else {
            idCounter = 0;
        }
        bean.setId(Bean.nextId());
        bean.setName(txtBeansName.getText().toString());
        bean.setRoaster(txtRoasterName.getText().toString());
        bean.setDateAdded(new Date());
        bean.setDegreeOfRoast((int)sliderDegreeOfRoast.getValue());
        bean.setDecaf(checkIsDecaf.isChecked());
        bean.setFlavoured(checkIsFlavoured.isChecked());
        bean.setFlavour(txtFlavour.getText().toString());
        int blendOrSingle = blendOriginGroup.getCheckedRadioButtonId();
        blendOriginButton = findViewById(blendOrSingle);
        if(blendOriginButton.getId() == R.id.ab_blend_button) {
            bean.setBlend("Beans of blended origin");
        }
        else if(blendOriginButton.getId() == R.id.ab_single_origin_button) {
            bean.setBlend("Single origin beans");
        }
        else {
            bean.setBlend("Beans origin unknown");
        }
        bean.setUrlToShop(txtShopUrl.getText().toString());
        if (!txtPrice.getText().toString().isEmpty()) {
            bean.setCostPerKg(Float.parseFloat(txtPrice.getText().toString()));
        }
        bean.setCurrency(currencySpinner.getSelectedItem().toString());
        bean.setRating(ratingBar.getRating());
        bean.setNotes(txtNotes.getText().toString());
        beansList.add(bean);

        // persist the beans
        BeanDB beanDB = new BeanDB();
        beanDB.beansId = bean.getId();
        beanDB.name = bean.getName();
        beanDB.dateAdded = bean.getDateAdded();
        beanDB.roaster = bean.getRoaster();
        beanDB.degreeOfRoast = bean.getDegreeOfRoast();
        beanDB.isDecaf = bean.isDecaf();
        beanDB.isFlavoured = bean.isFlavoured();
        beanDB.flavour = bean.getFlavour();
        beanDB.isBlend = bean.isBlend();
        beanDB.urlToShop = bean.getUrlToShop();
        beanDB.costPerKg = bean.getCostPerKg();
        beanDB.currency = bean.getCurrency();
        beanDB.rating = bean.getRating();
        beanDB.notes = bean.getNotes();
        beansFromDB.add(beanDB);
        db.beanDao().insertBean(beanDB);
        Toast.makeText(this, "Beans " + bean.getName() + " saved.", Toast.LENGTH_SHORT).show();
    }

    /**
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
                .setMessage(R.string.dialog_discard_message)
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
     * Handles results from intents launched - gallery & camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            if(requestCode == GALLERY_REQUEST_CODE) {
                Uri imageChosen = data.getData();
                addPhotoButton.setImageURI(imageChosen);
                imageUri = imageChosen;
            }
            else if(requestCode == CAMERA_REQUEST_CODE) {
                if(!data.getExtras().isEmpty()) {
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
}