package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class Grams implements UnitOfMeasure {

    private static final String TAG = "Grams";

    private String type;
    private String unit;

    Grams(Context applicationContext) {
        type = applicationContext.getResources().getString(R.string.weight);
        unit = applicationContext.getResources().getString(R.string.grams);
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public int getTypeAsInt() {
        return TYPE_WEIGHT;
    }

    @Override
    public String getUnitAsString() {
        return unit;
    }

    @Override
    public int getUnitAsInt() {
        return UNIT_GRAMS;
    }

    @Override
    public int convertToInt(UnitOfMeasure newUnitOfMeasure, String inputWeight) {

//        if (inputWeight == NO_INPUT) {
//            return NO_INPUT;
//        }
//
//        if (newUnitOfMeasure.getTypeAsInt() == TYPE_WEIGHT) {
//
//            if (newUnitOfMeasure instanceof Grams) {
//                return (int) inputWeight;
//
//            } else if (newUnitOfMeasure instanceof KiloGrams) {
//                return convertKilogramsToBaseSiUnit(inputWeight);
//
//            }
//        }
        return INCONVERTIBLE;
    }

    @Override
    public double convertToDouble(UnitOfMeasure newUnitOfMeasure, String inputWeight) {
        int weight = Integer.parseInt(inputWeight);

        if (weight == NO_INPUT) {
            return NO_INPUT;
        }

        if (newUnitOfMeasure instanceof KiloGrams) {
            return convertToKilograms(weight);
        }

        return INCONVERTIBLE;
    }

    private double convertToKilograms(int weight) {
        double convertedWeight = (double) weight;
        Log.d(TAG, "convertToKilograms: int weight: " + weight);
        Log.d(TAG, "convertToKilograms: double weight: " + convertedWeight);
        convertedWeight = convertedWeight / 1000;
        Log.d(TAG, "convertToKilograms: double weight converted: " +convertedWeight);
        return convertedWeight;
    }

    @Override
    public int convertToBaseUnit(int value) {
        return value;
    }

    @Override
    public int convertToBaseUnit(double value) {
        Log.d(TAG, "convertToBaseUnit: " + value);
        Log.d(TAG, "convertToBaseUnit: " + (int) value);
        return (int) value;
    }
}