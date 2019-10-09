package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

class ImperialVolume extends UnitOfMeasureAbstract {

    ImperialVolume() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAXIMUM_VOLUME;
        minimumMeasurement = UnitOfMeasureConstants.MINIMUM_VOLUME;
        unitTwo = minimumMeasurement * 568.26125; // 1 Pint
        unitOne = unitTwo / 20; // 1 fluid ounce
        unitOneDecimal = unitOne / 10; // One tenth of a fl oz
        smallestUnit = unitOneDecimal;

        subtype = MeasurementSubtype.IMPERIAL_VOLUME;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.fluidOunce;
        unitTwoLabelStringResourceId = R.string.pints;
    }
}