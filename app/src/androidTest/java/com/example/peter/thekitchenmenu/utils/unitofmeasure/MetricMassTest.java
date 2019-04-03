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
    public void getBaseSiUnits() {
    }

    @Test
    public void baseSiUnitsAreSet() {

    }

    @Test
    public void getNumberOfMeasurementUnits() {
    }

    @Test
    public void setNumberOfItems() {
    }

    @Test
    public void getMinAndMax() {
    }

    @Test
    public void getNumberOfItems() {
    }

    @Test
    public void getPackMeasurementOne() {
    }

    @Test
    public void setPackMeasurementOne() {
    }

    @Test
    public void getPackMeasurementTwo() {
    }

    @Test
    public void setPackMeasurementTwo() {
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