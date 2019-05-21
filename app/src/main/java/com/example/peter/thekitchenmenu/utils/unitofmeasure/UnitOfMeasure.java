package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    int getTypeStringResourceId();

    MeasurementSubType getMeasurementSubType();

    double getBaseSiUnits();
    boolean baseSiUnitsAreSet(double baseSiUnits);

    int getNumberOfItems();
    boolean numberOfItemsAreSet(int numberOfItems);

    int getNumberOfMeasurementUnits();

    int getUnitOneLabelStringResourceId();
    double getPackMeasurementOne();
    boolean packMeasurementOneIsSet(double packMeasurementOne);
    double getItemMeasurementOne();
    boolean itemMeasurementOneIsSet(double itemMeasurementOne);

    int getUnitTwoLabelStringResourceId();
    int getPackMeasurementTwo();
    boolean packMeasurementTwoIsSet(int packMeasurementTwo);
    int getItemMeasurementTwo();
    boolean itemMeasurementTwoIsSet(int itemMeasurementTwo);

    boolean isValidMeasurement();
    Pair[] getInputDigitsFilter();
}