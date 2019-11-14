package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

class ImperialSpoon extends UnitOfMeasureAbstract {

    ImperialSpoon() {
        measurementType = IMPERIAL_SPOON_TYPE;
        subtype = IMPERIAL_SPOON_SUBTYPE;
        numberOfUnits = IMPERIAL_SPOON_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = IMPERIAL_SPOON_MAX_MEASUREMENT;
        minMeasurement = IMPERIAL_SPOON_MIN_MEASUREMENT;
        unitTwo = IMPERIAL_SPOON_UNIT_TWO;
        unitOne = IMPERIAL_SPOON_UNIT_ONE;
        unitTwoNoConversionFactor = IMPERIAL_SPOON_UNIT_TWO;
        unitOneNoConversionFactor = IMPERIAL_SPOON_UNIT_ONE;
        unitOneDecimal = IMPERIAL_SPOON_UNIT_ONE_DECIMAL;
        smallestUnit = IMPERIAL_SPOON_SMALLEST_UNIT;
        isConversionFactorEnabled = IMPERIAL_SPOON_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.teaspoon;
        unitTwoLabelStringResourceId = R.string.tablespoon;
    }
}
