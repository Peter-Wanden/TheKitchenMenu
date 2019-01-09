package com.example.peter.thekitchenmenu.ui.detail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
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
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteDbRefs;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.databinding.ProductDetailShellBinding;
import com.example.peter.thekitchenmenu.utils.BitmapUtils;
import com.example.peter.thekitchenmenu.utils.Converters;
import com.example.peter.thekitchenmenu.utils.InputValidation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import static com.example.peter.thekitchenmenu.app.Constants.ANONYMOUS;
import static com.example.peter.thekitchenmenu.data.entity.Product.CATEGORY;
import static com.example.peter.thekitchenmenu.data.entity.Product.CREATED_BY;
import static com.example.peter.thekitchenmenu.data.entity.Product.DESCRIPTION;
import static com.example.peter.thekitchenmenu.data.entity.Product.MADE_BY;
import static com.example.peter.thekitchenmenu.data.entity.Product.PACK_SIZE;
import static com.example.peter.thekitchenmenu.data.entity.Product.PROD_COMM_PRICE_AVE;
import static com.example.peter.thekitchenmenu.data.entity.Product.REMOTE_IMAGE_URI;
import static com.example.peter.thekitchenmenu.data.entity.Product.REMOTE_PRODUCT_ID;
import static com.example.peter.thekitchenmenu.data.entity.Product.SHELF_LIFE;
import static com.example.peter.thekitchenmenu.data.entity.Product.UNIT_OF_MEASURE;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.LOCAL_IMAGE_URI;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.LOCATION_IN_ROOM;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.LOCATION_ROOM;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.PRICE;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.RETAILER;
import static com.example.peter.thekitchenmenu.data.entity.UsersProductData.REMOTE_USED_PRODUCT_ID;

public class ProductDetail extends AppCompatActivity {

    private static final String TAG = "ProductDetail";
    private static final String EXISTING_PRODUCT = "existing_product";
    public static final String PUT_IN_USED_LIST = "put_on_used_list";
    public static final String IN_USED_LIST = "product_on_used_list_key";
    public static final String PRODUCT_FIELDS_EDITABLE = "base_fields_editable_status_key";
    public static final String USER_FIELDS_EDITABLE = "user_custom_fields_editable_status_key";

    ProductDetailShellBinding detailView;

    private ProductModel productModel;

    private String
            description,
            retailer,
            locationRoom,
            locationInRoom,
            madeBy,
            createdBy;

    private int
            unitOfMeasure,
            packSize,
            shelfLife,
            category;

    private double
            packPrice,
            packAvePrice;

    private boolean
            isExistingProduct,
            isCreator,
            inUsedList,
            isMultiUser,
            productDataIsEditable,
            userProductDataIsEditable,
            putInUsedList,
            isImageAvailable,
            hasCameraImageTaken;

    private boolean
            descriptionValidated,
            madeByValidated,
            packSizeValidated,
            uoMValidated,
            shelfLifeValidated,
            categoryValidated,
            retailerValidated,
            packPriceValidated,
            locationRoomValidated,
            locationInRoomValidated;

    private Menu editorMenuBar;
    private String tempImagePath;
    private Uri localImageUri;

    private Uri remoteImageStorageUri;

