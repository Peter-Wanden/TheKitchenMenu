package com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure;

import androidx.core.util.Pair;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureAbstract.LastMeasurementUpdated.*;

public abstract class UnitOfMeasureAbstract implements UnitOfMeasure {

    enum LastMeasurementUpdated {
        TOTAL_MEASUREMENT,
        ITEM_MEASUREMENT
    }

    enum ResultStatus {
        RESULT_OK,
    }

    public static final int UNIT_ONE_WIDTH_INDEX = 0;
    public static final int UNIT_TWO_WIDTH_INDEX = 1;
    public static final int CONVERSION_FACTOR_WIDTH_INDEX = 2;

    protected MeasurementType measurementType;
    protected MeasurementSubtype subtype;
    protected int numberOfUnits;
    protected double maxMeasurement;
    protected double minMeasurement;
    protected double unitTwo;
    protected double unitOne;
    protected double unitTwoNoConversionFactor;
    protected double unitOneNoConversionFactor;
    protected double unitOneDecimal;
    protected double smallestUnit;
    protected boolean isConversionFactorEnabled;

    protected int typeStringResourceId;
    protected int subtypeStringResourceId;
    protected int unitOneLabelStringResourceId;
    protected int unitTwoLabelStringResourceId;

    private LastMeasurementUpdated lastMeasurementUpdated = TOTAL_MEASUREMENT;
    private double totalBaseUnits;
    private int numberOfItems = UnitOfMeasureConstants.MIN_NUMBER_OF_ITEMS;
    private int oldNumberOfItems;
    private double itemSize = smallestUnit;
    private int totalUnitTwo;
    private double totalUnitOne;
    private int itemUnitTwo;
    private double itemUnitOne;
    private double conversionFactor = UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;

    UnitOfMeasureAbstract() {
    }

    @Override
    public MeasurementType getMeasurementType() {
        return measurementType;
    }

    @Override
    public MeasurementSubtype getMeasurementSubtype() {
        return subtype;
    }

    @Override
    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    @Override
    public int getTypeStringResourceId() {
        return typeStringResourceId;
    }

    @Override
    public boolean isConversionFactorEnabled() {
        return isConversionFactorEnabled;
    }

    @Override
    public boolean isConversionFactorSet(double conversionFactor) {
        if (isConversionFactorEnabled) {
            if (isConversionFactorWithinBounds(conversionFactor)) {
                if (isConversionFactorPreviouslyChanged()) {
                    resetToOriginalValuesBeforeConversionFactorChanged();
                }
                return isNewConversionFactorApplied(conversionFactor);
            }
        }
        return false;
    }

    private boolean isConversionFactorWithinBounds(double conversionFactor) {
        return conversionFactor >= UnitOfMeasureConstants.MIN_CONVERSION_FACTOR &&
                conversionFactor <= UnitOfMeasureConstants.MAX_CONVERSION_FACTOR;
    }

    private boolean isConversionFactorPreviouslyChanged() {
        return conversionFactor != UnitOfMeasureConstants.DEFAULT_CONVERSION_FACTOR;
    }

    private void resetToOriginalValuesBeforeConversionFactorChanged() {
        unitOne = unitOneNoConversionFactor;
        unitTwo = unitTwoNoConversionFactor;
        isTotalBaseUnitsSet((totalUnitTwo * unitTwo) + (totalUnitOne * unitOne)
        );
    }

    private boolean isNewConversionFactorApplied(double conversionFactor) {
        this.conversionFactor = conversionFactor;
        unitOne = conversionFactor * unitOne;
        unitTwo = conversionFactor * unitTwo;
        isTotalBaseUnitsSet((totalUnitTwo * unitTwo) + (totalUnitOne * unitOne));
        // what if total base units exceed max??
        return true;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double getItemBaseUnits() {
        return totalBaseUnits / numberOfItems;
    }

    @Override
    public boolean isItemBaseUnitsSet(double itemBaseUnits) {
        if (isTotalBaseUnitsSet(itemBaseUnits * numberOfItems)) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;
        } else
            return false;
    }

    @Override
    public double getTotalBaseUnits() {
        return totalBaseUnits;
    }

