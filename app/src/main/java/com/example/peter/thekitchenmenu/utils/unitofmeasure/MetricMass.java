package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.*;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    private static final double UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000;
    private static final double UNIT_GRAM = BASE_SI_UNIT_MASS;

    private String type;
    private String subType;
    private String unitKilogram;
    private String unitGram;

    private Integer numberOfItems = 0;
    private double baseSiUnits;
    private Integer packMeasurementInKilograms = 0;
    private Integer packMeasurementInGrams = 0;
    private Integer itemMeasurementInKilograms = 0;
    private Integer itemMeasurementInGrams = 0;


    MetricMass(Context context) {

        type = context.getResources().getString(R.string.mass);
        subType = context.getResources().getString(R.string.sub_type_metric_mass);
        unitKilogram = context.getResources().getString(R.string.kilograms);
        unitGram = context.getResources().getString(R.string.grams);
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
    public String getSubTypeAsString() {
        return subType;
    }

    @Override
    public MeasurementSubType getSubType() {
        return MeasurementSubType.TYPE_METRIC_MASS;
    }

    @Override
    public List<MeasurementUnits> getUnits() {

        List<MeasurementUnits> measurementUnits = new ArrayList<>();
        measurementUnits.set(GRAMS.getIntValue(), GRAMS);
        measurementUnits.set(KILOGRAMS.getIntValue(), KILOGRAMS);

        return measurementUnits;
    }

    @Override
    public String[] getUnitsAsString() {

        String[] unitOfMeasureUnits = new String[5];
        unitOfMeasureUnits[GRAMS.getIntValue()] = unitGram;
        unitOfMeasureUnits[KILOGRAMS.getIntValue()] = unitKilogram;

        return unitOfMeasureUnits;
    }

    @Override
    public Measurement getMinAndMax() {

        Measurement measurement = new Measurement();
        measurement.setType(type);
        measurement.setMeasurementUnitOne(unitGram);
        measurement.setMeasurementUnitTwo(unitKilogram);

        double minMeasurement;
        if (numberOfItems > 0) minMeasurement = UNIT_GRAM * numberOfItems;
        else minMeasurement = UNIT_GRAM;

        measurement.setMinimumMeasurement(minMeasurement);
        measurement.setMeasurementOne(getMeasurementInGrams(MAX_MASS));
        measurement.setMeasurementTwo(getMeasurementInKilograms(MAX_MASS));

        return measurement;
    }

    private double convertToBaseSiUnits(Measurement measurementToConvert) {

        double gramsToGrams = measurementToConvert.getMeasurementOne();
        double kilogramsToGrams = measurementToConvert.getMeasurementTwo() * UNIT_KILOGRAM;

        return kilogramsToGrams + gramsToGrams;
    }

    @Override
    public boolean setNumberOfItems(int numberOfItems) {

        if (numberOfItems >= MULTI_PACK_MINIMUM_NO_OF_ITEMS &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS) {

            this.numberOfItems = numberOfItems;
            return true;
        }
        return false;
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean setBaseSiUnits(double baseSiUnits) {

        if (numberOfItems > 1) {

            if (baseSiUnits <= MAX_MASS && baseSiUnits >= UNIT_GRAM * numberOfItems) {

                this.baseSiUnits = baseSiUnits;
                setPackMeasurement();
                setItemMeasurement();

                return true;
            }
            return false;

        } else if (baseSiUnits <= MAX_MASS && baseSiUnits >= UNIT_GRAM) {

            this.baseSiUnits = baseSiUnits;
            setPackMeasurement();

            return true;
        }
        return false;
    }

    private void setPackMeasurement() {

        packMeasurementInGrams = getMeasurementInGrams(baseSiUnits);
        packMeasurementInKilograms = getMeasurementInKilograms(baseSiUnits);
    }

    private void setItemMeasurement() {

        double itemSizeInBaseUnits = baseSiUnits / numberOfItems;
        itemMeasurementInGrams = getMeasurementInGrams(itemSizeInBaseUnits);
        itemMeasurementInKilograms = getMeasurementInKilograms(itemSizeInBaseUnits);
    }

    private int getMeasurementInKilograms(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_KILOGRAM);
    }

    private int getMeasurementInGrams(double baseSiUnits) {
        return (int) (baseSiUnits % UNIT_KILOGRAM / UNIT_GRAM);
    }

    @Override
    public int[] getInputFilterFormat() {

        int[] inputFilterFormat = new int[5];
        inputFilterFormat[GRAMS.getIntValue()] = 3;
        inputFilterFormat[KILOGRAMS.getIntValue()] = 3;

        return inputFilterFormat;
    }

    @Override
    public int getPackMeasurementOne() {
        return packMeasurementInGrams;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(packMeasurementOne);
        measurement.setMeasurementTwo(packMeasurementInKilograms);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementInKilograms;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(packMeasurementInGrams);
        measurement.setMeasurementTwo(packMeasurementTwo);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getItemMeasurementOne() {
        return itemMeasurementInGrams;
    }

    @Override
    public boolean setItemMeasurementOne(int itemMeasurementOne) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(itemMeasurementOne * numberOfItems);
        measurement.setMeasurementTwo(itemMeasurementInKilograms * numberOfItems);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getItemMeasurementTwo() {
        return itemMeasurementInKilograms;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(itemMeasurementInGrams * numberOfItems);
        measurement.setMeasurementTwo(itemMeasurementTwo * numberOfItems);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }
}