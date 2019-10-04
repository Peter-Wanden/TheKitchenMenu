package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_MASS;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

public class ImperialMassTest {

    private ImperialMass imperialMass = new ImperialMass();

    @Test
    public void testGetNumberOfMeasurementUnits() {

        assertThat(imperialMass.getNumberOfMeasurementUnits(), is(2));
    }


    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseSiInRangeMin() { // IN RANGE MIN

        assertThat(imperialMass.totalBaseUnitsAreSet(2.834952313), is(true));

        assertThat(imperialMass.getTotalMeasurementOne(), is(.1));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(.1));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(2.834952313));

        System.out.println();
    }

    @Test
    public void testBaseSiOutOfRangeMin() { // OUT OF RANGE MIN

        assertThat(imperialMass.totalBaseUnitsAreSet(2.83495231), is(false));

        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiInRangeMax() { // IN RANGE MAX

        assertThat(imperialMass.totalBaseUnitsAreSet((MAXIMUM_MASS)), is(true));

        assertThat(imperialMass.getTotalMeasurementOne(), is(.7));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(22));
        assertThat(imperialMass.getItemMeasurementOne(), is(.7));
        assertThat(imperialMass.getItemMeasurementTwo(), is(22));
        assertThat(imperialMass.getTotalBaseUnits(), is(MAXIMUM_MASS));

        System.out.println();
    }

    @Test
    public void testBaseSiOutOfRangeMax() { // OUT OF RANGE MAX

        assertThat(imperialMass.totalBaseUnitsAreSet(MAXIMUM_MASS + 1), is(false));

        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiViolatesMinimumItemSize() { // CONDITION: BASE SI SMALLER THAN SMALLEST ITEM

        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));

        assertThat(imperialMass.totalBaseUnitsAreSet(5.6699), is(false));

        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));

        assertThat(imperialMass.totalBaseUnitsAreSet(5.7), is(true));

        assertThat(imperialMass.getTotalMeasurementOne(), is(.2));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(.1));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(5.7));

        System.out.println();
    }

    @Test
    public void testBaseSiRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(imperialMass.totalBaseUnitsAreSet(5500.), is(true));

        // Check pack and item values have updated correctly
        assertThat(imperialMass.getTotalMeasurementOne(), is(2.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(12));
        assertThat(imperialMass.getItemMeasurementOne(), is(2.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(12));
        assertThat(imperialMass.getTotalBaseUnits(), is(5500.));

        System.out.println();
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        // Set to max
        assertThat(imperialMass.totalMeasurementOneIsSet(352.7), is(true));


        // Check value set
        assertThat(imperialMass.getTotalMeasurementOne(), is(.70));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(22));
        assertThat(imperialMass.getItemMeasurementOne(), is(.7));
        assertThat(imperialMass.getItemMeasurementTwo(), is(22));
        assertThat(imperialMass.getTotalBaseUnits(), is(9998.8768061875));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        // Set to max plus 1
        assertThat(imperialMass.totalMeasurementOneIsSet(352.8), is(false));

        // Check values no changes
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(imperialMass.totalMeasurementOneIsSet(.1), is(true));

        // Check set
        assertThat(imperialMass.getTotalMeasurementOne(), is(.1));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(.1));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(2.8349523125000005));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMin() { // OUT OF RANGE MIN

        // Set to .01 below min
        assertThat(imperialMass.totalMeasurementOneIsSet(0.09), is(false));

        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));
    }

    //////////////////////////// PACK MEASUREMENT TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMaximumInRangeMeasurementUnitTwo() { // IN RANGE MAX

        // Set to max
        assertThat(imperialMass.totalMeasurementTwoIsSet(22), is(true));

        // Check value set
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(22));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(22));

        System.out.println();
    }

    @Test
    public void testMaximumOutOfRangeMeasurementUnitTwo() { // OUT OF RANGE MAX

        // Set to max +1
        assertThat(imperialMass.totalMeasurementTwoIsSet(23), is(false));

        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMinInRangeMeasurementUnitTwo() { // IN RANGE MIN

        // Set to min
        assertThat(imperialMass.totalMeasurementTwoIsSet(1), is(true));

        // Check value set
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(1));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(1));
        assertThat(imperialMass.getTotalBaseUnits(), is(453.59237));
    }

    @Test
    public void testMinimumOutOfRangeMeasurementTwo() { // OUT OF RANGE MIN

        // Set to min -1
        assertThat(imperialMass.totalMeasurementTwoIsSet(-1), is(false));

        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testInRangeMeasurementOneAndTwo() { // CONDITION: IN RANGE

        // Set arbitrary in range value
        assertThat(imperialMass.totalMeasurementOneIsSet(5), is(true));
        assertThat(imperialMass.totalMeasurementTwoIsSet(15), is(true));

        // Check values set
        assertThat(imperialMass.getTotalMeasurementOne(), is(5.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(15));
        assertThat(imperialMass.getItemMeasurementOne(), is(5.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(15));
        assertThat(imperialMass.getTotalBaseUnits(), is(6945.633165625));
    }

    //////////////////////////// SETTING NUMBER OF ITEMS TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testSetNumberOfItemsMinInRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - IN RANGE MIN

        // Set arbitrary number of items, base si at zero
        assertThat(imperialMass.numberOfItemsIsSet(5), is(true));

        // Check set
        assertThat(imperialMass.getNumberOfItems(), is(5));
        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testSetNumberOfItemsMinOutOfRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MIN

        // Set out of range min
        assertThat(imperialMass.numberOfItemsIsSet(0), is(false));

        // Check values unchanged (1 is default)
        assertThat(imperialMass.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMaxWithNoBaseSI() { // CONDITION: BASE SI NOT YET SET - IN RANGE MAX

        // Set to max within range
        assertThat(imperialMass.numberOfItemsIsSet(99), is(true));

        // Check set
        assertThat(imperialMass.getNumberOfItems(), is(99));

        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeMaxWithNoBaseSI() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MAX

        // Set to max +1
        assertThat(imperialMass.numberOfItemsIsSet(1000), is(false));

        // Check values unchanged
        assertThat(imperialMass.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMinWithBaseSI() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE MIN

        // Set value to pack measurement - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(imperialMass.totalMeasurementOneIsSet(.2), is(true));

        // Set number of items
        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));

        // Check pack measurement unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(.2));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        // Check item measurement changed
        assertThat(imperialMass.getItemMeasurementOne(), is(.1));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(5.669904625000001));
    }

    @Test
    public void testNumberOfItemsOutOfRangeAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE (ODD)

        // Set pack to number not divisible by number of items
        assertThat(imperialMass.totalMeasurementOneIsSet(3), is(true));

        // Set number of items not divisible by pack size
        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));

        // Check item measurements have rounded correctly
        assertThat(imperialMass.getTotalMeasurementOne(), is(3.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementOne(), is(1.5));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(85.048569375));
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE & TWO - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(imperialMass.totalMeasurementOneIsSet(5), is(true));
        assertThat(imperialMass.totalMeasurementTwoIsSet(1), is(true));

        // Set number of items
        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));

        assertThat(imperialMass.getTotalMeasurementOne(), is(5.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(1));
        // Check item measurements have changed
        assertThat(imperialMass.getItemMeasurementOne(), is(10.5));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(595.339985625));
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(imperialMass.itemMeasurementOneIsSet(5), is(true));

        // Set number of items
        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));

        // Check pack measurements have changed
        assertThat(imperialMass.getTotalMeasurementOne(), is(10.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        // Check item measurement are unchanged
        assertThat(imperialMass.getItemMeasurementOne(), is(5.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
        assertThat(imperialMass.getTotalBaseUnits(), is(283.49523125));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(imperialMass.itemMeasurementOneIsSet(5), is(true));
        assertThat(imperialMass.itemMeasurementTwoIsSet(1), is(true));

        // Set items
        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(imperialMass.getTotalMeasurementOne(), is(10.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(2));
        // Check ITEM measurements are unchanged
        assertThat(imperialMass.getItemMeasurementOne(), is(5.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(1));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(imperialMass.itemMeasurementOneIsSet(1), is(true));
        assertThat(imperialMass.itemMeasurementTwoIsSet(11), is(true));

        // Set items so to high
        assertThat(imperialMass.numberOfItemsIsSet(2), is(false));

        // Check values unchanged
        assertThat(imperialMass.getTotalMeasurementOne(), is(1.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(11));
        assertThat(imperialMass.getItemMeasurementOne(), is(1.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(11));
        assertThat(imperialMass.getTotalBaseUnits(), is(5017.8655931250005));
    }

    @Test
    public void testNumberOfItemsInRangeSetWithItemWithBaseSiThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(imperialMass.itemMeasurementOneIsSet(15), is(true));
        assertThat(imperialMass.itemMeasurementTwoIsSet(2), is(true));

        // Change number of items
        assertThat(imperialMass.numberOfItemsIsSet(3), is(true));

        // Check pack measurement has changed
        assertThat(imperialMass.getTotalMeasurementOne(), is(13.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(8));
        assertThat(imperialMass.getItemMeasurementOne(), is(15.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(2));

        // Set item measurement again
        assertThat(imperialMass.numberOfItemsIsSet(5), is(true));

        // Check pack measurement changed
        assertThat(imperialMass.getTotalMeasurementOne(), is(11.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(14));
        assertThat(imperialMass.getItemMeasurementOne(), is(15.));
        assertThat(imperialMass.getItemMeasurementTwo(), is(2));
        assertThat(imperialMass.getTotalBaseUnits(), is(6662.137934375));
    }

    @Test
    public void test_setting_item_one_and_two() {

        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));
        assertThat(imperialMass.itemMeasurementTwoIsSet(1), is(true));
        assertThat(imperialMass.getTotalBaseUnits(), is(907.18474));
        assertThat(imperialMass.itemMeasurementOneIsSet(2), is(true));
        assertThat(imperialMass.getItemMeasurementOne(), is(2.));
        assertThat(imperialMass.getTotalBaseUnits(), is(1020.5828325));
    }

    @Test
    public void settingBaseSi() {

        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));
        assertThat(imperialMass.totalMeasurementOneIsSet(2), is(true));
        assertThat(imperialMass.totalMeasurementOneIsSet(20.), is(true));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(1));
        assertThat(imperialMass.getTotalMeasurementOne(), is(4.));
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {

        // Setup
        assertThat(imperialMass.numberOfItemsIsSet(2), is(true));
        assertThat(imperialMass.totalMeasurementOneIsSet(5.2), is(true));
        assertThat(imperialMass.totalMeasurementTwoIsSet(1), is(true));

        // Gradual teardown, as the user would type
        assertThat(imperialMass.totalMeasurementTwoIsSet(0), is(true));
        assertThat(imperialMass.totalMeasurementOneIsSet(5.), is(true));
        assertThat(imperialMass.totalMeasurementOneIsSet(5), is(true));

        assertThat(imperialMass.totalMeasurementOneIsSet(0), is(false));
        assertThat(imperialMass.getTotalBaseUnits(), is(0.));

        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));
        assertThat(imperialMass.getItemMeasurementOne(), is(0.));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
        assertThat(imperialMass.getItemMeasurementTwo(), is(0));
    }

    @Test
    public void pack_measurement_two_forces_out_of_bounds_is_then_zeroed_out() {

        // Setup in range values
        assertThat(imperialMass.totalMeasurementOneIsSet(10), is(true));
        assertThat(imperialMass.getTotalMeasurementOne(), is(10.));


        assertThat(imperialMass.totalMeasurementTwoIsSet(19), is(true));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(19));

        // Attempt to set pack two out of range, which should return false
        // and zero out pack measurement two.
        assertThat(imperialMass.totalMeasurementTwoIsSet(22), is(false));

        // Check pack measurement one is unaffected
        assertThat(imperialMass.getTotalMeasurementOne(), is(10.));

        // Check pack two has been zeroed out
        assertThat(imperialMass.getTotalMeasurementTwo(), is(0));
    }

    @Test
    public void pack_measurement_one_forces_out_of_bounds_is_then_zeroed_out() {

        // Setup in range values
        assertThat(imperialMass.totalMeasurementTwoIsSet(22), is(true));
        assertThat(imperialMass.getTotalMeasurementTwo(), is(22));

        // Attempt to set pack one out of range, which should return false
        // and zero out pack measurement one.
        assertThat(imperialMass.totalMeasurementOneIsSet(1), is(false));
        assertThat(imperialMass.getTotalMeasurementOne(), is(0.));

    }
}