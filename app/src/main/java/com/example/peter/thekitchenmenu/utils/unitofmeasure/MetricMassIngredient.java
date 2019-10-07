package com.example.peter.thekitchenmenu.utils.unitofmeasure;

class MetricMassIngredient extends UnitOfMeasureIngredient {

    MetricMassIngredient() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAXIMUM_MASS;
        minimumMeasurement = UnitOfMeasureConstants.MINIMUM_MASS;
        unitTwo = minimumMeasurement * 1000; // 1 kilogram
        unitOne = unitTwo / 1000; // 1 gram
        unitOneDecimal = 0; // no fractions of a gram
        smallestUnit = unitOne;

        subtype = MeasurementSubtype.METRIC_MASS;
    }
}
