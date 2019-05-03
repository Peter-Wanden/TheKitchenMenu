package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.viewmodels.ImageEditorViewModel;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductIdentityViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductMeasurementViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductUserDataEditorViewModel;

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
    ProductUserDataEditorViewModel userDataEditorViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialiseViews();
        setViewModels();
        setObservers();
        setModelValidationObservers();

        // TODO - Get the intent, establish if existing product to edit or new product to create
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(PRODUCT_ID)) {

            productEditorViewModel.isNewProduct(false);

        } else {

            productEditorViewModel.isNewProduct(true);

        }

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

        userDataEditorViewModel = ViewModelProviders.of(
                this).get(ProductUserDataEditorViewModel.class);
    }

    private void setObservers() {

        // Dish out the entities to the models
        final Observer<ProductEntity> productObserver = productEntity -> {

            if (productEntity != null) {

                imageEditorViewModel.getImageModel().setRemoteImageUri(
                        productEntity.getRemoteImageUri());

                identityEditorViewModel.getIdentityModel().setDescription(
                        productEntity.getDescription());

                identityEditorViewModel.getIdentityModel().setMadeBy(
                        productEntity.getMadeBy());

                identityEditorViewModel.getIdentityModel().setCategory(
                        productEntity.getCategory());

                identityEditorViewModel.getIdentityModel().setShelfLife(
                        productEntity.getShelfLife());

                measurementEditorViewModel.getMeasurementValidation().changeUnitOfMeasure(
                        productEntity.getUnitOfMeasureSubType());

                measurementEditorViewModel.getMeasurementValidation().numberOfItemsUpdated(
                        productEntity.getNumberOfItems());

                boolean baseSiUnitsAreSet =
                        measurementEditorViewModel.getMeasurementValidation().setBaseSiUnits(
                        productEntity.getBaseSiUnits());

                if (baseSiUnitsAreSet) {
                    Log.d(TAG, "tkm - setObservers: Base units set!");
                }
            }
        };

        productEditorViewModel.getProductEntity().observe(
                this, productObserver);

        final Observer<ProductUserDataEntity> userDataObserver = userDataEntity -> {

            if (userDataEntity != null) {

                imageEditorViewModel.getImageModel().setLocalImageUri(
                        userDataEntity.getLocalImageUri());

                userDataEditorViewModel.getUserDataModel().setRetailer(
                        userDataEntity.getRetailer());

                userDataEditorViewModel.getUserDataModel().setPrice(
                        userDataEntity.getPrice());

                userDataEditorViewModel.getUserDataModel().setLocationRoom(
                        userDataEntity.getLocationRoom());

                userDataEditorViewModel.getUserDataModel().setLocationInRoom(
                        userDataEntity.getLocationInRoom());
            }
        };

        productEditorViewModel.getProductUserDataEntity().observe(
                this, userDataObserver);
    }

    private void setModelValidationObservers() {

        final Observer<Boolean> imageModelIsValid = imageIsValid -> {

            if (imageIsValid) checkAllProductModelsAreValidated();
        };

        imageEditorViewModel.getImageModelIsValid().observe(
                this, imageModelIsValid);

        final Observer<Boolean> identityModelIsValid = identityIsValid -> {

            if (identityIsValid) checkAllProductModelsAreValidated();
        };

        identityEditorViewModel.getGetIdentityModelIsValid().observe(
                this, identityModelIsValid);

        final Observer<Boolean> measurementModelIsValid = measurementIsValid -> {

            if (measurementIsValid) checkAllProductModelsAreValidated();
        };

        measurementEditorViewModel.getMeasurementIsValid().observe(
                this, measurementModelIsValid);

        final Observer<Boolean> userDataModelIsValid = userDataValid -> {

            if (userDataValid) checkAllProductModelsAreValidated();
        };

        userDataEditorViewModel.getUserDataModelIsValidated().observe(
                this, userDataModelIsValid);

        final Observer<Boolean> entityModelsAreValid = entitiesValid -> {

            // data entities can be saved
        };

        productEditorViewModel.getEntitiesAreValid().observe(
                this, entityModelsAreValid);
    }

    private void checkAllProductModelsAreValidated() {

        if (identityEditorViewModel.getGetIdentityModelIsValid().getValue() == Boolean.TRUE) {

            productEditorViewModel.getEntitiesAreValid().setValue(Boolean.TRUE);
            Log.d(TAG, "tkm - all entities are valid, saving entity data.");
        }
    }
}
