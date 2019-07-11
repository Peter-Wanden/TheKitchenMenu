package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductViewerActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryFavoriteProduct;
import com.example.peter.thekitchenmenu.ui.catalog.product.ProductCatalogActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class ProductViewerActivity
        extends AppCompatActivity
        implements ProductViewerNavigator, FavoriteProductViewerNavigator {

    private static final String TAG = "tkm-ProductViewerAct";

    public static final String EXTRA_NEW_PRODUCT_ID = "NEW_PRODUCT_ID";
    // Request codes other activities use to determine the type of request they made to this activity
    public static final int REQUEST_VIEW_PRODUCT = 1;
    public static final int REQUEST_REVIEW_PRODUCT = 2;
    // Result codes this activity provides
    public static final int RESULT_DELETE_PRODUCT_OK = RESULT_FIRST_USER + 1;
    public static final int RESULT_FAVORITE_ADDED_OK = RESULT_FIRST_USER + 2;
    public static final int RESULT_FAVORITE_NOT_ADDED = RESULT_FIRST_USER + 3;
    public static final int RESULT_PRODUCT_UNAVAILABLE = RESULT_FIRST_USER + 4;

    private ProductViewerActivityBinding binding;
    private ProductViewerViewModel productViewerViewModel;
    private FavoriteProductViewerViewModel favoriteProductViewerViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        setupViewModels();
        setupObservers();
        addFragments();
        setActivityTitle();
        productViewerViewModel.getHasOptionsMenuEvent().setValue(true);
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.product_viewer_activity);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.productViewerActivityToolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewModels() {
        productViewerViewModel = obtainProductViewerViewModel(this);
        productViewerViewModel.setNavigator(this);

        if (getIntent().hasExtra(EXTRA_NEW_PRODUCT_ID))
            productViewerViewModel.setNewProductAdded(true);
        else
            productViewerViewModel.setNewProductAdded(false);

        favoriteProductViewerViewModel = obtainFavoriteProductViewerViewModel(this);
        favoriteProductViewerViewModel.setNavigator(this);
    }

    private void setupObservers() {
        favoriteProductViewerViewModel.getProductId().observe(
                this, this::startProductViewerViewModel);
    }

    public static ProductViewerViewModel obtainProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryProduct factory = ViewModelFactoryProduct.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProductViewerViewModel.class);
    }

    public static FavoriteProductViewerViewModel obtainFavoriteProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryFavoriteProduct factory = ViewModelFactoryFavoriteProduct.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(FavoriteProductViewerViewModel.class);
    }

    private void addFragments() {
        String productId = null;

        if (getIntent().hasExtra(ProductEditorActivity.EXTRA_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID);

        } else if (getIntent().hasExtra(EXTRA_NEW_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(EXTRA_NEW_PRODUCT_ID);
        }

        ProductViewerFragment productViewerFragment =
                findOrReplaceViewerFragment(productId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                productViewerFragment,
                R.id.product_viewer_content_frame);

        FavoriteProductViewerFragment favoriteProductViewerFragment =
                findOrReplaceFavoriteProductViewerFragment();

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                favoriteProductViewerFragment,
                R.id.favorite_product_viewer_content_frame);
    }

    @NonNull
    private ProductViewerFragment findOrReplaceViewerFragment(String productId) {
        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_viewer_content_frame);

        if (fragment == null)
            fragment = ProductViewerFragment.newInstance(productId);
        return fragment;
    }

    @NonNull
    private FavoriteProductViewerFragment findOrReplaceFavoriteProductViewerFragment() {
        FavoriteProductViewerFragment fragment = (FavoriteProductViewerFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.favorite_product_viewer_content_frame);

        if (fragment == null) {
            String favoriteProductId = null;

            if (getIntent().hasExtra(FavoriteProductEditorActivity.EXTRA_FAVORITE_PRODUCT_ID)) {
                favoriteProductId = getIntent().getStringExtra(
                        FavoriteProductEditorActivity.EXTRA_FAVORITE_PRODUCT_ID);
            }
            fragment = FavoriteProductViewerFragment.newInstance(favoriteProductId);
        }
        return fragment;
    }

    private void setActivityTitle() {
        if (getIntent().hasExtra(ProductEditorActivity.EXTRA_PRODUCT_ID))
            setTitle(R.string.activity_title_view_product);
        else if (getIntent().hasExtra(EXTRA_NEW_PRODUCT_ID))
            setTitle(R.string.activity_title_review_new_product);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (productViewerViewModel.isNewProductAdded()) {
            goToProductCatalog();
        }
        if (favoriteProductViewerViewModel.isFavoriteAddedEdited()) {
            setResult(FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK);
        }
        finish();
        return true;
    }

    private void goToProductCatalog() {
        Intent intent = new Intent(this, ProductCatalogActivity.class);
        intent.putExtra(
                ProductCatalogActivity.NEW_PRODUCT_ID_ADDED,
                productViewerViewModel.product.get().getId());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goToProductCatalog();
    }

    @Override
    public void editProduct(String productId) {
        Intent intent = new Intent(this, ProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(intent, ProductEditorActivity.REQUEST_ADD_EDIT_PRODUCT);
    }

    @Override
    public void deleteProduct(String productId) {
        favoriteProductViewerViewModel.deleteFavoriteProduct();
        setResult(RESULT_DELETE_PRODUCT_OK);
        finish();
    }

    @Override
    public void addFavoriteProduct() {
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);
        intent.putExtra(
                ProductEditorActivity.EXTRA_PRODUCT_ID,
                productViewerViewModel.product.get().getId());
        startActivityForResult(
                intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    public void editFavoriteProduct() {
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);
        intent.putExtra(
                ProductEditorActivity.EXTRA_PRODUCT_ID,
                productViewerViewModel.product.get().getId());
        intent.putExtra(
                FavoriteProductEditorActivity.EXTRA_FAVORITE_PRODUCT_ID,
                favoriteProductViewerViewModel.favoriteProduct.get().getId());
        startActivityForResult(
                intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);

        if (requestCode == FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT &&
                resultCode == FavoriteProductEditorActivity.RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK) {

            favoriteProductViewerViewModel.handleActivityResult(
                    requestCode,
                    resultCode,
                    data.getStringExtra(FavoriteProductEditorActivity.EXTRA_FAVORITE_PRODUCT_ID));
        }
    }

    @Override
    protected void onDestroy() {
        productViewerViewModel.onActivityDestroyed();
        favoriteProductViewerViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    private void startProductViewerViewModel(String productId) {
        productViewerViewModel.start(productId);
    }
}