package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.ui.ViewModelFactoryProduct;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.ui.imageeditor.ImageEditorViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

public class ProductEditorActivity extends AppCompatActivity implements AddEditProductNavigator {

    private static final String TAG = "tkm-EditorActivity";

    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";
    public static final int REQUEST_ADD_EDIT_PRODUCT = 5;
    public static final int RESULT_ADD_EDIT_PRODUCT_OK = RESULT_FIRST_USER + 4;

    private ProductEditorBinding productEditorBinding;

    private ProductEditorViewModel productEditorViewModel;
    private ImageEditorViewModel imageEditorViewModel;
    private ProductIdentityViewModel identityEditorViewModel;
    private ProductMeasurementViewModel measurementEditorViewModel;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseBindings();
        setupToolbar();
        setViewModels();
        setObservers();
        subscribeToNavigationChanges();
        getProductId();
    }

    private void initialiseBindings() {
        productEditorBinding = DataBindingUtil.setContentView(this, R.layout.product_editor);
        productEditorBinding.setLifecycleOwner(this);
    }

    private void setViewModels() {
        productEditorViewModel = obtainViewModel(this);
        productEditorViewModel.setNavigator(this);

        imageEditorViewModel = ViewModelProviders.of(
                this).get(ImageEditorViewModel.class);

        identityEditorViewModel = ViewModelProviders.of(
                this).get(ProductIdentityViewModel.class);

        measurementEditorViewModel = ViewModelProviders.of(
                this).get(ProductMeasurementViewModel.class);
    }

    private static ProductEditorViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactoryProduct factory = ViewModelFactoryProduct.getInstance(
                activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(ProductEditorViewModel.class);
    }

    private void setObservers() {

        // Dish out the entity values to the models
        final Observer<ProductEntity> productObserver = productEntity -> {

            if (productEntity != null) {

                ImageModel imageModel = new ImageModel();
                imageModel.setWebImageUrl(productEntity.getWebImageUrl());
                imageModel.setRemoteSmallImageUri(imageModel.getRemoteSmallImageUri());
                imageModel.setRemoteMediumImageUri(imageModel.getRemoteMediumImageUri());
                imageModel.setRemoteLargeImageUri(imageModel.getRemoteLargeImageUri());

                imageEditorViewModel.getExistingImageModel().setValue(imageModel);

                ProductIdentityModel identityModel = new ProductIdentityModel();
                identityModel.setDescription(productEntity.getDescription());
                identityModel.setShoppingListItemName(productEntity.getShoppingListItemName());
                identityModel.setCategory(productEntity.getCategory());
                identityModel.setShelfLife(productEntity.getShelfLife());

                identityEditorViewModel.getExistingIdentityModel().setValue(identityModel);

                ProductMeasurementModel measurementModelIn = new ProductMeasurementModel();
                measurementModelIn.setMeasurementSubtype(
                        MeasurementSubtype.values()[productEntity.getUnitOfMeasureSubtype()]);
                measurementModelIn.setNumberOfProducts(productEntity.getNumberOfProducts());
                measurementModelIn.setBaseUnits(productEntity.getBaseUnits());

                measurementEditorViewModel.setMeasurementModel(measurementModelIn);
            }
        };

        productEditorViewModel.getExistingProductEntity().observe(this, productObserver);

        final Observer<ImageModel> imageModelObserver = imageModel ->
                productEditorViewModel.setUpdatedImageModel(imageModel);

        imageEditorViewModel.getExistingImageModel().observe(
                this, imageModelObserver);

        final Observer<ProductIdentityModel> identityModelObserver = IdentityModel ->
                productEditorViewModel.setUpdatedIdentityModel(IdentityModel);

        identityEditorViewModel.getExistingIdentityModel().observe(
                this, identityModelObserver);

        final Observer<ProductMeasurementModel> measurementModelObserver = measurementModel ->
                productEditorViewModel.setUpdatedMeasurementModel(measurementModel);

        measurementEditorViewModel.getMeasurementModel().observe(
                this, measurementModelObserver);
    }

    private void subscribeToNavigationChanges() {
        identityEditorViewModel.getIdentityModelValidEvent().observe(
                this, identityModelIsValid ->
                        productEditorViewModel.setIdentityModelIsValid(identityModelIsValid));

        measurementEditorViewModel.getMeasurementModelIsValidEvent().observe(
                this, measurementModelIsValid ->
                        productEditorViewModel.setMeasurementModelIsValid(measurementModelIsValid));

        productEditorViewModel.getShowSaveButtonEvent().observe(this, showSaveButton -> {
            ProductEditorActivity.this.showSaveButton = showSaveButton;
            invalidateOptionsMenu();
        });
    }

    private void getProductId() {
        if (getIntent().getStringExtra(EXTRA_PRODUCT_ID) != null) {
            productEditorViewModel.editProduct(getIntent().getStringExtra(EXTRA_PRODUCT_ID));
            productEditorViewModel.setExistingProduct(true);
            setTitle(getApplicationContext().getString(R.string.activity_title_edit_product));
        } else {
            productEditorViewModel.setExistingProduct(false);
            setTitle(R.string.activity_title_add_new_product);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(productEditorBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null && getIntent().getStringExtra(EXTRA_PRODUCT_ID) != null) {
            productEditorBinding.toolbar.setTitle(
                    this.getString(R.string.activity_title_edit_product));
        } else {
            productEditorBinding.toolbar.setTitle(
                    this.getString(R.string.activity_title_add_new_product));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_editor, menu);
        return true;
    }

    private boolean showSaveButton;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_product_editor_action_save).setVisible(showSaveButton);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_product_editor_action_save){
            productEditorViewModel.saveProduct();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductSaved(String productId) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_PRODUCT_ID, productId);
        setResult(RESULT_ADD_EDIT_PRODUCT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        productEditorViewModel.onActivityDestroyed();
        super.onDestroy();
    }
}
