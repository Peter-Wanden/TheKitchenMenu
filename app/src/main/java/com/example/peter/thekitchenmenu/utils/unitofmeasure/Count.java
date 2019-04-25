package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class Count implements UnitOfMeasure {

    private static final String TAG = "Count";

    private static final int COUNT_NUMBER_OF_MEASUREMENT_UNITS = 1;
    private static final double UNIT_COUNT = BASE_UNIT_COUNT;

    private int typeStringResourceId;
    private int subTypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    private int numberOfItems = SINGLE_ITEM;
    private double baseSiUnits = 0;

    public Count() {

        typeStringResourceId = R.string.count;
        subTypeStringResourceId = R.string.count;
        unitOneLabelStringResourceId = R.string.each;
        unitTwoLabelStringResourceId = R.string.empty_string;
    }

    @Override
    public int getTypeStringResourceId() {
        return 0;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_COUNT;
    }

    @Override
    public int getSubTypeStringResourceId() {
        return 0;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_COUNT;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return COUNT_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public int getUnitOneLabelStringResourceId() {
        return 0;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return 0;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean baseSiUnitsAreSet(double baseSiUnits) {

        if (numberOfItems > 1) {

            if (baseSiUnits <= MAXIMUM_COUNT && baseSiUnits >= UNIT_COUNT * numberOfItems) {
                this.baseSiUnits = baseSiUnits;
                return true;
            }
        }

        if (baseSiUnits > MINIMUM_COUNT && baseSiUnits < MAXIMUM_COUNT) {
            this.baseSiUnits = baseSiUnits;
            return true;
        }
        return false;
    }

    @Override
    public boolean numberOfItemsAreSet(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems > SINGLE_ITEM &&
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
    public Pair[] getInputDigitsFilter() {

        Pair<Integer, Integer> unitOneDigitsFilter = new Pair<>(MULTI_PACK_MAXIMUM_NO_OF_ITEMS, 0);
        Pair<Integer, Integer> unitTwoDigitsFilter = new Pair<>(0, 0);

        Pair[] digitFilters = new Pair[2];
        digitFilters[0] = unitOneDigitsFilter;
        digitFilters[1] = unitTwoDigitsFilter;

        return digitFilters;
    }

    @Override
    public double getPackMeasurementOne() {
        return 0;
    }

    @Override
    public boolean packMeasurementOneIsSet(double packMeasurementOne) {
        return false;
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
    public double getItemMeasurementOne() {
        return 0;
    }

    @Override
    public boolean itemMeasurementOneIsSet(double itemMeasurementOne) {
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
        return new int[0];
    }
}
