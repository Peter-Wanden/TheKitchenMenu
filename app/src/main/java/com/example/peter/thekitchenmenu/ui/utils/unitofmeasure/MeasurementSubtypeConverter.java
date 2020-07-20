package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.MeasurementSubtype;

public class MeasurementSubtypeConverter {

//    @InverseMethod("toSubtype")
    public static int toInt(MeasurementSubtype subtype) {
        return subtype.asInt();
    }

    public static MeasurementSubtype toSubtype(int subtypeAsInt) {
        return MeasurementSubtype.fromInt(subtypeAsInt);
    }
}
