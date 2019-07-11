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

    public static final String EXTRA_FAVORITE_PRODUCT_ID = "FAVORITE_PRODUCT_ID";
    public static final int REQUEST_ADD_EDIT_FAVORITE_PRODUCT = 3;
    public static final int RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK = RESULT_FIRST_USER + 1;

    private FavoriteProductEditorActivityBinding binding;
    private FavoriteProductEditorViewModel favoriteProductEditorViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        setupViewModels();
        findOrCreateFragments();
        setBindingInstanceVariables();
        subscribeToNavigationChanges();
        setActivityTitle();
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
        ProductViewerViewModel productViewerViewModel = obtainProductViewerViewModel(this);
        productViewerViewModel.canAddRemoveFavorites.set(false);
        favoriteProductEditorViewModel = obtainFavoriteProductEditorViewModel(this);
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
        String productId = null;
        String favoriteProductId = null;

        if (getIntent().hasExtra(ProductEditorActivity.EXTRA_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID);
        }

        if (getIntent().hasExtra(EXTRA_FAVORITE_PRODUCT_ID)) {
            favoriteProductId = getIntent().getStringExtra(EXTRA_FAVORITE_PRODUCT_ID);
        }

        ProductViewerFragment productViewerFragment =
                findOrCreateProductViewerFragment(productId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                productViewerFragment,
                R.id.product_viewer_contentFrame);

        FavoriteProductEditorFragment favoriteProductEditorFragment =
                findOrCreateFavoriteProductEditorFragment(productId, favoriteProductId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                favoriteProductEditorFragment,
                R.id.favorite_product_editor_contentFrame);
    }

    private ProductViewerFragment findOrCreateProductViewerFragment(String productId) {

        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().
                findFragmentById(R.id.product_viewer_contentFrame);

        if (fragment == null)
            fragment = ProductViewerFragment.newInstance(productId);

        return fragment;
    }

    private FavoriteProductEditorFragment findOrCreateFavoriteProductEditorFragment(
            String productId, String favoriteProductId) {

        FavoriteProductEditorFragment fragment = (FavoriteProductEditorFragment)
                getSupportFragmentManager().
                findFragmentById(R.id.favorite_product_editor_contentFrame);

        if (fragment == null) fragment =
                FavoriteProductEditorFragment.newInstance(productId, favoriteProductId);

        return fragment;
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(favoriteProductEditorViewModel);
    }

    private void subscribeToNavigationChanges() {
        favoriteProductEditorViewModel.getFavoriteProductSaved().observe(
                this, FavoriteProductEditorActivity.this::onFavoriteProductSaved);
    }

    private void setActivityTitle() {
        if (getIntent().hasExtra(FavoriteProductEditorFragment.ARGUMENT_FAVORITE_PRODUCT_ID))
            setTitle(R.string.activity_title_edit_favorite_product);
        else setTitle(R.string.activity_title_add_favorite_product);
    }

    @Override
    public void onFavoriteProductSaved(String productId) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FAVORITE_PRODUCT_ID, productId);
        setResult(RESULT_ADD_EDIT_FAVORITE_PRODUCT_OK, intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

