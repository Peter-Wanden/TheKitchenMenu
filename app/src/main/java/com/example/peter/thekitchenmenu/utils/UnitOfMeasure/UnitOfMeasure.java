package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

public interface UnitOfMeasure<T> {

    // Weight / volume / count
    String getTypeAsString();
    int getTypeAsInt();

    // Grams / ml / count etc.
    String getUnitAsString();
    int getUnitAsInt();

    // Decimal to cardinal
    int convertToInt(UnitOfMeasure newUnitOfMeasure, String value);

    double convertToDouble(UnitOfMeasure newUnitOfMeasure, String value);

    int convertToBaseUnit(double value);

    int convertToBaseUnit(int value);

}
