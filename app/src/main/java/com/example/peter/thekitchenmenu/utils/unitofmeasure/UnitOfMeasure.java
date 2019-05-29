package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    int getTypeStringResourceId();

    MeasurementSubtype getMeasurementSubtype();

    double getBaseUnits();
    boolean baseUnitsAreSet(double baseSiUnits);

    int getNumberOfProducts();
    boolean numberOfProductsIsSet(int numberOfProducts);

    int getNumberOfMeasurementUnits();

    int getUnitOneLabelStringResourceId();
    double getPackMeasurementOne();
    boolean packMeasurementOneIsSet(double newPackMeasurementOne);
    double getProductMeasurementOne();
    boolean productMeasurementOneIsSet(double newProductMeasurementOne);

    int getUnitTwoLabelStringResourceId();
    int getPackMeasurementTwo();
    boolean packMeasurementTwoIsSet(int newPackMeasurementTwo);
    int getProductMeasurementTwo();
    boolean productMeasurementTwoIsSet(int newProductMeasurementTwo);

    boolean isValidMeasurement();
    Pair[] getMeasurementUnitDigitLengthArray();
}