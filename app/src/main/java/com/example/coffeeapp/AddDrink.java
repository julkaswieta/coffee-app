package com.example.coffeeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

public class AddDrink extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private MaterialAlertDialogBuilder backDialogBuilder;
    private AlertDialog backDialog;

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

        // set the toolbar as the action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Add new drink");
        // add the back button to it
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

    public void createDrink() {

    }
}