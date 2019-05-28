package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    int getTypeStringResourceId();

    MeasurementSubtype getMeasurementSubtype();

    double getBaseUnits();
    boolean baseUnitsAreSet(double baseSiUnits);

    int getNumberOfProducts();
    boolean numberOfProductsIsSet(int numberOfItems);

    int getNumberOfMeasurementUnits();

    int getUnitOneLabelStringResourceId();
    double getPackMeasurementOne();
    boolean packMeasurementOneIsSet(double packMeasurementOne);
    double getProductMeasurementOne();
    boolean productMeasurementOneIsSet(double productMeasurementOne);

    int getUnitTwoLabelStringResourceId();
    int getPackMeasurementTwo();
    boolean packMeasurementTwoIsSet(int packMeasurementTwo);
    int getProductMeasurementTwo();
    boolean productMeasurementTwoIsSet(int productMeasurementTwo);

    boolean isValidMeasurement();
    Pair[] getMeasurementUnitNumberTypeArray();
}