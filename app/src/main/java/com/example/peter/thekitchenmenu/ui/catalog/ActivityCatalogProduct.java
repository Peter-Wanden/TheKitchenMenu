package com.example.peter.thekitchenmenu.ui.catalog;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.databinding.ActivityCatalogProductBinding;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProduct;

import com.example.peter.thekitchenmenu.utils.GsonUtils;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatalogProductList;
import com.example.peter.thekitchenmenu.widget.WidgetService;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ActivityCatalogProduct
        extends
        AppCompatActivity
        implements
        AdapterCatalogProduct.ProductCatalogAdapterOnClickHandler {

//    public static final String LOG_TAG = ActivityCatalogProduct.class.getSimpleName();

    /* Adapter for the product list view */
    public AdapterCatalogProduct mCatalogAdapter;

    /* *******************
     * Firebase database *
     *********************/
    /* Authentication instance */
    private FirebaseAuth mFBAuth;

    /* Authentication state listener */
    private FirebaseAuth.AuthStateListener mFBAuthStateListener;

    /* Authentication users unique ID generated for this user / app combination */
    private String mUserUid;

    ActivityCatalogProductBinding mCatalogProductBinding;
    GridLayoutManager mGridManager;
    LinearLayoutManager mLinearManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCatalogProductBinding = DataBindingUtil.
                setContentView(this, R.layout.activity_catalog_product);

        setupViews();

        // If the user is not logged in
        mUserUid = Constants.ANONYMOUS;

        // Get an instance of Firebase authentication */
        mFBAuth = FirebaseAuth.getInstance();

        // Get a remote configuration instance
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        /* Create the adapter and pass in the this classes context and the listener which is also
        this class, as this class implements the click handler. */
        mCatalogAdapter = new AdapterCatalogProduct(this, this);

        mCatalogProductBinding.activityCatalogProductRv.setAdapter(mCatalogAdapter);

        // Firebase authentication listener (attached in onResume and detached in onPause).
        // From: Udacity AND Firebase
        mFBAuthStateListener = firebaseAuth -> {
            // Find out if the user is logged in or not
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                onSignedInInitialise(user.getUid());
            } else {
                // User is signed out
                onSignedOutCleanUp();
                // For more examples of sign in see:
                // https://github.com/firebase/FirebaseUI-Android/tree/master/auth#sign-in
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

        // Create Remote Config Setting to enable developer mode.
        // Fetching configs from the server is normally limited to 5 requests per hour.
        // Enabling developer mode allows many more requests to be made per hour, so developers
        // can test different config values during development.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
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

        // Create a shared preferences object. This will hold the base data for the widget
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        productLiveData.observe(this, products -> {

            if (products != null) {
                mCatalogAdapter.setProducts(products);

                // Set the product list to shared preferences for the widgets adapter
                preferences
                        .edit()
                        .putString(Constants.PRODUCT_KEY, GsonUtils.productsToJson(products))
                        .apply();

                /* Update the widget with the new list od products */
                WidgetService.startActionUpdateWidget(this);
            }
        });
    }

    private void setupViews() {

        /* Creates RecyclerViews with dynamic widths depending on the display type. */
        // Portrait for phone.
        // Landscape for phone.
        // Portrait for tablet.
        // Landscape for tablet.
        if (getResources().getBoolean(R.bool.is_tablet) ||
                getResources().getBoolean(R.bool.is_landscape)) {

            mGridManager = new GridLayoutManager(
                    Objects.requireNonNull(this)
                            .getApplicationContext(), columnCalculator());

            mCatalogProductBinding.activityCatalogProductRv.setLayoutManager(mGridManager);

        } else {
            mLinearManager = new
                    LinearLayoutManager(Objects.requireNonNull(this)
                    .getApplicationContext(),
                    LinearLayoutManager.VERTICAL, false);

            mCatalogProductBinding.activityCatalogProductRv.setLayoutManager(mLinearManager);
        }

        mCatalogProductBinding.activityCatalogProductRv.setHasFixedSize(true);

        mCatalogProductBinding.activityCatalogProductFab.setOnClickListener(v -> {

            Intent addProductIntent = new Intent(ActivityCatalogProduct.this,
                    ActivityDetailProduct.class);
            startActivity(addProductIntent);
        });

        setSupportActionBar(mCatalogProductBinding.activityCatalogProductToolbar);
    }

    /* Screen width column calculator */
    private int columnCalculator() {

        DisplayMetrics metrics = new DisplayMetrics();
        Objects.requireNonNull(this)
                .getWindowManager()
                .getDefaultDisplay()
                .getMetrics(metrics);

        // Width of smallest tablet
        int divider = 600;
        int width = metrics.widthPixels;
        int columns = width / divider;
        if (columns < 2) return 2;

        return columns;
    }

    /**
     * A product has been clicked in the RecyclerView. Add the Firebase product to an intent
     * and go to ActivityDetailProduct.
     *
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
        // As the user is now logged out its good practice to clean up the adapter and detach
        // the DB listener
        mCatalogAdapter.setProducts(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_catalog, menu);
        return true;
    }

    /* Handles the menu sign out button */
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

    /* Handle sign in / out */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, R.string.sign_in_conformation, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity, exit the app
                Toast.makeText(this, R.string.sign_in_canceled, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
