package com.example.peter.thekitchenmenu.ui.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.databinding.ActivityDetailProductBinding;
import com.example.peter.thekitchenmenu.data.model.Product;
import com.example.peter.thekitchenmenu.utils.BitmapUtils;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.example.tkmapplibrary.dataValidation.InputValidation;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityDetailProduct
        extends AppCompatActivity {

    public static final String LOG_TAG = ActivityDetailProduct.class.getSimpleName();

    // TODO - Input validation:
    // TODO - Use 'do while'. See Intro to Java L13:L6.2-2
    // TODO - Make sure numbers cannot go negative OR be a decimal

    // Data binding classes for the layouts we use in this class
    ActivityDetailProductBinding mDetailProductBinding;

    // Product object instance
    private Product
            mProduct;

    // String member variables for the product fields
    private String
            mDescription,
            mRetailer,
            mLocationRoom,
            mLocationInRoom,
            mMadeBy,
            mCreatedBy;

    // Integer member variables for the product fields
    private int
            mUnitOfMeasure,
            mPackSize,
            mShelfLife,
            mCategory;

    // Double member variables for the product fields
    private double
            mPackPrice,
            mPackPriceAverage;

    // Boolean member variables for the applications logic in switching through its decision tree
    private boolean
            mIsExistingProduct,
            mIsCreator,
            mInUsersUsedList,
            mIsMultiUser,
            mBaseFieldsAreEditable,
            mUserFieldsAreEditable,
            mPutProductOnUsedList,
            mImageAvailable,
            mCameraImageTaken;

    // Booleans that keep track of validated user input fields
    private boolean
            mDescriptionIsValidated,
            mMadeByIsValidated,
            mPackSizeIsValidated,
            mUoMIsValidated,
            mShelfLifeIsValidated,
            mCategoryIsValidated,
            mRetailerIsValidated,
            mPackPriceIsValidated,
            mLocationRoomIsValidated,
            mLocationInRoomIsValidated;

    // Menu bar
    private Menu mProductEditorMenu;

    // The temporary path on the device where the full size image is held for processing
    private String mTempImagePath;

    // The permanent public (device local) URI for the local image
    private Uri mLocalImageUri;

    // The Firebase Image storage location
    private Uri mFbStorageImageUri;

    /* Firebase database reference - the entry point for accessing products */
    private DatabaseReference mFbCollectionProduct;
    /* Firebase database reference - the entry point for accessing users */
    private DatabaseReference mFbCollectionUsers;
    /* Firebase database reference - the entry point for accessing used products */
    private DatabaseReference mFbCollectionUsedProducts;
    /* Authentication instance */
    private FirebaseAuth mFBAuth;
    /* Authentication state listener */
    private FirebaseAuth.AuthStateListener mFBAuthStateListener;
    /* Authentication users unique ID generated for this user / app combination */
    private String mUserUid;
    /* Unique Firebase product reference */
    private String mFbProductReferenceKey;
    /* Unique Firebase used product reference */
    private String mFbUsedProductsUserKey;
    /* Firebase storage instance */
    private FirebaseStorage mFirebaseStorage;
    /* Firebase storage reference object */
    private StorageReference mImageStorageReference;

    // Color the icons
    // From: https://stackoverflow.com/questions/24301235/tint-menu-icons/39535399#39535399
    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {

        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assignMemberVariables();
        setupFireBase();
        initialiseFirebaseAuthentication();
        checkSavedInstanceState(savedInstanceState);
        initialiseViews();

        Object o = ServerValue.TIMESTAMP;

    }

    /* Initialise anything here that can only be done when signed in */
    private void onSignedInInitialise(String userUid) {

        // Set the user ID
        mUserUid = userUid;

        // If there is a product passed with the intent this is an existing product
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.PRODUCT_FB_REFERENCE_KEY)) {

            // Update the existing product bool
            mIsExistingProduct = true;

            // This intent has been passed the base product fields of a Firebase product
            setScreenTitle(Constants.ACTIVITY_TITLE_PRODUCT_VIEW);

            // Update the Firebase product reference key with the product passed in
            if (mFbProductReferenceKey.equals(Constants.DEFAULT_FB_PRODUCT_ID)) {

                // Set the incoming product to its member variable
                mProduct = intent.getParcelableExtra(Constants.PRODUCT_FB_REFERENCE_KEY);

                // Set the Firebase product reference ID from the incoming intent */
                mFbProductReferenceKey = mProduct.getFbProductReferenceKey();

                // Identify if this user is the creator of this product
                mIsCreator = intent.getBooleanExtra(Constants.PRODUCT_IS_CREATOR_KEY, mIsCreator);

                // Find out if the product is on this users used list. This is a database operation
                // and may take some time. We cannot proceed until we get the result. So continue
                // operations from method gertUsedList()
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

                mFbProductReferenceKey = mProduct.getFbProductReferenceKey();
                mFbUsedProductsUserKey = mProduct.getFbUsedProductsUserKey();
                mDescription = mProduct.getDescription();
                mRetailer = mProduct.getRetailer();
                mMadeBy = mProduct.getMadeBy();
                mUnitOfMeasure = mProduct.getUnitOfMeasure();
                mPackSize = mProduct.getPackSize();
                mShelfLife = mProduct.getShelfLife();
                mLocationRoom = mProduct.getLocationRoom();
                mLocationInRoom = mProduct.getLocationInRoom();
                mCategory = mProduct.getCategory();
                mPackPrice = mProduct.getPackPrice();
                mPackPriceAverage = mProduct.getPackPriceAverage();
                mLocalImageUri = Uri.parse(mProduct.getLocalImageUri());
                mFbStorageImageUri = Uri.parse(mProduct.getLocalImageUri());
                mCreatedBy = mProduct.getCreatedBy();
            }

            // Update the bool's
            mIsExistingProduct =
                    savedInstanceState.getBoolean(Constants.PRODUCT_IS_EXISTING_KEY);
            mIsCreator =
                    savedInstanceState.getBoolean(Constants.PRODUCT_IS_CREATOR_KEY);
            mInUsersUsedList =
                    savedInstanceState.getBoolean(Constants.PRODUCT_ON_USED_LIST_KEY);
            mPutProductOnUsedList =
                    savedInstanceState.getBoolean(Constants.PRODUCT_PUT_ON_USED_LIST);
            mBaseFieldsAreEditable =
                    savedInstanceState.getBoolean(Constants.COMM_FIELDS_EDITABLE_STATUS_KEY);
            mUserFieldsAreEditable =
                    savedInstanceState.getBoolean(Constants.MY_CUSTOM_FIELDS_EDITABLE_STATUS_KEY);

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

        /* Assign bool's their default values */
        // Set the default value for creator
        mIsCreator = false;

        // Set the default value for in an existing or new product
        mIsExistingProduct = false;

        // Set the default value for whether this product is on the users current used list
        mInUsersUsedList = false;

        // Set the default value for whether this product should be added to the users used list
        mPutProductOnUsedList = false;

        // Set the editable booleans to false
        mBaseFieldsAreEditable = false;
        mUserFieldsAreEditable = false;

        // Set the default value for whether the product has more then one user
        mIsMultiUser = false;

        // Tells us if the user has taken an image
        mImageAvailable = false;

        // Tells us if an image has been taken by the camera
        mCameraImageTaken = false;

        /* Construct a default product for field value comparison and validation */
        mProduct = new Product(
                Constants.DEFAULT_PRODUCT_DESCRIPTION,
                Constants.DEFAULT_PRODUCT_MADE_BY,
                Constants.DEFAULT_FB_PRODUCT_ID,
                Constants.DEFAULT_FB_USED_PRODUCT_ID,
                Constants.DEFAULT_PRODUCT_RETAILER,
                Constants.DEFAULT_PRODUCT_UOM,
                Constants.DEFAULT_PRODUCT_PACK_SIZE,
                Constants.DEFAULT_PRODUCT_SHELF_LIFE,
                Constants.DEFAULT_PRODUCT_LOC,
                Constants.DEFAULT_PRODUCT_LOC_IN_ROOM,
                Constants.DEFAULT_PRODUCT_CATEGORY,
                Constants.DEFAULT_PRODUCT_PRICE,
                Constants.DEFAULT_PRODUCT_PRICE_AVERAGE,
                Constants.DEFAULT_LOCAL_IMAGE_URI,
                Constants.DEFAULT_FB_IMAGE_STORAGE_URI,
                Constants.PRODUCT_COMM_CREATED_BY_KEY,
                Constants.DEFAULT_COMM_CREATE_DATE,
                Constants.DEFAULT_COMM_LAST_UPDATE,
                Constants.DEFAULT_MY_CREATE_DATE,
                Constants.DEFAULT_MY_LAST_UPDATE);

        // Set default value for created by
        mCreatedBy = "";

        // Set the default value for the local and Firebase Storage Image Uri's
        mLocalImageUri = Uri.parse(mProduct.getLocalImageUri());
        mFbStorageImageUri = Uri.parse(mProduct.getFbStorageImageUri());

        // Instance of Firebase storage
        mFirebaseStorage = FirebaseStorage.getInstance();

        // Instance of our Firebase storage reference
        mImageStorageReference = mFirebaseStorage
                .getReference()
                .child(Constants.FB_STORAGE_IMAGE_REFERENCE);

        // Default value for the used product reference
        mFbUsedProductsUserKey = Constants.DEFAULT_FB_USED_PRODUCT_ID;

        // Default values of Bool's that record the state of input validation
        mDescriptionIsValidated = false;
        mMadeByIsValidated = false;
        mPackSizeIsValidated = false;
        mUoMIsValidated = false;
        mShelfLifeIsValidated = false;
        mCategoryIsValidated = false;
        mRetailerIsValidated = false;
        mPackPriceIsValidated = false;
        mLocationRoomIsValidated = false;
        mLocationInRoomIsValidated = false;
    }

    /* Sets up Firebase instance and references */
    private void setupFireBase() {

        // Get a reference to the Firebase database
        FirebaseDatabase mFbDatabase = FirebaseDatabase.getInstance();

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
    private void onSignedOutCleanUp() {
        mUserUid = Constants.ANONYMOUS;
    }

    /* This is a new product being generated so setup*/
    private void newProductInitialise() {

        // Set the title
        setScreenTitle(Constants.ACTIVITY_TITLE_PRODUCT_ADD);

        // Set the appropriate editable layouts visibility
        mBaseFieldsAreEditable = true;
        updateBaseFieldsEditableStatus();
        mUserFieldsAreEditable = true;
        updateUserFieldsEditableStatus();

        // Set the FAB to gone
        mDetailProductBinding.
                activityDetailProductFab.
                setVisibility(View.GONE);

        // Set the image editing buttons to visible
        mDetailProductBinding.
                activityDetailProductIbAddCameraPicture.
                setVisibility(View.VISIBLE);

        mDetailProductBinding.
                activityDetailProductIbRotatePicture.
                setVisibility(View.VISIBLE);

        mDetailProductBinding.
                activityDetailProductIbAddGalleryPicture.
                setVisibility(View.VISIBLE);

        // Move the cursor to the first field
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtDescription.
                requestFocus();

        // Update the bool's
        mIsCreator = true;
        mIsExistingProduct = false;
        mPutProductOnUsedList = true;
    }

    /* Validates the 'description' field against the validation rules in the TKMAppLibrary */
    private boolean validateDescription(String description) {

        // Validate its content
        int validateDescription = InputValidation.validateProductDescription(description);

        // If there is an error
        if (validateDescription != 0) {

            // Report the error
            switch (validateDescription) {
                case 1:
                    mDetailProductBinding.
                            activityDetailProductBaseFieldsEditableInclude.
                            activityDetailProductBaseFieldsEditableEtDescription.
                            setError(getResources().getString(
                                    R.string.input_error_product_description_too_short));
                    return false;

                case 2:
                    mDetailProductBinding.
                            activityDetailProductBaseFieldsEditableInclude.
                            activityDetailProductBaseFieldsEditableEtDescription.
                            setError(getResources().getString(
                                    R.string.input_error_product_description_too_long));
                    return false;
            }
        }
        // If all is well update the member and bool
        mDescription = description;
        return true;
    }

    /* Validates the 'made by' field against the validation rules in the TKMAppLibrary */
    private boolean validateMadeBy(String madeBy) {

        // Validate its content
        int validateMadeBy = InputValidation.validateMadeBy(madeBy);

        // If there is an error
        if (validateMadeBy != 0) {

            // Report the error
            switch (validateMadeBy) {
                case 1:
                    mDetailProductBinding.
                            activityDetailProductBaseFieldsEditableInclude.
                            activityDetailProductBaseFieldsEditableEtMadeBy.
                            setError(getResources().getString(
                                    R.string.input_error_product_description_too_short));
                    return false;

                case 2:
                    mDetailProductBinding.
                            activityDetailProductBaseFieldsEditableInclude.
                            activityDetailProductBaseFieldsEditableEtMadeBy
                            .setError(getResources().getString(
                                    R.string.input_error_product_description_too_long));
                    return false;
            }
        }
        mMadeBy = madeBy;
        return true;
    }

    /* Validates the 'pack size' field against the validation rules in the TKMAppLibrary */
    private boolean validatePackSize(String packSizeString) {

        // Get the value in the field
        if (!TextUtils.isEmpty(packSizeString)) {

            int packSizeInt = Integer.parseInt(packSizeString);

            // Check against validation rules in TKMAppLibrary
            boolean validatePackSize = InputValidation.validatePackSize(packSizeInt);

            // If there is an error
            if (!validatePackSize) {
                mDetailProductBinding.
                        activityDetailProductBaseFieldsEditableInclude.
                        activityDetailProductBaseFieldsEditableEtPackSize.
                        setError(getResources().getString(
                                R.string.input_error_product_pack_size));
                return false;
            }
            // Update the member and bool
            mPackSize = packSizeInt;
            return true;
        }

        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtPackSize.
                setError(getResources().getString(
                        R.string.input_error_product_pack_size));

        return false;
    }

    /* Validates the 'unit of measure' against the validation rules in the TKMAppLibrary */
    private boolean validateUnitOfMeasure() {

        // Unit of measure is automatically updated when user selects an item in the spinner.
        // See setupUoMSpinner() for details. Check against validation rules in TKMAppLibrary.
        mUoMIsValidated = InputValidation.validateUoM(mUnitOfMeasure);

        // If there is an error
        if (!mUoMIsValidated) {

            // Set the error to the TextView next to the UoM spinner
            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableTvSpinnerUoMSetError.
                    setError(getString(R.string.
                            activity_detail_product_base_fields_editable_error_UoM));

            return false;
        }

        // Clear any errors
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableTvSpinnerUoMSetError.
                setError(null);

        return true;
    }

    /* Validates the 'shelf life' against the validation rules in the TKMAppLibrary */
    private boolean validateShelfLife() {

        // Unit of measure is automatically updated when user selects an item in the spinner.
        // See setupShelfLifeSpinner() for details. Check against validation rules in TKMAppLibrary.
        mShelfLifeIsValidated = InputValidation.validateShelfLife(mShelfLife);

        // If there is an error
        if (!mShelfLifeIsValidated) {
            // Set the error to the TextView next to the shelf life spinner
            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableTvSpinnerShelfLifeSetError.
                    setError(getString(R.string.
                            activity_detail_product_base_fields_editable_error_shelf_life));

            return false;
        }
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableTvSpinnerShelfLifeSetError.
                setError(null);

        return true;
    }

    /* Validates the 'category' against the validation rules in the TKMAppLibrary */
    private boolean validateCategory() {

        // The category is automatically updated when user selects an item in the spinner.
        // See setupCategorySpinner() for details. Check against validation rules in TKMAppLibrary.
        mCategoryIsValidated = InputValidation.validateProductCategory(mCategory);

        // If there is an error
        if (!mCategoryIsValidated) {
            // Set the error to the TextView next to the category spinner
            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableTvSpinnerCategorySetError.
                    setError(getString(R.string.
                            activity_detail_product_base_fields_editable_error_category));

            return false;
        }
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableTvSpinnerCategorySetError.
                setError(null);

        return true;
    }

    /* Validates the 'retailer' field against the validation rules in the TKMAppLibrary */
    private boolean validateRetailer(String retailer) {

        // Validate its content
        int validateRetailer = InputValidation.validateRetailer(retailer);

        // If there is an error
        if (validateRetailer != 0) {

            // Report the error
            switch (validateRetailer) {
                case 1:
                    mDetailProductBinding.
                            activityDetailProductUserFieldsEditableInclude.
                            activityDetailProductEtRetailer.
                            setError(getResources().getString(
                                    R.string.input_error_product_description_too_short));

                    return false;

                case 2:
                    mDetailProductBinding.
                            activityDetailProductUserFieldsEditableInclude.
                            activityDetailProductEtRetailer.
                            setError(getResources().getString(
                                    R.string.input_error_product_description_too_long));

                    return false;
            }
        }
        // Update the member and bool.
        mRetailer = retailer;
        return true;
    }

    /* Validates the 'price' field against the validation rules in the TKMAppLibrary */
    private boolean validatePackPrice() {

        // Get the value in the field
        if (!mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtPrice.
                getText().
                toString().
                equals("") ||

                !mDetailProductBinding.
                        activityDetailProductUserFieldsEditableInclude.
                        activityDetailProductEtPrice.
                        getText().
                        toString().
                        isEmpty()) {

            mPackPrice = Double.parseDouble(mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtPrice.
                    getText().
                    toString().
                    trim());

            // Check against validation rules in TKMAppLibrary
            boolean validatePrice = InputValidation.validatePrice(mPackPrice);

            // If there is an error
            if (!validatePrice) {
                mDetailProductBinding.
                        activityDetailProductUserFieldsEditableInclude.
                        activityDetailProductEtPrice.
                        setError(getResources().getString(
                                R.string.input_error_product_pack_price));

                return false;
            }
            mPackPriceIsValidated = true;

            return true;
        }
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtPrice.
                setError(getResources().getString(
                        R.string.input_error_product_pack_price));

        return false;
    }

    /* Validates the 'location room' field against the validation rules in the TKMAppLibrary */
    private boolean validateLocationRoom(String locationRoom) {

        // Validate its content
        int validateLocationRoom = InputValidation.validateLocRoom(locationRoom);

        // If there is an error
        if (validateLocationRoom != 0) {

            // Report the error
            switch (validateLocationRoom) {
                case 1:
                    mDetailProductBinding.
                            activityDetailProductUserFieldsEditableInclude.
                            activityDetailProductEtLocationRoom.
                            setError(getResources().getString(
                                    R.string.input_error_product_loc_room_too_short));

                    return false;

                case 2:
                    mDetailProductBinding.
                            activityDetailProductUserFieldsEditableInclude.
                            activityDetailProductEtLocationRoom.
                            setError(getResources().getString(
                                    R.string.input_error_product_loc_room_too_long));

                    return false;
            }
        }
        // If all is well update the member and bool
        mLocationRoom = locationRoom;
        return true;
    }

    /* Validates the 'location in room' field against the validation rules in the TKMAppLibrary */
    private boolean validateLocationInRoom(String locationInRoom) {

        // Validate its content
        int validateLocationInRoom = InputValidation.validateLocInRoom(locationInRoom);

        // If there is an error
        if (validateLocationInRoom != 0) {

            // Report the error
            switch (validateLocationInRoom) {
                case 1:
                    mDetailProductBinding.
                            activityDetailProductUserFieldsEditableInclude.
                            activityDetailProductEtLocationInRoom.
                            setError(getResources().getString(
                                    R.string.input_error_product_loc_in_room_too_short));

                    return false;

                case 2:
                    mDetailProductBinding.
                            activityDetailProductUserFieldsEditableInclude.
                            activityDetailProductEtLocationInRoom.
                            setError(getResources().getString(
                                    R.string.input_error_product_loc_in_room_too_long));

                    return false;
            }
        }
        // If all is well update the member and bool
        mLocationInRoom = locationInRoom;
        return true;
    }

    /* Check to see if the product is in the users user product list */
    private void getUsedList() {

        // Is the product in the users 'used' list
        DatabaseReference productRef = mFbCollectionUsers
                .child(mUserUid)
                .child(Constants.FB_COLLECTION_PRODUCTS)
                .child(mFbProductReferenceKey);

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If the product is in the users used list it will show up here
                if (dataSnapshot.exists()) {

                    // Convert the snapshot into a Product object
                    mProduct = dataSnapshot.getValue(Product.class);

                    // Update products Firebase product reference key
                    mProduct.setFbProductReferenceKey(dataSnapshot.getKey());

                    // This product is in the users used list, so update the used bool
                    mInUsersUsedList = true;

                    // Update the member variable for the used products user key
                    mFbUsedProductsUserKey = mProduct.getFbUsedProductsUserKey();

                    // Update the image storage location, so if it changes later we can
                    // find out by doing a comparison.
                    mFbStorageImageUri = Uri.parse(mProduct.getFbStorageImageUri());

                    // Update the visibility of the uneditable view containers
                    showUneditableViewContainers();

                    // Update the UI with the base and user data
                    populateUi();

                } else {
                    // This product is not in the users used list, so update the used bool
                    mInUsersUsedList = false;

                    // Update the visibility of the uneditable view containers
                    showUneditableViewContainers();

                    // Update the UI with the product and user data
                    populateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /* Checks to see if more than one user is using a product. */
    private void checkMultiUserStatus() {

        // Get a reference to: /collection_used_products/[product ID]/[used product ref].
        Query usedProductRef = mFbCollectionUsedProducts
                .child(mFbProductReferenceKey).limitToFirst(2);

        usedProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // This is a snapshot of all users that have this product in their used product
                    // list. We only need to know if there is more than one, so we can use the a
                    // limit to query to avoid a large amount of unnecessary data being returned.
                    long childCount = dataSnapshot.getChildrenCount();

                    // Set the value of mIsMultiUser
                    if (childCount > 1) {

                        // This product is being used by more then one person, so update the bool.
                        mIsMultiUser = true;

                        // This also means the base fields cannot be edited. Make only the
                        // userSpecific fields editable.
                        mUserFieldsAreEditable = true;
                        updateUserFieldsEditableStatus();

                    } else {

                        // Just to confirm this product is not multi-user
                        mIsMultiUser = false;

                        // If there is only one person using this product, the creator, let them
                        // edit the base fields.
                        mBaseFieldsAreEditable = true;
                        updateBaseFieldsEditableStatus();

                        // If they are editing the base fields they may also want to edit the user
                        // fields.
                        mUserFieldsAreEditable = true;
                        updateUserFieldsEditableStatus();

                    }

                    // Reset the menus to show the correct menu items.
                    invalidateOptionsMenu();

                    // Set the screen title for edit mode.
                    setScreenTitle(Constants.ACTIVITY_TITLE_PRODUCT_EDIT);

                    // Update the data in the Ui
                    populateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * saveProduct()
     * There are six reasons a user might want to save product data:
     * 1. A new product is being created.
     * 2. An existing product is being updated by the creator and the creator is the only user.
     * 3. An existing product is being updated by the creator and there is more than one user.
     * 4. An existing product is being added by someone who may not have used this product before.
     * 5. An existing product is being updated by someone who has used this product before.
     * 6. An existing product is being added by the creator who has previously deleted the product.
     */
    private void saveProduct() {

        /*
           1. This is a new product. Various parts of it and its users data needs to be stored in
              three locations in the database:
              1. /collection/products - Stores product data that is common to all users.
              2. /collection/users/[user ID]/collection_products/[product ID] - Users product lists.
              3. /collection_used_products/[product ref key] - Count of how many are using a
                 product.
         */
        if (mIsCreator &&
                mBaseFieldsAreEditable &&
                mUserFieldsAreEditable &&
                !mInUsersUsedList &&
                mPutProductOnUsedList &&
                !mIsExistingProduct &&
                !mIsMultiUser) {

            // This is a new product, so check all fields are validated
            if (checkValidationBaseFields() && checkValidationUserFields()) {

                // Reduce bounce as we say in the electronics trade (double click) by hiding the
                // menu items once pressed.
                setMenuItemVisibility(false, false, false);

                /*
                   Set the new product to Firebase
                   See: https://firebase.google.com/docs/database/admin/save-data
                */

                // Create a reference to the '/collection/products' location 1 (as mentioned
                // above)
                final DatabaseReference collectionProductsRef = mFbCollectionProduct.push();

                // Extract the unique product ID
                Uri productReferenceUri = Uri.parse(collectionProductsRef.toString());
                mFbProductReferenceKey = productReferenceUri.getLastPathSegment();

                // Create a reference to /collection_users/[user ID]/collection_products/
                // [product ID] location 2 (as mentioned above)
                final DatabaseReference userProductRef = mFbCollectionUsers
                        .child(mUserUid)
                        .child(Constants.FB_COLLECTION_PRODUCTS)
                        .child(mFbProductReferenceKey);

                // Create a reference to the /collection_used_products/[product ref key]
                // location 3 (as mentioned above)
                final DatabaseReference usedProductRef = mFbCollectionUsedProducts
                        .child(mFbProductReferenceKey).push();

                // Extract the /used_products/user/[product reference key]
                Uri usedProductUserUri = Uri.parse(usedProductRef.toString());
                mFbUsedProductsUserKey = usedProductUserUri.getLastPathSegment();

                if (mImageAvailable &&
                        mDetailProductBinding.
                                activityDetailProductIv.
                                getDrawable() != null) {

                    // From: https://firebase.google.com/docs/storage/android/upload-files
                    // Get the data from the ImageView as bytes (as a very small image).
                    mDetailProductBinding.
                            activityDetailProductIv.
                            setDrawingCacheEnabled(true);

                    mDetailProductBinding.
                            activityDetailProductIv.
                            buildDrawingCache();

                    Bitmap bitmap = ((BitmapDrawable) mDetailProductBinding.
                            activityDetailProductIv.getDrawable()).getBitmap();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] productImage = baos.toByteArray();

                    // Create a reference to store the image
                    final StorageReference imageRef = mImageStorageReference
                            .child(mFbProductReferenceKey);

                    // Create an upload task
                    UploadTask uploadTask = imageRef.putBytes(productImage);

                    // Create a Task to get the returned URL
                    Task<Uri> urlTask = uploadTask.continueWithTask(task -> {

                        if (!task.isSuccessful()) {
                            // There is an error so delete the temp file
                            if (mCameraImageTaken) {

                                BitmapUtils.deleteImageFile(this, mTempImagePath);
                                mCameraImageTaken = false;
                            }

                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continue with the task to get the download URL
                        return imageRef.getDownloadUrl();

                    }).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            // Update the member variable with the new image URL
                            mFbStorageImageUri = task.getResult();

                            if (mCameraImageTaken) {
                                // Now it is stored in FireStore we can delete the temp file
                                BitmapUtils.deleteImageFile(this, mTempImagePath);
                                mCameraImageTaken = false;
                            }

                            /*
                               Now the image has uploaded and we have our keys  we can use them in
                               our data
                            */

                            // Create the base product information for /collection/products location
                            // Convert to a map
                            Map<String, Object> baseProductMap = convertBaseProductToMap();

                            // Save the base product (common product information twixt all users) to
                            // Firebase reference: /collection_products/[unique product reference]
                            // location 1
                            collectionProductsRef.setValue(baseProductMap);

                            // Add in the additional values and keys required
                            // for /collection/users/[user ID]/collection_products/[product ID]
                            Map<String, Object> completeProductMap =
                                    convertProductToMap();

                            // Save the full product to location 2
                            // Reference:
                            // /collection/users/[user ID]/collection_products/[product ID]
                            userProductRef.setValue(completeProductMap);

                            // Save a product reference and user to the used products
                            // reference at location 3
                            usedProductRef.setValue(mUserUid);

                            finish();
                        }
                    });
                } else {

                    // No image was taken, so just save the EditText data
                    // Create the base product information for /collection/products location
                    // Convert to a map
                    Map<String, Object> baseProductMap = convertBaseProductToMap();

                    // Save the base product (common product information twixt all users) to
                    // Firebase reference: /collection_products/[unique product reference]
                    // location 1
                    collectionProductsRef.setValue(baseProductMap);

                    // Add in the additional values and keys required
                    // for /collection/users/[user ID]/collection_products/[product ID]
                    Map<String, Object> completeProductMap = convertProductToMap();

                    // Save the full product to location 2
                    // reference: /collection/users/[user ID]/collection_products/[product ID]
                    userProductRef.setValue(completeProductMap);

                    // Save a product reference and user to the used products
                    // reference at location 3
                    usedProductRef.setValue(mUserUid);

                    finish();
                }

            } else {
                // Not all fields have been completed and validated so show an error
                Toast.makeText(this,
                        R.string.activity_detail_product_error_incomplete_fields, Toast
                                .LENGTH_LONG).show();
            }
            return;

        /*
           2. This is an existing product, created by the user who is editing it. It is not due to
              be added to the users used list as it is already in the users used product list. Both
              the base and user specific fields are editable, and the product has only one user.

              This product is being updated. The parts of this product and its users data that has
              changed need to be stored in the following locations in the database:
              1. /collection/products - Update any of the base product data that has changed
              2. /collection/users/[user ID]/collection_products/[product ID] - Update any user data
                 that has changed
              4. /collection_product_images/[product ID] - Update the image in Firestore if it has
                 been changed.

              Nothing in the users used product list will change so there is no need to update this
              part of the database
        */
        } else if (mIsExistingProduct &&
                mIsCreator &&
                !mPutProductOnUsedList &&
                mInUsersUsedList &&
                mBaseFieldsAreEditable &&
                mUserFieldsAreEditable &&
                !mIsMultiUser) {

            // Validate the product base fields
            boolean baseFieldsValidated = checkValidationBaseFields();

            // If base fields are validated...
            if (baseFieldsValidated) {

                // Validate the user product specific fields
                boolean userFieldsValidated = checkValidationUserFields();

                // If the product specific fields are validated...
                if (userFieldsValidated) {
                    // If there has been changes to the image, save them
                    // Location

                    if (mImageAvailable &&
                            mDetailProductBinding.
                                    activityDetailProductIv.getDrawable() != null) {

                        // Either:
                        // 1. A new image has been added to an existing product that did not have
                        //    an image or
                        // 2. The existing image has been modified e.g. rotated or
                        // 3. The existing image is being replaced.

                        // From: https://firebase.google.com/docs/storage/android/upload-files
                        // Get the data from the ImageView as bytes and downsize image.
                        mDetailProductBinding.
                                activityDetailProductIv.
                                setDrawingCacheEnabled(true);

                        mDetailProductBinding.
                                activityDetailProductIv.
                                buildDrawingCache();

                        Bitmap bitmap = ((BitmapDrawable) mDetailProductBinding.
                                activityDetailProductIv.
                                getDrawable()).
                                getBitmap();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                        byte[] productImage = baos.toByteArray();
                        // Case 2 & 3
                        // Create a database reference from the existing image reference
                        if (!mProduct.getFbStorageImageUri().equals("")) {

                            // Get the existing reference. If you save an image to an existing
                            // Firestore location it changes the download URL by adding a new media
                            // token, so we need to get the new download URL for all writes.
                            mImageStorageReference = mFirebaseStorage
                                    .getReferenceFromUrl(mProduct.getFbStorageImageUri());

                            // Create a new database reference for the new image
                        } else if (mProduct.getFbStorageImageUri().equals("")) {

                            // Create a reference to store the image
                            mImageStorageReference = mImageStorageReference
                                    .child(mFbProductReferenceKey);
                        }

                        // Save the new image to FireStore
                        UploadTask uploadTask = mImageStorageReference.putBytes(productImage);

                        // Create a Task to get the returned URL
                        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {

                            if (!task.isSuccessful()) {

                                // There is an error so delete the temp file
                                if (mCameraImageTaken) {
                                    BitmapUtils.deleteImageFile(getParent(), mTempImagePath);
                                    mCameraImageTaken = false;
                                }

                                throw Objects.requireNonNull(task.getException());
                            }

                            // Continue with the task to get the download URL
                            return mImageStorageReference.getDownloadUrl();

                        }).addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                // Update the member variable with the new image URL
                                mFbStorageImageUri = task.getResult();

                            /*
                               Collate any changes and update the child data in the database.
                            */

                                // mProduct (our reference product) was updated by getUsedList()
                                // when we checked to see if the product existed. We can use this
                                // instance to check for and collate any updates.
                                getProductUpdates();

                                // Create a map of any changes to the base data
                                Map<String, Object> baseProductMap = convertBaseProductToMap();

                                // Create a map of any changes to all of the product data
                                Map<String, Object> completeProductMap = convertProductToMap();

                                // If there have been updates to the base data, save them
                                if (baseProductMap != null) {

                                    // Save the changes to the products base data
                                    // Location 1
                                    saveBaseProductUpdates(baseProductMap);
                                }

                                // If there have been changes to any of the product data. save them
                                // Location 2
                                if (completeProductMap != null) {

                                    saveUserProductUpdates(completeProductMap);
                                }
                                finish();
                            }
                        });

                    } else {

                        /*
                            Collate any changes and update the child data in the database.
                         */

                        // mProduct (our reference product) was updated by getUsedList()
                        // when we checked to see if the product existed. We can use this
                        // instance to compare and collate any updates.
                        getProductUpdates();

                        // Create a map of any changes to the base data
                        Map<String, Object> baseProductMap = convertBaseProductToMap();

                        // Create a map of any changes to all of the product data
                        Map<String, Object> completeProductMap = convertProductToMap();

                        // If there have been updates to the base data, save them
                        if (baseProductMap != null) {

                            // Save the changes to the products base data
                            // Location 1
                            saveBaseProductUpdates(baseProductMap);
                        }

                        // If there have been changes to any of the product data. save them
                        // Location 2
                        if (completeProductMap != null) {

                            saveUserProductUpdates(completeProductMap);
                        }
                        finish();
                    }
                }
            }
            return;

        /*
           3. This is an existing product, created by the user who is editing it. It is not due to
              be added to the users used list because it is already in the users used list. As it is
              used by more than one person the creator is no longer allowed to edit its base
              information. As such base fields are not editable however the user fields are, as the
              data they contain is specific to each user.

              This product is having its user data updated which will only change in one location in
              the database:
              1. /collection_users/[user ID]/collection_products/[product ID] - Update user data
        */
        } else if (mIsExistingProduct &&
                mIsCreator &&
                !mPutProductOnUsedList &&
                mInUsersUsedList &&
                mIsMultiUser &&
                !mBaseFieldsAreEditable &&
                mUserFieldsAreEditable) {

            // Validate the user product specific fields
            boolean userFieldsValidated = checkValidationUserFields();

            if (userFieldsValidated) {

                /*
                   Collate any changes and update the child data in the database.
                */

                // mProduct (our reference product) was updated by getUsedList() when we checked
                // to see if the product existed. We can use this instance to check for and
                // collate any updates.
                Map<String, Object> productUserUpdates = getUserUpdates();

                // If there are updates save them to the database
                // Location 1
                if (productUserUpdates != null) {
                    saveUserProductUpdates(productUserUpdates);
                }
            }
            finish();

        /*
           4. This is an existing product, not created by the user who is editing it. It is not
              currently in the users used list but it is due to be added. In this instance it is not
              relevant how many users use this product, so we do not include the multi-user boolean.
              As the user did not create this product the base fields are not editable however the
              user specific fields are editable and have been verified.

              This product has been created by another user and this user is adding it to their
              list. We will need to add two entries to the database to locations 1 and 3:

              1. /collection_users/[User ID]/collection_products/[product ID] - Add the product to
                 the users used list.
              3. /collection_used_products/[product ref key]/[used products user key]/[user id] -
                 Add the users ID to the used product reference document.
        */
        } else if (mIsExistingProduct &&
                !mIsCreator &&
                !mInUsersUsedList &&
                mPutProductOnUsedList &&
                !mBaseFieldsAreEditable &&
                mUserFieldsAreEditable) {

            // Validate the user product specific fields
            boolean userFieldsValidated = checkValidationUserFields();

            if (userFieldsValidated) {

                /*
                   Collate any changes and update the child data in the database.
                */

                // mProduct (our reference product) was updated by getUsedList() when we checked
                // to see if the product existed. We can use this instance to check for and
                // collate any updates. In theory, all fields should be updated in this instance,
                // therefore we should not need to perform this task. However we still need to
                // produce an object map and checking for changes does this for us.
                Map<String, Object> productUserUpdates = getUserUpdates();

                // If there are updates save them to the database
                if (productUserUpdates != null) {

                    // The current user is adding this product to their used product list therefore
                    // we need to add this users ID to:
                    // /collection_used_products/[product ref key]/[used products user key]/[user id]
                    // and update fbUsedProductUserID
                    // Location 3
                    addUserToUsedProducts();

                    // Add in the reference provided by addUserToUsedProducts
                    productUserUpdates.put(
                            Constants.PRODUCT_MY_FB_USED_PRODUCT_KEY, mFbUsedProductsUserKey);

                    // Location 1
                    saveUserProductUpdates(productUserUpdates);
                }
            }
            finish();

        /*
           5. This is an existing product, not created by the user who is editing it. It is
              currently in the users used list so it is not due to be added. In this instance it is
              not relevant how many users use this product, so we do not include the multi-user
              boolean. As the user did not create this product the base fields are not editable
              however the user specific fields are editable and have been verified.

              This product has been created by another user and this user has it on their list and
              is updating the user specific information. We will need to edit entries at one
              location in the database:

              1. /collection_users/[User ID]/collection_products/[product ID] - Perform the child
                 updates.
        */
        } else if (mIsExistingProduct &&
                !mIsCreator &&
                mInUsersUsedList &&
                !mPutProductOnUsedList &&
                !mBaseFieldsAreEditable &&
                mUserFieldsAreEditable) {

            // Validate the users input fields
            boolean userFieldsValidated = checkValidationUserFields();

            if (userFieldsValidated) {

                // Create a HashMap
                Map<String, Object> productUserUpdates = getUserUpdates();

                // If there are updates save them to the database
                if (productUserUpdates != null) {
                    // Location 1
                    saveUserProductUpdates(productUserUpdates);
                }
            }
            finish();

        /*
           6. This is an existing product, created by the user who is editing it. It is
              not currently in the users used list and due to be added. In this instance it is
              not relevant how many users use this product, so we do not include the multi-user
              boolean. As the user created this product the base fields are not editable
              however the user specific fields are editable and have been verified.

              This product has been created by this user and this user has previously deleted this
              product from their list. They are now adding it back and updating the user
              specific data. We will need to edit entries at two locations in the database:

              1. /collection_users/[User ID]/collection_products/[product ID] - Perform the child
                 updates (location 2).
              3. /collection/used_products/[product ref key] - Count of how many are using a product
                 (location 3)
        */
        } else if (mIsExistingProduct &&
                mIsCreator &&
                !mInUsersUsedList &&
                mPutProductOnUsedList &&
                !mBaseFieldsAreEditable &&
                mUserFieldsAreEditable) {

            // Validate the users input fields
            boolean userFieldsValidated = checkValidationUserFields();

            if (userFieldsValidated) {

                // Create a HashMap
                Map<String, Object> productUserUpdates = getUserUpdates();

                // If there are updates save them to the database
                if (productUserUpdates != null) {

                    // Location 3 - This needs to be completed first as we need the reference
                    addUserToUsedProducts();

                    // Add in the reference provided by addUserToUsedProducts
                    productUserUpdates.put(
                            Constants.PRODUCT_MY_FB_USED_PRODUCT_KEY, mFbUsedProductsUserKey);

                    // Location 2
                    saveUserProductUpdates(productUserUpdates);
                }
            }
        }
        finish();
    }

    /**
     * Checks for updates to the product data and updates our reference version of mProduct with
     * the new values
     */
    private void getProductUpdates() {

        // Add any changes to the base product information
        if (!mProduct.getDescription().equals(mDescription)) {
            mProduct.setDescription(mDescription);
        }
        if (!mMadeBy.equals(mProduct.getMadeBy())) {
            mProduct.setMadeBy(mMadeBy);
        }
        if (mCategory != mProduct.getCategory()) {
            mProduct.setCategory(mCategory);
        }
        if (mShelfLife != mProduct.getShelfLife()) {
            mProduct.setShelfLife(mShelfLife);
        }
        if (mPackSize != mProduct.getPackSize()) {
            mProduct.setPackSize(mPackSize);
        }
        if (mUnitOfMeasure != mProduct.getUnitOfMeasure()) {
            mProduct.setUnitOfMeasure(mUnitOfMeasure);
        }
        if (mPackPriceAverage != mProduct.getPackPriceAverage()) {
            mProduct.setPackPriceAverage(mPackPriceAverage);
        }
        if (!mFbStorageImageUri.toString().equals(mProduct.getFbStorageImageUri())) {
            mProduct.setFbStorageImageUri(mFbStorageImageUri.toString());
        }

        // Start of user updates
        if (!mFbProductReferenceKey.equals(mProduct.getFbProductReferenceKey())) {
            mProduct.setFbProductReferenceKey(mFbProductReferenceKey);
        }
        if (!mFbUsedProductsUserKey.equals(mProduct.getFbUsedProductsUserKey())) {
            mProduct.setFbUsedProductsUserKey(mFbUsedProductsUserKey);
        }
        if (!mRetailer.equals(mProduct.getRetailer())) {
            mProduct.setRetailer(mRetailer);
        }
        if (!mLocationRoom.equals(mProduct.getLocationRoom())) {
            mProduct.setLocationRoom(mLocationRoom);
        }
        if (!mLocationInRoom.equals(mProduct.getLocationInRoom())) {
            mProduct.setLocationInRoom(mLocationInRoom);
        }
        if (mPackPrice != mProduct.getPackPrice()) {
            mProduct.setPackPrice(mPackPrice);
        }
        if (!mLocalImageUri.toString().equals(mProduct.getLocalImageUri())) {
            mProduct.setLocalImageUri(mLocalImageUri.toString());
        }
        // No need to check packPriceAve as it is automatically calculated
        // No need to check createdBy as it cannot be modified once created
    }

    /* Checks for updates to the user specific product data */
    private Map<String, Object> getUserUpdates() {

        // Now for the user specific information, create a HashMap to store the changes
        // We store the whole product in the /collection_user/[uid]/collection_products location
        Map<String, Object> productUserUpdates = new HashMap<>();

        // Create a counter to see how many updates there are, if any.
        int userUpdateCounter = 0;

        /* Add the base data to the HashMap. */
        productUserUpdates.put(
                Constants.PRODUCT_COMM_DESCRIPTION_KEY,
                mProduct.getDescription());
        productUserUpdates.put(
                Constants.PRODUCT_COMM_MADE_BY_KEY,
                mProduct.getMadeBy());
        productUserUpdates.put(
                Constants.PRODUCT_COMM_CATEGORY_KEY,
                mProduct.getCategory());
        productUserUpdates.put(
                Constants.PRODUCT_COMM_SHELF_LIFE_KEY,
                mProduct.getShelfLife());
        productUserUpdates.put(
                Constants.PRODUCT_COMM_PACK_SIZE_KEY,
                mProduct.getPackSize());
        productUserUpdates.put(
                Constants.PRODUCT_COMM_UNIT_OF_MEASURE_KEY,
                mProduct.getUnitOfMeasure());
        productUserUpdates.put(
                Constants.PRODUCT_COMM_PRICE_AVE_KEY, mProduct.getPackPrice());
        productUserUpdates.put(
                Constants.PRODUCT_COMM_CREATED_BY_KEY,
                mProduct.getCreatedBy());

        // Add in the user specific data to the HashMap
        productUserUpdates.put(
                Constants.PRODUCT_MY_LOCAL_IMAGE_URI_KEY,
                mProduct.getLocalImageUri());
        productUserUpdates.put(
                Constants.PRODUCT_MY_FB_STORAGE_IMAGE_URI_KEY,
                mProduct.getFbStorageImageUri());
        productUserUpdates.put(
                Constants.PRODUCT_MY_FB_REFERENCE_KEY,
                mProduct.getFbProductReferenceKey());
        productUserUpdates.put(
                Constants.PRODUCT_MY_FB_USED_PRODUCT_KEY,
                mProduct.getFbUsedProductsUserKey());

        // Compare and add any changes from the user specific fields to the HashMap
        if (!mRetailer.equals(mProduct.getRetailer())) {
            productUserUpdates.put(Constants.PRODUCT_MY_RETAILER_KEY, mRetailer);
            mProduct.setRetailer(mRetailer);
            userUpdateCounter++;
        }
        if (!mLocationRoom.equals(mProduct.getLocationRoom())) {
            productUserUpdates.put(Constants.PRODUCT_MY_LOCATION_ROOM_KEY, mLocationRoom);
            mProduct.setLocationRoom(mLocationRoom);
            userUpdateCounter++;
        }
        if (!mLocationInRoom.equals(mProduct.getLocationInRoom())) {
            productUserUpdates.put(Constants.PRODUCT_MY_LOCATION_IN_ROOM_KEY, mLocationInRoom);
            mProduct.setLocationInRoom(mLocationInRoom);
            userUpdateCounter++;
        }
        if (mPackPrice != mProduct.getPackPrice()) {
            productUserUpdates.put(Constants.PRODUCT_MY_PACK_PRICE_KEY, mPackPrice);
            mProduct.setPackPrice(mPackPrice);
            userUpdateCounter++;
        }

        // If there have been updates return the updated Map
        if (userUpdateCounter > 0) {
            return productUserUpdates;
        }
        // If there are no changes return null
        return null;
    }

    /* Saves any product user updates to the database */
    private void saveUserProductUpdates(Map<String, Object> productUserUpdates) {

        // Make a reference to the users products
        DatabaseReference userProductRef = mFbCollectionUsers
                .child(mUserUid)
                .child(Constants.FB_COLLECTION_PRODUCTS)
                .child(mFbProductReferenceKey);

        // Save the changes to the users products
        userProductRef.updateChildren(productUserUpdates);
    }

    /* Saves any product base updates to the database */
    private void saveBaseProductUpdates(Map<String, Object> productBaseUpdates) {

        // Update the product specific information in Firebase
        DatabaseReference reference = mFbCollectionProduct
                .child(mFbProductReferenceKey);

        reference.updateChildren(productBaseUpdates);
    }

    /*
    Adds the current user ID to:
    /collection_used_products/[product ref key]/[used products user key]/[user id]
    */
    private void addUserToUsedProducts() {

        // Create the database reference
        DatabaseReference usedProductRef = mFbCollectionUsedProducts
                .child(mFbProductReferenceKey).push();

        // Extract the /used_products/user/[product reference key]
        Uri usedProductUserUri = Uri.parse(usedProductRef.toString());
        mFbUsedProductsUserKey = usedProductUserUri.getLastPathSegment();

        // Add the users ID
        usedProductRef.setValue(mUserUid);
    }

    /* Validates the user input from the products base (user common) fields */
    private boolean checkValidationBaseFields() {

        // If the 'description' field is not validated,
        if (!mDescriptionIsValidated) {

            // validate it.
            validateDescription(mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableEtDescription.
                    getText().toString().trim());

            // If the 'made by' field is not validated,
        } else if (!mMadeByIsValidated) {

            // validate it.
            validateMadeBy(mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableEtMadeBy.
                    getText().toString().trim());

            // If the 'pack size' field is not validated,
        } else if (!mPackSizeIsValidated) {

            // validate it.
            validatePackSize(String.valueOf(mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableEtPackSize.
                    getText().toString().trim()));

            // If the 'unit of measure' field is not validated,
        } else if (!mUoMIsValidated) {

            // validate it.
            mUnitOfMeasure = mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableSpinnerUoM.
                    getSelectedItemPosition();
            validateUnitOfMeasure();

            // If he 'shelf life' field is not validated,
        } else if (!mShelfLifeIsValidated) {

            // validate it.
            mShelfLife = mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableSpinnerShelfLife.
                    getSelectedItemPosition();
            validateShelfLife();

            // If the 'category' field is not validated,
        } else if (!mCategoryIsValidated) {

            // validate it.
            mCategory = mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableSpinnerCategory.
                    getSelectedItemPosition();
            validateCategory();
        }

        return mDescriptionIsValidated &&
                mMadeByIsValidated &&
                mPackSizeIsValidated &&
                mUoMIsValidated &&
                mShelfLifeIsValidated &&
                mCategoryIsValidated;
    }

    /* Validates the product fields that are specific to each user */
    private boolean checkValidationUserFields() {

        // If the 'retailer' field is not validated,
        if (!mRetailerIsValidated) {

            // validate it.
            validateRetailer(mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtRetailer
                    .getText().toString().trim());

        // If the 'pack price' field is not validated,
        } else if (!mPackPriceIsValidated &&
                !TextUtils.isEmpty(mDetailProductBinding.
                        activityDetailProductUserFieldsEditableInclude.
                        activityDetailProductEtPrice.
                        getText().toString())) {

            // validate it.
            validatePackPrice();

        // If the 'location room' is not validated,
        } else if (!mLocationRoomIsValidated) {

            // validate it
            validateLocationRoom(mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtLocationRoom
                    .getText().toString().trim());

        // If the 'location in room' is not validated,
        } else if (!mLocationInRoomIsValidated) {

            // validate it.
            validateLocationInRoom(mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtLocationInRoom.
                    getText().toString().trim());
        }

        return mRetailerIsValidated &&
                mPackPriceIsValidated &&
                mLocationRoomIsValidated &&
                mLocationInRoomIsValidated;
    }

    /* Converts the products base data to an object map */
    // Todo - use the mapping in the product class
    private Map<String, Object> convertBaseProductToMap() {

        // Create the base product information for /collection/products location
        Product newProductData = new Product();
        newProductData.setDescription(mDescription);
        newProductData.setMadeBy(mMadeBy);
        newProductData.setCategory(mCategory);
        newProductData.setShelfLife(mShelfLife);
        newProductData.setPackSize(mPackSize);
        newProductData.setUnitOfMeasure(mUnitOfMeasure);
        newProductData.setPackPriceAverage(Constants.DEFAULT_PRODUCT_PRICE_AVERAGE);
        newProductData.setCreatedBy(mUserUid);
        newProductData.setFbStorageImageUri(mFbStorageImageUri.toString());

        // Convert to a map
        return newProductData.commProductToMap();
    }

    /* Given the base updates, converts the entire product data to a map object */
    // Todo - use the mapping in the product class
    private Map<String, Object> convertProductToMap() {

        Product newProductData = new Product();

        // Add in the base data
        newProductData.setDescription(mDescription);
        newProductData.setMadeBy(mMadeBy);
        newProductData.setCategory(mCategory);
        newProductData.setShelfLife(mShelfLife);
        newProductData.setPackSize(mPackSize);
        newProductData.setUnitOfMeasure(mUnitOfMeasure);
        newProductData.setPackPriceAverage(Constants.DEFAULT_PRODUCT_PRICE_AVERAGE);
        newProductData.setCreatedBy(mUserUid);
        newProductData.setFbStorageImageUri(mFbStorageImageUri.toString());

        // Add in the additional values and keys
        newProductData.setFbProductReferenceKey(mFbProductReferenceKey);
        newProductData.setFbUsedProductsUserKey(mFbUsedProductsUserKey);
        newProductData.setRetailer(mRetailer);
        newProductData.setLocationRoom(mLocationRoom);
        newProductData.setLocationInRoom(mLocationInRoom);
        newProductData.setPackPrice(mPackPrice);
        newProductData.setLocalImageUri(mLocalImageUri.toString());

        // Convert to Map
        return newProductData.userFieldsToMap();
    }

    /* Called when updating a products data. There are four states to deal with:
     * - Base fields editable
     * - Base fields uneditable
     * - User fields editable
     * - User fields uneditable
     * */
    private void populateUi() {

        // If there is an image Uri present, load the image
        if (!Uri.EMPTY.equals(Uri.parse(mProduct.getFbStorageImageUri()))) {
            Picasso.
                    get().
                    load(mProduct.getFbStorageImageUri()).
                    into(mDetailProductBinding.
                            activityDetailProductIv);
        }

        // Base fields editable criteria:
        // 1. Has to be creator of product
        // 2. Base fields have to be editable
        if (mIsCreator &&
                mBaseFieldsAreEditable) {

            // Update the EditText fields for the base product information
            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableEtDescription.
                    setText(mProduct.getDescription());

            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableEtMadeBy.
                    setText(mProduct.getMadeBy());

            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableEtPackSize.
                    setText(String.valueOf(mProduct.getPackSize()));

            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableSpinnerUoM.
                    setSelection(mProduct.getUnitOfMeasure());

            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableSpinnerShelfLife.
                    setSelection(mProduct.getShelfLife());

            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableSpinnerCategory.
                    setSelection(mProduct.getCategory());

        // Base fields uneditable criteria:
        // 1. View has to be visible
        } else if (!mBaseFieldsAreEditable) {

            // Update the TextViews
            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableTvDescription.
                    setText(mProduct.getDescription());

            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableTvMadeBy.
                    setText(mProduct.getMadeBy());

            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableTvPackSize.
                    setText(String.valueOf(mProduct.getPackSize()));

            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableTvUoM.
                    setText(Converters.getUnitOfMeasureString(
                    this, mProduct.getUnitOfMeasure()));

            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableTvShelfLife.
                    setText(Converters.getShelfLifeString(
                    this, mProduct.getShelfLife()));

            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableTvCategory.
                    setText(Converters.getCategoryString(
                    this, mProduct.getCategory()));
        }

        // User specific fields editable criteria:
        // 1. Has to be on the users used product list or about to be on it
        // 2. User fields have to be editable
        if ((mInUsersUsedList ||
                mPutProductOnUsedList) &&
                mUserFieldsAreEditable) {

            mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtRetailer.
                    setText(mProduct.getRetailer());

            mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtPrice.
                    setText(String.valueOf(mProduct.getPackPrice()));

            mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtLocationRoom.
                    setText(mProduct.getLocationRoom());

            mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtLocationInRoom.
                    setText(mProduct.getLocationInRoom());

            // Set the menu items for this UI state
            invalidateOptionsMenu();

        // User specific fields uneditable container criteria:
        // 1. Has to be in the users used product list.
        // 2. Uneditable status has to be true.
        // Todo - Why  are mUserFieldsAreEditable is always false, this may be correct behaviour
        } else if (mInUsersUsedList &&
                !mUserFieldsAreEditable) {

            // Update the uneditable views
            mDetailProductBinding.
                    activityDetailProductUserFieldsUneditableInclude.
                    activityDetailProductUserFieldsUneditableTvRetailer.
                    setText(mProduct.getRetailer());

            NumberFormat format = NumberFormat.getCurrencyInstance();
            mDetailProductBinding.
                    activityDetailProductUserFieldsUneditableInclude.
                    activityDetailProductUserFieldsUneditableTvPrice.
                    setText(String.valueOf(format.format(mProduct.getPackPrice())));

            mDetailProductBinding.
                    activityDetailProductUserFieldsUneditableInclude.
                    activityDetailProductUserFieldsUneditableTvLocationRoom.
                    setText(mProduct.getLocationRoom());

            mDetailProductBinding.
                    activityDetailProductUserFieldsUneditableInclude.
                    activityDetailProductUserFieldsUneditableTvLocationInRoom.
                    setText(mProduct.getLocationInRoom());

            // This product is all ready in the users used list so remove the FAB
            mDetailProductBinding.
                    activityDetailProductFab.
                    setVisibility(View.GONE);

            // This call invalidates the current menu options and calls onPrepareOptionsMenu()
            // which will in turn populate the menu with the correct icons for this view.
            invalidateOptionsMenu();
        }
    }

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
                .PRODUCT_ON_USED_LIST_KEY, mInUsersUsedList);
        outState.putBoolean(Constants
                .COMM_FIELDS_EDITABLE_STATUS_KEY, mBaseFieldsAreEditable);
        outState.putBoolean(Constants
                .MY_CUSTOM_FIELDS_EDITABLE_STATUS_KEY, mUserFieldsAreEditable);

    }

    /* Get a reference to all of the views */
    private void initialiseViews() {

        // Assign the DataBinding classes a binding instance for each of the layouts used by this
        // class.
        mDetailProductBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_detail_product);

        // Setup the Toolbar
        setSupportActionBar(mDetailProductBinding.activityDetailProductTbTop);

        setupUoMSpinner();
        setupShelfLifeSpinner();
        setupCategorySpinner();

        // Initial visibility for editable and uneditable base field containers
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableContainer.
                setVisibility(View.GONE);

        // Update the containers visibility state tracking boolean
        mBaseFieldsAreEditable = false;

        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductUserFieldsEditableContainer.
                setVisibility(View.GONE);

        // Update the containers visibility state tracking boolean
        mUserFieldsAreEditable = false;

        mDetailProductBinding.
                activityDetailProductBaseFieldsUneditableInclude.
                activityDetailProductBaseFieldsUneditableContainer.
                setVisibility(View.GONE);

        mDetailProductBinding.
                activityDetailProductUserFieldsUneditableInclude.
                activityDetailProductUserFieldsUneditableContainer.
                setVisibility(View.GONE);

        /*
            Here follows the click listeners for the various input fields.
        */

        /* OnClickListener for the add picture by camera button */
        mDetailProductBinding.activityDetailProductIbAddCameraPicture.
                setOnClickListener(v -> requestPermissions());

        /* OnClickListener for the rotate picture button */
        mDetailProductBinding.activityDetailProductIbRotatePicture.
                setOnClickListener(v -> rotateImage());

        /* onClickListener for the FAB */
        mDetailProductBinding.activityDetailProductFab.
                setOnClickListener(v -> openProductUserDataFields());

        /* onClickListener for the add image from gallery button */
        mDetailProductBinding.activityDetailProductIbAddGalleryPicture.
                setOnClickListener(v -> launchGallery());

        /*
        Here follows the change listeners for the various input fields.
        */

        // Text change listener for the 'description' EditText field
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtDescription.
                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // Validate the text as it changes
                        mDescriptionIsValidated = validateDescription(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

        // Focus change listener for the 'description' EditText field */
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtDescription.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    mDescriptionIsValidated = !hasFocus && validateDescription(
                            mDetailProductBinding.
                                    activityDetailProductBaseFieldsEditableInclude.
                                    activityDetailProductBaseFieldsEditableEtDescription
                                    .getText()
                                    .toString());
                });

        // Change listener for the 'made by' EditText field
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtMadeBy.
                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mMadeByIsValidated = validateMadeBy(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

        // Focus change listener for the 'made by' EditText field */
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtMadeBy.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    mMadeByIsValidated = !hasFocus && validateMadeBy(
                            mDetailProductBinding.
                                    activityDetailProductBaseFieldsEditableInclude.
                                    activityDetailProductBaseFieldsEditableEtMadeBy
                                    .getText()
                                    .toString());
                });

        // Change listener for the 'pack size' EditText field
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtPackSize.
                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mPackSizeIsValidated = validatePackSize(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

        // Focus change listener for the 'pack size' EditText field */
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableEtPackSize.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    mPackSizeIsValidated = !hasFocus && validatePackSize(
                            mDetailProductBinding.
                                    activityDetailProductBaseFieldsEditableInclude.
                                    activityDetailProductBaseFieldsEditableEtPackSize.
                                    getText().
                                    toString());
                });

        // Focus change listener for the 'unit of measure' spinner
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableContainerUoM.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    if (hasFocus) {
                        mUoMIsValidated = false;
                        hideKeyboard();

                        mDetailProductBinding.
                                activityDetailProductBaseFieldsEditableInclude.
                                activityDetailProductBaseFieldsEditableContainerUoM.
                                performClick();

                    } else {

                        // Validate its contents
                        mUoMIsValidated = validateUnitOfMeasure();

                        // If validated remove the error
                        if (mUoMIsValidated) {
                            mDetailProductBinding.
                                    activityDetailProductBaseFieldsEditableInclude.
                                    activityDetailProductBaseFieldsEditableTvSpinnerUoMSetError.
                                    setError(null);
                        }
                    }
                });

        // Focus change listener for the 'shelf life' spinner
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableSpinnerShelfLife.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    if (hasFocus) {

                        mShelfLifeIsValidated = false;
                        hideKeyboard();
                        mDetailProductBinding.
                                activityDetailProductBaseFieldsEditableInclude.
                                activityDetailProductBaseFieldsEditableSpinnerShelfLife.
                                performClick();
                    } else {
                        // Validate its contents
                        mShelfLifeIsValidated = validateShelfLife();
                    }
                });

        // Focus change listener for the 'category' spinner
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableSpinnerCategory.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    if (hasFocus) {
                        mCategoryIsValidated = false;
                        hideKeyboard();
                        mDetailProductBinding.
                                activityDetailProductBaseFieldsEditableInclude.
                                activityDetailProductBaseFieldsEditableSpinnerCategory.
                                performClick();
                    } else {
                        // Validate its contents
                        mCategoryIsValidated = validateCategory();
                    }
                });

        // Change listener for the 'made by' EditText field
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtRetailer.
                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mRetailerIsValidated = validateRetailer(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

        // Focus change listener for the 'retailer' EditText field */
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtRetailer.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    mRetailerIsValidated = !hasFocus && validateRetailer(
                            mDetailProductBinding.
                                    activityDetailProductUserFieldsEditableInclude.
                                    activityDetailProductEtRetailer.
                                    getText().
                                    toString()
                                    .trim());
                });

        // Focus change listener for the 'pack price' EditText field
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtPrice.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    mPackPriceIsValidated = !hasFocus && validatePackPrice();
                });

        // Focus change listener for the 'location room' EditText field
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtLocationRoom.
                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mLocationRoomIsValidated = validateLocationRoom(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

        // Focus change listener for the 'location room' EditText field */
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtLocationRoom.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    mLocationRoomIsValidated = !hasFocus && validateLocationRoom(
                            mDetailProductBinding.
                                    activityDetailProductUserFieldsEditableInclude.
                                    activityDetailProductEtLocationRoom.
                                    getText().
                                    toString().
                                    trim());
                });

        // Focus change listener for the 'location in room' EditText field
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtLocationInRoom.
                addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mLocationInRoomIsValidated = validateLocationInRoom(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

        // Focus change listener for the 'location in room' EditText field */
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtLocationInRoom.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    mLocationInRoomIsValidated = !hasFocus && validateLocationInRoom(
                            mDetailProductBinding.
                                    activityDetailProductUserFieldsEditableInclude.
                                    activityDetailProductEtLocationInRoom.
                                    getText().
                                    toString().
                                    trim());
                });
    }

    private void launchGallery() {
        // Launch an intent to open a gallery app
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

        startActivityForResult(Intent.createChooser(
                intent, getString(R.string.intent_gallery_picker_title)),
                Constants.REQUEST_IMAGE_PICKER);
    }

    /*
    Opens the product user data fields for a product that is being added to this users product
    used list
    */
    private void openProductUserDataFields() {

        // If the current product is not in the users used list, pressing the FAB opens up the
        // user specific fields, so the user can edit them.
        if (!mInUsersUsedList) {

            // Make the user specific fields visible
            mUserFieldsAreEditable = true;
            updateUserFieldsEditableStatus();

            // This product is going to be added to the users used list so update the bool
            mPutProductOnUsedList = true;

            // Turn off the FAB
            mDetailProductBinding.
                    activityDetailProductFab.
                    setVisibility(View.GONE);

            // In the user specific fields set the focus to the first field
            mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductEtRetailer.
                    requestFocus();

            // Show the save button
            setMenuItemVisibility(true, false, false);
        }
    }

    /* Rotates the image in the image view */
    private void rotateImage() {

        // Rotate the image by 90 degrees
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap RecipeImage = ((BitmapDrawable) mDetailProductBinding.
                activityDetailProductIv.
                getDrawable()).
                getBitmap();

        Bitmap rotated = Bitmap.createBitmap(RecipeImage, 0, 0,
                RecipeImage.getWidth(), RecipeImage.getHeight(), matrix, true);

        mDetailProductBinding.
                activityDetailProductIv.
                setImageBitmap(rotated);

        // The image has changed so update the bool
        mImageAvailable = true;
    }

    /*
       Hides the keyboard. Attrib:
       https://stackoverflow.com/questions/6443212/spinner-does-not-get-focus
    */
    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus())
                            .getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /* Sets up the visibility properties for an existing uneditable product */
    private void showUneditableViewContainers() {

        // This is for product viewing only, so no need for editable views
        mDetailProductBinding.
                activityDetailProductBaseFieldsEditableInclude.
                activityDetailProductBaseFieldsEditableContainer.
                setVisibility(View.GONE);

        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductUserFieldsEditableContainer
                .setVisibility(View.GONE);

        // Make relevant view containers visible
        mDetailProductBinding.activityDetailProductBaseFieldsUneditableInclude.
                activityDetailProductBaseFieldsUneditableContainer.
                setVisibility(View.VISIBLE);

        // Only show the 'user specific product data container' if in used list
        if (mInUsersUsedList) {

            mDetailProductBinding.
                    activityDetailProductUserFieldsUneditableInclude.
                    activityDetailProductUserFieldsUneditableContainer.
                    setVisibility(View.VISIBLE);
        } else {

            // This product is not in the users used list so we have no information as to how they
            // use it. So set the user fields to gone.
            mDetailProductBinding.
                    activityDetailProductUserFieldsUneditableInclude.
                    activityDetailProductUserFieldsUneditableContainer.
                    setVisibility(View.GONE);

            // Show the fab so they can add this product to their list if they wish.
            mDetailProductBinding.
                    activityDetailProductFab.
                    setVisibility(View.VISIBLE);
        }
    }

    /* Sets the visibility and editable properties for the base field views */
    private void updateBaseFieldsEditableStatus() {

        if (mBaseFieldsAreEditable) {

            // Base fields are editable, set the editable container to visible.
            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableContainer.
                    setVisibility(View.VISIBLE);

            // We are now editing as opposed to viewing so set the uneditable container to gone.
            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableContainer.
                    setVisibility(View.GONE);
        } else {

            // Base fields are uneditable, so set the editable fields to gone.
            mDetailProductBinding.
                    activityDetailProductBaseFieldsEditableInclude.
                    activityDetailProductBaseFieldsEditableContainer.
                    setVisibility(View.GONE);

            // And set the uneditable container to visible.
            mDetailProductBinding.
                    activityDetailProductBaseFieldsUneditableInclude.
                    activityDetailProductBaseFieldsUneditableContainer.
                    setVisibility(View.VISIBLE);
        }
    }

    /* Sets the visibility properties for the editable and non editable user specific views */
    private void updateUserFieldsEditableStatus() {

        if (mUserFieldsAreEditable) {

            mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductUserFieldsEditableContainer.
                    setVisibility(View.VISIBLE);

            mDetailProductBinding.
                    activityDetailProductUserFieldsUneditableInclude.
                    activityDetailProductUserFieldsUneditableContainer.
                    setVisibility(View.GONE);

        } else {

            mDetailProductBinding.
                    activityDetailProductUserFieldsEditableInclude.
                    activityDetailProductUserFieldsEditableContainer.
                    setVisibility(View.GONE);

            // If the product is on or is going to be on the users used list turn the user
            // specific fields on
            if (mInUsersUsedList || mPutProductOnUsedList) {

                mDetailProductBinding.
                        activityDetailProductUserFieldsUneditableInclude.
                        activityDetailProductUserFieldsUneditableContainer.
                        setVisibility(View.VISIBLE);

            } else {

                // If not on the users used list turn the view off
                mDetailProductBinding.
                        activityDetailProductUserFieldsUneditableInclude.
                        activityDetailProductUserFieldsUneditableContainer.
                        setVisibility(View.GONE);
            }
        }
    }

    /* Delete the product */
    private void deleteProduct() {

        /*
           This is an existing product. So there are up to four locations where data needs to
           be deleted:
           1. Delete the product form the users used product list which is located at
              /collection_users/[user id]/collection_products/[product id]
           2. Delete the entry for this product from this user in the used_products reference
           3. If this is the only user using this product, delete the product from
              /collection_products/
           4. If this is the only user using this product mFbStorageUri and is not empty, delete
              the image in FireStore it is pointing to.
        */

        if (mIsExistingProduct && mInUsersUsedList) {

            // For the product information located under the users product list, create a database
            // reference that needs removing
            DatabaseReference userProductRef = mFbCollectionUsers
                    .child(mUserUid)
                    .child(Constants.FB_COLLECTION_PRODUCTS)
                    .child(mFbProductReferenceKey);

            // Then remove the value
            userProductRef.removeValue().addOnCompleteListener(task -> {

                // For the product information located under the 'used_products'
                // collection, create the database reference that needs removing.
                DatabaseReference usedProductsRef = mFbCollectionUsedProducts
                        .child(mFbProductReferenceKey)
                        .child(mProduct.getFbUsedProductsUserKey());

                // Then remove the value.
                usedProductsRef.removeValue().addOnCompleteListener(task1 -> {

                    /*
                       Check to see if this product is being used by anyone else. If not remove it
                       from the database.
                    */

                    // Get a reference to: /collection_used_products/[product ID]/[used product ref].
                    Query usedProductRef = mFbCollectionUsedProducts
                            .child(mFbProductReferenceKey).limitToFirst(2);

                    usedProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // If getValue() is null there are no other users using this product, it
                            // is therefore safe to delete it from /collection_products/
                            if (dataSnapshot.getValue() == null) {

                                DatabaseReference deleteProductRef = mFbCollectionProduct
                                        .child(mFbProductReferenceKey);

                                // Remove the value.
                                deleteProductRef.removeValue();

                                // If there is an image stored with this product, delete it.
                                if (!TextUtils.isEmpty(mFbStorageImageUri.toString())) {

                                    StorageReference imageRef = mFirebaseStorage
                                            .getReferenceFromUrl(mFbStorageImageUri.toString());
                                    imageRef.delete();
                                }
                                finish();
                            } else {
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                });
            });
        }
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

        // If a camera image is taken
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {

            // If an image capture
            processAndSetImage();
            mImageAvailable = true;
            mCameraImageTaken = true;

        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_CANCELED) {

            // If the camera was cancelled, delete the temporary file
            BitmapUtils.deleteImageFile(this, mTempImagePath);
            mCameraImageTaken = false;

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_OK) {

            mImageAvailable = true;
            processAndSetImage();

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_CANCELED) {

            Log.e(LOG_TAG, "Media store intent cancelled");

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER && resultCode == RESULT_OK) {

            // Photo picker
            Uri selectedImageUri = data.getData();
            mImageAvailable = true;
            mDetailProductBinding.
                    activityDetailProductIv.
                    setImageURI(selectedImageUri);

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER && resultCode == RESULT_CANCELED) {

            Log.e(LOG_TAG, "Image picker intent cancelled");
        }
    }

    /* Resample's the image so it fits our imageView and uses less resources */
    private void processAndSetImage() {

        Bitmap mResultsBitmap = BitmapUtils.resampleImage(this, null, mTempImagePath);

        mDetailProductBinding.
                activityDetailProductIv.
                setImageBitmap(mResultsBitmap);
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

        Spinner UoMSpinner = findViewById(
                R.id.activity_detail_product_base_fields_editable_spinner_UoM);

        // Create an adapter for the spinner. The list options are from the String array in
        // arrays.xml. The spinner will use the default layout.
        ArrayAdapter UoMSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_UoM_metric_options, R.layout.list_item_spinner);

        // Specify dropdown layout style
        UoMSpinnerAdapter.setDropDownViewResource(R.layout.list_item_spinner);

        // Apply the adapter to the spinner
        UoMSpinner.setAdapter(UoMSpinnerAdapter);

        // Add the spinner to the list of focusable items, so that when the user clicks the next
        // button on the keyboard the spinner is focusable
        UoMSpinner.setFocusable(true);

        UoMSpinner.setFocusableInTouchMode(true);

        // Set the integer mUoM to the constant values
        UoMSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

                        // Validate the input
                        mUoMIsValidated = validateUnitOfMeasure();
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

        Spinner shelfLifeSpinner = findViewById(
                R.id.activity_detail_product_base_fields_editable_spinner_shelf_life);

        // Create an adapter for the spinner. The list options are from the String array in
        // arrays.xml. The spinner will use the default layout
        ArrayAdapter shelfLifeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_shelf_life_options, R.layout.list_item_spinner);

        // Specify dropdown layout style.
        shelfLifeSpinnerAdapter.setDropDownViewResource(R.layout.list_item_spinner);

        // Apply the adapter to the spinner
        shelfLifeSpinner.setAdapter(shelfLifeSpinnerAdapter);

        // Add the spinner to the list of focusable items, so that when the user clicks the next
        // button on the keyboard the spinner is focusable
        shelfLifeSpinner.setFocusable(true);

        shelfLifeSpinner.setFocusableInTouchMode(true);

        // Set the integer mSelected to the constant values
        shelfLifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String selection = (String) parent.getItemAtPosition(position);

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
                        // Validate the input
                        mShelfLifeIsValidated = validateShelfLife();
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

        Spinner categorySpinner = findViewById(
                R.id.activity_detail_product_base_fields_editable_spinner_category);

        // Create an adapter for the spinner. The list options are from the String array in
        // arrays.xml. The spinner will use the default layout.
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_product_category_options, R.layout.list_item_spinner);

        // Specify dropdown layout style.
        categorySpinnerAdapter.setDropDownViewResource(R.layout.list_item_spinner);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(categorySpinnerAdapter);

        // Add the spinner to the list of focusable items, so that when the user clicks the next
        // button on the keyboard the spinner is focusable
        categorySpinner.setFocusable(true);

        categorySpinner.setFocusableInTouchMode(true);

        // Set the integer mUoM to the constant values
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        // Validate the input field
                        mCategoryIsValidated = validateCategory();
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
            tintMenuIcon(this, saveItem, R.color.white);
            tintMenuIcon(this, editItem, R.color.white);
            tintMenuIcon(this, deleteItem, R.color.white);
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

        // If existing product, is not the creator, is in the used list, user fields editable.
        if (mIsExistingProduct &&
                !mIsCreator &&
                mInUsersUsedList &&
                mUserFieldsAreEditable) {

            setMenuItemVisibility(true, false, false);
        }

        // Existing product, user is not creator, is in used list, multi-user is not yet known, base
        // fields are not editable, user fields are not editable
        if (mIsExistingProduct &&
                !mIsCreator &&
                mInUsersUsedList &&
                !mIsMultiUser &&
                !mBaseFieldsAreEditable &&
                !mUserFieldsAreEditable) {

            setMenuItemVisibility(false, true, true);
        }

        // Existing product, user is creator, is in the users used list. Ui state is uneditable.
        if (mIsExistingProduct &&
                mIsCreator &&
                mInUsersUsedList &&
                !mBaseFieldsAreEditable &&
                !mUserFieldsAreEditable) {

            // Uneditable state
            setMenuItemVisibility(false, true, true);
        }

        // Existing product, user is creator, is in users used list, both editable containers are
        // visible.
        if (mIsExistingProduct &&
                mIsCreator &&
                mInUsersUsedList &&
                mBaseFieldsAreEditable &&
                mUserFieldsAreEditable) {

            // Editable state
            setMenuItemVisibility(true, false, true);
        }

        // Existing product, user is creator, is in users used list, base container is uneditable
        // user container is editable.
        if (mIsExistingProduct &&
                mIsCreator &&
                mInUsersUsedList &&
                !mBaseFieldsAreEditable &&
                mUserFieldsAreEditable) {

            // Editable state
            setMenuItemVisibility(true, false, true);
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

            case R.id.menu_product_editor_action_edit:
                editProduct();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Sets up the product for editing */
    private void editProduct() {

        // An existing product, not created by user, but on users used list
        if (mIsExistingProduct &&
                !mIsCreator &&
                mInUsersUsedList) {

            // Set the screen title to edit a product
            setScreenTitle(Constants.ACTIVITY_TITLE_PRODUCT_EDIT);

            // Make the user specific fields editable
            mUserFieldsAreEditable = true;
            updateUserFieldsEditableStatus();

            // Update the menu items
            invalidateOptionsMenu();

            // Update the data in the Ui
            populateUi();
        }

        if (mIsExistingProduct &&
                mIsCreator &&
                mInUsersUsedList) {
            // Check to see if more than one user is using this product. This is a
            // database operation and may take some time. We cannot proceed until we get the
            // result. So continue operations from method checkMultiUserStatus()
            checkMultiUserStatus();
        }

        // Request focus to the first editable filed
        mDetailProductBinding.
                activityDetailProductUserFieldsEditableInclude.
                activityDetailProductEtRetailer.
                requestFocus();

        // Make the ImageView editable
        mDetailProductBinding.
                activityDetailProductIbAddCameraPicture.
                setVisibility(View.VISIBLE);

        mDetailProductBinding.
                activityDetailProductIbRotatePicture.
                setVisibility(View.VISIBLE);

        mDetailProductBinding.
                activityDetailProductIbAddGalleryPicture.
                setVisibility(View.VISIBLE);
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

    // Animation for the transition between activities */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
