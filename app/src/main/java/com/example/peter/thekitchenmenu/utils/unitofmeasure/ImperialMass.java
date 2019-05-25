package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class ImperialMass implements UnitOfMeasure {

    private static final String TAG = "ImperialMass";

    private static final int IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_POUND = BASE_SI_UNIT_MASS * 453.59237;
    private static final double UNIT_OUNCE = UNIT_POUND / 16;
    private static final double UNIT_OUNCE_DECIMAL = UNIT_OUNCE / 10;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subTypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    private Integer numberOfItems = SINGLE_ITEM;
    private double itemSizeInBaseSiUnits = UNIT_OUNCE_DECIMAL;
    private double baseSiUnits = 0;
    private Integer packMeasurementInPounds = 0;
    private double packMeasurementInOunces = 0;
    private Integer itemMeasurementInPounds = 0;
    private double itemMeasurementInOunces = 0;

    ImperialMass() {
        typeStringResourceId = R.string.mass;
        subTypeStringResourceId = R.string.sub_type_imperial_mass;
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
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_IMPERIAL_MASS;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean baseSiUnitsAreSet(double baseSiUnits) {
        if (baseSiUnitsAreWithinBounds(baseSiUnits)) {
            this.baseSiUnits = baseSiUnits;
            setNewPackMeasurements();
            setNewItemMeasurements();
            return true;

        } else if (baseSiUnits == 0.) {
            this.baseSiUnits = 0.;
            packMeasurementInOunces = 0.;
            itemMeasurementInOunces = 0.;
            packMeasurementInPounds = 0;
            itemMeasurementInPounds = 0;
        }
        return false;
    }

    private boolean baseSiUnitsAreWithinBounds(double baseSiUnits) {
        return baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(baseSiUnits) &&
                baseSiUnitsAreWithinMaxMass(baseSiUnits);
    }

    private boolean baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(double baseSiUnits) {
        return baseSiUnits >= UNIT_OUNCE_DECIMAL * numberOfItems;
    }

    private boolean baseSiUnitsAreWithinMaxMass(double baseSiUnits) {
        // TODO - this equation should be extracted as CLASS_MAX_MASS
        //  then all classes can just return baseSiUnits <= CLASS_MAX_MASS
        return baseSiUnits <= (MAX_MASS / UNIT_OUNCE_DECIMAL) * UNIT_OUNCE_DECIMAL;
    }

    private void setNewPackMeasurements() {
        packMeasurementInPounds = getMeasurementInPounds(baseSiUnits);
        packMeasurementInOunces = getMeasurementInOunces(baseSiUnits);
    }

    private void setNewItemMeasurements() {
        itemSizeInBaseSiUnits = baseSiUnits / numberOfItems;
        itemMeasurementInOunces = getMeasurementInOunces(itemSizeInBaseSiUnits);
        itemMeasurementInPounds = getMeasurementInPounds(itemSizeInBaseSiUnits);
    }

    private double getMeasurementInOunces(double baseSiUnits) {
        double poundsInBasSi = getMeasurementInPounds(baseSiUnits) * UNIT_POUND;
        double ouncesInBaseSi = baseSiUnits - poundsInBasSi;

        return ouncesInBaseSi / UNIT_OUNCE;
    }

    private int getMeasurementInPounds(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_POUND);
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public boolean numberOfItemsAreSet(int numberOfItems) {
        if (numberOfItemsInPackAreWithinBounds(numberOfItems)) {

            if (baseSiUnits == NOT_YET_SET) {
                this.numberOfItems = numberOfItems;
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
        return numberOfItems >= SINGLE_ITEM && numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {
        return baseSiUnits / numberOfItems >= UNIT_OUNCE_DECIMAL;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        setNewItemMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(int numberOfItems) {
        return itemSizeInBaseSiUnits * numberOfItems <= MAX_MASS;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        baseSiUnitsAreSet(itemSizeInBaseSiUnits * numberOfItems);
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
    public boolean packMeasurementOneIsSet(double packMeasurementOne) {
        if (baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementOne(packMeasurementOne))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementOne(0.));
        return false;
    }

    private double baseSiUnitsWithPackMeasurementOne(double packMeasurementOne) {
        return (packMeasurementInPounds * UNIT_POUND) + (packMeasurementOne * UNIT_OUNCE);
    }

    @Override
    public double getItemMeasurementOne() {
        return roundDecimal(itemMeasurementInOunces);
    }

    @Override
    public boolean itemMeasurementOneIsSet(double itemMeasurementOne) {
        if (baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementOne(itemMeasurementOne))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementOne(0.));
        return false;
    }

    private double baseSiUnitsWithItemMeasurementOne(double itemMeasurementOne) {
        return ((itemMeasurementInPounds * UNIT_POUND) + (itemMeasurementOne * UNIT_OUNCE)) *
                numberOfItems;
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
    public boolean packMeasurementTwoIsSet(int packMeasurementTwo) {
        if (baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(packMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithPackMeasurementTwo(0));
        return false;
    }

    private double baseSiUnitsWithPackMeasurementTwo(int packMeasurementTwo) {
        return (packMeasurementTwo * UNIT_POUND) + (packMeasurementInOunces * UNIT_OUNCE);
    }

    @Override
    public int getItemMeasurementTwo() {
        return itemMeasurementInPounds;
    }

    @Override
    public boolean itemMeasurementTwoIsSet(int itemMeasurementTwo) {
        if (baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(itemMeasurementTwo))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else baseSiUnitsAreSet(baseSiUnitsWithItemMeasurementTwo(0));
        return false;
    }

    private double baseSiUnitsWithItemMeasurementTwo(int itemMeasurementTwo) {
        return ((itemMeasurementTwo * UNIT_POUND) + (itemMeasurementInOunces * UNIT_OUNCE)) *
                numberOfItems;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseSiUnits >= UNIT_OUNCE_DECIMAL && baseSiUnits <= MAX_MASS);
    }

    @Override
    public Pair[] getMeasurementUnitNumberTypeArray() {
        int maxPoundValue = (int) (MAX_MASS / UNIT_POUND);

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