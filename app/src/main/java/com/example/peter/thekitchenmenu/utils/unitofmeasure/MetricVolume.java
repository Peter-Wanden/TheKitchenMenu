package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

class MetricVolume extends UnitOfMeasureAbstract {

    MetricVolume() {
        numberOfMeasurementUnits = 2;
        maximumMeasurement = UnitOfMeasureConstants.MAX_VOLUME;
        minimumMeasurement = UnitOfMeasureConstants.MIN_VOLUME;
        unitTwo = minimumMeasurement * 1000;
        unitOne = unitTwo / 1000;
        unitOneDecimal = 0;
        smallestUnit = unitOne;

        subtype = MeasurementSubtype.METRIC_VOLUME;

        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_metric_volume;
        unitOneLabelStringResourceId = R.string.millilitres;
        unitTwoLabelStringResourceId = R.string.litres;
    }
}