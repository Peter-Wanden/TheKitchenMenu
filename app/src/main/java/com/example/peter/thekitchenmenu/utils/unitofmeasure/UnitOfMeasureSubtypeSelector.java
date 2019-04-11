package com.example.peter.thekitchenmenu.utils.unitofmeasure;

public abstract class UnitOfMeasureSubtypeSelector {

    public static UnitOfMeasure getClassWithSubType(MeasurementSubType subType) {

        switch (subType) {
            case TYPE_METRIC_MASS:
                return new MetricMass();

            case TYPE_IMPERIAL_MASS:
                return new ImperialMass();

            case TYPE_METRIC_VOLUME:
                return new MetricVolume();

            case TYPE_IMPERIAL_VOLUME:
                return new ImperialVolume();

            case TYPE_COUNT:
                return new Count();

            default:
                // default cannot return null
                return new Count();
        }
    }
}
