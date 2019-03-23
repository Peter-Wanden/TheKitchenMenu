package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.GRAMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementUnits.KILOGRAMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAX_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NO_INPUT;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    // Unit values as they relate to the International System of Units, or SI
    private static final int METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_GRAM = BASE_SI_UNIT_MASS;
    private static final double UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000;

    // Unit description strings
    private String typeAsString;
    private String subTypeAsString;
    private String unitGram;
    private String unitKilogram;

    // Min and max measurements
    private double minimumBaseSiMeasurement = UNIT_GRAM;
    private double maximumBaseSiMeasurement = MAX_MASS;
    private int minimumNumberOfItems = MULTI_PACK_MINIMUM_NO_OF_ITEMS;
    private int maximumNumberOfItems = MULTI_PACK_MAXIMUM_NO_OF_ITEMS;

    // Current measurements
    private int numberOfItems = 1;
    private double itemSizeInBaseSiUnits = minimumBaseSiMeasurement;
    private double baseSiUnits = 0.;
    private double packMeasurementInKilograms = 0;
    private double packMeasurementInGrams = 0;
    private double itemMeasurementInKilograms = 0;
    private double itemMeasurementInGrams = 0;

    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false; // false for pack, true for item measurement.

    MetricMass(Context context) {

        Resources resources = context.getResources();
        typeAsString = resources.getString(R.string.mass);
        subTypeAsString = resources.getString(R.string.sub_type_metric_mass);
        unitGram = resources.getString(R.string.grams);
        unitKilogram = resources.getString(R.string.kilograms);
    }

    @Override
    public String getTypeAsString() {
        return typeAsString;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_MASS;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_METRIC_MASS;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public String getMeasurementUnitOne() {
        return unitGram;
    }

    @Override
    public String getMeasurementUnitTwo() {
        return unitKilogram;
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

        boolean result = numberOfItemsInPack / itemSizeInBaseSiUnits >= minimumBaseSiMeasurement;
        Log.d(TAG, "itemSizeNotLessThanSmallestUnit: " + result);

        return result;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {

        this.numberOfItems = numberOfItemsInPack;
        setItemMeasurement();
    }

    @Override
    public ObservableMeasurementModel getMinAndMax() {

        ObservableMeasurementModel observableMeasurementModel = new ObservableMeasurementModel();
        observableMeasurementModel.setNumberOfItems(numberOfItems);

        if (numberOfItems >= MULTI_PACK_MINIMUM_NO_OF_ITEMS)
            observableMeasurementModel.setMinimumMeasurementOne((int) minimumMultiPackMeasurement());
        else observableMeasurementModel.setMinimumMeasurementOne((int) minimumBaseSiMeasurement);

        observableMeasurementModel.setMinimumMeasurementTwo(0);
        observableMeasurementModel.setMaximumMeasurementOne(0);
        observableMeasurementModel.setMaximumMeasurementTwo(getMeasurementInKilograms(MAX_MASS));

        return observableMeasurementModel;
    }

    private double minimumMultiPackMeasurement() {
        return minimumBaseSiMeasurement * numberOfItems;
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
    public boolean baseSiUnitsAreSet(double baseSiUnits) {

        Log.d(TAG, "baseSiUnitsAreSet: measurement received: " + baseSiUnits);
        Log.d(TAG, "baseSiUnitsAreSet: " + "minimum measurement for a pack is: "
                + minimumMultiPackMeasurement() + "max is: " + maximumBaseSiMeasurement);

        if (baseSiUnitsAreWithinBounds(baseSiUnits)) {

            this.baseSiUnits = baseSiUnits;
            setPackMeasurement();
            setItemMeasurement();

            return true;

        } else {

            resetNumericValues();
        }
        return false;
    }

    private boolean itemSizeTimesNumberOfItemsDoNotExceedMaxMass(int numberOfItemsInPack) {
        return itemSizeInBaseSiUnits * numberOfItemsInPack <= MAX_MASS;
    }

    private void setItemMeasurement() {

        itemSizeInBaseSiUnits = baseSiUnits / numberOfItems;
        itemMeasurementInGrams = getMeasurementInGrams(itemSizeInBaseSiUnits);
        itemMeasurementInKilograms = getMeasurementInKilograms(itemSizeInBaseSiUnits);

        maximumNumberOfItems = (int) (MAX_MASS / itemSizeInBaseSiUnits);

        Log.d(TAG, "setItemMeasurement: item size in base units: " + itemSizeInBaseSiUnits);
        Log.d(TAG, "setItemMeasurement: item kg: " + itemMeasurementInKilograms + " grams: " +
                itemMeasurementInGrams);
        Log.d(TAG, "setItemMeasurement: Max number of items is now: " + maximumNumberOfItems);
    }

    private boolean baseSiUnitsAreWithinBounds(double baseSiUnits) {

        double minimumPackMeasurement = minimumBaseSiMeasurement * numberOfItems;

        boolean result = baseSiUnits <= maximumBaseSiMeasurement &&
                baseSiUnits >= minimumPackMeasurement;
        Log.d(TAG, "baseSiUnitsAreWithinBounds: " + result);

        return result;
    }

    private void setPackMeasurement() {

        packMeasurementInGrams = getMeasurementInGrams(baseSiUnits);
        packMeasurementInKilograms = getMeasurementInKilograms(baseSiUnits);

        Log.d(TAG, "setPackMeasurement: Two: " + packMeasurementInKilograms +
                " One: " + packMeasurementInGrams);
    }

    private int getMeasurementInKilograms(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_KILOGRAM);
    }

    private int getMeasurementInGrams(double baseSiUnits) {
        return (int) (baseSiUnits % UNIT_KILOGRAM);
    }

    @Override
    public int getPackMeasurementOne() {
        Log.d(TAG, "getPackMeasurementOne: " + packMeasurementInGrams);
        return (int) packMeasurementInGrams;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {

        Log.d(TAG, "setPackMeasurementOne: New measurement in grams: " + packMeasurementOne);
        Log.d(TAG, "setPackMeasurementOne: Old kilograms: " + packMeasurementInKilograms);

        int measurement = (int) (packMeasurementInKilograms * UNIT_KILOGRAM + packMeasurementOne);

        Log.d(TAG, "setPackMeasurementOne: Add them together: " + measurement);
        boolean baseSiIsSet = baseSiUnitsAreSet(measurement);
        Log.d(TAG, "setPackMeasurementOne: Set to base Si: " + baseSiIsSet);

        if (baseSiIsSet) {

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

        Log.d(TAG, "setPackMeasurementTwo: new pack measurement in kg: " + packMeasurementTwo);
        Log.d(TAG, "setPackMeasurementTwo: old pack measurement in g : " + packMeasurementInGrams);

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
                "\n, unitKilogram='" + unitKilogram + '\'' +
                "\n, unitGram='" + unitGram + '\'' +
                "\n, numberOfItems=" + numberOfItems +
                "\n, baseSiUnits=" + baseSiUnits +
                "\n, packMeasurementInKilograms=" + packMeasurementInKilograms +
                "\n, packMeasurementInGrams=" + packMeasurementInGrams +
                "\n, itemMeasurementInKilograms=" + itemMeasurementInKilograms +
                "\n, itemMeasurementInGrams=" + itemMeasurementInGrams +
                '}';
    }
}