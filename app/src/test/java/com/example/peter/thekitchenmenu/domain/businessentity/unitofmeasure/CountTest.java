package com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure;

import org.junit.Before;
import org.junit.Test;

import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.*;
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
        assertTrue(SUT.isTotalBaseUnitsSet(COUNT_SMALLEST_UNIT));
        // Assert
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMin_false() {
        // Arrange
        double outOfRangeMin = COUNT_SMALLEST_UNIT - COUNT_SMALLEST_UNIT;
        // Act
        assertFalse(SUT.isTotalBaseUnitsSet(outOfRangeMin));
        // Assert
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(MAX_COUNT));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalUnitTwo(), DELTA);
        assertEquals(MAX_COUNT, SUT.getItemUnitTwo(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMax_false() {
        // Arrange
        double outOfRangeMax = MAX_COUNT + COUNT_SMALLEST_UNIT;
        // Act
        assertFalse(SUT.isTotalBaseUnitsSet(outOfRangeMax));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_totalBaseUnitsAreSet_sameValues() {
        // Arrange
        int measurement = 5;
        // Act
        assertTrue(SUT.isNumberOfItemsSet(measurement));
        assertTrue(SUT.isTotalBaseUnitsSet(measurement));
        // Assert
        assertEquals(measurement, SUT.getTotalUnitTwo());
        assertEquals(measurement, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_numberOfItemsAreSet_totalAndItemMeasurementsUpdateAsExpected() {
        // Arrange
        int measurement = 2;
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(MAX_COUNT));
        assertTrue(SUT.isNumberOfItemsSet(measurement));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalUnitTwo());
        assertEquals((MAX_COUNT / measurement), SUT.getItemUnitTwo());
    }

    @Test
    public void totalMeasurementOneIsSet_inRangeMin_true() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalUnitOneSet(COUNT_UNIT_ONE_DECIMAL));
        // Assert
        assertEquals(COUNT_UNIT_ONE_DECIMAL, SUT.getTotalUnitOne(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(MAX_COUNT));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalUnitTwo(), DELTA);
        assertEquals(MAX_COUNT, SUT.getItemUnitTwo(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalUnitTwoSet(MAX_COUNT + (int) COUNT_UNIT_TWO));
        // Assert
        assertEquals(MIN_COUNT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMin_false() {
        // Arrange
        int outOfRangeMin = (int) (COUNT_UNIT_TWO - COUNT_UNIT_TWO);
        // Act
        assertFalse(SUT.isTotalUnitTwoSet(outOfRangeMin));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_noBaseUnits_true() {
        // Arrange
        // Act
        assertTrue(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.isNumberOfItemsSet(MIN_NUMBER_OF_ITEMS - 1));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS + MIN_NUMBER_OF_ITEMS));
        // Assert
    }

    @Test
    public void totalMeasurementTwoIsSet_numberOfItemsAreSet_numberOfItemsAdjustsItemSize() {
        // Arrange
        int totalMeasurementTwo = 20;
        int numberOfItems = 10;
        int expectedItemMeasurementTwo = totalMeasurementTwo / numberOfItems;
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(totalMeasurementTwo));
        assertTrue(SUT.isNumberOfItemsSet(numberOfItems));
        // Assert
        assertEquals(expectedItemMeasurementTwo, SUT.getItemUnitTwo());
    }

    @Test
    public void totalMeasurementTwoIsSet_numberOfItemsIsSet_itemSizeIsFraction() {
        // Arrange
        int totalMeasurementTwoHigherThanNumberOfItems = 3;
        int numberOfItemsLowerThanTotalMeasurementTwo = 2;
        double expectedItemSize = totalMeasurementTwoHigherThanNumberOfItems /
                numberOfItemsLowerThanTotalMeasurementTwo;
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(totalMeasurementTwoHigherThanNumberOfItems));
        assertTrue(SUT.isNumberOfItemsSet(numberOfItemsLowerThanTotalMeasurementTwo));
        // Assert
        assertEquals(expectedItemSize, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwo_numberOfItemsChanged_numberOfItemsAdjustsItemSize() {
        int arbitraryNoOfItems = 2;
        int expectedItemUnits = MAX_COUNT / 2;

        assertTrue(SUT.isTotalUnitTwoSet(MAX_COUNT));
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNoOfItems));
        assertEquals(expectedItemUnits, SUT.getItemUnitTwo());
    }

    @Test
    public void itemMeasurementOneIsSet_numberOfItemsChanged_numberOfItemsAdjustsTotalSize() {
        int arbitraryNoOfItems = 2;

        assertTrue(SUT.isItemUnitTwoSet((MAX_COUNT / 2)));
        System.out.println(SUT);

        assertTrue(SUT.isNumberOfItemsSet(arbitraryNoOfItems));
        System.out.println(SUT);
        assertEquals(MAX_COUNT, SUT.getTotalUnitTwo(), DELTA);
        assertEquals(MAX_COUNT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void itemMeasurementTwoIsSet_numberOfItemsIsSetOutOfRangeMax_false() {
        // Arrange
        assertTrue(SUT.isItemUnitTwoSet(MAX_COUNT));
        // Act
        assertFalse(SUT.isNumberOfItemsSet(2));
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
        assertTrue(SUT.isNumberOfItemsSet(numberOfPacks));
        assertTrue(SUT.isItemUnitTwoSet((numberOfItemsInPack)));

        // Assert
        assertEquals(totalNumberOfItems, SUT.getTotalUnitTwo());
        assertTrue(SUT.isItemUnitTwoSet(newNumberOfItemsInPack));
        assertEquals(newTotalNumberOfItems, SUT.getTotalUnitTwo(), DELTA);
    }

    @Test
    public void totalMeasurementTwo_numberOfItemsSet_numberOfItemsAutoAdjustsItemMeasurement() {
        // Arrange
        int initialNumberOfPacks = 10;
        double itemUnits = MAX_COUNT / initialNumberOfPacks;
        int newNumberOfPacks = 20;
        double newItemUnits = MAX_COUNT / newNumberOfPacks;
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(MAX_COUNT));
        assertTrue(SUT.isNumberOfItemsSet(initialNumberOfPacks));
        // Assert
        assertEquals(itemUnits, SUT.getItemUnitTwo(), DELTA);
        // Act
        assertTrue(SUT.isNumberOfItemsSet(newNumberOfPacks));
        // Assert
        assertEquals(newItemUnits, SUT.getItemUnitTwo(), DELTA);
    }

    @Test
    public void totalBaseUnitsAewSet_numberOfItemsAreSet_mixedNumberReturnRoundedCorrectly() {
        // Arrange
        double nonIntegerDivisibleBaseUnits = 5;
        int nonIntegerDivisibleNumberOfItems = 3;
        int expectedItemMeasurementTwo = (int) nonIntegerDivisibleBaseUnits / nonIntegerDivisibleNumberOfItems;
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(nonIntegerDivisibleBaseUnits));
        assertTrue(SUT.isNumberOfItemsSet(nonIntegerDivisibleNumberOfItems));
        // Assert
        assertEquals((nonIntegerDivisibleBaseUnits), SUT.getTotalUnitTwo(), DELTA);
        assertEquals(expectedItemMeasurementTwo, SUT.getItemUnitTwo(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_measurementSetTwiceInSuccession_secondValueReturned() {
        // Arrange
        int arbitraryNumberOfItems = 2;
        int arbitraryFirstMeasurement = 2;
        int arbitrarySecondMeasurement = 20;
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNumberOfItems));
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(arbitraryFirstMeasurement));
        // Assert
        assertEquals(arbitraryFirstMeasurement, SUT.getTotalBaseUnits(), DELTA);
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(arbitrarySecondMeasurement));
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
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNumberOfItems));
        assertTrue(SUT.isTotalUnitTwoSet(arbitraryFirstValue));
        assertTrue(SUT.isTotalUnitTwoSet(arbitrarySecondValue));
        // Assert
        assertEquals(arbitrarySecondValue, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(arbitrarySecondValue, SUT.getTotalUnitTwo());
    }

    @Test
    public void totalMeasurementTwoIsSet_measurementTwoDeletedDigitByDigit_false() {
        // Setup
        assertTrue(SUT.isNumberOfItemsSet(2));
        assertTrue(SUT.isTotalUnitTwoSet(MAX_COUNT));
        assertEquals(MAX_COUNT, SUT.getTotalBaseUnits(), DELTA);

        // Incremental change as the user deletes total measurement value
        assertTrue(SUT.isTotalUnitTwoSet((MAX_COUNT / 10)));
        assertEquals((double)(MAX_COUNT / 10), SUT.getTotalBaseUnits(), DELTA);

        assertTrue(SUT.isTotalUnitTwoSet((MAX_COUNT / 100)));
        assertEquals((double)(MAX_COUNT / 100), SUT.getTotalBaseUnits(), DELTA);

        assertFalse(SUT.isTotalUnitTwoSet((0)));
        assertEquals((0), SUT.getTotalBaseUnits(), DELTA);

        assertEquals((0), SUT.getTotalUnitTwo());
        assertEquals((0), SUT.getItemUnitTwo());
    }

    @Test
    public void totalMeasurementIsSet_totalDividedByPortions_itemAsFraction() {
        // Arrange
        int numberOfPortions = 16;
        int noOfOnions = 1;
        double expectedFractionOfAnOnionForOnePortion = noOfOnions / numberOfPortions;
        // Act
        SUT.isNumberOfItemsSet(numberOfPortions);
        SUT.isTotalUnitTwoSet(noOfOnions);
        // Assert
        assertEquals(expectedFractionOfAnOnionForOnePortion, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void totalMeasurementIsSet_threePortionsAtOneAndHalf_itemIsHalf() {
        // Arrange
        int numberOfPortions = 3;
        int wholeUnits = 1;
        double fractionUnit = 0.5;
        double expectedItemUnits = (wholeUnits + fractionUnit) / numberOfPortions;
        // Act
        SUT.isTotalUnitTwoSet(wholeUnits);
        SUT.isTotalUnitOneSet(fractionUnit);
        // Assert
        assertEquals(expectedItemUnits, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void totalMeasurementIsSet_totalIsFraction_minItemSizeExceeded() {
        // Arrange
        int whole = 1;
        double part = 0.5;
        int numberOrPortions = 4;
        // Act
        assertTrue(SUT.isNumberOfItemsSet(numberOrPortions));
        assertTrue(SUT.isTotalUnitTwoSet(whole));
        assertTrue(SUT.isTotalUnitOneSet(part));
        // Assert
        assertEquals(whole, SUT.getTotalUnitTwo());
        assertEquals(part, SUT.getTotalUnitOne(), DELTA);
        assertEquals((whole+part), SUT.getTotalBaseUnits(), DELTA);
        assertEquals(((whole+part)/numberOrPortions), SUT.getItemBaseUnits(), DELTA);
    }
}