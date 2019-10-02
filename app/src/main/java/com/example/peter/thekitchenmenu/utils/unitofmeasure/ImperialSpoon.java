package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

class ImperialSpoon extends UnitOfMeasureImpl {

    ImperialSpoon() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAXIMUM_VOLUME;
        minimumMeasurement = UnitOfMeasureConstants.MINIMUM_VOLUME;
        unitOne = minimumMeasurement * 5; // 5ml
        unitTwo = minimumMeasurement * 15; // 15ml
        unitOneDecimal = 0; // No decimal places
        smallestUnit = unitOne;

        subtype = MeasurementSubtype.TYPE_IMPERIAL_SPOON;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.teaspoon;
        unitTwoLabelStringResourceId = R.string.tablespoon;
    }
}
