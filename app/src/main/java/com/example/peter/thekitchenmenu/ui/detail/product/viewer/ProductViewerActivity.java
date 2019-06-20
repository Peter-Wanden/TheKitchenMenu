package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductViewerActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryFavoriteProduct;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorFragment;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class ProductViewerActivity extends AppCompatActivity implements ProductViewerNavigator {

    private static final String TAG = "tkm-ProductViewerAct";

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";
    public static final int REQUEST_CODE = 1;
    public static final int DELETE_RESULT_OK = RESULT_FIRST_USER + 2;


    private ProductViewerActivityBinding binding;
    private ProductViewerViewModel productViewerViewModel;
    private FavoriteProductViewerViewModel favoriteProductViewerViewModel;
    private String productId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        initialiseBindings();
        setupToolbar();
        setupViewModels();
        addFragments();
        subscribeToNavigationChanges();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.product_viewer_activity);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.productViewerActivityToolbar);
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
    }

    private void setupViewModels() {
        productViewerViewModel = obtainProductViewerViewModel(this);
        favoriteProductViewerViewModel = obtainFavoriteProductViewerViewModel(this);
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
    private FavoriteProductViewerFragment findOrReplaceFavoriteProductViewerFragment(String productId) {

        FavoriteProductViewerFragment fragment = (FavoriteProductViewerFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.favorite_product_viewer_content_frame);

        if (fragment == null) fragment = FavoriteProductViewerFragment.newInstance(productId);
        return fragment;
    }

    private void subscribeToNavigationChanges() {
        favoriteProductViewerViewModel.getAddFavoriteProduct().observe(
                this, addFavoriteProductEvent ->
                ProductViewerActivity.this.addNewFavoriteProduct());

        favoriteProductViewerViewModel.getEditFavoriteProduct().observe(
                this, editFavoriteProductEvent ->
                        ProductViewerActivity.this.editFavoriteProduct());

        favoriteProductViewerViewModel.getRemoveFavoriteProduct().observe(
                this, removeProductEvent ->
                ProductViewerActivity.this.deleteFavoriteProduct());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void editProduct(String productId) {
        // Navigate to ProductEditor, with onActivityResult()

    }

    @Override
    public void deleteProduct(String productId) {
        // Confirm delete, Navigate to product catalog with appropriate onActivityResult()
    }

    @Override
    public void deleteFavoriteProduct() {
        setResult(DELETE_RESULT_OK);
        finish();
    }

    @Override
    public void addNewFavoriteProduct() {
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
        favoriteProductViewerViewModel.handleActivityResult(requestCode, resultCode);
    }
}
