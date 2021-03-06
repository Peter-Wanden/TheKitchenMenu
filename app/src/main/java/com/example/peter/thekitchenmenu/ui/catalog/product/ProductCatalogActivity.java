package com.example.peter.thekitchenmenu.ui.catalog.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductModel;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.ui.catalog.CatalogFragmentPageAdapter;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.ProductViewerActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class ProductCatalogActivity
        extends AppCompatActivity
        implements ProductNavigator, ProductItemNavigator {

    private static final String TAG = "tkm-" + ProductCatalogActivity.class.getSimpleName() + ":";

    private ProductCatalogViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupSearch();
        setupToolBar();
        setupFab();
        setupViewModel();
        setupFragmentPageAdapter();
        setTitle(this.getResources().getString(R.string.activity_title_product_catalog));
    }

    private void initialiseBindings() {
    }

    private void setupSearch() {
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
    }

    private void setupViewModel() {
        viewModel = obtainViewModel(this);
        viewModel.setNavigators(this, this);
        viewModel.start();
    }

    public static ProductCatalogViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactoryProduct factory =
                ViewModelFactoryProduct.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, factory).get(ProductCatalogViewModel.class);
    }

    private void setupFragmentPageAdapter() {
        CatalogFragmentPageAdapter fragmentPageAdapter =
                new CatalogFragmentPageAdapter(getSupportFragmentManager());

        fragmentPageAdapter.addFragment(ProductCatalogAllFragment.newInstance(),
                getString(R.string.activity_catalog_products_tab_1_title));
        fragmentPageAdapter.addFragment(ProductCatalogFavoritesFragment.newInstance(),
                getString(R.string.activity_catalog_products_tab_2_title));


    }

    private void setupToolBar() {
//        setSupportActionBar(binding.productCatalogActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupFab(){
        FloatingActionButton fab = findViewById(R.id.product_catalog_activity_fab);
        fab.setOnClickListener(view -> viewModel.addNewProduct());
    }

    @Override
    public void addNewProduct() {
        System.out.println(TAG + "addNewProduct: starting ProductEditor for result");
        Intent intent = new Intent(this, ProductEditorActivity.class);
        startActivityForResult(intent, ProductEditorActivity.REQUEST_ADD_EDIT_PRODUCT);
    }

    @Override
    public void addToFavorites(String productId) {
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(
                intent, FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    public void removeFromFavorites(String productId) {
        // Handled by interactor in view model
    }

    @Override
    public void viewProduct(ProductModel productModel) {
        Intent intent = new Intent(this, ProductViewerActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID,
                productModel.getProductEntity().getDataId());
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

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                viewModel.searchQuery(searchQuery);
                return false;
            }
        });
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
    protected void onDestroy() {
        viewModel.onActivityDestroyed();
        super.onDestroy();
    }
}