    @Override
    public boolean isTotalBaseUnitsSet(double totalBaseUnits) {
        if (isBaseUnitsWithinBounds(totalBaseUnits)) {
            updateNewMeasurements(totalBaseUnits);
            return true;

        } else if (totalBaseUnits == UnitOfMeasureConstants.NOT_SET) {
            resetMeasurements();
        }
        return false;
    }

    private void updateNewMeasurements(double totalBaseUnits) {
        this.totalBaseUnits = totalBaseUnits;
        setTotalUnits();
        setItemUnits();
    }

    private void resetMeasurements() {
        this.totalBaseUnits = UnitOfMeasureConstants.NOT_SET;
        totalUnitOne = UnitOfMeasureConstants.NOT_SET;
        totalUnitTwo = UnitOfMeasureConstants.NOT_SET;
        itemUnitOne = UnitOfMeasureConstants.NOT_SET;
        itemUnitTwo = UnitOfMeasureConstants.NOT_SET;
    }

    private boolean isBaseUnitsWithinBounds(double baseUnits) {
        if (baseUnits == UnitOfMeasureConstants.NOT_SET) {
            return false;
        }
        if (isBaseUnitsWithinUpperBounds(baseUnits)) {
            if (isBaseUnitsWithinLowerBounds(baseUnits)) {
                if (isOldNumberOfItemsLargerThanCurrentNumberOfItems()) {
                    if (isSettingOldNumberOfItemsLowerThanMinimumMeasurement(baseUnits)) {
                        adjustNumberOfItemsSoBaseUnitsFitWithinLowerBounds(baseUnits);
                    } else {
                        this.totalBaseUnits = baseUnits;
                        isNumberOfItemsSet(oldNumberOfItems);
                        oldNumberOfItems = UnitOfMeasureConstants.NOT_SET;
                    }
                } // What if its smaller??
                return true;
            } else if (baseUnits > smallestUnit && !isConversionFactorEnabled) {
                oldNumberOfItems = numberOfItems;
                adjustNumberOfItemsSoBaseUnitsFitWithinLowerBounds(baseUnits);
                return true;
            } else
                return isConversionFactorEnabled;
        }
        return false;
    }

    private boolean isBaseUnitsWithinUpperBounds(double baseUnits) {
        return baseUnits <= maxMeasurement;
    }

    private boolean isBaseUnitsWithinLowerBounds(double baseUnits) {
        return baseUnits >= smallestUnit * numberOfItems;
    }

    private boolean isOldNumberOfItemsLargerThanCurrentNumberOfItems() {
        return oldNumberOfItems > numberOfItems;
    }

    private boolean isSettingOldNumberOfItemsLowerThanMinimumMeasurement(double baseUnits) {
        return baseUnits / minMeasurement < oldNumberOfItems;
    }

    private void adjustNumberOfItemsSoBaseUnitsFitWithinLowerBounds(double baseUnits) {
        this.totalBaseUnits = baseUnits;
        isNumberOfItemsSet((int) (baseUnits / minMeasurement));
    }

    private void setTotalUnits() {
        totalUnitOne = getUnitOneFromBaseUnits(totalBaseUnits);
        totalUnitTwo = getUnitTwoFromBaseUnits(totalBaseUnits);
    }

    private void setItemUnits() {
        itemSize = totalBaseUnits / numberOfItems;
        itemUnitOne = getUnitOneFromBaseUnits(itemSize);
        itemUnitTwo = getUnitTwoFromBaseUnits(itemSize);
    }

    private double getUnitOneFromBaseUnits(double baseUnits) {
        double unitTwoInBaseUnits = getUnitTwoFromBaseUnits(baseUnits) * unitTwo;
        double unitOneInBaseUnits = baseUnits - unitTwoInBaseUnits;
        return unitOneInBaseUnits / unitOne;
    }

