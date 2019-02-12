package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.MetricMass.BASE_SI_UNIT_MASS;

public class ImperialMass implements UnitOfMeasure {

    private static final double BASE_SI_UNIT_POUND = BASE_SI_UNIT_MASS * 453.59237;
    private static final double BASE_SI_UNIT_OUNCE = BASE_SI_UNIT_MASS * 28.3495231;
    private String type;

    ImperialMass(Context context) {
        this.type = context.getResources().getString(R.string.mass);
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public MeasurementType getType() {
        return MeasurementType.TYPE_MASS;
    }

    @Override
    public String getUnitsAsString() {
        return null;
    }

    @Override
    public int getUnitAsInt() {
        return 0;
    }

    @Override
    public void setMeasurement(double measurement) {

    }

    @Override
    public double getMeasurement() {
        return 0;
    }

    @Override
    public void setBaseSiUnits(int baseSiUnits) {

    }

    @Override
    public int getBaseSiUnits() {
        return 0;
    }

    @Override
    public int convertToBaseSiUnits(double measurement) {
        return 0;
    }

    @Override
    public double getMax() {
        return 0;
    }

    @Override
    public Pair<Integer, Integer> getInputFilterFormat() {
        return null;
    }
}
