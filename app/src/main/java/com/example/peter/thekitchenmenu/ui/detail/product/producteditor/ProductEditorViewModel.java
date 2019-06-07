package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.data.repository.ProductRepository;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-EditorViewModel";

    private String activityTitle; // "Add new product" or "Edit product"

    private ProductRepository repository;
    // From repo, or empty if new product. Set an updatedProductEntity here to update repo
    private MutableLiveData<ProductEntity> existingProductEntity = new MutableLiveData<>();
    private SingleLiveEvent<Void> editingComplete = new SingleLiveEvent<>();

    // Populated as new data is set from models. Once complete post to existingProductEntity
    private ProductEntity updatedProductEntity;

    // The various model updates that make up an updated ProductEntity
    private ImageModel updatedImageModel = new ImageModel();
    private ProductIdentityModel updatedIdentityModel = new ProductIdentityModel();
    private ProductMeasurementModel updatedMeasurementModel = new ProductMeasurementModel();

    private boolean
            imageModelIsValid = false,
            identityModelIsValid = false,
            measurementModelIsValid = false;

    @Bindable
    public boolean allModelsAreValid = false;

    // TODO - Change category and shelf life to an enum

    public ProductEditorViewModel(@NonNull Application application, ProductRepository repository) {
        super(application);
        this.repository = repository;

        activityTitle = application.getString(R.string.activity_title_edit_product);

        ProductEntity productEntityMetricMassTest = new ProductEntity(
                "Heinz Baked Beanz, 4 pack",
                "Baked beans",
                1,
                12,
                4,
                1660,
                0,
                "",
                "https://d25hqtnqp5nl24.cloudfront.net/images/products/4/LN_019030_BP_4.jpg",
                "",
                "",
                "");

        ProductEntity productEntityImperialMassTest = new ProductEntity(
                "Cadbury Wispa Chocolate Multipack 4 x 30g",
                "Cadbury",
                1,
                9,
                4,
                120,
                0,
                "",
                "https://img.tesco.com/Groceries/pi/372/7622210255372/IDShot_540x540.jpg",
                "",
                "",
                "");

        ProductEntity productEntityImperialVolumeTest = new ProductEntity(
                "Domestos Spray Multi-Purpose Bleach",
                "Domestos",
                0,
                1,
                1,
                700,
                2,
                "",
                "",
                "https://assets.iceland.co.uk/i/iceland/Domestos_700m_Spray_Bleach_73591.jpg",
                "",
                "");

        this.existingProductEntity.setValue(productEntityMetricMassTest);
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    SingleLiveEvent<Void> getEditingCompleteEvent() {
        return editingComplete;
    }

    MutableLiveData<ProductEntity> getExistingProductEntity() {
        if (existingProductEntity == null) existingProductEntity = new MutableLiveData<>();
        return existingProductEntity;
    }

    void setUpdatedImageModel(ImageModel updatedImageModel) {
        this.updatedImageModel = updatedImageModel;
        imageModelIsValid = true;
        allProductModelsAreValid();
        // TODO Save images to remote database
    }

    void setUpdatedIdentityModel(ProductIdentityModel updatedIdentityModel) {
        this.updatedIdentityModel = updatedIdentityModel;
        identityModelIsValid = true;
        allProductModelsAreValid();
    }

    void setUpdatedMeasurementModel(ProductMeasurementModel updatedMeasurementModel) {
        this.updatedMeasurementModel = updatedMeasurementModel;
        measurementModelIsValid = true;
        allProductModelsAreValid();
    }

    private void allProductModelsAreValid() {
        if (imageModelIsValid && identityModelIsValid && measurementModelIsValid) {
            allModelsAreValid = true;
            notifyPropertyChanged(BR.allModelsAreValid);
        }
    }

    void onFabClick() {
        saveProduct();
    }

    private void saveProduct() {

        updatedProductEntity = new ProductEntity(
                updatedIdentityModel.getDescription(),
                updatedIdentityModel.getShoppingListItemName(),
                updatedIdentityModel.getCategory(),
                updatedIdentityModel.getShelfLife(),
                updatedMeasurementModel.getNumberOfProducts(),
                updatedMeasurementModel.getBaseUnits(),
                updatedMeasurementModel.getMeasurementSubtype().ordinal(),
                Constants.getUserId().getValue(),
                updatedImageModel.getWebImageUrl(),
                updatedImageModel.getRemoteSmallImageUri(),
                updatedImageModel.getRemoteMediumImageUri(),
                updatedImageModel.getLocalLargeImageUri());

        Log.d(TAG, "saveProduct: updated Product entity=" + updatedProductEntity.toString());
        createNewProduct(updatedProductEntity);
    }

    private void createNewProduct(ProductEntity product) {
        repository.saveProduct(product);
        editingComplete.call();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // TODO - Abort all network operations, unsubscribe observers and drop callbacks.
    }
}