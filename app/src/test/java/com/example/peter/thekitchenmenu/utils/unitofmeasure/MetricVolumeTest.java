package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_VOLUME;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MetricVolumeTest {

    private MetricVolume metricVolume = new MetricVolume();

    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseSiInRangeMin() { // IN RANGE MIN

        assertThat(metricVolume.baseUnitsAreSet(1), is(true));

        assertThat(metricVolume.getPackMeasurementOne(), is(1.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(1.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(1.));

        System.out.println();
    }

    @Test
    public void testBaseSiOutOfRangeMin() { // OUT OF RANGE MIN

        assertThat(metricVolume.baseUnitsAreSet(0.9), is(false));

        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiInRangeMax() { // IN RANGE MAX

        assertThat(metricVolume.baseUnitsAreSet(MAXIMUM_VOLUME), is(true));

        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(10));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(10));
        assertThat(metricVolume.getBaseUnits(), is(MAXIMUM_VOLUME));

        System.out.println();
    }

    @Test
    public void testBaseSiOutOfRangeMax() { // OUT OF RANGE MAX

        assertThat(metricVolume.baseUnitsAreSet(MAXIMUM_VOLUME + 1), is(false));

        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiViolatesMinimumItemSize() { // CONDITION: BASE SI SMALLER THAN SMALLEST ITEM

        assertThat(metricVolume.numberOfProductsIsSet(5), is(true));

        assertThat(metricVolume.baseUnitsAreSet(4), is(false));

        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        assertThat(metricVolume.numberOfProductsIsSet(5), is(true));

        assertThat(metricVolume.baseUnitsAreSet(5), is(true));

        assertThat(metricVolume.getPackMeasurementOne(), is(5.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(1.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(5.));

        System.out.println();
    }

    @Test
    public void testBaseSiRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(metricVolume.baseUnitsAreSet(5500), is(true));

        // Check pack and item values have updated correctly
        assertThat(metricVolume.getPackMeasurementOne(), is(500.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(5));
        assertThat(metricVolume.getProductMeasurementOne(), is(500.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(5));
        assertThat(metricVolume.getBaseUnits(), is(5500.));

        System.out.println();
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        // Set to max
        assertThat(metricVolume.packMeasurementOneIsSet(MAXIMUM_VOLUME), is(true));

        // Check value set
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(10));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(10));
        assertThat(metricVolume.getBaseUnits(), is(MAXIMUM_VOLUME));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        // Set to max plus 1
        assertThat(metricVolume.packMeasurementOneIsSet(10001), is(false));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(metricVolume.packMeasurementOneIsSet(1.), is(true));

        // Check set
        assertThat(metricVolume.getPackMeasurementOne(), is(1.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(1.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(1.));
    }

    @Test
    public void testMeasurementUnit_One_Out_Of_Range_Min() { // OUT OF RANGE MIN

        // Set to .1 below min
        assertThat(metricVolume.packMeasurementOneIsSet(0.9), is(false));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));
    }


    //////////////////////////// PACK MEASUREMENT TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMaximumInRangeMeasurementUnitTwo() { // IN RANGE MAX

        // Set to max
        assertThat(metricVolume.packMeasurementTwoIsSet(10), is(true));

        // Check value set
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(10));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(10));

        System.out.println();
    }

    @Test
    public void testMaximumOutOfRangeMeasurementUnitTwo() { // OUT OF RANGE MAX

        // Set to max +1
        assertThat(metricVolume.packMeasurementTwoIsSet(11), is(false));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMinInRangeMeasurementUnitTwo() { // IN RANGE MIN

        // Set to min
        assertThat(metricVolume.packMeasurementTwoIsSet(1), is(true));

        // Check value set
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(1));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(1));
        assertThat(metricVolume.getBaseUnits(), is(1000.));
    }

    @Test
    public void testMinimumOutOfRangeMeasurementTwo() { // OUT OF RANGE MIN

        // Set to min -1
        assertThat(metricVolume.packMeasurementTwoIsSet(-1), is(false));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));
    }

    //////////////////////////// PACK ONE AND TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testInRangeMeasurementOneAndTwo() { // CONDITION: IN RANGE

        // Set arbitrary in range value
        assertThat(metricVolume.packMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.packMeasurementTwoIsSet(5), is(true));

        // Check values set
        assertThat(metricVolume.getPackMeasurementOne(), is(500.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(5));
        assertThat(metricVolume.getProductMeasurementOne(), is(500.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(5));
        assertThat(metricVolume.getBaseUnits(), is(5500.));
    }

    //TODO////////////////////////// ITEM ONE AND TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    //////////////////////////// SETTING NUMBER OF ITEMS TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testSetNumberOfItemsMinInRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - IN RANGE MIN

        // Set arbitrary number of items, base si at zero
        assertThat(metricVolume.numberOfProductsIsSet(5), is(true));

        // Check set
        assertThat(metricVolume.getNumberOfProducts(), is(5));
        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));
    }

    @Test
    public void testSetNumberOfItemsMinOutOfRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MIN

        // Set out of range min
        assertThat(metricVolume.numberOfProductsIsSet(0), is(false));

        // Check values unchanged (1 is default)
        assertThat(metricVolume.getNumberOfProducts(), is(1));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMaxWithNoBaseSI() { // CONDITION: BASE SI NOT YET SET - IN RANGE MAX

        // Set to max within range
        assertThat(metricVolume.numberOfProductsIsSet(999), is(true));

        // Check set
        assertThat(metricVolume.getNumberOfProducts(), is(999));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeMaxWithNoBaseSI() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MAX

        // Set to max +1
        assertThat(metricVolume.numberOfProductsIsSet(1000), is(false));

        // Check values unchanged
        assertThat(metricVolume.getNumberOfProducts(), is(1));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMinWithBaseSI() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE MIN

        // Set value to pack measurement - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(metricVolume.packMeasurementOneIsSet(2), is(true));

        // Set number of items
        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));

        // Check item measurement changed
        assertThat(metricVolume.getPackMeasurementOne(), is(2.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(1.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(2.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE (ODD)

        // Set pack to number not divisible by number of items
        assertThat(metricVolume.packMeasurementOneIsSet(3), is(true));

        // Set number of items not divisible by pack size
        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));

        // Check item measurements have rounded correctly
        assertThat(metricVolume.getPackMeasurementOne(), is(3.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementOne(), is(1.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(3.));
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE & TWO - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(metricVolume.packMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.packMeasurementTwoIsSet(1), is(true));

        // Set number of items
        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));

        // Check item measurements have changed
        assertThat(metricVolume.getPackMeasurementOne(), is(500.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(1));
        assertThat(metricVolume.getProductMeasurementOne(), is(750.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(1500.));
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricVolume.productMeasurementOneIsSet(500), is(true));

        // Set number of items
        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));

        // Check pack measurement have changed
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(1));
        assertThat(metricVolume.getProductMeasurementOne(), is(500.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(1000.));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricVolume.productMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.productMeasurementTwoIsSet(1), is(true));

        // Set items
        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(3));
        assertThat(metricVolume.getProductMeasurementOne(), is(500.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(1));
        assertThat(metricVolume.getBaseUnits(), is(3000.));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricVolume.productMeasurementOneIsSet(1), is(true));
        assertThat(metricVolume.productMeasurementTwoIsSet(5), is(true));

        // Set items so to high
        assertThat(metricVolume.numberOfProductsIsSet(2), is(false));

        // Check values unchanged
        assertThat(metricVolume.getPackMeasurementOne(), is(1.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(5));
        assertThat(metricVolume.getProductMeasurementOne(), is(1.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(5));
        assertThat(metricVolume.getBaseUnits(), is(5001.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWithItemWithBaseSiThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(metricVolume.productMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.productMeasurementTwoIsSet(1), is(true));

        // Change number of items
        assertThat(metricVolume.numberOfProductsIsSet(3), is(true));

        // Check pack measurement has changed
        assertThat(metricVolume.getPackMeasurementOne(), is(500.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(4));
        assertThat(metricVolume.getProductMeasurementOne(), is(500.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(1));
        assertThat(metricVolume.getBaseUnits(), is(4500.));

        // Set item measurement again
        assertThat(metricVolume.numberOfProductsIsSet(5), is(true));

        // Check pack measurement changed
        assertThat(metricVolume.getPackMeasurementOne(), is(500.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(7));
        assertThat(metricVolume.getProductMeasurementOne(), is(500.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(1));
        assertThat(metricVolume.getBaseUnits(), is(7500.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWith_ItemWithBaseSI() {
        // CONDITION: BASE SI SET BY PACK - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set pack measurement last changed by setting pack measurement
        assertThat(metricVolume.packMeasurementTwoIsSet(10), is(true));
        assertThat(metricVolume.getBaseUnits(), is(MAXIMUM_VOLUME));

        // Change number of items
        assertThat(metricVolume.numberOfProductsIsSet(10), is(true));

        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(10));

        // Check item measurements have changed
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(1));
        assertThat(metricVolume.getBaseUnits(), is(MAXIMUM_VOLUME));

        // Change number of items
        assertThat(metricVolume.numberOfProductsIsSet(20), is(true));

        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(10));
        // Check item measurements have changed
        assertThat(metricVolume.getProductMeasurementOne(), is(500.));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
        assertThat(metricVolume.getBaseUnits(), is(MAXIMUM_VOLUME));
    }

    @Test
    public void testMixedNumberReturnValues() {

        assertThat(metricVolume.baseUnitsAreSet(5), is(true));
        assertThat(metricVolume.numberOfProductsIsSet(3), is(true));
        assertThat(metricVolume.getPackMeasurementOne(), is(5.));
        assertThat(metricVolume.getProductMeasurementOne(), is(1.0));
    }

    @Test
    public void test_setting_pack_one() {

        assertThat(metricVolume.getBaseUnits(), is(0.));
        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));
        assertThat(metricVolume.packMeasurementOneIsSet(2.), is(true));
        assertThat(metricVolume.getBaseUnits(), is(2.));
        assertThat(metricVolume.packMeasurementOneIsSet(20.), is(true));
        assertThat(metricVolume.getBaseUnits(), is(20.));
    }

    @Test
    public void settingBaseSi() {

        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));
        assertThat(metricVolume.packMeasurementOneIsSet(2), is(true));
        assertThat(metricVolume.packMeasurementOneIsSet(20.), is(true));
        assertThat(metricVolume.getBaseUnits(), is(20.));
        assertThat(metricVolume.getPackMeasurementOne(), is(20.));
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {

        // Setup
        assertThat(metricVolume.numberOfProductsIsSet(2), is(true));
        assertThat(metricVolume.packMeasurementOneIsSet(500), is(true));
        assertThat(metricVolume.packMeasurementTwoIsSet(1), is(true));
        assertThat(metricVolume.getBaseUnits(), is(1500.));

        // Gradual teardown, as the user would type
        assertThat(metricVolume.packMeasurementTwoIsSet(0), is(true));
        assertThat(metricVolume.getBaseUnits(), is(500.));

        assertThat(metricVolume.packMeasurementOneIsSet(50), is(true));
        assertThat(metricVolume.getBaseUnits(), is(50.));

        assertThat(metricVolume.packMeasurementOneIsSet(5), is(true));
        assertThat(metricVolume.getBaseUnits(), is(5.));

        assertThat(metricVolume.baseUnitsAreSet(0), is(false));
        assertThat(metricVolume.getBaseUnits(), is(0.));

        assertThat(metricVolume.getPackMeasurementOne(), is(0.));
        assertThat(metricVolume.getProductMeasurementOne(), is(0.));
        assertThat(metricVolume.getPackMeasurementTwo(), is(0));
        assertThat(metricVolume.getProductMeasurementTwo(), is(0));
    }
}