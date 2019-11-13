package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import androidx.core.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

public class ImperialMassTest {
    // region constants ----------------------------------------------------------------------------
    private double DELTA = 0.0001;
    double ONE_POUND = IMPERIAL_MASS_MIN_MEASUREMENT * 453.59237;
    double ONE_OUNCE = ONE_POUND / 16;
    double ONE_TENTH_OUNCE = ONE_OUNCE / 10;
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
        assertEquals(IMPERIAL_MASS_NUMBER_OF_MEASUREMENT_UNITS, SUT.getNumberOfMeasurementUnits());
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMin_true() {
        // Arrange
        unitValuesFromBaseUnits = getValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_SMALLEST_UNIT));
        // Assert
        assertEquals(unitValuesFromBaseUnits.first, SUT.getTotalMeasurementOne(), DELTA);
        assertEquals(unitValuesFromBaseUnits.first, SUT.getItemMeasurementOne(), DELTA);
        assertEquals(IMPERIAL_MASS_SMALLEST_UNIT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_UNIT_ONE_DECIMAL - DELTA));
        // Assert
        assertEquals((0), SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMax_true() {
        // Arrange
        unitValuesFromBaseUnits = getValuesFromBaseUnits(MAX_MASS);
        double unitOne = unitValuesFromBaseUnits.first;
        int unitTwo = unitValuesFromBaseUnits.second;
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_MAX_MEASUREMENT));
        // Assert
        assertEquals(unitTwo, SUT.getTotalMeasurementTwo());
        assertEquals(unitOne, SUT.getTotalMeasurementOne(), DELTA);
        assertEquals(IMPERIAL_MASS_MAX_MEASUREMENT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMax_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalBaseUnitsAreSet(MAX_MASS + 1));
        // Assert
        assertEquals((0), SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_baseUnitsSmallerThanSmallestItemSize_numberOfItemsReduced() {
        // Arrange
        double slightlySmallerThanTwoSmallestItems = (IMPERIAL_MASS_SMALLEST_UNIT * 2) - (DELTA * 2);
        // Act
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertTrue(SUT.totalBaseUnitsAreSet(slightlySmallerThanTwoSmallestItems));
        // Assert
        assertEquals((1), SUT.getNumberOfItems());
    }

    @Test
    public void totalBaseUnitsAreSetNumberOfItemsIsSet_minimumValues_itemMeasurementOneMinimum() {
        // Arrange
        // Act
        assertTrue(SUT.numberOfItemsIsSet(2));
        assertTrue(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_SMALLEST_UNIT * 2));
        // Assert
        assertEquals((0.2), SUT.getTotalMeasurementOne(), DELTA);
        assertEquals((0.1), SUT.getItemMeasurementOne(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_minimumItemSize_noOfItemsNotChanged() {
        // Arrange
        assertTrue(SUT.numberOfItemsIsSet(2));
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(IMPERIAL_MASS_SMALLEST_UNIT * 2));
        // Assert
        assertEquals((0.2), SUT.getTotalMeasurementOne(), DELTA);
        assertEquals((0), SUT.getTotalMeasurementTwo(), DELTA);
        assertEquals((0.1), SUT.getItemMeasurementOne(), DELTA);
        assertEquals((0), SUT.getItemMeasurementTwo());
        assertEquals((IMPERIAL_MASS_SMALLEST_UNIT * 2), SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_packAndItemHoldCorrectValues() {
        // Arrange
        double totalBasUnits = 5500; // any arbitrary value under MAX_MASS

        unitValuesFromBaseUnits = getValuesFromBaseUnits(totalBasUnits);
        int unitTwo = unitValuesFromBaseUnits.second;
        double unitOne = unitValuesFromBaseUnits.first;
        // Act
        assertTrue(SUT.totalBaseUnitsAreSet(totalBasUnits));
        // Assert
        assertEquals(unitOne, SUT.getTotalMeasurementOne(), DELTA);
        assertEquals(unitTwo, SUT.getTotalMeasurementTwo());
        assertEquals(unitOne, SUT.getItemMeasurementOne(), DELTA);
        assertEquals(unitTwo, SUT.getItemMeasurementTwo());
        assertEquals(totalBasUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_inRangeMax_true() {
        // Arrange
        double maximumMeasurementOne = roundDecimal(MAX_MASS / ONE_OUNCE);
        double expectedTotalBaseUnits = maximumMeasurementOne * ONE_OUNCE;
        // Act
        assertTrue(SUT.totalMeasurementOneIsSet(maximumMeasurementOne));
        // Assert
        assertEquals(expectedTotalBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_outOfRangeMax_false() {
        // Arrange
        double outOfRangeMaxMeasurementOne = roundDecimal(MAX_MASS / ONE_OUNCE + 0.1);
        // Act
        assertFalse(SUT.totalMeasurementOneIsSet(outOfRangeMaxMeasurementOne));
        // Assert
        assertEquals(SUT.getTotalBaseUnits(), 0, DELTA);
    }

    @Test
    public void totalMeasurementOneIsSet_inRangeMin_true() {
        // Arrange
        unitValuesFromBaseUnits = getValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        double minMeasurementOne = unitValuesFromBaseUnits.first;
        // Act
        assertTrue(SUT.totalMeasurementOneIsSet(minMeasurementOne));
        // Assert
        assertEquals(IMPERIAL_MASS_SMALLEST_UNIT, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalUnitOneIsSet_outOfRangeMin_false() {
        // Arrange
        unitValuesFromBaseUnits = getValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        double unitOneOutOfRangeMin = unitValuesFromBaseUnits.first - DELTA;
        // Act
        assertFalse(SUT.totalMeasurementOneIsSet(unitOneOutOfRangeMin));
        // Assert
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_maxInRange_true() {
        // Arrange
        unitValuesFromBaseUnits = getValuesFromBaseUnits(MAX_MASS);
        int maxUnitTwoValue = unitValuesFromBaseUnits.second;
        double expectedBaseUnits = getBaseUnitsFromUnitsValues(new Pair<>(0., maxUnitTwoValue));
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(maxUnitTwoValue));
        // Assert
        assertEquals(expectedBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMax_false() {
        // Arrange
        unitValuesFromBaseUnits = getValuesFromBaseUnits(MAX_MASS);
        int outOfRangeMaxUnitTwo = unitValuesFromBaseUnits.second + 1;
        // Act
        assertFalse(SUT.totalMeasurementTwoIsSet(outOfRangeMaxUnitTwo));
        // Assert
        assertEquals(0., SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_inRangeMin_true() {
        // Arrange
        unitValuesFromBaseUnits = getValuesFromBaseUnits(ONE_POUND);
        // Act
        assertTrue(SUT.totalMeasurementTwoIsSet(unitValuesFromBaseUnits.second));
        // Assert
        assertEquals(ONE_POUND, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_zeroValue_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalMeasurementTwoIsSet(0));
        // Assert
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementTwoIsSet_outOfRangeMin_false() {
        // Arrange
        // Act
        assertFalse(SUT.totalMeasurementTwoIsSet(-1));
        // Assert
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalMeasurementOne_thenTotalMeasurementTwo_true() {
        // Arrange
        double arbitraryUnitOneValue = 5;
        int arbitraryUnitTwoValue = 15;
        double expectedBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(arbitraryUnitOneValue, arbitraryUnitTwoValue));
        // Act
        assertTrue(SUT.totalMeasurementOneIsSet(arbitraryUnitOneValue));
        assertTrue(SUT.totalMeasurementTwoIsSet(arbitraryUnitTwoValue));
        // Assert
        assertEquals(expectedBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_inRange_true() {
        // Arrange
        int arbitraryNumberOfItems = 5;
        // Act
        assertTrue(SUT.numberOfItemsIsSet(arbitraryNumberOfItems));
        // Assert
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_outOfRangeMin_false() {
        // Arrange
        int numberOfItemsOutOfRangeMin = MIN_NUMBER_OF_ITEMS -1;
        // Act
        assertFalse(SUT.numberOfItemsIsSet(numberOfItemsOutOfRangeMin));
        // Assert
        assertEquals(MIN_NUMBER_OF_ITEMS, SUT.getNumberOfItems());
    }

    @Test
    public void numberOfItemsIsSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.numberOfItemsIsSet(MAX_NUMBER_OF_ITEMS));
        // Assert
    }

    @Test
    public void numberOfItemsAreSet_outOfRangeMax_false() {
        // Set to max +1
        assertFalse(SUT.numberOfItemsIsSet(MAX_NUMBER_OF_ITEMS + 1));
    }

    @Test
    public void numberOfItemsIsSet_numberOfItemsAdjustsItemMeasurements() {
        // Arrange
        int arbitraryNumberOfItems = 2;
        unitValuesFromBaseUnits = getValuesFromBaseUnits(IMPERIAL_MASS_SMALLEST_UNIT);
        double minMeasurementOne = unitValuesFromBaseUnits.first;
        double minMeasurementOneMultipliedByItems = minMeasurementOne * arbitraryNumberOfItems;
        double expectedBaseUnits = getBaseUnitsFromUnitsValues(
                new Pair<>(minMeasurementOneMultipliedByItems, 0));
        // Act
        // Set value of total measurement - sets lastMeasurementUpdated to TOTAL_MEASUREMENT
        // When number of items is updated item measurement should change
        assertTrue(SUT.totalMeasurementOneIsSet(minMeasurementOneMultipliedByItems));
        // Set number of items
        assertTrue(SUT.numberOfItemsIsSet(arbitraryNumberOfItems));

        // Assert
        // Check pack measurement unchanged
        assertEquals(minMeasurementOneMultipliedByItems, SUT.getTotalMeasurementOne(), DELTA);
        assertEquals(0, SUT.getTotalMeasurementTwo());
        // Check item measurement changed
        assertEquals(minMeasurementOne, SUT.getItemMeasurementOne(), DELTA);
        assertEquals(0, SUT.getItemMeasurementTwo());
        assertEquals(expectedBaseUnits, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void numberOfItemsIsSet_afterTotalMeasurementOneIsSet_itemSizeRoundsCorrectly() {
        // Arrange
        double oddMeasurementOne = 3;
        int nonDivisibleToCardinalNumberOfItems = 2;
        double totalBaseUnits = getBaseUnitsFromUnitsValues(new Pair<>(oddMeasurementOne, 0));
        double expectedItemBaseUnits = totalBaseUnits / nonDivisibleToCardinalNumberOfItems;
        unitValuesFromBaseUnits = getValuesFromBaseUnits(expectedItemBaseUnits);
        double expectedMeasurementOne = unitValuesFromBaseUnits.first;
        // Act
        assertTrue(SUT.totalMeasurementOneIsSet(oddMeasurementOne));
        assertTrue(SUT.numberOfItemsIsSet(nonDivisibleToCardinalNumberOfItems));
        // Assert
        assertEquals(oddMeasurementOne, SUT.getTotalMeasurementOne(), DELTA);
        assertEquals(expectedMeasurementOne, SUT.getItemMeasurementOne(), DELTA);
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE & TWO - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(SUT.totalMeasurementOneIsSet(5), is(true));
        assertThat(SUT.totalMeasurementTwoIsSet(1), is(true));

        // Set number of items
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        assertThat(SUT.getTotalMeasurementOne(), is(5.));
        assertThat(SUT.getTotalMeasurementTwo(), is(1));
        // Check item measurements have changed
        assertThat(SUT.getItemMeasurementOne(), is(10.5));
        assertThat(SUT.getItemMeasurementTwo(), is(0));

        assertThat(SUT.getTotalBaseUnits(), is(595.339985625));
    }

    @Test
    public void numberOfItemsIsSet_afterTotalMeasurementsSet_numberOfItemsAdjustsItemSize() {
        // Arrange
        double arbitraryTotalMeasurementOne = 5;
        int arbitraryTotalMeasurementTwo = 1;

        int arbitraryNumberOfItems = 2;

        double totalBaseUnitsBeforeItemsSet = getBaseUnitsFromUnitsValues(new Pair<>(5., 1));
        double totalBaseUnitsAfterItemsSet = totalBaseUnitsBeforeItemsSet / arbitraryNumberOfItems;

        unitValuesFromBaseUnits = getValuesFromBaseUnits(totalBaseUnitsAfterItemsSet);
        double expectedItemUnitOneValue = unitValuesFromBaseUnits.first;
        int expectedItemUnitTwoValue = unitValuesFromBaseUnits.second;
        // Act
        assertTrue(SUT.totalMeasurementOneIsSet(arbitraryTotalMeasurementOne));
        assertTrue(SUT.totalMeasurementTwoIsSet(arbitraryTotalMeasurementTwo));
        assertTrue(SUT.numberOfItemsIsSet(arbitraryNumberOfItems));
        // Assert
        assertEquals(expectedItemUnitOneValue, SUT.getItemMeasurementOne(), DELTA);
        assertEquals(expectedItemUnitTwoValue, SUT.getItemMeasurementTwo());
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.itemMeasurementOneIsSet(5), is(true));

        // Set number of items
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        // Check pack measurements have changed
        assertThat(SUT.getTotalMeasurementOne(), is(10.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        // Check item measurement are unchanged
        assertThat(SUT.getItemMeasurementOne(), is(5.));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(283.49523125));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.itemMeasurementOneIsSet(5), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(1), is(true));

        // Set items
        assertThat(SUT.numberOfItemsIsSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(SUT.getTotalMeasurementOne(), is(10.));
        assertThat(SUT.getTotalMeasurementTwo(), is(2));
        // Check ITEM measurements are unchanged
        assertThat(SUT.getItemMeasurementOne(), is(5.));
        assertThat(SUT.getItemMeasurementTwo(), is(1));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.itemMeasurementOneIsSet(1), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(11), is(true));

        // Set items so to high
        assertThat(SUT.numberOfItemsIsSet(2), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalMeasurementOne(), is(1.));
        assertThat(SUT.getTotalMeasurementTwo(), is(11));
        assertThat(SUT.getItemMeasurementOne(), is(1.));
        assertThat(SUT.getItemMeasurementTwo(), is(11));
        assertThat(SUT.getTotalBaseUnits(), is(5017.8655931250005));
    }

    @Test
    public void testNumberOfItemsInRangeSetWithItemWithBaseSiThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(SUT.itemMeasurementOneIsSet(15), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(2), is(true));

        // Change number of items
        assertThat(SUT.numberOfItemsIsSet(3), is(true));

        // Check pack measurement has changed
        assertThat(SUT.getTotalMeasurementOne(), is(13.));
        assertThat(SUT.getTotalMeasurementTwo(), is(8));
        assertThat(SUT.getItemMeasurementOne(), is(15.));
        assertThat(SUT.getItemMeasurementTwo(), is(2));

        // Set item measurement again
        assertThat(SUT.numberOfItemsIsSet(5), is(true));

        // Check pack measurement changed
        assertThat(SUT.getTotalMeasurementOne(), is(11.));
        assertThat(SUT.getTotalMeasurementTwo(), is(14));
        assertThat(SUT.getItemMeasurementOne(), is(15.));
        assertThat(SUT.getItemMeasurementTwo(), is(2));
        assertThat(SUT.getTotalBaseUnits(), is(6662.137934375));
    }

    @Test
    public void test_setting_item_one_and_two() {

        assertThat(SUT.numberOfItemsIsSet(2), is(true));
        assertThat(SUT.itemMeasurementTwoIsSet(1), is(true));
        assertThat(SUT.getTotalBaseUnits(), is(907.18474));
        assertThat(SUT.itemMeasurementOneIsSet(2), is(true));
        assertThat(SUT.getItemMeasurementOne(), is(2.));
        assertThat(SUT.getTotalBaseUnits(), is(1020.5828325));
    }

    @Test
    public void settingBaseSi() {

        assertThat(SUT.numberOfItemsIsSet(2), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(2), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(20.), is(true));
        assertThat(SUT.getTotalMeasurementTwo(), is(1));
        assertThat(SUT.getTotalMeasurementOne(), is(4.));
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {

        // Setup
        assertThat(SUT.numberOfItemsIsSet(2), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(5.2), is(true));
        assertThat(SUT.totalMeasurementTwoIsSet(1), is(true));

        // Gradual teardown, as the user would type
        assertThat(SUT.totalMeasurementTwoIsSet(0), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(5.), is(true));
        assertThat(SUT.totalMeasurementOneIsSet(5), is(true));

        assertThat(SUT.totalMeasurementOneIsSet(0), is(false));
        assertThat(SUT.getTotalBaseUnits(), is(0.));

        assertThat(SUT.getTotalMeasurementOne(), is(0.));
        assertThat(SUT.getItemMeasurementOne(), is(0.));
        assertThat(SUT.getTotalMeasurementTwo(), is(0));
        assertThat(SUT.getItemMeasurementTwo(), is(0));
    }

    @Test
    public void pack_measurement_two_forces_out_of_bounds_is_then_zeroed_out() {

        // Setup in range values
        assertThat(SUT.totalMeasurementOneIsSet(10), is(true));
        assertThat(SUT.getTotalMeasurementOne(), is(10.));


        assertThat(SUT.totalMeasurementTwoIsSet(19), is(true));
        assertThat(SUT.getTotalMeasurementTwo(), is(19));

        // Attempt to set pack two out of range, which should return false
        // and zero out pack measurement two.
        assertThat(SUT.totalMeasurementTwoIsSet(22), is(false));

        // Check pack one is unaffected
        assertThat(SUT.getTotalMeasurementOne(), is(10.));

        // Check pack two is unaffected
        assertThat(SUT.getTotalMeasurementTwo(), is(19));
    }


    @Test
    public void pack_measurement_one_forces_out_of_bounds_is_then_zeroed_out() {

        // Setup in range values
        assertThat(SUT.totalMeasurementTwoIsSet(22), is(true));
        assertThat(SUT.getTotalMeasurementTwo(), is(22));

        // Attempt to set pack one out of range, which should return false
        // and zero out pack measurement one.
        assertThat(SUT.totalMeasurementOneIsSet(1), is(false));
        assertThat(SUT.getTotalMeasurementOne(), is(0.));

    }

    // A crude but effective way of calculating pounds and ounces from grams
    private Pair<Double, Integer> getValuesFromBaseUnits(double baseUnits) {
        int pounds = (int) (baseUnits / ONE_POUND);
        double poundsInBaseUnits = pounds * ONE_POUND;
        double ouncesInBaseUnits = baseUnits - poundsInBaseUnits;
        double ounces = ouncesInBaseUnits / ONE_OUNCE;

        double unitOne = roundDecimal(ounces);

        return new Pair<>(unitOne, pounds);
    }

    private double getBaseUnitsFromUnitsValues(Pair<Double, Integer> unitValues) {
        double unitOne = unitValues.first;
        int unitTwo = unitValues.second;

        double unitTwoInBaseUnits = unitTwo * ONE_POUND;

        int unitOneInteger = (int) unitOne;
        double unitOneIntegerInBaseUnits = unitOneInteger * ONE_OUNCE;

        double unitOneDecimal = unitOne - unitOneInteger;
        double unitOneDecimalInBaseUnits = unitOneDecimal * ONE_OUNCE;

        return unitTwoInBaseUnits + unitOneIntegerInBaseUnits + unitOneDecimalInBaseUnits;
    }

    private double roundDecimal(double valueToRound) {
        NumberFormat decimalFormat = NumberFormat.getInstance();
        decimalFormat.setRoundingMode(RoundingMode.HALF_EVEN);

        if (decimalFormat instanceof DecimalFormat)
            ((DecimalFormat) decimalFormat).applyPattern("##.#");

        return Double.parseDouble(decimalFormat.format(valueToRound));
    }
}