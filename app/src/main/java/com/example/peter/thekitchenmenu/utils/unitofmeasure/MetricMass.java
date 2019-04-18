package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    // Unit values as they relate to the International System of Units, or SI
    private static final int METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_GRAM = BASE_SI_UNIT_MASS;
    private static final double UNIT_KILOGRAM = BASE_SI_UNIT_MASS * 1000.;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subTypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;
    private int unitThreeLabelStringResourceId;

    // Min and max measurements
    private double minimumItemSize = UNIT_GRAM;
    private double maximumBaseSiMeasurement = MAX_MASS;

    // Current measurements
    private int numberOfItems = SINGLE_ITEM;
    private double itemSizeInBaseSiUnits = minimumItemSize;
    private double baseSiUnits = 0.;
    private int packMeasurementInKilograms = 0;
    private double packMeasurementInGrams = 0.;
    private int itemMeasurementInKilograms = 0;
    private double itemMeasurementInGrams = 0.;


    public MetricMass() {

        typeStringResourceId = R.string.mass;
        subTypeStringResourceId = R.string.sub_type_metric_mass;
        unitOneLabelStringResourceId = R.string.grams;
        unitTwoLabelStringResourceId = R.string.kilograms;
        unitThreeLabelStringResourceId = R.string.empty_string;
    }

    @Override
    public int getNumberOfMeasurementUnits() {

        return METRIC_MASS_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public int getTypeStringResourceId() {

        return typeStringResourceId;
    }

    @Override
    public MeasurementType getMeasurementType() {

        return MeasurementType.TYPE_MASS;
    }

    @Override
    public int getSubTypeStringResourceId() {

        return subTypeStringResourceId;
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
            setNewItemMeasurements();

            return true;

        } else if (baseSiUnits == 0.) {

            this.baseSiUnits = 0.; // allows for a reset

            packMeasurementInGrams = 0.;
            itemMeasurementInGrams = 0.;
            packMeasurementInKilograms = 0;
            itemMeasurementInKilograms = 0;
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

    private void setNewItemMeasurements() {

        itemSizeInBaseSiUnits = baseSiUnits / numberOfItems;
        itemMeasurementInGrams = getMeasurementInGrams(itemSizeInBaseSiUnits);
        itemMeasurementInKilograms = getMeasurementInKilograms(itemSizeInBaseSiUnits);
    }

    private double getMeasurementInGrams(double baseSiUnits) {

        return baseSiUnits % UNIT_KILOGRAM;
    }

    private int getMeasurementInKilograms(double baseSiUnits) {

        return (int) (baseSiUnits / UNIT_KILOGRAM);
    }

    @Override
    public int getNumberOfItems() {

        return numberOfItems;
    }

    @Override
    public boolean numberOfItemsAreSet(int numberOfItems) {

        if (numberOfItemsInPackAreWithinBounds(numberOfItems)) {

            if (baseSiUnits == NOT_YET_SET) {

                this.numberOfItems = numberOfItems;

                return true;

            } else {

                if (lastMeasurementUpdated == PACK_MEASUREMENT) {

                    if (itemSizeNotLessThanSmallestUnit(numberOfItems)) {

                        setItemsInPackByAdjustingItemSize(numberOfItems);

                        return true;
                    }

                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {

                    if (itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(numberOfItems)) {

                        setItemsInPackByAdjustingPackSize(numberOfItems);

                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfItemsInPackAreWithinBounds(int numberOfItems) {

        return numberOfItems >= SINGLE_ITEM && numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {

        return baseSiUnits / numberOfItems >= minimumItemSize;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {

        this.numberOfItems = numberOfItemsInPack;
        setNewItemMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(int numberOfItems) {

        return itemSizeInBaseSiUnits * numberOfItems <= MAX_MASS;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItems) {

        this.numberOfItems = numberOfItems;
        baseSiUnitsAreSet(itemSizeInBaseSiUnits * numberOfItems);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {

        return unitOneLabelStringResourceId;
    }

    @Override
    public double getPackMeasurementOne() {

        return (int) Math.floor(packMeasurementInGrams * 1);
    }

    @Override
    public boolean packMeasurementOneIsSet(double packMeasurementOne) {

        if (baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementOne(packMeasurementOne))) {

            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementOne(0.));

        return false;
    }

    private double baseSiUnitsWithPackMeasurementOne(double packMeasurementOne) {

        return packMeasurementInKilograms * UNIT_KILOGRAM + packMeasurementOne;
    }

    @Override
    public double getItemMeasurementOne() {

        return Math.floor(itemMeasurementInGrams * 1) / 1;
    }

    @Override
    public boolean itemMeasurementOneIsSet(double itemMeasurementOne) {

        if (baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementOne(itemMeasurementOne))) {

            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementOne(0.));

        return false;
    }

    private double baseSiUnitsWithItemMeasurementOne(double itemMeasurementOne) {

        return (itemMeasurementInKilograms * UNIT_KILOGRAM + itemMeasurementOne) * numberOfItems;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {

        return unitTwoLabelStringResourceId;
    }

    @Override
    public int getPackMeasurementTwo() {

        return packMeasurementInKilograms;
    }

    @Override
    public boolean packMeasurementTwoIsSet(int packMeasurementTwo) {

        if (baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(packMeasurementTwo))) {

            lastMeasurementUpdated = PACK_MEASUREMENT;

            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(0));

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
    public boolean itemMeasurementTwoIsSet(int itemMeasurementTwo) {

        if (baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(itemMeasurementTwo))) {

            lastMeasurementUpdated = ITEM_MEASUREMENT;

            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(0));

        return false;
    }

    private double baseSiUnitsWithItemMeasurementTwo(int itemMeasurementTwo) {

        return (itemMeasurementTwo * UNIT_KILOGRAM + itemMeasurementInGrams) * numberOfItems;
    }

    @Override
    public int getUnitThreeLabelStringResourceId() {

        return unitThreeLabelStringResourceId;
    }

    @Override
    public int getPackMeasurementThree() {
        return 0;
    }

    @Override
    public boolean packMeasurementThreeIsSet(int packMeasurementThree) {
        return false;
    }

    @Override
    public int getItemMeasurementThree() {
        return 0;
    }

    @Override
    public boolean itemMeasurementThreeIsSet(int itemMeasurementThree) {
        return false;
    }

    @Override
    public int[] getMeasurementError() {

        return new int[]{

                getTypeStringResourceId(),
                (int) (maximumBaseSiMeasurement / UNIT_KILOGRAM),
                getUnitTwoLabelStringResourceId(),
                (int) (minimumItemSize),
                getUnitOneLabelStringResourceId()};
    }

    @Override
    public Pair[] getInputDigitsFilter() {

        int maxKilogramValue = (int) (MAX_MASS / UNIT_KILOGRAM);

        int kilogramDigits = 0;
        while (maxKilogramValue > 0) {
            kilogramDigits++;
            maxKilogramValue = maxKilogramValue / 10;
        }

        Pair<Integer, Integer> unitOneDigitsFormat = new Pair<>(3, 0);
        Pair<Integer, Integer> unitTwoDigitsFormat = new Pair<>(kilogramDigits, 0);
        Pair<Integer, Integer> unitThreeDigitsFormat = new Pair<>(0, 0);

        Pair[] digitsFormat = new Pair[3];

        digitsFormat[0] = unitOneDigitsFormat;
        digitsFormat[1] = unitTwoDigitsFormat;
        digitsFormat[2] = unitThreeDigitsFormat;

        return digitsFormat;
    }
}