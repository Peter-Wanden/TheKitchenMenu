package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import org.junit.Test;

import androidx.test.core.app.ApplicationProvider;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAX_MASS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MetricMassTest {

    private Context context = ApplicationProvider.getApplicationContext();
    private MetricMass metricMass = new MetricMass();

    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseSiInRangeMin() { // IN RANGE MIN

        assertThat(metricMass.baseSiUnitsAreSet(1), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(1.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(1.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(1.));

        System.out.println();
    }

    @Test
    public void testBaseSiOutOfRangeMin() { // OUT OF RANGE MIN

        assertThat(metricMass.baseSiUnitsAreSet(0.9), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiInRangeMax() { // IN RANGE MAX

        assertThat(metricMass.baseSiUnitsAreSet(MAX_MASS), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(10));
        assertThat(metricMass.getBaseSiUnits(), is(MAX_MASS));

        System.out.println();
    }

    @Test
    public void testBaseSiOutOfRangeMax() { // OUT OF RANGE MAX

        assertThat(metricMass.baseSiUnitsAreSet(MAX_MASS + 1), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiViolatesMinimumItemSize() { // CONDITION: BASE SI SMALLER THAN SMALLEST ITEM

        metricMass.numberOfItemsAreSet(5);

        assertThat(metricMass.baseSiUnitsAreSet(4), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseSiAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        metricMass.numberOfItemsAreSet(5);

        assertThat(metricMass.baseSiUnitsAreSet(5), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(5.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(1.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(5.));

        System.out.println();
    }

    @Test
    public void testBaseSiRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(metricMass.baseSiUnitsAreSet(5500), is(true));

        // Check pack and item values have updated correctly
        assertThat(metricMass.getPackMeasurementOne(), is(500.));
        assertThat(metricMass.getPackMeasurementTwo(), is(5));
        assertThat(metricMass.getItemMeasurementOne(), is(500.));
        assertThat(metricMass.getItemMeasurementTwo(), is(5));
        assertThat(metricMass.getBaseSiUnits(), is(5500.));

        System.out.println();
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        // Set to max
        assertThat(metricMass.packMeasurementOneIsSet(MAX_MASS), is(true));

        // Check value set
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(10));
        assertThat(metricMass.getBaseSiUnits(), is(MAX_MASS));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        // Set to max plus 1
        assertThat(metricMass.packMeasurementOneIsSet(10001), is(false));

        // Check values unchanges
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(metricMass.packMeasurementOneIsSet(1.), is(true));

        // Check set
        assertThat(metricMass.getPackMeasurementOne(), is(1.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(1.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(1.));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMin() { // OUT OF RANGE MIN

        // Set to .1 below min
        assertThat(metricMass.packMeasurementOneIsSet(0.9), is(false));

        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    //////////////////////////// PACK MEASUREMENT TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMaximumInRangeMeasurementUnitTwo() { // IN RANGE MAX

        // Set to max
        assertThat(metricMass.packMeasurementTwoIsSet(10), is(true));

        // Check value set
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(10));

        System.out.println();
    }

    @Test
    public void testMaximumOutOfRangeMeasurementUnitTwo() { // OUT OF RANGE MAX

        // Set to max +1
        assertThat(metricMass.packMeasurementTwoIsSet(11), is(false));

        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMinInRangeMeasurementUnitTwo() { // IN RANGE MIN

        // Set to min
        assertThat(metricMass.packMeasurementTwoIsSet(1), is(true));

        // Check value set
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(1));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(1));
        assertThat(metricMass.getBaseSiUnits(), is(1000.));
    }

    @Test
    public void testMinimumOutOfRangeMeasurementTwo() { // OUT OF RANGE MIN

        // Set to min -1
        assertThat(metricMass.packMeasurementTwoIsSet(-1), is(false));

        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    //////////////////////////// PACK ONE AND TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testInRangeMeasurementOneAndTwo() { // CONDITION: IN RANGE

        // Set arbitrary in range value
        assertThat(metricMass.packMeasurementOneIsSet(500), is(true));
        assertThat(metricMass.packMeasurementTwoIsSet(5), is(true));

        // Check values set
        assertThat(metricMass.getPackMeasurementOne(), is(500.));
        assertThat(metricMass.getPackMeasurementTwo(), is(5));
        assertThat(metricMass.getItemMeasurementOne(), is(500.));
        assertThat(metricMass.getItemMeasurementTwo(), is(5));
        assertThat(metricMass.getBaseSiUnits(), is(5500.));
    }


    //////////////////////////// SETTING NUMBER OF ITEMS TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testSetNumberOfItemsMinInRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - IN RANGE MIN

        // Set arbitrary number of items, base si at zero
        assertThat(metricMass.numberOfItemsAreSet(5), is(true));

        // Check set
        assertThat(metricMass.getNumberOfItems(), is(5));
        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    @Test
    public void testSetNumberOfItemsMinOutOfRangeWithNoBaseSi() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MIN

        // Set out of range min
        assertThat(metricMass.numberOfItemsAreSet(0), is(false));

        // Check values unchanged (1 is default)
        assertThat(metricMass.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMaxWithNoBaseSI() { // CONDITION: BASE SI NOT YET SET - IN RANGE MAX

        // Set to max within range
        assertThat(metricMass.numberOfItemsAreSet(999), is(true));

        // Check set
        assertThat(metricMass.getNumberOfItems(), is(999));

        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeMaxWithNoBaseSI() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MAX

        // Set to max +1
        assertThat(metricMass.numberOfItemsAreSet(1000), is(false));

        // Check values unchanged
        assertThat(metricMass.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMinWithBaseSI() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE MIN

        // Set value to pack measurement - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(metricMass.packMeasurementOneIsSet(2), is(true));

        // Set number of items
        assertThat(metricMass.numberOfItemsAreSet(2), is(true));

        // Check item measurement changed
        assertThat(metricMass.getPackMeasurementOne(), is(2.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(1.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(2.));
    }

    @Test
    public void testNumberOfItemsOutOfRangeAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE (ODD)

        // Set pack to number not divisible by number of items
        assertThat(metricMass.packMeasurementOneIsSet(3), is(true));

        // Set number of items not divisible by pack size
        assertThat(metricMass.numberOfItemsAreSet(2), is(true));

        // Check item measurements have rounded correctly
        assertThat(metricMass.getPackMeasurementOne(), is(3.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(1.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(3.));
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE & TWO - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(metricMass.packMeasurementOneIsSet(500), is(true));
        assertThat(metricMass.packMeasurementTwoIsSet(1), is(true));

        // Set number of items
        assertThat(metricMass.numberOfItemsAreSet(2), is(true));

        // Check item measurements have changed
        assertThat(metricMass.getPackMeasurementOne(), is(500.));
        assertThat(metricMass.getPackMeasurementTwo(), is(1));
        assertThat(metricMass.getItemMeasurementOne(), is(750.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(1500.));
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricMass.itemMeasurementOneIsSet(500), is(true));

        // Set number of items
        assertThat(metricMass.numberOfItemsAreSet(2), is(true));

        // Check pack measurement have changed
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(1));
        assertThat(metricMass.getItemMeasurementOne(), is(500.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(1000.));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricMass.itemMeasurementOneIsSet(500), is(true));
        assertThat(metricMass.itemMeasurementTwoIsSet(1), is(true));

        // Set items
        assertThat(metricMass.numberOfItemsAreSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(3));
        assertThat(metricMass.getItemMeasurementOne(), is(500.));
        assertThat(metricMass.getItemMeasurementTwo(), is(1));
        assertThat(metricMass.getBaseSiUnits(), is(3000.));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(metricMass.itemMeasurementOneIsSet(1), is(true));
        assertThat(metricMass.itemMeasurementTwoIsSet(5), is(true));

        // Set items so to high
        assertThat(metricMass.numberOfItemsAreSet(2), is(false));

        // Check values unchanged
        assertThat(metricMass.getPackMeasurementOne(), is(1.));
        assertThat(metricMass.getPackMeasurementTwo(), is(5));
        assertThat(metricMass.getItemMeasurementOne(), is(1.));
        assertThat(metricMass.getItemMeasurementTwo(), is(5));
        assertThat(metricMass.getBaseSiUnits(), is(5001.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWithItemWithBaseSiThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(metricMass.itemMeasurementOneIsSet(500), is(true));
        assertThat(metricMass.itemMeasurementTwoIsSet(1), is(true));

        // Change number of items
        assertThat(metricMass.numberOfItemsAreSet(3), is(true));

        // Check pack measurement has changed
        assertThat(metricMass.getPackMeasurementOne(), is(500.));
        assertThat(metricMass.getPackMeasurementTwo(), is(4));
        assertThat(metricMass.getItemMeasurementOne(), is(500.));
        assertThat(metricMass.getItemMeasurementTwo(), is(1));
        assertThat(metricMass.getBaseSiUnits(), is(4500.));

        // Set item measurement again
        assertThat(metricMass.numberOfItemsAreSet(5), is(true));

        // Check pack measurement changed
        assertThat(metricMass.getPackMeasurementOne(), is(500.));
        assertThat(metricMass.getPackMeasurementTwo(), is(7));
        assertThat(metricMass.getItemMeasurementOne(), is(500.));
        assertThat(metricMass.getItemMeasurementTwo(), is(1));
        assertThat(metricMass.getBaseSiUnits(), is(7500.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWith_ItemWithBaseSI() {
        // CONDITION: BASE SI SET BY PACK - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set pack measurement last changed by setting pack measurement
        assertThat(metricMass.packMeasurementTwoIsSet(10), is(true));
        assertThat(metricMass.getBaseSiUnits(), is(MAX_MASS));

        // Change number of items
        assertThat(metricMass.numberOfItemsAreSet(10), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));

        // Check item measurements have changed
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(1));
        assertThat(metricMass.getBaseSiUnits(), is(MAX_MASS));

        // Change number of items
        assertThat(metricMass.numberOfItemsAreSet(20), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));
        // Check item measurements have changed
        assertThat(metricMass.getItemMeasurementOne(), is(500.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(MAX_MASS));
    }

    @Test
    public void testMixedNumberReturnValues() {

        assertThat(metricMass.baseSiUnitsAreSet(5), is(true));
        assertThat(metricMass.numberOfItemsAreSet(3), is(true));
        assertThat(metricMass.getPackMeasurementOne(), is(5.));
        assertThat(metricMass.getItemMeasurementOne(), is(1.0));
    }



    @Test
    public void getNumberOfMeasurementUnits() {
    }

    @Test
    public void setNumberOfItems() {
    }

    @Test
    public void getNumberOfItems() {
    }

    @Test
    public void getPackMeasurementThree() {
    }

    @Test
    public void setPackMeasurementThree() {
    }

    @Test
    public void getItemMeasurementOne() {
    }

    @Test
    public void setItemMeasurementOne() {
    }

    @Test
    public void getItemMeasurementTwo() {
    }

    @Test
    public void setItemMeasurementTwo() {
    }

    @Test
    public void getItemMeasurementThree() {
    }

    @Test
    public void setItemMeasurementThree() {
    }

    @Test
    public void resetNumericValues() {
    }
}