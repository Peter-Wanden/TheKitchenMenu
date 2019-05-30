package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-EditorViewModel";

    private String title; // Add product, Edit product

    // From repo, or empty if new product. Set an updatedProductEntity here to update repo
    private MutableLiveData<ProductEntity> existingProductEntity = new MutableLiveData<>();

    // Populated as new data is set from models. Once complete post to existingProductEntity
    private ProductEntity updatedProductEntity = new ProductEntity();

    // The various model updates that make up an updated ProductEntity
    private ImageModel updatedImageModel = new ImageModel();
    private ProductIdentityModel updatedIdentityModel = new ProductIdentityModel();
    private ProductMeasurementModel measurementModel = new ProductMeasurementModel();

    private boolean
            imageModelValid = false,
    identityModelValid = false,
    measurementModelValid = false;

    @Bindable
    public boolean allModelsValid = false;

    // TODO - Change category and shelf life to an enum

    public ProductEditorViewModel(@NonNull Application applicationContext) {
        super(applicationContext);

        title = applicationContext.getString(R.string.activity_title_edit_product);

        ProductEntity productEntityMetricMassTest = new ProductEntity(
                0,
                "Heinz Baked Beanz, 4 pack",
                "Baked beans",
                1,
                12,
                4,
                1660,
                0,
                0,
                "",
                "https://d25hqtnqp5nl24.cloudfront.net/images/products/4/LN_019030_BP_4.jpg",
                "",
                "",
                "",
                0,
                0,
                "");

        ProductEntity productEntityImperialMassTest = new ProductEntity(
                0,
                "Cadbury Wispa Chocolate Multipack 4 x 30g",
                "Cadbury",
                1,
                9,
                4,
                120,
                1,
                0,
                "",
                "https://img.tesco.com/Groceries/pi/372/7622210255372/IDShot_540x540.jpg",
                "",
                "",
                "",
                0,
                0,
                "");

        ProductEntity productEntityImperialVolumeTest = new ProductEntity(
                0,
                "Domestos Spray Multi-Purpose Bleach",
                "Domestos",
                0,
                1,
                1,
                700,
                2,
                0,
                "",
                "",
                "https://assets.iceland.co.uk/i/iceland/Domestos_700m_Spray_Bleach_73591.jpg",
                "",
                "",
                0,
                0,
                "");

        ProductEntity productEntityNoValues = new ProductEntity();

        this.existingProductEntity.setValue(productEntityMetricMassTest);
    }

    public String getTitle() {
        return title;
    }

    MutableLiveData<ProductEntity> getExistingProductEntity() {
        if (existingProductEntity == null) existingProductEntity = new MutableLiveData<>();
        return existingProductEntity;
    }

    void setUpdatedImageModel(ImageModel updatedImageModel) {
        this.updatedImageModel = updatedImageModel;
        imageModelValid = true;
        allProductModelsAreValid();
        // TODO Save images to remote database
    }

    void setUpdatedIdentityModel(ProductIdentityModel updatedIdentityModel) {
        this.updatedIdentityModel = updatedIdentityModel;
        identityModelValid = true;
        allProductModelsAreValid();
    }

    void setMeasurementModel(ProductMeasurementModel measurementModel) {
        this.measurementModel = measurementModel;
        measurementModelValid = true;
        allProductModelsAreValid();
    }

    private void allProductModelsAreValid() {
        if (imageModelValid && identityModelValid && measurementModelValid) {
            allModelsValid = true;
            notifyPropertyChanged(BR.allModelsValid);
        }
    }

    public void onFabClick() {
        saveProduct();
    }

    private void saveProduct() {

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // TODO - Abort all network operations, unsubscribe observers and drop callbacks.
    }
}