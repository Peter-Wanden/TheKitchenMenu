package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;

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
    int getPackMeasurementOne();
    boolean setPackMeasurementOne(int packMeasurementOne);
    int getItemMeasurementOne();
    boolean setItemMeasurementOne(int itemMeasurementOne);

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

    ProductMeasurementModel getMinAndMax();

    int[] getInputFilterFormat();

    void resetNumericValues();
}