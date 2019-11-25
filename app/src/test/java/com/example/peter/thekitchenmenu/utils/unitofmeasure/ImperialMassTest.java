package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.ImperialMass;

import org.junit.Before;
import org.junit.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasureConstants.*;
import static org.junit.Assert.*;

public class ImperialMassTest {
    // region constants ----------------------------------------------------------------------------
    private double DELTA = 0.0001;
    private double ONE_POUND = IMPERIAL_MASS_MIN_MEASUREMENT * 453.59237;
    private double ONE_OUNCE = ONE_POUND / 16;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private ImperialMass SUT;
    private Pair<Double, Integer> unitValuesFromBaseUnits;

    @Before
    public void setUp() {
        SUT = new ImperialMass();
    }

    @Test
    public void getNumberOfMeasurementUnits_NumberOfMeasurementUnitsReturned() {
        assertEquals(IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS, SUT.getNumberOfUnits());
    }

    @Test
    public void getMinUnitOne_minUnitOneReturned() {
        // Arrange
        // Act
        // Assert
        assertEquals(IMPERIAL_MASS_SMALLEST_UNIT, SUT.getMinUnitOne(), DELTA);
    }

    @Test
    public void getMaxUnitOne_maxUnitOneReturned() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_MAX_MEASUREMENT);
        double expectedMaxUnitOne = roundDecimal(unitValuesFromBaseUnits.first);
        // Act
        // Assert
        assertEquals(expectedMaxUnitOne, SUT.getMaxUnitOne(), DELTA);
    }

    @Test
    public void getMaxUnitTwo_maxUnitTwoReturned() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_MAX_MEASUREMENT);
        int expectedMaxUnitTwo = unitValuesFromBaseUnits.second;
        // Act
        // Assert
        assertEquals(expectedMaxUnitTwo, SUT.getMaxUnitTwo());
    }

    // todo, test digit widths
    // todo, test numberOfItems autoAdjust

    @Test
    public void isTotalBaseUnitsSet_inRangeMin_true() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(IMPERIAL_MASS_SMALLEST_UNIT));
        // Assert
        assertEquals(unitValuesFromBaseUnits.first, SUT.getTotalUnitOne(), DELTA);
        assertEquals(unitValuesFromBaseUnits.first, SUT.getItemUnitOne(), DELTA);
        assertEquals(IMPERIAL_MASS_SMALLEST_UNIT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalBaseUnitsSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalBaseUnitsSet(IMPERIAL_MASS_UNIT_ONE_DECIMAL - DELTA));
        // Assert
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalBaseUnitsSet_inRangeMax_true() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(MAX_MASS);
        double unitOne = unitValuesFromBaseUnits.first;
        int unitTwo = unitValuesFromBaseUnits.second;
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(IMPERIAL_MASS_MAX_MEASUREMENT));
        // Assert
        assertEquals(unitTwo, SUT.getTotalUnitTwo());
        assertEquals(unitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(IMPERIAL_MASS_MAX_MEASUREMENT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalBaseUnitsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalBaseUnitsSet(IMPERIAL_MASS_MAX_MEASUREMENT + 1));
        // Assert
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalBaseUnitsSet_baseUnitsSmallerThanSmallestItemSize_numberOfItemsReduced() {
        // Arrange
        double slightlySmallerThanTwoSmallestItems = (IMPERIAL_MASS_SMALLEST_UNIT * 2) - (DELTA * 2);
        // Act
        assertTrue(SUT.isNumberOfItemsSet(2));
        assertTrue(SUT.isTotalBaseUnitsSet(slightlySmallerThanTwoSmallestItems));
        // Assert
        assertEquals((1), SUT.getNumberOfItems());
    }

    @Test
    public void isTotalBaseUnitsSet_isNumberOfItemsSet_minimumValues_itemMeasurementOneMinimum() {
        // Arrange
        int arbitraryNumberOfItems = 4;
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        double totalBaseUnits = arbitraryNumberOfItems * IMPERIAL_MASS_SMALLEST_UNIT;
        double expectedTotalUnitOne = unitValuesFromBaseUnits.first * arbitraryNumberOfItems;
        double expectedItemUnitOne = expectedTotalUnitOne / arbitraryNumberOfItems;
        // Act
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNumberOfItems));
        assertTrue(SUT.isTotalBaseUnitsSet(totalBaseUnits));
        // Assert
        assertEquals(expectedTotalUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedItemUnitOne, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void isTotalBaseUnitsSet_minimumItemSize_noOfItemsNotChanged() {
        // Arrange
        int arbitraryNoOfItems = 2;
        double totalBaseUnits = IMPERIAL_MASS_SMALLEST_UNIT * arbitraryNoOfItems;
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(totalBaseUnits);
        double expectedTotalUnitOne = unitValuesFromBaseUnits.first;
        double expectedItemUnits = unitValuesFromBaseUnits.first / arbitraryNoOfItems;
        // Act
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNoOfItems));
        assertTrue(SUT.isTotalBaseUnitsSet(totalBaseUnits));
        // Assert
        assertEquals(expectedTotalUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedItemUnits, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void isTotalBaseUnitsSet_packAndItemHoldCorrectValues() {
        // Arrange
        double arbitraryTotalBasUnits = 5500;

        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(arbitraryTotalBasUnits);
        int unitTwo = unitValuesFromBaseUnits.second;
        double unitOne = unitValuesFromBaseUnits.first;
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(arbitraryTotalBasUnits));
        // Assert
        assertEquals(unitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(unitTwo, SUT.getTotalUnitTwo());
        assertEquals(unitOne, SUT.getItemUnitOne(), DELTA);
        assertEquals(unitTwo, SUT.getItemUnitTwo());
        assertEquals(arbitraryTotalBasUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitOneSet_inRangeMax_true() {
        // Arrange
        double maximumMeasurementOne = roundDecimal(MAX_MASS / ONE_OUNCE);
        double expectedTotalBaseUnits = maximumMeasurementOne * ONE_OUNCE;
        // Act
        assertTrue(SUT.isTotalUnitOneSet(maximumMeasurementOne));
        // Assert
        assertEquals(expectedTotalBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitOneSet_outOfRangeMax_false() {
        // Arrange
        double outOfRangeMaxMeasurementOne = roundDecimal(MAX_MASS / ONE_OUNCE + 0.1);
        // Act
        assertFalse(SUT.isTotalUnitOneSet(outOfRangeMaxMeasurementOne));
        // Assert
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitOneSet_inRangeMin_true() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        double minMeasurementOne = unitValuesFromBaseUnits.first;
        // Act
        assertTrue(SUT.isTotalUnitOneSet(minMeasurementOne));
        // Assert
        assertEquals(IMPERIAL_MASS_SMALLEST_UNIT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitOneSet_outOfRangeMin_false() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        double unitOneOutOfRangeMin = unitValuesFromBaseUnits.first - DELTA;
        // Act
        assertFalse(SUT.isTotalUnitOneSet(unitOneOutOfRangeMin));
        // Assert
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitTwoSet_maxInRange_true() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(MAX_MASS);
        int maxUnitTwoValue = unitValuesFromBaseUnits.second;
        double expectedBaseUnits = getBaseUnitsFromUnitsValues(new Pair<>(0., maxUnitTwoValue));
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(maxUnitTwoValue));
        // Assert
        assertEquals(expectedBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitTwoSet_outOfRangeMax_false() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(MAX_MASS);
        int outOfRangeMaxUnitTwo = unitValuesFromBaseUnits.second + 1;
        // Act
        assertFalse(SUT.isTotalUnitTwoSet(outOfRangeMaxUnitTwo));
        // Assert
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitTwoSet_inRangeMin_true() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(ONE_POUND);
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(unitValuesFromBaseUnits.second));
        // Assert
        assertEquals(ONE_POUND, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitTwoSet_zeroValue_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalUnitTwoSet(NOT_SET));
        // Assert
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitTwoSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.isTotalUnitTwoSet(-1));
        // Assert
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitOneSet_thenIsTotalUnitTwoSet_true() {
        // Arrange
        double arbitraryUnitOneValue = 5;
        int arbitraryUnitTwoValue = 15;
        double expectedBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(arbitraryUnitOneValue, arbitraryUnitTwoValue));
        // Act
        assertTrue(SUT.isTotalUnitOneSet(arbitraryUnitOneValue));
        assertTrue(SUT.isTotalUnitTwoSet(arbitraryUnitTwoValue));
        // Assert
        assertEquals(expectedBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitTwoSet_outOfBounds_existingValuesUnaffected() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_MAX_MEASUREMENT);
        double unitOneInRange = unitValuesFromBaseUnits.first;
        int unitTwoInRange = unitValuesFromBaseUnits.second -1;
        int unitTwoOutOfRange = unitValuesFromBaseUnits.second + 1;
        // Act
        assertTrue(SUT.isTotalUnitOneSet(unitOneInRange));
        assertTrue(SUT.isTotalUnitTwoSet(unitTwoInRange));
        // Attempt to set total unit two out of range
        assertFalse(SUT.isTotalUnitTwoSet(unitTwoOutOfRange));
        // Assert
        // Check totals are unaffected
        assertEquals(unitOneInRange, SUT.getTotalUnitOne(), DELTA);
        assertEquals(unitTwoInRange, SUT.getTotalUnitTwo());
    }

    @Test
    public void isTotalUnitOneSet_outOfBounds_existingValuesUnaffected() {
        // Arrange
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_MAX_MEASUREMENT);
        double unitOneInRange = unitValuesFromBaseUnits.first;
        int unitTwoInRange = unitValuesFromBaseUnits.second;
        double unitOneOutOfRange = unitValuesFromBaseUnits.first + 0.1;
        // Act
        assertTrue(SUT.isTotalUnitTwoSet(unitTwoInRange));
        assertTrue(SUT.isTotalUnitOneSet(unitOneInRange));
        // Attempt to set total unit one out of range
        assertFalse(SUT.isTotalUnitOneSet(unitOneOutOfRange));
        // Assert
        assertEquals(unitOneInRange, SUT.getTotalUnitOne(), DELTA);
        assertEquals(unitTwoInRange, SUT.getTotalUnitTwo());
    }

    // Changing 'number of items' tests.
    // Rules are as follows:
    // -> If last measurement updated is TOTAL_UNITS, total base units remain constant
    //      -> Increasing number of items proportionately reduces item size
    //      -> Decreasing number of items proportionately increases item size
    // -> If last measurement updated is ITEM_UNITS, item base units remain constant
    //      -> Increasing number of items proportionately increases total base units
    //      -> Decreasing number of items proportionately decreases total base units
    // -> If neither item nor total measurements have been set, changing number of items has
    //    no effect on the base units of either measurement

    // -> The minimum number of items is 1
    @Test
    public void isNumberOfItemsSet_inRangeMin_true() {
        // Arrange
        // Act
        assertTrue(SUT.isNumberOfItemsSet(MIN_NUMBER_OF_ITEMS));
        // Assert
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void isNumberOfItemsSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.isNumberOfItemsSet(MIN_NUMBER_OF_ITEMS -1));
        // Assert
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void isNumberOfItemsSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS));
        // Assert
        assertEquals(MAX_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void isNumberOfItemsSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS + 1));
        // Assert
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void isNumberOfItemsSet_whenLastUpdatedIsTotal_increasingNumberOfItemsDecreasesItemUnit() {
        // Arrange
        int arbitraryNumberOfItems = 2;
        // Setup test with minimum unit values
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        double minUnitOneValue = unitValuesFromBaseUnits.first;

        double minUnitOneMultipliedByNoOfItems = minUnitOneValue * arbitraryNumberOfItems;

        double expectedTotalBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(minUnitOneMultipliedByNoOfItems, NOT_SET)
        );
        double expectedItemBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(minUnitOneMultipliedByNoOfItems / arbitraryNumberOfItems, NOT_SET)
        );
        // Act
        // Set value of total measurement - sets lastMeasurementUpdated to TOTAL_MEASUREMENT
        assertTrue(SUT.isTotalUnitOneSet(minUnitOneMultipliedByNoOfItems));
        // Check item units equal to total units
        assertEquals(minUnitOneMultipliedByNoOfItems, SUT.getItemUnitOne(), DELTA);

        // Increase number of items
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNumberOfItems));

        // Assert
        // Check total units unchanged
        assertEquals(minUnitOneMultipliedByNoOfItems, SUT.getTotalUnitOne(), DELTA);
        // Check total base units unchanged
        assertEquals(expectedTotalBaseUnits, SUT.getTotalBaseUnits(), DELTA);

        // Check item units decreased proportionately
        assertEquals(minUnitOneValue, SUT.getItemUnitOne(), DELTA);
        // Check item base units decreased proportionately
        assertEquals(expectedItemBaseUnits, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void isNumberOfItemsSet_whenLastUpdatedIsTotal_increasingNumberOfItemsDecreasesItemUnits() {
        // Arrange - same as above only using both units and measuring base unit output
        int arbitraryNumberOfItems = 2;
        double arbitraryTotalUnitOne = 5;
        int arbitraryTotalUnitTwo = 1;

        double totalBaseUnitsBeforeItemsSet = getBaseUnitsFromUnitsValues(
                new Pair<>(arbitraryTotalUnitOne, arbitraryTotalUnitTwo));
        double totalBaseUnitsAfterItemsSet = totalBaseUnitsBeforeItemsSet / arbitraryNumberOfItems;

        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(totalBaseUnitsAfterItemsSet);
        double expectedItemUnitOneValue = unitValuesFromBaseUnits.first;
        int expectedItemUnitTwoValue = unitValuesFromBaseUnits.second;
        // Act
        assertTrue(SUT.isTotalUnitOneSet(arbitraryTotalUnitOne));
        assertTrue(SUT.isTotalUnitTwoSet(arbitraryTotalUnitTwo));
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNumberOfItems));
        // Assert
        assertEquals(expectedItemUnitOneValue, SUT.getItemUnitOne(), DELTA);
        assertEquals(expectedItemUnitTwoValue, SUT.getItemUnitTwo());
    }

    @Test
    public void isNumberOfItemsSet_whenLastUpdatedIsTotal_itemSizeRoundsCorrectly() {
        // Arrange
        double oddUnitOne = 3;
        int nonCardinalDivisibleNumberOfItems = 2;
        double totalBaseUnits = getBaseUnitsFromUnitsValues(new Pair<>(oddUnitOne, NOT_SET));
        double expectedItemBaseUnits = totalBaseUnits / nonCardinalDivisibleNumberOfItems;
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(expectedItemBaseUnits);
        double expectedItemUnitOneValue = unitValuesFromBaseUnits.first;
        // Act
        assertTrue(SUT.isTotalUnitOneSet(oddUnitOne));
        assertTrue(SUT.isNumberOfItemsSet(nonCardinalDivisibleNumberOfItems));
        // Assert
        assertEquals(oddUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedItemUnitOneValue, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void isNumberOfItemsSet_whenLastUpdateIsTotal_decreasingNoOfItemsIncreasesItemSize() {
        // Arrange
        int initialNoOfItems = 10;
        double expectedTotalUnitOneConstant = 10;
        double expectedInitialItemUnitOne = initialNoOfItems / expectedTotalUnitOneConstant;
        int noOfItemsDecrease = initialNoOfItems / 2;
        double expectedUnitOneIncreaseAfterNoItemsDecrease =
                expectedTotalUnitOneConstant / noOfItemsDecrease;
        // Act
        assertTrue(SUT.isNumberOfItemsSet(initialNoOfItems));
        assertTrue(SUT.isTotalUnitOneSet(expectedTotalUnitOneConstant));
        // Assert
        assertEquals(expectedInitialItemUnitOne, SUT.getItemUnitOne(), DELTA);
        // Act
        assertTrue(SUT.isNumberOfItemsSet(noOfItemsDecrease));
        // Assert
        assertEquals(expectedTotalUnitOneConstant, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedUnitOneIncreaseAfterNoItemsDecrease, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void isNumberOfItemsSet_whenLastUpdateIsItem_increasingNoOfItemsIncreasesTotals() {
        // Arrange
        int increasedNoOfItems = 2;
        double expectedItemUnitOneConstant = 5;
        int expectedItemUnitTwoConstant = 1;
        double expectedIncreasedTotalUnitOne = increasedNoOfItems * expectedItemUnitOneConstant;
        int expectedIncreasedTotalUnitTwo = increasedNoOfItems * expectedItemUnitTwoConstant;
        double expectedTotalBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(expectedIncreasedTotalUnitOne, expectedIncreasedTotalUnitTwo)
        );

        // Set last updated to measurement to item measurement by setting an item measurement
        assertTrue(SUT.isItemUnitOneSet(expectedItemUnitOneConstant));
        assertTrue(SUT.isItemUnitTwoSet(expectedItemUnitTwoConstant));
        // Act
        assertTrue(SUT.isNumberOfItemsSet(increasedNoOfItems));
        // Assert
        // Check item values have remained constant
        assertEquals(expectedItemUnitOneConstant, SUT.getItemUnitOne(), DELTA);
        assertEquals(expectedItemUnitTwoConstant, SUT.getItemUnitTwo());
        // Check total values have increased
        assertEquals(expectedIncreasedTotalUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedIncreasedTotalUnitTwo, SUT.getTotalUnitTwo());
        // Check base total units increased
        assertEquals(expectedTotalBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isNumberOfItemsSet_whenLastUpdateIsItem_increasingNoOfItemsIncreasesTotalsMoreThanOnce() {
        // Arrange
        int firstNoOfItems = 1;
        double constantItemUnitOne = 15;
        int constantItemUnitTwo = 2;
        double expectedTotalItemBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(constantItemUnitOne, constantItemUnitTwo));
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(expectedTotalItemBaseUnits * firstNoOfItems);
        double expectedTotalUnitOneAfterFirstNoOfItems = unitValuesFromBaseUnits.first;
        int expectedTotalUnitTwoAfterFirstNoOfItems = unitValuesFromBaseUnits.second;

        int secondNumberOfItems = 3;
        double secondItemBaseUnits = expectedTotalItemBaseUnits * secondNumberOfItems;
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(secondItemBaseUnits);
        double expectedTotalUnitOneAfterSecondNoOfItems = unitValuesFromBaseUnits.first;
        int expectedTotalUnitTwoAfterSecondNoOfItems = unitValuesFromBaseUnits.second;

        int thirdNoOfItems = 5;
        double thirdItemBaseUnits = expectedTotalItemBaseUnits * thirdNoOfItems;
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(thirdItemBaseUnits);
        double expectedTotalUnitOneAfterThirdNoOfItems = unitValuesFromBaseUnits.first;
        int expectedTotalUnitTwoAfterThirdNoOfItems = unitValuesFromBaseUnits.second;

        // Act
        assertTrue(SUT.isItemUnitOneSet(constantItemUnitOne));
        assertTrue(SUT.isItemUnitTwoSet(constantItemUnitTwo));
        assertTrue(SUT.isNumberOfItemsSet(firstNoOfItems));
        // Assert
        assertEquals(expectedTotalUnitOneAfterFirstNoOfItems, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedTotalUnitTwoAfterFirstNoOfItems, SUT.getTotalUnitTwo());
        // Act
        assertTrue(SUT.isNumberOfItemsSet(secondNumberOfItems));
        // Assert
        assertEquals(expectedTotalUnitOneAfterSecondNoOfItems, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedTotalUnitTwoAfterSecondNoOfItems, SUT.getTotalUnitTwo());
        // Act
        assertTrue(SUT.isNumberOfItemsSet(thirdNoOfItems));
        // Assert
        assertEquals(expectedTotalUnitOneAfterThirdNoOfItems, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedTotalUnitTwoAfterThirdNoOfItems, SUT.getTotalUnitTwo());
    }

    @Test
    public void isNumberOfItemsSet_whenLastUpdateIsItem_totalExceedsMax_false() {
        // Arrange
        int arbitraryNoOfItems = 2;
        unitValuesFromBaseUnits = getUnitValuesFromBaseUnits(IMPERIAL_MASS_MAX_MEASUREMENT);
        double unitOne = unitValuesFromBaseUnits.first;
        int unitTwo = unitValuesFromBaseUnits.second;
        double expectedBaseUnits = getBaseUnitsFromUnitsValues(new Pair<>(unitOne, unitTwo));
        // Act
        assertTrue(SUT.isItemUnitOneSet(unitOne));
        assertTrue(SUT.isItemUnitTwoSet(unitTwo));
        // Assert
        assertFalse(SUT.isNumberOfItemsSet(arbitraryNoOfItems));
        assertEquals(expectedBaseUnits, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(expectedBaseUnits, SUT.getItemBaseUnits(), DELTA);
    }

    @Test
    public void isItemUnitOneSet_IsItemUnitTwoSet_isNoOfItemsSet_valuesAsExpected() {
        // Arrange
        int arbitraryNoOfItems = 2;
        int itemUnitTwo = 1;
        double itemUnitTwoInBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>((double) NOT_SET, itemUnitTwo)) * arbitraryNoOfItems;

        double itemUnitOne = 2;
        double totalBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(itemUnitOne, itemUnitTwo)) * arbitraryNoOfItems;
        // Act
        assertTrue(SUT.isNumberOfItemsSet(arbitraryNoOfItems));
        assertTrue(SUT.isItemUnitTwoSet(itemUnitTwo));
        // Assert
        assertEquals(itemUnitTwoInBaseUnits, SUT.getTotalBaseUnits(), DELTA);
        // Act
        assertTrue(SUT.isItemUnitOneSet(itemUnitOne));
        // Assert
        assertEquals(itemUnitOne, SUT.getItemUnitOne(), DELTA);
        assertEquals(totalBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void isTotalUnitOneSet_unitOneGreaterThanASingleUnitTwo_unitOneAndTwoUpdateAutomatically() {
        // Arrange
        // Just to be clear - if a unit one value is entered that exceeds the value of a single
        // unit two, then the unit of measure converts the value entered into a unit one and two
        // value. For example, there are 16fl oz in a pint. If the user enters 20 fl oz in unit one
        // the unit of measure will update the measurement to 1pt(unitTwo) 4fl oz(unitOne).
        double flOzFourOzGreaterThanAPint = 20;
        int onePint = 1;
        double fourOz = 4;
        // Act
        assertTrue(SUT.isTotalUnitOneSet(flOzFourOzGreaterThanAPint));
        // Assert
        assertEquals(onePint, SUT.getTotalUnitTwo());
        assertEquals(fourOz, SUT.getTotalUnitOne(), DELTA);
    }

    @Test
    public void gradualDeletionOfValues_mimicsAUserDeletingValuesFromTheUi_eventualZeroValue() {
        // Arrange
        assertTrue(SUT.isTotalUnitOneSet(5.2));
        assertTrue(SUT.isTotalUnitTwoSet(1));

        // Gradual deletion of values, as the user would type
        assertTrue(SUT.isTotalUnitTwoSet(NOT_SET));
        assertTrue(SUT.isTotalUnitOneSet(5));
        assertTrue(SUT.isTotalUnitOneSet(5));

        // Assert
        assertFalse(SUT.isTotalUnitOneSet(NOT_SET));
        assertEquals(NOT_SET, SUT.getTotalBaseUnits(), DELTA);
        assertEquals(NOT_SET, SUT.getTotalUnitOne(), DELTA);
        assertEquals(NOT_SET, SUT.getItemUnitOne(), DELTA);
        assertEquals(NOT_SET, SUT.getTotalUnitTwo());
        assertEquals(NOT_SET, SUT.getItemUnitTwo());
    }

    // A crude but effective way of calculating pounds and ounces from grams
    private Pair<Double, Integer> getUnitValuesFromBaseUnits(double baseUnits) {
        int unitTwo = (int) (baseUnits / ONE_POUND);
        double unitOne = (baseUnits - (unitTwo * ONE_POUND)) / ONE_OUNCE;
        return new Pair<>(roundDecimal(unitOne), unitTwo);
    }

    // A crude but effective way of calculating grams from pounds and ounces
    private double getBaseUnitsFromUnitsValues(Pair<Double, Integer> unitValues) {
        double unitOne = unitValues.first;
        int unitTwo = unitValues.second;
        return (unitTwo * ONE_POUND) + (unitOne * ONE_OUNCE);
    }

    private double roundDecimal(double valueToRound) {
        NumberFormat decimalFormat = NumberFormat.getInstance();
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        if (decimalFormat instanceof DecimalFormat)
            ((DecimalFormat) decimalFormat).applyPattern("##.#");

        return Double.parseDouble(decimalFormat.format(valueToRound));
    }
}