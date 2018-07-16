package com.example.peter.thekitchenmenu.ui.detail;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.TKMContract;
import com.example.peter.thekitchenmenu.databinding.ActivityProductDetailBinding;

public class ProductDetailActivity
        extends AppCompatActivity {

    // Binding class for this activity
    ActivityProductDetailBinding mProductDetailBinding;
    // Unit of measure
    private int mUoM;
    // Category spinner
    private Spinner mUoMSpinner;
    // Shelf Life
    private int mShelfLife;
    // Shelf life spinner
    private Spinner mShelfLifeSpinner;
    // Category
    private int mCategory;
    // Category spinner
    private Spinner mCategorySpinner;
    // URI for the current product. Null if a new product
    private Uri mCurrentProductUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProductDetailBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_product_detail);

        // TODO - Merge these with the detail binding class
        mUoMSpinner = findViewById(R.id.activity_detail_spinner_UoM);
        mShelfLifeSpinner = findViewById(R.id.activity_product_detail_spinner_shelf_life);
        mCategorySpinner = findViewById(R.id.activity_detail_spinner_category);

        setupUoMSpinner();
        setupShelfLifeSpinner();
        setupCategorySpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the unit of measurefor a product.
     */
    private void setupUoMSpinner() {

        // Create an adapter for the spinner. The list options are from the String array in
        // arrays.xml. The spinner will use the default layout.
        ArrayAdapter UoMSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_UoM_metric_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        UoMSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mUoMSpinner.setAdapter(UoMSpinnerAdapter);

        // Set the integer mUoM to the constant values
        mUoMSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.uom_option_0))) {
                        mUoM = TKMContract.ProductEntry.NOTHING_SELECTED;

                    } else if (selection.equals(getString(R.string.uom_option_1))) {
                        mUoM = TKMContract.ProductEntry.GRAMS;

                    } else if (selection.equals(getString(R.string.uom_option_2))) {
                        mUoM = TKMContract.ProductEntry.MILLILITRES;

                    } else if (selection.equals(getString(R.string.uom_option_3))) {
                        mUoM = TKMContract.ProductEntry.COUNT;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUoM = TKMContract.ProductEntry.NOTHING_SELECTED;
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the shelf life of the product.
     */
    private void setupShelfLifeSpinner() {

        // Create an adapter for the spinner. The list options are from the String array in
        // arrays.xml. The spinner will use the default layout
        ArrayAdapter shelfLifeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_shelf_life_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        shelfLifeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mShelfLifeSpinner.setAdapter(shelfLifeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mShelfLifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.shelf_life_option_0))) {
                        mShelfLife = TKMContract.ProductEntry.NOTHING_SELECTED;

                    } else if (selection.equals(getString(R.string.shelf_life_option_1))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_1_DAY;

                    } else if (selection.equals(getString(R.string.shelf_life_option_2))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_3_DAYS;

                    } else if (selection.equals(getString(R.string.shelf_life_option_3))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_5_DAYS;

                    } else if (selection.equals(getString(R.string.shelf_life_option_4))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_7_DAYS;

                    } else if (selection.equals(getString(R.string.shelf_life_option_5))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_14_DAYS;

                    } else if (selection.equals(getString(R.string.shelf_life_option_6))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_21_DAYS;

                    } else if (selection.equals(getString(R.string.shelf_life_option_7))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_31_DAYS;

                    } else if (selection.equals(getString(R.string.shelf_life_option_8))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_90_DAYS;

                    } else if (selection.equals(getString(R.string.shelf_life_option_9))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;

                    } else if (selection.equals(getString(R.string.shelf_life_option_10))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;

                    } else if (selection.equals(getString(R.string.shelf_life_option_11))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;

                    } else if (selection.equals(getString(R.string.shelf_life_option_12))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;

                    } else if (selection.equals(getString(R.string.shelf_life_option_13))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;

                    } else if (selection.equals(getString(R.string.shelf_life_option_14))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;

                    } else if (selection.equals(getString(R.string.shelf_life_option_15))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;

                    } else if (selection.equals(getString(R.string.shelf_life_option_16))) {
                        mShelfLife = TKMContract.ProductEntry.SHELF_LIFE_LONG_LIFE;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mShelfLife = TKMContract.ProductEntry.NOTHING_SELECTED;
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the category for a product.
     */
    private void setupCategorySpinner() {

        // Create an adapter for the spinner. The list options are from the String array in
        // arrays.xml. The spinner will use the default layout.
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        // Set the integer mUoM to the constant values
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.category_option_0))) {
                        mCategory = TKMContract.ProductEntry.NOTHING_SELECTED;

                    } else if (selection.equals(getString(R.string.category_option_1))) {
                        mCategory = TKMContract.ProductEntry.NON_FOOD;

                    } else if (selection.equals(getString(R.string.category_option_2))) {
                        mCategory = TKMContract.ProductEntry.FOOD_OTHER;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = TKMContract.ProductEntry.NOTHING_SELECTED;
            }
        });
    }

    /* Inflate the menu options from res/menu/menu_product_editor.xml. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_editor, menu);
        return true;
    }

    /* This method is called after invalidateOptionsMenu(), so the menu can be updated (some menu
       items can be hidden or made visible). */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide the "Delete" menu item.
        if ( mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.menu_product_editor_action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    /* This method manages the actions as they are selected from the AppBar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Save (tick) menu option selected
            case R.id.menu_product_editor_action_save:
                // TODO - Save product and exit activity
                // saveProduct;
                // Exit to previous activity finish();


        }



        return super.onOptionsItemSelected(item);
    }
}
