package com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor;

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
import com.example.peter.thekitchenmenu.databinding.FavoriteProductEditorActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryFavoriteProduct;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.ProductViewerFragment;
import com.example.peter.thekitchenmenu.ui.detail.product.productviewer.ProductViewerViewModel;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class FavoriteProductEditorActivity
        extends AppCompatActivity
        implements AddEditFavoriteProductNavigator {

    private static final String TAG = "tkm-FavProductEditAct";

    public static final int REQUEST_ADD_EDIT_FAVORITE_PRODUCT = 3;
    public static final int RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK = RESULT_FIRST_USER + 1;

    private FavoriteProductEditorActivityBinding binding;
    private FavoriteProductEditorViewModel favoriteProductEditorViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        findOrCreateFragments();
        setupViewModels();
        subscribeToNavigationChanges();
        setupObservables();
    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this,
                R.layout.favorite_product_editor_activity);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.favoriteProductEditorActivityToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setupViewModels() {
        favoriteProductEditorViewModel = obtainFavoriteProductEditorViewModel(this);
        ProductViewerViewModel viewModel = obtainProductViewerViewModel(this);
        viewModel.setViewOnlyMode(true);
    }

    @NonNull
    public static ProductViewerViewModel obtainProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryProduct factoryProduct =
                ViewModelFactoryProduct.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factoryProduct).get(ProductViewerViewModel.class);
    }

    @NonNull
    public static FavoriteProductEditorViewModel obtainFavoriteProductEditorViewModel(
            FragmentActivity activity) {

        ViewModelFactoryFavoriteProduct factoryFavoriteProduct =
                ViewModelFactoryFavoriteProduct.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factoryFavoriteProduct).
                get(FavoriteProductEditorViewModel.class);
    }

    private void findOrCreateFragments() {
        String productId = getProductId();
        findOrCreateProductViewerFragment(productId);
        findOrCreateFavoriteProductEditorFragment(productId);
    }

    private String getProductId() {
        String productId = getIntent().getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID);
        return  productId == null ? "" : productId;
    }

    private void findOrCreateProductViewerFragment(String productId) {
        ProductViewerFragment fragment = (ProductViewerFragment) getSupportFragmentManager().
                findFragmentById(R.id.product_viewer_contentFrame);

        if (fragment == null)
            fragment = ProductViewerFragment.newInstance(productId);

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment, R.id.product_viewer_contentFrame);
    }

    private void findOrCreateFavoriteProductEditorFragment(String productId) {
        FavoriteProductEditorFragment fragment = (FavoriteProductEditorFragment)
                getSupportFragmentManager().
                findFragmentById(R.id.favorite_product_editor_contentFrame);

        if (fragment == null) fragment =
                FavoriteProductEditorFragment.newInstance(productId);

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment, R.id.favorite_product_editor_contentFrame);
    }

    private void subscribeToNavigationChanges() {
        favoriteProductEditorViewModel.getFavoriteProductSaved().observe(
                this, FavoriteProductEditorActivity.this::onFavoriteProductSaved);
    }

    private void setupObservables() {
        favoriteProductEditorViewModel.getSetActivityTitle().observe(
                this, this::setActivityTitle);
    }

    private void setActivityTitle(String activityTitle) {
        setTitle(activityTitle);
    }

    @Override
    public void onFavoriteProductSaved(String productId) {
        Intent intent = new Intent();
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        setResult(RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK, intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

