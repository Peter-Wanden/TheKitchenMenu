package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.util.Log;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MINIMUM_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NOT_YET_SET;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MINIMUM_NUMBER_OF_PRODUCTS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_NUMBER_OF_PRODUCTS;

public class ImperialVolume implements UnitOfMeasure {

    private static final String TAG = "tkm-ImperialVolume";

    // Unit values as they relate to the base SI units for this class
    private static final int NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_TWO = MINIMUM_VOLUME * 568.26125; // Pint
    private static final double UNIT_ONE = UNIT_TWO / 20; // Fluid ounce
    private static final double UNIT_ONE_DECIMAL = UNIT_ONE / 10; // One tenth fl oz
    private static final double MINIMUM_MEASUREMENT = UNIT_ONE_DECIMAL;
    private static final double MAXIMUM_MEASUREMENT = MAXIMUM_VOLUME;

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
    private int numberOfProducts = MINIMUM_NUMBER_OF_PRODUCTS;
    private int oldNumberOfProducts;
    private double productSize = MINIMUM_MEASUREMENT;
    private Integer packMeasurementTwo = 0;
    private double packMeasurementOne = 0;
    private Integer productMeasurementTwo = 0;
    private double productMeasurementOne = 0;

    ImperialVolume() {
        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.fluidOunce;
        unitTwoLabelStringResourceId = R.string.pints;
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
        return MeasurementSubtype.TYPE_IMPERIAL_VOLUME;
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
        return baseUnits <= MAXIMUM_VOLUME;
    }

    private boolean baseUnitsWithinLowerBounds(double baseUnits) {
        return baseUnits >= MINIMUM_MEASUREMENT * numberOfProducts;
    }

    private void adjustNumberOfProductsSoBaseUnitsFitWithinLowerBounds(double baseUnits) {
        this.baseUnits = baseUnits;
        numberOfProductsIsSet((int) (baseUnits / MINIMUM_MEASUREMENT));
    }

    private boolean oldNumberOfProductsLargerThanCurrentNumberOfProducts() {
        return oldNumberOfProducts > numberOfProducts;
    }

    private boolean settingOldNumberOfProductsBreaksMinimumMeasurement(double baseUnits) {
        return baseUnits / MINIMUM_MEASUREMENT < oldNumberOfProducts;
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
        double unitTwoRemainder = baseUnits % UNIT_TWO;
        return unitTwoRemainder / UNIT_ONE;
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
                if (lastMeasurementUpdated == PACK_MEASUREMENT) {
                    if (productSizeNotLessThanSmallestUnit(numberOfProducts)) {
                        setNumberOfProductsInPackByAdjustingProductSize(numberOfProducts);
                        return true;
                    }

                } else if (lastMeasurementUpdated == PRODUCT_MEASUREMENT) {
                    if (ProductSizeMultipliedByNumberOfProductsDoesNotExceedMax(numberOfProducts)) {
                        setNumberOfProductsInPackByAdjustingPackSize(numberOfProducts);
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

    private boolean productSizeNotLessThanSmallestUnit(int numberOfProducts) {
        return baseUnits / numberOfProducts >= UNIT_ONE_DECIMAL;
    }

    private void setNumberOfProductsInPackByAdjustingProductSize(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
        setNewProductMeasurements();
    }

    private boolean ProductSizeMultipliedByNumberOfProductsDoesNotExceedMax(int numberOfProducts) {
        return productSize * numberOfProducts <= MAXIMUM_VOLUME;
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
    public boolean packMeasurementOneIsSet(double packMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithNewPackMeasurementOne(packMeasurementOne))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithNewPackMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithNewPackMeasurementOne(double packMeasurementOne) {
        return (packMeasurementTwo * UNIT_TWO) + (packMeasurementOne * UNIT_ONE);
    }

    @Override
    public double getProductMeasurementOne() {
        return roundDecimal(productMeasurementOne);
    }

    @Override
    public boolean productMeasurementOneIsSet(double productMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithProductMeasurementOne(productMeasurementOne))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithProductMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithProductMeasurementOne(double productMeasurementOne) {
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
    public boolean packMeasurementTwoIsSet(int packMeasurementTwo) {
        if (baseUnitsAreSet(baseUnitsWithPackMeasurementTwo(packMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithPackMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithPackMeasurementTwo(int packMeasurementTwo) {
        return (packMeasurementTwo * UNIT_TWO) + (packMeasurementOne * UNIT_ONE);
    }

    @Override
    public int getProductMeasurementTwo() {
        return productMeasurementTwo;
    }

    @Override
    public boolean productMeasurementTwoIsSet(int productMeasurementTwo) {
        if (baseUnitsAreSet(baseUnitsWithProductMeasurementTwo(productMeasurementTwo))) {
            lastMeasurementUpdated = PRODUCT_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithProductMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithProductMeasurementTwo(int productMeasurementTwo) {
        return ((productMeasurementTwo * UNIT_TWO) + (productMeasurementOne * UNIT_ONE)) *
                numberOfProducts;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= MINIMUM_MEASUREMENT && baseUnits <= MAXIMUM_MEASUREMENT);
    }

    @Override
    public Pair[] getMeasurementUnitDigitLengthArray() {
        int maxPintValue = (int) (MAXIMUM_VOLUME / UNIT_TWO);

        int pintDigits = 0;
        while (maxPintValue > 0) {
            pintDigits++;
            maxPintValue = maxPintValue / 10;
        }

        Pair<Integer, Integer> unitOneDigitsFormat = new Pair<>(2, 1);
        Pair<Integer, Integer> unitTwoDigitsFormat = new Pair<>(pintDigits, 0);

        Pair[] digitFormats = new Pair[2];

        digitFormats[0] = unitOneDigitsFormat;
        digitFormats[1] = unitTwoDigitsFormat;

        return digitFormats;
    }

    private double roundDecimal(double valueToRound) {
        NumberFormat decimalFormat = NumberFormat.getInstance();
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        if (decimalFormat instanceof DecimalFormat)
            ((DecimalFormat) decimalFormat).applyPattern("##.#");

        return Double.parseDouble(decimalFormat.format(valueToRound));
    }
}
