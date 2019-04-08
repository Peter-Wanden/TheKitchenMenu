package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.os.Bundle;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
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

// TODO - remove all the code from ProductEditor Fragment and rename this activity to ProductEditor
public class ProductEditor extends AppCompatActivity {

    private static final String TAG = "ProductEditor";

    ProductEditorBinding productEditorBinding;

    ProductEditorViewModel productEditorViewModel;
    ImageEditorViewModel imageEditorViewModel;
    ProductIdentityViewModel productIdentityViewModel;
    ProductMeasurementViewModel productMeasurementViewModel;
    ProductUserDataEditorViewModel productUserDataEditorViewModel;

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

        productIdentityViewModel = ViewModelProviders.of(
                this).get(ProductIdentityViewModel.class);

        productMeasurementViewModel = ViewModelProviders.of(
                this).get(ProductMeasurementViewModel.class);

        productUserDataEditorViewModel = ViewModelProviders.of(
                this).get(ProductUserDataEditorViewModel.class);


    }

    private void setObservers() {

        final Observer<ProductEntity> productObserver = productEntity -> {

            if (productEntity != null) {

                // Update ProductImageModel, ProductIdentityModel and ProductUserDataModel in
                // their respective view models
                productIdentityViewModel.getIdentityModel().setDescription(
                        productEntity.getDescription());

                productIdentityViewModel.getIdentityModel().setMadeBy(
                        productEntity.getMadeBy());

                productIdentityViewModel.getIdentityModel().setCategory(
                        productEntity.getCategory());

                productIdentityViewModel.getIdentityModel().setShelfLife(
                        productEntity.getShelfLife());

//                Log.d(TAG, "setObservers: Updating unit of measure to: " +
//                        productEntity.getUnitOfMeasureSubType());
//
//                productMeasurementViewModel.getMeasurementValidation().changeUnitOfMeasure(
//                        productEntity.getUnitOfMeasureSubType());
//
//                productMeasurementViewModel.getMeasurementValidation().numberOfItemsUpdated(
//                        productEntity.getNumberOfItems());
//
//                boolean baseSiUnitsAreSet = productMeasurementViewModel.getMeasurementValidation().setBaseSiUnits(
//                        productEntity.getBaseSiUnits());
//
//                if (baseSiUnitsAreSet) {
//                    Log.d(TAG, "setObservers: Base units set ok!");
//                }
            }
        };

        productEditorViewModel.getProductEntity().observe(this, productObserver);
    }
}
