package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

class MetricMass extends UnitOfMeasureAbstract {

    MetricMass() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAXIMUM_MASS;
        minimumMeasurement = UnitOfMeasureConstants.MINIMUM_MASS;
        unitTwo = minimumMeasurement * 1000; // 1 kilogram
        unitOne = unitTwo / 1000; // 1 gram
        unitOneDecimal = 0; // no fractions of a gram
        smallestUnit = unitOne;

        subtype = MeasurementSubtype.METRIC_MASS;

        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_metric_mass;
        unitOneLabelStringResourceId = R.string.grams;
        unitTwoLabelStringResourceId = R.string.kilograms;
    }
}