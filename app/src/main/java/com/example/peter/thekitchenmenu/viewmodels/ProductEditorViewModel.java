package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ProductImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.data.model.ProductUserDataModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private boolean isNewProduct = false; // Todo - control this bool with intent
    // bundle passed into activity.

    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();
    private MutableLiveData<ProductUserDataEntity> productUserDataEntity = new MutableLiveData<>();
    private MutableLiveData<Boolean> entitiesAreValid = new MutableLiveData<>(false);

    private ProductEntity newProductEntity = new ProductEntity();
    private ProductUserDataEntity newUserDataEntity = new ProductUserDataEntity();
    private ProductImageModel imageModel= new ProductImageModel();
    private ProductIdentityModel identityModel = new ProductIdentityModel();
    private ProductMeasurementModel measurementModel = new ProductMeasurementModel();
    private ProductUserDataModel userDataModel = new ProductUserDataModel();

    private String title;

    // TODO - Change category and shelf life to an enum

    public ProductEditorViewModel(@NonNull Application applicationContext) {
        super(applicationContext);

        title = applicationContext.getString(R.string.activity_title_edit_product);
        // TODO -

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
                "",
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
                "",
                "file:///sdcard/img.png",
                0,
                0
        );

//        this.productUserDataEntity.setValue(userDataEntity);
    }

    public void isNewProduct(boolean newProduct) {
        isNewProduct = newProduct;
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

    public MutableLiveData<Boolean> getEntitiesAreValid() {

        if (entitiesAreValid == null)
            entitiesAreValid = new MutableLiveData<>(false);

        return entitiesAreValid;
    }

    public void setEntitiesAreValid(MutableLiveData<Boolean> entitiesAreValid) {

        this.entitiesAreValid = entitiesAreValid;
    }

    public void setImageModel(ProductImageModel imageModel) {

        this.imageModel = imageModel;
        updateEntitiesWithImageModelData();
    }

    private void updateEntitiesWithImageModelData() {

        if (isNewProduct) {

            // If there's a local image and no remote image, which for a new product should always
            // be the case, save the
            if (imageModel.getRemoteImageUri().isEmpty() &&
                    !imageModel.getLocalImageUri().isEmpty())
                newProductEntity.setRemoteImageUri(imageModel.getLocalImageUri());

            if (imageModel.getRemoteImageThumbUri().isEmpty() &&
                    !imageModel.getLocalImageThumbUri().isEmpty()) {
                newProductEntity.setRemoteImageThumbUri(imageModel.getLocalImageThumbUri());
            }

            if (imageModel.getWebImageUrl().isEmpty())

            if (!imageModel.getLocalImageThumbUri().isEmpty()) {
                newUserDataEntity.setLocalImageThumbUri(imageModel.getLocalImageThumbUri());
            }

            if (!imageModel.getLocalImageUri().isEmpty()) {
                newUserDataEntity.setLocalImageUri(imageModel.getLocalImageUri());
            }
        }
    }

    public void setIdentityModel(ProductIdentityModel identityModel) {
        this.identityModel = identityModel;
    }

    public void setMeasurementModel(ProductMeasurementModel measurementModel) {
        this.measurementModel = measurementModel;
    }

    public void setUserDataModel(ProductUserDataModel userDataModel) {
        this.userDataModel = userDataModel;
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