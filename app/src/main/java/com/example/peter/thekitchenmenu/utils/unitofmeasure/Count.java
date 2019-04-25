package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class Count implements UnitOfMeasure {

    private static final String TAG = "Count";

    // Unit values
    private static final int COUNT_NUMBER_OF_MEASUREMENT_UNITS = 1;
    private static final double UNIT_COUNT = BASE_UNIT_COUNT;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subTypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    // Min and max measurements
    private double minimumItemSize = UNIT_COUNT;
    private double maximumMeasurement = MAX_COUNT;

    private int numberOfItems = SINGLE_ITEM;
    private double itemSizeInCountUnits = minimumItemSize;
    private double baseUnits = 0.;
    private double packMeasurementCount = 0;
    private double itemMeasurementCount = 0;

    public Count() {

        typeStringResourceId = R.string.count;
        subTypeStringResourceId = R.string.count;
        unitOneLabelStringResourceId = R.string.each;
        unitTwoLabelStringResourceId = R.string.empty_string;
    }

    @Override
    public int getNumberOfMeasurementUnits() {

        return COUNT_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public int getTypeStringResourceId() {

        return typeStringResourceId;
    }

    @Override
    public MeasurementType getMeasurementType() {

        return MeasurementType.TYPE_COUNT;
    }

    @Override
    public int getSubTypeStringResourceId() {

        return subTypeStringResourceId;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {

        return MeasurementSubType.TYPE_COUNT;
    }

    @Override
    public double getBaseSiUnits() {
        return baseUnits;
    }

    @Override
    public boolean baseSiUnitsAreSet(double newCount) {

        if (newCountIsWithinBounds(newCount)) {

            this.baseUnits = newCount;
            packMeasurementCount = baseUnits;
            itemMeasurementCount = baseUnits / numberOfItems;

            return true;

        } else if (newCount == 0.) {

            this.baseUnits = 0.; // allows for a reset

            packMeasurementCount = 0;
            itemMeasurementCount = 0;
        }

        return false;
    }

    private boolean newCountIsWithinBounds(double newCount) {

        return newCountDoesNotMakeItemSmallerThanSmallestCount(newCount) &&
                newCountIsWithinMaxCount(newCount);
    }

    private boolean newCountDoesNotMakeItemSmallerThanSmallestCount(double newCount) {

        return newCount >= UNIT_COUNT * numberOfItems;
    }

    private boolean newCountIsWithinMaxCount(double newCount) {

        return newCount <= maximumMeasurement;
    }

    @Override
    public int getNumberOfItems() {

        return numberOfItems;
    }

    @Override
    public boolean numberOfItemsAreSet(int numberOfItems) {

        if (numberOfItemsInPackAreWithinBounds(numberOfItems)) {

            if (baseUnits == NOT_YET_SET) {

                this.numberOfItems = numberOfItems;

                return true;

            } else {

                if (lastMeasurementUpdated == PACK_MEASUREMENT) {

                    if (itemSizeNotLessThanSmallestUnit(numberOfItems)) {

                        setItemsInPackByAdjustingItemSize(numberOfItems);

                        return true;
                    }

                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {

                    if (itemSizeMultipliedByNumberOfItemsDoNotExceedMaxCount(numberOfItems)) {

                        setItemsInPackByAdjustingPackSize(numberOfItems);

                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfItemsInPackAreWithinBounds(int numberOfItems) {

        return numberOfItems >= SINGLE_ITEM && numberOfItems <= MAX_COUNT;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {

        return baseUnits / numberOfItems >= minimumItemSize;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {

        this.numberOfItems = numberOfItemsInPack;
        setNewItemMeasurements();
    }

    private void setNewItemMeasurements() {

        itemSizeInCountUnits = baseUnits / numberOfItems;
        itemMeasurementCount = itemSizeInCountUnits;
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxCount(int numberOfItems) {

        return itemSizeInCountUnits * numberOfItems <= MAX_COUNT;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItems) {

        this.numberOfItems = numberOfItems;
        baseSiUnitsAreSet(itemSizeInCountUnits * numberOfItems);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {

        return unitOneLabelStringResourceId;
    }

    @Override
    public double getPackMeasurementOne() {

        return packMeasurementCount;
    }

    @Override
    public boolean packMeasurementOneIsSet(double packMeasurementOne) {

        return baseSiUnitsAreSet(packMeasurementOne);
    }

    @Override
    public double getItemMeasurementOne() {

        return itemMeasurementCount;
    }

    @Override
    public boolean itemMeasurementOneIsSet(double itemMeasurementOne) {

        return baseSiUnitsAreSet(itemMeasurementOne * numberOfItems);
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return R.string.empty_string;
    }

    @Override
    public int getPackMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean packMeasurementTwoIsSet(int packMeasurementTwo) {
        return false;
    }

    @Override
    public int getItemMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean itemMeasurementTwoIsSet(int itemMeasurementTwo) {
        return false;
    }

    @Override
    public int[] getMeasurementError() {

        return new int[]{

                getTypeStringResourceId(),
                MAX_COUNT,
                getUnitTwoLabelStringResourceId(),
                (int) (minimumItemSize),
                getUnitOneLabelStringResourceId()};
    }

    @Override
    public Pair[] getInputDigitsFilter() {

        Pair<Integer, Integer> unitOneDigitsFilter = new Pair<>(MULTI_PACK_MAXIMUM_NO_OF_ITEMS, 0);
        Pair<Integer, Integer> unitTwoDigitsFilter = new Pair<>(0, 0);

        Pair[] digitFilters = new Pair[2];
        digitFilters[0] = unitOneDigitsFilter;
        digitFilters[1] = unitTwoDigitsFilter;

        return digitFilters;
    }
}
