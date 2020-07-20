package com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

public class ImperialSpoon extends UnitOfMeasureAbstract {

    public ImperialSpoon() {
        measurementType = UnitOfMeasureConstants.IMPERIAL_SPOON_TYPE;
        subtype = UnitOfMeasureConstants.IMPERIAL_SPOON_SUBTYPE;
        numberOfUnits = UnitOfMeasureConstants.IMPERIAL_SPOON_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = UnitOfMeasureConstants.IMPERIAL_SPOON_MAX_MEASUREMENT;
        minMeasurement = UnitOfMeasureConstants.IMPERIAL_SPOON_MIN_MEASUREMENT;
        unitTwo = UnitOfMeasureConstants.IMPERIAL_SPOON_UNIT_TWO;
        unitOne = UnitOfMeasureConstants.IMPERIAL_SPOON_UNIT_ONE;
        unitTwoNoConversionFactor = UnitOfMeasureConstants.IMPERIAL_SPOON_UNIT_TWO;
        unitOneNoConversionFactor = UnitOfMeasureConstants.IMPERIAL_SPOON_UNIT_ONE;
        unitOneDecimal = UnitOfMeasureConstants.IMPERIAL_SPOON_UNIT_ONE_DECIMAL;
        smallestUnit = UnitOfMeasureConstants.IMPERIAL_SPOON_SMALLEST_UNIT;
        isConversionFactorEnabled = UnitOfMeasureConstants.IMPERIAL_SPOON_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.teaspoon;
        unitTwoLabelStringResourceId = R.string.tablespoon;
    }
}
