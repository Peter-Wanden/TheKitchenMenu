package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.util.Log;

import com.example.peter.thekitchenmenu.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import androidx.core.util.Pair;

public class ImperialMass implements UnitOfMeasure {

    private static final String TAG = "tkm-ImperialMass";

    private static final int NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double MAXIMUM_MEASUREMENT = UnitOfMeasureConstants.MAXIMUM_MASS;
    private static final double MINIMUM_MEASUREMENT = UnitOfMeasureConstants.MINIMUM_MASS;
    private static final double UNIT_TWO = MINIMUM_MEASUREMENT * 453.59237; //1 pound
    private static final double UNIT_ONE = UNIT_TWO / 16; // 1 ounce
    private static final double UNIT_ONE_DECIMAL = UNIT_ONE / 10; // ounces / 10
    private static final double SMALLEST_UNIT = UNIT_ONE_DECIMAL;


    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean PRODUCT_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subtypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    // Measurement defaults
    private double baseUnits = 0;
    private int numberOfProducts = UnitOfMeasureConstants.MINIMUM_NUMBER_OF_PRODUCTS;
    private int oldNumberOfProducts;
    private double productSize = SMALLEST_UNIT;
    private int packMeasurementTwo = 0;
    private double packMeasurementOne = 0;
    private int productMeasurementTwo = 0;
    private double productMeasurementOne = 0;

    ImperialMass() {
        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_imperial_mass;
        unitOneLabelStringResourceId = R.string.ounces;
        unitTwoLabelStringResourceId = R.string.pounds;
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
        return MeasurementSubtype.TYPE_IMPERIAL_MASS;
    }

    @Override
    public double getBaseUnits() {
        return baseUnits;
    }

    @Override
    public boolean baseUnitsAreSet(double baseUnits) {
        if (baseUnitsWithinBounds(baseUnits)) {
            this.baseUnits = baseUnits;
            setNewPackMeasurements();
            setNewProductMeasurements();
            return true;

        } else if (baseUnits == 0.) {
            this.baseUnits = 0.;
            packMeasurementOne = 0.;
            packMeasurementTwo = 0;
            productMeasurementOne = 0.;
            productMeasurementTwo = 0;
        }
        return false;
    }

    private boolean baseUnitsWithinBounds(double baseUnits) {
        if (baseUnits == 0) return false;
        if (baseUnitsWithinUpperBounds(baseUnits)) {
            if (baseUnitsWithinLowerBounds(baseUnits)) {
                if (oldNumberOfProductsLargerThanCurrentNumberOfProducts()) {
                    if (settingOldNumberOfProductsBreaksMinimumMeasurement(baseUnits)) {
                        adjustNumberOfProductsSoBaseUnitsFitWithinLowerBounds(baseUnits);
                    } else {
                        this.baseUnits = baseUnits;
                        numberOfProductsIsSet(oldNumberOfProducts);
                        oldNumberOfProducts = 0;
                    }
                }
                return true;
            } else {
                oldNumberOfProducts = numberOfProducts;
                adjustNumberOfProductsSoBaseUnitsFitWithinLowerBounds(baseUnits);
                return true;
            }
        }
        return false;
    }

    private boolean baseUnitsWithinUpperBounds(double baseUnits) {
        return baseUnits <= (MAXIMUM_MEASUREMENT / SMALLEST_UNIT) * SMALLEST_UNIT;
    }

    private boolean baseUnitsWithinLowerBounds(double baseUnits) {
        return baseUnits >= MINIMUM_MEASUREMENT * numberOfProducts;
    }

    private boolean oldNumberOfProductsLargerThanCurrentNumberOfProducts() {
        return oldNumberOfProducts > numberOfProducts;
    }

    private boolean settingOldNumberOfProductsBreaksMinimumMeasurement(double baseUnits) {
        return baseUnits / MINIMUM_MEASUREMENT < oldNumberOfProducts;
    }

