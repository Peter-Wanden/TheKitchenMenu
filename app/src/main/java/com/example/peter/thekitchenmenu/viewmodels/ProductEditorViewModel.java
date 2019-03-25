package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();

    // TODO - Change category and shelf life to an enum

    public ProductEditorViewModel(@NonNull Application applicationContext) {
        super(applicationContext);

        ProductEntity productEntity = new ProductEntity(
                0,
                "Mars Bar",
                "Mars",
                1,
                5,
                5,
                5250,
                1,
                0,
                "",
                "",
                0,
                0,
                "");
        this.productEntity.setValue(productEntity);

    }

    public MutableLiveData<ProductEntity> getProductEntity() {

        if (productEntity == null) {

            productEntity = new MutableLiveData<>();
        }
        return productEntity;
    }

    // Changes the reference to a new productEntity, triggering LiveData to update the database.
    private void saveProductEntity() {

        // TODO - Check all validation bool's before saving ProductEntity
//        if (productEntity.getValue() != null) {
//
//            ProductEntity newProductEntity = new ProductEntity();
//
//            newProductEntity.setDescription(identityModel.getDescription());
//            newProductEntity.setMadeBy(identityModel.getMadeBy());
//            newProductEntity.setCategory(identityModel.getCategory());
//            newProductEntity.setShelfLife(identityModel.getShelfLife());
//
//            newProductEntity.setNumberOfItems(productModel.getNumberOfItems());
//            newProductEntity.setBaseSiUnits(productModel.getBaseSiUnits());
//            newProductEntity.setUnitOfMeasureSubType(productModel.getUnitOfMeasureSubType());
//            newProductEntity.setPackAvePrice(productModel.getPackAvePrice());
//            newProductEntity.setCreatedBy(productModel.getCreatedBy());
//            newProductEntity.setRemoteImageUri(productModel.getRemoteImageUri());
//            newProductEntity.setCreateDate(productModel.getCreateDate());
//            newProductEntity.setLastUpdate(productModel.getLastUpdate());
//            newProductEntity.setRemoteProductId(productModel.getRemoteProductId());
//
//            productEntity.setValue(newProductEntity);
//        }
    }
}