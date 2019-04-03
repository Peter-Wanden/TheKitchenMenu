package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.*;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    // Unit values as they relate to the International System of Units, or SI
    private static final int METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_GRAM = BASE_SI_UNIT_MASS;
    private static final double UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000;

    // Unit description strings
    private String typeAsString;
    private String subTypeAsString;
    private String measurementUnitOne;
    private String measurementUnitTwo;
    private String measurementUnitThree;

    // Min and max measurements
    private double minimumItemBaseSiMeasurement = UNIT_GRAM;
    private double maximumBaseSiMeasurement = MAX_MASS;
    private int minimumNumberOfItems = SINGLE_ITEM;
    private int maximumNumberOfItems = (int) (MAX_MASS / minimumItemBaseSiMeasurement);

    // Current measurements
    private int numberOfItems = SINGLE_ITEM;
    private double itemSizeInBaseSiUnits = minimumItemBaseSiMeasurement;
    private double baseSiUnits = 0.;
    private double packMeasurementInKilograms = 0;
    private double packMeasurementInGrams = 0;
    private double itemMeasurementInKilograms = 0;
    private double itemMeasurementInGrams = 0;

    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false; // false for pack, true for item measurement.

    public MetricMass(Context context) {

        Resources resources = context.getResources();
        typeAsString = resources.getString(R.string.mass);
        subTypeAsString = resources.getString(R.string.sub_type_metric_mass);
        measurementUnitOne = resources.getString(R.string.grams);
        measurementUnitTwo = resources.getString(R.string.kilograms);
        measurementUnitThree = "";
    }

    @Override
    public String getMeasurementTypeAsString() {
        return typeAsString;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_MASS;
    }

    @Override
    public String getMeasurementSubTypeAsString() {
        return subTypeAsString;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_METRIC_MASS;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean baseSiUnitsAreSet(double baseSiUnits) {

        if (baseSiUnitsAreWithinBounds(baseSiUnits)) {

            this.baseSiUnits = baseSiUnits;
            setNewPackMeasurements();
            setItemMeasurements();

            return true;

        } else {

            resetNumericValues();
        }
        return false;
    }

    private boolean baseSiUnitsAreWithinBounds(double baseSiUnits) {

        return baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(baseSiUnits) &&
                baseSiUnitsAreWithinMaxMass(baseSiUnits);
    }

    private boolean baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(double baseSiUnits) {

        return baseSiUnits >= UNIT_GRAM * numberOfItems;
    }

    private boolean baseSiUnitsAreWithinMaxMass(double basSiUnits) {

        return basSiUnits <= maximumBaseSiMeasurement;
    }

    private void setNewPackMeasurements() {

        packMeasurementInGrams = getMeasurementInGrams(baseSiUnits);
        packMeasurementInKilograms = getMeasurementInKilograms(baseSiUnits);
    }

    private double getMeasurementInKilograms(double baseSiUnits) {

        int kilograms = (int) (baseSiUnits / UNIT_KILOGRAM);

        return (double) kilograms;
    }

    private double getMeasurementInGrams(double baseSiUnits) {

        // TODO - does this need to be converted to an int?
        int grams = (int) (baseSiUnits % UNIT_KILOGRAM);

        return (double) (grams);
    }

    private void setItemMeasurements() {

        itemSizeInBaseSiUnits = baseSiUnits / numberOfItems;
        itemMeasurementInGrams = getMeasurementInGrams(itemSizeInBaseSiUnits);
        itemMeasurementInKilograms = getMeasurementInKilograms(itemSizeInBaseSiUnits);

        maximumNumberOfItems = (int) (MAX_MASS / itemSizeInBaseSiUnits);

        Log.d(TAG, "setItemMeasurements: item size in base units: " + itemSizeInBaseSiUnits);
        Log.d(TAG, "setItemMeasurements: item kg: " + itemMeasurementInKilograms + " grams: " +
                itemMeasurementInGrams);
        Log.d(TAG, "setItemMeasurements: Max number of items is now: " + maximumNumberOfItems);
    }







    @Override
    public int getNumberOfMeasurementUnits() {
        return METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public String getMeasurementUnitOneLabel() {
        return measurementUnitOne;
    }

    @Override
    public String getMeasurementUnitTwoLabel() {
        return measurementUnitTwo;
    }

    @Override
    public String getMeasurementUnitThreeLabel() {
        return measurementUnitThree;
    }

    @Override
    public boolean setNumberOfItems(int numberOfItemsInPack) {

        Log.d(TAG, "setNumberOfItems: " + numberOfItemsInPack);

        if (numberOfItemsInPackAreWithinBounds(numberOfItemsInPack)) {

            Log.d(TAG, "setNumberOfItems: number of items is within bounds");

            if (newUnitOfMeasureIsBeingInitialised()) {

                Log.d(TAG, "setNumberOfItems: New unit of measure is being initialised " +
                        "- Setting number of items in pack to: " + numberOfItemsInPack);

                this.numberOfItems = numberOfItemsInPack;

                return true;

            } else {

                if (lastMeasurementUpdated == PACK_MEASUREMENT) {

                    Log.d(TAG, "setNumberOfItems: last measurement updated was pack");

                    if (itemSizeNotLessThanSmallestUnit(numberOfItemsInPack)) {

                        setItemsInPackByAdjustingItemSize(numberOfItemsInPack);

                        return true;
                    }

                    Log.d(TAG, "setNumberOfItems: item size would be smaller than " +
                            "smallest item, aborting");


                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {

                    Log.d(TAG, "setNumberOfItems: last measurement updated was item");

                    if (itemSizeTimesNumberOfItemsDoNotExceedMaxMass(numberOfItemsInPack)) {

                        setItemsInPackByAdjustingPackSize(numberOfItemsInPack);

                        return true;

                    }
                    Log.d(TAG, "setNumberOfItems: item size will exceed max mass, " +
                            "cannot set number of items, aborting");
                    return false;
                }
            }

        }
        return false;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItemsInPack) {

        this.numberOfItems = numberOfItemsInPack;
        double newBaseSiUnitSize = itemSizeInBaseSiUnits * numberOfItemsInPack;
        boolean baseUnitsAreNowSet = baseSiUnitsAreSet(newBaseSiUnitSize);

        Log.d(TAG, "setItemsInPackByAdjustingPackSize: number of items: " + numberOfItemsInPack);
        Log.d(TAG, "setItemsInPackByAdjustingPackSize: new base unit size: " + newBaseSiUnitSize);
        Log.d(TAG, "setItemsInPackByAdjustingPackSize: base units are set: " + baseUnitsAreNowSet);
    }

    private boolean newUnitOfMeasureIsBeingInitialised() {

        return baseSiUnits == NO_INPUT;
    }

    private boolean numberOfItemsInPackAreWithinBounds(int numberOfItemsInPack) {

        boolean result = numberOfItemsInPack >= minimumNumberOfItems &&
                numberOfItemsInPack <= maximumNumberOfItems;

        Log.d(TAG, "numberOfItemsInPackAreWithinBounds: " + result);

        return result;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItemsInPack) {

        boolean result = numberOfItemsInPack / itemSizeInBaseSiUnits >= minimumItemBaseSiMeasurement;
        Log.d(TAG, "itemSizeNotLessThanSmallestUnit: " + result);

        return result;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {

        this.numberOfItems = numberOfItemsInPack;
        setItemMeasurements();
    }

    @Override
    public ProductMeasurementModel getMinAndMax() {

        ProductMeasurementModel productMeasurementModel = new ProductMeasurementModel();
        productMeasurementModel.setNumberOfItems(numberOfItems);

        if (numberOfItems > SINGLE_ITEM)
            productMeasurementModel.setMinimumMeasurementOne((int) minimumMultiPackMeasurement());
        else productMeasurementModel.setMinimumMeasurementOne((int) minimumItemBaseSiMeasurement);

        productMeasurementModel.setMinimumMeasurementTwo(0);
        productMeasurementModel.setMaximumMeasurementOne(0);
        productMeasurementModel.setMaximumMeasurementTwo((int) getMeasurementInKilograms(MAX_MASS));

        return productMeasurementModel;
    }

    private double minimumMultiPackMeasurement() {
        return minimumItemBaseSiMeasurement * numberOfItems;
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItems;
    }

    private boolean itemSizeTimesNumberOfItemsDoNotExceedMaxMass(int numberOfItemsInPack) {
        return itemSizeInBaseSiUnits * numberOfItemsInPack <= MAX_MASS;
    }

    @Override
    public int getPackMeasurementOne() {
        Log.d(TAG, "getPackMeasurementOne: " + packMeasurementInGrams);
        return (int) packMeasurementInGrams;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {

        int baseSiUnits = (int) (packMeasurementInKilograms * UNIT_KILOGRAM + packMeasurementOne);

        if (baseSiUnitsAreSet(baseSiUnits)) {

            lastMeasurementUpdated = false;

            return true;
        }

        return false;
    }

    @Override
    public int getPackMeasurementTwo() {
        Log.d(TAG, "getPackMeasurementTwo: " + packMeasurementInKilograms);
        return (int) packMeasurementInKilograms;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        int measurement = (int) (packMeasurementTwo * UNIT_KILOGRAM + packMeasurementInGrams);

        Log.d(TAG, "setPackMeasurementTwo: Add them together: " + measurement);
        boolean baseSiIsSet = baseSiUnitsAreSet(measurement);
        Log.d(TAG, "setPackMeasurementTwo: set to base Si: " + baseSiIsSet);

        if (baseSiIsSet) {

            lastMeasurementUpdated = false;

            return true;
        }

        return false;
    }

    @Override
    public int getPackMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementThree(int packMeasurementThree) {
        return false;
    }

    @Override
    public int getItemMeasurementOne() {
        return (int) this.itemMeasurementInGrams;
    }

    @Override
    public boolean setItemMeasurementOne(int itemMeasurementOne) {

        Log.d(TAG, "setItemMeasurementOne: new item measurement in g :" + itemMeasurementOne);
        Log.d(TAG, "setItemMeasurementOne: old item measurement in kg:" + itemMeasurementInKilograms);

        int measurement = (itemMeasurementOne * numberOfItems) +
                (int) (packMeasurementInKilograms * UNIT_KILOGRAM);

        Log.d(TAG, "setItemMeasurementOne: new pack base Si: " + measurement);

        boolean baseSiIsSet = baseSiUnitsAreSet(measurement);

        if (baseSiIsSet) {

            lastMeasurementUpdated = true;

            return true;
        }

        return false;
    }

    @Override
    public int getItemMeasurementTwo() {
        return (int) itemMeasurementInKilograms;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {

        Log.d(TAG, "setItemMeasurementTwo: old item measurement in g : " + itemMeasurementInGrams);
        Log.d(TAG, "setItemMeasurementTwo: new item measurement in kg: " + itemMeasurementTwo);

        int measurement = (int) (packMeasurementInGrams +
                (itemMeasurementTwo * numberOfItems * UNIT_KILOGRAM));

        Log.d(TAG, "setItemMeasurementTwo: new pack base Si: " + measurement);

        boolean baseSiIsSet = baseSiUnitsAreSet(measurement);

        if (baseSiIsSet) {

            lastMeasurementUpdated = true;

            return true;
        }

        return false;
    }

    @Override
    public int getItemMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementThree(int itemMeasurementThree) {
        return false;
    }

    @Override
    public void resetNumericValues() {

        baseSiUnits = 0;
        numberOfItems = 0;
        packMeasurementInKilograms = 0;
        packMeasurementInGrams = 0;
        itemMeasurementInKilograms = 0;
        itemMeasurementInGrams = 0;
    }

    @Override
    public int[] getInputFilterFormat() {

        int[] inputFilterFormat = new int[5];
        inputFilterFormat[GRAMS.getIntValue()] = 3;
        inputFilterFormat[KILOGRAMS.getIntValue()] = 3;

        return inputFilterFormat;
    }

    @Override
    public String toString() {
        return "MetricMass{" +
                "\ntype='" + typeAsString + '\'' +
                "\n, subType='" + subTypeAsString + '\'' +
                "\n, measurementUnitOne='" + measurementUnitOne + '\'' +
                "\n, measurementUnitTwo='" + measurementUnitTwo + '\'' +
                "\n, measurementUnitThree'" + measurementUnitThree + '\'' +
                "\n, numberOfItems=" + numberOfItems +
                "\n, baseSiUnits=" + baseSiUnits +
                "\n, packMeasurementInKilograms=" + packMeasurementInKilograms +
                "\n, packMeasurementInGrams=" + packMeasurementInGrams +
                "\n, itemMeasurementInKilograms=" + itemMeasurementInKilograms +
                "\n, itemMeasurementInGrams=" + itemMeasurementInGrams +
                '}';
    }
}