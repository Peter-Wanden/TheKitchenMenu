package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    String getMeasurementTypeAsString();
    MeasurementType getMeasurementType();

    String getMeasurementSubTypeAsString();
    MeasurementSubType getMeasurementSubType();

    double getBaseSiUnits();
    boolean baseSiUnitsAreSet(double baseSiUnits);

    int getNumberOfItems();
    boolean setNumberOfItems(int numberOfItems);

    int getNumberOfMeasurementUnits();

    String getMeasurementUnitOneLabel();
    double getPackMeasurementOne();
    boolean setPackMeasurementOne(double packMeasurementOne);
    double getItemMeasurementOne();
    boolean setItemMeasurementOne(double itemMeasurementOne);

    String getMeasurementUnitTwoLabel();
    int getPackMeasurementTwo();
    boolean setPackMeasurementTwo(int packMeasurementTwo);
    int getItemMeasurementTwo();
    boolean setItemMeasurementTwo(int itemMeasurementTwo);

    String getMeasurementUnitThreeLabel();
    int getPackMeasurementThree();
    boolean setPackMeasurementThree(int packMeasurementThree);
    int getItemMeasurementThree();
    boolean setItemMeasurementThree(int itemMeasurementThree);

    String[] getMeasurementError();

    Pair[] getInputDigitsFilter();

    void resetNumericValues();
}