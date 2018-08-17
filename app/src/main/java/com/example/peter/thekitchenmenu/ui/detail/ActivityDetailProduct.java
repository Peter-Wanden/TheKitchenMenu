package com.example.peter.thekitchenmenu.ui.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.Product;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    // Boolean member variables for logically switching through class decision tree
    private boolean
            mIsExistingProduct,
            mIsCreator,
            mInUsersUsedList,
            mIsMultiUser,
            mBaseFieldsAreEditable,
            mUserFieldsAreEditable,
            mPutProductOnUsedList,
            mImageAvailabe,
            mCameraImageTaken;

    // Containers for groups of fields
    private LinearLayout
            mBaseFieldsEditableContainer,
            mUserFieldsEditableContainer;

    private ConstraintLayout
            mBaseFieldsUnEditableContainer,
            mUserFieldsUneditableContainer;

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
    private ImageButton mAddImageCameraIB;
    private ImageButton mRotateImageIB;
    private ImageButton mAddImageGalleryIB;

    // FAB
    private FloatingActionButton mFab;

    // The temporary path on the device where the full size image is held for processing
    private String mTempImagePath;

    // The re-sampled image we set to the ImageView
    private Bitmap mResultsBitmap;

    // The permanent public (device local) URI for the local image
    private Uri mLocalImageUri;

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
        setContentView(R.layout.activity_detail_product);
        Log.e(LOG_TAG, "onCreate() - called");

        assignMemberVariables();
        setupFireBase();
        initialiseFirebaseAuthentication();
        checkSavedInstanceState(savedInstanceState);
        initialiseViews();
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
            setScreenTitle(Constants.SCREEN_TITLE_PRODUCT_VIEW);

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
                    savedInstanceState.getBoolean(Constants.BASE_FIELDS_EDITABLE_STATUS_KEY);
            mUserFieldsAreEditable =
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
        mImageAvailabe = false;

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
                Constants.PRODUCT_BASE_CREATED_BY_KEY);

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
        mUserFieldsAreEditable = true;
        updateUserFieldsEditableStatus();

        // Set the FAB to gone
        mFab.setVisibility(View.GONE);

        // Set the image editing buttons to visible
        mAddImageCameraIB.setVisibility(View.VISIBLE);
        mRotateImageIB.setVisibility(View.VISIBLE);
        mAddImageGalleryIB.setVisibility(View.VISIBLE);

        // Move the cursor to the first field
        mDescriptionET.requestFocus();

        // Update the bool's
        mIsCreator = true;
        mIsExistingProduct = false;
        mPutProductOnUsedList = true;
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
                    Log.e(LOG_TAG, "getUsedList - datasnap is: " + dataSnapshot);

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

                    // Update the UI with the product and user data
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
                    setScreenTitle(Constants.SCREEN_TITLE_PRODUCT_EDIT);

                    // Update the data in the Ui
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
        mResultsBitmap = BitmapUtils.resampleImage(this, mLocalImageUri, null);
        mProductIV.setImageBitmap(mResultsBitmap);

    }

    /**
     * saveProduct()
     * There are six reasons a user might want to save product data:
     * 1. A new product is being created
     * 2. An existing product is being updated by the creator and they are the only user
     * 3. An existing product is being updated by the creator and there is more than one user
     * 4. An existing product is being added by someone who may not have used this product before
     * 5. An existing product is being updated by someone who has used this product before
     * 6. An existing product is being added by the creator hwo has previously deleted the product
     */
    private void saveProduct() {

        Log.e(LOG_TAG, "\nsaveProduct() - Truth table: " +
                "\nmIsCreator: " + mIsCreator +
                "\nmBaseFieldsAreEditable: " + mBaseFieldsAreEditable +
                "\nmUserFieldsAreEditable: " + mUserFieldsAreEditable +
                "\nmInUsersUsedList: " + mInUsersUsedList +
                "\nmPutProductOnUsedList: " + mPutProductOnUsedList +
                "\nmIsExistingProduct: " + mIsExistingProduct +
                "\nmIsMultiUser: " + mIsMultiUser);

        // Todo Implement on up pressed save changes and touch listener
        // Todo - can these updates be written in a single write

        /*
           1. This is a new product. Various parts of it and its users data needs to be stored in
              three locations in the database:

              1. /collection/products - Stores product data that is common to all users
              2. /collection/users/[user ID]/collection_products/[product ID] - Users product lists
              3. /collection/used_products/[product ref key] - Count of how many are using a product
         */
        if (mIsCreator &&
                mBaseFieldsAreEditable &&
                mUserFieldsAreEditable &&
                !mInUsersUsedList &&
                mPutProductOnUsedList &&
                !mIsExistingProduct &&
                !mIsMultiUser) {

            Log.e(LOG_TAG, "saveProduct() - Case 1: This is a new product. Save " +
                    "locations: 1 + 2 + 3 + Image (if available)");

            // Validate the product base fields
            boolean baseFieldsValidated = validateBaseFields();

            // If base fields are validated...
            if (baseFieldsValidated) {

                // Validate the user product specific fields
                boolean userFieldsAreValidated = validateUserFields();

                // If the product specific fields are validated...
                if (userFieldsAreValidated) {

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

                    // Create a reference to /collection/users/[user ID]/collection_products/
                    // [product ID] location 2 (as mentioned above)
                    final DatabaseReference userProductRef = mFbCollectionUsers
                            .child(mUserUid)
                            .child(Constants.FB_COLLECTION_PRODUCTS)
                            .child(mFbProductReferenceKey);

                    // Create a reference to the /collection/used_products/[product ref key]
                    // location 3 (as mentioned above)
                    final DatabaseReference usedProductRef = mFbCollectionUsedProducts
                            .child(mFbProductReferenceKey).push();

                    // Extract the /used_products/user/[product reference key]
                    Uri usedProductUserUri = Uri.parse(usedProductRef.toString());
                    mFbUsedProductsUserKey = usedProductUserUri.getLastPathSegment();

                    if (mImageAvailabe) {

                        // From: https://firebase.google.com/docs/storage/android/upload-files
                        // Get the data from the ImageView as bytes (as a very small image).
                        mProductIV.setDrawingCacheEnabled(true);
                        mProductIV.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) mProductIV.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
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

                                Log.e(LOG_TAG, "save Product: fbStorageImageUri is: " +
                                        mFbStorageImageUri);

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
                }
            }
            // Todo - turn off all editable fields, turn on non-editable fields and load unsaved
            // todo - data into them. Show a loading indicator while waiting for the image to process
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

            Log.e(LOG_TAG, "saveProduct() - Case 2: Existing product, created by user, " +
                    "being edited by user, all fields editable, single user. Save Locations: 1 + 2");

            // Validate the product base fields
            boolean baseFieldsValidated = validateBaseFields();

            // If base fields are validated...
            if (baseFieldsValidated) {

                // Validate the user product specific fields
                boolean userFieldsValidated = validateUserFields();

                // If the product specific fields are validated...
                if (userFieldsValidated) {
                    // If there has been changes to the image, save them
                    // Location

                    Log.e(LOG_TAG, "saveProduct() - isImageTaken() = " + mImageAvailabe);

                    if (mImageAvailabe) {

                        // Either:
                        // 1. A new image has been added to an existing product that did not have
                        //    an image or
                        // 2. The existing image has been modified e.g. rotated or
                        // 3. The existing image is being replaced.

                        // From: https://firebase.google.com/docs/storage/android/upload-files
                        // Get the data from the ImageView as bytes and downsize image.
                        mProductIV.setDrawingCacheEnabled(true);
                        mProductIV.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) mProductIV.getDrawable()).getBitmap();
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

                                Log.e(LOG_TAG, "saveProduct() - Updated image saved to: " +
                                        mFbStorageImageUri);

                            /*
                               Collate any changes and update the child data in the database.
                            */

                                // mProduct (our reference product) was updated by getUsedList()
                                // when we checked to see if the product existed. We can use this
                                // instance to check for and collate any updates.
                                Map<String, Object> productBaseUpdates = getBaseUpdates();
                                Map<String, Object> productUserUpdates = getUserUpdates();

                                // If there has been updates to the base data, save them
                                if (productBaseUpdates != null) {

                                    // Save the changes to the products base data
                                    // Location 1
                                    saveBaseProductUpdates(productBaseUpdates);
                                }

                                // If there has been changes to the user data, save them
                                // Location 2
                                if (productUserUpdates != null) {

                                    // Save the changes to the users product data
                                    saveUserProductUpdates(productUserUpdates);
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
                        // instance to check for and collate any updates.
                        Map<String, Object> productBaseUpdates = getBaseUpdates();
                        Map<String, Object> productUserUpdates = getUserUpdates();

                        // If there has been updates to the base data, save them
                        if (productBaseUpdates != null) {

                            // Save the changes to the products base data
                            // Location 1
                            saveBaseProductUpdates(productBaseUpdates);
                        }

                        // If there has been changes to the user data, save them
                        // Location 2
                        if (productUserUpdates != null) {

                            // Save the changes to the users product data
                            saveUserProductUpdates(productUserUpdates);
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

            Log.e(LOG_TAG, "saveProduct() - Case 3: This is a existing multi-user product. " +
                    "The user is the creator. Only the user data can be updated - Save locations: 1");

            // Validate the user product specific fields
            boolean userFieldsValidated = validateUserFields();

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

            Log.e(LOG_TAG, "saveProduct() - Case 4: This is a existing product, with one " +
                    " or many users. The user is not the creator. The user is adding this product" +
                    "to their product list - Save Locations 1 + 3");

            // Todo - can these updates be written in a single write

            // Validate the user product specific fields
            boolean userFieldsValidated = validateUserFields();

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
                            Constants.PRODUCT_USER_FB_USED_USER_KEY, mFbUsedProductsUserKey);

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

            Log.e(LOG_TAG, "saveProduct() - Case 5: Existing product, not creator, being " +
                    "edited, in users used list, base fields uneditable - Save locations: 1");

            // Validate the users input fields
            boolean userFieldsValidated = validateUserFields();

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
              product from thier their list. They are now adding it back and updating the user
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

            Log.e(LOG_TAG, "saveProduct() - Case 6: Existing product, creator, being " +
                    "added back into creators list, put on used list, base fields uneditable" +
                    " - Save locations: 3 and 2");

            // Validate the users input fields
            boolean userFieldsValidated = validateUserFields();

            if (userFieldsValidated) {

                // Create a HashMap
                Map<String, Object> productUserUpdates = getUserUpdates();

                // If there are updates save them to the database
                if (productUserUpdates != null) {

                    // Location 3 - This needs to be completed first as we need the reference
                    addUserToUsedProducts();

                    // Add in the reference provided by addUserToUsedProducts
                    productUserUpdates.put(
                            Constants.PRODUCT_USER_FB_USED_USER_KEY, mFbUsedProductsUserKey);

                    // Location 2
                    saveUserProductUpdates(productUserUpdates);
                }
            }

        } else {
            Log.e(LOG_TAG, "Save action did not meet criteria required to make changes to " +
                    "the database. Nothing was modified.");
        }
        finish();
    }

    /* Checks for updates to the base product data */
    private Map<String, Object> getBaseUpdates() {

        // Create a HashMap to store any changes, then compare
        // each field in the base product data. If any change has occurred, add it to the
        // HashMap.
        Map<String, Object> baseUpdates = new HashMap<>();
        // Create a counter to see how many updates there are, if any.
        int baseUpdateCounter = 0;

        // Add any changes to the base product information
        if (!mProduct.getDescription().equals(mDescription)) {
            baseUpdates.put(Constants.PRODUCT_BASE_DESCRIPTION_KEY, mDescription);
            mProduct.setDescription(mDescription);
            baseUpdateCounter++;
        }
        if (!mMadeBy.equals(mProduct.getMadeBy())) {
            baseUpdates.put(Constants.PRODUCT_BASE_MADE_BY_KEY, mMadeBy);
            mProduct.setMadeBy(mMadeBy);
            baseUpdateCounter++;
        }
        if (mCategory != mProduct.getCategory()) {
            baseUpdates.put(Constants.PRODUCT_BASE_CATEGORY_KEY, mCategory);
            mProduct.setCategory(mCategory);
            baseUpdateCounter++;
        }
        if (mShelfLife != mProduct.getShelfLife()) {
            baseUpdates.put(Constants.PRODUCT_BASE_SHELF_LIFE_KEY, mShelfLife);
            mProduct.setShelfLife(mShelfLife);
            baseUpdateCounter++;
        }
        if (mPackSize != mProduct.getPackSize()) {
            baseUpdates.put(Constants.PRODUCT_BASE_PACK_SIZE_KEY, mPackSize);
            mProduct.setPackSize(mPackSize);
            baseUpdateCounter++;
        }
        if (mUnitOfMeasure != mProduct.getUnitOfMeasure()) {
            baseUpdates.put(Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY, mUnitOfMeasure);
            mProduct.setUnitOfMeasure(mUnitOfMeasure);
            baseUpdateCounter++;
        }
        if (mPackPriceAverage != mProduct.getPackPriceAverage()) {
            baseUpdates.put(Constants.PRODUCT_BASE_PRICE_AVE_KEY, mPackPriceAverage);
            mProduct.setPackPriceAverage(mPackPriceAverage);
            baseUpdateCounter++;
        }
        if (!mFbStorageImageUri.toString().equals(mProduct.getFbStorageImageUri())) {
            baseUpdates.put(Constants.PRODUCT_USER_FB_STORAGE_IMAGE_URI_KEY, mFbStorageImageUri.toString());
            mProduct.setFbStorageImageUri(mFbStorageImageUri.toString());
            baseUpdateCounter++;
        }
        // No need to check packPriceAve as it is automatically calculated
        // No need to check createdBy as it cannot be modified once created

        // If there have been changes return the baseUpdates Map
        if (baseUpdateCounter > 0) {
            return baseUpdates;
        }

        // If there are no updates return null
        return null;
    }

    /* Checks for updates to the user specific product data */
    private Map<String, Object> getUserUpdates() {

        // Now for the user specific information, create a HashMap to store the changes
        // We store the whole product in the /collection_user/[uid]/collection_products location
        Map<String, Object> productUserUpdates = new HashMap<>();

        // Add the existing base data to the HashMap
        productUserUpdates.put(
                Constants.PRODUCT_BASE_DESCRIPTION_KEY,
                mProduct.getDescription());
        productUserUpdates.put(
                Constants.PRODUCT_BASE_MADE_BY_KEY,
                mProduct.getMadeBy());
        productUserUpdates.put(
                Constants.PRODUCT_BASE_CATEGORY_KEY,
                mProduct.getCategory());
        productUserUpdates.put(
                Constants.PRODUCT_BASE_SHELF_LIFE_KEY,
                mProduct.getShelfLife());
        productUserUpdates.put(
                Constants.PRODUCT_BASE_PACK_SIZE_KEY,
                mProduct.getPackSize());
        productUserUpdates.put(
                Constants.PRODUCT_BASE_UNIT_OF_MEASURE_KEY,
                mProduct.getUnitOfMeasure());
        productUserUpdates.put(
                Constants.PRODUCT_BASE_PRICE_AVE_KEY, mProduct.getPackPrice());
        productUserUpdates.put(
                Constants.PRODUCT_BASE_CREATED_BY_KEY,
                mProduct.getCreatedBy());

        // Add in the user specific data to the HashMap
        productUserUpdates.put(
                Constants.PRODUCT_USER_LOCAL_IMAGE_URI_KEY,
                mProduct.getLocalImageUri());
        productUserUpdates.put(
                Constants.PRODUCT_USER_FB_STORAGE_IMAGE_URI_KEY,
                mProduct.getFbStorageImageUri());
        productUserUpdates.put(
                Constants.PRODUCT_USER_FB_REFERENCE_KEY,
                mProduct.getFbProductReferenceKey());
        productUserUpdates.put(
                Constants.PRODUCT_USER_FB_USED_USER_KEY,
                mProduct.getFbUsedProductsUserKey());

        // Create a counter to see how many updates there are, if any.
        int userUpdateCounter = 0;

        // Compare and add any changes from the user specific fields to the HashMap
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

        // If there have been updates return the updated Map
        if (userUpdateCounter > 0) {
            Log.e(LOG_TAG, "getUserUpdates() - There are: " + userUpdateCounter + " user updates.");
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

        Log.e(LOG_TAG, "saveBaseProductUpdates() - Looks like: " + productBaseUpdates);

        reference.updateChildren(productBaseUpdates);

    }

    /* Adds the current user ID to:
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
    // Todo - shift to utils class
    private boolean validateBaseFields() {

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
            return false;
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
            return false;
        }

        // Category - Direct from spinners, user cannot select an incorrect value

        // Shelf life - Direct from spinners, user cannot select an incorrect value

        // Pack size
        mPackSize = Integer.parseInt(mPackSizeET.getText().toString().trim());
        boolean validatePackSize = InputValidation.validatePackSize(mPackSize);

        if (!validatePackSize) {
            mPackSizeET.requestFocus();
            mPackSizeET.setError(getResources().getString(
                    R.string.input_error_pack_size));
            return false;
        }

        // Unit of measure - Direct from spinners, user cannot select an incorrect value

        // Pack price - No validation required (that I can think of)

        return true;
    }

    /* Validates the product fields that are specific to each user */
    // Todo - shift to utils class
    private boolean validateUserFields() {

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
            return false;
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
            return false;
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
            return false;
        }

        // Pack price
        // TODO - Implement currency, change data type to Float
        String unformattedPrice = mPackPriceET.getText().toString().trim();

        if (unformattedPrice.length() > 0) {
            mPackPrice = Double.parseDouble(unformattedPrice);
        } else {
            mPackPrice = 0.0;
        }
        return true;
    }

    /* Converts the products base data to an object map */
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
        return newProductData.baseProductToMap();
    }

    /* Given the base updates, converts the entire product data to a map object */
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
     * - User specific fields editable
     * - User specific fields uneditable
     * */
    private void populateUi() {
        Log.e(LOG_TAG, "\npopulateUi() - State of bool's as follows:"
                + "isExistingProduct: " + mIsExistingProduct + "\n"
                + "IsCreator: " + mIsCreator + "\n"
                + "ProductInUsedList: " + mInUsersUsedList + "\n"
                + "IsMultiUser : " + mIsMultiUser + "\n"
                + "mPutProductOnUsedList: " + mPutProductOnUsedList + "\n"
                + "BaseFields are editable: " + mBaseFieldsAreEditable + "\n"
                + "UserFieldsAreEditable: " + mUserFieldsAreEditable);

        // Load the image if there is one, from Firestore
        if (!mProduct.getFbStorageImageUri().equals("")) {

            Picasso.get()
                    .load(mProduct.getFbStorageImageUri())
                    .into(mProductIV, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.e(LOG_TAG, "populateUi() - Picasso - Image loaded successfully.");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(LOG_TAG, "populateUi() - Picasso - Image failed with error: " + e);

                        }
                    });


//            Picasso.get().load(mProduct.getFbStorageImageUri()).into(mProductIV);

            Log.e(LOG_TAG, "mProduct.getFbStorageImageUri() is: " +
                    mProduct.getFbStorageImageUri());
            Log.e(LOG_TAG, "populateUi() - There should be an image on the screen");
        }

        // Base fields editable criteria:
        // 1. Has to be creator of product
        // 2. View has to be visible
        if (mIsCreator && mBaseFieldsEditableContainer.getVisibility() == View.VISIBLE) {

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

            // Update the TextViews
            mDescriptionTV.setText(mProduct.getDescription());
            mMadeByTV.setText(mProduct.getMadeBy());
            mPackSizeTV.setText(String.valueOf(mProduct.getPackSize()));

            mUoMTV.setText(Converters.getUnitOfMeasureString(
                    this, mProduct.getUnitOfMeasure()));

            mShelfLifeTV.setText(Converters.getShelfLifeString(
                    this, mProduct.getShelfLife()));

            mCategoryTV.setText(Converters.getCategoryString(
                    this, mProduct.getCategory()));
        }

        // User specific fields editable criteria:
        // 1. Has to be on the users used product list or about to be on it
        // 2. View has to be visible
        if ((mInUsersUsedList || mPutProductOnUsedList) && mUserFieldsEditableContainer
                .getVisibility() == View.VISIBLE) {

            mRetailerET.setText(mProduct.getRetailer());
            mPackPriceET.setText(String.valueOf(mProduct.getPackPrice()));
            mLocationRoomET.setText(mProduct.getLocationRoom());
            mLocationInRoomET.setText(mProduct.getLocationInRoom());

            // Set the menu items for this UI state
            invalidateOptionsMenu();

            // User specific fields uneditable container criteria
            // 1. Has to be in the users used product list
            // 2. View has to be visible
        } else if (mInUsersUsedList && mUserFieldsUneditableContainer
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

        // ToDo - could this be why we arent seeing the image?
        if (!Uri.EMPTY.equals(Uri.parse(mProduct.getFbStorageImageUri()))) {
            Picasso.get().load(mProduct.getFbStorageImageUri()).into(mProductIV);
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
                .BASE_FIELDS_EDITABLE_STATUS_KEY, mBaseFieldsAreEditable);
        outState.putBoolean(Constants
                .USER_CUSTOM_FIELDS_EDITABLE_STATUS_KEY, mUserFieldsAreEditable);

    }

    /* Get a reference eto all of the views */
    private void initialiseViews() {

        // Containers
        mBaseFieldsEditableContainer = findViewById(
                R.id.activity_detail_product_base_fields_editable_container);
        mBaseFieldsUnEditableContainer = findViewById(
                R.id.activity_detail_product_base_fields_uneditable_container);
        mUserFieldsEditableContainer = findViewById(
                R.id.activity_detail_product_user_fields_container);
        mUserFieldsUneditableContainer = findViewById(
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

        // Image buttons
        mAddImageCameraIB = findViewById(
                R.id.activity_detail_product_ib_add_camera_picture);
        mRotateImageIB = findViewById(
                R.id.activity_detail_product_ib_rotate_picture);
        mAddImageGalleryIB = findViewById(
                R.id.activity_detail_product_ib_add_gallery_picture);

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
        mAddImageCameraIB.setOnClickListener(v -> requestPermissions());

        /* OnClickListener for the rotate picture button */
        mRotateImageIB.setOnClickListener(v -> {

            // Rotate the image by 90 degrees
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap RecipeImage = ((BitmapDrawable) mProductIV.getDrawable()).getBitmap();
            Bitmap rotated = Bitmap.createBitmap(RecipeImage, 0, 0,
                    RecipeImage.getWidth(), RecipeImage.getHeight(), matrix, true);
            mProductIV.setImageBitmap(rotated);

            // The image has changed so update the bool
            mImageAvailabe = true;
        });

        /* onClickListener for the FAB */
        mFab.setOnClickListener(v -> {

            // If the current product is not in the users used list, pressing the FAB opens up the
            // user specific fields, so the user can edit the user specific fields
            if (!mInUsersUsedList) {

                // Make the user specific fields visible
                mUserFieldsAreEditable = true;
                updateUserFieldsEditableStatus();

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

        /* onClickListener for the add image from gallery button */
        mAddImageGalleryIB.setOnClickListener(v -> {

            // Launch an intent to open a gallery app
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

            startActivityForResult(Intent.createChooser(
                    intent, getString(R.string.intent_gallery_picker_title)),
                    Constants.REQUEST_IMAGE_PICKER);
        });
    }

    /* Sets up the visibility properties for an existing uneditable product */
    private void showUneditableViewContainers() {

        // This is for product viewing only, so no need for editable views
        mBaseFieldsEditableContainer.setVisibility(View.GONE);
        mUserFieldsEditableContainer.setVisibility(View.GONE);

        // Make relevant view containers visible
        mBaseFieldsUnEditableContainer.setVisibility(View.VISIBLE);
        // Only show user the 'specific product data container' if in used list
        if (mInUsersUsedList) {
            mUserFieldsUneditableContainer.setVisibility(View.VISIBLE);
        } else {
            // This product in not in the users list. Show the fab so they can add it
            mUserFieldsUneditableContainer.setVisibility(View.GONE);
            mFab.setVisibility(View.VISIBLE);
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
    private void updateUserFieldsEditableStatus() {

        if (mUserFieldsAreEditable) {
            mUserFieldsEditableContainer.setVisibility(View.VISIBLE);
            mUserFieldsUneditableContainer.setVisibility(View.GONE);
        } else {
            mUserFieldsEditableContainer.setVisibility(View.GONE);
            // If the product is on or is going to be on the users used list turn the user
            // specific fields on
            if (mInUsersUsedList || mPutProductOnUsedList) {
                mUserFieldsUneditableContainer.setVisibility(View.VISIBLE);
            } else {
                // If not on the users used list turn the view off
                mUserFieldsUneditableContainer.setVisibility(View.GONE);
            }
        }
    }

    /* Delete the product */
    // todo - Complete the delete product action
    // todo - Create a list of deleted products to compare with Firebase products
    // todo - so we don't display products from firebase that the user has deleted
    // todo - delete the product information from /products/ and /users/products/
    private void deleteProduct() {

        /*
           This is an existing product. So there are potentially four locations where data needs to
           be deleted:
           1. Delete the product form the users used product list which is located at
              /collection_users/[user id]/collection_products/[product id]
           2. Delete the entry for this product from this user in the used_products reference
           3. If this is the only user using this product, delete the product from
              /collection_products/
           4. If this is the only user using this product mFbStorageUri is not empty, delete the
              image in FireStore it is pointing to.
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
                                if (!mFbStorageImageUri.toString().equals("")) {

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
            mImageAvailabe = true;
            mCameraImageTaken = true;

        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_CANCELED) {
            // If the camera was cancelled, delete the temporary file
            BitmapUtils.deleteImageFile(this, mTempImagePath);
            mCameraImageTaken = false;

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_OK) {
            mImageAvailabe = true;
            processAndSetImage();

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_CANCELED) {
            // Todo - handle a cancelled result
            Log.e(LOG_TAG, "Media store intent cancelled");

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER && resultCode == RESULT_OK) {
            // Photo picker
            Uri selectedImageUri = data.getData();
            mImageAvailabe = true;
            mProductIV.setImageURI(selectedImageUri);

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER && resultCode == RESULT_CANCELED) {
            Log.e(LOG_TAG, "Image picker intent cancelled");
        }
    }

    /* Resample's the image so it fits our imageView and uses less resources */
    private void processAndSetImage() {

        mResultsBitmap = BitmapUtils.resampleImage(this, null, mTempImagePath);
        mProductIV.setImageBitmap(mResultsBitmap);

//        addImageToGallery();
    }

    /* Add the photo to the media providers database so it is accessible to all */
    private void addImageToGallery() {
        // Get the full size temporary image file
        File file = new File(mTempImagePath);
        // Move it to a public area and return its content Uri
        MediaScannerConnection.scanFile(
                getApplicationContext(),
                new String[]{file.getAbsolutePath()}, null, (path, uri) -> {
                    if (uri != null) {
                        // Update the products public image Uri
                        mLocalImageUri = uri;
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

//        Log.e(LOG_TAG, "\nonPrepareOptionsMenu() - Truth table as follows: " +
//                "\nIsExistingProduct: " + mIsExistingProduct +
//                "\nIsCreator: " + mIsCreator +
//                "\nInUsedList: " + mInUsersUsedList +
//                "\nIsMultiUser: " + mIsMultiUser +
//                "\nBaseFieldsAreEditable : " + mBaseFieldsAreEditable +
//                "\nUserFieldsAreEditable : " + mUserFieldsAreEditable);

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
                mUserFieldsUneditableContainer.getVisibility() == View.VISIBLE &&
                mBaseFieldsUnEditableContainer.getVisibility() == View.VISIBLE) {

            // Uneditable state
            setMenuItemVisibility(false, true, true);
        }

        // Existing product, user is creator, is in users used list, both editable containers are
        // visible.
        if (mIsExistingProduct &&
                mIsCreator &&
                mInUsersUsedList &&
                mUserFieldsEditableContainer.getVisibility() == View.VISIBLE &&
                mBaseFieldsEditableContainer.getVisibility() == View.VISIBLE) {

            // Editable state
            setMenuItemVisibility(true, false, true);
        }

        // Existing product, user is creator, is in users used list, base container is uneditable
        // user container is editable.
        if (mIsExistingProduct &&
                mIsCreator &&
                mInUsersUsedList &&
                mBaseFieldsEditableContainer.getVisibility() == View.GONE &&
                mUserFieldsEditableContainer.getVisibility() == View.VISIBLE) {

            // Editable state
            setMenuItemVisibility(true, false, true);
        }


        return true;
    }

    /* This method manages the actions as they are selected from the AppBar */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Log.e(LOG_TAG, "\nonOptionsItemSelected - State of bool's as follows:"
//                + "isExistingProduct: " + mIsExistingProduct + "\n"
//                + "IsCreator: " + mIsCreator + "\n"
//                + "ProductInUsedList: " + mInUsersUsedList + "\n"
//                + "mPutProductOnUsedList: " + mPutProductOnUsedList + "\n"
//                + "mProduct.GetUsedProductsUserKey = " + mProduct.getFbUsedProductsUserKey());

        switch (item.getItemId()) {
            // Save (tick) menu option selected
            case R.id.menu_product_editor_action_save:
                // Reduce bounce as we say in the electronics trade (double click) by hiding the
                // menu items once pressed.
                setMenuItemVisibility(false, false, false);
                // Save product and exit activity
                saveProduct();
                break;

            case R.id.menu_product_editor_action_delete:
                // Delete product
                deleteProduct();
                break;

            case R.id.menu_product_editor_action_edit:

                // An existing product, not created by user, but on users used list
                if (mIsExistingProduct && !mIsCreator && mInUsersUsedList) {

                    // Set the screen title to edit a product
                    setScreenTitle(Constants.SCREEN_TITLE_PRODUCT_EDIT);

                    // Make the user specific fields editable
                    mUserFieldsAreEditable = true;
                    updateUserFieldsEditableStatus();

                    // Update the menu items
                    invalidateOptionsMenu();

                    // Update the data in the Ui
                    populateUi();
                }

                if (mIsExistingProduct && mIsCreator && mInUsersUsedList) {
                    // Check to see if more than one user is using this product. This is a
                    // database operation and may take some time. We cannot proceed until we get the
                    // result. So continue operations from method checkMultiUserStatus()
                    checkMultiUserStatus();
                }

                // Request focus to the first editable filed
                mRetailerET.requestFocus();

                // Make the ImageView editable
                mAddImageCameraIB.setVisibility(View.VISIBLE);
                mRotateImageIB.setVisibility(View.VISIBLE);
                mAddImageGalleryIB.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    /* Sets the visibility properties of the menu items */
    private void setMenuItemVisibility(boolean saveItem, boolean editItem, boolean deleteItem) {

        // Todo - change all instances of this to: invalidateOptionsMenu()
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
}
