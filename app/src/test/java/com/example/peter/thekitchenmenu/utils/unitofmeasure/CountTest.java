package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Before;
import org.junit.Test;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;
import static org.junit.Assert.*;

public class CountTest {

    // region constants ----------------------------------------------------------------------------
    private double DELTA = 1;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private UnitOfMeasure SUT;

    @Before
    public void setUp() {
        SUT = new Count();
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMin_true() {
        // Arrange
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(1));
        // Assert
        assertEquals(1, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalBaseUnitsAreSet(0.9));
        // Assert
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(MAX_COUNT));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalMeasurementTwo(), DELTA);
        assertEquals(MAX_COUNT, SUT.getItemMeasurementTwo(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalBaseUnitsAreSet((MAX_COUNT + 1)));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_totalBaseUnitsSetToLowerValueThanNumberOfItems_numberOfItemsAutoAdjustedToBaseUnitValue() {
        // Arrange
        int numberOfItemsHigherThanBaseUnits = 10;
        double baseUnitsLowerThanNumberOfItems = 5;
        int expectedNoOfItemsAdjustment = (int) (numberOfItemsHigherThanBaseUnits - baseUnitsLowerThanNumberOfItems);
        // Act
        assertTrue(SUT.numberOfItemsIsSet(numberOfItemsHigherThanBaseUnits));
        assertTrue(SUT.totalBaseUnitsAreSet(baseUnitsLowerThanNumberOfItems));
        // Assert
        assertEquals(expectedNoOfItemsAdjustment, SUT.getNumberOfItems());
    }

    @Test
    public void numberOfItemsIsSet_totalBaseUnitsAreSet_sameValuesOk() {
        // Arrange
        // Act
        assertTrue(SUT.numberOfItemsIsSet(5));
        assertTrue(SUT.totalBaseUnitsAreSet(5));
        // Assert
        assertEquals(5, SUT.getTotalMeasurementTwo());
        assertEquals(5, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_numberOfItemsAreSet_totalAndItemMeasurementsUpdateAsExpected() {
        // Arrange
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(MAX_COUNT));
        assertTrue(SUT.numberOfItemsIsSet((2)));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalMeasurementTwo());
        assertEquals((MAX_COUNT / 2), SUT.getItemMeasurementTwo());
    }

    @Test
    public void totalMeasurementOneIsSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(MAX_COUNT));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalMeasurementTwo(), DELTA);
        assertEquals(MAX_COUNT, SUT.getItemMeasurementTwo(), DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalMeasurementTwoIsSet((MAX_COUNT + 1)));
        // Assert
        assertEquals(MIN_COUNT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_inRange_min() {
        // Arrange
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(MIN_COUNT));
        // Assert
        assertEquals(MIN_COUNT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalMeasurementTwoIsSet((0)));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnits_true() {
        // Arrange
        // Act
        assertTrue(SUT.numberOfItemsIsSet(MAX_NUMBER_OF_ITEMS));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.numberOfItemsIsSet(0));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.numberOfItemsIsSet(MAX_NUMBER_OF_ITEMS + 1));
        // Assert
    }

    @Test
    public void totalMeasurementTwoIsSet_numberOfItemsAreSet_numberOfItemsAdjustsItemSize() {
        // Arrange
        int totalMeasurementTwo = 20;
        int numberOfItems = 10;
        int expectedItemMeasurementTwo = totalMeasurementTwo / numberOfItems;
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(totalMeasurementTwo));
        assertTrue(SUT.numberOfItemsIsSet(numberOfItems));
        // Assert
        assertEquals(expectedItemMeasurementTwo, SUT.getItemMeasurementTwo());
    }

    @Test
    public void totalMeasurementTwoIsSet_numberOfItemsIsSet_itemSizeIsFraction() {
        // Arrange
        int totalMeasurementTwoHigherThanNumberOfItems = 3;
        int numberOfItemsLowerThanTotalMeasurementTwo = 2;
        double expectedItemSize = totalMeasurementTwoHigherThanNumberOfItems /
                numberOfItemsLowerThanTotalMeasurementTwo;
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(totalMeasurementTwoHigherThanNumberOfItems));
        assertTrue(SUT.numberOfItemsIsSet(numberOfItemsLowerThanTotalMeasurementTwo));
        // Assert
        assertEquals(expectedItemSize, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwo_numberOfItemsChanged_numberOfItemsAdjustsItemSize() {
        int arbitraryNoOfItems = 2;
        int expectedItemUnits = MAX_COUNT / 2;

        assertTrue(SUT.totalMeasurementTwoIsSet(MAX_COUNT));
        assertTrue(SUT.numberOfItemsIsSet(arbitraryNoOfItems));
        assertEquals(expectedItemUnits, SUT.getItemMeasurementTwo());
    }

    @Test
    public void itemMeasurementOneIsSet_numberOfItemsChanged_numberOfItemsAdjustsTotalSize() {
        int arbitraryNoOfItems = 2;

        assertTrue(SUT.itemMeasurementTwoIsSet((MAX_COUNT / 2)));
        System.out.println(SUT);

        assertTrue(SUT.numberOfItemsIsSet(arbitraryNoOfItems));
        System.out.println(SUT);
        assertEquals(MAX_COUNT, SUT.getTotalMeasurementTwo(), DELTA);
        assertEquals(MAX_COUNT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void itemMeasurementTwoIsSet_numberOfItemsIsSetOutOfRangeMax_false() {
        // Arrange
        assertTrue(SUT.itemMeasurementTwoIsSet(MAX_COUNT));
        // Act
        assertFalse(SUT.numberOfItemsIsSet(2));
        // Assert
    }

    @Test
    public void itemMeasurementTwoIsSet_changeNumberOfItemsTwice_totalMeasurementTwoAutoAdjusts() {
        // Arrange
        int numberOfPacks = 6;
        int numberOfItemsInPack = 6;
        int newNumberOfItemsInPack = 12;
        int totalNumberOfItems = numberOfItemsInPack * numberOfPacks;
        int newTotalNumberOfItems = newNumberOfItemsInPack * numberOfPacks;

        // Act
        assertTrue(SUT.numberOfItemsIsSet(numberOfPacks));
        assertTrue(SUT.itemMeasurementTwoIsSet((numberOfItemsInPack)));
        assertEquals(totalNumberOfItems, SUT.getTotalMeasurementTwo());

        assertTrue(SUT.itemMeasurementTwoIsSet(newNumberOfItemsInPack));
        assertEquals(newTotalNumberOfItems, SUT.getTotalMeasurementTwo(), DELTA);
        // Assert
        System.out.println(SUT);
    }

    @Test
    public void totalMeasurementTwo_numberOfItemsSet_numberOfItemsAutoAdjustsItemMeasurement() {
        // Arrange
        int initialNumberOfPacks = 10;
        double itemUnits = MAX_COUNT / initialNumberOfPacks;
        int newNumberOfPacks = 20;
        double newItemUnits = MAX_COUNT / newNumberOfPacks;
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(MAX_COUNT));
        assertTrue(SUT.numberOfItemsIsSet(initialNumberOfPacks));
        // Assert
        assertEquals(itemUnits, SUT.getItemMeasurementTwo(), DELTA);
        // Act
        assertTrue(SUT.numberOfItemsIsSet(newNumberOfPacks));
        // Assert
        assertEquals(newItemUnits, SUT.getItemMeasurementTwo(), DELTA);
    }

    @Test
    public void totalBaseUnitsAewSet_numberOfItemsAreSet_mixedNumberReturnRoundedCorrectly() {
        // Arrange
        double nonIntegerDivisibleBaseUnits = 5;
        int nonIntegerDivisibleNumberOfItems = 3;
        int expectedItemMeasurementTwo = (int) nonIntegerDivisibleBaseUnits / nonIntegerDivisibleNumberOfItems;
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(nonIntegerDivisibleBaseUnits));
        assertTrue(SUT.numberOfItemsIsSet(nonIntegerDivisibleNumberOfItems));
        // Assert
        assertEquals((nonIntegerDivisibleBaseUnits), SUT.getTotalMeasurementTwo(), DELTA);
        assertEquals(expectedItemMeasurementTwo, SUT.getItemMeasurementTwo(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_measurementSetTwiceInSuccession_secondValueReturned() {
        // Arrange
        int arbitraryNumberOfItems = 2;
        int arbitraryFirstMeasurement = 2;
        int arbitrarySecondMeasurement = 20;
        assertTrue(SUT.numberOfItemsIsSet(arbitraryNumberOfItems));
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(arbitraryFirstMeasurement));
        // Assert
        assertEquals(arbitraryFirstMeasurement, SUT.getTotalBaseUnits(), DELTA);
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(arbitrarySecondMeasurement));
        // Assert
        assertEquals(arbitrarySecondMeasurement, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_numberOfItemsIsSet_numberOfItemsDoesNotAdjustTotalMeasurement() {
        // Arrange
        int arbitraryNumberOfItems = 2;
        int arbitraryFirstValue = 2;
        int arbitrarySecondValue = 20;
        // Act
        assertTrue(SUT.numberOfItemsIsSet(arbitraryNumberOfItems));
        assertTrue(SUT.totalMeasurementTwoIsSet(arbitraryFirstValue));
        assertTrue(SUT.totalMeasurementTwoIsSet(arbitrarySecondValue));
        // Assert
        assertEquals(arbitrarySecondValue, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(arbitrarySecondValue, SUT.getTotalMeasurementTwo());
    }

    @Test
    public void totalMeasurementTwoIsSet_measurementTwoDeletedDigitByDigit_false() {
        // Setup
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertTrue(SUT.totalMeasurementTwoIsSet(MAX_COUNT));
        assertEquals(MAX_COUNT, SUT.getTotalBaseUnits(), DELTA);

        // Incremental change as the user deletes total measurement value
        assertTrue(SUT.totalMeasurementTwoIsSet((MAX_COUNT / 10)));
        assertEquals((double)(MAX_COUNT / 10), SUT.getTotalBaseUnits(), DELTA);

        assertTrue(SUT.totalMeasurementTwoIsSet((MAX_COUNT / 100)));
        assertEquals((double)(MAX_COUNT / 100), SUT.getTotalBaseUnits(), DELTA);

        assertFalse(SUT.totalMeasurementTwoIsSet((0)));
        assertEquals((0), SUT.getTotalBaseUnits(), DELTA);

        assertEquals((0), SUT.getTotalMeasurementTwo());
        assertEquals((0), SUT.getItemMeasurementTwo());
    }
}