package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.databaseRemote.RemoteSignIn;
import com.example.peter.thekitchenmenu.data.model.ProductCommunity;
import com.example.peter.thekitchenmenu.databinding.ActivityCatalogProductBinding;
import com.example.peter.thekitchenmenu.data.model.Product;
import com.example.peter.thekitchenmenu.ui.detail.ActivityDetailProduct;


/**
 * The main entry point for product related actions and the host activity
 * for {@link FragmentCatProdComm} and {@link FragmentCatProdMy}.
 */
public class ActivityCatProd
        extends
        AppCompatActivity
        implements
        OnClickProdMy{

    public static final String LOG_TAG = ActivityCatProd.class.getSimpleName();

    // Instance of the Firebase remote sign in class
    RemoteSignIn mRemoteSignIn;

    // Binding class for the views.
    ActivityCatalogProductBinding mCatProdBinding;

    // Adapter for the ViewPager.
    AdapterPageCatProd mAdapterPageCatProd;

    // ViewPager to swipe through the fragments.
    ViewPager mViewPager;

    private FragmentCatProdComm mFragmentCatProdComm;
    private FragmentCatProdMy mFragmentCatProdMy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentCatProdComm = new FragmentCatProdComm();
        mFragmentCatProdMy = new FragmentCatProdMy();
        mRemoteSignIn = new RemoteSignIn(this);
        initialiseViews();
    }

    // Sets up the views for this activity.
    private void initialiseViews() {

        mCatProdBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_catalog_product);

        mCatProdBinding.activityCatalogProductPb.setVisibility(View.GONE);

        mCatProdBinding.activityCatalogProductFab.setOnClickListener(v -> {

            Intent addProductIntent = new Intent(ActivityCatProd.this,
                    ActivityDetailProduct.class);
            startActivity(addProductIntent);

            // Sliding animation.
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        // Sets up the ToolBar.
        setSupportActionBar(mCatProdBinding.activityCatalogProductToolbar);

        // View pager for the FragmentCatProdComm and FragmentCatProdMy fragments.
        mAdapterPageCatProd = new AdapterPageCatProd(getSupportFragmentManager());

        mViewPager = mCatProdBinding.activityCatalogProductVp;
        mViewPager.setAdapter(mAdapterPageCatProd);

        // Sets up the ViewPager for the FragmentCatProdComm and FragmentCatProdMy fragments.
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
        mAdapterPageCatProd.addFragment(mFragmentCatProdComm,
                        getString(R.string.activity_catalog_products_tab_1_title));

        // Page 1 - for a list of the users used products.
        mAdapterPageCatProd.addFragment(mFragmentCatProdMy,
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_catalog, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onClick(ProductCommunity clickedProduct, boolean isCreator) {
        /*
          A product has been clicked in the RecyclerView. Add the Firebase product to an intent
          and go to ActivityDetailProduct.
         */
        Intent intent = new Intent(ActivityCatProd.this, ActivityDetailProduct.class);

        intent.putExtra(Constants.PRODUCT_FB_REFERENCE_KEY, clickedProduct);
        intent.putExtra(Constants.PRODUCT_IS_CREATOR_KEY, isCreator);

        startActivity(intent);

        // Sliding animation
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
