package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Before;
import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class MetricMassTest {

    // region constants ----------------------------------------------------------------------------
    private double ERROR_MARGIN_DELTA = 0.00001;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private UnitOfMeasure SUT;

    @Before
    public void setup() {
        SUT = new MetricMass();
    }

    @Test
    public void baseUnitsAreSet_inRangeMin_setTrue() { // IN RANGE MIN

        assertTrue(SUT.totalBaseUnitsAreSet(MIN_MASS));
        assertEquals(MIN_MASS, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_outOfRangeMin_setFalse() {

        assertFalse(SUT.totalBaseUnitsAreSet(MIN_MASS - .1));
        assertEquals(0, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_inRangeMax_setTrue() {

        assertTrue(SUT.totalBaseUnitsAreSet(MAX_MASS));
        assertEquals(SUT.getTotalBaseUnits(), MAX_MASS, ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_outOfRangeMax_setFalse() {

        assertFalse(SUT.totalBaseUnitsAreSet(MAX_MASS + 1));
        assertEquals(0, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_baseUnitsViolateMinimumItemSize_noOfItemsAdjusted() {

        assertTrue(SUT.numberOfItemsIsSet(5));
        assertTrue(SUT.totalBaseUnitsAreSet(4));
        assertEquals(4, SUT.getNumberOfItems());
        assertEquals(4, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_baseAtMinimumItemSize_setTrue() {

        assertTrue(SUT.numberOfItemsIsSet(5));
        assertTrue(SUT.totalBaseUnitsAreSet(5));
        assertEquals(MIN_MASS, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(5, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_totalAndItemUpdated() {

        assertTrue(SUT.totalBaseUnitsAreSet(5500));

        assertEquals(500, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(5, SUT.getTotalMeasurementTwo(), ERROR_MARGIN_DELTA);
        assertEquals(500, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(5, SUT.getItemMeasurementTwo());
    }

    @Test
    public void totalMeasurementOne_inRangeMax_setTrue() {

        assertTrue(SUT.totalMeasurementOneIsSet(MAX_MASS));

        assertEquals(0, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals((int) MAX_MASS / 1000, SUT.getTotalMeasurementTwo());
        assertEquals((int) MAX_MASS % 1000, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals((int) MAX_MASS / 1000, SUT.getItemMeasurementTwo());

        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void totalMeasurementOne_outOfRangeMax_setFalse() {

        assertFalse(SUT.totalMeasurementOneIsSet(MAX_MASS + 1));
        assertEquals(0, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void totalMeasurementOne_inRangeMin_setTrue() {

        assertTrue(SUT.totalMeasurementOneIsSet(MIN_MASS));

        assertEquals(MIN_MASS, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(MIN_MASS, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void totalMeasurementOne_outOfRangeMin_setFalse() {

        assertFalse(SUT.totalMeasurementOneIsSet(MIN_MASS - .1));
        assertEquals(0, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_inRangeMax_setTrue() {

        assertTrue(SUT.totalMeasurementTwoIsSet((int) MAX_MASS / 1000));
        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMax_setFalse() {

        assertFalse(SUT.totalMeasurementTwoIsSet((int) (MAX_MASS / 1000) + 1));
        assertEquals(0, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_inRangeMin_setTrue() {

        assertTrue(SUT.totalMeasurementTwoIsSet((int) MAX_MASS / 10000));
        assertEquals(MAX_MASS / 10, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void totalMeasurementOneAndTwo_inRange_setTrue() {

        assertTrue(SUT.totalMeasurementOneIsSet(500));
        assertTrue(SUT.totalMeasurementTwoIsSet(5));

        assertEquals(500, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(5, SUT.getTotalMeasurementTwo());

        assertEquals(5500, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnitsInRangeMax_setTrue() {
        assertTrue(SUT.numberOfItemsIsSet(MAX_NUMBER_OF_ITEMS));
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnitsMinOutOfRange_setFalse() {
        assertFalse(SUT.numberOfItemsIsSet(MIN_NUMBER_OF_ITEMS - 1));
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnitsOutOfRangeMax_setFalse() {
        assertFalse(SUT.numberOfItemsIsSet(MAX_NUMBER_OF_ITEMS + 1));
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void numberOfItemsIsSet_inRangeMinWithBaseUnitsSet_setTrue() {

        assertTrue(SUT.totalMeasurementOneIsSet(2));
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertEquals(2, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeOdd_adjustsItemSize() {

        // Set total to number not divisible by number of items
        assertTrue(SUT.totalMeasurementOneIsSet(3));

        // Set number of items not divisible by pack size
        assertTrue(SUT.numberOfItemsIsSet(2));

        // Check item measurements have rounded correctly
        assertEquals(3, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(0, SUT.getTotalMeasurementTwo());
        assertEquals(1, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(0, SUT.getItemMeasurementTwo(), ERROR_MARGIN_DELTA);
        assertEquals(3, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_addingNumberOfItemsAdjustsItemSize_setTrue() {

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertTrue(SUT.totalMeasurementOneIsSet(500));
        assertTrue(SUT.totalMeasurementTwoIsSet(1));
        // Set number of items
        assertTrue(SUT.numberOfItemsIsSet(2));
        // Check item measurements have changed
        assertEquals(750, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(0, SUT.getItemMeasurementTwo());
        assertEquals(1500, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_numberOfItemsAdjustsTotalSize_setTrue() {

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated TOTAL measurement should change
        assertTrue(SUT.itemMeasurementOneIsSet(500));
        // Set number of items
        assertTrue(SUT.numberOfItemsIsSet(2));
        // Check pack measurement have changed
        assertEquals(1, SUT.getTotalMeasurementTwo());
        assertEquals(500, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(1000, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_itemsOneAndTwoAdjustsTotalSize_setTrue() {

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated TOTAL measurement should change
        assertTrue(SUT.itemMeasurementOneIsSet(500));
        assertTrue(SUT.itemMeasurementTwoIsSet(1));

        // Set items
        assertTrue(SUT.numberOfItemsIsSet(2));

        // Check PACK measurements have changed
        assertEquals(0, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(3, SUT.getTotalMeasurementTwo());
        assertEquals(500, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(1, SUT.getItemMeasurementTwo());
        assertEquals(3000, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_itemOneAndTwoAdjustsTotalSizeOutOfRange_setFalse() {

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated TOTAL measurement should change
        assertTrue(SUT.itemMeasurementTwoIsSet((int) MAX_MASS / 2 / 1000));
        assertTrue(SUT.itemMeasurementOneIsSet(1));

        // Set items out of range for this measurement (2 * 5001 = 10002 > MAX_MASS)
        assertFalse(SUT.numberOfItemsIsSet(2));

        // Check values unchanged
        assertEquals(5001, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_inRangeSetWithItemWithBaseSiThenChanged_measurementsAsExpected() {
        // Set item measurement as last changed
        assertTrue(SUT.itemMeasurementOneIsSet(500));
        assertTrue(SUT.itemMeasurementTwoIsSet(1));

        // Change number of items
        assertTrue(SUT.numberOfItemsIsSet(3));

        // Check total measurement has changed
        assertEquals(500, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(4, SUT.getTotalMeasurementTwo());
        assertEquals(500, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(1, SUT.getItemMeasurementTwo());
        assertEquals(4500, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        // Set number of items again
        assertTrue(SUT.numberOfItemsIsSet(5));

        // Check total measurement changed again
        assertEquals(500, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(7, SUT.getTotalMeasurementTwo());
        assertEquals(500, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(1, SUT.getItemMeasurementTwo());
        assertEquals(7500, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void testNumberOfItemsInRangeSetWith_ItemWithBaseSI() {
        // Set total measurement as last
        assertTrue(SUT.totalMeasurementTwoIsSet((int) MAX_MASS / 1000));
        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        // Change number of items
        assertTrue(SUT.numberOfItemsIsSet(10));
        assertEquals(10, SUT.getTotalMeasurementTwo());

        // Check item measurements have changed
        assertEquals(0, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(1, SUT.getItemMeasurementTwo(), ERROR_MARGIN_DELTA);
        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        // Change number of items
        assertTrue(SUT.numberOfItemsIsSet(20));

        assertEquals(0, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(10, SUT.getTotalMeasurementTwo());
        // Check item measurements have changed
        assertEquals(500, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(0, SUT.getItemMeasurementTwo());
        assertEquals(MAX_MASS, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_testMixedNumberReturnValues() {
        assertTrue(SUT.totalBaseUnitsAreSet(5));
        assertTrue(SUT.numberOfItemsIsSet(3));
        assertEquals(5, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(1, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void setTotalMeasurementOne_measurementIsSet() {
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertTrue(SUT.totalMeasurementOneIsSet(2.));
        assertEquals(2, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
        assertTrue(SUT.totalMeasurementOneIsSet(20));
        assertEquals(20, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void baseUnitsAreSet_setTrue() {
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertTrue(SUT.totalMeasurementOneIsSet(2));
        assertTrue(SUT.totalMeasurementOneIsSet(20));
        assertEquals(20, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);
        assertEquals(20, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertTrue(SUT.totalMeasurementOneIsSet(500));
        assertTrue(SUT.totalMeasurementTwoIsSet(1));
        assertEquals(1500, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        // Gradual teardown, as the user would type
        assertTrue(SUT.totalMeasurementTwoIsSet(0));
        assertEquals(500, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        assertTrue(SUT.totalMeasurementOneIsSet(50));
        assertEquals(50, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        assertTrue(SUT.totalMeasurementOneIsSet(5));
        assertEquals(5, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        assertFalse(SUT.totalBaseUnitsAreSet(0));
        assertEquals(0, SUT.getTotalBaseUnits(), ERROR_MARGIN_DELTA);

        assertEquals(0, SUT.getTotalMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(0, SUT.getTotalMeasurementTwo());
        assertEquals(0, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
        assertEquals(0, SUT.getItemMeasurementTwo());
    }

    @Test
    public void itemBaseUnitsAreSet_thenNumberOfItems_itemSizeIsTotalBaseUnitsDivNumberItems() {
        // Arrange
        // Act
        SUT.itemBaseUnitsAreSet(2000);
        SUT.numberOfItemsIsSet(4);
        // Assert
        assertEquals(8, SUT.getTotalMeasurementTwo());
        assertEquals(2, SUT.getItemMeasurementTwo(), ERROR_MARGIN_DELTA);
    }

    @Test
    public void numberOfItemsIsSet_thenItemBaseUnitsAreSet_itemSizeIsTotalBaseUnitsMultipliedByNumberItems() {

        // Arrange
        // Act
        SUT.numberOfItemsIsSet(4);
        SUT.itemBaseUnitsAreSet(2000);
        // Assert
        assertEquals(8, SUT.getTotalMeasurementTwo());
        assertEquals(0, SUT.getItemMeasurementOne(), ERROR_MARGIN_DELTA);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}