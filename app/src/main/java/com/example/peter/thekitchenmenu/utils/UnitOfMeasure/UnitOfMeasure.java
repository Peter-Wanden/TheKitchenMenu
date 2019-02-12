package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import androidx.core.util.Pair;

public interface UnitOfMeasure {

    // Returns weight, volume or count.
    String getTypeAsString();

    MeasurementType getType();

    String getUnitsAsString();

    int getUnitAsInt();

    void setMeasurement(double measurement);

    double getMeasurement();

    void setBaseSiUnits(int baseSiUnits);

    int getBaseSiUnits();

    int convertToBaseSiUnits(double measurement);

    double getMax();

    // TODO - Returns numbers before and after decimal places.
    // TODO - make a calculation within UoM class based on MAX_VALUE
    Pair<Integer, Integer> getInputFilterFormat();
}