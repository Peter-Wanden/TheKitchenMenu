package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.imageeditor.ImageEditorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

public class ProductEditorActivity extends AppCompatActivity implements AddEditProductNavigator {

    private static final String TAG = "tkm-EditorActivity";
    public static final String EXTRA_PRODUCT_ID = "PRODUCT_ID";
    public static final String EXTRA_IS_CREATOR = "IS_CREATOR";

    ProductEditorBinding productEditorBinding;
    ProductEditorViewModel productEditorViewModel;

    ImageEditorViewModel imageEditorViewModel;
    ProductIdentityViewModel identityEditorViewModel;
    ProductMeasurementViewModel measurementEditorViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseViews();
        setViewModels();
        setObservers();
        setupFab();

        // TODO - Get the intent, establish if existing product to edit or new product to create
//        Intent intent = getIntent();
//
//        if (intent != null && intent.hasExtra(PRODUCT_ID)) {
//
//            productEditorViewModel.isNewProduct(false);
//
//        } else {
//
//            productEditorViewModel.isNewProduct(true);
//
//        }

        setTitle(productEditorViewModel.getActivityTitle());
    }

    private void initialiseViews() {

        productEditorBinding = DataBindingUtil.setContentView(this, R.layout.product_editor);
        productEditorBinding.setLifecycleOwner(this);
        setSupportActionBar(productEditorBinding.toolbar);
    }

    private void setViewModels() {

        productEditorViewModel = ViewModelProviders.of(
                this).get(ProductEditorViewModel.class);

        imageEditorViewModel = ViewModelProviders.of(
                this).get(ImageEditorViewModel.class);

        identityEditorViewModel = ViewModelProviders.of(
                this).get(ProductIdentityViewModel.class);

        measurementEditorViewModel = ViewModelProviders.of(
                this).get(ProductMeasurementViewModel.class);
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

                measurementEditorViewModel.setMeasurementModelIn(measurementModelIn);
            }
        };

        productEditorViewModel.getExistingProductEntity().observe(this, productObserver);

        // TODO - Observe the Models values - report them back ProductEditorViewModel
        final Observer<ImageModel> imageModelObserver = imageModel ->
                productEditorViewModel.setUpdatedImageModel(imageModel);

        imageEditorViewModel.getExistingImageModel().observe(
                this, imageModelObserver);

        final Observer<ProductIdentityModel> identityModelObserver = IdentityModel ->
                productEditorViewModel.setUpdatedIdentityModel(IdentityModel);

        identityEditorViewModel.getExistingIdentityModel().observe(
                this, identityModelObserver);

        final Observer<ProductMeasurementModel> measurementModelOutObserver = measurementModelOut ->
                productEditorViewModel.setUpdatedMeasurementModel(measurementModelOut);

        measurementEditorViewModel.getModelOut().observe(
                this, measurementModelOutObserver);
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.product_editor_save_fab);
        fab.setOnClickListener(v -> productEditorViewModel.onFabClick());
    }

    @Override
    public void reviewBeforeSave() {

    }
}
