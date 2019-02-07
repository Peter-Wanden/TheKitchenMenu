package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class KiloGrams implements UnitOfMeasure {

    private static final String TAG = "KiloGrams";

    private String type;
    private String unit;

    KiloGrams(Context applicationContext) {
        type = applicationContext.getResources().getString(R.string.weight);
        unit = applicationContext.getResources().getString(R.string.kilograms);
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
        return UNIT_KILOGRAMS;
    }

    @Override
    public int convertToInt(UnitOfMeasure newUnitOfMeasure, String inputWeight) {
        double weight = Double.parseDouble(inputWeight);

        if (weight == NO_INPUT) {
            Log.d(TAG, "convertTo: input weight is: " + weight);
            return NO_INPUT;
        }

        if (newUnitOfMeasure instanceof Grams) {
            return convertWeightToBaseUnits(weight);
        }

        return INCONVERTIBLE;
    }

    @Override
    public double convertToDouble(UnitOfMeasure newUnitOfMeasure, String value) {
        return 0;
    }


    @Override
    public int convertToBaseUnit(int weight) {
        Log.d(TAG, "convertToBaseUnit: int is: " + weight);
        return convertWeightToBaseUnits(weight);
    }

    @Override
    public int convertToBaseUnit(double weight) {
        Log.d(TAG, "convertToBaseUnit: double is: " + weight);
        return convertWeightToBaseUnits(weight);
    }

    private int convertWeightToBaseUnits(double weight) {
        Log.d(TAG, "convertWeightToBaseUnits: weight is: " + weight);
        double weightConvertedToBaseUnits = weight * 1000;
        Log.d(TAG, "convertWeightToBaseUnits: weight * 1k is: " + weightConvertedToBaseUnits);
        Log.d(TAG, "convertWeightToBaseUnits: weight (int) is: " + (int) weightConvertedToBaseUnits);
        return (int) weightConvertedToBaseUnits;
    }
}