    private void adjustNumberOfProductsSoBaseUnitsFitWithinLowerBounds(double baseUnits) {
        this.baseUnits = baseUnits;
        numberOfProductsIsSet((int) (baseUnits / MINIMUM_MEASUREMENT));
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
        double unitTwoInBaseUnits = getUnitTwoMeasurement(baseUnits) * UNIT_TWO;
        double unitOneInBaseUnits = baseUnits - unitTwoInBaseUnits;
        return unitOneInBaseUnits / UNIT_ONE;
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
            if (baseUnits == UnitOfMeasureConstants.NOT_YET_SET) {
                this.numberOfProducts = numberOfProducts;
                return true;
            } else {
                if (lastMeasurementUpdated == PACK_MEASUREMENT) {
                    if (productSizeNotLessThanSmallestUnit(numberOfProducts)) {
                        setNumberOfProductsInPackByAdjustingProductSize(numberOfProducts);
                        return true;
                    }
                } else if (lastMeasurementUpdated == PRODUCT_MEASUREMENT) {
                    if (productSizeMultipliedByNumberOfProductsDoesNotExceedMaximum(numberOfProducts)) {
                        setNumberOfProductsInPackByAdjustingPackSize(numberOfProducts);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfProductsInPackAreWithinBounds(int numberOfProducts) {
        return numberOfProducts >= UnitOfMeasureConstants.MINIMUM_NUMBER_OF_PRODUCTS &&
                numberOfProducts <= UnitOfMeasureConstants.MAXIMUM_NUMBER_OF_PRODUCTS;
    }

    private boolean productSizeNotLessThanSmallestUnit(int numberOfPacks) {
        return baseUnits / numberOfPacks >= SMALLEST_UNIT;
    }

    private void setNumberOfProductsInPackByAdjustingProductSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        setNewProductMeasurements();
    }

    private boolean productSizeMultipliedByNumberOfProductsDoesNotExceedMaximum(
            int numberOfProducts) {
        return productSize * numberOfProducts <= MAXIMUM_MEASUREMENT;
    }

    private void setNumberOfProductsInPackByAdjustingPackSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        baseUnitsAreSet(productSize * numberOfProducts);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {
        return unitOneLabelStringResourceId;
    }

    @Override
    public double getPackMeasurementOne() {
        return roundDecimal(packMeasurementOne);
    }

    @Override
    public boolean packMeasurementOneIsSet(double newPackMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithNewPackMeasurementOne(newPackMeasurementOne))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithNewPackMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithNewPackMeasurementOne(double newPackMeasurementOne) {
        return (packMeasurementTwo * UNIT_TWO) + (newPackMeasurementOne * UNIT_ONE);
    }

    @Override
    public double getProductMeasurementOne() {
        return roundDecimal(productMeasurementOne);
    }

    @Override
    public boolean productMeasurementOneIsSet(double newProductMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithNewProductMeasurementOne(newProductMeasurementOne))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithNewProductMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithNewProductMeasurementOne(double productMeasurementOne) {
        return ((productMeasurementTwo * UNIT_TWO) + (productMeasurementOne * UNIT_ONE)) *
                numberOfProducts;
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
    public boolean packMeasurementTwoIsSet(int newPackMeasurementTwo) {
        if (baseUnitsAreSet(baseUnitsNewWithPackMeasurementTwo(newPackMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsNewWithPackMeasurementTwo(0));
        return false;
    }

    private double baseUnitsNewWithPackMeasurementTwo(int newPackMeasurementTwo) {
        return (newPackMeasurementTwo * UNIT_TWO) + (packMeasurementOne * UNIT_ONE);
    }

    @Override
    public int getProductMeasurementTwo() {
        return productMeasurementTwo;
    }

    @Override
    public boolean productMeasurementTwoIsSet(int newProductMeasurementTwo) {
        if (baseUnitsAreSet(baseUnitsWithNewProductMeasurementTwo(newProductMeasurementTwo))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithNewProductMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithNewProductMeasurementTwo(int newProductMeasurementTwo) {
        return ((newProductMeasurementTwo * UNIT_TWO) + (productMeasurementOne * UNIT_ONE)) *
                numberOfProducts;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= MINIMUM_MEASUREMENT && baseUnits <= MAXIMUM_MEASUREMENT);
    }

    @Override
    public Pair[] getMeasurementUnitsDigitWidths() {

        // Calculates the max digit width of unit two
        int maximumUnitTwoValue = (int) (MAXIMUM_MEASUREMENT / UNIT_TWO);
        int unitTwoDigits = 0;
        while (maximumUnitTwoValue > 0) {
            unitTwoDigits++;
            maximumUnitTwoValue = maximumUnitTwoValue / 10;
        }
        Pair<Integer, Integer> unitTwoDigitsWidth = new Pair<>(unitTwoDigits, 0);

        // Calculates the max digit width of unit one
        int maximumUnitOneValue = (int) (UNIT_TWO / UNIT_ONE) -1;
        int unitOneDigitsBeforeDecimal = 0;
        while (maximumUnitOneValue > 0) {
            unitOneDigitsBeforeDecimal ++;
            maximumUnitOneValue = maximumUnitOneValue / 10;
        }
        int unitsOneDigitsAfterDecimal = 0;
        boolean isUnitAfterDecimal = UNIT_ONE_DECIMAL > 0;
        if (isUnitAfterDecimal) unitsOneDigitsAfterDecimal = 1;
        Pair<Integer, Integer> unitOneDigitsWidth = new Pair<>
                (unitOneDigitsBeforeDecimal, unitsOneDigitsAfterDecimal);


        Pair[] MeasurementUnitDigitWidths = new Pair[2];
        MeasurementUnitDigitWidths[0] = unitOneDigitsWidth;
        MeasurementUnitDigitWidths[1] = unitTwoDigitsWidth;

        Log.d(TAG, "getMeasurementUnitsDigitWidths: unitOne before decimal width=" +
                unitOneDigitsBeforeDecimal + " unitOne after decimal=" + unitsOneDigitsAfterDecimal +
                " unitTwo digits width=" + unitTwoDigits);

        return MeasurementUnitDigitWidths;
    }

    private double roundDecimal(double valueToRound) {
        NumberFormat decimalFormat = NumberFormat.getInstance();
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        if (decimalFormat instanceof DecimalFormat)
            ((DecimalFormat) decimalFormat).applyPattern("##.#");

        return Double.parseDouble(decimalFormat.format(valueToRound));
    }
}