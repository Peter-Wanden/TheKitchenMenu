package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class ImperialMass implements UnitOfMeasure {

    private static final String TAG = "tkm-ImperialMass";

    private static final int IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_POUND = MINIMUM_MASS * 453.59237;
    private static final double UNIT_OUNCE = UNIT_POUND / 16;
    private static final double UNIT_OUNCE_DECIMAL = UNIT_OUNCE / 10;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subtypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    private Integer numberOfProductsInPack = MINIMUM_NUMBER_OF_PRODUCTS;
    private double singleProductSizeInBaseUnits = UNIT_OUNCE_DECIMAL;
    private double baseUnits = 0;
    private Integer packMeasurementInPounds = 0;
    private double packMeasurementInOunces = 0;
    private Integer itemMeasurementInPounds = 0;
    private double itemMeasurementInOunces = 0;

    ImperialMass() {
        typeStringResourceId = R.string.mass;
        subtypeStringResourceId = R.string.sub_type_imperial_mass;
        unitOneLabelStringResourceId = R.string.ounces;
        unitTwoLabelStringResourceId = R.string.pounds;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS;
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
        if (baseUnitsAreWithinBounds(baseUnits)) {
            this.baseUnits = baseUnits;
            setNewPackMeasurements();
            setNewItemMeasurements();
            return true;

        } else if (baseUnits == 0.) {
            this.baseUnits = 0.;
            packMeasurementInOunces = 0.;
            itemMeasurementInOunces = 0.;
            packMeasurementInPounds = 0;
            itemMeasurementInPounds = 0;
        }
        return false;
    }

    private boolean baseUnitsAreWithinBounds(double baseUnits) {
        return baseUnitsDoNotMakeItemSmallerThanSmallestUnit(baseUnits) &&
                baseUnitsAreWithinMaxMass(baseUnits);
    }

    private boolean baseUnitsDoNotMakeItemSmallerThanSmallestUnit(double baseUnits) {
        return baseUnits >= UNIT_OUNCE_DECIMAL * numberOfProductsInPack;
    }

    private boolean baseUnitsAreWithinMaxMass(double baseUnits) {
        // TODO - this equation should be extracted as CLASS_MAX_MASS
        //  then all classes can just return baseUnits <= CLASS_MAX_MASS
        return baseUnits <= (MAXIMUM_MASS / UNIT_OUNCE_DECIMAL) * UNIT_OUNCE_DECIMAL;
    }

    private void setNewPackMeasurements() {
        packMeasurementInPounds = getMeasurementInPounds(baseUnits);
        packMeasurementInOunces = getMeasurementInOunces(baseUnits);
    }

    private void setNewItemMeasurements() {
        singleProductSizeInBaseUnits = baseUnits / numberOfProductsInPack;
        itemMeasurementInOunces = getMeasurementInOunces(singleProductSizeInBaseUnits);
        itemMeasurementInPounds = getMeasurementInPounds(singleProductSizeInBaseUnits);
    }

    private double getMeasurementInOunces(double baseUnits) {
        double poundsInBaseUnits = getMeasurementInPounds(baseUnits) * UNIT_POUND;
        double ouncesInBaseUnits = baseUnits - poundsInBaseUnits;

        return ouncesInBaseUnits / UNIT_OUNCE;
    }

    private int getMeasurementInPounds(double baseUnits) {
        return (int) (baseUnits / UNIT_POUND);
    }

    @Override
    public int getNumberOfProducts() {
        return numberOfProductsInPack;
    }

    @Override
    public boolean numberOfProductsIsSet(int numberOfProducts) {
        if (numberOfProductsInPackAreWithinBounds(numberOfProducts)) {

            if (baseUnits == NOT_YET_SET) {
                this.numberOfProductsInPack = numberOfProducts;
                return true;

            } else {
                if (lastMeasurementUpdated == PACK_MEASUREMENT) {

                    if (singlePackSizeNotLessThanSmallestUnit(numberOfProducts)) {
                        setNumberOfProductsInPackByAdjustingProductSize(numberOfProducts);
                        return true;
                    }

                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {

                    if (singleProductSizeMultipliedByNumberOfProductsDoNotExceedMaxMass(numberOfProducts)) {
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

    private boolean singlePackSizeNotLessThanSmallestUnit(int numberOfPacks) {
        return baseUnits / numberOfPacks >= UNIT_OUNCE_DECIMAL;
    }

    private void setNumberOfProductsInPackByAdjustingProductSize(int numberOfProducts) {
        this.numberOfProductsInPack = numberOfProducts;
        setNewItemMeasurements();
    }

    private boolean singleProductSizeMultipliedByNumberOfProductsDoNotExceedMaxMass(
            int numberOfProducts) {
        return singleProductSizeInBaseUnits * numberOfProducts <= MAXIMUM_MASS;
    }

    private void setNumberOfProductsInPackByAdjustingPackSize(int numberOfProducts) {
        this.numberOfProductsInPack = numberOfProducts;
        baseUnitsAreSet(singleProductSizeInBaseUnits * numberOfProducts);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {
        return unitOneLabelStringResourceId;
    }

    @Override
    public double getPackMeasurementOne() {
        return roundDecimal(packMeasurementInOunces);
    }

    @Override
    public boolean packMeasurementOneIsSet(double newPackMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithPackMeasurementOne(newPackMeasurementOne))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithPackMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithPackMeasurementOne(double packMeasurementOne) {
        return (packMeasurementInPounds * UNIT_POUND) + (packMeasurementOne * UNIT_OUNCE);
    }

    @Override
    public double getProductMeasurementOne() {
        return roundDecimal(itemMeasurementInOunces);
    }

    @Override
    public boolean productMeasurementOneIsSet(double newProductMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithItemMeasurementOne(newProductMeasurementOne))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithItemMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithItemMeasurementOne(double itemMeasurementOne) {
        return ((itemMeasurementInPounds * UNIT_POUND) + (itemMeasurementOne * UNIT_OUNCE)) *
                numberOfProductsInPack;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return unitTwoLabelStringResourceId;
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementInPounds;
    }

    @Override
    public boolean packMeasurementTwoIsSet(int newPackMeasurementTwo) {
        if (baseUnitsAreSet(baseUnitsWithPackMeasurementTwo(newPackMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithPackMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithPackMeasurementTwo(int packMeasurementTwo) {
        return (packMeasurementTwo * UNIT_POUND) + (packMeasurementInOunces * UNIT_OUNCE);
    }

    @Override
    public int getProductMeasurementTwo() {
        return itemMeasurementInPounds;
    }

    @Override
    public boolean productMeasurementTwoIsSet(int newProductMeasurementTwo) {
        if (baseUnitsAreSet(baseUnitsWithItemMeasurementTwo(newProductMeasurementTwo))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithItemMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithItemMeasurementTwo(int itemMeasurementTwo) {
        return ((itemMeasurementTwo * UNIT_POUND) + (itemMeasurementInOunces * UNIT_OUNCE)) *
                numberOfProductsInPack;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= UNIT_OUNCE_DECIMAL && baseUnits <= MAXIMUM_MASS);
    }

    @Override
    public Pair[] getMeasurementUnitDigitLengthArray() {
        int maxPoundValue = (int) (MAXIMUM_MASS / UNIT_POUND);

        int poundDigits = 0;
        while (maxPoundValue > 0) {
            poundDigits++;
            maxPoundValue = maxPoundValue / 10;
        }

        Pair<Integer, Integer> unitOneDigitsFormat = new Pair<>(2, 1);
        Pair<Integer, Integer> unitTwoDigitsFormat = new Pair<>(poundDigits, 0);

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