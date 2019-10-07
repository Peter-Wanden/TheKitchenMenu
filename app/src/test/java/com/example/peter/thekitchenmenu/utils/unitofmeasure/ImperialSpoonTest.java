package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.*;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ImperialSpoonTest {

    // region constants ----------------------------------------------------------------------------
    private double DELTA = 0.00001;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private ImperialSpoon SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new ImperialSpoon();

    }

    // Test data from Butter Chicken recipe
    @Test
    public void testButter() {
        // Arrange
        int fourTablespoons = 4;
        double oneMlEqNGrams = 0.9595;
        double quantityPerServing = 3.598125;
        int numberOfPortions = 16;
        double total = 57.57;
        // Act
        SUT.setOneMLEqualsNGrams(oneMlEqNGrams);
        SUT.numberOfItemsIsSet(numberOfPortions);
        SUT.totalMeasurementTwoIsSet(fourTablespoons);
        // Assert
        assertEquals(total, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(quantityPerServing, SUT.getItemSizeInBaseUnits(), DELTA);
    }

    @Test
    public void testGarlicPuree() {
        // Arrange
        int numberOfPortions = 16;
        int fourTeaspoons = 6;
        double oneMlEqNGrams = 0.574;
        double quantityPerServing = 1.07625;
        double total = 17.22;
        // Act
        SUT.setOneMLEqualsNGrams(oneMlEqNGrams);
        SUT.numberOfItemsIsSet(numberOfPortions);
        SUT.totalMeasurementOneIsSet(fourTeaspoons);
        // Assert
        assertEquals(total, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(quantityPerServing, SUT.getItemSizeInBaseUnits(), DELTA);
    }

    @Test
    public void testGroundGinger() {
        // Arrange
        int numberOfPortions = 16;
        int twoTableSpoons = 2;
        double oneMlEqNGrams = 0.36;

        double quantityPerServing = .675;
        double total = 10.8;
        // Act
        SUT.numberOfItemsIsSet(numberOfPortions);
        SUT.setOneMLEqualsNGrams(oneMlEqNGrams);
        SUT.totalMeasurementTwoIsSet(twoTableSpoons);
        // Assert
        assertEquals(total, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(quantityPerServing, SUT.getItemSizeInBaseUnits(), DELTA);
    }

    // Test data from Home Made Chili recipe
    @Test
    public void testBartMarjoram() {
        // Arrange
        int numberOfPortions = 4;
        double halfATeaSpoon = 0.5;
        double oneMlEqNGrams = 0.12;

        double quantityPerServing = .075;
        double total = 0.3;
        // Act
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        SUT.setOneMLEqualsNGrams(oneMlEqNGrams);
        assertTrue(SUT.totalMeasurementOneIsSet(halfATeaSpoon));
        // Assert
        assertEquals(total, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(quantityPerServing, SUT.getItemSizeInBaseUnits(), DELTA);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}