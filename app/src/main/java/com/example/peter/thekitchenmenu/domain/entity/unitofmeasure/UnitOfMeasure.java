package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementType;

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
    double getMinUnitOne();
    double getMaxUnitOne();
    int getMaxUnitTwo();
    Pair[] getMaxUnitDigitWidths();
}