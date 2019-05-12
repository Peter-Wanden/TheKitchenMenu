package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.utils.ProductMeasurementHandler;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;

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

    public MutableLiveData<ProductMeasurementModel> getMeasurementModel() {
        return measurementModel;
    }

    public ProductMeasurementHandler getMeasurementValidation() {
        return measurementValidation;
    }

    public ProductMeasurementModel getNewMeasurement() {
        return newMeasurement;
    }

    public void setNewMeasurement(ProductMeasurementModel newMeasurement) {
        this.newMeasurement = newMeasurement;
    }
}
