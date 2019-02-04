package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

public interface UnitOfMeasure {

    // weight / volume / count
    String getTypeAsString();
    int getTypeAsInt();

    // grams / ml / count etc.
    String getUnitAsString();
    int getUnitAsInt();

    // Screen value as String to base unit
    Integer convertValueToBaseSiUnit(String value);

    // Number type (cardinal / decimal)
    int getInputNumberType();
}
