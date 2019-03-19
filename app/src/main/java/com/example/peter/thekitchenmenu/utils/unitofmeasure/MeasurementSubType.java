package com.example.peter.thekitchenmenu.utils.unitofmeasure;

public enum MeasurementSubType {

    NOTHING_SELECTED(MeasurementType.TYPE_NO_TYPE),
    TYPE_METRIC_MASS(MeasurementType.TYPE_MASS),
    TYPE_IMPERIAL_MASS(MeasurementType.TYPE_MASS),
    SELECT_VOLUME(MeasurementType.TYPE_VOLUME),
    TYPE_METRIC_VOLUME(MeasurementType.TYPE_VOLUME),
    TYPE_IMPERIAL_VOLUME(MeasurementType.TYPE_VOLUME),
    SELECT_COUNT(MeasurementType.TYPE_COUNT),
    TYPE_COUNT(MeasurementType.TYPE_COUNT);

    private MeasurementType measurementType;

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    MeasurementSubType(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }
}