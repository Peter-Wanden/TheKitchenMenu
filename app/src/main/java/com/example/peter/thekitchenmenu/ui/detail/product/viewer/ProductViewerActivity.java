package com.example.peter.thekitchenmenu.ui.detail.product.viewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryUsedProduct;
import com.example.peter.thekitchenmenu.ui.detail.product.usedproducteditor.UsedProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class ProductViewerActivity extends AppCompatActivity implements ProductViewerNavigator {

    private static final String TAG = "tkm-ProductViewerAct";

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";

    private ProductViewerActivityBinding binding;
    private ProductViewerViewModel productViewerViewModel;
    private UsedProductViewerViewModel usedProductViewerViewModel;
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
        usedProductViewerViewModel = obtainUsedProductViewerViewModel(this);
    }

    public static ProductViewerViewModel obtainProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryProduct factory = ViewModelFactoryProduct.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProductViewerViewModel.class);
    }

    public static UsedProductViewerViewModel obtainUsedProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryUsedProduct factory = ViewModelFactoryUsedProduct.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(UsedProductViewerViewModel.class);
    }

    private void addFragments() {

        ProductViewerFragment productViewerFragment =
                findOrReplaceViewerFragment(productId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                productViewerFragment,
                R.id.product_viewer_content_frame);

        UsedProductViewerFragment usedProductViewerFragment =
                findOrReplaceUsedProductViewerFragment(productId);

        ActivityUtils.replaceFragmentInActivity(
                getSupportFragmentManager(),
                usedProductViewerFragment,
                R.id.used_product_viewer_content_frame);
    }

    @NonNull
    private ProductViewerFragment findOrReplaceViewerFragment(String productId) {

        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_viewer_content_frame);

        if (fragment == null) fragment = ProductViewerFragment.newInstance(productId);
        return fragment;
    }

    @NonNull
    private UsedProductViewerFragment findOrReplaceUsedProductViewerFragment(String productId) {

        UsedProductViewerFragment fragment = (UsedProductViewerFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.used_product_viewer_content_frame);

        if (fragment == null) fragment = UsedProductViewerFragment.newInstance(productId);
        return fragment;
    }

    private void subscribeToNavigationChanges() {
        usedProductViewerViewModel.getAddUsedProduct().observe(this, addUsedProductEvent ->
                ProductViewerActivity.this.addNewUsedProduct());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void editProduct() {
        // Navigate to ProductEditor, with onActivityResult()
    }

    @Override
    public void deleteUsedProduct() {
        // delete from used products and finish
    }

    @Override
    public void addNewUsedProduct() {
        Intent intent = new Intent(this, UsedProductEditorActivity.class);

        intent.putExtra(
                UsedProductEditorActivity.EXTRA_PRODUCT_ID,
                productViewerViewModel.product.get().getId());

        startActivityForResult(
                intent,
                UsedProductEditorActivity.REQUEST_ADD_NEW_USED_PRODUCT);
    }

    @Override
    public void editUsedProduct() {
        Intent intent = new Intent(this, UsedProductEditorActivity.class);

        intent.putExtra(
                UsedProductEditorActivity.EXTRA_PRODUCT_ID,
                usedProductViewerViewModel.usedProduct.get().getProductId());
        intent.putExtra(
                UsedProductEditorActivity.EXTRA_USED_PRODUCT_ID,
                usedProductViewerViewModel.usedProduct.get().getId());

        startActivityForResult(
                intent,
                UsedProductEditorActivity.REQUEST_EDIT_USED_PRODUCT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
