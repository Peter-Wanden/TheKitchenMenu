package com.example.peter.thekitchenmenu.ui.bindingadapters;

import androidx.databinding.InverseMethod;

import com.example.peter.thekitchenmenu.ui.utils.CountFraction;

public class CountFractionsEnumToIntConverter {

    @InverseMethod("toFraction")
    public static int toInt(CountFraction fraction) {
        return fraction.asInt();
    }

    public static CountFraction toFraction(int fractionAsInt) {
        return CountFraction.fromInt(fractionAsInt);
    }
}
