package com.example.peter.thekitchenmenu.utils.unitofmeasure;

public interface UnitOfMeasure {

    String getTypeAsString();
    MeasurementType getMeasurementType();
    MeasurementSubType getMeasurementSubType();

    int getNumberOfMeasurementUnits();
    String getMeasurementUnitOne();
    String getMeasurementUnitTwo();

    ObservableMeasurementModel getMinAndMax();

    double getBaseSiUnits();
    boolean baseSiUnitsAreSet(double baseSiUnits);

    boolean setNumberOfItems(int numberOfItems);
    int getNumberOfItems();

    int getPackMeasurementOne();
    boolean setPackMeasurementOne(int packMeasurementOne);
    int getPackMeasurementTwo();
    boolean setPackMeasurementTwo(int packMeasurementTwo);
    int getPackMeasurementThree();
    boolean setPackMeasurementThree(int packMeasurementThree);
    int getItemMeasurementOne();
    boolean setItemMeasurementOne(int itemMeasurementOne);
    int getItemMeasurementTwo();
    boolean setItemMeasurementTwo(int itemMeasurementTwo);
    int getItemMeasurementThree();
    boolean setItemMeasurementThree(int itemMeasurementThree);

    int[] getInputFilterFormat();

    void resetNumericValues();
}