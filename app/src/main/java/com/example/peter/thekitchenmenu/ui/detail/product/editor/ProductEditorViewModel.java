package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
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
    private ProductMeasurementModel measurementModelOut = new ProductMeasurementModel();

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
//        Log.d(TAG, "setUpdatedImageModel:" + updatedImageModel.toString());
        // TODO Save images to remote database
        //
    }

    void setUpdatedIdentityModel(ProductIdentityModel updatedIdentityModel) {
        this.updatedIdentityModel = updatedIdentityModel;
//        Log.d(TAG, "setEditedIdentityModel:" + updatedIdentityModel.toString());
    }

    void setMeasurementModelOut(ProductMeasurementModel measurementModelOut) {
        this.measurementModelOut = measurementModelOut;
//        Log.d(TAG, "setMeasurementModelOut:" + measurementModelOut.toString());
    }

    public void onFabClick() {

    }
}