package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    int getTypeStringResourceId();

    MeasurementSubtype getMeasurementSubtype();

    double getConversionFactor();
    boolean conversionFactorIsSet(double conversionFactor);

    double getTotalBaseUnits();
    boolean totalBaseUnitsAreSet(double totalBaseUnits);

    double getItemBaseUnits();
    boolean itemBaseUnitsAreSet(double itemBaseUnits);

    int getNumberOfItems();
    boolean numberOfItemsIsSet(int numberOfItems);

    int getNumberOfMeasurementUnits();

    int getUnitOneLabelResourceId();
    double getTotalMeasurementOne();
    boolean totalMeasurementOneIsSet(double newTotalMeasurementOne);
    double getItemMeasurementOne();
    boolean itemMeasurementOneIsSet(double newItemMeasurementOne);

    int getUnitTwoLabelResourceId();
    int getTotalMeasurementTwo();
    boolean totalMeasurementTwoIsSet(int newTotalMeasurementTwo);
    int getItemMeasurementTwo();
    boolean itemMeasurementTwoIsSet(int newItemMeasurementTwo);

    boolean isValidMeasurement();
    Pair[] getMeasurementUnitsDigitWidths();
}