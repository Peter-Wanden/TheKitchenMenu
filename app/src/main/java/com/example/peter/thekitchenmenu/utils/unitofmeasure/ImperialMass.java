package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

class ImperialMass extends UnitOfMeasureAbstract {

    ImperialMass() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAXIMUM_MASS;
        minimumMeasurement = UnitOfMeasureConstants.MINIMUM_MASS;
        unitTwo = minimumMeasurement * 453.59237; // 1 pound
        unitOne = unitTwo / 16; // 1 ounce
        unitOneDecimal = unitOne / 10; // one tenth of an ounce
        smallestUnit = unitOneDecimal;

        subtype = MeasurementSubtype.IMPERIAL_MASS;

        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_imperial_mass;
        unitOneLabelStringResourceId = R.string.ounces;
        unitTwoLabelStringResourceId = R.string.pounds;
    }
}