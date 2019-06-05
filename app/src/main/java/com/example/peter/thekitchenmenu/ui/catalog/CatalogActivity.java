package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.databinding.ProductCatalogActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactory;
import com.example.peter.thekitchenmenu.ui.detail.product.editor.ProductEditorActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class CatalogActivity
        extends AppCompatActivity
        implements ProductNavigator, ProductItemNavigator {

    private static final String TAG = "tkm-ProductCatalogAct";
    public static final String PRODUCT_CATALOG_VIEW_MODEL_TAG = "PRODUCT_CATALOG_VIEW_MODEL_TAG";

    CatalogProductsViewModel viewModel;
    ProductCatalogActivityBinding bindings;
    CatalogFragmentPageAdapter tabPageAdapter;
    ViewPager tabViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupSearch();
        setupToolBar();
        setupFab();
        setupViewModel();
        setupFragmentPageAdapter();
    }

    private void initialiseBindings() {
        bindings = DataBindingUtil.setContentView(this, R.layout.product_catalog_activity);
        bindings.activityCatalogProductPb.setVisibility(View.GONE);
    }

    private void setupSearch() {
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    }

    private void setupFragmentPageAdapter() {
        tabPageAdapter = new CatalogFragmentPageAdapter(getSupportFragmentManager());
        tabViewPager = bindings.activityCatalogProductVp;
        tabViewPager.setAdapter(tabPageAdapter);

        if (bindings.activityCatalogProductVp != null) {
            setupViewPager(bindings.activityCatalogProductVp);
        }

        // Sets up the Tab's and their titles.
        bindings.activityCatalogProductTl.setupWithViewPager(bindings.activityCatalogProductVp);
    }

    private void setupViewPager(ViewPager viewPager) {
        tabPageAdapter = new CatalogFragmentPageAdapter(getSupportFragmentManager());
        tabPageAdapter.addFragment(new CatalogAllFragment(), getString(R.string.activity_catalog_products_tab_1_title));
        tabPageAdapter.addFragment(new CatalogUsedFragment(), getString(R.string.activity_catalog_products_tab_2_title));

        viewPager.setAdapter(tabPageAdapter);
    }

    private void setupToolBar() {
        setSupportActionBar(bindings.activityCatalogProductToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupFab() {
        bindings.activityCatalogAddNewProductFab.setOnClickListener(v -> {
            viewModel.addNewProduct();
        });
    }

    public static CatalogProductsViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(CatalogProductsViewModel.class);
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.setNavigator(this);

        viewModel.getSelected().observe(this, selectedProduct -> {
            if (selectedProduct != null) {
                launchProductEditor(selectedProduct);
            }
        });
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
                return true;

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
    public void addNewProduct() {
        Intent intent = new Intent(this, ProductEditorActivity.class);
        startActivity(intent);
    }

    // Todo - this will be the product viewing and adding user details page (with fab to editing
    //  if owned and not used by others)
    private void launchProductEditor(ProductEntity selectedProduct) {
        Intent intent = new Intent(CatalogActivity.this, ProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, selectedProduct.getId());
        intent.putExtra(ProductEditorActivity.EXTRA_IS_CREATOR, viewModel.getIsCreator().getValue());
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        viewModel.onActivityDestroyed();
        super.onDestroy();
    }

    @Override
    public void openProductDetails(ProductEntity product, boolean isCreator) {
        Log.d(TAG, "openProductDetails: called!");
    }
}
