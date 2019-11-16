package com.example.peter.thekitchenmenu.domain.unitofmeasureentities;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum MeasurementSubtype {

    METRIC_MASS(MeasurementType.MASS) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new MetricMass();
        }

        @Override
        public int asInt() {
            return 0;
        }
    },

    IMPERIAL_MASS(MeasurementType.MASS) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new ImperialMass();
        }

        @Override
        public int asInt() {
            return 1;
        }
    },

    METRIC_VOLUME(MeasurementType.VOLUME) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new MetricVolume();
        }

        @Override
        public int asInt() {
            return 2;
        }
    },

    IMPERIAL_VOLUME(MeasurementType.VOLUME) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new ImperialVolume();
        }

        @Override
        public int asInt() {
            return 3;
        }
    },

    COUNT(MeasurementType.COUNT) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new Count();
        }

        @Override
        public int asInt() {
            return 4;
        }
    },

    IMPERIAL_SPOON(MeasurementType.VOLUME) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new ImperialSpoon();
        }

        @Override
        public int asInt() {
            return 5;
        }

    };

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, MeasurementSubtype> map = new HashMap<>();

    static {
        for (MeasurementSubtype subtype : MeasurementSubtype.values())
            map.put(subtype.asInt(), subtype);
    }

    public static MeasurementSubtype fromInt(int subTypeAsInt) {
        return map.get(subTypeAsInt);
    }

    private MeasurementType measurementType;

    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    MeasurementSubtype(MeasurementType measurementType) {
        this.measurementType = measurementType;
    }

    public abstract UnitOfMeasure getMeasurementClass();

    public abstract int asInt();
}