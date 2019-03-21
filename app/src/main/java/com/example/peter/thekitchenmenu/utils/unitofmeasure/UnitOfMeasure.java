package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

public interface UnitOfMeasure {

    String getTypeAsString();
    MeasurementType getMeasurementType();
    MeasurementSubType getMeasurementSubType();

    String getMeasurementUnitOne();
    String getMeasurementUnitTwo();

    ObservableMeasurementModel getMinAndMax();

    void setNewMeasurementValuesTo(ObservableMeasurementModel observableMeasurementModel);
    boolean getValuesFromObservableProductModel(ObservableProductModel productModel);

    double getBaseSiUnits();
    boolean baseSiUnitsAreSet(double baseSiUnits);

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

    void resetNumericValues();
}