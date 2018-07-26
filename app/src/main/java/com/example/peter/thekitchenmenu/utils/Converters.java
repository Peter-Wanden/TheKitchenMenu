package com.example.peter.thekitchenmenu.utils;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

public class Converters {
    /* Helper method to convert the unit of measure from an integer to a String value */
    public static String getStringUnitOfMeasure(Context context, int requestUnitOfMeasure) {

        String unitOfMeasure;

        switch (requestUnitOfMeasure) {
            case 1:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_1);
                break;
            case 2:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_2);
                break;
            case 3:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_3);
                break;
            default:
                unitOfMeasure = context.getResources().getString(R.string.uom_option_0);
                break;
        }
        return unitOfMeasure;
    }
}
