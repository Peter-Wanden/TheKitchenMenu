package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.utils.ProductMeasurementHandler;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private ProductMeasurementModel measurement;
    private ProductMeasurementHandler measurementValidation;
    private MutableLiveData<Boolean> measurementIsValid = new MutableLiveData<>(false);

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

    public MutableLiveData<Boolean> getMeasurementIsValid() {
        return measurementIsValid;
    }
}
