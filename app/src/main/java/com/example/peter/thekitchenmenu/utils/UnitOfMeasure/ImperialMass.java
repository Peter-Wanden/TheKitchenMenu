package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

import java.util.HashMap;

import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.MetricMass.BASE_SI_UNIT_MASS;
import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class ImperialMass {

    private static final String TAG = "ImperialMass";

    private static final double UNIT_POUND = BASE_SI_UNIT_MASS * 453.59237;
    private static final double UNIT_OUNCE = UNIT_POUND / 16;
    private double baseSiUnits;
    private Integer measurementInPounds;
    private Integer measurementInOunces;
    private String measurementType;
    private String poundsUnit;
    private String ouncesUnit;

    ImperialMass(Context context) {

        this.measurementType = context.getResources().getString(R.string.mass);
        this.poundsUnit = context.getResources().getString(R.string.pounds);
        this.ouncesUnit = context.getResources().getString(R.string.ounces);
    }

    public String getTypeAsString() {
        return measurementType;
    }

    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_MASS;
    }

    public HashMap<UnitOfMeasureUnits, String> getUnits() {

        HashMap<UnitOfMeasureUnits, String> unitOfMeasureUnits = new HashMap<>();
        unitOfMeasureUnits.put(UnitOfMeasureUnits.POUNDS, poundsUnit);
        unitOfMeasureUnits.put(UnitOfMeasureUnits.OUNCES, ouncesUnit);

        return unitOfMeasureUnits;
    }

    public boolean setMeasurement(HashMap<UnitOfMeasureUnits, Integer> measurement) {

        if (measurement.containsKey(UnitOfMeasureUnits.POUNDS) &&
                measurement.containsKey(UnitOfMeasureUnits.OUNCES)) {

            if (checkMax(measurement) && checkMin(measurement)) {

                measurementInPounds = measurement.get(UnitOfMeasureUnits.POUNDS);
                baseSiUnits = Math.round(measurementInPounds * UNIT_POUND);

                measurementInOunces = measurement.get(UnitOfMeasureUnits.OUNCES);
                baseSiUnits += Math.round(measurementInOunces * UNIT_OUNCE);

                return true;
            }
        }
        return false;
    }

    public HashMap<UnitOfMeasureUnits, Integer> getMeasurement() {

        HashMap<UnitOfMeasureUnits, Integer> unitOfMeasureUnits = new HashMap<>();
        unitOfMeasureUnits.put(UnitOfMeasureUnits.POUNDS, measurementInPounds);
        unitOfMeasureUnits.put(UnitOfMeasureUnits.OUNCES, measurementInOunces);

        return unitOfMeasureUnits;
    }

    public boolean setBaseSiUnits(int baseSiUnits) {

        if (baseSiUnits > MIN_MASS && baseSiUnits < MAX_MASS) {
            this.baseSiUnits = baseSiUnits;
            return true;
        }
        return false;
    }

    public int getBaseSiUnits() {
        return (int) Math.round(baseSiUnits);
    }

    public HashMap<UnitOfMeasureUnits, Integer> getMax() {

        int pounds = (int) (MAX_MASS / UNIT_POUND);
        int ounces = (int) (MAX_MASS % UNIT_OUNCE);

        HashMap<UnitOfMeasureUnits, Integer> maxValues = new HashMap<>();
        maxValues.put(UnitOfMeasureUnits.POUNDS, pounds);
        maxValues.put(UnitOfMeasureUnits.OUNCES, ounces);

        return maxValues;
    }

    private boolean checkMax(HashMap<UnitOfMeasureUnits, Integer> checkMeasurement) {

        HashMap<UnitOfMeasureUnits, Integer> maxValues = getMax();

        int inputPounds = checkMeasurement.get(UnitOfMeasureUnits.POUNDS);
        int inputOunces = checkMeasurement.get(UnitOfMeasureUnits.OUNCES);
        int maxPounds = maxValues.get(UnitOfMeasureUnits.POUNDS);
        int maxOunces = maxValues.get(UnitOfMeasureUnits.OUNCES);

        return inputPounds <= maxPounds && inputOunces <= maxOunces;
    }

    public boolean checkMin(HashMap<UnitOfMeasureUnits, Integer> checkMeasurement) {

        int minPounds = 1;
        int minOunces = 1;
        int inputPounds = checkMeasurement.get(UnitOfMeasureUnits.POUNDS);
        int inputOunces = checkMeasurement.get(UnitOfMeasureUnits.OUNCES);

        return inputPounds >= minPounds && inputOunces >= minOunces;
    }

    public HashMap<UnitOfMeasureUnits, Integer> getInputFilterFormat() {

        HashMap<UnitOfMeasureUnits, Integer> inputFilterFormat = new HashMap<>();
        HashMap<UnitOfMeasureUnits, Integer> maxValues = getMax();
        int maxPoundValue = maxValues.get(UnitOfMeasureUnits.POUNDS);
        int maxOunceValue = maxValues.get(UnitOfMeasureUnits.OUNCES);

        int poundDigits = 0;
        while (maxPoundValue > 0) {
            poundDigits++;
            maxPoundValue = maxPoundValue / 10;
        }

        int ounceDigits = 0;
        while (maxOunceValue > 0) {
            ounceDigits++;
            maxOunceValue = maxOunceValue / 10;
        }

        inputFilterFormat.put(UnitOfMeasureUnits.POUNDS, poundDigits);
        inputFilterFormat.put(UnitOfMeasureUnits.OUNCES, ounceDigits);

        return inputFilterFormat;
    }
}
