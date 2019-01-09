package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.databinding.ActivityCatalogProductBinding;
import com.example.peter.thekitchenmenu.ui.detail.ProductDetail;
import com.example.peter.thekitchenmenu.viewmodels.ViewModelCatProd;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import static com.example.peter.thekitchenmenu.app.Constants.PRODUCT_FB_REFERENCE_KEY;
import static com.example.peter.thekitchenmenu.app.Constants.PRODUCT_IS_CREATOR_KEY;

public class ProductCatalog extends AppCompatActivity {

    private static final String TAG = "ProductCatalog";
    ViewModelCatProd viewModel;
    ActivityCatalogProductBinding bindings;
    AdapterPageCatProd tabPageAdapter;
    ViewPager tabViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseViews();
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ViewModelCatProd.class);
        viewModel.getSelected().observe(this, selectedProduct -> {
            if (selectedProduct != null) {
                launchDetailActivity(selectedProduct);
            }
        });
    }

    private void launchDetailActivity(ProductModel selectedProduct) {
        Intent intent = new Intent(ProductCatalog.this, ProductDetail.class);
        intent.putExtra(PRODUCT_FB_REFERENCE_KEY, selectedProduct);
        intent.putExtra(PRODUCT_IS_CREATOR_KEY, viewModel.getIsCreator().getValue());
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void initialiseViews() {
        // TODO - Bind views to XML files
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        bindings = DataBindingUtil.setContentView(this, R.layout.activity_catalog_product);
        bindings.activityCatalogProductPb.setVisibility(View.GONE);
        bindings.activityCatalogProductFab.setOnClickListener(v -> {
            Intent addProductIntent = new Intent(ProductCatalog.this,
                    ProductDetail.class);
            startActivity(addProductIntent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });

        setSupportActionBar(bindings.activityCatalogProductToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabPageAdapter = new AdapterPageCatProd(getSupportFragmentManager());
        tabViewPager = bindings.activityCatalogProductVp;
        tabViewPager.setAdapter(tabPageAdapter);

        if (bindings.activityCatalogProductVp != null) {
            setupViewPager(bindings.activityCatalogProductVp);
        }

        // Sets up the Tab's and their titles.
        bindings.activityCatalogProductTl.setupWithViewPager(bindings.activityCatalogProductVp);

    }

    private void setupViewPager(ViewPager viewPager) {
        tabPageAdapter = new AdapterPageCatProd(getSupportFragmentManager());
        tabPageAdapter.addFragment(new FragmentCatVmProd(), getString(R.string.activity_catalog_products_tab_1_title));
        tabPageAdapter.addFragment(new FragmentCatVmProdMy(), getString(R.string.activity_catalog_products_tab_2_title));
        viewPager.setAdapter(tabPageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
