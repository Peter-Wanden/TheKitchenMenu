package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.entity.ProductUserDataEntity;
import com.example.peter.thekitchenmenu.data.model.ProductIdentityModel;
import com.example.peter.thekitchenmenu.data.model.ImageModel;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.data.model.ProductUserDataModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    // From repo, or empty if new product. Posts updates back to repo when replaced by newProductEntity
    private MutableLiveData<ProductEntity> productEntity = new MutableLiveData<>();

    // Populated as new data is set from models. Once complete is posted to ProductEntity
    private ProductEntity newProductEntity = new ProductEntity();

    // Updated by image, identity and measurement view models, observed by this view model
    private MutableLiveData<ImageModel> imageModel = new MutableLiveData<>();
    private MutableLiveData<ProductIdentityModel> identityModel = new MutableLiveData<>();
    private MutableLiveData<ProductMeasurementModel> measurementModel = new MutableLiveData<>();

    private String title; // Add product, Edit product

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

    public MutableLiveData<ProductEntity> getProductEntity() {

        if (productEntity == null) productEntity = new MutableLiveData<>();
        return productEntity;
    }

    public MutableLiveData<ImageModel> getImageModel() {

        if (imageModel == null) imageModel = new MutableLiveData<>();
        return imageModel;
    }

    public MutableLiveData<ProductIdentityModel> getIdentityModel() {

        if (identityModel == null) identityModel = new MutableLiveData<>();
        return identityModel;
    }

    public MutableLiveData<ProductMeasurementModel> getMeasurementModel() {

        if (measurementModel == null) measurementModel = new MutableLiveData<>();
        return measurementModel;
    }

    public void onFabClick() {

    }

    public String getTitle() {
        return title;
    }
}