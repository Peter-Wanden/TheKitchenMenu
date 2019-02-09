package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

public interface UnitOfMeasure {

    String getTypeAsString();

    int getTypeAsInt();

    String getUnitAsString();

    int getUnitAsInt();

    void setMeasurement(double measurement);

    double getMeasurement();

    void setBaseSiUnits(int baseSiUnits);

    int getBaseSiUnits();

    int convertToBaseSiUnits(double measurement);

    double getMax();
}