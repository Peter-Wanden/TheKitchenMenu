package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.viewmodels.ImageEditorViewModel;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductIdentityViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductMeasurementViewModel;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

public class ProductEditor extends AppCompatActivity {

    private static final String TAG = "ProductEditor";
    private static final String PRODUCT_ID = "product_id";

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

        setTitle(productEditorViewModel.getTitle());
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

                productEditorViewModel.getImageModel().setValue(imageModel);

                ProductIdentityModel identityModel = new ProductIdentityModel();
                identityModel.setDescription(productEntity.getDescription());
                identityModel.setMadeBy(productEntity.getMadeBy());
                identityModel.setCategory(productEntity.getCategory());
                identityModel.setShelfLife(productEntity.getShelfLife());

                productEditorViewModel.getIdentityModel().setValue(identityModel);

                ProductMeasurementModel measurementModel = new ProductMeasurementModel();
                measurementModel.setMeasurementSubType(
                        MeasurementSubType.values()[productEntity.getUnitOfMeasureSubType()]);
                measurementModel.setNumberOfItems(productEntity.getNumberOfItems());
                measurementModel.setBaseSiUnits(productEntity.getBaseSiUnits());

                productEditorViewModel.getMeasurementModel().setValue(measurementModel);
            }
        };

        productEditorViewModel.getProductEntity().observe(this, productObserver);

        // Dish out the models to the view models
        final Observer<ImageModel> imageEditorObserver = imageModel -> {

            imageEditorViewModel.getImageModel().setValue(imageModel);
            Log.d(TAG, "setObservers: image model updated");
        };

        productEditorViewModel.getImageModel().observe(this, imageEditorObserver);

        final Observer<ProductIdentityModel> identityModelObserver = newIdentityModel ->
                identityEditorViewModel.getIdentityModel().setValue(newIdentityModel);

        productEditorViewModel.getIdentityModel().observe(this, identityModelObserver);

        final Observer<ProductMeasurementModel> measurementModelObserver = newMeasurementModel ->
                measurementEditorViewModel.getMeasurementModel().setValue(newMeasurementModel);

        productEditorViewModel.getMeasurementModel().observe(this, measurementModelObserver);
    }
}
