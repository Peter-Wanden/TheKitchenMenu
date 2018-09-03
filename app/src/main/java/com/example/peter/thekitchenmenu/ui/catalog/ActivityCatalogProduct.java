package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.databinding.ActivityCatalogProductBinding;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProduct;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;
import java.util.Objects;

public class ActivityCatalogProduct
        extends
        AppCompatActivity {

    public static final String LOG_TAG = ActivityCatalogProduct.class.getSimpleName();

    /* *******************
     * Firebase database *
     *********************/
    /* Authentication instance */
    private FirebaseAuth mFBAuth;

    /* Authentication state listener */
    private FirebaseAuth.AuthStateListener mFBAuthStateListener;

    /* Authentication users unique ID generated for this user / app combination */
    private String mUserId;

    /* Binding class for the views */
    ActivityCatalogProductBinding mCatalogProductBinding;

    /* Adapter for the ViewPager */
    AdapterPageCatalogProduct mAdapterPageCatalogProduct;

    /* ViewPager to swipe through the fragments */
    ViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseFirebase();
        initialiseViews();

        // If the user is not logged in
        mUserId = Constants.ANONYMOUS;

        // TODO - Save the fragments state

        // Post configuration change, restores the state of the previous layout manager to the new
        // layout manager which could be either a grid or linear layout manager
//        if (savedInstanceState !=null && savedInstanceState.containsKey("layoutManagerState")) {
//            mLayoutManagerState = savedInstanceState.getParcelable("layoutManagerState");
//        }
    }

    /* Initialises the Firebase components required for this activity and logs the user in */
    private void initialiseFirebase() {

        // Get an instance of Firebase authentication */
        mFBAuth = FirebaseAuth.getInstance();

        // Get a remote configuration instance
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

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

    /* Sets up the views for this activity */
    private void initialiseViews() {

        mCatalogProductBinding = DataBindingUtil.
                setContentView(this, R.layout.activity_catalog_product);

        mCatalogProductBinding.activityCatalogProductPb.setVisibility(View.GONE);

        /* Creates RecyclerViews with dynamic widths depending on the display type. */
        // Portrait for phone.
        // Landscape for phone.
        // Portrait for tablet.
        // Landscape for tablet.
//        if (getResources().getBoolean(R.bool.is_tablet) ||
//                getResources().getBoolean(R.bool.is_landscape)) {
//
//            mGridManager = new GridLayoutManager(
//                    Objects.requireNonNull(this)
//                            .getApplicationContext(), columnCalculator());
//
//            mCatalogProductBinding.activityCatalogProductRv.setLayoutManager(mGridManager);
//
//        } else {
//            mLinearManager = new
//                    LinearLayoutManager(Objects.requireNonNull(this)
//                    .getApplicationContext(),
//                    LinearLayoutManager.VERTICAL, false);
//
//            mCatalogProductBinding.activityCatalogProductRv.setLayoutManager(mLinearManager);
//        }

//        mCatalogProductBinding.activityCatalogProductRv.setHasFixedSize(true);

        mCatalogProductBinding.activityCatalogProductFab.setOnClickListener(v -> {

            Intent addProductIntent = new Intent(ActivityCatalogProduct.this,
                    ActivityDetailProduct.class);
            startActivity(addProductIntent);

            // Sliding animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        /* Create the adapter and pass in this classes context and the listener which is also
        this class, as this class implements the click handler. */
//        mCatalogAdapter = new AdapterCatalogProduct(this, this);
//
//        mCatalogProductBinding.activityCatalogProductRv.setAdapter(mCatalogAdapter);

        /* Setup the ToolBar */
        setSupportActionBar(mCatalogProductBinding.activityCatalogProductToolbar);

        /* View pager for the recycler view fragments */
        mAdapterPageCatalogProduct =
                new AdapterPageCatalogProduct(
                        getSupportFragmentManager());

        mViewPager = mCatalogProductBinding.activityCatalogProductVp;
        mViewPager.setAdapter(mAdapterPageCatalogProduct);

        /* Sets up the ViewPager for the fragments that display the product lists */
        if (mCatalogProductBinding.activityCatalogProductVp != null) {
            setupViewPager(mCatalogProductBinding.activityCatalogProductVp);
        }

        /* Sets up the Tab's and their titles */
        mCatalogProductBinding.activityCatalogProductTl.
                setupWithViewPager(mCatalogProductBinding.activityCatalogProductVp);

    }

    /* Setup the ViewPager */
    private void setupViewPager(ViewPager viewPager) {

        mAdapterPageCatalogProduct = new AdapterPageCatalogProduct(getSupportFragmentManager());

        // Page 0 - for a list of all products.
        mAdapterPageCatalogProduct.
                addFragment(new FragmentCatalogProducts(), "All Products");

        // Page 1 - for a list of the users used products.
        mAdapterPageCatalogProduct.
                addFragment(new FragmentCatalogUsedProducts(), "My Products");

        viewPager.setAdapter(mAdapterPageCatalogProduct);
    }

    /* Clean up tasks after sign out */
    private void onSignedOutCleanUp() {

        // Nullify the user unique ID
        mUserId = Constants.ANONYMOUS;

        // Clear out the adapter
//        mCatalogAdapter.setProducts(null);
    }

    /* User is now signed in - attach to the Firebase database */
    private void onSignedInInitialise(String userUid) {

        // Update the user id
        mUserId = userUid;


        /*
           We can only get and set the data once the user has signed in, as we need the user ID
        */

        // ViewModel and LiveData for all products - Supplies data to Fragment 0, tab 0
//        ViewModelCatalogProductList viewModelCatalogProducts =
//                ViewModelProviders.of(this).get(ViewModelCatalogProductList.class);
//
//        LiveData<List<Product>> productLiveData =
//                viewModelCatalogProducts.getProductsLiveData();
//
//        // Create a shared preferences object. This will hold the base data for the widget
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        productLiveData.observe(this, products -> {
//
//            if (products != null) {
////                mCatalogAdapter.setProducts(products);
//                mCatalogProductBinding.activityCatalogProductPb.setVisibility(View.GONE);
////                mCatalogProductBinding.
////                        activityCatalogProductRv.
////                        getLayoutManager().
////                        onRestoreInstanceState(mLayoutManagerState);
//
//                // Set the product list to shared preferences for the widgets adapter
//                preferences
//                        .edit()
//                        .putString(Constants.PRODUCT_KEY, GsonUtils.productsToJson(products))
//                        .apply();
//
//                /* Update the widget with the new list od products */
//                WidgetService.startActionUpdateWidget(this);
//            }
//        });

        // ViewModel and LiveData for all users used products - Supplies data to Fragment 1, tab 1
//        ViewModelCatalogProductUsedList viewModelCatalogProductUsedList =
//                ViewModelProviders.of(this, new ViewModelFactoryProducts(mUserId))
//                        .get(ViewModelCatalogProductUsedList.class);

        Log.e(LOG_TAG, "onSignedInInitialise(String userUid) - User ID is: " + mUserId);
//        viewModelCatalogProductUsedList.setUserId(mUserId);

//        LiveData<List<Product>> productUsedLiveData =
//                viewModelCatalogProductUsedList.getProductsLiveData();
//
//        productUsedLiveData.observe(this, products -> {
//
//            if (products != null) {
//                mCatalogProductBinding.activityCatalogProductPb.setVisibility(View.GONE);
//            }
//        });

       mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
               if (position == 1) {
                   FragmentCatalogUsedProducts usedProducts = (FragmentCatalogUsedProducts)
                           mAdapterPageCatalogProduct.getItem(position);
                   usedProducts.setViewModel(mUserId);

                   Log.e(LOG_TAG, "onSignedInInitialise() - ViewPagerListener - User ID has been set to: " + mUserId);

               }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });

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
//     * @param fbProduct - The selected product.
     */
//    @Override
//    public void onClick(Product fbProduct, boolean isCreator, View view) {

//        Intent intent = new Intent(
//                ActivityCatalogProduct.this,
//                ActivityDetailProduct.class);
//
//        intent.putExtra(Constants.PRODUCT_FB_REFERENCE_KEY, fbProduct);
//        intent.putExtra(Constants.PRODUCT_IS_CREATOR_KEY, isCreator);
//
//        startActivity(intent);
//
//        // Sliding animation
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add the firebase authentication state listener to the authentication instance
        if (mFBAuthStateListener != null) {
            mFBAuth.addAuthStateListener(mFBAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFBAuthStateListener != null) {
            // Remove the firebase authentication state listener from the authentication instance
            mFBAuth.removeAuthStateListener(mFBAuthStateListener);
        }

//        mLayoutManagerState = mCatalogProductBinding.activityCatalogProductRv
//                .getLayoutManager()
//                .onSaveInstanceState();
//
//        // As the user is now logged out its good practice to clean up the adapter and detach
//        // the DB listener
//        mCatalogAdapter.setProducts(null);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        outState.putParcelable("layoutManagerState", mLayoutManagerState);
    }
}
