package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

class ImperialMass extends UnitOfMeasureAbstract {

    ImperialMass() {
        measurementType = IMPERIAL_MASS_TYPE;
        subtype = IMPERIAL_MASS_SUBTYPE;
        numberOfMeasurementUnits = IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS;
        maximumMeasurement = IMPERIAL_MASS_MAX_MEASUREMENT;
        minimumMeasurement = IMPERIAL_MASS_SMALLEST_UNIT;
        unitTwo = IMPERIAL_MASS_UNIT_TWO;
        unitOne = IMPERIAL_MASS_UNIT_ONE;
        unitOneDecimal = IMPERIAL_MASS_UNIT_ONE_DECIMAL;
        smallestUnit = IMPERIAL_MASS_SMALLEST_UNIT;
        isConversionFactorEnabled = IMPERIAL_MASS_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_imperial_mass;
        unitOneLabelStringResourceId = R.string.ounces;
        unitTwoLabelStringResourceId = R.string.pounds;
    }
}