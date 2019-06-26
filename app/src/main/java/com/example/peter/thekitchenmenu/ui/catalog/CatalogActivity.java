package com.example.peter.thekitchenmenu.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductCatalogActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryFavoriteProduct;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.ProductViewerActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class CatalogActivity
        extends AppCompatActivity
        implements ProductNavigator, ProductItemNavigator {

    private static final String TAG = "tkm-ProductCatalogAct";

    public static final String NEW_PRODUCT_ID_ADDED = "NEW_PRODUCT_ID_ADDED";
    CatalogProductsViewModel viewModel;
    ProductCatalogActivityBinding binding;
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
        binding = DataBindingUtil.setContentView(this, R.layout.product_catalog_activity);
        binding.activityCatalogProductPb.setVisibility(View.GONE);
    }

    private void setupSearch() {
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    }

    private void setupFragmentPageAdapter() {
        tabPageAdapter = new CatalogFragmentPageAdapter(getSupportFragmentManager());
        tabViewPager = binding.activityCatalogProductVp;
        tabViewPager.setAdapter(tabPageAdapter);

        if (binding.activityCatalogProductVp != null) {
            setupViewPager(binding.activityCatalogProductVp);
        }

        // Sets up the Tab's and their titles.
        binding.activityCatalogProductTl.setupWithViewPager(binding.activityCatalogProductVp);
    }

    private void setupViewPager(ViewPager viewPager) {
        tabPageAdapter = new CatalogFragmentPageAdapter(getSupportFragmentManager());
        tabPageAdapter.addFragment(new CatalogAllFragment(), getString(R.string.activity_catalog_products_tab_1_title));
        tabPageAdapter.addFragment(new CatalogFavoritesFragment(), getString(R.string.activity_catalog_products_tab_2_title));

        viewPager.setAdapter(tabPageAdapter);
    }

    private void setupToolBar() {
        setSupportActionBar(binding.activityCatalogProductToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupFab() {
        binding.activityCatalogAddNewProductFab.setOnClickListener(v -> viewModel.addNewProduct());
    }

    public static CatalogProductsViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactoryFavoriteProduct factory =
                ViewModelFactoryFavoriteProduct.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(CatalogProductsViewModel.class);
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.setNavigators(this, this);
        viewModel.prepareData();

        viewModel.getOpenProductEvent().observe(this, productId -> {
            if (productId != null) viewProduct(productId);
        });

        viewModel.getAddToFavoritesEvent().observe(this, productId -> {
            if (productId != null) addToFavorites(productId);
        });
    }

    @Override
    public void addNewProduct() {
        Intent intent = new Intent(this, ProductEditorActivity.class);
        startActivity(intent);
    }

    @Override
    public void addToFavorites(String productId) {
        // Launch AddToFavorites, don't forget to deal with onActivityResult
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);

        intent.putExtra(
                FavoriteProductEditorActivity.EXTRA_PRODUCT_ID,
                productId);

        startActivityForResult(
                intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    public void removeFromFavorites(String productId) {
        // Delete from FavoriteProducts / or show dialog and refresh data
    }

    @Override
    public void viewProduct(String productId) {
        Intent intent = new Intent(CatalogActivity.this, ProductViewerActivity.class);
        intent.putExtra(ProductViewerActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(intent, ProductViewerActivity.REQUEST_VIEW_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.handleActivityResult(requestCode, resultCode, data);
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
}
