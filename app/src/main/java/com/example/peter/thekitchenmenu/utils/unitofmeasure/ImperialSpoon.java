package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

class ImperialSpoon extends UnitOfMeasureAbstract {

    ImperialSpoon() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAXIMUM_VOLUME;
        minimumMeasurement = UnitOfMeasureConstants.MINIMUM_VOLUME / 1000;
        unitOne = 5; // 5ml
        unitTwo = 15; // 15ml
        unitOneDecimal = 1; // One decimal place for half / quarter / whatever of a teaspoon
        smallestUnit = unitOne;

        subtype = MeasurementSubtype.IMPERIAL_SPOON;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.teaspoon;
        unitTwoLabelStringResourceId = R.string.tablespoon;
    }
}
