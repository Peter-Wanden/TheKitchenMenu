package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.TKMDatabase;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProduct;

import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogProductList;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class ActivityCatalogProduct
        extends
        AppCompatActivity
        implements
        AdapterCatalogProduct.ProductCatalogAdapterOnClickHandler {

    public static final String LOG_TAG = ActivityCatalogProduct.class.getSimpleName();

    /* Adapter for the product list view */
    public AdapterCatalogProduct mCatalogAdapter;

    /* RecyclerView for the list view */
    private RecyclerView mRecyclerView;

    /* *******************
     * Firebase database *
     *********************/
    /* Authentication instance */
    private FirebaseAuth mFBAuth;

    /* Authentication state listener */
    private FirebaseAuth.AuthStateListener mFBAuthStateListener;

    /* Authentication users unique ID generated for this user / app combination */
    private String mUserUid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_product);

        // If the user is not logged in
        mUserUid = Constants.ANONYMOUS;

        // Get an instance of Firebase authentication */
        mFBAuth = FirebaseAuth.getInstance();

        setupViews();

        /* Create the adapter and pass in the this classes context and the listener which is also
        this class, as this class implements the click handler. */
        mCatalogAdapter = new AdapterCatalogProduct(this, this);

        mRecyclerView.setAdapter(mCatalogAdapter);

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

    /* Clean up tasks after sign out */
    private void onSignedOutCleanUp() {

        // Nullify the user unique ID
        mUserUid = Constants.ANONYMOUS;

        // Clear out the adapter
        mCatalogAdapter.setProducts(null);
    }

    /* User is now signed in - attach to the Firebase database */
    private void onSignedInInitialise(String userUid) {

        // Update the user id
        mUserUid = userUid;
        mCatalogAdapter.setUserId(mUserUid);

        // We can only get and set the data once the user has signed in, as we need the user ID

        // LiveData and ViewModel for Firebase
        ViewModelCatalogProductList viewModelCatalogProducts =
                ViewModelProviders.of(this).get(ViewModelCatalogProductList.class);

        LiveData<List<Product>> productLiveData = viewModelCatalogProducts.getProductsLiveData();

        productLiveData.observe(this, products -> {
            if(products != null) {
                mCatalogAdapter.setProducts(products);
                // Loop through live data products and set them to the local database
            }
        });
    }

    private void setupViews() {

        /* Get a reference to the views */
        mRecyclerView = findViewById(R.id.activity_catalog_product_rv);
        FloatingActionButton mFab = findViewById(R.id.activity_catalog_product_fab);

        /* Create and set the layout manager */
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mFab.setOnClickListener(v -> {
            Intent addProductIntent = new Intent(ActivityCatalogProduct.this,
                    ActivityDetailProduct.class);
            startActivity(addProductIntent);
        });
    }

    /**
     * A product has been clicked in the RecyclerView. Add the Firebase product to an intent
     * and go to ActivityDetailProduct.
     * @param fbProduct - The selected product.
     */
    @Override
    public void onClick(Product fbProduct, boolean isCreator) {

        Intent intent = new Intent(
                ActivityCatalogProduct.this,
                ActivityDetailProduct.class);
        intent.putExtra(Constants.PRODUCT_FB_REFERENCE_KEY, fbProduct);
        intent.putExtra(Constants.PRODUCT_IS_CREATOR_KEY, isCreator);
        startActivity(intent);
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
        if (mFBAuthStateListener != null) {
            // Remove the firebase authentication state listener from the authentication instance
            mFBAuth.removeAuthStateListener(mFBAuthStateListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_catalog, menu);
        return true;
    }

    // Handles the menu sign out button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                // Sign the user out
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
