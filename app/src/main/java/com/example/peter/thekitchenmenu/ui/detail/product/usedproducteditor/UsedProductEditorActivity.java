package com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.UsedProductEditorActivityBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryUsedProduct;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.ProductViewerFragment;
import com.example.peter.thekitchenmenu.ui.detail.product.viewer.ProductViewerViewModel;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class UsedProductEditorActivity
        extends AppCompatActivity
        implements AddEditUsedProductNavigator {

    private static final String TAG = "tkm-UsedProductEditAct";

    // Intent data
    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";
    // Intent requests
    public static final int REQUEST_ADD_EDIT_USED_PRODUCT = 3;
    // Intent results
    public static final int RESULT_ADD_EDIT_USED_PRODUCT_OK = RESULT_FIRST_USER + 1;

    private UsedProductEditorActivityBinding binding;
    private ProductViewerViewModel productViewerViewModel;
    private UsedProductEditorViewModel usedProductEditorViewModel;

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
                R.layout.used_product_editor_activity);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.usedProductEditorActivityToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setupViewModels() {
        productViewerViewModel = obtainProductViewerViewModel(this);
        usedProductEditorViewModel = obtainUsedProductEditorViewModel(this);
    }

    @NonNull
    public static ProductViewerViewModel obtainProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryProduct factoryProduct =
                ViewModelFactoryProduct.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factoryProduct).get(ProductViewerViewModel.class);
    }

    @NonNull
    public static UsedProductEditorViewModel obtainUsedProductEditorViewModel(
            FragmentActivity activity) {

        ViewModelFactoryUsedProduct factoryUsedProduct =
                ViewModelFactoryUsedProduct.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factoryUsedProduct).
                get(UsedProductEditorViewModel.class);
    }

    private void findOrCreateFragments() {
        String productId = null;
        String usedProductId = null;

        if (getIntent().hasExtra(EXTRA_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        }

        if (getIntent().hasExtra(UsedProductEditorFragment.ARGUMENT_USED_PRODUCT_ID)) {
            usedProductId = getIntent().getStringExtra(
                    UsedProductEditorFragment.ARGUMENT_USED_PRODUCT_ID);
        }

        ProductViewerFragment productViewerFragment =
                findOrCreateProductViewerFragment(productId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                productViewerFragment,
                R.id.product_viewer_contentFrame);

        UsedProductEditorFragment usedProductEditorFragment =
                findOrCreateUsedProductEditorFragment(productId, usedProductId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                usedProductEditorFragment,
                R.id.used_product_editor_contentFrame);
    }

    private ProductViewerFragment findOrCreateProductViewerFragment(String productId) {

        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().
                findFragmentById(R.id.product_viewer_contentFrame);

        if (fragment == null) fragment = ProductViewerFragment.newInstance(productId);

        return fragment;
    }

    private UsedProductEditorFragment findOrCreateUsedProductEditorFragment(String productId,
                                                                            String usedProductId) {
        UsedProductEditorFragment fragment = (UsedProductEditorFragment)
                getSupportFragmentManager().
                findFragmentById(R.id.used_product_editor_contentFrame);

        if (fragment == null) fragment =
                UsedProductEditorFragment.newInstance(productId, usedProductId);

        return fragment;
    }

    private void setBindingInstanceVariables() {
        binding.setViewModel(usedProductEditorViewModel);
    }

    private void subscribeToNavigationChanges() {
        usedProductEditorViewModel.getUsedProductIsUpdated().observe(this, saved ->
                UsedProductEditorActivity.this.onUsedProductSaved());
    }

    private void setActivityTitle() {
        if (getIntent().hasExtra(UsedProductEditorFragment.ARGUMENT_USED_PRODUCT_ID))
            setTitle(R.string.activity_title_edit_used_product);
        else setTitle(R.string.activity_title_add_used_product);
    }

    @Override
    public void onUsedProductSaved() {
        setResult(RESULT_ADD_EDIT_USED_PRODUCT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

