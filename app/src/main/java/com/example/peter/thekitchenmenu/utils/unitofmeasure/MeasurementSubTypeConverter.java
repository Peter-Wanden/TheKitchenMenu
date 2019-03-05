package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.databinding.InverseMethod;

public class MeasurementSubTypeConverter {

    @InverseMethod("toSubType")
    public static int toInt(MeasurementSubType subType) {
        return subType.ordinal();
    }

    public static MeasurementSubType toSubType(int ordinal) {
        return MeasurementSubType.values()[ordinal];
    }
}
