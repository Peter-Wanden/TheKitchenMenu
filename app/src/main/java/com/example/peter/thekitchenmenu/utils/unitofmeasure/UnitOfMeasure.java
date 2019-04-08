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
    boolean numberOfItemsAreSet(int numberOfItems);

    int getNumberOfMeasurementUnits();

    String getMeasurementUnitOneLabel();
    double getPackMeasurementOne();
    boolean packMeasurementOneIsSet(double packMeasurementOne);
    double getItemMeasurementOne();
    boolean itemMeasurementOneIsSet(double itemMeasurementOne);

    String getMeasurementUnitTwoLabel();
    int getPackMeasurementTwo();
    boolean packMeasurementTwoIsSet(int packMeasurementTwo);
    int getItemMeasurementTwo();
    boolean itemMeasurementTwoIsSet(int itemMeasurementTwo);

    String getMeasurementUnitThreeLabel();
    int getPackMeasurementThree();
    boolean packMeasurementThreeIsSet(int packMeasurementThree);
    int getItemMeasurementThree();
    boolean itemMeasurementThreeIsSet(int itemMeasurementThree);

    String[] getMeasurementError();

    Pair[] getInputDigitsFilter();

    void resetNumericValues();
}