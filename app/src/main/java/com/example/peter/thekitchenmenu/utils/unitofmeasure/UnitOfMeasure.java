package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.data.entity.Product;

public interface UnitOfMeasure {

    String getTypeAsString();
    MeasurementType getMeasurementType();
    MeasurementSubType getMeasurementSubType();

    String getMeasurementUnitOne();
    String getMeasurementUnitTwo();

    Measurement getMinAndMax();
    Measurement setNewMeasurementValuesTo(Measurement measurement);
    boolean setValuesFromProduct(Product product);

    double getBaseSiUnits();
    boolean setBaseSiUnits(double baseSiUnits);

    boolean setNumberOfItemsInPack(int numberOfItems);
    int getNumberOfItemsInPack();

    int getPackMeasurementOne();
    boolean setPackMeasurementOne(int packMeasurementOne);
    int getPackMeasurementTwo();
    boolean setPackMeasurementTwo(int packMeasurementTwo);
    int getItemMeasurementOne();
    boolean setItemMeasurementOne(int itemMeasurementOne);
    int getItemMeasurementTwo();
    boolean setItemMeasurementTwo(int itemMeasurementTwo);

    int[] getInputFilterFormat();

    void resetNumericValues();
}