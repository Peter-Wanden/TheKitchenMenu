package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;

public class ProductMeasurementModel {

    private static final String TAG = "tkm - MeasurementModel";

    private MeasurementSubtype measurementSubtype;
    private int numberOfItems = 1;
    private double baseUnits;

    public ProductMeasurementModel(){}

    public ProductMeasurementModel(MeasurementSubtype measurementSubtype,
                                   int numberOfItems,
                                   double baseUnits) {
        this.measurementSubtype = measurementSubtype;
        this.numberOfItems = numberOfItems;
        this.baseUnits = baseUnits;
    }

    public MeasurementSubtype getMeasurementSubtype() {
        return measurementSubtype;
    }

    public void setMeasurementSubtype(MeasurementSubtype measurementSubtype) {
        this.measurementSubtype = measurementSubtype;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
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
                "\nnumberOfItems="           + numberOfItems +
                "\nbaseUnits="                  + baseUnits +
                '}';
    }
}
