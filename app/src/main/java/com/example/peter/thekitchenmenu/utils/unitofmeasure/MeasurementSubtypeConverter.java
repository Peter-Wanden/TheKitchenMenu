package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.databinding.InverseMethod;

public class MeasurementSubtypeConverter {

    @InverseMethod("toSubtype")
    public static int toInt(MeasurementSubtype subtype) {
        return subtype.ordinal();
    }

    public static MeasurementSubtype toSubtype(int ordinal) {
        return MeasurementSubtype.values()[ordinal];
    }
}
