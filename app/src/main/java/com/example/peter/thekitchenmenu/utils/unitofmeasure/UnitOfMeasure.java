package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    int getTypeStringResourceId();
    MeasurementType getMeasurementType();

    MeasurementSubType getMeasurementSubType();
    int getSubTypeStringResourceId();

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

    int[] getMeasurementError();

    Pair[] getInputDigitsFilter();
}