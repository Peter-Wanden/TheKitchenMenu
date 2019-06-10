package com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
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

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";
    public static final int REQUEST_ADD_EDIT_USED_PRODUCT_DETAILS = 1;
    public static final int ADD_USED_PRODUCT_DETAILS_OK = RESULT_FIRST_USER + 1;

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

    }

    private void initialiseBindings() {
        binding = DataBindingUtil.setContentView(this,
                R.layout.used_product_editor_activity);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.usedProductEditorActivityToolbar);
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
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        ProductViewerFragment productViewerFragment =
                findOrCreateProductViewerFragment(productId);
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                productViewerFragment,
                R.id.product_viewer_cotentFrame);

        UsedProductEditorFragment usedProductEditorFragment =
                findOrCreateUsedProductEditorFragment(productId);
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                usedProductEditorFragment,
                R.id.used_product_editor_contentFrame);
    }

    private ProductViewerFragment findOrCreateProductViewerFragment(String productId) {
        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().
                findFragmentById(R.id.product_viewer_cotentFrame);

        if (fragment == null) fragment = ProductViewerFragment.newInstance(productId);
        return fragment;
    }

    private UsedProductEditorFragment findOrCreateUsedProductEditorFragment(String productId) {
        UsedProductEditorFragment fragment = (UsedProductEditorFragment)
                getSupportFragmentManager().
                findFragmentById(R.id.used_product_editor_contentFrame);

        if (fragment == null) fragment = UsedProductEditorFragment.newInstance(productId);
        return fragment;
    }

    @Override
    public void onUsedProductSaved() {
        setResult(ADD_USED_PRODUCT_DETAILS_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

