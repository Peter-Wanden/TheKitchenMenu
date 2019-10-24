package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

class MetricMass extends UnitOfMeasureAbstract {

    MetricMass() {
        measurementType = METRIC_MASS_TYPE;
        subtype = METRIC_MASS_SUBTYPE;
        numberOfMeasurementUnits = METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS;
        maximumMeasurement = METRIC_MASS_MAX_MEASUREMENT;
        minimumMeasurement = METRIC_MASS_MIN_MEASUREMENT;
        unitTwo = METRIC_MASS_UNIT_TWO;
        unitOne = METRIC_MASS_UNIT_ONE;
        unitOneDecimal = METRIC_MASS_UNIT_ONE_DECIMAL;
        smallestUnit = METRIC_MASS_SMALLEST_UNIT;
        isConversionFactorEnabled = METRIC_MASS_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_metric_mass;
        unitOneLabelStringResourceId = R.string.grams;
        unitTwoLabelStringResourceId = R.string.kilograms;
    }
}