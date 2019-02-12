package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class Ounces implements UnitOfMeasure {

    private static final String TAG = "Ounces";
    private static final double BASE_SI_UNITS = 28.3495;

    private String type;
    private String unit;
    private double measurement;
    private int baseSiUnits;

    Ounces(Context applicationContext) {
        type = applicationContext.getResources().getString(R.string.mass);
        unit = applicationContext.getResources().getString(R.string.ounces);
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
        return unit;
    }

    @Override
    public int getUnitAsInt() {
        return UNIT_OUNCES;
    }

    @Override
    public void setMeasurement(double measurement) {
        baseSiUnits = convertToBaseSiUnits(measurement);
        this.measurement = measurement;
    }

    @Override
    public double getMeasurement() {
        return Math.round(measurement * 1d) /1d;
    }

    @Override
    public void setBaseSiUnits(int baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
        measurement = convertToMeasurement(baseSiUnits);
    }

    private double convertToMeasurement(int baseSiUnits) {
        return baseSiUnits / BASE_SI_UNITS;
    }

    @Override
    public int getBaseSiUnits() {
        return this.baseSiUnits;
    }

    @Override
    public int convertToBaseSiUnits(double measurement) {
        double baseSiUnits = measurement * BASE_SI_UNITS;
        this.baseSiUnits = (int) baseSiUnits;
        return this.baseSiUnits;
    }

    @Override
    public double getMax() {
        return MAX_MASS / BASE_SI_UNITS;
    }

    @Override
    public Pair<Integer, Integer> getInputFilterFormat() {
        return new Pair<>(FOUR_DECIMAL_PLACES, NO_DECIMAL_PLACES);
    }
}
