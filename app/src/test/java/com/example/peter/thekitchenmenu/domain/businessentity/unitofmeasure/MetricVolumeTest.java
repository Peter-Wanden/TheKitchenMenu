package com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.MAX_NUMBER_OF_ITEMS;
import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.MAX_VOLUME;
import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.METRIC_VOLUME_SMALLEST_UNIT;
import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.METRIC_VOLUME_UNIT_TWO;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MetricVolumeTest {

    // region constants ----------------------------------------------------------------------------
    private final double DELTA = .0001;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    // endregion helper fields ---------------------------------------------------------------------

    private MetricVolume SUT;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SUT = new MetricVolume();
    }


    @Test
    public void totalBaseUnitsAreSet_inRangeMin_true() {
        assertTrue(SUT.isTotalBaseUnitsSet(1));
        assertEquals(1, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMin_false() {
        // Arrange
        double outOfRangeMin = METRIC_VOLUME_SMALLEST_UNIT - METRIC_VOLUME_SMALLEST_UNIT / 10;
        // Act
        assertFalse(SUT.isTotalBaseUnitsSet(outOfRangeMin));
        // Assert
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(MAX_VOLUME));
        // Assert
        assertEquals(MAX_VOLUME, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_outOfRangeMax_false() {
        assertFalse(SUT.isTotalBaseUnitsSet(MAX_VOLUME + 1));
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_baseUnitsAtMinItemSize_numberOfItemsNotAdjusted() {
        // Arrange
        assertTrue(SUT.isNumberOfItemsSet(5));
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(5));
        // Assert
        assertEquals(5, SUT.getTotalUnitOne(), DELTA);
        assertEquals(0, SUT.getTotalUnitTwo());
        assertEquals(1, SUT.getItemUnitOne(), DELTA);
        assertEquals(0, SUT.getItemUnitTwo());
        assertEquals(5, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void totalBaseUnitsAreSet_totalAndItemUnitsSetToCorrectValues() {
        // Arrange
        double totalBaseUnits = 5500;
        int expectedUnitTwo = (int) (totalBaseUnits / METRIC_VOLUME_UNIT_TWO);
        double expectedUnitOne = totalBaseUnits % 1000;
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(totalBaseUnits));
        // Assert
        assertEquals(expectedUnitTwo, SUT.getTotalUnitTwo());
        assertEquals(expectedUnitTwo, SUT.getItemUnitTwo());
        assertEquals(expectedUnitOne, SUT.getTotalUnitOne(), DELTA);
        assertEquals(expectedUnitOne, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void isUnitOneSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalUnitOneSet(MAX_VOLUME));
        // Assert
        assertEquals(MAX_VOLUME, SUT.getTotalBaseUnits(), DELTA);
    }

    @Test
    public void unitOneOutOfRangeMax() { // OUT OF RANGE MAX

        // Set to max plus 1
        assertThat(SUT.isTotalUnitOneSet(10001), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void unitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(SUT.isTotalUnitOneSet(1.), is(true));

        // Check set
        assertThat(SUT.getTotalUnitOne(), is(1.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(1.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(1.));
    }

    @Test
    public void isTotalUnitOneSet_outOfRangeMin_false() {
        // Arrange
        double totalUnitOneOutOfRangeMin = METRIC_VOLUME_SMALLEST_UNIT -
                METRIC_VOLUME_SMALLEST_UNIT / 10;
        // Act
        assertFalse(SUT.isTotalUnitOneSet(totalUnitOneOutOfRangeMin));
        // Assert
    }

    @Test
    public void isTotalUnitTwoSet_inRangeMax_true() {
        // Arrange
        // Act
        assertTrue(SUT.isTotalUnitTwoSet((int) (MAX_VOLUME / 1000)));
        // Assert
    }

    @Test
    public void testMaxOutOfRangeUnitTwo() { // OUT OF RANGE MAX

        // Set to max +1
        assertThat(SUT.isTotalUnitTwoSet(11), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMinInRangeUnitTwo() { // IN RANGE MIN

        // Set to min
        assertThat(SUT.isTotalUnitTwoSet(1), is(true));

        // Check value set
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(1));
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(1));
        assertThat(SUT.getTotalBaseUnits(), is(1000.));
    }

    @Test
    public void testMinimumOutOfRangeMeasurementTwo() { // OUT OF RANGE MIN

        // Set to min -1
        assertThat(SUT.isTotalUnitTwoSet(-1), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testInRangeUnitOneAndTwo() { // CONDITION: IN RANGE

        // Set arbitrary in range value
        assertThat(SUT.isTotalUnitOneSet(500), is(true));
        assertThat(SUT.isTotalUnitTwoSet(5), is(true));

        // Check values set
        assertThat(SUT.getTotalUnitOne(), is(500.));
        assertThat(SUT.getTotalUnitTwo(), is(5));
        assertThat(SUT.getItemUnitOne(), is(500.));
        assertThat(SUT.getItemUnitTwo(), is(5));
        assertThat(SUT.getTotalBaseUnits(), is(5500.));
    }

    @Test
    public void testSetNumberOfItemsMinInRangeWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - IN RANGE MIN

        // Set arbitrary number of items, base si at zero
        assertThat(SUT.isNumberOfItemsSet(5), is(true));

        // Check set
        assertThat(SUT.getNumberOfItems(), is(5));
        // Check values unchanged
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testSetNumberOfItemsMinOutOfRangeWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - OUT OF RANGE MIN

        // Set out of range min
        assertThat(SUT.isNumberOfItemsSet(0), is(false));

        // Check values unchanged (1 is default)
        assertThat(SUT.getNumberOfItems(), is(1));

        // Check values unchanged
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void testNumberOfItemsInRangeMaxWithNoBaseUnits() { // CONDITION: BASE SI NOT YET SET - IN RANGE MAX

        // Set to max within range
        assertThat(SUT.isNumberOfItemsSet(999), is(true));

        // Check set
        assertThat(SUT.getNumberOfItems(), is(999));

        // Check values unchanged
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(0.));
    }

    @Test
    public void numberOfItemIsSet_outOfRangeMax_false() {
        assertFalse(SUT.isNumberOfItemsSet(MAX_NUMBER_OF_ITEMS + 1));
        assertEquals(1, SUT.getNumberOfItems());
    }

    @Test
    public void testNumberOfItemsInRangeMinWithBaseUnits() { // CONDITION: BASE SI SET BY PACK ONE - NO OF ITEMS IN RANGE MIN

        // Set value to pack measurement - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(SUT.isTotalUnitOneSet(2), is(true));

        // Set number of items
        assertThat(SUT.isNumberOfItemsSet(2), is(true));

        // Check item measurement changed
        assertThat(SUT.getTotalUnitOne(), is(2.));
        assertThat(SUT.getTotalUnitTwo(), is(0));
        assertThat(SUT.getItemUnitOne(), is(1.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(2.));
    }

    @Test
    public void testNumberOfItemsAdjustsItemSize() { // CONDITION: BASE SI SET BY PACK ONE & TWO - NO OF ITEMS ADJUSTS ITEM SIZE

        // Set value to both pack measurements - sets lastMeasurementUpdated to PACK
        // When number of items is updated item measurement should change
        assertThat(SUT.isTotalUnitOneSet(500), is(true));
        assertThat(SUT.isTotalUnitTwoSet(1), is(true));

        // Set number of items
        assertThat(SUT.isNumberOfItemsSet(2), is(true));

        // Check item measurements have changed
        assertThat(SUT.getTotalUnitOne(), is(500.));
        assertThat(SUT.getTotalUnitTwo(), is(1));
        assertThat(SUT.getItemUnitOne(), is(750.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(1500.));
    }

    @Test
    public void testNumberOfItemsOneAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE - NO OF ITEMS ADJUSTS PACK SIZE

        // Set value to item measurement - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.isItemUnitOneSet(500), is(true));

        // Set number of items
        assertThat(SUT.isNumberOfItemsSet(2), is(true));

        // Check pack measurement have changed
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(1));
        assertThat(SUT.getItemUnitOne(), is(500.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(1000.));
    }

    @Test
    public void testNumberOfItemsOneAndTwoAdjustsPackSize() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - IN RANGE

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.isItemUnitOneSet(500), is(true));
        assertThat(SUT.isItemUnitTwoSet(1), is(true));

        // Set items
        assertThat(SUT.isNumberOfItemsSet(2), is(true));

        // Check PACK measurements have changed
        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(3));
        assertThat(SUT.getItemUnitOne(), is(500.));
        assertThat(SUT.getItemUnitTwo(), is(1));
        assertThat(SUT.getTotalBaseUnits(), is(3000.));
    }

    @Test
    public void testNumberOfItemsSetItemOneAndTwoAdjustsPackSizeOutOfRange() { // CONDITION: BASE SI SET BY ITEM ONE & TWO - NO OF ITEMS ADJUSTS PACK SIZE - OUT OF RANGE MAX

        // Set value to both item measurements - sets lastMeasurementUpdated to ITEM
        // When number of items is updated PACK measurement should change
        assertThat(SUT.isItemUnitOneSet(1), is(true));
        assertThat(SUT.isItemUnitTwoSet(5), is(true));

        // Set items so to high
        assertThat(SUT.isNumberOfItemsSet(2), is(false));

        // Check values unchanged
        assertThat(SUT.getTotalUnitOne(), is(1.));
        assertThat(SUT.getTotalUnitTwo(), is(5));
        assertThat(SUT.getItemUnitOne(), is(1.));
        assertThat(SUT.getItemUnitTwo(), is(5));
        assertThat(SUT.getTotalBaseUnits(), is(5001.));
    }

    @Test
    public void testNumberOfItemsInRange_SetWithItemBaseUnitsThenChangedAgain() {
        // CONDITION: BASE SI SET BY ITEM - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set item measurement last changed by setting item measurement
        assertThat(SUT.isItemUnitOneSet(500), is(true));
        assertThat(SUT.isItemUnitTwoSet(1), is(true));

        // Change number of items
        assertThat(SUT.isNumberOfItemsSet(3), is(true));

        // Check pack measurement has changed
        assertThat(SUT.getTotalUnitOne(), is(500.));
        assertThat(SUT.getTotalUnitTwo(), is(4));
        assertThat(SUT.getItemUnitOne(), is(500.));
        assertThat(SUT.getItemUnitTwo(), is(1));
        assertThat(SUT.getTotalBaseUnits(), is(4500.));

        // Set item measurement again
        assertThat(SUT.isNumberOfItemsSet(5), is(true));

        // Check pack measurement changed
        assertThat(SUT.getTotalUnitOne(), is(500.));
        assertThat(SUT.getTotalUnitTwo(), is(7));
        assertThat(SUT.getItemUnitOne(), is(500.));
        assertThat(SUT.getItemUnitTwo(), is(1));
        assertThat(SUT.getTotalBaseUnits(), is(7500.));
    }

    @Test
    public void testNumberOfItemsInRangeSetWith_ItemWithBaseUnits() {
        // CONDITION: BASE SI SET BY PACK - NO OF ITEMS CHANGED - THEN NO OF ITEMS CHANGED AGAIN

        // Set pack measurement last changed by setting pack measurement
        assertThat(SUT.isTotalUnitTwoSet(10), is(true));
        assertThat(SUT.getTotalBaseUnits(), is(MAX_VOLUME));

        // Change number of items
        assertThat(SUT.isNumberOfItemsSet(10), is(true));

        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(10));

        // Check item measurements have changed
        assertThat(SUT.getItemUnitOne(), is(0.));
        assertThat(SUT.getItemUnitTwo(), is(1));
        assertThat(SUT.getTotalBaseUnits(), is(MAX_VOLUME));

        // Change number of items
        assertThat(SUT.isNumberOfItemsSet(20), is(true));

        assertThat(SUT.getTotalUnitOne(), is(0.));
        assertThat(SUT.getTotalUnitTwo(), is(10));
        // Check item measurements have changed
        assertThat(SUT.getItemUnitOne(), is(500.));
        assertThat(SUT.getItemUnitTwo(), is(0));
        assertThat(SUT.getTotalBaseUnits(), is(MAX_VOLUME));
    }

    @Test
    public void totalBaseUnitsAreSet_settingNoOfItemsProducesItemUnitOneFraction() {
        // Arrange
        double baseUnits = 1;
        int numberOfItemsNonDivisibleByBaseUnits = 2;
        double unitOneFractional = baseUnits / numberOfItemsNonDivisibleByBaseUnits;
        // Act
        assertTrue(SUT.isTotalBaseUnitsSet(baseUnits));
        assertTrue(SUT.isNumberOfItemsSet(numberOfItemsNonDivisibleByBaseUnits));
        // Assert
        assertEquals(unitOneFractional, SUT.getItemUnitOne(), DELTA);
    }

    @Test
    public void isTotalUnitOnSet_noOfItemsSet_baseUnitsDividedByUnitsAndItems() {
        // Arrange
        assertTrue(SUT.isNumberOfItemsSet(9));
        assertTrue(SUT.isItemBaseUnitsSet(250));
        // Act
        // Assert
        assertTrue(SUT.isTotalUnitOneSet(8000));
        System.out.println(SUT);
    }

    @Test
    public void test_setting_pack_one() {

        assertThat(SUT.getTotalBaseUnits(), is(0.));
        assertThat(SUT.isNumberOfItemsSet(2), is(true));
        assertThat(SUT.isTotalUnitOneSet(2.), is(true));
        assertThat(SUT.getTotalBaseUnits(), is(2.));
        assertThat(SUT.isTotalUnitOneSet(20.), is(true));
        assertThat(SUT.getTotalBaseUnits(), is(20.));
    }

    @Test
    public void settingBaseUnits() {

        assertThat(SUT.isNumberOfItemsSet(2), is(true));
        assertThat(SUT.isTotalUnitOneSet(2), is(true));
        assertThat(SUT.isTotalUnitOneSet(20.), is(true));
        assertThat(SUT.getTotalBaseUnits(), is(20.));
        assertThat(SUT.getTotalUnitOne(), is(20.));
    }

    @Test
    public void test_for_zero_base_units_with_false_return() {
        // Setup
        assertTrue(SUT.isNumberOfItemsSet(2));
        assertTrue(SUT.isTotalUnitOneSet(500));
        assertTrue(SUT.isTotalUnitTwoSet(1));
        assertEquals(1500, SUT.getTotalBaseUnits(), DELTA);

        // Gradual teardown, as the user would type
        assertTrue(SUT.isTotalUnitTwoSet(0));
        assertEquals(500, SUT.getTotalBaseUnits(), DELTA);

        assertTrue(SUT.isTotalUnitOneSet(50));
        assertEquals(50, SUT.getTotalBaseUnits(), DELTA);

        assertTrue(SUT.isTotalUnitOneSet(5));
        assertEquals(5, SUT.getTotalBaseUnits(), DELTA);

        assertFalse(SUT.isTotalBaseUnitsSet(0));
        assertEquals(0, SUT.getTotalBaseUnits(), DELTA);
    }
}