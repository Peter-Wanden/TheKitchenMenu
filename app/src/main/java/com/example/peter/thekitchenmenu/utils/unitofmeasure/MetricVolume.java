package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

class MetricVolume extends UnitOfMeasureAbstract {

    MetricVolume() {
        measurementType = METRIC_VOLUME_TYPE;
        subtype = METRIC_VOLUME_SUBTYPE;
        numberOfMeasurementUnits = METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
        maximumMeasurement = METRIC_VOLUME_MAX_MEASUREMENT;
        minimumMeasurement = METRIC_VOLUME_MIN_MEASUREMENT;
        unitTwo = METRIC_VOLUME_UNIT_TWO;
        unitOne = METRIC_VOLUME_UNIT_ONE;
        unitOneDecimal = METRIC_VOLUME_UNIT_ONE_DECIMAL;
        smallestUnit = METRIC_VOLUME_SMALLEST_UNIT;
        isConversionFactorEnabled = METRIC_VOLUME_IS_CONVERSION_FACTOR_ENABLED;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_metric_volume;
        unitOneLabelStringResourceId = R.string.millilitres;
        unitTwoLabelStringResourceId = R.string.litres;
    }
}