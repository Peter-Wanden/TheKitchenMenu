package com.example.peter.thekitchenmenu.ui.bindingadapters;

import androidx.databinding.InverseMethod;

import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;

public class MeasurementSubtypeConverter {

    @InverseMethod("toSubtype")
    public static int toInt(MeasurementSubtype subtype) {
        return subtype.asInt();
    }

    public static MeasurementSubtype toSubtype(int subtypeAsInt) {
        return MeasurementSubtype.fromInt(subtypeAsInt);
    }
}
