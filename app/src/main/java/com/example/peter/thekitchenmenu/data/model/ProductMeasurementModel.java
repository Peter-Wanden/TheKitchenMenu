package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.google.firebase.database.annotations.NotNull;

public class ProductMeasurementModel {

    private static final String TAG = "tkm - MeasurementModel";

    private MeasurementSubType measurementSubType;
    private int numberOfItems = 1;
    private double baseSiUnits;

    public MeasurementSubType getMeasurementSubType() {
        return measurementSubType;
    }

    public void setMeasurementSubType(MeasurementSubType measurementSubType) {
        this.measurementSubType = measurementSubType;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    public void setBaseSiUnits(double baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
    }

    @NotNull
    @Override
    public String toString() {
        return "\ntkm-ProductMeasurementModel{" +
                "\nmeasurementSubType="         + measurementSubType +
                "\nnumberOfItems="              + numberOfItems +
                "\nbaseSiUnits="                + baseSiUnits +
                '}';
    }
}
