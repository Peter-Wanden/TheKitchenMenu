package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;
import com.example.peter.thekitchenmenu.R;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricVolume implements UnitOfMeasure {

    private static final String TAG = "MetricVolume";

    // Unit values as they relate to the International System of Units, or SI
    private static final int METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_MILLILITRE = BASE_SI_UNIT_VOLUME;
    private static final double UNIT_LITRE = UNIT_MILLILITRE * 1000.;
    private double minimumItemSize = UNIT_MILLILITRE;

    // Keeps track of the last updated measurement
    private static final boolean PACK_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    // Unit description string resource ID's
    private int typeStringResourceId;
    private int subtypeStringResourceId;
    private int unitOneLabelStringResourceId;
    private int unitTwoLabelStringResourceId;

    // Current measurements
    private int numberOfItems = ONE_PRODUCT;
    private double itemSizeInBaseSiUnits = minimumItemSize;
    private double baseSiUnits = 0.;
    private int packMeasurementInLitres = 0;
    private double packMeasurementInMillilitres = 0.;
    private int itemMeasurementInLitres = 0;
    private double itemMeasurementInMillilitres = 0.;

    MetricVolume() {
        typeStringResourceId = R.string.volume;
        subtypeStringResourceId = R.string.sub_type_metric_volume;
        unitOneLabelStringResourceId = R.string.millilitres;
        unitTwoLabelStringResourceId = R.string.litres;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public int getTypeStringResourceId() {
        return typeStringResourceId;
    }

    @Override
    public MeasurementSubtype getMeasurementSubtype() {
        return MeasurementSubtype.TYPE_METRIC_VOLUME;
    }

    @Override
    public double getBaseUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean baseUnitsAreSet(double baseSiUnits) {
        if (baseSiUnitsAreWithinBounds(baseSiUnits)) {
            this.baseSiUnits = baseSiUnits;
            setNewPackMeasurements();
            setNewItemMeasurements();
            return true;

        } else if (baseSiUnits == 0.) {
            this.baseSiUnits = 0.; // allows for a reset
            packMeasurementInMillilitres = 0.;
            itemMeasurementInMillilitres = 0.;
            packMeasurementInLitres = 0;
            itemMeasurementInLitres = 0;
        }
        return false;
    }

    private boolean baseSiUnitsAreWithinBounds(double baseSiUnits) {
        return baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(baseSiUnits) &&
                baseSiUnitsAreWithinMaxVolume(baseSiUnits);
    }

    private boolean baseSiUnitsDoNotMakeItemSmallerThanSmallestUnit(double baseSiUnits) {
        return baseSiUnits >= UNIT_MILLILITRE * numberOfItems;
    }

    private boolean baseSiUnitsAreWithinMaxVolume(double basSiUnits) {
        return basSiUnits <= MAX_VOLUME;
    }

    private void setNewPackMeasurements() {
        packMeasurementInMillilitres = getMeasurementInMillilitres(baseSiUnits);
        packMeasurementInLitres = getMeasurementInLitres(baseSiUnits);
    }

    private void setNewItemMeasurements() {
        itemSizeInBaseSiUnits = baseSiUnits / numberOfItems;
        itemMeasurementInMillilitres = getMeasurementInMillilitres(itemSizeInBaseSiUnits);
        itemMeasurementInLitres = getMeasurementInLitres(itemSizeInBaseSiUnits);
    }

    private double getMeasurementInMillilitres(double baseSiUnits) {
        return baseSiUnits % UNIT_LITRE;
    }

    private int getMeasurementInLitres(double baseSiUnits) {
        return (int) (baseSiUnits / UNIT_LITRE);
    }

    @Override
    public int getNumberOfProducts() {
        return numberOfItems;
    }

    @Override
    public boolean numberOfProductsIsSet(int numberOfItems) {
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
        return numberOfItems >= ONE_PRODUCT && numberOfItems <= MAXIMUM_NO_OF_PRODUCTS;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {
        return baseSiUnits / numberOfItems >= minimumItemSize;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {
        this.numberOfItems = numberOfItemsInPack;
        setNewItemMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(int numberOfItems) {
        return itemSizeInBaseSiUnits * numberOfItems <= MAX_VOLUME;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        baseUnitsAreSet(itemSizeInBaseSiUnits * numberOfItems);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {
        return unitOneLabelStringResourceId;
    }

    @Override
    public double getPackMeasurementOne() {
        return (int) Math.floor(packMeasurementInMillilitres *1);
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
        return packMeasurementInLitres * UNIT_LITRE + packMeasurementOne;
    }

    @Override
    public double getProductMeasurementOne() {
        return Math.floor(itemMeasurementInMillilitres * 1) / 1;
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
        return (itemMeasurementInLitres * UNIT_LITRE + itemMeasurementOne) * numberOfItems;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return unitTwoLabelStringResourceId;
    }

    @Override
    public int getPackMeasurementTwo() {
        return packMeasurementInLitres;
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
        return packMeasurementTwo * UNIT_LITRE + packMeasurementInMillilitres;
    }

    @Override
    public int getProductMeasurementTwo() {
        return itemMeasurementInLitres;
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
        return (itemMeasurementTwo * UNIT_LITRE + itemMeasurementInMillilitres) * numberOfItems;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseSiUnits >= minimumItemSize && baseSiUnits <= MAX_VOLUME);
    }

    @Override
    public Pair[] getMeasurementUnitNumberTypeArray() {

        int maxLitreValue = (int) (MAX_VOLUME / UNIT_LITRE);

        int litreDigits = 0;
        while (maxLitreValue > 0) {
            litreDigits++;
            maxLitreValue = maxLitreValue / 10;
        }

        Pair<Integer, Integer> unitOneDigitsFormat = new Pair<>(3, 0);
        Pair<Integer, Integer> unitTwoDigitsFormat = new Pair<>(litreDigits, 0);

        Pair[] digitsFormat = new Pair[2];
        digitsFormat[0] = unitOneDigitsFormat;
        digitsFormat[1] = unitTwoDigitsFormat;

        return digitsFormat;
    }
}
