package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.databinding.ProductViewerActivityBinding;
import com.example.peter.thekitchenmenu.ui.AppCompatActivityDialogActions;
import com.example.peter.thekitchenmenu.ui.UnsavedChangesDialogFragment;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryFavoriteProduct;
import com.example.peter.thekitchenmenu.ui.detail.product.favoriteproducteditor.FavoriteProductEditorActivity;
import com.example.peter.thekitchenmenu.ui.detail.product.producteditor.ProductEditorActivity;
import com.example.peter.thekitchenmenu.utils.ActivityUtils;

public class ProductViewerActivity
        extends AppCompatActivityDialogActions
        implements ProductViewerNavigator, FavoriteProductViewerNavigator {

    private static final String TAG = "tkm-" + ProductViewerActivity.class.getSimpleName() + ":";

    public static final int REQUEST_VIEW_PRODUCT = 1;
    public static final int RESULT_VIEW_DATA_CHANGED = RESULT_FIRST_USER + 1;
    public static final int RESULT_VIEW_NO_DATA_CHANGED = RESULT_FIRST_USER + 2;

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
        binding.setViewModel(productViewerViewModel);

        favoriteProductViewerViewModel = obtainFavoriteProductViewerViewModel(this);
        favoriteProductViewerViewModel.setNavigator(this);
    }

    private void setupObservers() {
        productViewerViewModel.getSetTitleEvent().observe(this, titleStringResourceId ->
                setTitle(ProductViewerActivity.this.getResources().getString(titleStringResourceId)));

        favoriteProductViewerViewModel.isFavoriteAddedEdited().observe(this,
                favoriteChanged -> productViewerViewModel.setDataHasChanged(favoriteChanged));
    }

    public static ProductViewerViewModel obtainProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryProduct factory = ViewModelFactoryProduct.getInstance(
                activity.getApplication());

        return new ViewModelProvider(activity, factory).get(ProductViewerViewModel.class);
    }

    public static FavoriteProductViewerViewModel obtainFavoriteProductViewerViewModel(
            FragmentActivity activity) {

        ViewModelFactoryFavoriteProduct factory = ViewModelFactoryFavoriteProduct.getInstance(
                activity.getApplication());

        return new ViewModelProvider(activity, factory).get(FavoriteProductViewerViewModel.class);
    }

    private void addFragments() {
        Intent intent = getIntent();
        String productId = null;

        if (intent.hasExtra(ProductEditorActivity.EXTRA_PRODUCT_ENTITY)) {
            ProductEntity productEntity = getIntent().getParcelableExtra(
                    ProductEditorActivity.EXTRA_PRODUCT_ENTITY);
            productId = productEntity.getId();
            findOrReplaceProductViewerFragment(productEntity);

        } else if (intent.hasExtra(ProductEditorActivity.EXTRA_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID);
            findOrReplaceProductViewerFragment(productId);
        }
        findOrReplaceFavoriteProductViewerFragment(productId);
    }

    private void findOrReplaceProductViewerFragment(ProductEntity productEntity) {
        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_viewer_content_frame);

        if (fragment == null)
            fragment = ProductViewerFragment.newInstance(productEntity);

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment, R.id.product_viewer_content_frame);
    }

    private void findOrReplaceProductViewerFragment(String productId) {
        ProductViewerFragment fragment = (ProductViewerFragment)
                getSupportFragmentManager().findFragmentById(R.id.product_viewer_content_frame);

        if (fragment == null)
            fragment = ProductViewerFragment.newInstance(productId);

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment, R.id.product_viewer_content_frame);
    }

    private void findOrReplaceFavoriteProductViewerFragment(String productId) {
        FavoriteProductViewerFragment fragment = (FavoriteProductViewerFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.favorite_product_viewer_content_frame);

        if (fragment == null)
            fragment = FavoriteProductViewerFragment.newInstance(productId);

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment, R.id.favorite_product_viewer_content_frame);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ProductEditorActivity.REQUEST_ADD_EDIT_PRODUCT) {
            productViewerViewModel.handleActivityResult(resultCode, data);
        }

        else if (requestCode == FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT) {
            favoriteProductViewerViewModel.handleActivityResult(resultCode, data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        productViewerViewModel.upOrBackPressed();
    }

    @Override
    public void editProduct(ProductEntity productEntity) {
        Intent intent = new Intent(this, ProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ENTITY, productEntity);
        startActivityForResult(intent, ProductEditorActivity.REQUEST_ADD_EDIT_PRODUCT);
    }

    @Override
    public void deleteProduct(String productId) {
        // Product deleted by view model, if favorite exists it too must be deleted
        favoriteProductViewerViewModel.deleteFavoriteProduct();
        setResult(RESULT_VIEW_DATA_CHANGED);
        finish();
    }

    @Override
    public void doneWithProduct(String productId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);

        setResult(RESULT_VIEW_DATA_CHANGED, resultIntent);
        finish();
    }

    @Override
    public void postProduct() {
        // Handled by {@link ProductViewerViewModel}
    }

    @Override
    public void discardProductEdits() {
        discardChanges();
    }

    @Override
    public void discardChanges() {
        setResult(RESULT_VIEW_NO_DATA_CHANGED);
        finish();
    }

    @Override
    public void addFavoriteProduct(String productId) {
        addEditFavoriteProduct(productId);
    }

    @Override
    public void editFavoriteProduct(String productId) {
        addEditFavoriteProduct(productId);
    }

    private void addEditFavoriteProduct(String productId) {
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    public void showUnsavedChangesDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment previousDialog = getSupportFragmentManager().findFragmentByTag(
                UnsavedChangesDialogFragment.TAG);

        if (previousDialog != null)
            ft.remove(previousDialog);
        ft.addToBackStack(null);

        UnsavedChangesDialogFragment dialogFragment = UnsavedChangesDialogFragment.newInstance(
                this.getTitle().toString());
        dialogFragment.show(ft, UnsavedChangesDialogFragment.TAG);
    }

    @Override
    protected void onDestroy() {
        productViewerViewModel.onActivityDestroyed();
        favoriteProductViewerViewModel.onActivityDestroyed();
        super.onDestroy();
    }
}