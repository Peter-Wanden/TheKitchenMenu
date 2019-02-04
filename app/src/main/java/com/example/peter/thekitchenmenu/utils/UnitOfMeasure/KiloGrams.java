package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;

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
    public Integer convertValueToBaseSiUnit(String inputWeight) {
        if (inputWeight == null || inputWeight.equals("")) {
            return 0;
        }
        return weightConvertedToBasSi(inputWeight);
    }

    private Integer weightConvertedToBasSi(String inputWeight) {
        double weight = parseInputWeight(inputWeight);
        return convertWeightToBaseUnits(weight);
    }

    private int convertWeightToBaseUnits(double weight) {
        double weightConvertedToBaseUnits = weight * 1000;
        return (int) weightConvertedToBaseUnits;
    }

    private double parseInputWeight(String inputWeight) {
        if (inputWeight.equals(".")) {
            return Double.parseDouble(appendZeroTo(inputWeight));
        }
        return Double.parseDouble(inputWeight);
    }

    private String appendZeroTo(String inputWeight) {
        StringBuilder builder = new StringBuilder(inputWeight);
        builder.insert(0, 0);
        return builder.toString();
    }

    @Override
    public int getInputNumberType() {
        return DECIMAL;
    }
}
