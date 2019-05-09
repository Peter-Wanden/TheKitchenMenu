package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.data.model.ProductUserDataModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private String title; // Add product, Edit product

    // From repo, or empty if new product. Posts updates back to repo when replaced by newProductEntity
    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();

    // Populated as new data is set from models. Once complete is posted to ProductEntity
    private ProductEntity newProductEntity = new ProductEntity();

    // Model updates
    private ImageModel updatedImageModel = new ImageModel();
    private ProductIdentityModel updatedIdentityModel = new ProductIdentityModel();
    private ProductMeasurementModel updatedMeasurementModel = new ProductMeasurementModel();

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
                "",
                "https://d1ycl3zewbvuig.cloudfront.net/images/products/11/LN_759450_BP_11.jpg",
                0,
                0,
                "");

        this.productEntity.setValue(productEntityImperialMassTest);
    }

    public String getTitle() {

        return title;
    }

    public MutableLiveData<ProductEntity> getProductEntity() {

        if (productEntity == null) productEntity = new MutableLiveData<>();
        return productEntity;
    }

    public void setUpdatedImageModel(ImageModel updatedImageModel) {
        this.updatedImageModel = updatedImageModel;
        Log.d(TAG, "tkm - setUpdatedImageModel: Image model updated");
        // TODO - Save images to remote database
    }

    public void setUpdatedIdentityModel(ProductIdentityModel updatedIdentityModel) {
        this.updatedIdentityModel = updatedIdentityModel;
        Log.d(TAG, "tkm - setUpdatedIdentityModel: Identity model updated");
    }

    public void setUpdatedMeasurementModel(ProductMeasurementModel updatedMeasurementModel) {
        this.updatedMeasurementModel = updatedMeasurementModel;
        Log.d(TAG, "setUpdatedMeasurementModel: Measurement model updated");
    }

    public void onFabClick() {

    }
}