    private DatabaseReference remoteProductCollectionReference;
    private DatabaseReference remoteUserCollectionReference;
    private DatabaseReference remoteUsedProductsCollectionReference;
    private String remoteProductReferenceKey;
    private String remoteUsedProductsUserKeyReference;
    private FirebaseStorage remoteImageStorageDb;
    private StorageReference remoteImageStorageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assignMemberVariables();
        setupFireBase();
        checkSavedInstanceState(savedInstanceState);
        initialiseViews();
        if (!Constants.getUserId().getValue().equals(ANONYMOUS)) {
            ifSignedInInitialise();
        }
    }

    private void ifSignedInInitialise() {

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.PRODUCT_FB_REFERENCE_KEY)) {
            isExistingProduct = true;

            setTitle(R.string.activity_title_view_product);

            // Update the Firebase product reference key with the product passed in
            if (remoteProductReferenceKey.equals(Constants.DEFAULT_FB_PRODUCT_ID)) {
                productModel = intent.getParcelableExtra(Constants.PRODUCT_FB_REFERENCE_KEY);

                // Set the Firebase product reference ID from the incoming intent */
                remoteProductReferenceKey = productModel.getFbProductReferenceKey();

                // Identify if this user is the creator of this product
                isCreator = intent.getBooleanExtra(Constants.PRODUCT_IS_CREATOR_KEY, isCreator);

                // Find out if the product is on this users used list. This is a database operation
                // and may take some time. We cannot proceed until we get the result. So continue
                // operations from method gertUsedList()
                getUsedList();
            }
        } else {
            addNewProduct();
        }
    }

    /* Checks to see if there has been a configuration change, if so recovers the data */
    private void checkSavedInstanceState(@Nullable Bundle savedInstanceState) {

        //If it exists get the current product from saved instance state
        if (savedInstanceState != null && savedInstanceState.containsKey(
                Constants.PRODUCT_FB_REFERENCE_KEY)) {

            // Restore the instance of productModel
            productModel = savedInstanceState.getParcelable(
                    Constants.PRODUCT_FB_REFERENCE_KEY);

            // Update the product reference
            if (productModel != null) {

                remoteProductReferenceKey = productModel.getFbProductReferenceKey();
                remoteUsedProductsUserKeyReference = productModel.getFbUsedProductsUserKey();
                description = productModel.getDescription();
                retailer = productModel.getRetailer();
                madeBy = productModel.getMadeBy();
                unitOfMeasure = productModel.getUnitOfMeasure();
                packSize = productModel.getPackSize();
                shelfLife = productModel.getShelfLife();
                locationRoom = productModel.getLocationRoom();
                locationInRoom = productModel.getLocationInRoom();
                category = productModel.getCategory();
                packPrice = productModel.getPackPrice();
                packAvePrice = productModel.getPackAvePrice();
                localImageUri = Uri.parse(productModel.getLocalImageUri());
                remoteImageStorageUri = Uri.parse(productModel.getLocalImageUri());
                createdBy = productModel.getCreatedBy();
            }

            // Update the bool's
            isExistingProduct = savedInstanceState.getBoolean(EXISTING_PRODUCT);
            isCreator = savedInstanceState.getBoolean(Constants.PRODUCT_IS_CREATOR_KEY);
            inUsedList = savedInstanceState.getBoolean(IN_USED_LIST);
            putInUsedList = savedInstanceState.getBoolean(PUT_IN_USED_LIST);
            productDataIsEditable = savedInstanceState.getBoolean(PRODUCT_FIELDS_EDITABLE);
            userProductDataIsEditable = savedInstanceState.getBoolean(USER_FIELDS_EDITABLE);

            // Update the Strings
            remoteProductReferenceKey = savedInstanceState
                    .getString(Constants.PRODUCT_FB_REFERENCE_KEY);
        }
    }

    /* Sets the defaults for member variables */
    private void assignMemberVariables() {

        isCreator = false;
        isExistingProduct = false;
        inUsedList = false;
        putInUsedList = false;
        productDataIsEditable = false;
        userProductDataIsEditable = false;
        isMultiUser = false;
        isImageAvailable = false;
        hasCameraImageTaken = false;

        /* Construct a default product for field value comparison and validation */
        productModel = new ProductModel(
                Constants.DEFAULT_PROD_MY_ID,
                Constants.DEFAULT_REMOTE_REF_ID,
                Constants.DEFAULT_FB_USED_PRODUCT_ID,
                Constants.DEFAULT_PRODUCT_RETAILER,
                Constants.DEFAULT_PRODUCT_LOC,
                Constants.DEFAULT_PRODUCT_LOC_IN_ROOM,
                Constants.DEFAULT_PRODUCT_PRICE,
                Constants.DEFAULT_LOCAL_IMAGE_URI,
                Constants.DEFAULT_MY_CREATE_DATE,
                Constants.DEFAULT_MY_LAST_UPDATE,
                Constants.DEFAULT_LOCAL_PROD_COMM_ID,
                Constants.DEFAULT_REMOTE_REF_ID,
                Constants.DEFAULT_PRODUCT_DESCRIPTION,
                Constants.DEFAULT_PRODUCT_MADE_BY,
                Constants.DEFAULT_PRODUCT_CATEGORY,
                Constants.DEFAULT_PRODUCT_SHELF_LIFE,
                Constants.DEFAULT_PRODUCT_PACK_SIZE,
                Constants.DEFAULT_PRODUCT_UOM,
                Constants.DEFAULT_PRODUCT_PRICE_AVERAGE,
                Constants.DEFAULT_PRODUCT_CREATED_BY,
                Constants.DEFAULT_REMOTE_IMAGE_STORAGE_URI,
                Constants.DEFAULT_COMM_CREATE_DATE,
                Constants.DEFAULT_COMM_LAST_UPDATE);

        // Set default value for created by
        createdBy = "";

        // Set the default value for the local and Firebase Storage Image Uri's
        localImageUri = Uri.parse(productModel.getLocalImageUri());
        remoteImageStorageUri = Uri.parse(productModel.getFbStorageImageUri());

        // Instance of Firebase storage
        remoteImageStorageDb = FirebaseStorage.getInstance();

        // Instance of our Firebase storage reference
        remoteImageStorageReference = remoteImageStorageDb
                .getReference()
                .child(Constants.FB_STORAGE_IMAGE_REFERENCE);

        // Default value for the used product reference
        remoteUsedProductsUserKeyReference = Constants.DEFAULT_FB_USED_PRODUCT_ID;

        // Default values of Bool's that record the state of input validation
        descriptionValidated = false;
        madeByValidated = false;
        packSizeValidated = false;
        uoMValidated = false;
        shelfLifeValidated = false;
        categoryValidated = false;
        retailerValidated = false;
        packPriceValidated = false;
        locationRoomValidated = false;
        locationInRoomValidated = false;
    }

    /* Sets up Firebase instance and references */
    private void setupFireBase() {

        remoteProductCollectionReference = RemoteDbRefs.getRefProdComm();
        remoteUserCollectionReference = RemoteDbRefs.getRefUsers();
        remoteUsedProductsCollectionReference = RemoteDbRefs.getRefUserProd();
        remoteProductReferenceKey = Constants.DEFAULT_FB_PRODUCT_ID;
    }

    private void addNewProduct() {

        setTitle(R.string.activity_title_add_new_product);

        productFieldsAreEditable(true);
        usersProductInfoFieldsAreEditable(true);

        detailView.fab.setVisibility(View.GONE);
        detailView.pictureFromCameraButton.setVisibility(View.VISIBLE);
        detailView.pictureFromGalleryButton.setVisibility(View.VISIBLE);
        detailView.rotatePictureButton.setVisibility(View.VISIBLE);

        detailView.productEditable.editableDescription.requestFocus();

        isCreator = true;
        isExistingProduct = false;
        putInUsedList = true;
    }

    private boolean validateTextField(String newDescription) {
        String validationResult = InputValidation.validateTextField(this, newDescription);

        if (validationResult.equals(getString(R.string.validation_text_too_short))) {
            detailView.productEditable.editableDescription.setError(
                    getString(R.string.validation_text_too_short));
            return false;

        } else if (validationResult.equals(getString(R.string.validation_text_too_long))) {
            detailView.productEditable.editableDescription.setError(
                    getString(R.string.validation_text_too_long));
            return false;

        } else {
            this.description = newDescription;
            return true;
        }
    }

    private boolean validatePackSize(String newPackSize) {

        if (!TextUtils.isEmpty(newPackSize)) {
            int validatedPackSize = Integer.parseInt(newPackSize);
            boolean packSizeIsValidated = InputValidation.validatePackSize(validatedPackSize);

            if (packSizeIsValidated) {
                packSize = validatedPackSize;
                return true;
            }
        }

        detailView.productEditable.editablePackSize.setError(
                getString(R.string.input_error_product_pack_size));
        return false;
    }

    private boolean validateUnitOfMeasure() {
        uoMValidated = com.example.tkmapplibrary.dataValidation.InputValidation.validateUoM(unitOfMeasure);

        if (!uoMValidated) {
            detailView.productEditable.unitOfMeasureError.setError(getString(
                    R.string.validation_error_unit_of_measure));
            return false;
        }

        detailView.productEditable.unitOfMeasureError.setError(null);
        return true;
    }

    private boolean validateShelfLife() {
        shelfLifeValidated = com.example.tkmapplibrary.dataValidation.InputValidation.validateShelfLife(shelfLife);

        if (!shelfLifeValidated) {
            detailView.productEditable.shelfLifeError.setError(getString(
                    R.string.validation_error_shelf_life));

            return false;
        }
        detailView.productEditable.shelfLifeError.setError(null);
        return true;
    }

    private boolean validateCategory() {
        categoryValidated = com.example.tkmapplibrary.dataValidation.InputValidation.validateProductCategory(category);

        if (!categoryValidated) {
            detailView.productEditable.categoryError.setError(
                    getString(R.string.validation_error_category));
            return false;
        }
        detailView.productEditable.categoryError.setError(null);
        return true;
    }

    private boolean validatePackPrice() {

        // Get the value in the field
        if (!detailView.userProductEditable.editablePrice.getText().toString().equals("") ||
                !detailView.userProductEditable.editablePrice.getText().toString().isEmpty()) {

            packPrice = Double.parseDouble(detailView.userProductEditable.editablePrice.
                    getText().toString().trim());

            // Check against validation rules in TKMAppLibrary
            boolean validatePrice = com.example.tkmapplibrary.dataValidation.InputValidation.validatePrice(packPrice);

            // If there is an error
            if (!validatePrice) {
                detailView.userProductEditable.editablePrice.setError(getResources().getString(
                        R.string.input_error_product_pack_price));
                return false;
            }
            packPriceValidated = true;
            return true;
        }
        detailView.userProductEditable.editablePrice.setError(getResources().getString(
                R.string.input_error_product_pack_price));
        return false;
    }

    /* Check to see if the product is in the users user product list */
    private void getUsedList() {

        // Is the product in the users 'used' list
        DatabaseReference remoteProductReference = RemoteDbRefs.
                getRefProdMy(Constants.getUserId().getValue(), remoteProductReferenceKey);

        remoteProductReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If the product is in the users used list it will show up here
                if (dataSnapshot.exists()) {

                    // Convert the snapshot into a Product view model
//                    productModel = dataSnapshot.getValue(ProductModel.class);

                    // Update products Firebase product reference key
//                    productModel.setRemoteProductId(dataSnapshot.getKey());

                    // This product is in the users used list, so update the used bool
                    inUsedList = true;

                    // Update the member variable for the used products user key
//                    remoteUsedProductsUserKeyReference = productModel.getRemoteIdUsedProduct();

                    Log.d(TAG, "onDataChange: " + productModel.toString());

                    // Update the image storage location, so if it changes later we can
                    // find out by doing a comparison.
//                    remoteImageStorageUri = Uri.parse(productModel.getRemoteImageUri());

                    // Update the visibility of the uneditable view containers
                    showUneditableViewContainers();

                    // Update the UI with the base and user data
                    populateUi();

                } else {
                    // This product is not in the users used list, so update the used bool
                    inUsedList = false;

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
        Query usedProductRef = remoteUsedProductsCollectionReference
                .child(remoteProductReferenceKey).limitToFirst(2);

        usedProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // This is a snapshot of all users that have this product in their used product
                    // list. We only need to know if there is more than one, so we can use the a
                    // limit to query to avoid a large amount of unnecessary data being returned.
                    long childCount = dataSnapshot.getChildrenCount();

                    // Set the value of isMultiUser
                    if (childCount > 1) {

                        // This product is being used by more then one person, so update the bool.
                        isMultiUser = true;

                        // This also means the base fields cannot be edited. Make only the
                        // userSpecific fields editable.
                        usersProductInfoFieldsAreEditable(true);

                    } else {

                        // Just to confirm this product is not multi-user
                        isMultiUser = false;

                        // If there is only one person using this product, the creator, let them
                        // edit the base fields.
                        productFieldsAreEditable(true);

                        // If they are editing the base fields they may also want to edit the user
                        // fields.
                        usersProductInfoFieldsAreEditable(true);

                    }

                    // Reset the menus to show the correct menu items.
                    invalidateOptionsMenu();

                    // Set the screen title for edit mode.
                    setTitle(R.string.activity_title_edit_product);

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
     * 1. A new product is being created or an existing product is being:
     * 2. - updated by the creator and the creator is the only user.
     * 3. - updated by the creator and there is more than one user.
     * 4. - added by someone who has not used this product before, or has and is using it again.
     * 5. - updated by someone who has used this product before.
     * 6. - added by the creator who has previously deleted the product.
     */
    private void saveProduct() {

        /*
           1. This is a new product. Various parts of it and its users data needs to be stored in
              three locations in the remote database:
              1. /collection/products - Stores product data that is common to all users.
              2. /collection/users/[user ID]/collection_products/[product ID] - Users product lists.
              3. /collection_used_products/[product ref key] - Count of how many are using a
                 product.
         */
        if (isCreator &&
                productDataIsEditable &&
                userProductDataIsEditable &&
                !inUsedList &&
                putInUsedList &&
                !isExistingProduct &&
                !isMultiUser) {

            // This is a new product, so check all fields are validated
            if (validateProductFields() && checkValidationUserFields()) {

                // Reduce bounce as we say in the electronics trade (double click) by hiding the
                // menu items once pressed.
                setMenuItemVisibility(false, false, false);

                /*
                   Set the new product to Firebase
                   See: https://firebase.google.com/docs/database/admin/save-data
                */

                // Create a reference to the '/collection/products' location 1 (as mentioned
                // above)
                final DatabaseReference collectionProductsRef = remoteProductCollectionReference.push();

                // Extract the unique product ID
                Uri productReferenceUri = Uri.parse(collectionProductsRef.toString());
                remoteProductReferenceKey = productReferenceUri.getLastPathSegment();

                // Create a reference to /collection_users/[user ID]/collection_products/
                // [product ID] location 2 (as mentioned above)
                final DatabaseReference userProductRef = remoteUserCollectionReference
                        .child(Constants.getUserId().getValue())
                        .child(Constants.FB_COLLECTION_PRODUCTS)
                        .child(remoteProductReferenceKey);

                // Create a reference to the /collection_used_products/[product ref key]
                // location 3 (as mentioned above)
                final DatabaseReference usedProductRef = remoteUsedProductsCollectionReference
                        .child(remoteProductReferenceKey).push();

                // Extract the /used_products/user/[product reference key]
                Uri usedProductUserUri = Uri.parse(usedProductRef.toString());
                remoteUsedProductsUserKeyReference = usedProductUserUri.getLastPathSegment();

                if (isImageAvailable && detailView.productImageView.getDrawable() != null) {

                    // From: https://firebase.google.com/docs/storage/android/upload-files
                    // Get the data from the ImageView as bytes (as a very small image).
                    detailView.productImageView.setDrawingCacheEnabled(true);
                    detailView.productImageView.buildDrawingCache();

                    Bitmap bitmap = ((BitmapDrawable) detailView.productImageView.getDrawable()).
                            getBitmap();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] productImage = baos.toByteArray();

                    // Create a reference to store the image
                    final StorageReference imageRef = remoteImageStorageReference
                            .child(remoteProductReferenceKey);

                    // Create an upload task
                    UploadTask uploadTask = imageRef.putBytes(productImage);

                    // Create a Task to get the returned URL
                    Task<Uri> urlTask = uploadTask.continueWithTask(task -> {

                        if (!task.isSuccessful()) {
                            // There is an error so delete the temp file
                            if (hasCameraImageTaken) {

                                BitmapUtils.deleteImageFile(this, tempImagePath);
                                hasCameraImageTaken = false;
                            }

                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continue with the task to get the download URL
                        return imageRef.getDownloadUrl();

                    }).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            // Update the member variable with the new image URL
                            remoteImageStorageUri = task.getResult();

                            if (hasCameraImageTaken) {
                                // Now it is stored in FireStore we can delete the temp file
                                BitmapUtils.deleteImageFile(this, tempImagePath);
                                hasCameraImageTaken = false;
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
                            usedProductRef.setValue(Constants.getUserId().getValue());

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
                    usedProductRef.setValue(Constants.getUserId().getValue());

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
        } else if (isExistingProduct &&
                isCreator &&
                !putInUsedList &&
                inUsedList &&
                productDataIsEditable &&
                userProductDataIsEditable &&
                !isMultiUser) {

            // Validate the product base fields
            boolean productFieldsValidated = validateProductFields();

            // If base fields are validated...
            if (productFieldsValidated) {

                // Validate the user product specific fields
                boolean userFieldsValidated = checkValidationUserFields();

                // If the product specific fields are validated...
                if (userFieldsValidated) {
                    // If there has been changes to the image, save them
                    // Location

                    if (isImageAvailable && detailView.productImageView.getDrawable() != null) {

                        // Either:
                        // 1. A new image has been added to an existing product that did not have
                        //    an image or
                        // 2. The existing image has been modified e.g. rotated or
                        // 3. The existing image is being replaced.

                        // From: https://firebase.google.com/docs/storage/android/upload-files
                        // Get the data from the ImageView as bytes and downsize image.
                        detailView.productImageView.setDrawingCacheEnabled(true);
                        detailView.productImageView.buildDrawingCache();

                        Bitmap bitmap = ((BitmapDrawable) detailView.productImageView.
                                getDrawable()).getBitmap();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                        byte[] productImage = baos.toByteArray();
                        // Case 2 & 3
                        // Create a database reference from the existing image reference
                        if (!productModel.getFbStorageImageUri().equals("")) {

                            // Get the existing reference. If you save an image to an existing
                            // Firestore location it changes the download URL by adding a new media
                            // token, so we need to get the new download URL for all writes.
                            remoteImageStorageReference = remoteImageStorageDb
                                    .getReferenceFromUrl(productModel.getFbStorageImageUri());

                            // Create a new database reference for the new image
                        } else if (productModel.getFbStorageImageUri().equals("")) {

                            // Create a reference to store the image
                            remoteImageStorageReference = remoteImageStorageReference
                                    .child(remoteProductReferenceKey);
                        }

                        // Save the new image to FireStore
                        UploadTask uploadTask = remoteImageStorageReference.putBytes(productImage);

                        // Create a Task to get the returned URL
                        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {

                            if (!task.isSuccessful()) {

                                // There is an error so delete the temp file
                                if (hasCameraImageTaken) {
                                    BitmapUtils.deleteImageFile(getParent(), tempImagePath);
                                    hasCameraImageTaken = false;
                                }

                                throw Objects.requireNonNull(task.getException());
                            }

                            // Continue with the task to get the download URL
                            return remoteImageStorageReference.getDownloadUrl();

                        }).addOnCompleteListener(task -> {

                            if (task.isSuccessful()) {

                                // Update the member variable with the new image URL
                                remoteImageStorageUri = task.getResult();

                            /*
                               Collate any changes and update the child data in the database.
                            */

                                // productModel (our reference product) was updated by getUsedList()
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

                        // productModel (our reference product) was updated by getUsedList()
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
        } else if (isExistingProduct &&
                isCreator &&
                !putInUsedList &&
                inUsedList &&
                isMultiUser &&
                !productDataIsEditable &&
                userProductDataIsEditable) {

            // Validate the user product specific fields
            boolean userFieldsValidated = checkValidationUserFields();

            if (userFieldsValidated) {

                /*
                   Collate any changes and update the child data in the database.
                */

                // productModel (our reference product) was updated by getUsedList() when we checked
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
        } else if (isExistingProduct &&
                !isCreator &&
                !inUsedList &&
                putInUsedList &&
                !productDataIsEditable &&
                userProductDataIsEditable) {

            // Validate the user product specific fields
            boolean userFieldsValidated = checkValidationUserFields();

            if (userFieldsValidated) {

                /*
                   Collate any changes and update the child data in the database.
                */

                // productModel (our reference product) was updated by getUsedList() when we checked
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
                    productUserUpdates.put(REMOTE_USED_PRODUCT_ID, remoteUsedProductsUserKeyReference);

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
        } else if (isExistingProduct &&
                !isCreator &&
                inUsedList &&
                !putInUsedList &&
                !productDataIsEditable &&
                userProductDataIsEditable) {

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
        } else if (isExistingProduct &&
                isCreator &&
                !inUsedList &&
                putInUsedList &&
                !productDataIsEditable &&
                userProductDataIsEditable) {

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
                    productUserUpdates.put(REMOTE_USED_PRODUCT_ID, remoteUsedProductsUserKeyReference);

                    // Location 2
                    saveUserProductUpdates(productUserUpdates);
                }
            }
        }
        finish();
    }

    /**
     * Checks for updates to the product data and updates our reference version of productModel with
     * the new values
     */
    private void getProductUpdates() {

        // Add any changes to the base product information
        if (!productModel.getDescription().equals(description)) {
            productModel.setDescription(description);
        }
        if (!madeBy.equals(productModel.getMadeBy())) {
            productModel.setMadeBy(madeBy);
        }
        if (category != productModel.getCategory()) {
            productModel.setCategory(category);
        }
        if (shelfLife != productModel.getShelfLife()) {
            productModel.setShelfLife(shelfLife);
        }
        if (packSize != productModel.getPackSize()) {
            productModel.setPackSize(packSize);
        }
        if (unitOfMeasure != productModel.getUnitOfMeasure()) {
            productModel.setUnitOfMeasure(unitOfMeasure);
        }
        if (packAvePrice != productModel.getPackAvePrice()) {
            productModel.setPackAvePrice(packAvePrice);
        }
        if (!remoteImageStorageUri.toString().equals(productModel.getFbStorageImageUri())) {
            productModel.setFbStorageImageUri(remoteImageStorageUri.toString());
        }

        // Start of user updates
        if (!remoteProductReferenceKey.equals(productModel.getFbProductReferenceKey())) {
            productModel.setFbProductReferenceKey(remoteProductReferenceKey);
        }
        if (!remoteUsedProductsUserKeyReference.equals(productModel.getFbUsedProductsUserKey())) {
            productModel.setFbUsedProductsUserKey(remoteUsedProductsUserKeyReference);
        }
        if (!retailer.equals(productModel.getRetailer())) {
            productModel.setRetailer(retailer);
        }
        if (!locationRoom.equals(productModel.getLocationRoom())) {
            productModel.setLocationRoom(locationRoom);
        }
        if (!locationInRoom.equals(productModel.getLocationInRoom())) {
            productModel.setLocationInRoom(locationInRoom);
        }
        if (packPrice != productModel.getPackPrice()) {
            productModel.setPackPrice(packPrice);
        }
        if (!localImageUri.toString().equals(productModel.getLocalImageUri())) {
            productModel.setLocalImageUri(localImageUri.toString());
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
        productUserUpdates.put(DESCRIPTION, productModel.getDescription());
        productUserUpdates.put(MADE_BY, productModel.getMadeBy());
        productUserUpdates.put(CATEGORY, productModel.getCategory());
        productUserUpdates.put(SHELF_LIFE, productModel.getShelfLife());
        productUserUpdates.put(PACK_SIZE, productModel.getPackSize());
        productUserUpdates.put(UNIT_OF_MEASURE, productModel.getUnitOfMeasure());
        productUserUpdates.put(PROD_COMM_PRICE_AVE, productModel.getPackPrice());
        productUserUpdates.put(CREATED_BY, productModel.getCreatedBy());

        // Add in the user specific data to the HashMap
        productUserUpdates.put(LOCAL_IMAGE_URI, productModel.getLocalImageUri());
        productUserUpdates.put(REMOTE_IMAGE_URI, productModel.getFbStorageImageUri());
        productUserUpdates.put(REMOTE_PRODUCT_ID, productModel.getFbProductReferenceKey());
        productUserUpdates.put(REMOTE_USED_PRODUCT_ID, productModel.getFbUsedProductsUserKey());

        // Compare and add any changes from the user specific fields to the HashMap
        if (!retailer.equals(productModel.getRetailer())) {
            productUserUpdates.put(RETAILER, retailer);
            productModel.setRetailer(retailer);
            userUpdateCounter++;
        }
        if (!locationRoom.equals(productModel.getLocationRoom())) {
            productUserUpdates.put(LOCATION_ROOM, locationRoom);
            productModel.setLocationRoom(locationRoom);
            userUpdateCounter++;
        }
        if (!locationInRoom.equals(productModel.getLocationInRoom())) {
            productUserUpdates.put(LOCATION_IN_ROOM, locationInRoom);
            productModel.setLocationInRoom(locationInRoom);
            userUpdateCounter++;
        }
        if (packPrice != productModel.getPackPrice()) {
            productUserUpdates.put(PRICE, packPrice);
            productModel.setPackPrice(packPrice);
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
        DatabaseReference userProductRef = remoteUserCollectionReference
                .child(Constants.getUserId().getValue())
                .child(Constants.FB_COLLECTION_PRODUCTS)
                .child(remoteProductReferenceKey);

        // Save the changes to the users products
        userProductRef.updateChildren(productUserUpdates);
    }

    /* Saves any product base updates to the database */
    private void saveBaseProductUpdates(Map<String, Object> productBaseUpdates) {

        // Update the product specific information in Firebase
        DatabaseReference reference = remoteProductCollectionReference
                .child(remoteProductReferenceKey);

        reference.updateChildren(productBaseUpdates);
    }

    /*
    Adds the current user ID to:
    /collection_used_products/[product ref key]/[used products user key]/[user id]
    */
    private void addUserToUsedProducts() {

        // Create the database reference
        DatabaseReference usedProductRef = remoteUsedProductsCollectionReference
                .child(remoteProductReferenceKey).push();

        // Extract the /used_products/user/[product reference key]
        Uri usedProductUserUri = Uri.parse(usedProductRef.toString());
        remoteUsedProductsUserKeyReference = usedProductUserUri.getLastPathSegment();

        // Add the users ID
        usedProductRef.setValue(Constants.getUserId().getValue());
    }

    private boolean validateProductFields() {

        if (!descriptionValidated) {
            validateTextField(detailView.productEditable.
                    editableDescription.getText().toString().trim());

        } else if (!madeByValidated) {
            validateTextField(detailView.productEditable.
                    editableMadeBy.getText().toString().trim());

        } else if (!packSizeValidated) {
            validatePackSize(String.valueOf(detailView.productEditable.
                    editablePackSize.getText().toString().trim()));

        } else if (!uoMValidated) {
            unitOfMeasure = detailView.productEditable.
                    spinnerUnitOfMeasure.getSelectedItemPosition();
            validateUnitOfMeasure();

        } else if (!shelfLifeValidated) {
            shelfLife = detailView.productEditable.spinnerShelfLife.getSelectedItemPosition();
            validateShelfLife();

        } else if (!categoryValidated) {
            category = detailView.productEditable.spinnerCategory.getSelectedItemPosition();
            validateCategory();
        }

        return descriptionValidated &&
                madeByValidated &&
                packSizeValidated &&
                uoMValidated &&
                shelfLifeValidated &&
                categoryValidated;
    }

    private boolean checkValidationUserFields() {

        if (!retailerValidated) {
            validateTextField(detailView.userProductEditable.
                    editableRetailer.getText().toString().trim());

        } else if (!packPriceValidated && !TextUtils.isEmpty(detailView.
                userProductEditable.editablePrice.getText().toString())) {
            validatePackPrice();

        } else if (!locationRoomValidated) {
            validateTextField(detailView.userProductEditable.
                    editableLocationRoom.getText().toString().trim());

        } else if (!locationInRoomValidated) {
            validateTextField(detailView.userProductEditable.
                    editableLocationInRoom.getText().toString().trim());
        }

        return retailerValidated &&
                packPriceValidated &&
                locationRoomValidated &&
                locationInRoomValidated;
    }

    /* Converts the products base data to an object map */
    // Todo - use the mapping in the product class
    private Map<String, Object> convertBaseProductToMap() {

        // Create the base product information for /collection/products location
        ProductModel newProductData = new ProductModel();
        newProductData.setDescription(description);
        newProductData.setMadeBy(madeBy);
        newProductData.setCategory(category);
        newProductData.setShelfLife(shelfLife);
        newProductData.setPackSize(packSize);
        newProductData.setUnitOfMeasure(unitOfMeasure);
        newProductData.setPackAvePrice(Constants.DEFAULT_PRODUCT_PRICE_AVERAGE);
        newProductData.setCreatedBy(Constants.getUserId().getValue());
        newProductData.setFbStorageImageUri(remoteImageStorageUri.toString());

        // Convert to a map
        return newProductData.commProductToMap();
    }

    /* Given the base updates, converts the entire product data to a map object */
    // Todo - use the mapping in the product class
    private Map<String, Object> convertProductToMap() {

        ProductModel newProductData = new ProductModel();

        // Add in the base data
        newProductData.setDescription(description);
        newProductData.setMadeBy(madeBy);
        newProductData.setCategory(category);
        newProductData.setShelfLife(shelfLife);
        newProductData.setPackSize(packSize);
        newProductData.setUnitOfMeasure(unitOfMeasure);
        newProductData.setPackAvePrice(Constants.DEFAULT_PRODUCT_PRICE_AVERAGE);
        newProductData.setCreatedBy(Constants.getUserId().getValue());
        newProductData.setFbStorageImageUri(remoteImageStorageUri.toString());

        // Add in the additional values and keys
        newProductData.setFbProductReferenceKey(remoteProductReferenceKey);
        newProductData.setFbUsedProductsUserKey(remoteUsedProductsUserKeyReference);
        newProductData.setRetailer(retailer);
        newProductData.setLocationRoom(locationRoom);
        newProductData.setLocationInRoom(locationInRoom);
        newProductData.setPackPrice(packPrice);
        newProductData.setLocalImageUri(localImageUri.toString());

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
        if (!Uri.EMPTY.equals(Uri.parse(productModel.getFbStorageImageUri()))) {
            Picasso.get().load(productModel.getFbStorageImageUri()).
                    into(detailView.productImageView);
        }

        // Product fields editable criteria:
        // 1. Has to be creator of product
        // 2. Base fields have to be editable
        if (isCreator && productDataIsEditable) {

            // Update the EditText fields for the product information
            detailView.productEditable.editableDescription.setText(productModel.getDescription());
            detailView.productEditable.editableMadeBy.setText(productModel.getMadeBy());
            detailView.productEditable.editablePackSize.setText(String.valueOf(productModel.getPackSize()));
            detailView.productEditable.spinnerUnitOfMeasure.setSelection(productModel.getUnitOfMeasure());
            detailView.productEditable.spinnerShelfLife.setSelection(productModel.getShelfLife());
            detailView.productEditable.spinnerCategory.setSelection(productModel.getCategory());

            // Base fields uneditable criteria:
            // 1. View has to be visible
        } else if (!productDataIsEditable) {

            // Update the TextViews
            detailView.productUneditable.description.setText(productModel.getDescription());
            detailView.productUneditable.madeBy.setText(productModel.getMadeBy());
            detailView.productUneditable.packSize.setText(String.valueOf(productModel.getPackSize()));
            detailView.productUneditable.unitOfMeasure.setText(Converters.getUnitOfMeasureString(
                    this, productModel.getUnitOfMeasure()));
            detailView.productUneditable.shelfLife.setText(Converters.getShelfLifeString(
                    this, productModel.getShelfLife()));
            detailView.productUneditable.category.setText(Converters.getCategoryString(
                    this, productModel.getCategory()));
        }

        // User specific fields editable criteria:
        // 1. Has to be on the users used product list or about to be on it
        // 2. User fields have to be editable
        if ((inUsedList ||
                putInUsedList) &&
                userProductDataIsEditable) {

            detailView.userProductEditable.editableRetailer.setText(productModel.getRetailer());
            detailView.userProductEditable.editablePrice.setText(String.valueOf(productModel.getPackPrice()));
            detailView.userProductEditable.editableLocationRoom.setText(productModel.getLocationRoom());
            detailView.userProductEditable.editableLocationInRoom.setText(productModel.getLocationInRoom());

            // Set the menu items for this UI state
            invalidateOptionsMenu();

            // User specific fields uneditable container criteria:
            // 1. Has to be in the users used product list.
            // 2. Uneditable status has to be true.
            // Todo - Why  are userProductDataIsEditable is always false, this may be correct behaviour
        } else if (inUsedList &&
                !userProductDataIsEditable) {

            // Update the uneditable views
            detailView.userProductUneditable.retailer.setText(productModel.getRetailer());
            NumberFormat format = NumberFormat.getCurrencyInstance();
            detailView.userProductUneditable.price.setText(
                    String.valueOf(format.format(productModel.getPackPrice())));
            detailView.userProductUneditable.locationRoom.setText(productModel.getLocationRoom());
            detailView.userProductUneditable.locationInRoom.setText(productModel.getLocationInRoom());

            // This product is all ready in the users used list so remove the FAB
            detailView.fab.setVisibility(View.GONE);

            // This call invalidates the current menu options and calls onPrepareOptionsMenu()
            // which will in turn populate the menu with the correct icons for this view.
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        /* Save the current product */
        outState.putString(Constants.USER_ID_KEY, Constants.getUserId().getValue());
        // outState.putParcelable(Constants.PRODUCT_KEY, productModel);
        outState.putString(Constants.PRODUCT_FB_REFERENCE_KEY, remoteProductReferenceKey);

        // Save the bool's!
        outState.putBoolean(Constants.PRODUCT_IS_CREATOR_KEY, isCreator);
        outState.putBoolean(EXISTING_PRODUCT, isExistingProduct);
        outState.putBoolean(PUT_IN_USED_LIST, putInUsedList);
        outState.putBoolean(IN_USED_LIST, inUsedList);
        outState.putBoolean(PRODUCT_FIELDS_EDITABLE, productDataIsEditable);
        outState.putBoolean(USER_FIELDS_EDITABLE, userProductDataIsEditable);

    }

    /* Get a reference to all of the views */
    private void initialiseViews() {

        detailView = DataBindingUtil.setContentView(this, R.layout.product_detail_shell);
        setSupportActionBar(detailView.toolbar);

        setupUoMSpinner();
        setupShelfLifeSpinner();
        setupCategorySpinner();

        detailView.productEditable.productContainer.setVisibility(View.GONE);
        productDataIsEditable = false;
        detailView.userProductEditable.productUserContainer.setVisibility(View.GONE);
        userProductDataIsEditable = false;

        detailView.productUneditable.productContainer.setVisibility(View.GONE);
        detailView.userProductUneditable.productUserContainer.setVisibility(View.GONE);

        detailView.pictureFromCameraButton.setOnClickListener(v -> requestPermissions());
        detailView.rotatePictureButton.setOnClickListener(v -> rotateImage());
        detailView.fab.setOnClickListener(v -> openProductUserDataFields());
        detailView.pictureFromGalleryButton.setOnClickListener(v -> launchGallery());

        /* Description text and focus change listeners */
        detailView.productEditable.editableDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Validate the text as it changes
                descriptionValidated = validateTextField(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        detailView.productEditable.editableDescription.setOnFocusChangeListener((v, hasFocus) -> {

            // When this field gains focus its content may be changing, if so it will not be
            // validated. When this field looses focus, its content will need validating.
            descriptionValidated = !hasFocus && validateTextField(detailView.
                    productEditable.editableDescription.getText().toString());
        });

        detailView.productEditable.editableMadeBy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                madeByValidated = validateTextField(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        detailView.productEditable.editableMadeBy.setOnFocusChangeListener((v, hasFocus)
                -> madeByValidated = !hasFocus && validateTextField(
                detailView.productEditable.editableMadeBy.getText().toString()));

        // Change listener for the 'pack size' EditText field
        detailView.productEditable.editablePackSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                packSizeValidated = validatePackSize(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Focus change listener for the 'pack size' EditText field */
        detailView.productEditable.editablePackSize.setOnFocusChangeListener((v, hasFocus) -> {

            // When this field gains focus its content may be changing, if so it will not be
            // validated. When this field looses focus, its content will need validating.
            packSizeValidated = !hasFocus && validatePackSize(
                    detailView.productEditable.editablePackSize.getText().toString());
        });

        // Focus change listener for the 'unit of measure' spinner
        detailView.productEditable.spinnerUnitOfMeasure.
                setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    if (hasFocus) {
                        uoMValidated = false;
                        hideKeyboard();

                        detailView.productEditable.spinnerUnitOfMeasure.performClick();

                    } else {

                        // Validate its contents
                        uoMValidated = validateUnitOfMeasure();

                        // If validated remove the error
                        if (uoMValidated) {
                            detailView.productEditable.unitOfMeasureError.setError(null);
                        }
                    }
                });

        // Focus change listener for the 'shelf life' spinner
        detailView.productEditable.spinnerShelfLife.setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    if (hasFocus) {

                        shelfLifeValidated = false;
                        hideKeyboard();
                        detailView.productEditable.spinnerShelfLife.performClick();
                    } else {
                        // Validate its contents
                        shelfLifeValidated = validateShelfLife();
                    }
                });

        // Focus change listener for the 'category' spinner
        detailView.productEditable.spinnerCategory.setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    if (hasFocus) {
                        categoryValidated = false;
                        hideKeyboard();
                        detailView.productEditable.spinnerCategory.performClick();
                    } else {
                        // Validate its contents
                        categoryValidated = validateCategory();
                    }
                });

        // Retailer text and focus change listeners */
        detailView.userProductEditable.editableRetailer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                retailerValidated = validateTextField(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        detailView.userProductEditable.editableRetailer.setOnFocusChangeListener((v, hasFocus)
                -> retailerValidated = !hasFocus && validateTextField(detailView.
                userProductEditable.editableRetailer.getText().toString().trim()));

        // Focus change listener for the 'pack price' EditText field
        detailView.userProductEditable.editablePrice.setOnFocusChangeListener((v, hasFocus) -> {

                    // When this field gains focus its content may be changing, if so it will not be
                    // validated. When this field looses focus, its content will need validating.
                    packPriceValidated = !hasFocus && validatePackPrice();
                });

        detailView.userProductEditable.editableLocationRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locationRoomValidated = validateTextField(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        detailView.userProductEditable.editableLocationRoom.setOnFocusChangeListener((v, hasFocus) -> {
            locationRoomValidated = !hasFocus && validateTextField(
                    detailView.userProductEditable.editableLocationRoom.getText().toString().trim());
        });

        detailView.userProductEditable.editableLocationInRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locationInRoomValidated = validateTextField(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        detailView.userProductEditable.editableLocationInRoom.setOnFocusChangeListener((v, hasFocus) -> {
            locationInRoomValidated = !hasFocus && validateTextField(
                    detailView.userProductEditable.editableLocationInRoom.getText().toString().trim());
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
        if (!inUsedList) {

            // Make the user specific fields visible
            usersProductInfoFieldsAreEditable(true);

            // This product is going to be added to the users used list so update the bool
            putInUsedList = true;

            // Turn off the FAB
            detailView.fab.setVisibility(View.GONE);

            // In the user specific fields set the focus to the first field
            detailView.userProductEditable.editableRetailer.requestFocus();

            // Show the save button
            setMenuItemVisibility(true, false, false);
        }
    }

    /* Rotates the image in the image view */
    private void rotateImage() {

        // Rotate the image by 90 degrees
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap RecipeImage = ((BitmapDrawable) detailView.productImageView.getDrawable()).getBitmap();

        Bitmap rotated = Bitmap.createBitmap(RecipeImage, 0, 0,
                RecipeImage.getWidth(), RecipeImage.getHeight(), matrix, true);

        detailView.productImageView.setImageBitmap(rotated);

        // The image has changed so update the bool
        isImageAvailable = true;
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
        detailView.productEditable.productContainer.setVisibility(View.GONE);
        detailView.userProductEditable.productUserContainer.setVisibility(View.GONE);

        // Make relevant view containers visible
        detailView.productUneditable.productContainer.setVisibility(View.VISIBLE);

        // Only show the 'user specific product data container' if in used list
        if (inUsedList) {

            detailView.userProductUneditable.productUserContainer.setVisibility(View.VISIBLE);
        } else {

            // This product is not in the users used list so we have no information as to how they
            // use it. So set the user fields to gone.
            detailView.userProductUneditable.productUserContainer.setVisibility(View.GONE);

            // Show the fab so they can add this product to their list if they wish.
            detailView.fab.setVisibility(View.VISIBLE);
        }
    }

    /* Sets the visibility and editable properties for the base field views */
    private void productFieldsAreEditable(boolean newStatus) {
        productDataIsEditable = newStatus;

        if (productDataIsEditable) {

            // Base fields are editable, set the editable container to visible.
            detailView.productEditable.productContainer.setVisibility(View.VISIBLE);

            // We are now editing as opposed to viewing so set the uneditable container to gone.
            detailView.productUneditable.productContainer.setVisibility(View.GONE);
        } else {

            // Base fields are uneditable, so set the editable fields to gone.
            detailView.productEditable.productContainer.setVisibility(View.GONE);

            // And set the uneditable container to visible.
            detailView.productUneditable.productContainer.setVisibility(View.VISIBLE);
        }
    }

    /* Sets the visibility properties for the editable and non editable user specific views */
    private void usersProductInfoFieldsAreEditable(boolean newStatus) {
        userProductDataIsEditable = newStatus;

        if (userProductDataIsEditable) {

            detailView.userProductEditable.productUserContainer.setVisibility(View.VISIBLE);
            detailView.userProductUneditable.productUserContainer.setVisibility(View.GONE);

        } else {

            detailView.userProductEditable.productUserContainer.setVisibility(View.GONE);

            // If the product is on or is going to be on the users used list turn the user
            // specific fields on
            if (inUsedList || putInUsedList) {

                detailView.userProductUneditable.productUserContainer.setVisibility(View.VISIBLE);

            } else {

                // If not on the users used list turn the view off
                detailView.userProductUneditable.productUserContainer.setVisibility(View.GONE);
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

        if (isExistingProduct && inUsedList) {

            // For the product information located under the users product list, create a database
            // reference that needs removing
            DatabaseReference userProductRef = remoteUserCollectionReference
                    .child(Constants.getUserId().getValue())
                    .child(Constants.FB_COLLECTION_PRODUCTS)
                    .child(remoteProductReferenceKey);

            // Then remove the value
            userProductRef.removeValue().addOnCompleteListener(task -> {

                // For the product information located under the 'used_products'
                // collection, create the database reference that needs removing.
                DatabaseReference usedProductsRef = remoteUsedProductsCollectionReference
                        .child(remoteProductReferenceKey)
                        .child(productModel.getFbUsedProductsUserKey());

                // Then remove the value.
                usedProductsRef.removeValue().addOnCompleteListener(task1 -> {

                    /*
                       Check to see if this product is being used by anyone else. If not remove it
                       from the database.
                    */

                    // Get a reference to: /collection_used_products/[product ID]/[used product ref].
                    Query usedProductRef = remoteUsedProductsCollectionReference
                            .child(remoteProductReferenceKey).limitToFirst(2);

                    usedProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // If getValue() is null there are no other users using this product, it
                            // is therefore safe to delete it from /collection_products/
                            if (dataSnapshot.getValue() == null) {

                                DatabaseReference deleteProductRef = remoteProductCollectionReference
                                        .child(remoteProductReferenceKey);

                                // Remove the value.
                                deleteProductRef.removeValue();

                                // If there is an image stored with this product, delete it.
                                if (!TextUtils.isEmpty(remoteImageStorageUri.toString())) {

                                    StorageReference imageRef = remoteImageStorageDb
                                            .getReferenceFromUrl(remoteImageStorageUri.toString());
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
                tempImagePath = photoFile.getAbsolutePath();

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
            isImageAvailable = true;
            hasCameraImageTaken = true;

        } else if (requestCode == Constants.REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_CANCELED) {

            // If the camera was cancelled, delete the temporary file
            BitmapUtils.deleteImageFile(this, tempImagePath);
            hasCameraImageTaken = false;

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_OK) {

            isImageAvailable = true;
            processAndSetImage();

        } else if (requestCode == Constants.REQUEST_IMAGE_MEDIA_STORE &&
                resultCode == RESULT_CANCELED) {

            Log.e(TAG, "Media store intent cancelled");

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER && resultCode == RESULT_OK) {

            // Photo picker
            Uri selectedImageUri = data.getData();
            isImageAvailable = true;
            detailView.productImageView.setImageURI(selectedImageUri);

        } else if (requestCode == Constants.REQUEST_IMAGE_PICKER && resultCode == RESULT_CANCELED) {

            Log.e(TAG, "Image picker intent cancelled");
        }
    }

    /* Resample's the image so it fits our imageView and uses less resources */
    private void processAndSetImage() {

        Bitmap mResultsBitmap = BitmapUtils.resampleImage(this, null, tempImagePath);

        detailView.productImageView.setImageBitmap(mResultsBitmap);
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
                R.id.spinner_unit_of_measure);

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
                        unitOfMeasure = getResources().getInteger(R.integer.item_not_selected);

                    } else if (selection.equals(getString(R.string.uom_option_1))) {
                        unitOfMeasure = getResources().getInteger(R.integer.uom_grams_int);

                    } else if (selection.equals(getString(R.string.uom_option_2))) {
                        unitOfMeasure = getResources().getInteger(R.integer.uom_milliliter_int);

                    } else if (selection.equals(getString(R.string.uom_option_3))) {
                        unitOfMeasure = getResources().getInteger(R.integer.uom_count_int);
                    }
                }

                // Validate the input
                uoMValidated = validateUnitOfMeasure();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                unitOfMeasure = getResources().getInteger(R.integer.item_not_selected);
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the shelf life of the product.
     */
    private void setupShelfLifeSpinner() {

        Spinner shelfLifeSpinner = findViewById(
                R.id.spinner_shelf_life);

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
                        shelfLife = getResources().getInteger(R.integer.item_not_selected);

                    } else if (selection.equals(getString(R.string.shelf_life_option_1))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_1);

                    } else if (selection.equals(getString(R.string.shelf_life_option_2))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_2);

                    } else if (selection.equals(getString(R.string.shelf_life_option_3))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_3);

                    } else if (selection.equals(getString(R.string.shelf_life_option_4))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_4);

                    } else if (selection.equals(getString(R.string.shelf_life_option_5))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_5);

                    } else if (selection.equals(getString(R.string.shelf_life_option_6))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_6);

                    } else if (selection.equals(getString(R.string.shelf_life_option_7))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_7);

                    } else if (selection.equals(getString(R.string.shelf_life_option_8))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_8);

                    } else if (selection.equals(getString(R.string.shelf_life_option_9))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_9);

                    } else if (selection.equals(getString(R.string.shelf_life_option_10))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_10);

                    } else if (selection.equals(getString(R.string.shelf_life_option_11))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_11);

                    } else if (selection.equals(getString(R.string.shelf_life_option_12))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_12);

                    } else if (selection.equals(getString(R.string.shelf_life_option_13))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_13);

                    } else if (selection.equals(getString(R.string.shelf_life_option_14))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_14);

                    } else if (selection.equals(getString(R.string.shelf_life_option_15))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_15);

                    } else if (selection.equals(getString(R.string.shelf_life_option_16))) {
                        shelfLife = getResources().getInteger(R.integer.shelf_life_option_16);
                    }
                }
                // Validate the input
                shelfLifeValidated = validateShelfLife();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                shelfLife = getResources().getInteger(R.integer.item_not_selected);
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the category for a product.
     */
    private void setupCategorySpinner() {

        Spinner categorySpinner = findViewById(
                R.id.spinner_category);

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
                        category = getResources().getInteger(R.integer.item_not_selected);

                    } else if (selection.equals(getString(R.string.product_category_option_1))) {
                        category = getResources().getInteger(R.integer.category_option_1);

                    } else if (selection.equals(getString(R.string.product_category_option_2))) {
                        category = getResources().getInteger(R.integer.category_option_2);
                    }
                }
                // Validate the input field
                categoryValidated = validateCategory();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = R.string.product_category_option_0;
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

        // Update the member variable
        editorMenuBar = menu;

        /* Initial status bar options */
        setMenuItemVisibility(false, false, false);

        // If product is a new product, hide the "Delete" and "Edit"  menu items.
        if (remoteProductReferenceKey.equals(Constants.DEFAULT_FB_PRODUCT_ID)) {
            setMenuItemVisibility(true, false, false);
        }

        // If existing product, is not the creator, is in the used list, user fields editable.
        if (isExistingProduct &&
                !isCreator &&
                inUsedList &&
                userProductDataIsEditable) {
            setMenuItemVisibility(true, false, false);
        }

        // Existing product, user is not creator, is in used list, multi-user is not yet known, base
        // fields are not editable, user fields are not editable
        if (isExistingProduct &&
                !isCreator &&
                inUsedList &&
                !isMultiUser &&
                !productDataIsEditable &&
                !userProductDataIsEditable) {
            setMenuItemVisibility(false, true, true);
        }

        // Existing product, user is creator, is in the users used list. Ui state is uneditable.
        if (isExistingProduct &&
                isCreator &&
                inUsedList &&
                !productDataIsEditable &&
                !userProductDataIsEditable) {
            // Uneditable state
            setMenuItemVisibility(false, true, true);
        }

        // Existing product, user is creator, is in users used list, both editable containers are
        // visible.
        if (isExistingProduct &&
                isCreator &&
                inUsedList &&
                productDataIsEditable &&
                userProductDataIsEditable) {
            // Editable state
            setMenuItemVisibility(true, false, true);
        }

        // Existing product, user is creator, is in users used list, base container is uneditable
        // user container is editable.
        if (isExistingProduct &&
                isCreator &&
                inUsedList &&
                !productDataIsEditable &&
                userProductDataIsEditable) {
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

    private void editProduct() {

        if (isExistingProduct && !isCreator && inUsedList) {

            setTitle(R.string.activity_title_edit_product);

            // Make the user specific fields editable
            usersProductInfoFieldsAreEditable(true);

            // Update the menu items
            invalidateOptionsMenu();

            // Update the data in the Ui
            populateUi();
        }

        if (isExistingProduct && isCreator && inUsedList) {
            // Check to see if more than one user is using this product. This is a
            // database operation and may take some time. We cannot proceed until we get the
            // result. So continue operations from method checkMultiUserStatus()
            checkMultiUserStatus();
        }

        // Request focus to the first editable filed
        detailView.userProductEditable.editableRetailer.requestFocus();

        // Make the ImageView editable
        detailView.pictureFromCameraButton.setVisibility(View.VISIBLE);
        detailView.rotatePictureButton.setVisibility(View.VISIBLE);
        detailView.pictureFromGalleryButton.setVisibility(View.VISIBLE);
    }

    /* Sets the visibility properties of the menu items */
    private void setMenuItemVisibility(boolean saveItem, boolean editItem, boolean deleteItem) {

        editorMenuBar.findItem(R.id.menu_product_editor_action_save).setVisible(saveItem);
        editorMenuBar.findItem(R.id.menu_product_editor_action_edit).setVisible(editItem);
        editorMenuBar.findItem(R.id.menu_product_editor_action_delete).setVisible(deleteItem);
    }

    // Animation for the transition between activities */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
