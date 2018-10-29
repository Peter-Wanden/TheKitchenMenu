package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.databinding.ActivityCatalogProductBinding;
import com.example.peter.thekitchenmenu.model.Product;
import com.example.peter.thekitchenmenu.repository.Repository;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProduct;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;

/**
 * The main entry point for product related actions and the host activity
 * for {@link FragmentCatalogCommunityProducts} and {@link FragmentCatalogMyProducts}.
 */
public class ActivityCatalogProduct
        extends
        AppCompatActivity
        implements
        FragmentCatalog.FragmentCatalogOnClickHandler{

    public static final String LOG_TAG = ActivityCatalogProduct.class.getSimpleName();

    /* *******************
     * Firebase database *
     *********************/
    /* Authentication instance */
    private FirebaseAuth mFBAuth;

    /* Authentication state listener */
    private FirebaseAuth.AuthStateListener mFBAuthStateListener;

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
    }

    /**
     *  Initialises the Firebase components required for this activity and logs the user in
     */
    private void initialiseFirebase() {

        // Gets an instance of Firebase authentication */
        mFBAuth = FirebaseAuth.getInstance();

        // Gets a remote configuration instance
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Firebase authentication listener (attached in onResume and detached in onPause).
        // From: Udacity AND Firebase.
        mFBAuthStateListener = firebaseAuth -> {
            // Find out if the user is logged in
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

        // Creates a Remote Config Setting to enable developer mode.
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

        mCatalogProductBinding.activityCatalogProductFab.setOnClickListener(v -> {

            Intent addProductIntent = new Intent(ActivityCatalogProduct.this,
                    ActivityDetailProduct.class);
            startActivity(addProductIntent);

            // Sliding animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        /* Sets up the ToolBar */
        setSupportActionBar(mCatalogProductBinding.activityCatalogProductToolbar);

        /*
        View pager for the {@link FragmentCatalogCommunityProducts} and
        {@link FragmentCatalogMyProducts} fragments
        */
        mAdapterPageCatalogProduct =
                new AdapterPageCatalogProduct(
                        getSupportFragmentManager());

        mViewPager = mCatalogProductBinding.activityCatalogProductVp;
        mViewPager.setAdapter(mAdapterPageCatalogProduct);

        /*
        Sets up the ViewPager for the {@link FragmentCatalogCommunityProducts} and
        {@link FragmentCatalogMyProducts} fragments.
        */
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

        // Page 0 - for a list of all (community) products.
        mAdapterPageCatalogProduct.
                addFragment(new FragmentCatalogCommunityProducts(),
                        getString(R.string.activity_catalog_products_tab_1_title));

        // Page 1 - for a list of the users used products.
        mAdapterPageCatalogProduct.
                addFragment(new FragmentCatalogMyProducts(),
                        getString(R.string.activity_catalog_products_tab_2_title));

        viewPager.setAdapter(mAdapterPageCatalogProduct);
    }

    /* Clean up tasks after sign out */
    private void onSignedOutCleanUp() {

        // Set the user ID to ANONYMOUS in shared preferences
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                edit().
                putString(Constants.USER_ID_KEY, Constants.ANONYMOUS).
                apply();
    }

    /**
     *  Invoked when user is signed in.
     * @param userUid - The unique User ID issued by Google
     */
    private void onSignedInInitialise(String userUid) {
        Log.e(LOG_TAG, "SIGNED IN CALLED!");
        // Sync remote data.

        // TODO - remove, temp for testing
        Log.e(LOG_TAG, "OnSignedIn: Product is live called");
        Repository repository = new Repository(getApplication());
        repository.productMyIsLive(true, userUid);

        /*
        The user ID is used throughout the app, so save it to a shared preferences object with a
        reference to the application context.
        */
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                edit().
                putString(Constants.USER_ID_KEY, userUid).
                apply();

        /*
        Now we have the user ID and FragmentCatalogCommunityProducts is attached we can set the
        user ID.
        */
        FragmentCatalogCommunityProducts communityProducts = (FragmentCatalogCommunityProducts)
                mAdapterPageCatalogProduct.getItem(Constants.TAB_COMM_PRODUCTS);
        if (communityProducts.isAdded()) {
            communityProducts.remoteLoginStatus(userUid);
        }

        /*
           We can only get and set the data once the user has signed in, as we need the user ID
        */

        /*
        FragmentCatalogMyProducts requires the user ID to function. As such this listener is
        implemented after onSignedInInitialise as this is the first instance where the user ID
        is available and this is the earliest we can get an attached instance to pass the variable.
        */
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == Constants.TAB_MY_PRODUCTS) {

                    FragmentCatalogMyProducts myProducts = (FragmentCatalogMyProducts)
                            mAdapterPageCatalogProduct.getItem(position);
                    /*
                    Now the adapter and ViewModel has a reference to the UserId we can call the
                    ViewModel to populate the adaptor.
                    */
                    myProducts.setViewModel();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

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
    public void onClick(Product clickedProduct, boolean isCreator) {
        /*
          A product has been clicked in the RecyclerView. Add the Firebase product to an intent
          and go to ActivityDetailProduct.
         */
        Intent intent = new Intent(
                ActivityCatalogProduct.this,
                ActivityDetailProduct.class);

        intent.putExtra(Constants.PRODUCT_FB_REFERENCE_KEY, clickedProduct);
        intent.putExtra(Constants.PRODUCT_IS_CREATOR_KEY, isCreator);

        startActivity(intent);

        // Sliding animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
