package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.utils.ProductMeasurementHandler;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;

import androidx.annotation.NonNull;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private ProductMeasurementModel measurement;
    private ProductMeasurementHandler measurementValidation;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        measurement = new ProductMeasurementModel();

        measurementValidation = new ProductMeasurementHandler(
                application,
                this);

    }

    public ProductMeasurementHandler getMeasurementValidation() {
        return measurementValidation;
    }

    public ProductMeasurementModel getMeasurement() {
        return measurement;
    }
}
