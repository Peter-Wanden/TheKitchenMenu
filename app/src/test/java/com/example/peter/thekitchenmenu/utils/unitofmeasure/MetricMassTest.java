package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

import org.junit.Before;
import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;


public class MetricMassTest {

    // region constants ----------------------------------------------------------------------------
    private double DELTA = 0.00001;
    private Pair<Double, Integer> unitValuesFromBaseUnits;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private UnitOfMeasure SUT;

    @Before
    public void setup() {
        SUT = new MetricMass();
    }

    @Test
    public void baseUnitsAreSet_inRangeMin_true() {
        assertTrue(SUT.isTotalBaseUnitsSet(MIN_MASS));
        assertEquals(MIN_MASS, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void baseUnitsAreSet_outOfRangeMin_false() {
        assertFalse(SUT.isTotalBaseUnitsSet(MIN_MASS - .1));
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void baseUnitsAreSet_inRangeMax_true() {
        assertTrue(SUT.isTotalBaseUnitsSet(MAX_MASS));
        assertEquals(SUT.getTotalBaseUnits(), MAX_MASS, DELTA);
    }

    @Test
    public void baseUnitsAreSet_outOfRangeMax_false() {
        assertFalse(SUT.isTotalBaseUnitsSet(MAX_MASS + 1));
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void baseUnitsAreSet_baseUnitsViolateMinimumItemSize_noOfItemsAdjustedAccordingly() {
        // Arrange
        int noOfItems = 5;
        double minBaseUnitsThatFitIntoNoOfItems = noOfItems / METRIC_MASS_SMALLEST_UNIT;
        double baseUnitsBelowMinForNoOfItems = minBaseUnitsThatFitIntoNoOfItems - 1;
        int maxNoOfItemsThatFitIntoBaseUnitsBelowMin =
                (int) (baseUnitsBelowMinForNoOfItems / METRIC_MASS_SMALLEST_UNIT);

        assertTrue(SUT.isNumberOfItemsSet(noOfItems));
        assertTrue(SUT.isTotalBaseUnitsSet(baseUnitsBelowMinForNoOfItems));
        assertEquals(maxNoOfItemsThatFitIntoBaseUnitsBelowMin, SUT.getNumberOfItems());
        assertEquals(baseUnitsBelowMinForNoOfItems, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void baseUnitsAreSet_baseAtMinimumItemSize_true() {
        int noOfItems = 5;
        double minBaseUnitsThatFitIntoNoOfItems = noOfItems / METRIC_MASS_SMALLEST_UNIT;
        assertTrue(SUT.isNumberOfItemsSet(noOfItems));
        assertTrue(SUT.isTotalBaseUnitsSet(minBaseUnitsThatFitIntoNoOfItems));
        assertEquals(METRIC_MASS_SMALLEST_UNIT, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void baseUnitsAreSet_totalAndItemUpdatedWithCorrectValues() {
        // Arrange
        double totalBaseUnitsInRange = MAX_MASS / 2;
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(totalBaseUnitsInRange);
        double expectedUnitOne = unitValuesFromBaseUnits.first;
        int expectedUnitTwo = unitValuesFromBaseUnits.second;

        assertTrue(SUT.isTotalBaseUnitsSet(totalBaseUnitsInRange));
        assertEquals(expectedUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedUnitTwo, SUT.getTotalUnitTwo(), DELTA);
        assertEquals(expectedUnitOne, SUT.getItemUnitOne(), DELTA);
        assertEquals(expectedUnitTwo, SUT.getItemUnitTwo());
    }

    @Test
    public void getMinUnitOne_correctValueReturned() {
        // Arrange
        // Act
        assertEquals(METRIC_MASS_MIN_MEASUREMENT, SUT.getMinUnitOneInBaseUnits(), DELTA);
        // Assert
    }

    @Test
    public void getMaxMeasurementOne_getMaxMeasurementTwo_correctValuesReturned() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(MAX_MASS);
        double expectedUnitOneMaxValue = unitValuesFromBaseUnits.first;
        int expectedUnitTwoMaxValue = unitValuesFromBaseUnits.second;
        // Act
        assertEquals(expectedUnitOneMaxValue, SUT.getMaxUnitOne(), DELTA);
        assertEquals(expectedUnitTwoMaxValue, SUT.getMaxUnitTwo());
        // Assert
    }

    @Test
    public void totalMeasurementOne_inRangeMax_true() {
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(MAX_MASS);
        double expectedUnitOne = unitValuesFromBaseUnits.first;
        int expectedUnitTwo = unitValuesFromBaseUnits.second;

        assertTrue(SUT.isTotalUnitOneSet(METRIC_MASS_MAX_MEASUREMENT));

        assertEquals(expectedUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedUnitTwo, SUT.getTotalUnitTwo());
        assertEquals(expectedUnitOne, SUT.getItemUnitOne(), DELTA);
        assertEquals(expectedUnitTwo, SUT.getItemUnitTwo());
        assertEquals(METRIC_MASS_MAX_MEASUREMENT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOne_outOfRangeMax_false() {
        assertFalse(SUT.isTotalUnitOneSet(METRIC_MASS_MAX_MEASUREMENT + 1));
        assertEquals((double) NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOne_inRangeMin_true() {
        assertTrue(SUT.isTotalUnitOneSet(METRIC_MASS_MIN_MEASUREMENT));
        assertEquals(METRIC_MASS_MIN_MEASUREMENT, SUT.getTotalUnitOne(), DELTA);
    }

    @Test
    public void totalMeasurementOne_outOfRangeMin_false() {
        assertFalse(SUT.isTotalUnitOneSet(METRIC_MASS_MIN_MEASUREMENT - .1));
        assertEquals((double) NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_inRangeMax_true() {
        int maxUnitTwo = SUT.getMaxUnitTwo();
        assertTrue(SUT.isTotalUnitTwoSet(maxUnitTwo));
        assertEquals(METRIC_MASS_MAX_MEASUREMENT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMax_false() {
        // Arrange
        int totalTwoOutOfRangeMax = SUT.getMaxUnitTwo() + 1;
        // Act
        assertFalse(SUT.isTotalUnitTwoSet(totalTwoOutOfRangeMax));
        // Assert
        assertEquals((double) NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMax_doesNotAffectOtherValues() {
        // Arrange
        int totalTwoOutOfRangeMax = SUT.getMaxUnitTwo() + 1;
        double expectedItemBaseUnits = 250;
        // Act
        assertTrue(SUT.isItemBaseUnitsSet(expectedItemBaseUnits));
        assertFalse(SUT.isTotalUnitTwoSet(totalTwoOutOfRangeMax));
        assertEquals(expectedItemBaseUnits, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void isTotalMeasurementOneAndTwo_inRange_true() {
        double totalUnitOne = 500;
        int totalUnitTwo = 5;
        double totalBaseUnits = getBaseUnitsFromUnitsValues(new Pair<>(totalUnitOne, totalUnitTwo));
        assertTrue(SUT.isTotalUnitOneSet(totalUnitOne));
        assertTrue(SUT.isTotalUnitTwoSet(totalUnitTwo));

        assertEquals(totalUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(totalUnitTwo, SUT.getTotalUnitTwo());

        assertEquals(totalBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnitsInRangeMax_setTrue() {
        assertTrue(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS));
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnitsMinOutOfRange_setFalse() {
        assertFalse(SUT.isNumberOfItemsSet(MIN_NUMBER_OF_ITEMS - 1));
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnitsOutOfRangeMax_setFalse() {
        assertFalse(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS + 1));
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void numberOfItemsIsSet_inRangeMinWithBaseUnitsSet_setTrue() {

        assertTrue(SUT.isTotalUnitOneSet(2));
        assertTrue(SUT.isNumberOfItemsSet(2));
        assertEquals(2, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeOdd_adjustsItemSize() {

        // Set total to number not divisible by number of items
        assertTrue(SUT.isTotalUnitOneSet(3));

        // Set number of items not divisible by pack size
        assertTrue(SUT.isNumberOfItemsSet(2));

        // Check item measurements have rounded correctly
        assertEquals(3, SUT.getTotalUnitOne(), DELTA);
        assertEquals(0, SUT.getTotalUnitTwo());
        assertEquals(1, SUT.getItemUnitOne(), DELTA);
        assertEquals(0, SUT.getItemUnitTwo(), DELTA);
        assertEquals(3, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_addingNumberOfItemsAdjustsItemSize_setTrue() {

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertTrue(SUT.isTotalUnitOneSet(500));
        assertTrue(SUT.isTotalUnitTwoSet(1));
        // Set number of items
        assertTrue(SUT.isNumberOfItemsSet(2));
        // Check item measurements have changed
        assertEquals(750, SUT.getItemUnitOne(), DELTA);
        assertEquals(0, SUT.getItemUnitTwo());
        assertEquals(1500, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_numberOfItemsAdjustsTotalSize_setTrue() {

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated TOTAL measurement should change
        assertTrue(SUT.isItemUnitOneSet(500));
        // Set number of items
        assertTrue(SUT.isNumberOfItemsSet(2));
        // Check pack measurement have changed
        assertEquals(1, SUT.getTotalUnitTwo());
        assertEquals(500, SUT.getItemUnitOne(), DELTA);
        assertEquals(1000, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_itemsOneAndTwoAdjustsTotalSize_setTrue() {

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated TOTAL measurement should change
        assertTrue(SUT.isItemUnitOneSet(500));
        assertTrue(SUT.isItemUnitTwoSet(1));

        // Set items
        assertTrue(SUT.isNumberOfItemsSet(2));

        // Check PACK measurements have changed
        assertEquals(0, SUT.getTotalUnitOne(), DELTA);
        assertEquals(3, SUT.getTotalUnitTwo());
        assertEquals(500, SUT.getItemUnitOne(), DELTA);
        assertEquals(1, SUT.getItemUnitTwo());
        assertEquals(3000, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_itemOneAndTwoAdjustsTotalSizeOutOfRange_setFalse() {

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated TOTAL measurement should change
        assertTrue(SUT.isItemUnitTwoSet((int) MAX_MASS / 2 / 1000));
        assertTrue(SUT.isItemUnitOneSet(1));

        // Set items out of range for this measurement (2 * 5001 = 10002 > MAX_MASS)
        assertFalse(SUT.isNumberOfItemsSet(2));

        // Check values unchanged
        assertEquals(5001, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_inRangeSetWithItemWithBaseSiThenChanged_measurementsAsExpected() {
        // Set item measurement as last changed
        assertTrue(SUT.isItemUnitOneSet(500));
        assertTrue(SUT.isItemUnitTwoSet(1));

        // Change number of items
        assertTrue(SUT.isNumberOfItemsSet(3));

        // Check total measurement has changed
        assertEquals(500, SUT.getTotalUnitOne(), DELTA);
        assertEquals(4, SUT.getTotalUnitTwo());
        assertEquals(500, SUT.getItemUnitOne(), DELTA);
        assertEquals(1, SUT.getItemUnitTwo());
        assertEquals(4500, SUT.getTotalBaseUnits(), DELTA);

        // Set number of items again
        assertTrue(SUT.isNumberOfItemsSet(5));

        // Check total measurement changed again
        assertEquals(500, SUT.getTotalUnitOne(), DELTA);
        assertEquals(7, SUT.getTotalUnitTwo());
        assertEquals(500, SUT.getItemUnitOne(), DELTA);
        assertEquals(1, SUT.getItemUnitTwo());
        assertEquals(7500, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void testNumberOfItemsInRangeSetWith_ItemWithBaseSI() {
        // Set total measurement as last
        assertTrue(SUT.isTotalUnitTwoSet((int) MAX_MASS / 1000));
        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), DELTA);

        // Change number of items
        assertTrue(SUT.isNumberOfItemsSet(10));
        assertEquals(10, SUT.getTotalUnitTwo());

        // Check item measurements have changed
        assertEquals(0, SUT.getItemUnitOne(), DELTA);
        assertEquals(1, SUT.getItemUnitTwo(), DELTA);
        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), DELTA);

        // Change number of items
        assertTrue(SUT.isNumberOfItemsSet(20));

        assertEquals(0, SUT.getTotalUnitOne(), DELTA);
        assertEquals(10, SUT.getTotalUnitTwo());
        // Check item measurements have changed
        assertEquals(500, SUT.getItemUnitOne(), DELTA);
        assertEquals(0, SUT.getItemUnitTwo());
        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void baseUnitsAreSet_testMixedNumberReturnValues() {
        assertTrue(SUT.isTotalBaseUnitsSet(5));
        assertTrue(SUT.isNumberOfItemsSet(3));
        assertEquals(5, SUT.getTotalUnitOne(), DELTA);
        assertEquals(1, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void setTotalMeasurementOne_measurementIsSet() {
        assertTrue(SUT.isNumberOfItemsSet(2));
        assertTrue(SUT.isTotalUnitOneSet(2.));
        assertEquals(2, SUT.getTotalBaseUnits(), DELTA);
        assertTrue(SUT.isTotalUnitOneSet(20));
        assertEquals(20, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void baseUnitsAreSet_setTrue() {
        assertTrue(SUT.isNumberOfItemsSet(2));
        assertTrue(SUT.isTotalUnitOneSet(2));
        assertTrue(SUT.isTotalUnitOneSet(20));
        assertEquals(20, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(20, SUT.getTotalUnitOne(), DELTA);
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {
        assertTrue(SUT.isNumberOfItemsSet(2));
        assertTrue(SUT.isTotalUnitOneSet(500));
        assertTrue(SUT.isTotalUnitTwoSet(1));
        assertEquals(1500, SUT.getTotalBaseUnits(), DELTA);

        // Gradual teardown, as the user would type
        assertTrue(SUT.isTotalUnitTwoSet(0));
        assertEquals(500, SUT.getTotalBaseUnits(), DELTA);

        assertTrue(SUT.isTotalUnitOneSet(50));
        assertEquals(50, SUT.getTotalBaseUnits(), DELTA);

        assertTrue(SUT.isTotalUnitOneSet(5));
        assertEquals(5, SUT.getTotalBaseUnits(), DELTA);

        assertFalse(SUT.isTotalBaseUnitsSet(0));
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);

        assertEquals(0, SUT.getTotalUnitOne(), DELTA);
        assertEquals(0, SUT.getTotalUnitTwo());
        assertEquals(0, SUT.getItemUnitOne(), DELTA);
        assertEquals(0, SUT.getItemUnitTwo());
    }

    @Test
    public void itemBaseUnitsAreSet_thenNumberOfItems_itemSizeIsTotalBaseUnitsDivNumberItems() {
        // Arrange
        // Act
        SUT.isItemBaseUnitsSet(2000);
        SUT.isNumberOfItemsSet(4);
        // Assert
        assertEquals(8, SUT.getTotalUnitTwo());
        assertEquals(2, SUT.getItemUnitTwo(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_thenItemBaseUnitsAreSet_itemSizeIsTotalBaseUnitsMultipliedByNumberItems() {

        // Arrange
        // Act
        SUT.isNumberOfItemsSet(4);
        SUT.isItemBaseUnitsSet(2000);
        // Assert
        assertEquals(8, SUT.getTotalUnitTwo());
        assertEquals(0, SUT.getItemUnitOne(), DELTA);
    }

    // A crude but effective way of calculating pounds and ounces from grams
    private Pair<Double, Integer> getUnitValuesFromBaseUnits(double baseUnits) {
        int unitTwo = (int) (baseUnits / METRIC_MASS_UNIT_TWO);
        double unitOne = (baseUnits - (unitTwo * METRIC_MASS_UNIT_TWO)) / METRIC_MASS_UNIT_ONE;
        return new Pair<>(unitOne, unitTwo);
    }

    // A crude but effective way of calculating grams from pounds and ounces
    private double getBaseUnitsFromUnitsValues(Pair<Double, Integer> unitValues) {
        double unitOne = unitValues.first;
        int unitTwo = unitValues.second;
        return (unitTwo * METRIC_MASS_UNIT_TWO) + (unitOne * METRIC_MASS_UNIT_ONE);
    }


    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}