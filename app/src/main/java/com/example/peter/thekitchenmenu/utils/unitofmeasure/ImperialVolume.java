package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.util.Log;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAX_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NOT_YET_SET;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.ONE_PRODUCT;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_NO_OF_PRODUCTS;

public class ImperialVolume implements UnitOfMeasure {

    private static final String TAG = "tkm-ImperialVolume";

    private static final int NUMBER_OF_MEASUREMENT_UNITS = 2;

    private static final double UNIT_TWO = BASE_SI_UNIT_VOLUME * 568.26125; // Pint
    private static final double UNIT_ONE = UNIT_TWO / 20; // Fluid ounce
    private static final double UNIT_ONE_DECIMAL = UNIT_ONE / 10; // One tenth fl oz
    private static final double MAXIMUM_MEASUREMENT = MAX_VOLUME;
    private static final double MINIMUM_MEASUREMENT = UNIT_ONE_DECIMAL;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subtypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    private double baseUnits = 0;
    private int numberOfProducts = ONE_PRODUCT;
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
        if (baseUnitsAreWithinBounds(baseUnits)) {
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

    private boolean baseUnitsAreWithinBounds(double baseUnits) {
        return baseUnitsAreWithinLowerBounds(baseUnits) &&
                baseUnitsAreWithinUpperBounds(baseUnits);
    }

    private boolean baseUnitsAreWithinLowerBounds(double baseUnits) {
        return baseUnits >= MINIMUM_MEASUREMENT * numberOfProducts;
    }

    private boolean baseUnitsAreWithinUpperBounds(double baseSiUnits) {
        return baseSiUnits <= MAX_VOLUME;
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
        int unitOneValueWithoutDecimal = (int) (unitTwoRemainder / UNIT_ONE);
        double unitOneRemainder = unitTwoRemainder % UNIT_ONE;
        double unitOneDecimalValue = (unitOneRemainder / UNIT_ONE_DECIMAL) / 10;
        return unitOneValueWithoutDecimal + unitOneDecimalValue;
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
        return numberOfItems >= ONE_PRODUCT && numberOfItems <= MAXIMUM_NO_OF_PRODUCTS;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {
        return baseUnits / numberOfItems >= UNIT_ONE_DECIMAL;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItems) {
        this.numberOfProducts = numberOfItems;
        setNewProductMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(int numberOfItems) {
        return productSize * numberOfItems <= MAX_VOLUME;
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
        return roundDecimal(packMeasurementOne);
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
        return (packMeasurementTwo * UNIT_TWO) + (packMeasurementOne * UNIT_ONE);
    }

    @Override
    public double getProductMeasurementOne() {
        return roundDecimal(productMeasurementOne);
    }

    @Override
    public boolean productMeasurementOneIsSet(double productMeasurementOne) {
        if (baseUnitsAreSet(baseSiUnitsWithItemMeasurementOne(productMeasurementOne))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseSiUnitsWithItemMeasurementOne(0.));
        return false;
    }

    private double baseSiUnitsWithItemMeasurementOne(double itemMeasurementOne) {
        return ((productMeasurementTwo * UNIT_TWO) + (itemMeasurementOne * UNIT_ONE)) *
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
        if (baseUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(packMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(0));
        return false;
    }

    private double baseSiUnitsWithPackMeasurementTwo(int packMeasurementTwo) {
        return (packMeasurementTwo * UNIT_TWO) + (packMeasurementOne * UNIT_ONE);
    }

    @Override
    public int getProductMeasurementTwo() {
        return productMeasurementTwo;
    }

    @Override
    public boolean productMeasurementTwoIsSet(int productMeasurementTwo) {
        if (baseUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(productMeasurementTwo))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(0));
        return false;
    }

    private double baseSiUnitsWithItemMeasurementTwo(int itemMeasurementTwo) {
        return ((itemMeasurementTwo * UNIT_TWO) + (productMeasurementOne *
                UNIT_ONE)) * numberOfProducts;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= UNIT_ONE_DECIMAL && baseUnits <= MAX_VOLUME);
    }

    @Override
    public Pair[] getMeasurementUnitNumberTypeArray() {
        int maxPintValue = (int) (MAX_VOLUME / UNIT_TWO);
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
