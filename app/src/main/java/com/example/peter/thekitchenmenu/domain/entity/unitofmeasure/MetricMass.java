package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricMass extends UnitOfMeasureAbstract {

    public MetricMass() {
        measurementType = METRIC_MASS_TYPE;
        subtype = METRIC_MASS_SUBTYPE;
        numberOfUnits = METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = METRIC_MASS_MAX_MEASUREMENT;
        minMeasurement = METRIC_MASS_MIN_MEASUREMENT;
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