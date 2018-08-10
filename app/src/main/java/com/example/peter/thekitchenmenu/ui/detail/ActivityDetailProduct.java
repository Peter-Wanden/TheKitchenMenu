package com.example.peter.thekitchenmenu.ui.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.AppExecutors;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.utils.BitmapUtils;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.example.tkmapplibrary.dataValidation.InputValidation;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActivityDetailProduct
        extends AppCompatActivity {

    public static final String LOG_TAG = ActivityDetailProduct.class.getSimpleName();

    // Room database instance
    private TKMDatabase
            mDb;

    // Product object instance
    private Product
            mProduct;

    // String member variables for the product fields
    private String
            mDescription,
            mRetailer,
            mLocationRoom,
            mLocationInRoom,
            mMadeBy;

    // Integer member variables for the product fields
    private int
            mProductId = Constants.DEFAULT_PRODUCT_ID,
            mUnitOfMeasure,
            mPackSize,
            mShelfLife,
            mCategory;

    // Double member variables for the product fields
    private double mPackPrice;

    // Boolean member variables for logically switching through class decision tree
    private boolean
            mIsCreator,
            mBaseFieldsAreEditable,
            mUserSpecificFieldsAreEditable,
            mProductInUsedList,
            mPutProductOnUsedList,
            mIsExistingProduct;

    // Containers for groups of fields
    private LinearLayout
            mBaseFieldsEditableContainer,
            mBaseFieldsUnEditableContainer,
            mUserSpecificFieldsEditableContainer,
            mUserSpecificFieldsUneditableContainer;

    // Fields for the EditTexts in activity_detail_product_base_fields_editable
    private EditText
            mDescriptionET,
            mMadeByET,
            mRetailerET,
            mPackSizeET,
            mLocationRoomET,
            mLocationInRoomET,
            mPackPriceET;

    // Fields for the TextViews in activity_detail_product_base_fields_uneditable
    private TextView
            mDescriptionTV,
            mMadeByTV,
            mPackSizeTV,
            mUoMTV,
            mShelfLifeTV,
            mCategoryTV,
            mRetailerTV,
            mLocationRoomTV,
            mLocationInRoomTV,
            mPackPriceTV;

    // Spinners for the fields in activity_detail_product
    private Spinner
            mUoMSpinner,
            mShelfLifeSpinner,
            mCategorySpinner;

    // Menu bar
    private Menu mProductEditorMenu;

    // Image field
    private ImageView mProductIV;

    // Add product image button
    private ImageButton mAddImageIB;

    // FAB
    private FloatingActionButton mFab;

    // The temporary path on the device where the full size image is held for processing
    private String mTempImagePath;

    // The re-sampled image we set to the ImageView
    private Bitmap mResultsBitmap;

    // The permanent public (device local) URI for the local image
    private Uri mProductImageUri;

    // The Firebase Image storage location
    private Uri mFbStorageImageUri;

    /* *******************
     * Firebase database *
     *********************/
    /* Firebase database instance */
    private FirebaseDatabase mFbDatabase;
    /* Firebase database root reference */
    private DatabaseReference mFbDatabaseReference;
    /* Firebase database reference - the entry point for accessing products */
    private DatabaseReference mFbCollectionProduct;
    /* Firebase database reference - the entry point for accessing users */
    private DatabaseReference mFbCollectionUsers;
    /* Firebase database reference - the entry point for accessing used products */
    private DatabaseReference mFbCollectionUsedProducts;
    /* Firebase database */
    private DatabaseReference mProductBaseDocumentRef;
    /* Authentication instance */
    private FirebaseAuth mFBAuth;
    /* Authentication state listener */
    private FirebaseAuth.AuthStateListener mFBAuthStateListener;
    /* Authentication users unique ID generated for this user / app combination */
    private String mUserUid;
    /* Unique Firebase product reference */
    private String mFbProductReferenceKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        assignMemberVariables();
        setupFireBase();
        initialiseFirebaseAuthentication();
        checkSavedInstanceState(savedInstanceState);
        initialiseViews();

        // Get a reference to the Room database instance
        mDb = TKMDatabase.getInstance(getApplicationContext());
    }

    /* Initialise anything here that can only be done when signed in */
    private void onSignedInInitialise(String userUid) {

        // Set the user ID
        mUserUid = userUid;
        Log.e(LOG_TAG, "onSignedInInitialise - user ID is: " + mUserUid);

        /* If there is a product passed with the intent this is an existing product */
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.PRODUCT_FB_REFERENCE_KEY)) {

            // Update the existing product bool
            mIsExistingProduct = true;

            // This intent has been passed the base product fields of a Firebase product
            setScreenTitle(Constants.SCREEN_TITLE_PRODUCT_VIEW);

            // Update the Firebase product reference key with the product passed in
            if (mFbProductReferenceKey.equals(Constants.DEFAULT_FB_PRODUCT_ID)) {

                // Set the incoming product to its member variable
                mProduct = intent.getParcelableExtra(Constants.PRODUCT_FB_REFERENCE_KEY);

                // Set the Firebase product reference ID from the incoming intent */
                mFbProductReferenceKey = mProduct.getFbProductReferenceId();

                // Identify if this user is the creator of this product
                mIsCreator = intent.getBooleanExtra(Constants.PRODUCT_IS_CREATOR_KEY, mIsCreator);

                Log.e(LOG_TAG, "Intent received from CatalogProduct - Firebase product name is: "
                        + mProduct.getDescription());
                Log.e(LOG_TAG, "Intent received from CatalogProduct - Firebase Id is: "
                        + mProduct.getFbProductReferenceId());
                Log.e(LOG_TAG, "Value of mIsCreator is: " + mIsCreator);

                // Set up the views for an existing product
                getUsedList();

            }
        } else {
            /* If there is no reference ID passed in the intent then this is a new product */
            newProductInitialise();
        }
    }

    /* Sets the available titles for this activity */
    private void setScreenTitle(int screenTitle) {
        switch (screenTitle) {
            case 1:
                // Activity is in product 'viewing' mode
                setTitle(getString(R.string.activity_detail_product_title_view));
                break;
            case 2:
                // Activity is in 'edit' mode
                setTitle(getString(R.string.activity_detail_product_title_update));
                break;
            case 3:
                // Activity is in 'add new' mode
                setTitle(getString(R.string.activity_detail_product_title_add_new));
                break;
            default:
                // Default title
                setTitle(getString(R.string.activity_detail_product_title));
        }
    }

    /* Checks to see if there has been a configuration change, if so recovers the data */
    private void checkSavedInstanceState(@Nullable Bundle savedInstanceState) {

        //If it exists get the current product from saved instance state
        if (savedInstanceState != null && savedInstanceState.containsKey(
                Constants.PRODUCT_FB_REFERENCE_KEY)) {

            // Restore the instance of mProduct
            mProduct = savedInstanceState.getParcelable(
                    Constants.PRODUCT_FB_REFERENCE_KEY);

            // Update the product reference
            if (mProduct != null) {
                mFbProductReferenceKey = mProduct.getFbProductReferenceId();
                mDescription = mProduct.getDescription();
                mRetailer = mProduct.getRetailer();
                mLocationRoom = mProduct.getLocationRoom();
                mLocationInRoom = mProduct.getLocationInRoom();
                mMadeBy = mProduct.getMadeBy();
                mUnitOfMeasure = mProduct.getUnitOfMeasure();
                mPackSize = mProduct.getPackSize();
                mShelfLife = mProduct.getShelfLife();
                mCategory = mProduct.getCategory();
                mPackPrice = mProduct.getPackPrice();
            }

            // Update the bool's
            mIsExistingProduct =
                    savedInstanceState.getBoolean(Constants.PRODUCT_IS_EXISTING_KEY);
            mIsCreator =
                    savedInstanceState.getBoolean(Constants.PRODUCT_IS_CREATOR_KEY);
            mProductInUsedList =
                    savedInstanceState.getBoolean(Constants.PRODUCT_ON_USED_LIST_KEY);
            mPutProductOnUsedList =
                    savedInstanceState.getBoolean(Constants.PRODUCT_PUT_ON_USED_LIST);
            mBaseFieldsAreEditable =
                    savedInstanceState.getBoolean(Constants.BASE_FIELDS_EDITABLE_STATUS_KEY);
            mUserSpecificFieldsAreEditable =
                    savedInstanceState.getBoolean(Constants.USER_CUSTOM_FIELDS_EDITABLE_STATUS_KEY);

            // Update the Strings
            mUserUid = savedInstanceState
                    .getString(Constants.USER_ID_KEY);

            mFbProductReferenceKey = savedInstanceState
                    .getString(Constants.PRODUCT_FB_REFERENCE_KEY);

        }
    }

    /* Initialises Firebase authentication and sets the userID */
    private void initialiseFirebaseAuthentication() {

        // Get an instance to Firebase authentication
        mFBAuth = FirebaseAuth.getInstance();

        // Firebase authentication listener (attached in onResume and detached in onPause)
        mFBAuthStateListener = firebaseAuth -> {
            // Find out if the user is logged in or not
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                onSignedInInitialise(user.getUid());
            } else {
                // User is signed out
                onSignedOutCleanUp();
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                                .build(),
                        Constants.REQUEST_CODE_SIGN_IN);
            }
        };
    }

    /* Sets the defaults for member variables */
    private void assignMemberVariables() {

        // Set the default user
        mUserUid = Constants.ANONYMOUS;

        // Set the default value for creator
        mIsCreator = false;

        // Set the default value for in an existing or new product
        mIsExistingProduct = false;

        // Set the default value for whether this product is on the users current used list
        mProductInUsedList = false;

        // Set the default value for whether this product should be added to the users used
        // product list
        mPutProductOnUsedList = false;

        // Set the field focus booleans to false
        mBaseFieldsAreEditable = false;
        mUserSpecificFieldsAreEditable = false;

        /* Construct a default product for field value comparison and validation */
        mProduct = new Product(
                Constants.DEFAULT_PRODUCT_DESCRIPTION,
                Constants.DEFAULT_PRODUCT_MADE_BY,
                Constants.DEFAULT_FB_PRODUCT_ID,
                Constants.DEFAULT_PRODUCT_RETAILER,
                Constants.DEFAULT_PRODUCT_UOM,
                Constants.DEFAULT_PRODUCT_PACK_SIZE,
                Constants.DEFAULT_PRODUCT_SHELF_LIFE,
                Constants.DEFAULT_PRODUCT_LOC,
                Constants.DEFAULT_PRODUCT_LOC_IN_ROOM,
                Constants.DEFAULT_PRODUCT_CATEGORY,
                Constants.DEFAULT_PRODUCT_PRICE,
                Constants.DEFAULT_LOCAL_IMAGE_URI,
                Constants.DEFAULT_FB_IMAGE_STORAGE_URI,
                Constants.PRODUCT_BASE_CREATED_BY_KEY);

        // Set the default value for the local and Firebase Storage Image Uri's
        mProductImageUri = Uri.parse(mProduct.getLocalImageUri());
        mFbStorageImageUri = Uri.parse(mProduct.getFbStorageImageUri());
    }

    /* Sets up Firebase instance and references */
    private void setupFireBase() {

        // Get a reference to the Firebase database
        mFbDatabase = FirebaseDatabase.getInstance();

        // Get a reference to the root of the database
        mFbDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the reference point in the database for collection products
        mFbCollectionProduct = mFbDatabase
                .getReference()
                .child(Constants.FB_COLLECTION_PRODUCTS);

        // Get the reference point in the database for collection users
        mFbCollectionUsers = mFbDatabase
                .getReference()
                .child(Constants.FB_COLLECTION_USERS);

        /* Set the default Firebase used products reference */
        mFbCollectionUsedProducts = mFbDatabase
                .getReference()
                .child(Constants.FB_COLLECTION_USED_PRODUCTS);

        /* Set the default Firebase product collection reference */
        mFbProductReferenceKey = Constants.DEFAULT_FB_PRODUCT_ID;
    }

    /* User has signed out so clean up  */
    // Todo - Is there anything else that need cleaning up?
    private void onSignedOutCleanUp() {
        mUserUid = Constants.ANONYMOUS;
    }

    /* This is a new product being generated */
    private void newProductInitialise() {

        // Set the title
        setScreenTitle(Constants.SCREEN_TITLE_PRODUCT_ADD);

        // Set the appropriate layouts visibility
        mBaseFieldsAreEditable = true;
        updateBaseFieldsEditableStatus();
        mUserSpecificFieldsAreEditable = true;
        updateUserSpecificFieldsEditableStatus();

        // Set the FAB to gone
        mFab.setVisibility(View.GONE);

        // Move the cursor to the first field
        mDescriptionET.requestFocus();

        // Update the bool's
        mIsCreator = true;
        mIsExistingProduct = false;
        mPutProductOnUsedList = true;
    }

    /* If the user is the creator of this product, initialise as follows */
    private void isCreatorInitialise() {

        // This is the creator of the product, set the base fields to editable
        mBaseFieldsAreEditable = true;
        updateBaseFieldsEditableStatus();

        // Check to see if this product is on the users used list
        getUsedList();
    }

    /* If the user is not the creator of this product, initialise as follows */
    private void notCreatorInitialise() {

        // This user cannot edit the base information, as they are not the creator of the product
        // or there is more than one user of the product, so set non-editable views visible
        mBaseFieldsAreEditable = false;
        updateBaseFieldsEditableStatus();

        // Check to see if this product is on the users used list
        getUsedList();
    }

    /* Check to see if the product is in the users user pdocuct list */
    private void getUsedList() {
        // Is the product in the users 'used' list
        DatabaseReference productRef = mFbCollectionUsers
                .child(mUserUid)
                .child(Constants.FB_COLLECTION_PRODUCTS)
                .child(mFbProductReferenceKey);

        Log.e(LOG_TAG, "Database reference is: " + productRef);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If the product is in the users used list it will show up here
                if (dataSnapshot.exists()) {

                    Log.e(LOG_TAG, "getUsedList() - Data snapshot is: " + dataSnapshot);
                    mProduct = dataSnapshot.getValue(Product.class);
                    mProduct.setFbProductReferenceId(dataSnapshot.getKey());
                    Log.e(LOG_TAG, "getUsedList() - snapshot now added to mProduct.");
                    Log.e(LOG_TAG, "getUsedList() - Values of mProducts fields are: "
                            + dataSnapshot);
                    Log.e(LOG_TAG, "getUsedList() - Value of fbProductReferenceID is now: "
                            + mFbProductReferenceKey);
                    Log.e(LOG_TAG, "getUsedList() - Value of mProduct.get(fbProductReferenceID)" +
                            " is now: " + mProduct.getFbProductReferenceId());

                    // This product is in the users used list, so update the used bool
                    mProductInUsedList = true;
                    Log.e(LOG_TAG, "getUsedList() - mProductInUsedList = " + mProductInUsedList);
                    // Make the user specific fields visible
//                    updateUserSpecificFieldsEditableStatus(); Todo - used only in edit mode
                    updateUneditableFields();
                    populateUi();

                } else {
                    // This product is not in the users used list, so update the used bool
                    mProductInUsedList = false;
                    updateUneditableFields();

                    Log.e(LOG_TAG, "getUsedList() - mUserSpecificFieldsAreEditable is: "
                            + mUserSpecificFieldsAreEditable);
//                    updateUserSpecificFieldsEditableStatus();
                    populateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /* If available loads the product image */
    private void loadImage() {
        // Todo - what if image has been moved or deleted? SAVE TO Firebase Storage
        // Retrieve the image from public storage
        mResultsBitmap = BitmapUtils.resampleImage(this, mProductImageUri, null);
        mProductIV.setImageBitmap(mResultsBitmap);
        Log.e(LOG_TAG, "loadImage() - Getting image");

    }

    private void saveProduct() {
        Log.e(LOG_TAG, "saveProduct() - called.");
        Log.e(LOG_TAG, "FbProductRef is: " + mProduct.getFbProductReferenceId());


        // Todo Implement on up pressed save changes and touch listener

        if (mIsCreator && mBaseFieldsEditableContainer.getVisibility() == View.VISIBLE) {

            /* Read, trim and validate all base input fields */
            // Description
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

            // Made by
            mMadeBy = mMadeByET.getText().toString().trim();
            int validateMadeBy = InputValidation.validateMadeBy(mMadeBy);

            if (validateMadeBy != 0) {
                mMadeByET.requestFocus();

                switch (validateMadeBy) {
                    case 1:
                        mMadeByET.setError(getResources().getString(
                                R.string.input_error_product_description_too_short));
                        break;

                    case 2:
                        mMadeByET.setError(getResources().getString(
                                R.string.input_error_product_description_too_long));
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
        }

        /* Get the user specific information */
        // Retailer
        mRetailer = mRetailerET.getText().toString().trim();
        int validateRetailer = InputValidation.validateRetailer(mRetailer);

        if (validateRetailer != 0) {

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

        // Pack price
        // TODO - Implement currency, change data type to Float
        String unformattedPrice = mPackPriceET.getText().toString().trim();

        if (unformattedPrice.length() > 0) {
            mPackPrice = Double.parseDouble(unformattedPrice);
        } else {
            mPackPrice = 0.0;
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

        // Set the new product to Firebase
        // See: https://firebase.google.com/docs/database/admin/save-data
        if (mFbProductReferenceKey.equals(Constants.DEFAULT_FB_PRODUCT_ID)) {

            Log.e(LOG_TAG, "onSave() - Product is new");
            // This is a new product, so get a Firebase reference, save the product and user info.
            // Create the base product information
            Product productBase = new Product();
            productBase.setDescription(mDescription);
            productBase.setMadeBy(mMadeBy);
            productBase.setCategory(mCategory);
            productBase.setShelfLife(mShelfLife);
            productBase.setPackSize(mPackSize);
            productBase.setUnitOfMeasure(mUnitOfMeasure);
            productBase.setPackPrice(mPackPrice);
            productBase.setCreatedBy(mUserUid);

            Map<String, Object> baseProductMap = productBase.baseProductToMap();

            // Save the base product (common product information twixt all users) to
            // Firebase reference /collection_products/
            mFbCollectionProduct
                    .push()
                    .setValue(baseProductMap, (databaseError, databaseReference) -> {

                        // Get the database reference for this document
                        Uri uri = Uri.parse(databaseReference.toString());
                        mFbProductReferenceKey = uri.getLastPathSegment();

                        // Create a new product with all of the fields
                        Product newProduct = new Product(
                                mDescription,
                                mMadeBy,
                                mFbProductReferenceKey,
                                mRetailer,
                                mUnitOfMeasure,
                                mPackSize,
                                mShelfLife,
                                mLocationRoom,
                                mLocationInRoom,
                                mCategory,
                                mPackPrice,
                                mProductImageUri,
                                mFbStorageImageUri,
                                mUserUid);

                        // Save the full product to // reference /collection/users/user_id/products/
                        DatabaseReference userProductRef = mFbCollectionUsers
                                        .child(mUserUid)
                                        .child(Constants.FB_COLLECTION_PRODUCTS)
                                        .child(mFbProductReferenceKey);
                        userProductRef.setValue(newProduct);

                        // And finally to keep a tally on who is using what, save the product
                        // reference and user ID to reference:
                        // '/user_products/[product reference]/[user id]'
                        DatabaseReference usedProductRef = mFbCollectionUsedProducts
                                        .child(mFbProductReferenceKey);

                        usedProductRef.push().setValue(mUserUid);

                        // Save the product to the local database
                        AppExecutors.getInstance().diskIO().execute(() ->
                                mDb.getProductDao().insertProduct(newProduct));
                        finish();
                    });
        } else {
            // This is an existing product, so update any values that have changed.
            // mProduct was updated in onCreate with the existing base values, so we can
            // use it to compare with values extracted from the EditText fields.
            Log.e(LOG_TAG, "saveProduct() - This is an existing product");

            // Only a product creator can edit an existing product
            if (mIsCreator && mBaseFieldsEditableContainer.getVisibility() == View.VISIBLE) {
                // First off, create a HashMap to store the changes for the base product information
                Map<String, Object> productBaseUpdates = new HashMap<>();
                // Create a counter to see how many updates there are, if any.
                int updateBaseCounter = 0;

                // Add any changes to the base product information
                if (!mProduct.getDescription().equals(mDescription)) {
                    productBaseUpdates.put(Constants.PRODUCT_BASE_DESCRIPTION_KEY, mDescription);
                    mProduct.setDescription(mDescription);
                    updateBaseCounter++;
                }
                if (!mMadeBy.equals(mProduct.getMadeBy())) {
                    productBaseUpdates.put(Constants.PRODUCT_BASE_MADE_BY_KEY, mMadeBy);
                    mProduct.setMadeBy(mMadeBy);
                    updateBaseCounter++;
                }
                if (mPackSize != mProduct.getPackSize()) {
                    productBaseUpdates.put(Constants.PRODUCT_BASE_PACK_SIZE_KEY, mPackSize);
                    mProduct.setPackSize(mPackSize);
                    updateBaseCounter++;
                }
                if (mUnitOfMeasure != mProduct.getUnitOfMeasure()) {
                    productBaseUpdates.put(Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY, mUnitOfMeasure);
                    mProduct.setUnitOfMeasure(mUnitOfMeasure);
                    updateBaseCounter++;
                }
                if (mShelfLife != mProduct.getShelfLife()) {
                    productBaseUpdates.put(Constants.PRODUCT_BASE_SHELF_LIFE_KEY, mShelfLife);
                    mProduct.setShelfLife(mShelfLife);
                    updateBaseCounter++;
                }
                if (mCategory != mProduct.getCategory()) {
                    productBaseUpdates.put(Constants.PRODUCT_BASE_CATEGORY_KEY, mCategory);
                    mProduct.setCategory(mCategory);
                    updateBaseCounter++;
                }

                Log.e(LOG_TAG, "saveProduct() - there are: "
                        + updateBaseCounter + " base updates");

                // Save the base updates
                if (updateBaseCounter > 0) {

                    // Update the product specific information in Firebase
                    DatabaseReference reference = mFbCollectionProduct
                            .child(mProduct.getFbProductReferenceId());

                    reference.updateChildren(productBaseUpdates);

                    // Todo - remove room
                    // Now save the updates to Room database
                    AppExecutors.getInstance().diskIO().execute(() ->
                            mDb.getProductDao().updateProduct(mProduct));
                }
            }

            // Now for the user specific information, create a HashMap to store the changes
            Map<String, Object> productUserUpdates = new HashMap<>();

            // Add the existing base information to the HashMap
            productUserUpdates.put(
                    Constants.PRODUCT_BASE_DESCRIPTION_KEY,
                    mProduct.getDescription());
            productUserUpdates.put(
                    Constants.PRODUCT_BASE_MADE_BY_KEY,
                    mProduct.getMadeBy());
            productUserUpdates.put(
                    Constants.PRODUCT_BASE_PACK_SIZE_KEY,
                    mProduct.getPackSize());
            productUserUpdates.put(
                    Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY,
                    mProduct.getUnitOfMeasure());
            productUserUpdates.put(
                    Constants.PRODUCT_BASE_SHELF_LIFE_KEY,
                    mProduct.getShelfLife());
            productUserUpdates.put(
                    Constants.PRODUCT_BASE_CATEGORY_KEY,
                    mProduct.getCategory());
            productUserUpdates.put(
                    Constants.PRODUCT_BASE_CREATED_BY_KEY,
                    mProduct.getCreatedBy());
            productUserUpdates.put(
                    Constants.PRODUCT_FB_REFERENCE_KEY,
                    mProduct.getFbProductReferenceId());
            productUserUpdates.put(
                    Constants.PRODUCT_USER_FB_STORAGE_IMAGE_URI_KEY,
                    mProduct.getFbStorageImageUri());
            productUserUpdates.put(
                    Constants.PRODUCT_USER_LOCAL_IMAGE_URI_KEY,
                    mProduct.getLocalImageUri());

            // Create a counter to see how many updates there are, if any.
            int userUpdateCounter = 0;

            // Add any changes from the user specific fields to the HashMap
            if (!mRetailer.equals(mProduct.getRetailer())) {
                productUserUpdates.put(Constants.PRODUCT_USER_RETAILER_KEY, mRetailer);
                mProduct.setRetailer(mRetailer);
                userUpdateCounter++;
            }
            if (!mLocationRoom.equals(mProduct.getLocationRoom())) {
                productUserUpdates.put(Constants.PRODUCT_USER_LOCATION_ROOM_KEY, mLocationRoom);
                mProduct.setLocationRoom(mLocationRoom);
                userUpdateCounter++;
            }
            if (!mLocationInRoom.equals(mProduct.getLocationInRoom())) {
                productUserUpdates.put(Constants.PRODUCT_USER_LOCATION_IN_ROOM_KEY, mLocationInRoom);
                mProduct.setLocationInRoom(mLocationInRoom);
                userUpdateCounter++;
            }
            if (mPackPrice != mProduct.getPackPrice()) {
                productUserUpdates.put(Constants.PRODUCT_USER_PACK_PRICE_KEY, mPackPrice);
                mProduct.setPackPrice(mPackPrice);
                userUpdateCounter++;
            }

            // Save the user specific updates with the original base information
            if (userUpdateCounter > 0) {

                // Make a reference to the users products
                DatabaseReference userProductRef = mFbCollectionUsers
                        .child(mUserUid)
                        .child(Constants.FB_COLLECTION_PRODUCTS)
                        .child(mFbProductReferenceKey);

                // Save the changes to the users products
                userProductRef.updateChildren(productUserUpdates);

                Log.e(LOG_TAG, "SaveProduct() - UserSpecific updates - mProduct.getFbRefID = "
                        + mProduct.getFbProductReferenceId());

                // Update the used products collection only if it hasn't been added before
                if(mPutProductOnUsedList) {
                    DatabaseReference usedProductRef = mFbCollectionUsedProducts
                            .child(mProduct.getFbProductReferenceId());

                    usedProductRef.push().setValue(mUserUid);
                }
            }

            Log.e(LOG_TAG, "saveProduct() - there are: " + userUpdateCounter + " base updates");

            // Todo - make these two saves into a single HashMap

            finish();
        }
    }

    /* Called when updating a products information. There are four states to deal with:
     * - Base fields editable
     * - Base fields uneditable
     * - User specific fields editable
     * - User specific fields uneditable
     * */
    private void populateUi() {
        Log.e(LOG_TAG, "populateUi() called");

        // Base fields editable criteria:
        // 1. Has to be creator of product
        // 2. View has to be visible
        if (mIsCreator && mBaseFieldsEditableContainer.getVisibility() == View.VISIBLE) {
            Log.e(LOG_TAG, "IsCreator is: True && BaseFields are Visible");
            // Update the EditText fields for the base product information
            mDescriptionET.setText(mProduct.getDescription());
            mMadeByET.setText(mProduct.getMadeBy());
            mPackSizeET.setText(String.valueOf(mProduct.getPackSize()));
            mUoMSpinner.setSelection(mProduct.getUnitOfMeasure());
            mShelfLifeSpinner.setSelection(mProduct.getShelfLife());
            mCategorySpinner.setSelection(mProduct.getCategory());

            // Base fields uneditable criteria:
            // 1. View has to be visible
        } else if (mBaseFieldsUnEditableContainer.getVisibility() == View.VISIBLE) {

            Log.e(LOG_TAG, "IsCreator is: False && BaseFieldsEditor view is GONE");

            // Update the TextViews
            mDescriptionTV.setText(mProduct.getDescription());
            Log.e(LOG_TAG, "populateUi() called. User is not creator. setting description to: "
                    + mProduct.getDescription());
            mMadeByTV.setText(mProduct.getMadeBy());
            mPackSizeTV.setText(String.valueOf(mProduct.getPackSize()));

            mUoMTV.setText(Converters.getUnitOfMeasureString(
                    this, mProduct.getUnitOfMeasure()));

            mShelfLifeTV.setText(Converters.getShelfLifeString(
                    this, mProduct.getShelfLife()));

            mCategoryTV.setText(Converters.getCategoryString(
                    this, mProduct.getCategory()));

            Log.e(LOG_TAG, "Category is: " + Converters.getCategoryString(
                    this, mProduct.getCategory()));
        }

        // User specific fields editable criteria:
        // 1. Has to be on the users used product list or about to be on it
        // 2. View has to be visible
        if ((mProductInUsedList || mPutProductOnUsedList) && mUserSpecificFieldsEditableContainer
                .getVisibility() == View.VISIBLE) {

            mRetailerET.setText(mProduct.getRetailer());
            Log.e(LOG_TAG, "populateUi() - Product is on used list and Editable container " +
                    "is visible mProduct.getRetailer() is: " + mProduct.getRetailer());

            mPackPriceET.setText(String.valueOf(mProduct.getPackPrice()));
            mLocationRoomET.setText(mProduct.getLocationRoom());
            mLocationInRoomET.setText(mProduct.getLocationInRoom());

            // Set the menu items for this UI state
            setMenuItemVisibility(true, true, true);

            // User specific fields uneditable criteria
            // 1. Has to be in the users used product list
            // 2. View has to be visible
        } else if (mProductInUsedList && mUserSpecificFieldsUneditableContainer
                .getVisibility() == View.VISIBLE) {

            // Update the uneditable views
            mRetailerTV.setText(mProduct.getRetailer());
            mPackPriceTV.setText(String.valueOf(mProduct.getPackPrice()));
            mLocationRoomTV.setText(mProduct.getLocationRoom());
            mLocationInRoomTV.setText(mProduct.getLocationInRoom());

            // This product is all ready in the users used list so remove the FAB
            mFab.setVisibility(View.GONE);

            // This call invalidates the current menu options and calls onPrepareOptionsMenu()
            // which will in turn populate the menu with the correct icons for this view.
            invalidateOptionsMenu();

        }

        // Only load image if the Uri is not the default value (EMPTY)
        if (!Uri.EMPTY.equals(Uri.parse(mProduct.getLocalImageUri()))) {
            mProductImageUri = Uri.parse(mProduct.getLocalImageUri());
            loadImage();
        }
    }

//    /* Changes the focusable values of the base product editable fields */
//    private void baseFieldsChangeETFocus(boolean isFocusable) {
//
//        mDescriptionET.setFocusableInTouchMode(isFocusable);
//        mMadeByET.setFocusableInTouchMode(isFocusable);
//        mPackSizeET.setFocusableInTouchMode(isFocusable);
//        mUoMSpinner.setFocusableInTouchMode(isFocusable);
//        mShelfLifeSpinner.setFocusableInTouchMode(isFocusable);
//        mCategorySpinner.setFocusableInTouchMode(isFocusable);
//    }
//
//    /* Changes the focusable value of the user specific fields */
//    private void userFieldsChangeETFocus(boolean isFocusable) {
//
//        mRetailerET.setFocusableInTouchMode(isFocusable);
//        mPackPriceET.setFocusableInTouchMode(isFocusable);
//        mLocationRoomET.setFocusableInTouchMode(isFocusable);
//        mLocationInRoomET.setFocusableInTouchMode(isFocusable);
//    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        /* Save the current product */
        outState.putString(Constants.USER_ID_KEY, mUserUid);
        outState.putParcelable(Constants.PRODUCT_KEY, mProduct);
        outState.putString(Constants.PRODUCT_FB_REFERENCE_KEY, mFbProductReferenceKey);

        // Save the bool's!
        outState.putBoolean(Constants
                .PRODUCT_IS_CREATOR_KEY, mIsCreator);
        outState.putBoolean(Constants
                .PRODUCT_IS_EXISTING_KEY, mIsExistingProduct);
        outState.putBoolean(Constants
                .PRODUCT_PUT_ON_USED_LIST, mPutProductOnUsedList);
        outState.putBoolean(Constants
                .PRODUCT_ON_USED_LIST_KEY, mProductInUsedList);
        outState.putBoolean(Constants
                .BASE_FIELDS_EDITABLE_STATUS_KEY, mBaseFieldsAreEditable);
        outState.putBoolean(Constants
                .USER_CUSTOM_FIELDS_EDITABLE_STATUS_KEY, mUserSpecificFieldsAreEditable);

    }

    /* Get a reference eto all of the views */
    private void initialiseViews() {

        // Containers
        mBaseFieldsEditableContainer = findViewById(
                R.id.activity_detail_product_base_fields_editable_container);
        mBaseFieldsUnEditableContainer = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_container);
        mUserSpecificFieldsEditableContainer = findViewById(
                R.id.activity_detail_product_user_fields_container);
        mUserSpecificFieldsUneditableContainer = findViewById(
                R.id.activity_detail_product_user_fields_uneditable_container);

        // EditText fields
        mDescriptionET = findViewById(
                R.id.activity_detail_product_base_fields_editable_et_description);
        mMadeByET = findViewById(
                R.id.activity_detail_product_base_fields_editable_et_made_by);
        mPackSizeET = findViewById(
                R.id.activity_detail_product_base_fields_editable_et_pack_size);
        mRetailerET = findViewById(
                R.id.activity_detail_product_et_retailer);
        mLocationRoomET = findViewById(
                R.id.activity_detail_product_et_location_room);
        mLocationInRoomET = findViewById(
                R.id.activity_detail_product_et_location_in_room);
        mPackPriceET = findViewById(
                R.id.activity_detail_product_et_price);

        // TextViews
        mDescriptionTV = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_tv_description);
        mMadeByTV = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_tv_made_by);
        mPackSizeTV = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_tv_pack_size);
        mUoMTV = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_tv_UoM);
        mShelfLifeTV = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_tv_shelf_life);
        mCategoryTV = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_tv_category);
        mRetailerTV = findViewById(
                R.id.activity_detail_product_user_fields_uneditable_tv_retailer);
        mPackPriceTV = findViewById(
                R.id.activity_detail_product_user_fields_uneditable_tv_price);
        mLocationRoomTV = findViewById(
                R.id.activity_detail_product_user_fields_uneditable_tv_location_room);
        mLocationInRoomTV = findViewById(
                R.id.activity_detail_product_user_fields_uneditable_tv_location_in_room);

        // Spinner fields
        mUoMSpinner = findViewById(
                R.id.activity_detail_product_base_fields_editable_spinner_UoM);
        mShelfLifeSpinner = findViewById(
                R.id.activity_detail_product_base_fields_editable_spinner_shelf_life);
        mCategorySpinner = findViewById(
                R.id.activity_detail_product_base_fields_editable_spinner_category);

        // Image button
        mAddImageIB = findViewById(
                R.id.activity_detail_product_ib_add_picture);

        // Image field
        mProductIV = findViewById(
                R.id.activity_detail_product_iv);

        // Spinners
        setupUoMSpinner();
        setupShelfLifeSpinner();
        setupCategorySpinner();

        // Assign FAB
        mFab = findViewById(R.id.activity_detail_product_fab);

        // Initial visibility for editable and uneditable field containers
        mBaseFieldsEditableContainer.setVisibility(View.GONE);
        mBaseFieldsUnEditableContainer.setVisibility(View.GONE);

        /* OnClickListener for the add picture by camera button */
        mAddImageIB.setOnClickListener(v -> requestPermissions());

        /* onClickListener for the FAB */
        mFab.setOnClickListener(v -> {

            // If the current product is not in the users used list, pressing the FAB opens up the
            // user specific fields, so the user can edit the user specific fields
            if (!mProductInUsedList) {
                // Make the user specific fields visible
                mUserSpecificFieldsAreEditable = true;
                updateUserSpecificFieldsEditableStatus();
                // This product is going to be added to the users used lis so update the bool
                mPutProductOnUsedList = true;
                // Turn off the FAB
                mFab.setVisibility(View.GONE);
                // In the user specific fields set the focus to the first field
                mRetailerET.requestFocus();
                // Show the save button
                setMenuItemVisibility(true, false, false);
            }
        });
    }

    /* Sets up the visibility properties for an existing uneditable product */
    private void updateUneditableFields() {

        Log.e(LOG_TAG, "updateUneditableFields - called. InUsedList is: " + mProductInUsedList);
        // This is for product viewing only, so no need for editable views
        mBaseFieldsEditableContainer.setVisibility(View.GONE);
        mUserSpecificFieldsEditableContainer.setVisibility(View.GONE);
        // Make relevant view containers visible
        mBaseFieldsUnEditableContainer.setVisibility(View.VISIBLE);
        // Only show user the 'specific product information container' if in used list
        if (mProductInUsedList) {
            mUserSpecificFieldsUneditableContainer.setVisibility(View.VISIBLE);
        } else {
            Log.e(LOG_TAG, "updateUneditableFields - user specific fields are GONE");
            mUserSpecificFieldsUneditableContainer.setVisibility(View.GONE);
        }
    }

    /* Sets the visibility and editable properties for the base field views */
    private void updateBaseFieldsEditableStatus() {

        if (mBaseFieldsAreEditable) {
            mBaseFieldsEditableContainer.setVisibility(View.VISIBLE);
            mBaseFieldsUnEditableContainer.setVisibility(View.GONE);
        } else {
            mBaseFieldsEditableContainer.setVisibility(View.GONE);
            mBaseFieldsUnEditableContainer.setVisibility(View.VISIBLE);
        }
    }

    /* Sets the visibility properties for the editable and non editable user specific views */
    private void updateUserSpecificFieldsEditableStatus() {

        if (mUserSpecificFieldsAreEditable) {
            mUserSpecificFieldsEditableContainer.setVisibility(View.VISIBLE);
            mUserSpecificFieldsUneditableContainer.setVisibility(View.GONE);
        } else {
            mUserSpecificFieldsEditableContainer.setVisibility(View.GONE);
            // If the product is on or is going to be on the users used list turn the user
            // specific fields on
            if (mProductInUsedList || mPutProductOnUsedList) {
                mUserSpecificFieldsUneditableContainer.setVisibility(View.VISIBLE);
            } else {
                // If not on the users used list turn the view off
                mUserSpecificFieldsUneditableContainer.setVisibility(View.GONE);
            }
        }
    }

    /* Delete the product */
    // todo - Complete the delete product action
    // todo - Create a list of deleted products to compare with Firebase products
    // todo - so we don't display products from firebase that the user has deleted
    // todo - delete the product information from /products/ and /users/products/
    private void deleteProduct() {
        // This is an existing product that was not created by the user. So there sre two things
        // we need to do:
        // 1. Delete the product form the users used product list which is located at
        // /collection_users/[user id]/collection_products/[product id]
        // 2. Delete the entry for this product from this user in the used_products reference
        if (mIsExistingProduct && !mIsCreator && mProductInUsedList) {
//            DatabaseReference userProductRef =
        }

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

    /* Get the permissions result */
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

        MenuItem saveItem = menu.findItem(R.id.menu_product_editor_action_save);
        MenuItem editItem = menu.findItem(R.id.menu_product_editor_action_edit);
        MenuItem deleteItem = menu.findItem(R.id.menu_product_editor_action_delete);

        // Setup the icons
        if (saveItem != null && editItem != null && deleteItem != null) {
            tintMenuIcon(this, saveItem, R.color.primary_light);
            tintMenuIcon(this, editItem, R.color.primary_light);
            tintMenuIcon(this, deleteItem, R.color.primary_light);
        }
        return true;
    }

    /* This method is called after invalidateOptionsMenu(), so the menu can be updated (some menu
       items can be hidden or made visible). */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Update the member variable
        mProductEditorMenu = menu;

        /* Initial status bar options */
        setMenuItemVisibility(false, false, false);

        // If product is a new product, hide the "Delete" and "Edit"  menu items.
        if (mFbProductReferenceKey.equals(Constants.DEFAULT_FB_PRODUCT_ID)) {
            setMenuItemVisibility(true, false, false);
        }

        // If product is in the users used list and user specific fields are uneditable
        if (mProductInUsedList && mUserSpecificFieldsUneditableContainer
                .getVisibility() == View.VISIBLE) {
            setMenuItemVisibility(false, true, true);
        }

        // If product is in the users used list, the user is not the creator and the user specific
        // fields are not editable
        if (mIsExistingProduct
                && !mIsCreator
                && mProductInUsedList
                && mUserSpecificFieldsEditableContainer.getVisibility() == View.VISIBLE) {

            setMenuItemVisibility(true, false, false);
        }

        return true;
    }

    /* This method manages the actions as they are selected from the AppBar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Save (tick) menu option selected
            case R.id.menu_product_editor_action_save:
                // Reduce bounce (double click) by hiding the items once pressed.
                setMenuItemVisibility(false, false, false);
                // Save product and exit activity
                saveProduct();
                break;
            case R.id.menu_product_editor_action_delete:
                // Delete product
                deleteProduct();
                break;
            case R.id.menu_product_editor_action_edit:

                Log.e(LOG_TAG, "onOptionsItemSelected - Edit pressed state of vars is " +
                        "as follows: \n"
                        + "isExistingProduct: " + mIsExistingProduct + "\n"
                        + "IsCreator: " + mIsCreator + "\n"
                        + "ProductInUsedList: " + mProductInUsedList + "\n"
                        + "mPutProductOnUsedList: " + mPutProductOnUsedList);

                // An existing product, not created by user, but on users used list
                if (mIsExistingProduct && !mIsCreator && mProductInUsedList) {
                    // Set the screen title to edit a product
                    setScreenTitle(Constants.SCREEN_TITLE_PRODUCT_EDIT);
                    // Make the user specific fields editable
                    mUserSpecificFieldsAreEditable = true;
                    updateUserSpecificFieldsEditableStatus();
                    // Request focus to the first editable filed
                    mRetailerET.requestFocus();
                    // Update the menu items
                    invalidateOptionsMenu();
                    populateUi();

                } else {
                    Log.e(LOG_TAG, "Criteria not met for - mIsExistingProduct && !mIsCreator " +
                            "&& mProductInUsedList \n"
                            + " mIsExistingProduct (true): " + mIsExistingProduct + "\n"
                            + " mIsCreator (false): " + mIsCreator + "\n"
                            + " mProductInUsedList (true): " + mProductInUsedList);
                }
        }
        return super.onOptionsItemSelected(item);
    }

    /* Sets the visibility properties of the menu items */
    private void setMenuItemVisibility(boolean saveItem, boolean editItem, boolean deleteItem) {

        mProductEditorMenu.findItem(R.id.menu_product_editor_action_save).setVisible(saveItem);
        mProductEditorMenu.findItem(R.id.menu_product_editor_action_edit).setVisible(editItem);
        mProductEditorMenu.findItem(R.id.menu_product_editor_action_delete).setVisible(deleteItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add the firebase authentication state listener to the authentication instance
        mFBAuth.addAuthStateListener(mFBAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove the firebase authentication state listener from the authentication instance
        mFBAuth.removeAuthStateListener(mFBAuthStateListener);
    }

    // Color the icons
    // From: https://stackoverflow.com/questions/24301235/tint-menu-icons/39535399#39535399
    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }
}