    private int getUnitTwoFromBaseUnits(double baseUnits) {
        return (int) (baseUnits / unitTwo);
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public boolean isNumberOfItemsSet(int numberOfItems) {
        if (isNumberOfItemsWithinBounds(numberOfItems)) {
            if (totalBaseUnits == UnitOfMeasureConstants.NOT_SET) {
                this.numberOfItems = numberOfItems;
                return true;
            } else {
                if (lastMeasurementUpdated == TOTAL_MEASUREMENT) {
                    if (isNewItemSizeGreaterThanSmallestUnit(numberOfItems)) {
                        setNumberOfItemsByAdjustingItemSize(numberOfItems);
                        return true;
                    }
                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {
                    if (isNewItemSizeLessThanMaximumMeasurement(numberOfItems)) {
                        setNumberOfItemsByAdjustingTotalSize(numberOfItems);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isNumberOfItemsWithinBounds(int numberOfItems) {
        return numberOfItems >= UnitOfMeasureConstants.MIN_NUMBER_OF_ITEMS &&
                numberOfItems <= UnitOfMeasureConstants.MAX_NUMBER_OF_ITEMS;
    }

    private boolean isNewItemSizeGreaterThanSmallestUnit(int numberOfItems) {
        return totalBaseUnits / numberOfItems >= smallestUnit;
    }

    private void setNumberOfItemsByAdjustingItemSize(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        setItemUnits();
    }

    private boolean isNewItemSizeLessThanMaximumMeasurement(int numberOfItems) {
        return itemSize * numberOfItems <= maxMeasurement;
    }

    private void setNumberOfItemsByAdjustingTotalSize(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        isTotalBaseUnitsSet(itemSize * numberOfItems);
    }

    @Override
    public int getUnitOneLabelResourceId() {
        return unitOneLabelStringResourceId;
    }

    @Override
    public double getTotalUnitOne() {
        return roundDecimal(totalUnitOne);
    }

    @Override
    public boolean isTotalUnitOneSet(double totalUnitOne) {
        if (isTotalBaseUnitsSet(getBaseUnitsWithTotalUnitOne(totalUnitOne))) {
            lastMeasurementUpdated = TOTAL_MEASUREMENT;
            return true;
        }
        return false;
    }

    private double getBaseUnitsWithTotalUnitOne(double totalUnitOne) {
        return (totalUnitTwo * unitTwo) + (totalUnitOne * unitOne);
    }

    @Override
    public double getItemUnitOne() {
        return roundDecimal(itemUnitOne);
    }

    @Override
    public boolean isItemUnitOneSet(double itemUnitOne) {
        if (isTotalBaseUnitsSet(getBaseUnitsWithItemUnitOne(itemUnitOne))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;
        }
        return false;
    }

    private double getBaseUnitsWithItemUnitOne(double itemUnitOne) {
        return ((itemUnitTwo * unitTwo) + (itemUnitOne * unitOne)) * numberOfItems;
    }

    @Override
    public int getUnitTwoLabelResourceId() {
        return unitTwoLabelStringResourceId;
    }

    @Override
    public int getTotalUnitTwo() {
        return totalUnitTwo;
    }

    @Override
    public boolean isTotalUnitTwoSet(int totalUnitTwo) {
        if (isTotalBaseUnitsSet(getBaseUnitsWithTotalUnitTwo(totalUnitTwo))) {
            lastMeasurementUpdated = TOTAL_MEASUREMENT;
            return true;
        }
        return false;
    }

    private double getBaseUnitsWithTotalUnitTwo(int totalUnitTwo) {
        return (totalUnitTwo * unitTwo) + (totalUnitOne * unitOne);
    }

    @Override
    public int getItemUnitTwo() {
        return itemUnitTwo;
    }

    @Override
    public boolean isItemUnitTwoSet(int itemUnitTwo) {
        if (isTotalBaseUnitsSet(getBaseUnitsWithItemUnitTwo(itemUnitTwo))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;
        }
        return false;
    }

    private double getBaseUnitsWithItemUnitTwo(int itemUnitTwo) {
        return ((itemUnitTwo * unitTwo) + (itemUnitOne * unitOne)) * numberOfItems;
    }

    @Override
    public boolean isValidMeasurement() {
        return (totalBaseUnits >= minMeasurement &&
                totalBaseUnits <= maxMeasurement &&
                numberOfItems >= UnitOfMeasureConstants.MIN_NUMBER_OF_ITEMS &&
                isConversionFactorWithinBounds(conversionFactor));
    }

    @Override
    public double getMinUnitOne() {
        return roundDecimal(getUnitOneFromBaseUnits(smallestUnit));
    }

    @Override
    public double getMaxUnitOne() {
        return roundDecimal((maxMeasurement % unitTwo) / unitOne);
    }

    @Override
    public int getMaxUnitTwo() {
        return (int) (maxMeasurement / unitTwo);
    }

    @Override
    public Pair[] getMaxUnitDigitWidths() {

        // Calculates the max digit width of unit two
        int maxUnitTwo = getMaxUnitTwo();
        int unitTwoDigits = 0;
        while (maxUnitTwo > 0) {
            unitTwoDigits++;
            maxUnitTwo = maxUnitTwo / 10;
        }
        Pair<Integer, Integer> unitTwoMaxDigitsWidth = new Pair<>(unitTwoDigits, 0);

        // Calculates the max digit width of unit one
        int numberOfUnitOneInUnitTwo = (int) (unitTwo / unitOne) - 1;
        int unitOneDigitsBeforeDecimal = 0;
        while (numberOfUnitOneInUnitTwo > 0) {
            unitOneDigitsBeforeDecimal++;
            numberOfUnitOneInUnitTwo = numberOfUnitOneInUnitTwo / 10;
        }
        int unitsOneDigitsAfterDecimal = 0;
        boolean isUnitAfterDecimal = unitOneDecimal > 0;
        if (isUnitAfterDecimal)
            unitsOneDigitsAfterDecimal = 1;
        Pair<Integer, Integer> unitOneMaxDigitsWidth = new Pair<>
                (unitOneDigitsBeforeDecimal, unitsOneDigitsAfterDecimal);

        // Sets the digit widths for the conversion factor
        Pair<Integer, Integer> conversionFactorMaxDigitWidths = new Pair<>(1, 3);

        Pair[] unitDigitWidths = new Pair[3];
        unitDigitWidths[UNIT_ONE_WIDTH_INDEX] = unitOneMaxDigitsWidth;
        unitDigitWidths[UNIT_TWO_WIDTH_INDEX] = unitTwoMaxDigitsWidth;
        unitDigitWidths[CONVERSION_FACTOR_WIDTH_INDEX] = conversionFactorMaxDigitWidths;

        return unitDigitWidths;
    }

    protected double roundDecimal(double valueToRound) {
        NumberFormat decimalFormat = NumberFormat.getInstance();
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        if (decimalFormat instanceof DecimalFormat) {
            ((DecimalFormat) decimalFormat).applyPattern("##.#");
        }

        return Double.parseDouble(decimalFormat.format(valueToRound));
    }

    @Nonnull
    @Override
    public String toString() {
        return "UnitOfMeasureAbstract{" +
                "measurementType=" + measurementType +
                ", subtype=" + subtype +
                ", numberOfUnits=" + numberOfUnits +
                ", maxMeasurement=" + maxMeasurement +
                ", minMeasurement=" + minMeasurement +
                ", unitTwo=" + unitTwo +
                ", unitOne=" + unitOne +
                ", unitTwoNoConversionFactor=" + unitTwoNoConversionFactor +
                ", unitOneNoConversionFactor=" + unitOneNoConversionFactor +
                ", unitOneDecimal=" + unitOneDecimal +
                ", smallestUnit=" + smallestUnit +
                ", lastMeasurementUpdated=" + lastMeasurementUpdated +
                ", isConversionFactorEnabled=" + isConversionFactorEnabled +
                ", totalBaseUnits=" + totalBaseUnits +
                ", numberOfItems=" + numberOfItems +
                ", oldNumberOfItems=" + oldNumberOfItems +
                ", itemSize=" + itemSize +
                ", totalUnitTwo=" + totalUnitTwo +
                ", totalUnitOne=" + totalUnitOne +
                ", itemUnitTwo=" + itemUnitTwo +
                ", itemUnitOne=" + itemUnitOne +
                ", conversionFactor=" + conversionFactor +
                '}';
    }
}
