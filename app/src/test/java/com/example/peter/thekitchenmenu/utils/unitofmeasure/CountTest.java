package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_COUNT;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CountTest {

    private Count count = new Count();

    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseUnitsInRangeMin() { // IN RANGE MIN

        assertThat(count.baseUnitsAreSet(1), is(true));

        assertThat(count.getPackMeasurementOne(), is(1.));
        assertThat(count.getProductMeasurementOne(), is(1.));
        assertThat(count.getBaseUnits(), is(1.));
    }

    @Test
    public void testBaseUnitsOutOfRangeMin() { // OUT OF RANGE MIN

        assertThat(count.baseUnitsAreSet(0.9), is(false));

        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testBaseUnitsInRangeMax() { // IN RANGE MAX

        assertThat(count.baseUnitsAreSet(MAXIMUM_COUNT), is(true));

        assertThat(count.getPackMeasurementOne(), is(99.));
        assertThat(count.getProductMeasurementOne(), is(99.));
        assertThat(count.getBaseUnits(), is(99.));
    }

    @Test
    public void testBaseUnitsOutOfRangeMax() { // OUT OF RANGE MAX

        assertThat(count.baseUnitsAreSet(MAXIMUM_COUNT + 1), is(false));

        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testBaseUnitsViolatesMinimumItemSize() { // CONDITION: BASE SI SMALLER THAN SMALLEST ITEM

        assertThat(count.numberOfProductsIsSet(5), is(true));
        assertThat(count.baseUnitsAreSet(4), is(false));

        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testBaseUnitsAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        assertThat(count.numberOfProductsIsSet(5), is(true));
        assertThat(count.baseUnitsAreSet(5), is(true));

        assertThat(count.getPackMeasurementOne(), is(5.));
        assertThat(count.getProductMeasurementOne(), is(1.));
        assertThat(count.getBaseUnits(), is(5.));
    }

    @Test
    public void testBaseUnitsRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(count.baseUnitsAreSet(55), is(true));

        // Check pack and item values have updated correctly
        assertThat(count.getPackMeasurementOne(), is(55.));
        assertThat(count.getProductMeasurementOne(), is(55.));
        assertThat(count.getBaseUnits(), is(55.));
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        // Set to max
        assertThat(count.packMeasurementOneIsSet(MAXIMUM_COUNT), is(true));

        // Check value set
        assertThat(count.getPackMeasurementOne(), is(99.));
        assertThat(count.getProductMeasurementOne(), is(99.));
        assertThat(count.getBaseUnits(), is(99.));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        // Set to max plus 1
        assertThat(count.packMeasurementOneIsSet(100), is(false));

        // Check values unchanged
        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(count.packMeasurementOneIsSet(1.), is(true));

        // Check set
        assertThat(count.getPackMeasurementOne(), is(1.));
        assertThat(count.getProductMeasurementOne(), is(1.));
        assertThat(count.getBaseUnits(), is(1.));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMin() { // OUT OF RANGE MIN

        // Set to .1 below min
        assertThat(count.packMeasurementOneIsSet(0.9), is(false));

        // Check values unchanged
        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    //////////////////////////// SETTING NUMBER OF ITEMS TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testSetNumberOfItemsMinInRangeWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - IN RANGE MIN

        // Set arbitrary number of items, base si at zero
        assertThat(count.numberOfProductsIsSet(5), is(true));

        // Check set
        assertThat(count.getNumberOfProducts(), is(5));
        // Check values unchanged
        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testSetNumberOfItemsMinOutOfRangeWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MIN

        // Set out of range min
        assertThat(count.numberOfProductsIsSet(0), is(false));

        // Check values unchanged (1 is default)
        assertThat(count.getNumberOfProducts(), is(1));

        // Check values unchanged
        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMaxWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - IN RANGE MAX

        // Set to max within range
        assertThat(count.numberOfProductsIsSet(99), is(true));

        // Check set
        assertThat(count.getNumberOfProducts(), is(99));

        // Check values unchanged
        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeMaxWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MAX

        // Set to max +1
        assertThat(count.numberOfProductsIsSet(100), is(false));

        // Check values unchanged
        assertThat(count.getNumberOfProducts(), is(1));

        // Check values unchanged
        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMinWithBaseUnits() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE MIN

        // Set value to pack measurement - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(count.packMeasurementOneIsSet(2), is(true));

        // Set number of items
        assertThat(count.numberOfProductsIsSet(2), is(true));

        // Check item measurement changed
        assertThat(count.getPackMeasurementOne(), is(2.));
        assertThat(count.getProductMeasurementOne(), is(1.));
        assertThat(count.getBaseUnits(), is(2.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE (ODD)

        // Set pack to number not divisible by number of items
        assertThat(count.packMeasurementOneIsSet(3), is(true));

        // Set number of items not divisible by pack size
        assertThat(count.numberOfProductsIsSet(2), is(true));

        // Check item measurements have rounded correctly
        assertThat(count.getPackMeasurementOne(), is(3.));
        assertThat(count.getProductMeasurementOne(), is(1.5)); // Formatted to 1 by handler
        assertThat(count.getBaseUnits(), is(3.));
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(count.packMeasurementOneIsSet(10), is(true));

        // Set number of items
        assertThat(count.numberOfProductsIsSet(2), is(true));

        // Check item measurements have changed
        assertThat(count.getPackMeasurementOne(), is(10.));
        assertThat(count.getProductMeasurementOne(), is(5.));
        assertThat(count.getBaseUnits(), is(10.));
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(count.productMeasurementOneIsSet(5), is(true));

        // Set number of items
        assertThat(count.numberOfProductsIsSet(2), is(true));

        // Check pack measurement have changed
        assertThat(count.getPackMeasurementOne(), is(10.));
        assertThat(count.getProductMeasurementOne(), is(5.));
        assertThat(count.getBaseUnits(), is(10.));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(count.productMeasurementOneIsSet(500), is(true));

        // Set items
        assertThat(count.numberOfProductsIsSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(500.));
        assertThat(count.getBaseUnits(), is(3000.));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(count.productMeasurementOneIsSet(1), is(true));

        // Set items so to high
        assertThat(count.numberOfProductsIsSet(2), is(false));

        // Check values unchanged
        assertThat(count.getPackMeasurementOne(), is(1.));
        assertThat(count.getProductMeasurementOne(), is(1.));
        assertThat(count.getBaseUnits(), is(5001.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWithItemWithBaseUnitsThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(count.productMeasurementOneIsSet(500), is(true));

        // Change number of items
        assertThat(count.numberOfProductsIsSet(3), is(true));

        // Check pack measurement has changed
        assertThat(count.getPackMeasurementOne(), is(500.));
        assertThat(count.getProductMeasurementOne(), is(500.));
        assertThat(count.getBaseUnits(), is(4500.));

        // Set item measurement again
        assertThat(count.numberOfProductsIsSet(5), is(true));

        // Check pack measurement changed
        assertThat(count.getPackMeasurementOne(), is(500.));
        assertThat(count.getProductMeasurementOne(), is(500.));
        assertThat(count.getBaseUnits(), is(7500.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWith_ItemWithBaseUnits() {
        // CONDITION: BASE SI SET BY PACK - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set pack measurement last changed by setting pack measurement
        assertThat(count.getBaseUnits(), is(MAXIMUM_COUNT));

        // Change number of items
        assertThat(count.numberOfProductsIsSet(10), is(true));

        assertThat(count.getPackMeasurementOne(), is(0.));

        // Check item measurements have changed
        assertThat(count.getProductMeasurementOne(), is(0.));
        assertThat(count.getBaseUnits(), is(MAXIMUM_COUNT));

        // Change number of items
        assertThat(count.numberOfProductsIsSet(20), is(true));

        assertThat(count.getPackMeasurementOne(), is(0.));
        // Check item measurements have changed
        assertThat(count.getProductMeasurementOne(), is(500.));
        assertThat(count.getBaseUnits(), is(MAXIMUM_COUNT));
    }

    @Test
    public void testMixedNumberReturnValues() {

        assertThat(count.baseUnitsAreSet(5), is(true));
        assertThat(count.numberOfProductsIsSet(3), is(true));
        assertThat(count.getPackMeasurementOne(), is(5.));
        assertThat(count.getProductMeasurementOne(), is(1.0));
    }

    @Test
    public void test_setting_pack_one() {

        assertThat(count.getBaseUnits(), is(0.));
        assertThat(count.numberOfProductsIsSet(2), is(true));
        assertThat(count.packMeasurementOneIsSet(2.), is(true));
        assertThat(count.getBaseUnits(), is(2.));
        assertThat(count.packMeasurementOneIsSet(20.), is(true));
        assertThat(count.getBaseUnits(), is(20.));
    }

    @Test
    public void settingBaseUnits() {

        assertThat(count.numberOfProductsIsSet(2), is(true));
        assertThat(count.packMeasurementOneIsSet(2), is(true));
        assertThat(count.packMeasurementOneIsSet(20.), is(true));
        assertThat(count.getBaseUnits(), is(20.));
        assertThat(count.getPackMeasurementOne(), is(20.));
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {

        // Setup
        assertThat(count.numberOfProductsIsSet(2), is(true));
        assertThat(count.packMeasurementOneIsSet(500), is(true));
        assertThat(count.getBaseUnits(), is(1500.));

        // Gradual teardown, as the user would type
        assertThat(count.packMeasurementTwoIsSet(0), is(true));
        assertThat(count.getBaseUnits(), is(500.));

        assertThat(count.packMeasurementOneIsSet(50), is(true));
        assertThat(count.getBaseUnits(), is(50.));

        assertThat(count.packMeasurementOneIsSet(5), is(true));
        assertThat(count.getBaseUnits(), is(5.));

        assertThat(count.baseUnitsAreSet(0), is(false));
        assertThat(count.getBaseUnits(), is(0.));

        assertThat(count.getPackMeasurementOne(), is(0.));
        assertThat(count.getProductMeasurementOne(), is(0.));
    }
}