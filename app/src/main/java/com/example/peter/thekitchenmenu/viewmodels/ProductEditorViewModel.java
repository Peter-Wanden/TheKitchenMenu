package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();
    private MutableLiveData<ProductUserDataEntity> productUserDataEntity = new MutableLiveData<>();

    private MutableLiveData<Boolean> allProductDataValid = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> productImageModelValid = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> productIdentityModelValid = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> productMeasurementModelValid = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> allUserDataValid = new MutableLiveData<>(false);

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
                10,
                10000,
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
                10,
                4819.41893125,
                1,
                0,
                "",
                "",
                0,
                0,
                "");

        ProductEntity productEntityImperialVolumeTest = new ProductEntity(
                0,
                "Semi-Skimmed Milk",
                "Waitrose",
                1,
                3,
                2,
                10000,
                3,
                0,
                "",
                "https://d1ycl3zewbvuig.cloudfront.net/images/products/11/LN_759450_BP_11.jpg",
                0,
                0,
                "");

        this.productEntity.setValue(productEntityImperialMassTest);

        ProductUserDataEntity userDataEntity = new ProductUserDataEntity(
                0,
                0,
                "0",
                "0",
                "Waitrose",
                "Kitchen",
                "Fridge",
                12.34,
                "file:///sdcard/img.png",
                0,
                0
        );

//        this.productUserDataEntity.setValue(userDataEntity);
    }

    public MutableLiveData<ProductEntity> getProductEntity() {

        if (productEntity == null) productEntity = new MutableLiveData<>();
        return productEntity;
    }

    public MutableLiveData<ProductUserDataEntity> getProductUserDataEntity() {

        if (productUserDataEntity == null)
            productUserDataEntity = new MutableLiveData<>();

        return productUserDataEntity;
    }

    public MutableLiveData<Boolean> getAllProductDataValid() {

        if (allProductDataValid == null)
            allProductDataValid = new MutableLiveData<>(false);

        return allProductDataValid;
    }

    public MutableLiveData<Boolean> getProductImageModelValid() {

        if (productImageModelValid == null)
            productImageModelValid = new MutableLiveData<>(false);

        return productImageModelValid;
    }

    public MutableLiveData<Boolean> getProductIdentityModelValid() {

        if (productIdentityModelValid == null)
            productIdentityModelValid = new MutableLiveData<>(false);

        return productIdentityModelValid;
    }

    public MutableLiveData<Boolean> getProductMeasurementModelValid() {

        if (productMeasurementModelValid == null)
            productMeasurementModelValid = new MutableLiveData<>(false);

        return productMeasurementModelValid;
    }

    public MutableLiveData<Boolean> getAllUserDataValid() {

        if (allProductDataValid == null)
            allProductDataValid = new MutableLiveData<>(false);

        return allUserDataValid;
    }

    // Changes the reference to a new productEntity, triggering LiveData to update the database.
    private void saveProductEntity() {

        // TODO - Check all validation bool's before saving ProductEntity
        // TODO - Image - If local image copy from temp location to local storage, save Uri
        //  and delete image in temp location
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
//            newProductEntity.setProductCreateDate(productModel.getProductCreateDate());
//            newProductEntity.setProductLastUpdate(productModel.getProductLastUpdate());
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