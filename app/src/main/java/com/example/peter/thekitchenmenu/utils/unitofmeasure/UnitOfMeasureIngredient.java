package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

public abstract class UnitOfMeasureIngredient implements UnitOfMeasure {

    int numberOfMeasurementUnits;
    double maximumMeasurement;
    double minimumMeasurement;
    double unitTwo;
    double unitOne;
    double unitOneDecimal;
    double smallestUnit;

    MeasurementSubtype subtype;

    private static final boolean TOTAL_MEASUREMENT = false;
    private static final boolean ITEM_MEASUREMENT = true;
    private boolean lastMeasurementUpdated = false;

    int typeStringResourceId;
    int subtypeStringResourceId;
    int unitOneLabelStringResourceId;
    int unitTwoLabelStringResourceId;

    private double totalBaseUnits;
    private int numberOfItems = UnitOfMeasureConstants.MINIMUM_NUMBER_OF_ITEMS;
    private int oldNumberOfItems;
    double itemSize = smallestUnit;
    private int totalMeasurementTwo;
    private double totalMeasurementOne;
    private int itemMeasurementTwo;
    private double itemMeasurementOne;

    UnitOfMeasureIngredient() {}

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
    public double getItemBaseUnits() {
        return totalBaseUnits / numberOfItems;
    }

    @Override
    public boolean itemBaseUnitsAreSet(double itemBaseUnits) {
        return totalBaseUnitsAreSet(itemBaseUnits * numberOfItems);
    }

    @Override
    public double getTotalBaseUnits() {
        return totalBaseUnits;
    }

    @Override
    public boolean totalBaseUnitsAreSet(double totalBaseUnits) {
        if (baseUnitsWithinBounds(totalBaseUnits)) {
            this.totalBaseUnits = totalBaseUnits;
            setNewTotalMeasurements();
            setNewItemMeasurements();
            return true;

        } else if (totalBaseUnits == 0.) { // allows for a reset
            this.totalBaseUnits = 0.;
            totalMeasurementOne = 0.;
            totalMeasurementTwo = 0;
            itemMeasurementOne = 0.;
            itemMeasurementTwo = 0;
        }
        return false;
    }

    private boolean baseUnitsWithinBounds(double baseUnits) {
        if (baseUnits == 0)
            return false;
        if (baseUnitsWithinUpperBounds(baseUnits)) {
            if (baseUnitsWithinLowerBounds(baseUnits)) {
                if (oldNumberOfItemsLargerThanCurrentNumberOfItems()) {
                    if (settingOldNumberOfItemsBreaksMinimumMeasurement(baseUnits)) {
                        adjustNumberOfItemsSoBaseUnitsFitWithinLowerBounds(baseUnits);
                    } else {
                        this.totalBaseUnits = baseUnits;
                        numberOfItemsIsSet(oldNumberOfItems);
                        oldNumberOfItems = 0;
                    }
                } // What if its smaller??
                return true;
            } else if (baseUnits > minimumMeasurement) {
                oldNumberOfItems = numberOfItems;
                adjustNumberOfItemsSoBaseUnitsFitWithinLowerBounds(baseUnits);
                return true;
            }
        }
        return false;
    }

    private boolean baseUnitsWithinUpperBounds(double baseUnits) {
        return baseUnits <= (maximumMeasurement / smallestUnit) * smallestUnit;
    }

    private boolean baseUnitsWithinLowerBounds(double baseUnits) {
        return baseUnits >= minimumMeasurement * numberOfItems;
    }

    private boolean oldNumberOfItemsLargerThanCurrentNumberOfItems() {
        return oldNumberOfItems > numberOfItems;
    }

    private boolean settingOldNumberOfItemsBreaksMinimumMeasurement(double baseUnits) {
        return baseUnits / minimumMeasurement < oldNumberOfItems;
    }

    private void adjustNumberOfItemsSoBaseUnitsFitWithinLowerBounds(double baseUnits) {
        this.totalBaseUnits = baseUnits;
        numberOfItemsIsSet((int) (baseUnits / minimumMeasurement));
    }

    private void setNewTotalMeasurements() {
        totalMeasurementOne = getUnitOneMeasurement(totalBaseUnits);
        totalMeasurementTwo = getUnitTwoMeasurement(totalBaseUnits);
    }

