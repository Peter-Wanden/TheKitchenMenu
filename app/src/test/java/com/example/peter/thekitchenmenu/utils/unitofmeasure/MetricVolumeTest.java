package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_VOLUME;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MetricVolumeTest {

    private MetricVolume metricVolume = new MetricVolume();

    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseUnitsInRangeMin() { // IN RANGE MIN

        assertThat(metricVolume.totalBaseUnitsAreSet(1), is(true));

        assertThat(metricVolume.getTotalMeasurementOne(), is(1.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(1.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(1.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsOutOfRangeMin() { // OUT OF RANGE MIN

        assertThat(metricVolume.totalBaseUnitsAreSet(0.9), is(false));

        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsInRangeMax() { // IN RANGE MAX

        assertThat(metricVolume.totalBaseUnitsAreSet(MAXIMUM_VOLUME), is(true));

        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(10));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(10));
        assertThat(metricVolume.getTotalBaseUnits(), is(MAXIMUM_VOLUME));

        System.out.println();
    }

    @Test
    public void testBaseUnitsOutOfRangeMax() { // OUT OF RANGE MAX

        assertThat(metricVolume.totalBaseUnitsAreSet(MAXIMUM_VOLUME + 1), is(false));

        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsViolatesMinimumItemSize() { // CONDITION: BASE SI SMALLER THAN SMALLEST ITEM

        assertThat(metricVolume.numberOfItemsIsSet(5), is(true));

        assertThat(metricVolume.totalBaseUnitsAreSet(4), is(false));

        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        assertThat(metricVolume.numberOfItemsIsSet(5), is(true));

        assertThat(metricVolume.totalBaseUnitsAreSet(5), is(true));

        assertThat(metricVolume.getTotalMeasurementOne(), is(5.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(1.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(5.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(metricVolume.totalBaseUnitsAreSet(5500), is(true));

        // Check pack and item values have updated correctly
        assertThat(metricVolume.getTotalMeasurementOne(), is(500.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(5));
        assertThat(metricVolume.getItemMeasurementOne(), is(500.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(5));
        assertThat(metricVolume.getTotalBaseUnits(), is(5500.));

        System.out.println();
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        // Set to max
        assertThat(metricVolume.totalMeasurementOneIsSet(MAXIMUM_VOLUME), is(true));

        // Check value set
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(10));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(10));
        assertThat(metricVolume.getTotalBaseUnits(), is(MAXIMUM_VOLUME));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        // Set to max plus 1
        assertThat(metricVolume.totalMeasurementOneIsSet(10001), is(false));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(metricVolume.totalMeasurementOneIsSet(1.), is(true));

        // Check set
        assertThat(metricVolume.getTotalMeasurementOne(), is(1.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(1.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(1.));
    }

    @Test
    public void testMeasurementUnit_One_Out_Of_Range_Min() { // OUT OF RANGE MIN

        // Set to .1 below min
        assertThat(metricVolume.totalMeasurementOneIsSet(0.9), is(false));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));
    }


    //////////////////////////// PACK MEASUREMENT TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMaximumInRangeMeasurementUnitTwo() { // IN RANGE MAX

        // Set to max
        assertThat(metricVolume.totalMeasurementTwoIsSet(10), is(true));

        // Check value set
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(10));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(10));

        System.out.println();
    }

    @Test
    public void testMaximumOutOfRangeMeasurementUnitTwo() { // OUT OF RANGE MAX

        // Set to max +1
        assertThat(metricVolume.totalMeasurementTwoIsSet(11), is(false));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMinInRangeMeasurementUnitTwo() { // IN RANGE MIN

        // Set to min
        assertThat(metricVolume.totalMeasurementTwoIsSet(1), is(true));

        // Check value set
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(1));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(1));
        assertThat(metricVolume.getTotalBaseUnits(), is(1000.));
    }

    @Test
    public void testMinimumOutOfRangeMeasurementTwo() { // OUT OF RANGE MIN

        // Set to min -1
        assertThat(metricVolume.totalMeasurementTwoIsSet(-1), is(false));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));
    }

    //////////////////////////// PACK ONE AND TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testInRangeMeasurementOneAndTwo() { // CONDITION: IN RANGE

        // Set arbitrary in range value
        assertThat(metricVolume.totalMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.totalMeasurementTwoIsSet(5), is(true));

        // Check values set
        assertThat(metricVolume.getTotalMeasurementOne(), is(500.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(5));
        assertThat(metricVolume.getItemMeasurementOne(), is(500.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(5));
        assertThat(metricVolume.getTotalBaseUnits(), is(5500.));
    }

    //TODO////////////////////////// ITEM ONE AND TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    //////////////////////////// SETTING NUMBER OF ITEMS TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testSetNumberOfItemsMinInRangeWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - IN RANGE MIN

        // Set arbitrary number of items, base si at zero
        assertThat(metricVolume.numberOfItemsIsSet(5), is(true));

        // Check set
        assertThat(metricVolume.getNumberOfItems(), is(5));
        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testSetNumberOfItemsMinOutOfRangeWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MIN

        // Set out of range min
        assertThat(metricVolume.numberOfItemsIsSet(0), is(false));

        // Check values unchanged (1 is default)
        assertThat(metricVolume.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMaxWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - IN RANGE MAX

        // Set to max within range
        assertThat(metricVolume.numberOfItemsIsSet(999), is(true));

        // Check set
        assertThat(metricVolume.getNumberOfItems(), is(999));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeMaxWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MAX

        // Set to max +1
        assertThat(metricVolume.numberOfItemsIsSet(1000), is(false));

        // Check values unchanged
        assertThat(metricVolume.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMinWithBaseUnits() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE MIN

        // Set value to pack measurement - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(metricVolume.totalMeasurementOneIsSet(2), is(true));

        // Set number of items
        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));

        // Check item measurement changed
        assertThat(metricVolume.getTotalMeasurementOne(), is(2.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(1.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(2.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE (ODD)

        // Set pack to number not divisible by number of items
        assertThat(metricVolume.totalMeasurementOneIsSet(3), is(true));

        // Set number of items not divisible by pack size
        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));

        // Check item measurements have rounded correctly
        assertThat(metricVolume.getTotalMeasurementOne(), is(3.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementOne(), is(1.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(3.));
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE & TWO - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(metricVolume.totalMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.totalMeasurementTwoIsSet(1), is(true));

        // Set number of items
        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));

        // Check item measurements have changed
        assertThat(metricVolume.getTotalMeasurementOne(), is(500.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(1));
        assertThat(metricVolume.getItemMeasurementOne(), is(750.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(1500.));
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricVolume.itemMeasurementOneIsSet(500), is(true));

        // Set number of items
        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));

        // Check pack measurement have changed
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(1));
        assertThat(metricVolume.getItemMeasurementOne(), is(500.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(1000.));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricVolume.itemMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.itemMeasurementTwoIsSet(1), is(true));

        // Set items
        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(3));
        assertThat(metricVolume.getItemMeasurementOne(), is(500.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(1));
        assertThat(metricVolume.getTotalBaseUnits(), is(3000.));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricVolume.itemMeasurementOneIsSet(1), is(true));
        assertThat(metricVolume.itemMeasurementTwoIsSet(5), is(true));

        // Set items so to high
        assertThat(metricVolume.numberOfItemsIsSet(2), is(false));

        // Check values unchanged
        assertThat(metricVolume.getTotalMeasurementOne(), is(1.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(5));
        assertThat(metricVolume.getItemMeasurementOne(), is(1.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(5));
        assertThat(metricVolume.getTotalBaseUnits(), is(5001.));
    }

    @Test
    public void testNumberOfItemsInRange_SetWithItemBaseUnitsThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(metricVolume.itemMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.itemMeasurementTwoIsSet(1), is(true));

        // Change number of items
        assertThat(metricVolume.numberOfItemsIsSet(3), is(true));

        // Check pack measurement has changed
        assertThat(metricVolume.getTotalMeasurementOne(), is(500.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(4));
        assertThat(metricVolume.getItemMeasurementOne(), is(500.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(1));
        assertThat(metricVolume.getTotalBaseUnits(), is(4500.));

        // Set item measurement again
        assertThat(metricVolume.numberOfItemsIsSet(5), is(true));

        // Check pack measurement changed
        assertThat(metricVolume.getTotalMeasurementOne(), is(500.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(7));
        assertThat(metricVolume.getItemMeasurementOne(), is(500.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(1));
        assertThat(metricVolume.getTotalBaseUnits(), is(7500.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWith_ItemWithBaseUnits() {
        // CONDITION: BASE SI SET BY PACK - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set pack measurement last changed by setting pack measurement
        assertThat(metricVolume.totalMeasurementTwoIsSet(10), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(MAXIMUM_VOLUME));

        // Change number of items
        assertThat(metricVolume.numberOfItemsIsSet(10), is(true));

        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(10));

        // Check item measurements have changed
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(1));
        assertThat(metricVolume.getTotalBaseUnits(), is(MAXIMUM_VOLUME));

        // Change number of items
        assertThat(metricVolume.numberOfItemsIsSet(20), is(true));

        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(10));
        // Check item measurements have changed
        assertThat(metricVolume.getItemMeasurementOne(), is(500.));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
        assertThat(metricVolume.getTotalBaseUnits(), is(MAXIMUM_VOLUME));
    }

    @Test
    public void testMixedNumberReturnValues() {

        assertThat(metricVolume.totalBaseUnitsAreSet(5), is(true));
        assertThat(metricVolume.numberOfItemsIsSet(3), is(true));
        assertThat(metricVolume.getTotalMeasurementOne(), is(5.));
        assertThat(metricVolume.getItemMeasurementOne(), is(1.0));
    }

    @Test
    public void test_setting_pack_one() {

        assertThat(metricVolume.getTotalBaseUnits(), is(0.));
        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));
        assertThat(metricVolume.totalMeasurementOneIsSet(2.), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(2.));
        assertThat(metricVolume.totalMeasurementOneIsSet(20.), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(20.));
    }

    @Test
    public void settingBaseUnits() {

        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));
        assertThat(metricVolume.totalMeasurementOneIsSet(2), is(true));
        assertThat(metricVolume.totalMeasurementOneIsSet(20.), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(20.));
        assertThat(metricVolume.getTotalMeasurementOne(), is(20.));
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {

        // Setup
        assertThat(metricVolume.numberOfItemsIsSet(2), is(true));
        assertThat(metricVolume.totalMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.totalMeasurementTwoIsSet(1), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(1500.));

        // Gradual teardown, as the user would type
        assertThat(metricVolume.totalMeasurementTwoIsSet(0), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(500.));

        assertThat(metricVolume.totalMeasurementOneIsSet(50), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(50.));

        assertThat(metricVolume.totalMeasurementOneIsSet(5), is(true));
        assertThat(metricVolume.getTotalBaseUnits(), is(5.));

        assertThat(metricVolume.totalBaseUnitsAreSet(0), is(false));
        assertThat(metricVolume.getTotalBaseUnits(), is(0.));

        assertThat(metricVolume.getTotalMeasurementOne(), is(0.));
        assertThat(metricVolume.getItemMeasurementOne(), is(0.));
        assertThat(metricVolume.getTotalMeasurementTwo(), is(0));
        assertThat(metricVolume.getItemMeasurementTwo(), is(0));
    }
}