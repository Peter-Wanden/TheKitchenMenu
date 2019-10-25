package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ImperialSpoonTest {

    // region constants ----------------------------------------------------------------------------
    private double DELTA = 0.00001;
    private double TABLESPOON_VOLUME = IMPERIAL_SPOON_UNIT_TWO;
    private double TEASPOON_VOLUME = IMPERIAL_SPOON_UNIT_ONE;

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private ImperialSpoon SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new ImperialSpoon();
    }

    @Test
    public void setConversionFactorOutOfRangeMax_setFalse() {
        // Arrange
        // Act
        // Assert
        assertFalse(SUT.conversionFactorIsSet(MAX_CONVERSION_FACTOR + 0.0001));
    }

    @Test
    public void setConversionFactor_outOfRangeMin_setFalse() {
        // Arrange
        // Act
        // Assert
        assertFalse(SUT.conversionFactorIsSet(MIN_CONVERSION_FACTOR - 0.0001));
    }

    // Test data from Butter Chicken recipe
    @Test
    public void testButter() {
        // Arrange
        double conversionFactor = 0.9595; // the weight of 1ml (1cm3) of butter
        int numberOfPortions = 16;
        int numberOfTablespoons = 4;

        double expectedTotal = numberOfTablespoons * TABLESPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotal / numberOfPortions;

        // Act
        assertTrue(SUT.conversionFactorIsSet(conversionFactor));
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.totalMeasurementTwoIsSet(numberOfTablespoons));
        // Assert
        assertEquals(expectedTotal, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void testGarlicPuree() {
        // Arrange
        double conversionFactor = 0.574;
        int numberOfPortions = 16;
        int numberOfTeaspoons = 6;

        double expectedTotal = numberOfTeaspoons * TEASPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotal / numberOfPortions;
        // Act
        assertTrue(SUT.conversionFactorIsSet(conversionFactor));
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.totalMeasurementOneIsSet(numberOfTeaspoons));
        // Assert
        assertEquals(expectedTotal, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void testGroundGinger() {
        // Arrange
        double conversionFactor = 0.36;
        int numberOfPortions = 16;
        int numberOfTableSpoons = 2;

        double expectedTotal = numberOfTableSpoons * TABLESPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotal / numberOfPortions;
        // Act
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.conversionFactorIsSet(conversionFactor));
        assertTrue(SUT.totalMeasurementTwoIsSet(numberOfTableSpoons));
        // Assert
        assertEquals(expectedTotal, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    // Test data from Home Made Chili recipe
    @Test
    public void testBartMarjoram() {
        // Arrange
        double conversionFactor = 0.12; // 1cm3=ng of marjaoram, very light!
        int numberOfPortions = 4;
        double numberOfTeaSpoons = 0.5;

        double expectedTotalQuantity = numberOfTeaSpoons * TEASPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotalQuantity / numberOfPortions;
        // Act
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.conversionFactorIsSet(conversionFactor));
        assertTrue(SUT.totalMeasurementOneIsSet(numberOfTeaSpoons));
        // Assert
        assertEquals(expectedTotalQuantity, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void conversionFactorIsSet_beforeMeasurementSet_itemBaseUnitsAsExpected() {
        // Arrange
        double conversionFactor = 0.5;
        int numberOfPortions = 4;
        double numberOfTeaspoons = 1;

        double expectedTotalQuantity = numberOfTeaspoons * TEASPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotalQuantity / numberOfPortions;
        // Act
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.conversionFactorIsSet(conversionFactor));
        assertTrue(SUT.totalMeasurementOneIsSet(numberOfTeaspoons));
        // Assert
        assertEquals(expectedTotalQuantity, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void conversionFactorIsSet_afterMeasurement_itemBaseUnitsCorrect() {
        // Arrange
        double conversionFactor = 0.5;
        int numberOfPortions = 4;
        double numberOfTeaspoons = 1;

        double expectedTotalQuantity = numberOfTeaspoons * TEASPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotalQuantity / numberOfPortions;
        // Act
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.totalMeasurementOneIsSet(numberOfTeaspoons));
        assertTrue(SUT.conversionFactorIsSet(conversionFactor));
        // Assert
        assertEquals(expectedTotalQuantity, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_measurementBelowZero_measurementSetOk() {
        // Arrange
        double conversionFactor = 1;
        int numberOfPortions = 1;
        double numberOfTeaspoons = 0.5;

        double expectedTotalQuantity = numberOfTeaspoons * TEASPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotalQuantity / numberOfPortions;
        // Act
        // Assert
        assertTrue(SUT.totalMeasurementOneIsSet(numberOfTeaspoons));
        assertEquals(expectedTotalQuantity, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_minimumMeasurementOne_measurementSetOk() {
        // Arrange
        double conversionFactor = 0.5;
        double numberOfTeaspoons = 0.1; // A pinch of salt!
        int numberOfPortions = 2;

        double expectedTotalQuantity = numberOfTeaspoons * TEASPOON_VOLUME * conversionFactor;
        double expectedQuantityPerServing = expectedTotalQuantity / numberOfPortions;
        // Act
        assertTrue(SUT.totalMeasurementOneIsSet(numberOfTeaspoons));
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.conversionFactorIsSet(conversionFactor));
        // Assert
        assertEquals(expectedTotalQuantity, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void name() {
        // Arrange
        double numberOfTeaspoons = 2;
        int numberOfPortions = 9;

        double expectedTotalQuantity = numberOfTeaspoons * TEASPOON_VOLUME * MAX_CONVERSION_FACTOR;
        double expectedQuantityPerServing = expectedTotalQuantity / numberOfPortions;
        // Act
        assertTrue(SUT.totalMeasurementOneIsSet(numberOfTeaspoons));
        assertTrue(SUT.numberOfItemsIsSet(numberOfPortions));
        assertTrue(SUT.conversionFactorIsSet(MAX_CONVERSION_FACTOR));
        // Assert
        assertEquals(expectedTotalQuantity, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedQuantityPerServing, SUT.getItemBaseUnits(), DELTA);
        System.out.println(SUT);
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}