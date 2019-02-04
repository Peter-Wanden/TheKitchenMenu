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
    public Integer convertValueToBaseSiUnit(String inputWeight) {
        if (inputWeight == null || inputWeight.equals("")) {
            return 0;
        }

        if (inputWeight.contains(".")) {
            String noDecimalPoint = removeDecimalPointFromString(inputWeight);
            return Integer.parseInt(noDecimalPoint);
        }
        return Integer.parseInt(inputWeight);
    }

    private String removeDecimalPointFromString(String inputWeight) {
        inputWeight = inputWeight.replace(".", "");
        return inputWeight;
    }

    @Override
    public int getInputNumberType() {
        return CARDINAL;
    }
}
