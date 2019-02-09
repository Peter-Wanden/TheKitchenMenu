package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class KiloGrams implements UnitOfMeasure {

    private static final String TAG = "KiloGrams";
    private static final double BASE_SI_UNITS = 1000;

    private String type;
    private String unit;
    private double measurement = 0;
    private int baseSiUnits = 0;

    KiloGrams(Context Context) {
        type = Context.getResources().getString(R.string.mass);
        unit = Context.getResources().getString(R.string.kilograms);
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public int getTypeAsInt() {
        return TYPE_MASS;
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
    public void setMeasurement(double measurement) {
        this.measurement = measurement;
        baseSiUnits = convertToBaseSiUnits(this.measurement);
    }

    @Override
    public double getMeasurement() {
        return measurement;
    }

    @Override
    public void setBaseSiUnits(int baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
        measurement = convertToMeasurement(this.baseSiUnits);
    }

    private double convertToMeasurement(int baseSiUnits) {
        return baseSiUnits / BASE_SI_UNITS;
    }

    @Override
    public int getBaseSiUnits() {
        return baseSiUnits;
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
}
