package com.example.peter.thekitchenmenu.viewmodels;

import android.app.Application;

import com.example.peter.thekitchenmenu.utils.ObservableViewModel;
import com.example.peter.thekitchenmenu.utils.ProductSizeValidationHandler;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementModel;

import androidx.annotation.NonNull;

public class ProductSizeViewModel extends ObservableViewModel {

    private ProductSizeValidationHandler sizeValidationHandler;
    private MeasurementModel measurement;

    public ProductSizeViewModel(@NonNull Application application) {
        super(application);

        sizeValidationHandler = new ProductSizeValidationHandler(
                application,
                this);

        measurement = new MeasurementModel();
    }

    public ProductSizeValidationHandler getSizeValidationHandler() {
        return sizeValidationHandler;
    }

    public MeasurementModel getMeasurement() {
        return measurement;
    }
}
