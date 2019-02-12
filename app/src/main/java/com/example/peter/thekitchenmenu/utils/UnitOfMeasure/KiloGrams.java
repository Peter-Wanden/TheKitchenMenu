package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class KiloGrams implements UnitOfMeasure {

    private static final String TAG = "KiloGrams";
    private static final double BASE_SI_UNITS_KILOGRAMS = 1;
    private static final double BASE_SI_UNITS_GRAMS = 1000;

    private String type;
    private String unit;
    private double measurement;
    private int baseSiUnits;

    KiloGrams(Context Context) {
        type = Context.getResources().getString(R.string.mass);
        unit = Context.getResources().getString(R.string.kilograms);
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
        return UNIT_KILOGRAMS;
    }

    @Override
    public void setMeasurement(double measurement) {
        baseSiUnits = convertToBaseSiUnits(measurement);
        this.measurement = convertToMeasurement(baseSiUnits);
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
        return baseSiUnits / BASE_SI_UNITS_GRAMS;
    }

    @Override
    public int getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public int convertToBaseSiUnits(double measurement) {
        double baseSiUnits = measurement * BASE_SI_UNITS_GRAMS;
        this.baseSiUnits = (int) baseSiUnits;
        return this.baseSiUnits;
    }

    @Override
    public double getMax() {
        return MAX_MASS / BASE_SI_UNITS_GRAMS;
    }

    @Override
    public Pair<Integer, Integer> getInputFilterFormat() {
        return new Pair<>(THREE_DECIMAL_PLACES, THREE_DECIMAL_PLACES);
    }
}
