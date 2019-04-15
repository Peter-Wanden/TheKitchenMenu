package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();
    private String title;

    // TODO - Change category and shelf life to an enum

    public ProductEditorViewModel(@NonNull Application applicationContext) {
        super(applicationContext);

        title = applicationContext.getString(R.string.activity_title_edit_product);

        ProductEntity productEntityMetricMassTest = new ProductEntity(
                0,
                "Baked Beans",
                "Heinz",
                1,
                3,
                2,
                2500,
                0,
                0,
                "",
                "",
                0,
                0,
                "");

        ProductEntity productEntityImperialMassTest = new ProductEntity(
                0,
                "Cheez",
                "The Cheez Man",
                1,
                3,
                3,
                2834.95,
                1,
                0,
                "",
                "",
                0,
                0,
                "");

        this.productEntity.setValue(productEntityImperialMassTest);

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
//            newProductEntity.numberOfItemsAreSet(productModel.getNumberOfItems());
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

    public void onFabClick() {

    }

    public String getTitle() {
        return title;
    }
}