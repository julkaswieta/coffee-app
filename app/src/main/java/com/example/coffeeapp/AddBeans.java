package com.example.coffeeapp;

import static com.example.coffeeapp.Bean.idCounter;
import static com.example.coffeeapp.BeansList.beansFromDB;
import static com.example.coffeeapp.BeansList.beansList;
import static com.example.coffeeapp.BeansRecViewAdapter.BEANS_ID_KEY;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.coffeeapp.db.BeanDB;
import com.example.coffeeapp.db.CoffeeDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AddBeans extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 5;
    private static final int CAMERA_REQUEST_CODE = 6;
    private static final int CAMERA_PERMISSION_CODE = 7;
    private static final int GALLERY_PERMISSION_CODE = 8;
    private static final int FROM_GALLERY = 100;
    private static final int FROM_CAMERA = 101;
    private static final String BLEND = "Beans of blended origin";
    private static final String SINGLE_ORIGIN = "Single origin beans";
    private static final String UNKNOWN_ORIGIN = "Beans origin unknown";

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
    private RadioButton chosenOriginOption;
    private RadioButton blendOption;
    private RadioButton singleOriginOption;
    private RadioButton unknownOriginOption;
    private EditText txtShopUrl;
    private EditText txtPrice;
    private Spinner currencySpinner;
    private RatingBar ratingBar;
    private EditText txtNotes;
    private Bean incomingBean;
    ArrayList<String> currencies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beans);

        initViews();
        checkAndLoadEditData();
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
        blendOption = findViewById(R.id.ab_blend_button);
        singleOriginOption = findViewById(R.id.ab_single_origin_button);
        unknownOriginOption = findViewById(R.id.ab_blend_origin_unknown);

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Add new beans");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // fill the spinner with currencies
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
                if (checkIsFlavoured.isChecked()) {
                    txtFlavour.setEnabled(true);
                } else {
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
                if (txtBeansName.getText().toString().isEmpty() || txtRoasterName.getText().toString().isEmpty()) {
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
                } else {
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
                                if (ContextCompat.checkSelfPermission(AddBeans.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openCamera, CAMERA_REQUEST_CODE);
                                    photoSource = FROM_CAMERA;
                                } else {
                                    ActivityCompat.requestPermissions(AddBeans.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                                }

                            }
                        })
                        .setPositiveButton("Open gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // launches the gallery
                                if (ContextCompat.checkSelfPermission(AddBeans.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
                                    photoSource = FROM_GALLERY;
                                } else {
                                    ActivityCompat.requestPermissions(AddBeans.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                                }


                            }
                        }).show();
            }
        });
    }

    /**
     * Loads beans data if it's for editing
     */
    private void checkAndLoadEditData() {
        // check if the activity is open to add or edit
        Intent incomingBeans = getIntent();
        if (incomingBeans != null) {
            int beanId = incomingBeans.getIntExtra(BEANS_ID_KEY, -1);
            if (beanId != -1) {
                for (Bean b : beansList) {
                    if (b.getId() == beanId) {
                        incomingBean = b;
                        toolbarTitle.setText("Edit beans");
                        break;
                    }
                }
            }
        }
        // load the data
        if (incomingBean != null) {
            txtBeansName.setText(incomingBean.getName());
            txtRoasterName.setText(incomingBean.getRoaster());
            if (incomingBean.getDegreeOfRoast() != 0) {
                sliderDegreeOfRoast.setValue(incomingBean.getDegreeOfRoast());
            }
            checkIsDecaf.setChecked(incomingBean.isDecaf());
            checkIsFlavoured.setChecked(incomingBean.isFlavoured());
            if (!incomingBean.getFlavour().isEmpty()) {
                txtFlavour.setText(incomingBean.getFlavour());
            }
            if (incomingBean.isBlend().equals(BLEND)) {
                blendOption.setChecked(true);
            } else if (incomingBean.isBlend().equals(SINGLE_ORIGIN)) {
                singleOriginOption.setChecked(true);
            } else if (incomingBean.isBlend().equals(UNKNOWN_ORIGIN)) {
                unknownOriginOption.setChecked(true);
            }
            if (!incomingBean.getUrlToShop().isEmpty()) {
                txtShopUrl.setText(incomingBean.getUrlToShop());
            }
            if (incomingBean.getCostPerKg() != 0) {
                txtPrice.setText(String.valueOf(incomingBean.getCostPerKg()));
                // set currency
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).equals(incomingBean.getCurrency())) {
                        currencySpinner.setSelection(i);
                    }
                }
            }
            if (incomingBean.getRating() != 0) {
                ratingBar.setRating(incomingBean.getRating());
            }
            if (!incomingBean.getNotes().isEmpty()) {
                txtNotes.setText(incomingBean.getNotes());
            }
            if(incomingBean.getPhoto() != null) {
                addPhotoButton.setImageBitmap(incomingBean.getPhoto());
            }
        }
    }

    /**
     * Creates a Beans object and persists it in the DB
     */
    private void createBeans() {
        // update if it's update, else create a new one
        if (incomingBean != null) {
            incomingBean.setName(txtBeansName.getText().toString());
            incomingBean.setRoaster(txtRoasterName.getText().toString());
            incomingBean.setDegreeOfRoast((int) sliderDegreeOfRoast.getValue());
            incomingBean.setDecaf(checkIsDecaf.isChecked());
            incomingBean.setFlavoured(checkIsFlavoured.isChecked());
            incomingBean.setFlavour(txtFlavour.getText().toString());
            int blendOrSingle = blendOriginGroup.getCheckedRadioButtonId();
            chosenOriginOption = findViewById(blendOrSingle);
            if (chosenOriginOption.getId() == R.id.ab_blend_button) {
                incomingBean.setBlend(BLEND);
            } else if (chosenOriginOption.getId() == R.id.ab_single_origin_button) {
                incomingBean.setBlend(SINGLE_ORIGIN);
            } else {
                incomingBean.setBlend(UNKNOWN_ORIGIN);
            }
            incomingBean.setUrlToShop(txtShopUrl.getText().toString());
            if (!txtPrice.getText().toString().isEmpty()) {
                incomingBean.setCostPerKg(Float.parseFloat(txtPrice.getText().toString()));
            }
            incomingBean.setCurrency(currencySpinner.getSelectedItem().toString());
            incomingBean.setRating(ratingBar.getRating());
            incomingBean.setNotes(txtNotes.getText().toString());
            try{
                if (photoSource == FROM_GALLERY) {
                    incomingBean.setPhoto(getBitmapFromUri(imageUri));
                } else if (photoSource == FROM_CAMERA) {
                    incomingBean.setPhoto(photo);
                }
            } catch (IOException ex) {
                Log.e(this.getClass().toString(), "Couldn't save image");
            }

            CoffeeDatabase db = CoffeeDatabase.getDatabase(AddBeans.this.getApplicationContext());
            BeanDB beanDB = db.beanDao().getBeansById(incomingBean.getId());
            beanDB.name = incomingBean.getName();
            beanDB.dateAdded = incomingBean.getDateAdded();
            beanDB.roaster = incomingBean.getRoaster();
            beanDB.degreeOfRoast = incomingBean.getDegreeOfRoast();
            beanDB.isDecaf = incomingBean.isDecaf();
            beanDB.isFlavoured = incomingBean.isFlavoured();
            beanDB.flavour = incomingBean.getFlavour();
            beanDB.isBlend = incomingBean.isBlend();
            beanDB.urlToShop = incomingBean.getUrlToShop();
            beanDB.costPerKg = incomingBean.getCostPerKg();
            beanDB.currency = incomingBean.getCurrency();
            beanDB.rating = incomingBean.getRating();
            beanDB.notes = incomingBean.getNotes();
            if(incomingBean.getPhoto() != null) {
                beanDB.image = incomingBean.getPhoto();
            }
            else {
                beanDB.image = null;
            }
            db.beanDao().updateBean(beanDB);

        } else {
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
            bean.setDegreeOfRoast((int) sliderDegreeOfRoast.getValue());
            bean.setDecaf(checkIsDecaf.isChecked());
            bean.setFlavoured(checkIsFlavoured.isChecked());
            bean.setFlavour(txtFlavour.getText().toString());
            int blendOrSingle = blendOriginGroup.getCheckedRadioButtonId();
            if (blendOrSingle == R.id.ab_blend_button) {
                bean.setBlend(BLEND);
            } else if (blendOrSingle == R.id.ab_single_origin_button) {
                bean.setBlend(SINGLE_ORIGIN);
            } else {
                bean.setBlend(UNKNOWN_ORIGIN);
            }
            bean.setUrlToShop(txtShopUrl.getText().toString());
            if (!txtPrice.getText().toString().isEmpty()) {
                bean.setCostPerKg(Float.parseFloat(txtPrice.getText().toString()));
            }
            bean.setCurrency(currencySpinner.getSelectedItem().toString());
            bean.setRating(ratingBar.getRating());
            bean.setNotes(txtNotes.getText().toString());
            try{
                if (photoSource == FROM_GALLERY) {
                    bean.setPhoto(getBitmapFromUri(imageUri));
                } else if (photoSource == FROM_CAMERA) {
                    bean.setPhoto(photo);
                }
            } catch (IOException ex) {
                Log.e(this.getClass().toString(), "Couldn't save image");
            }
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
            if(bean.getPhoto() != null) {
                beanDB.image = bean.getPhoto();
            }
            else {
                beanDB.image = null;
            }
            beansFromDB.add(beanDB);
            db.beanDao().insertBean(beanDB);
            Toast.makeText(this, "Beans " + bean.getName() + " saved.", Toast.LENGTH_SHORT).show();
        }
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
                .setMessage(R.string.dialog_discard_beans_message)
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
}