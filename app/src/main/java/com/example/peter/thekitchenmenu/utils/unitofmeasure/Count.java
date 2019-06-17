package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class Count implements UnitOfMeasure {

    private static final String TAG = "tkm-Count";

    // Unit values
    private static final int COUNT_NUMBER_OF_MEASUREMENT_UNITS = 1;
    private static final double UNIT_COUNT = MINIMUM_COUNT;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subtypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    private int numberOfProducts = MINIMUM_NUMBER_OF_PRODUCTS;
    private double singlePackSizeInCountUnits = UNIT_COUNT;
    private double baseUnits = 0.;
    private double packMeasurementCount = 0;
    private double itemMeasurementCount = 0;

    Count() {
        typeStringResourceId = R.string.count;
        subtypeStringResourceId = R.string.count;
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
    public MeasurementSubtype getMeasurementSubtype() {
        return MeasurementSubtype.TYPE_COUNT;
    }

    @Override
    public double getBaseUnits() {
        return baseUnits;
    }

    @Override
    public boolean baseUnitsAreSet(double baseUnits) {
        if (newCountIsWithinBounds(baseUnits)) {
            this.baseUnits = baseUnits;
            packMeasurementCount = this.baseUnits;
            itemMeasurementCount = this.baseUnits / numberOfProducts;
            return true;

        } else if (baseUnits == 0.) {
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
        return newCount >= UNIT_COUNT * numberOfProducts;
    }

    private boolean newCountIsWithinMaxCount(double newCount) {
        return newCount <= MAXIMUM_COUNT;
    }

    @Override
    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    @Override
    public boolean numberOfProductsIsSet(int numberOfProducts) {
        if (numberOfProductsInPackAreWithinBounds(numberOfProducts)) {

            if (baseUnits == NOT_YET_SET) {
                this.numberOfProducts = numberOfProducts;
                return true;

            } else {

                if (lastMeasurementUpdated == PACK_MEASUREMENT) {
                    if (singlePackSizeNotLessThanSmallestUnit(numberOfProducts)) {
                        setNumberOfProductsInPackByAdjustingPackSize(numberOfProducts);
                        return true;
                    }

                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {
                    if (singlePackSizeMultipliedByNumberOfPacksDoNotExceedMaxCount(numberOfProducts)) {
                        setProductsInPackByAdjustingPackSize(numberOfProducts);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfProductsInPackAreWithinBounds(int numberOfProducts) {
        return numberOfProducts >= MINIMUM_NUMBER_OF_PRODUCTS &&
                numberOfProducts <= MAXIMUM_NUMBER_OF_PRODUCTS;
    }

    private boolean singlePackSizeNotLessThanSmallestUnit(int numberOfProducts) {
        return baseUnits / numberOfProducts >= UNIT_COUNT;
    }

    private void setNumberOfProductsInPackByAdjustingPackSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        setNewItemMeasurements();
    }

    private void setNewItemMeasurements() {
        singlePackSizeInCountUnits = baseUnits / numberOfProducts;
        itemMeasurementCount = singlePackSizeInCountUnits;
    }

    private boolean singlePackSizeMultipliedByNumberOfPacksDoNotExceedMaxCount(int numberOfProducts) {
        return singlePackSizeInCountUnits * numberOfProducts <= MAXIMUM_COUNT;
    }

    private void setProductsInPackByAdjustingPackSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        baseUnitsAreSet(singlePackSizeInCountUnits * numberOfProducts);
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
    public boolean packMeasurementOneIsSet(double newPackMeasurementOne) {
        if (baseUnitsAreSet(newPackMeasurementOne)) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(0.);
        return false;
    }

    @Override
    public double getProductMeasurementOne() {
        return itemMeasurementCount;
    }

    @Override
    public boolean productMeasurementOneIsSet(double newProductMeasurementOne) {
        if (baseUnitsAreSet(newProductMeasurementOne * numberOfProducts)) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(0.);
        return false;
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
    public boolean packMeasurementTwoIsSet(int newPackMeasurementTwo) {
        return false;
    }

    @Override
    public int getProductMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean productMeasurementTwoIsSet(int newProductMeasurementTwo) {
        return false;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= UNIT_COUNT && baseUnits <= MAXIMUM_COUNT);
    }

    @Override
    public Pair[] getMeasurementUnitDigitLengthArray() {

        Pair<Integer, Integer> unitOneDigitsFilter = new Pair<>(MAXIMUM_NUMBER_OF_PRODUCTS, 0);
        Pair<Integer, Integer> unitTwoDigitsFilter = new Pair<>(0, 0);

        Pair[] digitFilters = new Pair[2];
        digitFilters[0] = unitOneDigitsFilter;
        digitFilters[1] = unitTwoDigitsFilter;

        return digitFilters;
    }
}
