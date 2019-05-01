package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.viewmodels.ImageEditorViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductIdentityViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductMeasurementViewModel;
import com.example.peter.thekitchenmenu.viewmodels.ProductUserDataEditorViewModel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProductEditor extends AppCompatActivity {

    private static final String TAG = "ProductEditor";

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
        // TODO - Get the intent, establish if existing product to edit or new product to create
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

        final Observer<Boolean> productIdentityValid = identityIsValid -> {

            if (identityIsValid != null) {

                checkAllProductModelsAreValidated();
            }
        };

        // TODO - Set up observers from each ViewModel that point to main view model
        productEditorViewModel.getProductIdentityModelValid().observe(
                this, productIdentityValid);

        final Observer<Boolean> identityModelIsValid = identityIsValid -> {

            if (identityIsValid) {

                checkAllProductModelsAreValidated();
            }
        };

        identityEditorViewModel.getGetIdentityModelIsValid().observe(
                this, identityModelIsValid);
    }

    private void checkAllProductModelsAreValidated() {

        if (productEditorViewModel.getProductIdentityModelValid().getValue() == Boolean.TRUE) {

            Log.d(TAG, "tkm - checkAllProductModelsAreValidated: Called!");
            productEditorViewModel.getAllProductDataValid().setValue(Boolean.TRUE);
        }
    }
}
