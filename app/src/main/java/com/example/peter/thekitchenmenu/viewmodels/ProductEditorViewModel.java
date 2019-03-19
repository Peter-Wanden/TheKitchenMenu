package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.data.entity.Product;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;
import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.ProductTextValidationHandler;
import com.example.peter.thekitchenmenu.utils.ProductNumericValidationHandler;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.ObservableMeasurement;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureClassSelector;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_PACKS;

public class ProductEditorViewModel extends ObservableViewModel {

    private static final String TAG = "ProductEditorViewModel";

    private Context applicationContext;
    private ProductTextValidationHandler textValidationHandler;
    private ProductNumericValidationHandler numericValidationHandler;

    private MutableLiveData<Product> productEntity = new MutableLiveData<>();

    // TODO - Change category and shelf life to an enum

    // Field validation status
    private boolean descriptionValidated;
    private boolean madeByValidated;
    private boolean categoryValidated;
    private boolean shelfLifeValidated;
    private boolean numberOfItemsInPackValidated;
    private boolean packSizeValidated;

    // Measurements
    private ObservableProductModel productModel = new ObservableProductModel();
    private ObservableBoolean multiPack = new ObservableBoolean(false);
    private ObservableMeasurement measurement;

    public ProductEditorViewModel(@NonNull Application applicationContext) {
        super(applicationContext);
        this.applicationContext = applicationContext;


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

        if (this.productEntity.getValue() != null) {

            productModel.updateModelFromEntity(this.productEntity.getValue());

            if (isMultiPack()) {
                multiPack.set(true);
            }
        }

        measurement = new ObservableMeasurement();

        numericValidationHandler = new ProductNumericValidationHandler(
                applicationContext,
                this,
                measurement);

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

    public void setProductModel(ObservableProductModel productModel) {
        this.productModel = productModel;
    }

    private boolean isMultiPack() {
        return productModel.getNumberOfPacks() >= MULTI_PACK_MINIMUM_NO_OF_PACKS;
    }

    public ObservableMeasurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(ObservableMeasurement measurement) {
        Log.d(TAG, "setMeasurement: " + measurement.toString());
        this.measurement = measurement;
    }

    @Bindable
    public ObservableBoolean getMultiPack() {

        if (productModel != null) {
            Log.d(TAG, "getMultiPack: " + multiPack.get());
        }
        return multiPack;
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

    public void setNumberOfPacksInPackValidated(boolean numberOfItemsInPackValidated) {
        this.numberOfItemsInPackValidated = numberOfItemsInPackValidated;
    }

    // TODO - For dev only.

    private void printProduct() {
        if (productEntity.getValue() != null)
            Log.d(TAG, "printProduct: " + productEntity.getValue().toString());
    }

    // Changes the reference to the productEntity, triggering LiveData to update the database.
    private void saveProductEntity() {

        // TODO - Create a new product entity, Swap in the values from the model, set the new
        // TODO - entity over the old entity
        Product oldProduct = productEntity.getValue();
        Product newProduct = new Product();

        newProduct.setDescription(oldProduct.getDescription());
        newProduct.setMadeBy(oldProduct.getMadeBy());
        newProduct.setCategory(oldProduct.getCategory());
        newProduct.setNumberOfPacks(oldProduct.getNumberOfPacks());
        newProduct.setShelfLife(oldProduct.getShelfLife());
        newProduct.setBaseSiUnits(oldProduct.getBaseSiUnits());
        newProduct.setUnitOfMeasureSubType(oldProduct.getUnitOfMeasureSubType());
        newProduct.setPackAvePrice(oldProduct.getPackAvePrice());
        newProduct.setCreatedBy(oldProduct.getCreatedBy());
        newProduct.setRemoteImageUri(oldProduct.getRemoteImageUri());
        newProduct.setCreateDate(oldProduct.getCreateDate());
        newProduct.setLastUpdate(oldProduct.getLastUpdate());
        newProduct.setRemoteProductId(oldProduct.getRemoteProductId());

        productEntity.setValue(newProduct);
    }
}