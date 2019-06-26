package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

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
import com.example.peter.thekitchenmenu.ui.catalog.CatalogActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorFragment;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class ProductViewerActivity
        extends AppCompatActivity
        implements ProductViewerNavigator, FavoriteProductViewerNavigator {

    private static final String TAG = "tkm-ProductViewerAct";

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";
    public static final String EXTRA_NEW_PRODUCT_ID = "NEW_PRODUCT_ID";
    public static final int REQUEST_VIEW_PRODUCT = 1;
    public static final int REQUEST_REVIEW_PRODUCT = 2;
    public static final int RESULT_DELETE_PRODUCT_OK = RESULT_FIRST_USER + 1;
    public static final int RESULT_FAVORITE_ADDED_OK = RESULT_FIRST_USER + 2;
    public static final int RESULT_FAVORITE_NOT_ADDED = RESULT_FIRST_USER + 3;

    private ProductViewerActivityBinding binding;
    private ProductViewerViewModel productViewerViewModel;
    private FavoriteProductViewerViewModel favoriteProductViewerViewModel;
    private boolean productAdded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String productId = "";
        if (getIntent().getStringExtra(EXTRA_PRODUCT_ID) != null) {
            setTitle(R.string.activity_title_view_product);
            productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        } else if (getIntent().getStringExtra(EXTRA_NEW_PRODUCT_ID) != null) {
            setTitle(R.string.activity_title_review_new_product);
            productId = getIntent().getStringExtra(EXTRA_NEW_PRODUCT_ID);
            productAdded = true;
        }
        initialiseBindings();
        setupToolbar();
        setupViewModels();
        addFragments(productId);
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.product_viewer_activity);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.productViewerActivityToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViewModels() {
        productViewerViewModel = obtainProductViewerViewModel(this);
        productViewerViewModel.setNavigator(this);
        favoriteProductViewerViewModel = obtainFavoriteProductViewerViewModel(this);
        favoriteProductViewerViewModel.setNavigator(this);
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

    private void addFragments(String productId) {

        ProductViewerFragment productViewerFragment =
                findOrReplaceViewerFragment(productId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                productViewerFragment,
                R.id.product_viewer_content_frame);

        FavoriteProductViewerFragment favoriteProductViewerFragment =
                findOrReplaceFavoriteProductViewerFragment(productId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                favoriteProductViewerFragment,
                R.id.favorite_product_viewer_content_frame);
    }

    @NonNull
    private ProductViewerFragment findOrReplaceViewerFragment(String productId) {

        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_viewer_content_frame);

        if (fragment == null) fragment = ProductViewerFragment.newInstance(productId);
        return fragment;
    }

    @NonNull
    private FavoriteProductViewerFragment findOrReplaceFavoriteProductViewerFragment(
            String productId) {

        FavoriteProductViewerFragment fragment = (FavoriteProductViewerFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.favorite_product_viewer_content_frame);

        if (fragment == null) fragment = FavoriteProductViewerFragment.newInstance(productId);
        return fragment;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (productAdded) {
            goToProductCatalog();
        }
        if (favoriteProductViewerViewModel.isFavoriteAddedEdited()) {
            setResult(RESULT_FAVORITE_ADDED_OK);
            finish();

        } else {
            setResult(RESULT_FAVORITE_NOT_ADDED);
            finish();
        }
        return true;
    }

    private void goToProductCatalog() {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra(
                CatalogActivity.NEW_PRODUCT_ID_ADDED,
                productViewerViewModel.product.get().getId());
        startActivity(intent);
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
                FavoriteProductEditorActivity.EXTRA_PRODUCT_ID,
                productViewerViewModel.product.get().getId());
        startActivityForResult(
                intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    public void editFavoriteProduct() {
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);
        intent.putExtra(
                FavoriteProductEditorActivity.EXTRA_PRODUCT_ID,
                productViewerViewModel.product.get().getId());
        intent.putExtra(
                FavoriteProductEditorFragment.ARGUMENT_FAVORITE_PRODUCT_ID,
                favoriteProductViewerViewModel.favoriteProduct.get().getId());
        startActivityForResult(
                intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);

        if (requestCode == FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT)
            favoriteProductViewerViewModel.handleActivityResult(requestCode, resultCode);
    }

    @Override
    protected void onDestroy() {
        productViewerViewModel.onActivityDestroyed();
        favoriteProductViewerViewModel.onActivityDestroyed();
        super.onDestroy();
    }
}