package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteSignIn;
import com.example.peter.thekitchenmenu.data.model.VmProd;
import com.example.peter.thekitchenmenu.databinding.ActivityCatalogProductBinding;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProd;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatProd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import static com.example.peter.thekitchenmenu.app.Constants.PRODUCT_FB_REFERENCE_KEY;
import static com.example.peter.thekitchenmenu.app.Constants.PRODUCT_IS_CREATOR_KEY;


/**
 * The main entry point for product related actions and (currently) the host activity
 * for {@link FragmentCatVmProd} and {@link FragmentCatVmProdMy}.
 */
public class ProductCatalog
        extends AppCompatActivity {

    private static final String TAG = "ProductCatalog";

    RemoteSignIn mRemoteSignIn;
    ViewModelCatProd viewModelCatProd;
    ActivityCatalogProductBinding mCatProdBinding;
    AdapterPageCatProd mAdapterPageCatProd;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mRemoteSignIn = new RemoteSignIn(this);
        initialiseViews();
        setupViewModel();
    }

    // Sets up the view model and observes when a product value changes which indicates the user has
    // clicked a product.
    private void setupViewModel() {

        viewModelCatProd = ViewModelProviders.of(this).get(ViewModelCatProd.class);
        viewModelCatProd.getSelected().observe(this, vmProd -> {
            if (vmProd != null) {
                launchDetailActivity(vmProd);
            }
        });
    }

    // Launches the detail activity
    private void launchDetailActivity(VmProd vmProd) {

        Intent intent = new Intent(ProductCatalog.this, ActivityDetailProd.class);
        intent.putExtra(PRODUCT_FB_REFERENCE_KEY, vmProd);
        intent.putExtra(PRODUCT_IS_CREATOR_KEY, viewModelCatProd.getIsCreator().getValue());
        startActivity(intent);

        // Sliding animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // Sets up the views for this activity.
    private void initialiseViews() {
        // TODO - Bind views to XML files
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        mCatProdBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_catalog_product);

        mCatProdBinding.activityCatalogProductPb.setVisibility(View.GONE);

        mCatProdBinding.activityCatalogProductFab.setOnClickListener(v -> {

            Intent addProductIntent = new Intent(ProductCatalog.this,
                    ActivityDetailProd.class);
            startActivity(addProductIntent);

            // Sliding animation.
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        // Sets up the ToolBar.
        setSupportActionBar(mCatProdBinding.activityCatalogProductToolbar);

        // View pager for the FragmentCatVmProd and FragmentCatVmProdMy fragments.
        mAdapterPageCatProd = new AdapterPageCatProd(getSupportFragmentManager());

        mViewPager = mCatProdBinding.activityCatalogProductVp;
        mViewPager.setAdapter(mAdapterPageCatProd);

        // Sets up the ViewPager for the FragmentCatVmProd and FragmentCatVmProdMy fragments.
        if (mCatProdBinding.activityCatalogProductVp != null) {
            setupViewPager(mCatProdBinding.activityCatalogProductVp);
        }

        // Sets up the Tab's and their titles.
        mCatProdBinding.activityCatalogProductTl.
                setupWithViewPager(mCatProdBinding.activityCatalogProductVp);
    }

    // Setup the ViewPager.
    private void setupViewPager(ViewPager viewPager) {

        mAdapterPageCatProd = new AdapterPageCatProd(getSupportFragmentManager());

        // Page 0 - for a list of all (community) products.
        mAdapterPageCatProd.addFragment(new FragmentCatVmProd(),
                getString(R.string.activity_catalog_products_tab_1_title));

        // Page 1 - for a list of the users used products.
        mAdapterPageCatProd.addFragment(new FragmentCatVmProdMy(),
                getString(R.string.activity_catalog_products_tab_2_title));

        viewPager.setAdapter(mAdapterPageCatProd);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRemoteSignIn.authStateListener(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRemoteSignIn.authStateListener(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_catalog, menu);
        return true;
    }

    // Handles the menu sign out button.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                // Sign the user out
                mRemoteSignIn.signOut(this);
                return true;
            case R.id.action_search:
                // open the search bar
                onSearchRequested();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putString("fromClass", TAG);
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Send the sign in results to the remote sign in class.
        mRemoteSignIn.signInResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
