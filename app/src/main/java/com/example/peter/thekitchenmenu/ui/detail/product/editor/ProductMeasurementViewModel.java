package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private static final String TAG = "ProductMeasurementViewM";

    private MutableLiveData<ProductMeasurementModel> measurementModel = new MutableLiveData<>();

    // Whenever a newMeasurement is valid, set it to measurement model
    private ProductMeasurementModel newMeasurement;
    private ProductMeasurementHandler measurementValidation;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        newMeasurement = new ProductMeasurementModel();

        measurementValidation = new ProductMeasurementHandler(
                application,
                this);

    }

    MutableLiveData<ProductMeasurementModel> getMeasurementModel() {
        return measurementModel;
    }

    public ProductMeasurementHandler getMeasurementValidation() {
        return measurementValidation;
    }

    ProductMeasurementModel getNewMeasurement() {
        return newMeasurement;
    }

    public void setNewMeasurement(ProductMeasurementModel newMeasurement) {
        this.newMeasurement = newMeasurement;
    }
}
