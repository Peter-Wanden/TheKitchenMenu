package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.Count;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;

import org.junit.Before;
import org.junit.Test;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;
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
        assertTrue(SUT.isTotalBaseUnitsSet(1));
        // Assert
        assertEquals(1, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalBaseUnitsSet(0.9));
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
        // Act
        assertFalse(SUT.isTotalBaseUnitsSet((MAX_COUNT + 1)));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_totalBaseUnitsSetToLowerValueThanNumberOfItems_numberOfItemsAutoAdjustedToBaseUnitValue() {
        // Arrange
        int numberOfItemsHigherThanBaseUnits = 10;
        double baseUnitsLowerThanNumberOfItems = 5;
        int expectedNoOfItemsAdjustment = (int) (numberOfItemsHigherThanBaseUnits - baseUnitsLowerThanNumberOfItems);
        // Act
        assertTrue(SUT.isNumberOfItemsSet(numberOfItemsHigherThanBaseUnits));
        assertTrue(SUT.isTotalBaseUnitsSet(baseUnitsLowerThanNumberOfItems));
        // Assert
        assertEquals(expectedNoOfItemsAdjustment, SUT.getNumberOfItems());
    }

    @Test
    public void numberOfItemsIsSet_totalBaseUnitsAreSet_sameValuesOk() {
        // Arrange
        // Act
        assertTrue(SUT.isNumberOfItemsSet(5));
        assertTrue(SUT.isTotalBaseUnitsSet(5));
        // Assert
        assertEquals(5, SUT.getTotalUnitTwo());
        assertEquals(5, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_numberOfItemsAreSet_totalAndItemMeasurementsUpdateAsExpected() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(MAX_COUNT));
        assertTrue(SUT.isNumberOfItemsSet((2)));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalUnitTwo());
        assertEquals((MAX_COUNT / 2), SUT.getItemUnitTwo());
    }

    @Test
    public void totalMeasurementOneIsSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(MAX_COUNT));
        // Assert
        assertEquals(MAX_COUNT, SUT.getTotalUnitTwo(), DELTA);
        assertEquals(MAX_COUNT, SUT.getItemUnitTwo(), DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalUnitTwoSet((MAX_COUNT + 1)));
        // Assert
        assertEquals(MIN_COUNT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_inRange_min() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(MIN_COUNT));
        // Assert
        assertEquals(MIN_COUNT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalUnitTwoSet((0)));
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
        assertFalse(SUT.isNumberOfItemsSet(0));
        // Assert
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS + 1));
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
        assertEquals(totalNumberOfItems, SUT.getTotalUnitTwo());

        assertTrue(SUT.isItemUnitTwoSet(newNumberOfItemsInPack));
        assertEquals(newTotalNumberOfItems, SUT.getTotalUnitTwo(), DELTA);
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
}