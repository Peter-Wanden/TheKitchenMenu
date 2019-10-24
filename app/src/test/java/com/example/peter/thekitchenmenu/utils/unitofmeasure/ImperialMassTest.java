package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Before;
import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

public class ImperialMassTest {
    // region constants ----------------------------------------------------------------------------
    private double DELTA = 0.0001;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private ImperialMass SUT;

    @Before
    public void setUp() {
        SUT = new ImperialMass();
    }

    @Test
    public void getNumberOfMeasurementUnits_NumberOfMeasurementUnitsReturned() {
        assertEquals(IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS, SUT.getNumberOfMeasurementUnits());
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMin_true() {
        // Arrange
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_SMALLEST_UNIT));
        // Assert
        assertEquals(0.1, SUT.getTotalMeasurementOne(), DELTA);
        assertEquals(0.1, SUT.getItemMeasurementOne(), DELTA);
        assertEquals(IMPERIAL_MASS_SMALLEST_UNIT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_UNIT_ONE_DECIMAL - DELTA));
        // Assert
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMax_true() {
        // Arrange
        int maxMeasurementTwo = (int) (IMPERIAL_MASS_MAX_MEASUREMENT / IMPERIAL_MASS_UNIT_TWO);
        double measurementTwoInBaseUnits = maxMeasurementTwo * IMPERIAL_MASS_UNIT_TWO;
        double measurementOneInBaseUnits = IMPERIAL_MASS_MAX_MEASUREMENT - measurementTwoInBaseUnits;
        double measurementOneRemainder = measurementOneInBaseUnits / IMPERIAL_MASS_UNIT_ONE;
        double measurementOneRemoveRemainder = (int) (measurementOneRemainder * 10);
        double measurementOne = measurementOneRemoveRemainder / 10;
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_MAX_MEASUREMENT));
        // Assert
        assertEquals(maxMeasurementTwo, SUT.getTotalMeasurementTwo());
        assertEquals(measurementOne, SUT.getTotalMeasurementOne(), DELTA);
        assertEquals(IMPERIAL_MASS_MAX_MEASUREMENT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_MAX_MEASUREMENT + 1));
        // Assert
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_baseUnitsSmallerThanSmallestItemSize_numberOfItemsReduced() {
        // Arrange
        double slightlySmallerThanTwoSmallestItems = (IMPERIAL_MASS_SMALLEST_UNIT * 2) - (DELTA * 2);
        // Act
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertTrue(SUT.totalBaseUnitsAreSet(slightlySmallerThanTwoSmallestItems));
        // Assert
        assertEquals(1, SUT.getNumberOfItems());
    }

    @Test
    public void testBaseSiAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        assertThat(SUT.totalBaseUnitsAreSet(5.7), is(true));

        assertThat(SUT.getTotalMeasurementOne(), is(.2));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(.1));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(5.7));
    }

    @Test
    public void totalBaseUnitsAreSet_minimumItemSize_noOfItemsNotChanged() {
        // Arrange
        assertTrue(SUT.numberOfItemsIsSet(2));
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_SMALLEST_UNIT * 2));
        // Assert
        assertEquals((0.2), SUT.getTotalMeasurementOne(), DELTA);
        assertEquals((0), SUT.getTotalMeasurementTwo(), DELTA);
        assertEquals((0.1), SUT.getItemMeasurementOne(), DELTA);
        assertEquals((0), SUT.getItemMeasurementTwo());
        assertEquals((IMPERIAL_MASS_SMALLEST_UNIT * 2), SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void testBaseSiRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(SUT.totalBaseUnitsAreSet(5500.), is(true));

        // Check pack and item values have updated correctly
        assertThat(SUT.getTotalMeasurementOne(), is(2.));
        assertThat(SUT.getTotalMeasurementTwo(), is(12));
        assertThat(SUT.getItemMeasurementOne(), is(2.));
        assertThat(SUT.getItemMeasurementTwo(), is(12));
        assertThat(SUT.getTotalBaseUnits(), is(5500.));
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        // Set to max
        assertThat(SUT.totalMeasurementOneIsSet(352.7), is(true));


        // Check value set
        assertThat(SUT.getTotalMeasurementOne(), is(.70));
        assertThat(SUT.getTotalMeasurementTwo(), is(22));
        assertThat(SUT.getItemMeasurementOne(), is(.7));
        assertThat(SUT.getItemMeasurementTwo(), is(22));
        assertThat(SUT.getTotalBaseUnits(), is(9998.8768061875));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        // Set to max plus 1
        assertThat(SUT.totalMeasurementOneIsSet(352.8), is(false));

        // Check values no changes
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(SUT.totalMeasurementOneIsSet(.1), is(true));

        // Check set
        assertThat(SUT.getTotalMeasurementOne(), is(.1));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(.1));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(2.8349523125000005));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMin() { // OUT OF RANGE MIN

        // Set to .01 below min
        assertFalse(SUT.totalMeasurementOneIsSet(0.09));
        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    //////////////////////////// PACK MEASUREMENT TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMaximumInRangeMeasurementUnitTwo() { // IN RANGE MAX

        // Set to max
        assertThat(SUT.totalMeasurementTwoIsSet(22), is(true));

        // Check value set
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(22));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(22));
    }

    @Test
    public void testMaximumOutOfRangeMeasurementUnitTwo() { // OUT OF RANGE MAX

        // Set to max +1
        assertThat(SUT.totalMeasurementTwoIsSet(23), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testMinInRangeMeasurementUnitTwo() { // IN RANGE MIN

        // Set to min
        assertThat(SUT.totalMeasurementTwoIsSet(1), is(true));

        // Check value set
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(1));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(1));
        assertThat(SUT.getTotalBaseUnits(), is(453.59237));
    }

    @Test
    public void testMinimumOutOfRangeMeasurementTwo() { // OUT OF RANGE MIN

        // Set to min -1
        assertThat(SUT.totalMeasurementTwoIsSet(-1), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testInRangeMeasurementOneAndTwo() { // CONDITION: IN RANGE

        // Set arbitrary in range value
        assertThat(SUT.totalMeasurementOneIsSet(5), is(true));
        assertThat(SUT.totalMeasurementTwoIsSet(15), is(true));

        // Check values set
        assertThat(SUT.getTotalMeasurementOne(), is(5.));
        assertThat(SUT.getTotalMeasurementTwo(), is(15));
        assertThat(SUT.getItemMeasurementOne(), is(5.));
        assertThat(SUT.getItemMeasurementTwo(), is(15));
        assertThat(SUT.getTotalBaseUnits(), is(6945.633165625));
    }

    //////////////////////////// SETTING NUMBER OF ITEMS TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testSetNumberOfItemsMinInRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - IN RANGE MIN

        // Set arbitrary number of items, base si at zero
        assertThat(SUT.numberOfItemsIsSet(5), is(true));

        // Check set
        assertThat(SUT.getNumberOfItems(), is(5));
        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testSetNumberOfItemsMinOutOfRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MIN

        // Set out of range min
        assertThat(SUT.numberOfItemsIsSet(0), is(false));

        // Check values unchanged (1 is default)
        assertThat(SUT.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMaxWithNoBaseSI() { // CONDITION: BASE SI NOT YET SET - IN RANGE MAX

        // Set to max within range
        assertThat(SUT.numberOfItemsIsSet(99), is(true));

        // Check set
        assertThat(SUT.getNumberOfItems(), is(99));

        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void numberOfItemsAreSet_outOfRangeMax_false() {
        // Set to max +1
        assertFalse(SUT.numberOfItemsIsSet(MAX_NUMBER_OF_ITEMS + 1));
    }

    @Test
    public void testNumberOfItemsInRangeMinWithBaseSI() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE MIN

        // Set value to pack measurement - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(SUT.totalMeasurementOneIsSet(.2), is(true));

        // Set number of items
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        // Check pack measurement unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(.2));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        // Check item measurement changed
        assertThat(SUT.getItemMeasurementOne(), is(.1));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(5.669904625000001));
    }

    @Test
    public void testNumberOfItemsOutOfRangeAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE (ODD)

        // Set pack to number not divisible by number of items
        assertThat(SUT.totalMeasurementOneIsSet(3), is(true));

        // Set number of items not divisible by pack size
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        // Check item measurements have rounded correctly
        assertThat(SUT.getTotalMeasurementOne(), is(3.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementOne(), is(1.5));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(85.048569375));
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE & TWO - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(SUT.totalMeasurementOneIsSet(5), is(true));
        assertThat(SUT.totalMeasurementTwoIsSet(1), is(true));

        // Set number of items
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        assertThat(SUT.getTotalMeasurementOne(), is(5.));
        assertThat(SUT.getTotalMeasurementTwo(), is(1));
        // Check item measurements have changed
        assertThat(SUT.getItemMeasurementOne(), is(10.5));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(595.339985625));
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.itemMeasurementOneIsSet(5), is(true));

        // Set number of items
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        // Check pack measurements have changed
        assertThat(SUT.getTotalMeasurementOne(), is(10.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        // Check item measurement are unchanged
        assertThat(SUT.getItemMeasurementOne(), is(5.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(283.49523125));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.itemMeasurementOneIsSet(5), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(1), is(true));

        // Set items
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(SUT.getTotalMeasurementOne(), is(10.));
        assertThat(SUT.getTotalMeasurementTwo(), is(2));
        // Check ITEM measurements are unchanged
        assertThat(SUT.getItemMeasurementOne(), is(5.));
        assertThat(SUT.getItemMeasurementTwo(), is(1));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.itemMeasurementOneIsSet(1), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(11), is(true));

        // Set items so to high
        assertThat(SUT.numberOfItemsIsSet(2), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(1.));
        assertThat(SUT.getTotalMeasurementTwo(), is(11));
        assertThat(SUT.getItemMeasurementOne(), is(1.));
        assertThat(SUT.getItemMeasurementTwo(), is(11));
        assertThat(SUT.getTotalBaseUnits(), is(5017.8655931250005));
    }

    @Test
    public void testNumberOfItemsInRangeSetWithItemWithBaseSiThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(SUT.itemMeasurementOneIsSet(15), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(2), is(true));

        // Change number of items
        assertThat(SUT.numberOfItemsIsSet(3), is(true));

        // Check pack measurement has changed
        assertThat(SUT.getTotalMeasurementOne(), is(13.));
        assertThat(SUT.getTotalMeasurementTwo(), is(8));
        assertThat(SUT.getItemMeasurementOne(), is(15.));
        assertThat(SUT.getItemMeasurementTwo(), is(2));

        // Set item measurement again
        assertThat(SUT.numberOfItemsIsSet(5), is(true));

        // Check pack measurement changed
        assertThat(SUT.getTotalMeasurementOne(), is(11.));
        assertThat(SUT.getTotalMeasurementTwo(), is(14));
        assertThat(SUT.getItemMeasurementOne(), is(15.));
        assertThat(SUT.getItemMeasurementTwo(), is(2));
        assertThat(SUT.getTotalBaseUnits(), is(6662.137934375));
    }

    @Test
    public void test_setting_item_one_and_two() {

        assertThat(SUT.numberOfItemsIsSet(2), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(1), is(true));
        assertThat(SUT.getTotalBaseUnits(), is(907.18474));
        assertThat(SUT.itemMeasurementOneIsSet(2), is(true));
        assertThat(SUT.getItemMeasurementOne(), is(2.));
        assertThat(SUT.getTotalBaseUnits(), is(1020.5828325));
    }

    @Test
    public void settingBaseSi() {

        assertThat(SUT.numberOfItemsIsSet(2), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(2), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(20.), is(true));
        assertThat(SUT.getTotalMeasurementTwo(), is(1));
        assertThat(SUT.getTotalMeasurementOne(), is(4.));
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {

        // Setup
        assertThat(SUT.numberOfItemsIsSet(2), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(5.2), is(true));
        assertThat(SUT.totalMeasurementTwoIsSet(1), is(true));

        // Gradual teardown, as the user would type
        assertThat(SUT.totalMeasurementTwoIsSet(0), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(5.), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(5), is(true));

        assertThat(SUT.totalMeasurementOneIsSet(0), is(false));
        assertThat(SUT.getTotalBaseUnits(), is(0.));

        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
    }

    @Test
    public void pack_measurement_two_forces_out_of_bounds_is_then_zeroed_out() {

        // Setup in range values
        assertThat(SUT.totalMeasurementOneIsSet(10), is(true));
        assertThat(SUT.getTotalMeasurementOne(), is(10.));


        assertThat(SUT.totalMeasurementTwoIsSet(19), is(true));
        assertThat(SUT.getTotalMeasurementTwo(), is(19));

        // Attempt to set pack two out of range, which should return false
        // and zero out pack measurement two.
        assertThat(SUT.totalMeasurementTwoIsSet(22), is(false));

        // Check pack measurement one is unaffected
        assertThat(SUT.getTotalMeasurementOne(), is(10.));

        // Check pack two has been zeroed out
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
    }

    @Test
    public void pack_measurement_one_forces_out_of_bounds_is_then_zeroed_out() {

        // Setup in range values
        assertThat(SUT.totalMeasurementTwoIsSet(22), is(true));
        assertThat(SUT.getTotalMeasurementTwo(), is(22));

        // Attempt to set pack one out of range, which should return false
        // and zero out pack measurement one.
        assertThat(SUT.totalMeasurementOneIsSet(1), is(false));
        assertThat(SUT.getTotalMeasurementOne(), is(0.));

    }
}