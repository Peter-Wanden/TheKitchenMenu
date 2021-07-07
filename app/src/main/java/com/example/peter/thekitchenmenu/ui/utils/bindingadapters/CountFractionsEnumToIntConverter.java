package com.example.peter.thekitchenmenu.ui.utils.bindingadapters;

import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.CountFraction;

public class CountFractionsEnumToIntConverter {

//    @InverseMethod("toFraction")
    public static int toInt(CountFraction fraction) {
        return fraction.toInt();
    }

    public static CountFraction toFraction(int fractionAsInt) {
        return CountFraction.fromInt(fractionAsInt);
    }
}
