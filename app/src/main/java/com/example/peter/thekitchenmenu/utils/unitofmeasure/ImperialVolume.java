package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAX_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NOT_YET_SET;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.SINGLE_ITEM;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;

public class ImperialVolume implements UnitOfMeasure {

    private static final int IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_PINT = BASE_SI_UNIT_VOLUME * 568.26125;
    private static final double UNIT_FLUID_OUNCE = UNIT_PINT / 20;
    private static final double UNIT_FLUID_OUNCE_DECIMAL = UNIT_FLUID_OUNCE / 10;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subTypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    private int numberOfItems = SINGLE_ITEM;
    private double itemSizeInBaseSiUnits = UNIT_FLUID_OUNCE_DECIMAL;
    private double baseSiUnits = 0;
    private Integer packMeasurementInPints = 0;
    private double packMeasurementInFluidOunces = 0;
    private Integer itemMeasurementInPints = 0;
    private double itemMeasurementInFluidOunces = 0;

    ImperialVolume() {
        typeStringResourceId = R.string.volume;
        subTypeStringResourceId = R.string.sub_type_imperial_volume;
        unitOneLabelStringResourceId = R.string.fluidOunce;
        unitTwoLabelStringResourceId = R.string.pints;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public int getTypeStringResourceId() {
        return typeStringResourceId;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_IMPERIAL_VOLUME;
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
            packMeasurementInFluidOunces = 0.;
            itemMeasurementInFluidOunces = 0.;
            packMeasurementInPints = 0;
            itemMeasurementInPints = 0;
        }
        return false;
    }

    private boolean baseSiUnitsAreWithinBounds(double baseSiUnits) {
        return baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(baseSiUnits) &&
                baseSiUnitsAreWithinMaxMass(baseSiUnits);
    }

    private boolean baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(double baseSiUnits) {
        return baseSiUnits >= UNIT_FLUID_OUNCE_DECIMAL * numberOfItems;
    }

    private boolean baseSiUnitsAreWithinMaxMass(double baseSiUnits) {
        return baseSiUnits <=
                (MAX_VOLUME / UNIT_FLUID_OUNCE_DECIMAL) * UNIT_FLUID_OUNCE_DECIMAL;
    }

    private void setNewPackMeasurements() {
        packMeasurementInPints = getMeasurementInPints(baseSiUnits);
        packMeasurementInFluidOunces = getMeasurementFluidOunces(baseSiUnits);
    }

    private void setNewItemMeasurements() {
        itemSizeInBaseSiUnits = baseSiUnits / numberOfItems;
        itemMeasurementInFluidOunces = getMeasurementFluidOunces(itemSizeInBaseSiUnits);
        itemMeasurementInPints = getMeasurementInPints(itemSizeInBaseSiUnits);
    }

    private double getMeasurementFluidOunces(double baseSiUnits) {
        double pintsInBasSi = getMeasurementInPints(baseSiUnits) * UNIT_PINT;
        double fluidOuncesInBaseSi = baseSiUnits - pintsInBasSi;
        return fluidOuncesInBaseSi / UNIT_FLUID_OUNCE;
    }

    private int getMeasurementInPints(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_PINT);
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
        return baseSiUnits / numberOfItems >= UNIT_FLUID_OUNCE_DECIMAL;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        setNewItemMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(int numberOfItems) {
        return itemSizeInBaseSiUnits * numberOfItems <= MAX_VOLUME;
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
        return roundDecimal(packMeasurementInFluidOunces);
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
        return (packMeasurementInPints * UNIT_PINT) + (packMeasurementOne * UNIT_FLUID_OUNCE);
    }

    @Override
    public double getItemMeasurementOne() {
        return roundDecimal(itemMeasurementInFluidOunces);
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
        return ((itemMeasurementInPints * UNIT_PINT) + (itemMeasurementOne * UNIT_FLUID_OUNCE)) *
                numberOfItems;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return unitTwoLabelStringResourceId;
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementInPints;
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
        return (packMeasurementTwo * UNIT_PINT) + (packMeasurementInFluidOunces * UNIT_FLUID_OUNCE);
    }

    @Override
    public int getItemMeasurementTwo() {
        return itemMeasurementInPints;
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
        return ((itemMeasurementTwo * UNIT_PINT) + (itemMeasurementInFluidOunces *
                UNIT_FLUID_OUNCE)) * numberOfItems;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseSiUnits >= UNIT_FLUID_OUNCE_DECIMAL && baseSiUnits <= MAX_VOLUME);
    }

    @Override
    public Pair[] getInputDigitsFilter() {
        int maxPintValue = (int) (MAX_VOLUME / UNIT_PINT);
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
