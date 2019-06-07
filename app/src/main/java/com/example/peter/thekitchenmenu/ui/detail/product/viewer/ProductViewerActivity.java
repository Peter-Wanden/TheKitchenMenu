package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

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
import com.example.peter.thekitchenmenu.ui.detail.product.productuserdataeditor.UsedProductEditorFragment;
import com.example.peter.thekitchenmenu.ui.detail.product.productuserdataeditor.UsedProductEditorViewModel;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class ProductViewerActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";

    private ProductViewerActivityBinding binding;
    private ProductViewerViewModel viewerViewModel;
    private UsedProductEditorViewModel userDataEditorViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        addFragments();
    }

    private void addFragments() {
        ProductViewerFragment productViewerFragment = findOrReplaceViewerFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                productViewerFragment,
                R.id.product_viewer_content_frame);
        viewerViewModel = obtainProductViewerViewModel(this);

        UsedProductEditorFragment usedProductEditorFragment =
                findOrReplaceUsedProductEditorFragment();
        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                usedProductEditorFragment,
                R.id.used_product_editor_content_frame);
        userDataEditorViewModel = obtainUsedProductEditorViewModel(this);
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

    @NonNull
    private ProductViewerFragment findOrReplaceViewerFragment() {
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_viewer_content_frame);

        if (fragment == null) fragment = ProductViewerFragment.newInstance(productId);
        return fragment;
    }

    @NonNull
    private UsedProductEditorFragment findOrReplaceUsedProductEditorFragment() {
        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);

        UsedProductEditorFragment fragment = (UsedProductEditorFragment)
                getSupportFragmentManager().findFragmentById(R.id.used_product_editor_content_frame);

        if (fragment == null) fragment = UsedProductEditorFragment.newInstance(productId);
        return fragment;
    }

    public static ProductViewerViewModel obtainProductViewerViewModel(
            FragmentActivity activity) {
        ViewModelFactoryProduct factory = ViewModelFactoryProduct.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProductViewerViewModel.class);
    }

    public static UsedProductEditorViewModel obtainUsedProductEditorViewModel(
            FragmentActivity activity) {
        ViewModelFactoryProduct factory = ViewModelFactoryProduct.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(UsedProductEditorViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
