package com.example.peter.thekitchenmenu.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.AppExecutors;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelProduct;
import com.example.tkmapplibrary.dataValidation.InputValidation;

public class ActivityDetailProduct
        extends AppCompatActivity {

    public static final String LOG_TAG = ActivityDetailProduct.class.getSimpleName();

    // Product object instance
    private Product
            mProduct;

    // String member variables for the product fields
    private String
            mDescription,
            mRetailer,
            mLocationRoom,
            mLocationInRoom;

    // Integer member variables for the product fields
    private int
            mProductId = Constants.DEFAULT_PRODUCT_ID,
            mUnitOfMeasure,
            mPackSize,
            mShelfLife,
            mCategory;

    // Double member variables for the product fields
    private double mPackPrice;

    // Fields for the EditText views in activity_detail_product
    private EditText
            mDescriptionET,
            mRetailerET,
            mPackSizeET,
            mLocationRoomET,
            mLocationInRoomET,
            mPackPriceET;

    // Spinners for the fields in activity_detail_product
    private Spinner
            mUoMSpinner,
            mShelfLifeSpinner,
            mCategorySpinner;

    // Member variable for the database
    private TKMDatabase mDb;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        /* Construct a default product for field value comparison and validation */
        mProduct = new Product(
                Constants.DEFAULT_PRODUCT_DESCRIPTION,
                Constants.DEFAULT_PRODUCT_RETAILER,
                Constants.DEFAULT_PRODUCT_UOM,
                Constants.DEFAULT_PRODUCT_PACK_SIZE,
                Constants.DEFAULT_PRODUCT_SHELF_LIFE,
                Constants.DEFAULT_PRODUCT_LOC,
                Constants.DEFAULT_PRODUCT_LOC_IN_ROOM,
                Constants.DEFAULT_PRODUCT_CATEGORY,
                Constants.DEFAULT_PRODUCT_PRICE);

        mDb = TKMDatabase.getInstance(getApplicationContext());

        /* Setup the views */
        initialiseViews();

        /* If it exists get the current product from saved instance state */
        if(savedInstanceState != null && savedInstanceState.containsKey(
                Constants.PRODUCT_KEY)) {

            mProduct = savedInstanceState.getParcelable(
                    Constants.PRODUCT_KEY);

            // Todo - set the default product ID if mProduct ID null or -1 at this point
            mProductId = mProduct.getProductId();
        }

        /* If there is a product ID passed with the intent this is an existing product */
        Intent intent = getIntent();
        if(intent !=null && intent.hasExtra(Constants.PRODUCT_ID)) {

            // This intent has been passed a product ID, so the product is being updated.
            setTitle(getString(R.string.activity_detail_product_title_update));

            if(mProductId == Constants.DEFAULT_PRODUCT_ID) {
                // Set the product id from the incoming intent */
                mProductId = intent.getIntExtra(
                        Constants.PRODUCT_ID, Constants.DEFAULT_PRODUCT_ID);

                setupProductViewModel();
            }
        } else {
            /* If there is no product ID passed in the intent then this is a new product */
            setTitle(getString(R.string.activity_detail_product_title_add_new));
        }
    }

    /* View model for the product */
    private void setupProductViewModel() {

        ViewModelFactoryProduct factory = new ViewModelFactoryProduct(mDb, mProductId);
        final ViewModelProduct viewModelProduct = ViewModelProviders
                .of(this, factory)
                .get(ViewModelProduct.class);

        // Observe
        viewModelProduct
                .getProduct()
                .observe(this, new Observer<Product>() {
                    @Override
                    public void onChanged(@Nullable Product product) {
                        viewModelProduct.getProduct()
                                .removeObserver(this);
                        mProduct = product;
                        populateUi(mProduct);
            }
        });
    }

    private void saveProduct() {
        // Todo Implement on back pressed save changes and touch listener

        /* Read, trim and validate all input fields */

        // Product description
        mDescription = mDescriptionET.getText().toString().trim();
        int validateDescription = InputValidation.validateProductDescription(mDescription);

        if (validateDescription != 0) {
            mDescriptionET.requestFocus();

            switch (validateDescription) {
                case 1:
                    mDescriptionET.setError(getResources().getString(
                            R.string.input_error_product_description_too_short));
                    break;

                case 2:
                    mDescriptionET.setError(getResources().getString(
                            R.string.input_error_product_description_too_long));
                    break;
            }
            return;
        }

        // Retailer
        mRetailer = mRetailerET.getText().toString().trim();
        int validateRetailer = InputValidation.validateRetailer(mRetailer);

        if (validateRetailer != 0) {
            mRetailerET.requestFocus();

            switch (validateRetailer) {
                case 1:
                    mRetailerET.setError(getResources().getString(
                            R.string.input_error_product_retailer_too_short));
                    break;

                case 2:
                    mRetailerET.setError(getResources().getString(
                            R.string.input_error_product_retailer_too_long));
                    break;
            }
            return;
        }

        // Pack size
        mPackSize = Integer.parseInt(mPackSizeET.getText().toString().trim());
        boolean validatePackSize = InputValidation.validatePackSize(mPackSize);

        if(!validatePackSize) {
            mPackSizeET.requestFocus();
            mPackSizeET.setError(getResources().getString(
                    R.string.input_error_pack_size));
            return;
        }

        // Location room
        mLocationRoom = mLocationRoomET.getText().toString().trim();
        int validateLocRoom = InputValidation.validateLocRoom(mLocationRoom);

        if (validateLocRoom != 0) {
            mLocationRoomET.requestFocus();

            switch (validateLocRoom) {
                case 1:
                    mLocationRoomET.setError(getResources().getString(
                            R.string.input_error_product_loc_room_too_short));
                    break;

                case 2:
                    mLocationRoomET.setError(getResources().getString(
                            R.string.input_error_product_loc_room_too_long));
                    break;
            }
            return;
        }

        // Location in room
        mLocationInRoom = mLocationInRoomET.getText().toString().trim();
        int validateLocInRoom = InputValidation.validateLocInRoom(mLocationInRoom);

        if (validateLocInRoom != 0) {
            mLocationInRoomET.requestFocus();

            switch (validateLocInRoom) {
                case 1:
                    mLocationInRoomET.setError(getResources().getString(
                            R.string.input_error_product_loc_in_room_too_short));
                    break;

                case 2:
                    mLocationInRoomET.setError(getResources().getString(
                            R.string.input_error_product_loc_in_room_too_long));
                    break;
            }
            return;
        }

        // Pack price
        // TODO - Implement currency, no validation required I don't think...
        String unformattedPrice = mPackPriceET.getText().toString().trim();

        if (unformattedPrice.length()>0) {
            mPackPrice = Double.parseDouble(unformattedPrice);
        } else {
            mPackPrice = 0.0;
        }

        // Check to see if this is a new product and if the fields are blank.
        if (mProductId ==-1
                && TextUtils.isEmpty(mDescription)
                && TextUtils.isEmpty(mRetailer)
                && mUnitOfMeasure == 0
                && mPackSize == 0
                && mShelfLife == 0
                && TextUtils.isEmpty(mLocationRoom)
                && TextUtils.isEmpty(mLocationInRoom)
                && mCategory == 0
                && mPackPrice == 0) {
            // As no fields have been modified we can safely return without performing any actions
            return;
        }

        // Update the default product by adding the validated input fields
        mProduct.setDescription(mDescription);
        mProduct.setRetailer(mRetailer);
        mProduct.setUnitOfMeasure(mUnitOfMeasure);
        mProduct.setPackSize(mPackSize);
        mProduct.setShelfLife(mShelfLife);
        mProduct.setLocationRoom(mLocationRoom);
        mProduct.setLocationInRoom(mLocationInRoom);
        mProduct.setCategory(mCategory);
        mProduct.setPackPrice(mPackPrice);

        // Make the product information final
        final Product product = mProduct;

        AppExecutors.getInstance().diskIO().execute(() -> {
            // Insert the product
            if (mProductId == -1) { // Insert new.
                mDb.getProductDao().insertProduct(product);
                finish();
            } else { // Update existing.
                mDb.getProductDao().updateProduct(product);
                finish();
            }
        });
    }

    /**
     * This method is called when updating a products information.
     * @param product - The product whose ID was passed to this activity by the intent.
     */
    private void populateUi(Product product) {

        /* Update EditText fields */
        mDescriptionET.setText(product.getDescription());
        mRetailerET.setText(product.getRetailer());
        mPackSizeET.setText(String.valueOf(product.getPackSize()));
        mUoMSpinner.setSelection(product.getUnitOfMeasure());
        mShelfLifeSpinner.setSelection(product.getShelfLife());
        mLocationRoomET.setText(product.getLocationRoom());
        mLocationInRoomET.setText(product.getLocationInRoom());
        mCategorySpinner.setSelection(product.getCategory());
        mPackPriceET.setText(String.valueOf(product.getPackPrice()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        /* Save the current product */
        outState.putParcelable(Constants.PRODUCT_KEY, mProduct);
    }

    private void initialiseViews() {

        // EditText fields
        mDescriptionET = findViewById(R.id.activity_detail_product_et_description);
        mRetailerET = findViewById(R.id.activity_detail_product_et_retailer);
        mPackSizeET = findViewById(R.id.activity_detail_product_et_pack_size);
        mLocationRoomET = findViewById(R.id.activity_detail_product_et_location_room);
        mLocationInRoomET = findViewById(R.id.activity_detail_product_et_location_in_room);
        mPackPriceET = findViewById(R.id.activity_detail_product_et_price);

        // Spinner fields
        mUoMSpinner = findViewById(R.id.activity_detail_product_spinner_UoM);
        mShelfLifeSpinner = findViewById(R.id.activity_detail_product_spinner_shelf_life);
        mCategorySpinner = findViewById(R.id.activity_detail_product_spinner_category);

        setupUoMSpinner();
        setupShelfLifeSpinner();
        setupCategorySpinner();
    }

    /*
     * Setup the dropdown spinner that allows the user to select the unit of measure for a product.
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
                        mUnitOfMeasure = getResources().getInteger(R.integer.item_not_selected);

                    } else if (selection.equals(getString(R.string.uom_option_1))) {
                        mUnitOfMeasure = getResources().getInteger(R.integer.uom_grams_int);

                    } else if (selection.equals(getString(R.string.uom_option_2))) {
                        mUnitOfMeasure = getResources().getInteger(R.integer.uom_milliliter_int);

                    } else if (selection.equals(getString(R.string.uom_option_3))) {
                        mUnitOfMeasure = getResources().getInteger(R.integer.uom_count_int);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUnitOfMeasure = getResources().getInteger(R.integer.item_not_selected);
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

                // TODO - Turn this into a loop
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.shelf_life_option_0))) {
                        mShelfLife = getResources().getInteger(R.integer.item_not_selected);

                    } else if (selection.equals(getString(R.string.shelf_life_option_1))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_1);

                    } else if (selection.equals(getString(R.string.shelf_life_option_2))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_2);

                    } else if (selection.equals(getString(R.string.shelf_life_option_3))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_3);

                    } else if (selection.equals(getString(R.string.shelf_life_option_4))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_4);

                    } else if (selection.equals(getString(R.string.shelf_life_option_5))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_5);

                    } else if (selection.equals(getString(R.string.shelf_life_option_6))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_6);

                    } else if (selection.equals(getString(R.string.shelf_life_option_7))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_7);

                    } else if (selection.equals(getString(R.string.shelf_life_option_8))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_8);

                    } else if (selection.equals(getString(R.string.shelf_life_option_9))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_9);

                    } else if (selection.equals(getString(R.string.shelf_life_option_10))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_10);

                    } else if (selection.equals(getString(R.string.shelf_life_option_11))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_11);

                    } else if (selection.equals(getString(R.string.shelf_life_option_12))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_12);

                    } else if (selection.equals(getString(R.string.shelf_life_option_13))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_13);

                    } else if (selection.equals(getString(R.string.shelf_life_option_14))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_14);

                    } else if (selection.equals(getString(R.string.shelf_life_option_15))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_15);

                    } else if (selection.equals(getString(R.string.shelf_life_option_16))) {
                        mShelfLife = getResources().getInteger(R.integer.shelf_life_option_16);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mShelfLife = getResources().getInteger(R.integer.item_not_selected);
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
                R.array.array_product_category_options, android.R.layout.simple_spinner_item);

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
                    if (selection.equals(getString(R.string.product_category_option_0))) {
                        mCategory = getResources().getInteger(R.integer.item_not_selected);

                    } else if (selection.equals(getString(R.string.product_category_option_1))) {
                        mCategory = getResources().getInteger(R.integer.category_option_1);

                    } else if (selection.equals(getString(R.string.product_category_option_2))) {
                        mCategory = getResources().getInteger(R.integer.category_option_2);
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = R.string.product_category_option_0;
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
        if (mProductId == -1) {
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
                // Save product and exit activity
                saveProduct();
                break;
            case R.id.menu_product_editor_action_delete:
                // Delete product
                deleteProduct();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Delete the product */
    private void deleteProduct(){
        AppExecutors
                .getInstance()
                .diskIO()
                .execute(() -> mDb.getProductDao()
                        .deleteProduct(mProduct));
        finish();
    }
}
