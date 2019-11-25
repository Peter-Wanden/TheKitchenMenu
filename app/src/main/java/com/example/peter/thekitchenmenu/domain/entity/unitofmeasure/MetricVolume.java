package com.example.peter.thekitchenmenu.domain.entity.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricVolume extends UnitOfMeasureAbstract {

    public MetricVolume() {
        measurementType = METRIC_VOLUME_TYPE;
        subtype = METRIC_VOLUME_SUBTYPE;
        numberOfUnits = METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
        maxMeasurement = METRIC_VOLUME_MAX_MEASUREMENT;
        minMeasurement = METRIC_VOLUME_MIN_MEASUREMENT;
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