    private void setNewItemMeasurements() {
        itemSize = totalBaseUnits / numberOfItems;
        itemMeasurementOne = getUnitOneMeasurement(itemSize);
        itemMeasurementTwo = getUnitTwoMeasurement(itemSize);
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
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public boolean numberOfItemsIsSet(int numberOfItems) {
        if (numberOfItemsInTotalAreWithinBounds(numberOfItems)) {
            if (totalBaseUnits == UnitOfMeasureConstants.NOT_YET_SET) {
                this.numberOfItems = numberOfItems;
                return true;
            } else {
                if (lastMeasurementUpdated == TOTAL_MEASUREMENT) {
                    if (itemSizeNotLessThanSmallestUnit(numberOfItems)) {
                        setNumberOfItemsInTotalByAdjustingItemSize(numberOfItems);
                        return true;
                    }
                } else if (lastMeasurementUpdated == ITEM_MEASUREMENT) {
                    if (itemSizeMultipliedByNumberOfItemsDoesNotExceedMaximum(numberOfItems)) {
                        setNumberOfItemsInTotalByAdjustingTotal(numberOfItems);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean numberOfItemsInTotalAreWithinBounds(int numberOfItems) {
        return numberOfItems >= UnitOfMeasureConstants.MINIMUM_NUMBER_OF_ITEMS &&
                numberOfItems <= UnitOfMeasureConstants.MAXIMUM_NUMBER_OF_ITEMS;
    }

    private boolean itemSizeNotLessThanSmallestUnit(int numberOfItems) {
        return totalBaseUnits / numberOfItems >= smallestUnit;
    }

    private void setNumberOfItemsInTotalByAdjustingItemSize(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        setNewItemMeasurements();
    }

    private boolean itemSizeMultipliedByNumberOfItemsDoesNotExceedMaximum(int numberOfItems) {
        return itemSize * numberOfItems <= maximumMeasurement;
    }

    private void setNumberOfItemsInTotalByAdjustingTotal(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        totalBaseUnitsAreSet(itemSize * numberOfItems);
    }

    @Override
    public int getUnitOneLabelStringResourceId() {
        return unitOneLabelStringResourceId;
    }

    @Override
    public double getTotalMeasurementOne() {
        return roundDecimal(totalMeasurementOne);
    }

    @Override
    public boolean totalMeasurementOneIsSet(double newTotalMeasurementOne) {
        if (totalBaseUnitsAreSet(baseUnitsWithNewTotalMeasurementOne(newTotalMeasurementOne))) {
            lastMeasurementUpdated = TOTAL_MEASUREMENT;
            return true;

        } else
            totalBaseUnitsAreSet(baseUnitsWithNewTotalMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithNewTotalMeasurementOne(double newTotalMeasurementOne) {
        return (totalMeasurementTwo * unitTwo) + (newTotalMeasurementOne * unitOne);
    }

    @Override
    public double getItemMeasurementOne() {
        return roundDecimal(itemMeasurementOne);
    }

    @Override
    public boolean itemMeasurementOneIsSet(double newItemMeasurementOne) {
        if (totalBaseUnitsAreSet(baseUnitsWithNewItemMeasurementOne(newItemMeasurementOne))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else
            totalBaseUnitsAreSet(baseUnitsWithNewItemMeasurementOne(0.));
        return false;
    }

    private double baseUnitsWithNewItemMeasurementOne(double newItemMeasurementOne) {
        return ((itemMeasurementTwo * unitTwo) + (newItemMeasurementOne * unitOne)) * numberOfItems;
    }

    @Override
    public int getUnitTwoLabelStringResourceId() {
        return unitTwoLabelStringResourceId;
    }

    @Override
    public int getTotalMeasurementTwo() {
        return totalMeasurementTwo;
    }

    @Override
    public boolean totalMeasurementTwoIsSet(int newTotalMeasurementTwo) {
        if (totalBaseUnitsAreSet(baseUnitsWithNewTotalMeasurementTwo(newTotalMeasurementTwo))) {
            lastMeasurementUpdated = TOTAL_MEASUREMENT;
            return true;

        } else
            totalBaseUnitsAreSet(baseUnitsWithNewTotalMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithNewTotalMeasurementTwo(int newTotalMeasurementTwo) {
        return (newTotalMeasurementTwo * unitTwo) + (totalMeasurementOne * unitOne);
    }

    @Override
    public int getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    @Override
    public boolean itemMeasurementTwoIsSet(int newItemMeasurementTwo) {
        if (totalBaseUnitsAreSet(baseUnitsWithNewItemMeasurementTwo(newItemMeasurementTwo))) {
            lastMeasurementUpdated = ITEM_MEASUREMENT;
            return true;

        } else
            totalBaseUnitsAreSet(baseUnitsWithNewItemMeasurementTwo(0));
        return false;
    }

    private double baseUnitsWithNewItemMeasurementTwo(int newItemMeasurementTwo) {
        return ((newItemMeasurementTwo * unitTwo) + (itemMeasurementOne * unitOne)) * numberOfItems;
    }

    @Override
    public boolean isValidMeasurement() {
        return (totalBaseUnits >= minimumMeasurement &&
                totalBaseUnits <= maximumMeasurement &&
                numberOfItems > 0);
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

//        if (this instanceof ImperialMass || this instanceof ImperialVolume) {
//
//            NumberFormat decimalFormat = NumberFormat.getInstance();
//            decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);
//
//            if (decimalFormat instanceof DecimalFormat)
//                ((DecimalFormat) decimalFormat).applyPattern("##.#");
//
//            return Double.parseDouble(decimalFormat.format(valueToRound));
//        } else
            return (int) Math.floor(valueToRound * 1);
    }
}
