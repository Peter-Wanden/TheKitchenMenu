package com.example.peter.thekitchenmenu.domain.unitofmeasureentities;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    int getTypeStringResourceId();

    MeasurementType getMeasurementType();
    MeasurementSubtype getMeasurementSubtype();

    boolean isConversionFactorEnabled();
    double getConversionFactor();
    boolean isConversionFactorSet(double conversionFactor);

    double getTotalBaseUnits();
    boolean isTotalBaseUnitsSet(double totalBaseUnits);

    double getItemBaseUnits();
    boolean isItemBaseUnitsSet(double itemBaseUnits);

    int getNumberOfItems();
    boolean isNumberOfItemsSet(int numberOfItems);

    int getNumberOfUnits();

    int getUnitOneLabelResourceId();
    double getTotalUnitOne();
    boolean isTotalUnitOneSet(double totalUnitOne);
    double getItemUnitOne();
    boolean isItemUnitOneSet(double itemUnitOne);

    int getUnitTwoLabelResourceId();
    int getTotalUnitTwo();
    boolean isTotalUnitTwoSet(int totalUnitTwo);
    int getItemUnitTwo();
    boolean isItemUnitTwoSet(int itemUnitTwo);

    boolean isValidMeasurement();
    double getMinUnitOneInBaseUnits();
    double getMaxUnitOne();
    int getMaxUnitTwo();
    Pair[] getMaxUnitDigitWidths();
}