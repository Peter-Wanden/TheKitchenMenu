package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricMass implements UnitOfMeasure {

    private static final String TAG = "MetricMass";

    // Unit values as they relate to the International System of Units, or SI
    private static final int NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_ONE = BASE_SI_UNIT_MASS;
    private static final double UNIT_ONE_DECIMAL = 0;
    private static final double UNIT_TWO = BASE_SI_UNIT_MASS * 1000.;
    private static final double MINIMUM_MEASUREMENT = UNIT_ONE;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean PRODUCT_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subtypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    // Current measurements
    private double baseUnits = 0;
    private int numberOfProducts = ONE_PRODUCT;
    private double productSize = MINIMUM_MEASUREMENT;
    private int packMeasurementTwo = 0;
    private double packMeasurementOne = 0;
    private int productMeasurementTwo = 0;
    private double productMeasurementOne = 0;


    MetricMass() {
        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_metric_mass;
        unitOneLabelStringResourceId = R.string.grams;
        unitTwoLabelStringResourceId = R.string.kilograms;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public int getTypeStringResourceId() {
        return typeStringResourceId;
    }

    @Override
    public MeasurementSubtype getMeasurementSubtype() {
        return MeasurementSubtype.TYPE_METRIC_MASS;
    }

    @Override
    public double getBaseUnits() {
        return baseUnits;
    }

    @Override
    public boolean baseUnitsAreSet(double baseUnits) {
        if (baseUnitsAreWithinBounds(baseUnits)) {
            this.baseUnits = baseUnits;
            setNewPackMeasurements();
            setNewProductMeasurements();
            return true;

        } else if (baseUnits == 0.) { // allows for a reset
            this.baseUnits = 0.;
            packMeasurementOne = 0.;
            packMeasurementTwo = 0;
            productMeasurementOne = 0.;
            productMeasurementTwo = 0;
        }
        return false;
    }

    private boolean baseUnitsAreWithinBounds(double baseUnits) {
        return baseUnitsAreWithinLowerBounds(baseUnits) &&
                baseUnitsAreWithUpperBounds(baseUnits);
    }

    private boolean baseUnitsAreWithinLowerBounds(double baseUnits) {
        return baseUnits >= MINIMUM_MEASUREMENT * numberOfProducts;
    }

    private boolean baseUnitsAreWithUpperBounds(double baseUnits) {
        return baseUnits <= MAX_MASS;
    }

    private void setNewPackMeasurements() {
        packMeasurementOne = getUnitOneMeasurement(baseUnits);
        packMeasurementTwo = getUnitTwoMeasurement(baseUnits);
    }

    private void setNewProductMeasurements() {
        productSize = baseUnits / numberOfProducts;
        productMeasurementOne = getUnitOneMeasurement(productSize);
        productMeasurementTwo = getUnitTwoMeasurement(productSize);
    }

    private double getUnitOneMeasurement(double baseUnits) {
        return baseUnits % UNIT_TWO;
    }

    private int getUnitTwoMeasurement(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_TWO);
    }

    @Override
    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    @Override
    public boolean numberOfProductsIsSet(int numberOfItems) {
        if (numberOfItemsInPackAreWithinBounds(numberOfItems)) {

            if (baseUnits == NOT_YET_SET) {
                this.numberOfProducts = numberOfItems;
                return true;

            } else {
                if (lastMeasurementUpdated == PACK_MEASUREMENT) {
                    if (itemSizeNotLessThanSmallestUnit(numberOfItems)) {
                        setItemsInPackByAdjustingItemSize(numberOfItems);
                        return true;
                    }

                } else if (lastMeasurementUpdated == PRODUCT_MEASUREMENT) {
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
        return numberOfItems >= ONE_PRODUCT && numberOfItems <= MAXIMUM_NO_OF_PRODUCTS;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {
        return baseUnits / numberOfItems >= UNIT_ONE;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {
        this.numberOfProducts = numberOfItemsInPack;
        setNewProductMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(int numberOfItems) {
        return productSize * numberOfItems <= MAX_MASS;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItems) {
        this.numberOfProducts = numberOfItems;
        baseUnitsAreSet(productSize * numberOfItems);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {
        return unitOneLabelStringResourceId;
    }

    @Override
    public double getPackMeasurementOne() {
        return (int) Math.floor(packMeasurementOne * 1);
    }

    @Override
    public boolean packMeasurementOneIsSet(double packMeasurementOne) {
        if (baseUnitsAreSet(baseSiUnitsWithPackMeasurementOne(packMeasurementOne))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseSiUnitsWithPackMeasurementOne(0.));
        return false;
    }

    private double baseSiUnitsWithPackMeasurementOne(double packMeasurementOne) {
        return packMeasurementTwo * UNIT_TWO + packMeasurementOne;
    }

    @Override
    public double getProductMeasurementOne() {
        return Math.floor(productMeasurementOne * 1) / 1;
    }

    @Override
    public boolean productMeasurementOneIsSet(double productMeasurementOne) {
        if (baseUnitsAreSet(baseSiUnitsWithItemMeasurementOne(productMeasurementOne))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseSiUnitsWithItemMeasurementOne(0.));
        return false;
    }

    private double baseSiUnitsWithItemMeasurementOne(double itemMeasurementOne) {
        return (productMeasurementTwo * UNIT_TWO + itemMeasurementOne) * numberOfProducts;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return unitTwoLabelStringResourceId;
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementTwo;
    }

    @Override
    public boolean packMeasurementTwoIsSet(int packMeasurementTwo) {
        if (baseUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(packMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(0));
        return false;
    }

    private double baseSiUnitsWithPackMeasurementTwo(int packMeasurementTwo) {
        return packMeasurementTwo * UNIT_TWO + packMeasurementOne;
    }

    @Override
    public int getProductMeasurementTwo() {
        return productMeasurementTwo;
    }

    @Override
    public boolean productMeasurementTwoIsSet(int productMeasurementTwo) {
        if (baseUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(productMeasurementTwo))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(0));
        return false;
    }

    private double baseSiUnitsWithItemMeasurementTwo(int itemMeasurementTwo) {
        return (itemMeasurementTwo * UNIT_TWO + productMeasurementOne) * numberOfProducts;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= UNIT_ONE && baseUnits <= MAX_MASS);
    }

    @Override
    public Pair[] getMeasurementUnitNumberTypeArray() {

        int maxKilogramValue = (int) (MAX_MASS / UNIT_TWO);

        int kilogramDigits = 0;
        while (maxKilogramValue > 0) {
            kilogramDigits++;
            maxKilogramValue = maxKilogramValue / 10;
        }

        Pair<Integer, Integer> unitOneDigitsFormat = new Pair<>(3, 0);
        Pair<Integer, Integer> unitTwoDigitsFormat = new Pair<>(kilogramDigits, 0);

        Pair[] digitsFormat = new Pair[2];
        digitsFormat[0] = unitOneDigitsFormat;
        digitsFormat[1] = unitTwoDigitsFormat;

        return digitsFormat;
    }
}