package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

public interface UnitOfMeasure {

    String getTypeAsString();
    MeasurementType getMeasurementType();
    MeasurementSubType getMeasurementSubType();

    String getMeasurementUnitOne();
    String getMeasurementUnitTwo();

    ObservableMeasurement getMinAndMax();

    void setNewMeasurementValuesTo(ObservableMeasurement observableMeasurement);
    boolean getValuesFromObservableProductModel(ObservableProductModel productModel);

    double getBaseSiUnits();
    boolean setBaseSiUnits(double baseSiUnits);

    boolean setNumberOfPacksInPack(int numberOfItems);
    int getNumberOfPacksInPack();

    int getPackMeasurementOne();
    boolean setPackMeasurementOne(int packMeasurementOne);
    int getPackMeasurementTwo();
    boolean setPackMeasurementTwo(int packMeasurementTwo);
    int getSinglePackMeasurementOne();
    boolean setSinglePackMeasurementOne(int itemMeasurementOne);
    int getSinglePackMeasurementTwo();
    boolean setSinglePackMeasurementTwo(int itemMeasurementTwo);

    int[] getInputFilterFormat();

    void resetNumericValues();
}