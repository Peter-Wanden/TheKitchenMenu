package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

class ImperialVolume extends UnitOfMeasureAbstract {

    ImperialVolume() {
        measurementType = IMPERIAL_VOLUME_TYPE;
        subtype = IMPERIAL_VOLUME_SUBTYPE;
        numberOfMeasurementUnits = IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
        maximumMeasurement = IMPERIAL_VOLUME_MAX_MEASUREMENT;
        minimumMeasurement = IMPERIAL_VOLUME_MIN_MEASUREMENT;
        unitTwo = IMPERIAL_VOLUME_UNIT_TWO;
        unitOne = IMPERIAL_VOLUME_UNIT_ONE;
        unitOneDecimal = IMPERIAL_VOLUME_UNIT_ONE_DECIMAL;
        smallestUnit = IMPERIAL_VOLUME_SMALLEST_UNIT;
        isConversionFactorEnabled = IMPERIAL_VOLUME_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.fluidOunce;
        unitTwoLabelStringResourceId = R.string.pints;
    }
}