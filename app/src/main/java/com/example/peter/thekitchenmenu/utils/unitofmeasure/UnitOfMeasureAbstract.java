package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public abstract class UnitOfMeasureAbstract implements UnitOfMeasure {
    private static final String TAG = "tkm-UnitOfMeasureAbstract";

    // Unit values as they relate to the International System of Units, or SI
    private int numberOfMeasurementUnits;
    private static final double UNIT_ONE = BASE_SI_UNIT_MASS;
    private static final double UNIT_TWO = BASE_SI_UNIT_MASS * 1000.;
    private static final double MINIMUM_MEASUREMENT = UNIT_ONE;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT_LAST_UPDATED = false;
    private static final boolean PRODUCT_MEASUREMENT_LAST_UPDATED = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's

    // Current measurements
    private double baseUnits = 0.;
    private int numberOfProducts = ONE_PRODUCT;
    private double productSize = 0.;
    private int packMeasurementTwo = 0;
    private double packMeasurementOne = 0.;
    private int productMeasurementTwo = 0;
    private double productMeasurementOne = 0.;


    UnitOfMeasureAbstract() {}

    @Override
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    @Override
    public int getTypeStringResourceId() {
        return 0;
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

        } else if (baseUnits == 0.) {
            this.baseUnits = 0.; // allows for a reset
            packMeasurementOne = 0.;
            packMeasurementTwo = 0;
            productMeasurementOne = 0.;
            productMeasurementTwo = 0;
        }
        return false;
    }

    private boolean baseUnitsAreWithinBounds(double baseUnits) {
        return baseUnitsWithinLowerBounds(baseUnits) &&
                baseUnitsWithUpperBounds(baseUnits);
    }

    private boolean baseUnitsWithinLowerBounds(double baseUnits) {
        return baseUnits >= MINIMUM_MEASUREMENT * numberOfProducts;
    }

    private boolean baseUnitsWithUpperBounds(double baseUnits) {
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

    private int getUnitTwoMeasurement(double baseUnits) {
        return (int) (baseUnits / UNIT_TWO);
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
                if (lastMeasurementUpdated == PACK_MEASUREMENT_LAST_UPDATED) {
                    if (productSizeNotLessThanSmallestUnit(numberOfProducts)) {
                        setNumberOfProductsByAdjustingProductSize(numberOfProducts);
                        return true;
                    }

                } else if (lastMeasurementUpdated == PRODUCT_MEASUREMENT_LAST_UPDATED) {
                    if (packSizeDoesNotExceedMaxMass(numberOfProducts)) {
                        setNumberOfProductsByAdjustingPackSize(numberOfProducts);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfProductsInPackAreWithinBounds(int numberOfProducts) {
        return numberOfProducts >= ONE_PRODUCT && numberOfProducts <= MAXIMUM_NO_OF_PRODUCTS;
    }

    private boolean productSizeNotLessThanSmallestUnit(int numberOfProducts) {
        return baseUnits / numberOfProducts >= MINIMUM_MEASUREMENT;
    }

    private void setNumberOfProductsByAdjustingProductSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        setNewProductMeasurements();
    }

    private boolean packSizeDoesNotExceedMaxMass(int numberOfProducts) {
        return productSize * numberOfProducts <= MAX_MASS;
    }

    private void setNumberOfProductsByAdjustingPackSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        baseUnitsAreSet(productSize * numberOfProducts);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {
        return 0;
    }

    @Override
    public double getPackMeasurementOne() {
        return (int) Math.floor(packMeasurementOne * 1);
    }

    @Override
    public boolean packMeasurementOneIsSet(double packMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithPackMeasurementOne(packMeasurementOne))) {
            lastMeasurementUpdated = PACK_MEASUREMENT_LAST_UPDATED;
            return true;

        } else {
            baseUnitsAreSet(baseUnitsWithPackMeasurementOne(0.));
            lastMeasurementUpdated = PACK_MEASUREMENT_LAST_UPDATED;
            return false;
        }
    }

    private double baseUnitsWithPackMeasurementOne(double packMeasurementOne) {
        return (packMeasurementTwo * UNIT_TWO) + (packMeasurementOne * UNIT_ONE);
    }

    @Override
    public double getProductMeasurementOne() {
        return Math.floor(productMeasurementOne * 1) / 1;
    }

    @Override
    public boolean productMeasurementOneIsSet(double productMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithProductMeasurementOne(productMeasurementOne))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT_LAST_UPDATED;
            return true;

        } else {
            baseUnitsAreSet(baseUnitsWithProductMeasurementOne(0.));
            lastMeasurementUpdated = PRODUCT_MEASUREMENT_LAST_UPDATED;
            return false;
        }
    }

    private double baseUnitsWithProductMeasurementOne(double productMeasurementOne) {
        return ((productMeasurementTwo * UNIT_TWO) + (productMeasurementOne * UNIT_ONE)) *
                numberOfProducts;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return 0;
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementTwo;
    }

    @Override
    public boolean packMeasurementTwoIsSet(int packMeasurementTwo) {
        if (baseUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(packMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT_LAST_UPDATED;
            return true;

        } else {
            baseUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(0));
            lastMeasurementUpdated = PACK_MEASUREMENT_LAST_UPDATED;
            return false;
        }
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
        if (baseUnitsAreSet(baseUnitsWithProductMeasurementTwo(productMeasurementTwo))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT_LAST_UPDATED;
            return true;

        } else {
            baseUnitsAreSet(baseUnitsWithProductMeasurementTwo(0));
            lastMeasurementUpdated = PRODUCT_MEASUREMENT_LAST_UPDATED;
            return false;
        }
    }

    private double baseUnitsWithProductMeasurementTwo(int productMeasurementTwo) {
        return (productMeasurementTwo * UNIT_TWO + productMeasurementOne) * numberOfProducts;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= MINIMUM_MEASUREMENT && baseUnits <= MAX_MASS);
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
