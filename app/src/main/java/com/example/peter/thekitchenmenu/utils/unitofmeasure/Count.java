package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

class Count extends UnitOfMeasureAbstract {

    Count() {
        measurementType = COUNT_TYPE;
        subtype = COUNT_SUBTYPE;
        numberOfMeasurementUnits = COUNT_NUMBER_OF_MEASUREMENT_UNITS;
        maximumMeasurement = COUNT_MAX_MEASUREMENT;
        minimumMeasurement = COUNT_MIN_MEASUREMENT;
        unitTwo = COUNT_UNIT_TWO;
        unitOne = COUNT_UNIT_ONE;
        unitOneDecimal = COUNT_UNIT_ONE_DECIMAL;
        smallestUnit = COUNT_SMALLEST_UNIT;
        isConversionFactorEnabled = COUNT_IS_CONVERSION_FACTOR_ENABLED;
    }
}
