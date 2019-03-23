package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.ProductTextValidationHandler;
import com.example.peter.thekitchenmenu.utils.ProductNumericValidationHandler;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.ObservableMeasurementModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private ProductTextValidationHandler textValidationHandler;
    private ProductNumericValidationHandler numericValidationHandler;

    private MutableLiveData<Product> productEntity = new MutableLiveData<>();
    private ObservableProductModel productModel = new ObservableProductModel();;

    // TODO - Change category and shelf life to an enum

    // Field validation status
    private boolean descriptionValidated;
    private boolean madeByValidated;
    private boolean categoryValidated;
    private boolean shelfLifeValidated;
    private boolean numberOfItemsInPackValidated;
    private boolean packSizeValidated;

    // Measurements
    private ObservableMeasurementModel measurement;

    public ProductEditorViewModel(@NonNull Application applicationContext) {
        super(applicationContext);

        Product productEntity = new Product(
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

        measurement = new ObservableMeasurementModel();

        numericValidationHandler = new ProductNumericValidationHandler(
                applicationContext,
                this);

        textValidationHandler = new ProductTextValidationHandler(
                applicationContext,
                this);
    }

    public ProductTextValidationHandler getTextValidationHandler() {
        return textValidationHandler;
    }

    public ProductNumericValidationHandler getNumericValidationHandler() {
        return numericValidationHandler;
    }

    public MutableLiveData<Product> getProductEntity() {

        if (productEntity == null) {

            productEntity = new MutableLiveData<>();
        }
        return productEntity;
    }

    public ObservableProductModel getProductModel() {
        return productModel;
    }

    public ObservableMeasurementModel getMeasurement() {
        return measurement;
    }

    public void setMeasurement(ObservableMeasurementModel measurement) {
        Log.d(TAG, "setMeasurement: " + measurement.toString());
        this.measurement = measurement;
    }

    public void setDescriptionValidated(boolean descriptionValidated) {
        this.descriptionValidated = descriptionValidated;
    }

    public void setMadeByValidated(boolean madeByValidated) {
        this.madeByValidated = madeByValidated;
    }

    public void setPackSizeValidated(boolean packSizeValidated) {
        this.packSizeValidated = packSizeValidated;
    }

    public void setNumberOfItemsInPackValidated(boolean numberOfItemsInPackValidated) {
        this.numberOfItemsInPackValidated = numberOfItemsInPackValidated;
    }

    // TODO - For dev only.

    private void printProduct() {
        if (productEntity.getValue() != null)
            Log.d(TAG, "printProduct: " + productEntity.getValue().toString());
    }

    // Changes the reference to a new productEntity, triggering LiveData to update the database.
    private void saveProductEntity() {

        // TODO - Check all validation bool's before saving ProductEntity
        if (productEntity.getValue() != null) {

            Product newProduct = new Product();

            newProduct.setDescription(productModel.getDescription());
            newProduct.setMadeBy(productModel.getMadeBy());
            newProduct.setCategory(productModel.getCategory());
            newProduct.setNumberOfItems(productModel.getNumberOfItems());
            newProduct.setShelfLife(productModel.getShelfLife());
            newProduct.setBaseSiUnits(productModel.getBaseSiUnits());
            newProduct.setUnitOfMeasureSubType(productModel.getUnitOfMeasureSubType());
            newProduct.setPackAvePrice(productModel.getPackAvePrice());
            newProduct.setCreatedBy(productModel.getCreatedBy());
            newProduct.setRemoteImageUri(productModel.getRemoteImageUri());
            newProduct.setCreateDate(productModel.getCreateDate());
            newProduct.setLastUpdate(productModel.getLastUpdate());
            newProduct.setRemoteProductId(productModel.getRemoteProductId());

            productEntity.setValue(newProduct);
        }
    }
}