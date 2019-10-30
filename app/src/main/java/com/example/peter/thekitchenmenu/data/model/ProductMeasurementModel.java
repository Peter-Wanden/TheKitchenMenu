package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;

public class ProductMeasurementModel {

    private static final String TAG = "tkm - MeasurementModel";

    private MeasurementSubtype measurementSubtype;
    private int numberOfProducts = 1;
    private double baseUnits;

    public ProductMeasurementModel(){}

    public ProductMeasurementModel(MeasurementSubtype measurementSubtype,
                                   int numberOfProducts,
                                   double baseUnits) {
        this.measurementSubtype = measurementSubtype;
        this.numberOfProducts = numberOfProducts;
        this.baseUnits = baseUnits;
    }

    public MeasurementSubtype getMeasurementSubtype() {
        return measurementSubtype;
    }

    public void setMeasurementSubtype(MeasurementSubtype measurementSubtype) {
        this.measurementSubtype = measurementSubtype;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public double getBaseUnits() {
        return baseUnits;
    }

    public void setBaseUnits(double baseUnits) {
        this.baseUnits = baseUnits;
    }

    @NonNull
    @Override
    public String toString() {
        return "\ntkm-ProductMeasurementModel{" +
                "\nmeasurementSubtype="         + measurementSubtype +
                "\nnumberOfProducts="           + numberOfProducts +
                "\nbaseUnits="                  + baseUnits +
                '}';
    }
}
