package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.google.firebase.database.annotations.NotNull;

public class ProductMeasurementModel {

    private static final String TAG = "tkm - MeasurementModel";

    private MeasurementSubtype measurementSubtype;
    private int numberOfItems = 1;
    private double baseSiUnits;

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
                "\nmeasurementSubtype="         + measurementSubtype +
                "\nnumberOfItems="              + numberOfItems +
                "\nbaseSiUnits="                + baseSiUnits +
                '}';
    }
}
