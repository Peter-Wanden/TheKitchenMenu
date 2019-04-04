package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import org.junit.Test;

import androidx.test.core.app.ApplicationProvider;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MetricMassTest {

    private Context context = ApplicationProvider.getApplicationContext();
    private MetricMass metricMass = new MetricMass(context);

    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseSiInRangeMin() { // IN RANGE MIN

        System.out.println("Setting bas SI to minimum (1)");
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

        System.out.println("Testing out of range minimum (0.9)");
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

        System.out.println("Testing in range max");
        assertThat(metricMass.baseSiUnitsAreSet(UnitOfMeasureConstants.MAX_MASS), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(10));
        assertThat(metricMass.getBaseSiUnits(), is(10000.));

        System.out.println();
    }

    @Test
    public void testBaseSiOutOfRangeMax() { // OUT OF RANGE MAX

        System.out.println("Testing out of range max + 1");
        assertThat(metricMass.baseSiUnitsAreSet(UnitOfMeasureConstants.MAX_MASS + 1), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        System.out.println("Testing max in range measurement unit one (10,000)");
        assertThat(metricMass.setPackMeasurementOne(10000), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(10));
        assertThat(metricMass.getBaseSiUnits(), is(10000.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        System.out.println("Testing max out of range measurement unit one (10,001)");
        assertThat(metricMass.setPackMeasurementOne(10001), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        System.out.println("Testing min out of range measurement unit one (1)");
        assertThat(metricMass.setPackMeasurementOne(1.), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(1.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(1.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(1.));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMin() { // OUT OF RANGE MIN

        System.out.println("Testing min out of range measurement unit one (0.9)");
        assertThat(metricMass.setPackMeasurementOne(0.9), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    //////////////////////////// PACK MEASUREMENT TWO TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMaximumInRangeMeasurementUnitTwo() { // IN RANGE MAX

        System.out.println("Testing max in range kilograms (10)");
        assertThat(metricMass.setPackMeasurementTwo(10), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(10));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(10));

        System.out.println();
    }

    @Test
    public void testMaximumOutOfRangeMeasurementUnitTwo() { // OUT OF RANGE MAX

        System.out.println("Testing max out of range kilograms (11)");
        assertThat(metricMass.setPackMeasurementTwo(11), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMinInRangeMeasurementUnitTwo() { // IN RANGE MIN

        System.out.println("Testing min in range measurement unit two (1)");
        assertThat(metricMass.setPackMeasurementTwo(1), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(1));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(1));
        assertThat(metricMass.getBaseSiUnits(), is(1000.));
    }

    @Test
    public void testMinimumOutOfRangeMeasurementTwo() { // OUT OF RANGE MIN

        System.out.println("Testing min out of range measurement two (-1)");
        assertThat(metricMass.setPackMeasurementTwo(-1), is(false));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    //////////////////////////// SETTING NUMBER OF ITEMS TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testSetNumberOfItemsWithNoBaseSi() { // CONDITION: NEW CLASS CREATED, BASE SI NOT YET SET

        System.out.println("Setting number of items on new instance");
        assertThat(metricMass.setNumberOfItems(5), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsWithBaseSI() {

        // TODO - double check values

        metricMass.baseSiUnitsAreSet(5);
        assertThat(metricMass.setNumberOfItems(5), is(true));

        assertThat(metricMass.getPackMeasurementOne(), is(0.));
        assertThat(metricMass.getPackMeasurementTwo(), is(0));
        assertThat(metricMass.getItemMeasurementOne(), is(0.));
        assertThat(metricMass.getItemMeasurementTwo(), is(0));
        assertThat(metricMass.getBaseSiUnits(), is(0.));
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