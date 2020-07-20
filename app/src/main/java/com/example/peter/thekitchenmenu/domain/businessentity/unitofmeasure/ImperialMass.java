package com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

public class ImperialMass extends UnitOfMeasureAbstract {

    public ImperialMass() {
        measurementType = UnitOfMeasureConstants.IMPERIAL_MASS_TYPE;
        subtype = UnitOfMeasureConstants.IMPERIAL_MASS_SUBTYPE;
        numberOfUnits = UnitOfMeasureConstants.IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = UnitOfMeasureConstants.IMPERIAL_MASS_MAX_MEASUREMENT;
        minMeasurement = UnitOfMeasureConstants.IMPERIAL_MASS_SMALLEST_UNIT;
        unitTwo = UnitOfMeasureConstants.IMPERIAL_MASS_UNIT_TWO;
        unitOne = UnitOfMeasureConstants.IMPERIAL_MASS_UNIT_ONE;
        unitOneDecimal = UnitOfMeasureConstants.IMPERIAL_MASS_UNIT_ONE_DECIMAL;
        smallestUnit = UnitOfMeasureConstants.IMPERIAL_MASS_SMALLEST_UNIT;
        isConversionFactorEnabled = UnitOfMeasureConstants.IMPERIAL_MASS_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_imperial_mass;
        unitOneLabelStringResourceId = R.string.ounces;
        unitTwoLabelStringResourceId = R.string.pounds;
    }
}