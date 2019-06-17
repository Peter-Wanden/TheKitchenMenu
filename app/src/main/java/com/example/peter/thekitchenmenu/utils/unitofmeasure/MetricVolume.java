package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;
import com.example.peter.thekitchenmenu.R;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class MetricVolume implements UnitOfMeasure {

    private static final String TAG = "tkm-MetricVolume";

    // Unit values as they relate to the International System of Units, or SI
    private static final int METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_MILLILITRE = MINIMUM_VOLUME;
    private static final double UNIT_LITRE = UNIT_MILLILITRE * 1000.;
    private double minimumProductSize = UNIT_MILLILITRE;

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
    private int numberOfProductsInPack = MINIMUM_NUMBER_OF_PRODUCTS;
    private double singleProductSizeInBaseUnits = minimumProductSize;
    private double baseUnits = 0.;
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
            this.baseUnits = 0.; // allows for a reset
            packMeasurementInMillilitres = 0.;
            itemMeasurementInMillilitres = 0.;
            packMeasurementInLitres = 0;
            itemMeasurementInLitres = 0;
        }
        return false;
    }

    private boolean baseUnitsAreWithinBounds(double baseUnits) {
        return baseUnitsDoNotMakeSingleProductSmallerThanSmallestUnit(baseUnits) &&
                baseUnitsAreWithinMaxVolume(baseUnits);
    }

    private boolean baseUnitsDoNotMakeSingleProductSmallerThanSmallestUnit(double baseUnits) {
        return baseUnits >= UNIT_MILLILITRE * numberOfProductsInPack;
    }

    private boolean baseUnitsAreWithinMaxVolume(double baseUnits) {
        return baseUnits <= MAXIMUM_VOLUME;
    }

    private void setNewPackMeasurements() {
        packMeasurementInMillilitres = getMeasurementInMillilitres(baseUnits);
        packMeasurementInLitres = getMeasurementInLitres(baseUnits);
    }

    private void setNewItemMeasurements() {
        singleProductSizeInBaseUnits = baseUnits / numberOfProductsInPack;
        itemMeasurementInMillilitres = getMeasurementInMillilitres(singleProductSizeInBaseUnits);
        itemMeasurementInLitres = getMeasurementInLitres(singleProductSizeInBaseUnits);
    }

    private double getMeasurementInMillilitres(double baseUnits) {
        return baseUnits % UNIT_LITRE;
    }

    private int getMeasurementInLitres(double baseUnits) {
        return (int) (baseUnits / UNIT_LITRE);
    }

    @Override
    public int getNumberOfProducts() {
        return numberOfProductsInPack;
    }

    @Override
    public boolean numberOfProductsIsSet(int numberOfProducts) {
        if (numberOfItemsInPackAreWithinBounds(numberOfProducts)) {

            if (baseUnits == NOT_YET_SET) {
                this.numberOfProductsInPack = numberOfProducts;
                return true;

            } else {
                if (lastMeasurementUpdated == PACK_MEASUREMENT) {
                    if (itemSizeNotLessThanSmallestUnit(numberOfProducts)) {
                        setItemsInPackByAdjustingItemSize(numberOfProducts);
                        return true;
                    }

                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {
                    if (itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(numberOfProducts)) {
                        setItemsInPackByAdjustingPackSize(numberOfProducts);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfItemsInPackAreWithinBounds(int numberOfItems) {
        return numberOfItems >= MINIMUM_NUMBER_OF_PRODUCTS && numberOfItems <= MAXIMUM_NUMBER_OF_PRODUCTS;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {
        return baseUnits / numberOfItems >= minimumProductSize;
    }

    private void setItemsInPackByAdjustingItemSize(int numberOfItemsInPack) {
        this.numberOfProductsInPack = numberOfItemsInPack;
        setNewItemMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoNotExceedMaxMass(int numberOfItems) {
        return singleProductSizeInBaseUnits * numberOfItems <= MAXIMUM_VOLUME;
    }

    private void setItemsInPackByAdjustingPackSize(int numberOfItems) {
        this.numberOfProductsInPack = numberOfItems;
        baseUnitsAreSet(singleProductSizeInBaseUnits * numberOfItems);
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
    public boolean packMeasurementOneIsSet(double newPackMeasurementOne) {
        if (baseUnitsAreSet(baseUnitsWithNewPackMeasurementOne(newPackMeasurementOne))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithNewPackMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithNewPackMeasurementOne(double newPackMeasurementOne) {
        return packMeasurementInLitres * UNIT_LITRE + newPackMeasurementOne;
    }

    @Override
    public double getProductMeasurementOne() {
        return Math.floor(itemMeasurementInMillilitres * 1) / 1;
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
        return (itemMeasurementInLitres * UNIT_LITRE + itemMeasurementOne) * numberOfProductsInPack;
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
    public boolean packMeasurementTwoIsSet(int newPackMeasurementTwo) {
        if (baseUnitsAreSet(baseUnitsWithPackMeasurementTwo(newPackMeasurementTwo))) {
            lastMeasurementUpdated = PACK_MEASUREMENT;
            return true;

        } else baseUnitsAreSet(baseUnitsWithPackMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithPackMeasurementTwo(int packMeasurementTwo) {
        return packMeasurementTwo * UNIT_LITRE + packMeasurementInMillilitres;
    }

    @Override
    public int getProductMeasurementTwo() {
        return itemMeasurementInLitres;
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
        return (itemMeasurementTwo * UNIT_LITRE + itemMeasurementInMillilitres) * numberOfProductsInPack;
    }

    @Override
    public boolean isValidMeasurement() {
        return (baseUnits >= minimumProductSize && baseUnits <= MAXIMUM_VOLUME);
    }

    @Override
    public Pair[] getMeasurementUnitDigitLengthArray() {

        int maxLitreValue = (int) (MAXIMUM_VOLUME / UNIT_LITRE);

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
