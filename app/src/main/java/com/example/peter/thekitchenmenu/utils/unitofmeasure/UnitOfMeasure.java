package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    int getTypeStringResourceId();
    MeasurementType getMeasurementType();

    int getSubTypeStringResourceId();
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

    int getUnitThreeLabelStringResourceId();
    int getPackMeasurementThree();
    boolean packMeasurementThreeIsSet(int packMeasurementThree);
    int getItemMeasurementThree();
    boolean itemMeasurementThreeIsSet(int itemMeasurementThree);

    int[] getMeasurementError();

    Pair[] getInputDigitsFilter();
}