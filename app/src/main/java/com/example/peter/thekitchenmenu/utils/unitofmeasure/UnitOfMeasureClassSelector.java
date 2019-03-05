package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

public abstract class UnitOfMeasureClassSelector {

    public static UnitOfMeasure getClassWithSubType(Context context, MeasurementSubType measurementSubType) {

        switch (measurementSubType) {
            case TYPE_METRIC_MASS:
                return new MetricMass(context);

            case TYPE_IMPERIAL_MASS:
                return new ImperialMass(context);

            case TYPE_METRIC_VOLUME:
                return new MetricVolume(context);

            case TYPE_IMPERIAL_VOLUME:
                return new ImperialVolume(context);

            case TYPE_COUNT:
                return new Count(context);

            default:
                // TODO - default cannot return null
                return new Count(context);
        }
    }
}
