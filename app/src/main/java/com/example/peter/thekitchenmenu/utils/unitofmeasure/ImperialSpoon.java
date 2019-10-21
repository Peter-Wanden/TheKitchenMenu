package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

class ImperialSpoon extends UnitOfMeasureAbstract {

    ImperialSpoon() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAX_VOLUME;
        minimumMeasurement = UnitOfMeasureConstants.MIN_VOLUME / 1000;
        unitOne = 5; // 5ml
        unitTwo = 15; // 15ml
        unitOneDecimal = 1; // One decimal place representing 10ths of a teaspoon
        smallestUnit = unitOne;

        subtype = MeasurementSubtype.IMPERIAL_SPOON;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.teaspoon;
        unitTwoLabelStringResourceId = R.string.tablespoon;
    }
}
