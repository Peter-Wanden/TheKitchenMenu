package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import androidx.databinding.InverseMethod;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;

public class MeasurementSubtypeConverter {

    @InverseMethod("toSubtype")
    public static int toInt(MeasurementSubtype subtype) {
        return subtype.asInt();
    }

    public static MeasurementSubtype toSubtype(int subtypeAsInt) {
        return MeasurementSubtype.fromInt(subtypeAsInt);
    }
}
