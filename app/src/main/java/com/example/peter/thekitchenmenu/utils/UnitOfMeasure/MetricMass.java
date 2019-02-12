package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class MetricMass {

    private static final String TAG = "MetricMass";

    public static final int BASE_SI_UNIT_MASS = 1;
    private static final int BASE_SI_UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000;

    private String type;
    private String unit;
    private double measurement;
    private int baseSiUnits;
    private Pair<Integer, Integer> kilograms;
    private Pair<Integer, Integer> grams;

    MetricMass(Context Context) {
        type = Context.getResources().getString(R.string.mass);
        unit = Context.getResources().getString(R.string.kilograms);
    }

    public String getTypeAsString() {
        return type;
    }

    public MeasurementType getType() {
        return MeasurementType.TYPE_MASS;
    }

    public String getUnitsAsString() {
        return unit;
    }

    public int getUnitAsInt() {
        return UNIT_KILOGRAMS;
    }

    public void setMeasurement(double measurement) {
        baseSiUnits = convertToBaseSiUnits(measurement);
        this.measurement = convertToMeasurement(baseSiUnits);
    }

    public double getMeasurement() {
        kilograms = new Pair<>(1,1);
        grams = new Pair<>(2, 200);
        Pair<Pair, Pair> returnValue = new Pair<>(kilograms, grams);
        return measurement;
    }

    public void setBaseSiUnits(int baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
        measurement = convertToMeasurement(this.baseSiUnits);
    }

    private double convertToMeasurement(int baseSiUnits) {
        return baseSiUnits;
    }

    public int getBaseSiUnits() {
        return baseSiUnits;
    }

    public int convertToBaseSiUnits(double measurement) {
        double baseSiUnits = measurement;
        this.baseSiUnits = (int) baseSiUnits;
        return this.baseSiUnits;
    }

    public double getMax() {
        return MAX_MASS;
    }

    public Pair<Integer, Integer> getInputFilterFormat() {
        return new Pair<>(THREE_DECIMAL_PLACES, THREE_DECIMAL_PLACES);
    }
}
