package com.example.peter.thekitchenmenu.utils.unitofmeasure;

public enum MeasurementSubtype {

    TYPE_METRIC_MASS(MeasurementType.TYPE_MASS) {

        @Override
        public UnitOfMeasure getMeasurementClass() {

            return new MetricMass();
        }
    },

    TYPE_IMPERIAL_MASS(MeasurementType.TYPE_MASS) {

        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new ImperialMass();
        }
    },

    TYPE_METRIC_VOLUME(MeasurementType.TYPE_VOLUME) {

        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new MetricVolume();
        }
    },

    TYPE_IMPERIAL_VOLUME(MeasurementType.TYPE_VOLUME) {

        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new ImperialVolume();
        }
    },

    TYPE_COUNT(MeasurementType.TYPE_COUNT) {

        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new Count();
        }
    };

    private MeasurementType measurementType;

    // TODO - Implement for conversion between types
    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    MeasurementSubtype(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public abstract UnitOfMeasure getMeasurementClass();
}