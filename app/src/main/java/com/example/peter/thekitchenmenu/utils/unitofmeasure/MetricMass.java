package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAX_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NOT_YET_SET;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.SINGLE_ITEM;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    // Unit values as they relate to the International System of Units, or SI
    private static final int METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_GRAM = BASE_SI_UNIT_MASS;
    private static final double UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description strings
    private String typeAsString;
    private String subTypeAsString;
    private String measurementUnitOneLabel;
    private String measurementUnitTwoLabel;
    private String measurementUnitThreeLabel;

    // Min and max measurements
    private double minimumItemSize = UNIT_GRAM;
    private double maximumBaseSiMeasurement = MAX_MASS;
    private int minimumNumberOfItems = SINGLE_ITEM;
    private int maximumNumberOfItems = (int) (MAX_MASS / minimumItemSize);

    // Current measurements
    private int numberOfItems = SINGLE_ITEM;
    private double itemSizeInBaseSiUnits = minimumItemSize;
    private double baseSiUnits = 0.;
    private int packMeasurementInKilograms = 0;
    private double packMeasurementInGrams = 0;
    private int itemMeasurementInKilograms = 0;
    private double itemMeasurementInGrams = 0;


    public MetricMass(Context context) {

        Resources resources = context.getResources();
        typeAsString = resources.getString(R.string.mass);
        subTypeAsString = resources.getString(R.string.sub_type_metric_mass);
        measurementUnitOneLabel = resources.getString(R.string.grams);
        measurementUnitTwoLabel = resources.getString(R.string.kilograms);
        measurementUnitThreeLabel = "";
    }

    @Override
    public int getNumberOfMeasurementUnits() {

        return METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS;
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

    private int getMeasurementInKilograms(double baseSiUnits) {

        return (int) (baseSiUnits / UNIT_KILOGRAM);
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
    }

    @Override
    public int getNumberOfItems() {

        return numberOfItems;
    }

    @Override
    public boolean setNumberOfItems(int numberOfItemsInPack) {

        if (numberOfItemsInPackAreWithinBounds(numberOfItemsInPack)) {

            if (baseSiUnits == NOT_YET_SET) {

                this.numberOfItems = numberOfItemsInPack;

                return true;

            } else {

                if (lastMeasurementUpdated == PACK_MEASUREMENT) {

                    if (itemSizeNotLessThanSmallestUnit(numberOfItemsInPack)) {

                        setItemsInPackByAdjustingItemSize(numberOfItemsInPack);

                        return true;
                    }

                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {

                    if (itemSizeTimesNumberOfItemsDoNotExceedMaxMass(numberOfItemsInPack)) {

                        setItemsInPackByAdjustingPackSize(numberOfItemsInPack);

                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfItemsInPackAreWithinBounds(int numberOfItemsInPack) {

        return numberOfItemsInPack >= minimumNumberOfItems &&
                numberOfItemsInPack <= maximumNumberOfItems;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItemsInPack) {

        return numberOfItemsInPack / itemSizeInBaseSiUnits >= minimumItemSize;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {

        this.numberOfItems = numberOfItemsInPack;
        setItemMeasurements();
    }

    private boolean itemSizeTimesNumberOfItemsDoNotExceedMaxMass(int numberOfItemsInPack) {

        return itemSizeInBaseSiUnits * numberOfItemsInPack <= MAX_MASS;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItemsInPack) {

        this.numberOfItems = numberOfItemsInPack;
        baseSiUnitsAreSet(itemSizeInBaseSiUnits * numberOfItemsInPack);
    }

    @Override
    public String getMeasurementUnitOneLabel() {

        return measurementUnitOneLabel;
    }

    @Override
    public double getPackMeasurementOne() {

        return packMeasurementInGrams;
    }

    @Override
    public boolean setPackMeasurementOne(double packMeasurementOne) {

        if (baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementOne(packMeasurementOne))) {

            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;
        }
        return false;
    }

    private double baseSiUnitsWithPackMeasurementOne(double packMeasurementOne) {

        return packMeasurementInKilograms * UNIT_KILOGRAM + packMeasurementOne;
    }

    @Override
    public double getItemMeasurementOne() {

        return itemMeasurementInGrams;
    }

    @Override
    public boolean setItemMeasurementOne(double itemMeasurementOne) {

        if (baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementOne(itemMeasurementOne))) {

            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;
        }
        return false;
    }

    private double baseSiUnitsWithItemMeasurementOne(double itemMeasurementOne) {

        return (itemMeasurementOne * numberOfItems) + (packMeasurementInKilograms * UNIT_KILOGRAM);
    }

    @Override
    public String getMeasurementUnitTwoLabel() {

        return measurementUnitTwoLabel;
    }

    @Override
    public int getPackMeasurementTwo() {

        return packMeasurementInKilograms;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {

        if (baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(packMeasurementTwo))) {

            lastMeasurementUpdated = PACK_MEASUREMENT;

            return true;
        }
        return false;
    }

    private double baseSiUnitsWithPackMeasurementTwo(int packMeasurementTwo) {

        return packMeasurementTwo * UNIT_KILOGRAM + packMeasurementInGrams;
    }

    @Override
    public int getItemMeasurementTwo() {

        return itemMeasurementInKilograms;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {

        if (baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(itemMeasurementTwo))) {

            lastMeasurementUpdated = ITEM_MEASUREMENT;

            return true;
        }

        return false;
    }

    private double baseSiUnitsWithItemMeasurementTwo(int itemMeasurementTwo) {

        return itemMeasurementTwo * UNIT_KILOGRAM * numberOfItems;
    }

    @Override
    public String getMeasurementUnitThreeLabel() {

        return measurementUnitThreeLabel;
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
    public int getItemMeasurementThree() {

        return 0;
    }

    @Override
    public boolean setItemMeasurementThree(int itemMeasurementThree) {

        return false;
    }

    @Override
    public String[] getMeasurementError() {

        return new String[]{

                getMeasurementTypeAsString(),
                String.valueOf((int) maximumBaseSiMeasurement / UNIT_KILOGRAM),
                getMeasurementUnitTwoLabel(),
                getMeasurementUnitOneLabel(), String.valueOf((int) minimumItemSize),
                getMeasurementUnitOneLabel()};
    }

    @Override
    public Pair[] getInputDigitsFilter() {

        Pair<Integer, Integer> unitOneDigitsFilter = new Pair<>(3, 0);

        int maxKilogramValue = (int) (MAX_MASS / UNIT_KILOGRAM);
        int kilogramDigits = 0;

        while(maxKilogramValue > 0) {
            kilogramDigits ++;
            maxKilogramValue = maxKilogramValue / 10;
        }

        Pair<Integer, Integer> unitTwoDigitsFilter = new Pair<>(kilogramDigits, 0);

        Pair<Integer, Integer> unitThreeDigitsFilter = new Pair<>(0, 0);

        Pair[] digitFilters = new Pair[3];
        digitFilters[0] = unitOneDigitsFilter;
        digitFilters[1] = unitTwoDigitsFilter;
        digitFilters[2] = unitThreeDigitsFilter;

        return digitFilters;
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

    @NonNull
    @Override
    public String toString() {
        return "MetricMass{" +
                "\ntype='" + typeAsString + '\'' +
                "\n, subType='" + subTypeAsString + '\'' +
                "\n, measurementUnitOneLabel='" + measurementUnitOneLabel + '\'' +
                "\n, measurementUnitTwoLabel='" + measurementUnitTwoLabel + '\'' +
                "\n, measurementUnitThreeLabel'" + measurementUnitThreeLabel + '\'' +
                "\n, numberOfItems=" + numberOfItems +
                "\n, baseSiUnits=" + baseSiUnits +
                "\n, packMeasurementInKilograms=" + packMeasurementInKilograms +
                "\n, packMeasurementInGrams=" + packMeasurementInGrams +
                "\n, itemMeasurementInKilograms=" + itemMeasurementInKilograms +
                "\n, itemMeasurementInGrams=" + itemMeasurementInGrams +
                '}';
    }
}