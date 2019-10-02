package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum MeasurementSubtype {

    TYPE_METRIC_MASS(MeasurementType.TYPE_MASS) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new MetricMass();
        }

        @Override
        public int asInt() {
            return 0;
        }
    },

    TYPE_IMPERIAL_MASS(MeasurementType.TYPE_MASS) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new ImperialMass();
        }

        @Override
        public int asInt() {
            return 1;
        }
    },

    TYPE_METRIC_VOLUME(MeasurementType.TYPE_VOLUME) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new MetricVolume();
        }

        @Override
        public int asInt() {
            return 2;
        }
    },

    TYPE_IMPERIAL_VOLUME(MeasurementType.TYPE_VOLUME) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new ImperialVolume();
        }

        @Override
        public int asInt() {
            return 3;
        }
    },

    TYPE_COUNT(MeasurementType.TYPE_COUNT) {
        @Override
        public UnitOfMeasure getMeasurementClass() {
            return new Count();
        }

        @Override
        public int asInt() {
            return 4;
        }
    },

    TYPE_IMPERIAL_SPOON(MeasurementType.TYPE_VOLUME) {
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