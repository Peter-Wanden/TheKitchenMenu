package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class UnitOfMeasureImpl implements UnitOfMeasure {

    int numberOfMeasurementUnits;
    double maximumMeasurement;
    double minimumMeasurement;
    double unitTwo;
    double unitOne;
    double unitOneDecimal;
    double smallestUnit;

    MeasurementSubtype subtype;

    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean PRODUCT_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    int typeStringResourceId;
    int subtypeStringResourceId;
    int unitOneLabelStringResourceId;
    int unitTwoLabelStringResourceId;

    private double baseUnits;
    private int numberOfProducts = UnitOfMeasureConstants.MINIMUM_NUMBER_OF_PRODUCTS;
    private int oldNumberOfProducts;
    private double productSize = smallestUnit;
    private int packMeasurementTwo;
    private double packMeasurementOne;
    private int productMeasurementTwo;
    private double productMeasurementOne;

    UnitOfMeasureImpl() {}

    @Override
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    @Override
    public int getTypeStringResourceId() {
        return typeStringResourceId;
    }

    @Override
    public MeasurementSubtype getMeasurementSubtype() {
        return subtype;
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

        } else if (baseUnits == 0.) { // allows for a reset
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
                } // What if its smaller??
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
        return baseUnits <= (maximumMeasurement / smallestUnit) * smallestUnit;
    }

    private boolean baseUnitsWithinLowerBounds(double baseUnits) {
        return baseUnits >= minimumMeasurement * numberOfProducts;
    }

    private boolean oldNumberOfProductsLargerThanCurrentNumberOfProducts() {
        return oldNumberOfProducts > numberOfProducts;
    }

    private boolean settingOldNumberOfProductsBreaksMinimumMeasurement(double baseUnits) {
        return baseUnits / minimumMeasurement < oldNumberOfProducts;
    }

    private void adjustNumberOfProductsSoBaseUnitsFitWithinLowerBounds(double baseUnits) {
        this.baseUnits = baseUnits;
        numberOfProductsIsSet((int) (baseUnits / minimumMeasurement));
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
        double unitTwoInBaseUnits = getUnitTwoMeasurement(baseUnits) * unitTwo;
        double unitOneInBaseUnits = baseUnits - unitTwoInBaseUnits;
        return unitOneInBaseUnits / unitOne;
    }

    private int getUnitTwoMeasurement(double baseUnits) {
        return (int) (baseUnits / unitTwo);
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

    private boolean productSizeNotLessThanSmallestUnit(int numberOfProducts) {
        return baseUnits / numberOfProducts >= smallestUnit;
    }

    private void setNumberOfProductsInPackByAdjustingProductSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        setNewProductMeasurements();
    }

    private boolean productSizeMultipliedByNumberOfProductsDoesNotExceedMaximum(
            int numberOfProducts) {
        return productSize * numberOfProducts <= maximumMeasurement;
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
        return (packMeasurementTwo * unitTwo) + (newPackMeasurementOne * unitOne);
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
        return ((productMeasurementTwo * unitTwo) + (productMeasurementOne * unitOne)) *
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
        if (baseUnitsAreSet(baseUnitsWithNewPackMeasurementTwo(newPackMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithNewPackMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithNewPackMeasurementTwo(int newPackMeasurementTwo) {
        return (newPackMeasurementTwo * unitTwo) + (packMeasurementOne * unitOne);
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
        return ((newProductMeasurementTwo * unitTwo) + (productMeasurementOne * unitOne)) *
                numberOfProducts;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= minimumMeasurement &&
                baseUnits <= maximumMeasurement &&
                numberOfProducts > 0);
    }

    @Override
    public Pair[] getMeasurementUnitsDigitWidths() {

        // Calculates the max digit width of unit two
        int maximumUnitTwoValue = (int) (maximumMeasurement / unitTwo);
        int unitTwoDigits = 0;
        while (maximumUnitTwoValue > 0) {
            unitTwoDigits++;
            maximumUnitTwoValue = maximumUnitTwoValue / 10;
        }
        Pair<Integer, Integer> unitTwoDigitsWidth = new Pair<>(unitTwoDigits, 0);

        // Calculates the max digit width of unit one
        int maximumUnitOneValue = (int) (unitTwo / unitOne) -1;
        int unitOneDigitsBeforeDecimal = 0;
        while (maximumUnitOneValue > 0) {
            unitOneDigitsBeforeDecimal ++;
            maximumUnitOneValue = maximumUnitOneValue / 10;
        }
        int unitsOneDigitsAfterDecimal = 0;
        boolean isUnitAfterDecimal = unitOneDecimal > 0;
        if (isUnitAfterDecimal) unitsOneDigitsAfterDecimal = 1;
        Pair<Integer, Integer> unitOneDigitsWidth = new Pair<>
                (unitOneDigitsBeforeDecimal, unitsOneDigitsAfterDecimal);


        Pair[] measurementUnitDigitWidths = new Pair[2];
        measurementUnitDigitWidths[0] = unitOneDigitsWidth;
        measurementUnitDigitWidths[1] = unitTwoDigitsWidth;

        return measurementUnitDigitWidths;
    }

    private double roundDecimal(double valueToRound) {

        if (this instanceof ImperialMass || this instanceof ImperialVolume) {

            NumberFormat decimalFormat = NumberFormat.getInstance();
            decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

            if (decimalFormat instanceof DecimalFormat)
                ((DecimalFormat) decimalFormat).applyPattern("##.#");

            return Double.parseDouble(decimalFormat.format(valueToRound));
        } else {
            return (int) Math.floor(valueToRound * 1);
        }
    }

    @Override
    public String toString() {
        return "UnitOfMeasureImpl{" +
                "\nnumberOfMeasurementUnits=" + numberOfMeasurementUnits +
                ", \nmaximumMeasurement=" + maximumMeasurement +
                ", \nminimumMeasurement=" + minimumMeasurement +
                ", \nunitTwo=" + unitTwo +
                ", \nunitOne=" + unitOne +
                ", \nunitOneDecimal=" + unitOneDecimal +
                ", \nsmallestUnit=" + smallestUnit +
                ", \nsubtype=" + subtype +
                ", \nlastMeasurementUpdated=" + lastMeasurementUpdated +
                ", \ntypeStringResourceId=" + typeStringResourceId +
                ", \nsubtypeStringResourceId=" + subtypeStringResourceId +
                ", \nunitOneLabelStringResourceId=" + unitOneLabelStringResourceId +
                ", \nunitTwoLabelStringResourceId=" + unitTwoLabelStringResourceId +
                ", \nbaseUnits=" + baseUnits +
                ", \nnumberOfProducts=" + numberOfProducts +
                ", \noldNumberOfProducts=" + oldNumberOfProducts +
                ", \nproductSize=" + productSize +
                ", \npackMeasurementTwo=" + packMeasurementTwo +
                ", \npackMeasurementOne=" + packMeasurementOne +
                ", \nproductMeasurementTwo=" + productMeasurementTwo +
                ", \nproductMeasurementOne=" + productMeasurementOne +
                '}';
    }
}
