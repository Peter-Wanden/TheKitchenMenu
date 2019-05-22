package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.imageeditor.ImageEditorViewModel;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

public class ProductEditorActivity extends AppCompatActivity {

    private static final String TAG = "tkm-EditorActivity";
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

                imageEditorViewModel.getExistingImageModel().setValue(imageModel);

                ProductIdentityModel identityModel = new ProductIdentityModel();
                identityModel.setDescription(productEntity.getDescription());
                identityModel.setShoppingListItemName(productEntity.getShoppingListItemName());
                identityModel.setCategory(productEntity.getCategory());
                identityModel.setShelfLife(productEntity.getShelfLife());

                identityEditorViewModel.getExistingIdentityModel().setValue(identityModel);

                ProductMeasurementModel measurementModel = new ProductMeasurementModel();
                measurementModel.setMeasurementSubType(
                        MeasurementSubType.values()[productEntity.getUnitOfMeasureSubType()]);
                measurementModel.setNumberOfItems(productEntity.getNumberOfItems());
                measurementModel.setBaseSiUnits(productEntity.getBaseSiUnits());

                measurementEditorViewModel.getExistingMeasurementModel().setValue(measurementModel);
            }
        };

        productEditorViewModel.getExistingProductEntity().observe(this, productObserver);

        // TODO - Observe the Models values - report them back ProductEditorViewModel
        final Observer<ImageModel> imageModelObserver = imageModel -> {

            Log.d(TAG, "setObservers: ImageViewModel: New model received");
            productEditorViewModel.setUpdatedImageModel(imageModel);
        };

        imageEditorViewModel.getExistingImageModel().observe(
                this, imageModelObserver);

        final Observer<ProductIdentityModel> identityModelObserver = IdentityModel -> {

            Log.d(TAG, "setObservers: IdentityViewModel: New model received");
            productEditorViewModel.setUpdatedIdentityModel(IdentityModel);
        };

        identityEditorViewModel.getExistingIdentityModel().observe(
                this, identityModelObserver);

        final Observer<ProductMeasurementModel> measurementModelObserver = measurementModel -> {

            Log.d(TAG, "setObservers: MeasurementViewModel: New model received");
            productEditorViewModel.setUpdatedMeasurementModel(measurementModel);
        };

        measurementEditorViewModel.getExistingMeasurementModel().observe(
                this, measurementModelObserver);
    }
}
