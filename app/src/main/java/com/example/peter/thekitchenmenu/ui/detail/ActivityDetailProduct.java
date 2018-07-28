package com.example.peter.thekitchenmenu.ui.detail;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.AppExecutors;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.utils.BitmapUtils;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelProduct;
import com.example.tkmapplibrary.dataValidation.InputValidation;

import java.io.File;
import java.io.IOException;


public class ActivityDetailProduct
        extends AppCompatActivity {

    public static final String LOG_TAG = ActivityDetailProduct.class.getSimpleName();

    // Member variable for the database
    private TKMDatabase mDb;

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

    // Image field
    private ImageView mProductIV;

    // Add product image button
    private ImageButton mAddImageIB;

    // The temporary path on the device where the full size image is held for processing
    private String mTempImagePath;

    // The re-sampled image we set to the ImageView
    private Bitmap mResultsBitmap;

    // The permanent public URI for the image
    private Uri mProductImageUri;


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
                Constants.DEFAULT_PRODUCT_PRICE,
                Constants.DEFAULT_LOCAL_IMAGE_URI);

        // Set the default value of the product image member variable
        mProductImageUri = mProduct.getLocalImageUri();

        // Instantiate the database
        mDb = TKMDatabase.getInstance(getApplicationContext());

        /* Setup the views */
        initialiseViews();

        /* If it exists get the current product from saved instance state */
        if (savedInstanceState != null && savedInstanceState.containsKey(
                Constants.PRODUCT_KEY)) {

            mProduct = savedInstanceState.getParcelable(
                    Constants.PRODUCT_KEY);

            mProductId = mProduct.getProductId();
        }

        /* If there is a product ID passed with the intent this is an existing product */
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.PRODUCT_ID)) {

            // This intent has been passed a product ID, so the product is being updated.
            setTitle(getString(R.string.activity_detail_product_title_update));

            if (mProductId == Constants.DEFAULT_PRODUCT_ID) {
                // Set the product id from the incoming intent */
                mProductId = intent.getIntExtra(
                        Constants.PRODUCT_ID, Constants.DEFAULT_PRODUCT_ID);

                setupProductViewModel();
            }
        } else {
            /* If there is no product ID passed in the intent then this is a new product */
            setTitle(getString(R.string.activity_detail_product_title_add_new));
        }

        /* OnClickListener for the add picture by camera button */
        mAddImageIB.setOnClickListener(v -> {
            requestPermissions();
        });
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

                        // Update member variables
                        mProduct = product;
                        // Update the screen
                        populateUi();
                    }
                });
    }

    /* If available loads the product image */
    private void loadImage() {
        // Todo - what if image has been moved or deleted?
        // Retrieve the image from public storage
        mResultsBitmap = BitmapUtils.resampleImage(this, mProductImageUri, null);
        mProductIV.setImageBitmap(mResultsBitmap);
        Log.e(LOG_TAG, "loadImage() - Getting image");

    }

    private void saveProduct() {
        // Todo Implement on up pressed save changes and touch listener

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

        if (!validatePackSize) {
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

        if (unformattedPrice.length() > 0) {
            mPackPrice = Double.parseDouble(unformattedPrice);
        } else {
            mPackPrice = 0.0;
        }

        // Check to see if this is a new product and if the fields are blank.
        if (mProductId == -1
                && TextUtils.isEmpty(mDescription)
                && TextUtils.isEmpty(mRetailer)
                && mUnitOfMeasure == 0
                && mPackSize == 0
                && mShelfLife == 0
                && TextUtils.isEmpty(mLocationRoom)
                && TextUtils.isEmpty(mLocationInRoom)
                && mCategory == 0
                && mPackPrice == 0) {
            // Todo - add Uri field
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
        mProduct.setLocalImageUri(mProductImageUri);

        // Make the product information final
        final Product product = mProduct;

        // Insert or update the product
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

    /* This method is called when updating a products information. */
    private void populateUi() {

        /* Update EditText fields */
        mDescriptionET.setText(mProduct.getDescription());
        mRetailerET.setText(mProduct.getRetailer());
        mPackSizeET.setText(String.valueOf(mProduct.getPackSize()));
        mUoMSpinner.setSelection(mProduct.getUnitOfMeasure());
        mShelfLifeSpinner.setSelection(mProduct.getShelfLife());
        mLocationRoomET.setText(mProduct.getLocationRoom());
        mLocationInRoomET.setText(mProduct.getLocationInRoom());
        mCategorySpinner.setSelection(mProduct.getCategory());
        mPackPriceET.setText(String.valueOf(mProduct.getPackPrice()));

        // Only load image if the Uri is not the default value (EMPTY)
        if (!Uri.EMPTY.equals(mProduct.getLocalImageUri())) {
            mProductImageUri = mProduct.getLocalImageUri();
            loadImage();
        }
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

        // Image button
        mAddImageIB = findViewById(R.id.activity_detail_product_ib_add_picture);

        // Image field
        mProductIV = findViewById(R.id.activity_detail_product_iv);

        setupUoMSpinner();
        setupShelfLifeSpinner();
        setupCategorySpinner();
    }


    /* Delete the product */
    private void deleteProduct() {
        AppExecutors
                .getInstance()
                .diskIO()
                .execute(() -> mDb.getProductDao()
                        .deleteProduct(mProduct));
        finish();
    }

    /* Image capture - Launches an intent to take a picture */
    private void takePictureIntent() {

        // Attribution: https://developer.android.com/training/camera/photobasics
        // Also used in Udacity Advanced Android Emojify
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createImageFile(this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempImagePath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(this,
                        Constants.FILE_PROVIDER_AUTHORITY, photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            processAndSetImage();
        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE && resultCode == RESULT_OK) {
            processAndSetImage();
        } else {
            // Delete the temporary file
            BitmapUtils.deleteImageFile(this, mTempImagePath);
        }
    }

    /* Resample's the image so it fits our imageView and uses less resources */
    private void processAndSetImage() {

        mResultsBitmap = BitmapUtils.resampleImage(this, null, mTempImagePath);
        mProductIV.setImageBitmap(mResultsBitmap);
        addImageToGallery();
    }

    /* Add the photo to the media providers database so it is accessible to all */
    private void addImageToGallery() {
        // Get the full size temporary image file
        File f = new File(mTempImagePath);
        // Move it to a public area and return its content Uri
        MediaScannerConnection.scanFile(
                getApplicationContext(),
                new String[]{f.getAbsolutePath()}, null, (path, uri) -> {
                    if (uri != null) {
                        // Update the products public image Uri
                        mProductImageUri = uri;
                    } else {
                        Log.e(LOG_TAG, "Error moving image from temp to public location");
                    }
                });
    }

    /* Request permissions for access to the file storage area */
    public void requestPermissions() {

        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.REQUEST_STORAGE_PERMISSION);
        } else {
            takePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case Constants.REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    takePictureIntent();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.storage_permission_denied,
                            Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            }
        }
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
}
