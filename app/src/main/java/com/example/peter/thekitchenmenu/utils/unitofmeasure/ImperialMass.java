package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.*;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class ImperialMass implements UnitOfMeasure {

    private static final String TAG = "ImperialMass";

    private static final double UNIT_POUND = BASE_SI_UNIT_MASS * 453.59237;
    private static final double UNIT_OUNCE = UNIT_POUND / 16;

    private String type;
    private String subType;
    private String unitOunce;
    private String unitPound;

    private Integer numberOfItems = 0;
    private double baseSiUnits;
    private Integer packMeasurementInPounds = 0;
    private Integer packMeasurementInOunces = 0;
    private Integer itemMeasurementInPounds = 0;
    private Integer itemMeasurementInOunces = 0;

    ImperialMass(Context context) {

        type = context.getResources().getString(R.string.mass);
        subType = context.getResources().getString(R.string.sub_type_imperial_mass);
        unitPound = context.getResources().getString(R.string.pounds);
        unitOunce = context.getResources().getString(R.string.ounces);
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
        return MeasurementSubType.TYPE_IMPERIAL_MASS;
    }

    @Override
    public List<MeasurementUnits> getUnits() {

        List<MeasurementUnits> measurementUnits = new ArrayList<>();
        measurementUnits.set(OUNCES.getIntValue(), MeasurementUnits.OUNCES);
        measurementUnits.set(POUNDS.getIntValue(), MeasurementUnits.POUNDS);
        return measurementUnits;
    }

    @Override
    public String[] getUnitsAsString() {

        String[] unitOfMeasureUnits = new String[5];
        unitOfMeasureUnits[POUNDS.getIntValue()] = unitPound;
        unitOfMeasureUnits[OUNCES.getIntValue()] = unitOunce;

        return unitOfMeasureUnits;
    }

    @Override
    public Measurement getMinAndMax() {

        Measurement measurement = new Measurement();
        measurement.setType(type);
        measurement.setMeasurementUnitOne(unitOunce);
        measurement.setMeasurementUnitTwo(unitPound);

        double minMeasurement;
        if (numberOfItems > 0) minMeasurement = UNIT_OUNCE * numberOfItems;
        else minMeasurement = UNIT_OUNCE;

        measurement.setMinimumMeasurement(minMeasurement);
        measurement.setMeasurementOne(getMeasurementInOunces(MAX_MASS));
        measurement.setMeasurementTwo(getMeasurementInPounds(MAX_MASS));

        return measurement;
    }

    private double convertToBaseSiUnits(Measurement measurementToConvert) {

        double ouncesToGrams = measurementToConvert.getMeasurementOne() * UNIT_OUNCE;
        double poundsToGrams = measurementToConvert.getMeasurementTwo() * UNIT_POUND;

        return poundsToGrams + ouncesToGrams;
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

            if (baseSiUnits <= MAX_MASS && baseSiUnits >= UNIT_OUNCE * numberOfItems) {

                this.baseSiUnits = baseSiUnits;
                setPackMeasurement();
                setItemMeasurement();
                packMeasurementInPounds = (int) (this.baseSiUnits / UNIT_POUND);
                packMeasurementInOunces = (int) (this.baseSiUnits % UNIT_POUND / UNIT_OUNCE);
                return true;
            }

        }
        return false;
    }

    private void setPackMeasurement() {

        packMeasurementInOunces = getMeasurementInOunces(baseSiUnits);
        packMeasurementInPounds = getMeasurementInPounds(baseSiUnits);
    }

    private void setItemMeasurement() {

        double itemSizeInBaseUnits = baseSiUnits / numberOfItems;
        itemMeasurementInOunces = getMeasurementInOunces(itemSizeInBaseUnits);
        itemMeasurementInPounds = getMeasurementInPounds(itemSizeInBaseUnits);
    }

    private int getMeasurementInOunces(double baseSiUnits) {
        return (int) (baseSiUnits % UNIT_POUND / UNIT_OUNCE);
    }

    private int getMeasurementInPounds(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_POUND);
    }

    @Override
    public int[] getInputFilterFormat() {

        int[] inputFilterFormat = new int[3];
        Measurement maxValues = getMinAndMax();
        int maxOunceValue = maxValues.getMeasurementOne();
        int maxPoundValue = maxValues.getMeasurementTwo();

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

        inputFilterFormat[MeasurementUnits.POUNDS.getIntValue()] = poundDigits;
        inputFilterFormat[MeasurementUnits.OUNCES.getIntValue()] = ounceDigits;

        return inputFilterFormat;
    }

    @Override
    public int getPackMeasurementOne() {
        return packMeasurementInOunces;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(packMeasurementOne);
        measurement.setMeasurementTwo(packMeasurementInPounds);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementInPounds;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(packMeasurementInOunces);
        measurement.setMeasurementTwo(packMeasurementTwo);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getItemMeasurementOne() {
        return itemMeasurementInOunces;
    }

    @Override
    public boolean setItemMeasurementOne(int itemMeasurementOne) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(itemMeasurementOne * numberOfItems);
        measurement.setMeasurementTwo(itemMeasurementInPounds * numberOfItems);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }

    @Override
    public int getItemMeasurementTwo() {
        return itemMeasurementInPounds;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {

        Measurement measurement = new Measurement();
        measurement.setMeasurementOne(itemMeasurementInOunces * numberOfItems);
        measurement.setMeasurementTwo(itemMeasurementInPounds * numberOfItems);

        return setBaseSiUnits(convertToBaseSiUnits(measurement));
    }
}