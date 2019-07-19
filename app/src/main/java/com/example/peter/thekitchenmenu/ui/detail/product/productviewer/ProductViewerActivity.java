package com.example.peter.thekitchenmenu.ui.detail.product.productviewer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
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
        start();
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
        productViewerViewModel.getSetTitleEvent().observe(this, this::setActivityTitle);
    }

    private void setActivityTitle(String title) {
        setTitle(title);
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
        String productId = getIntent().getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID);
        findOrReplaceViewerFragment(productId);
        findOrReplaceFavoriteProductViewerFragment(productId);
    }

    private void findOrReplaceViewerFragment(String productId) {
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

        if (fragment == null) {
            fragment = FavoriteProductViewerFragment.newInstance(productId);
        }
        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                fragment, R.id.favorite_product_viewer_content_frame);
    }


    // Start modes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode);

        if (requestCode == ProductEditorActivity.REQUEST_ADD_EDIT_PRODUCT) {
            productViewerViewModel.handleActivityResult(requestCode, resultCode, data);
            favoriteProductViewerViewModel.start(
                    productViewerViewModel.productEntityObservable.get().getId());
        }

        else if (requestCode == FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT) {
            favoriteProductViewerViewModel.handleActivityResult(resultCode, data);
            productViewerViewModel.handleActivityResult(requestCode, resultCode, data);
        }
    }

    private void start() {
        Intent intent = getIntent();

        if (intent.hasExtra(ProductEditorActivity.EXTRA_PRODUCT_ENTITY)) {
            ProductEntity productEntity = getIntent().getParcelableExtra(
                    ProductEditorActivity.EXTRA_PRODUCT_ENTITY);
            productViewerViewModel.start(productEntity);
            favoriteProductViewerViewModel.start(productEntity.getId());
        }
        else if (intent.hasExtra(ProductEditorActivity.EXTRA_PRODUCT_ID)) {
            String productId = intent.getStringExtra(ProductEditorActivity.EXTRA_PRODUCT_ID);
            productViewerViewModel.start(productId);
            favoriteProductViewerViewModel.start(productId);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void editProduct(ProductEntity productEntity) {
        Intent intent = new Intent(this, ProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ENTITY, productEntity);
        startActivityForResult(intent, ProductEditorActivity.REQUEST_ADD_EDIT_PRODUCT);
    }

    @Override
    public void deleteProduct(String productId) {
        // Product has been deleted, if favorite exists it must delete also
        favoriteProductViewerViewModel.deleteFavoriteProduct();
        setResult(RESULT_DELETE_PRODUCT_OK);
        // todo - clear the backstack in the intent
        finish();
    }

    @Override
    public void doneWithProduct(String productId) {
        if (productId != null) {
            Intent intent = new Intent();
            intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        }
        setResult(ProductEditorActivity.RESULT_ADD_EDIT_PRODUCT_OK);
        // todo - clear the backstack
        finish();
    }

    @Override
    public void addFavoriteProduct(String productId) {
        Log.d(TAG, "addFavoriteProduct: productId=" + productId);
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    public void editFavoriteProduct(String productId) {
        Intent intent = new Intent(this, FavoriteProductEditorActivity.class);
        intent.putExtra(ProductEditorActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(intent,
                FavoriteProductEditorActivity.REQUEST_ADD_EDIT_FAVORITE_PRODUCT);
    }

    @Override
    protected void onDestroy() {
        productViewerViewModel.onActivityDestroyed();
        favoriteProductViewerViewModel.onActivityDestroyed();
        super.onDestroy();
    }
}