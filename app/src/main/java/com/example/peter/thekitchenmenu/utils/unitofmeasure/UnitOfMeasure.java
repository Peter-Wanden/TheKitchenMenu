package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import java.util.List;

public interface UnitOfMeasure {

    String getTypeAsString();
    MeasurementType getType();
    String getSubTypeAsString();
    MeasurementSubType getSubType();

    List<MeasurementUnits> getUnits();
    String[] getUnitsAsString();

    Measurement getMinAndMax();

    double getBaseSiUnits();
    boolean setBaseSiUnits(double baseSiUnits);

    boolean setNumberOfItems(int numberOfItems);
    int getNumberOfItems();

    int getPackMeasurementOne();
    boolean setPackMeasurementOne(int packMeasurementOne);
    int getPackMeasurementTwo();
    boolean setPackMeasurementTwo(int packMeasurementTwo);
    int getItemMeasurementOne();
    boolean setItemMeasurementOne(int itemMeasurementOne);
    int getItemMeasurementTwo();
    boolean setItemMeasurementTwo(int itemMeasurementTwo);

    int[] getInputFilterFormat();
}