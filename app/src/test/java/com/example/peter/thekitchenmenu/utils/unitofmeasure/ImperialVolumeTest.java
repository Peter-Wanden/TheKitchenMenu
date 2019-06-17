package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import org.junit.Test;

import java.math.RoundingMode;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MINIMUM_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MAXIMUM_VOLUME;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ImperialVolumeTest {

    private ImperialVolume imperialVolume = new ImperialVolume();

    private static final double UNIT_PINT = MINIMUM_VOLUME * 568.26125;
    private static final double UNIT_FLUID_OUNCE = UNIT_PINT / 20;
    private static final double UNIT_FLUID_OUNCE_DECIMAL = UNIT_FLUID_OUNCE / 10;

    private NumberFormat decimalInputNumberFormat = NumberFormat.getInstance();

    public ImperialVolumeTest() {

        decimalInputNumberFormat.setRoundingMode(RoundingMode.DOWN);
        decimalInputNumberFormat.setMaximumFractionDigits(1);
        decimalInputNumberFormat.setGroupingUsed(false);
    }

    @Test
    public void testGetNumberOfMeasurementUnits() {

        assertThat(imperialVolume.getNumberOfMeasurementUnits(), is(2));
    }

    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseUnitsInRangeMin() { // IN RANGE MIN

        assertThat(imperialVolume.baseUnitsAreSet(UNIT_FLUID_OUNCE_DECIMAL), is(true));

        assertThat(imperialVolume.getPackMeasurementOne(), is(.1));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementOne(), is(.1));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
        assertThat(imperialVolume.getBaseUnits(), is(UNIT_FLUID_OUNCE_DECIMAL));

        System.out.println();
    }

    @Test
    public void testBaseUnitsOutOfRangeMin() { // OUT OF RANGE MIN

        assertThat(imperialVolume.baseUnitsAreSet(UNIT_FLUID_OUNCE_DECIMAL - .00000001),
                is(false));

        assertThat(imperialVolume.getPackMeasurementOne(), is(0.));
        assertThat(imperialVolume.getProductMeasurementOne(), is(0.));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
        assertThat(imperialVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsInRangeMax() { // IN RANGE MAX

        assertThat(imperialVolume.baseUnitsAreSet((MAXIMUM_VOLUME)), is(true));

        assertThat(imperialVolume.getPackMeasurementOne(), is(12.));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(17));
        assertThat(imperialVolume.getProductMeasurementOne(), is(12.));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(17));
        assertThat(imperialVolume.getBaseUnits(), is(MAXIMUM_VOLUME));

        System.out.println();
    }

    @Test
    public void testBaseUnitsOutOfRangeMax() { // OUT OF RANGE MAX

        assertThat(imperialVolume.baseUnitsAreSet(MAXIMUM_VOLUME + .00000001), is(false));

        assertThat(imperialVolume.getPackMeasurementOne(), is(0.));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementOne(), is(0.));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
        assertThat(imperialVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsViolateMinimumItemSize() { // CONDITION: BASE SI SMALLER THAN SMALLEST ITEM

        assertThat(imperialVolume.numberOfProductsIsSet(2), is(true));

        assertThat(imperialVolume.baseUnitsAreSet(UNIT_FLUID_OUNCE_DECIMAL - .00000001),
                is(false));

        assertThat(imperialVolume.getPackMeasurementOne(), is(0.));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementOne(), is(0.));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
        assertThat(imperialVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        assertThat(imperialVolume.numberOfProductsIsSet(2), is(true));

        assertThat(imperialVolume.baseUnitsAreSet(UNIT_FLUID_OUNCE_DECIMAL * 2), is(true));

        assertThat(imperialVolume.getPackMeasurementOne(), is(.2));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementOne(), is(.1));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
        assertThat(imperialVolume.getBaseUnits(), is(UNIT_FLUID_OUNCE_DECIMAL * 2));

        System.out.println();
    }

    @Test
    public void testBaseUnitsRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(imperialVolume.baseUnitsAreSet((UNIT_PINT * 12) + (UNIT_FLUID_OUNCE * 2)),
                is(true));

        // Check pack and item values have updated correctly
        assertThat(imperialVolume.getPackMeasurementOne(), is(2.));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(12));
        assertThat(imperialVolume.getProductMeasurementOne(), is(2.));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(12));
        assertThat(imperialVolume.getBaseUnits(),
                is((UNIT_PINT * 12) + (UNIT_FLUID_OUNCE * 2)));

        System.out.println();
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        double maxFluidOunces = MAXIMUM_VOLUME / UNIT_FLUID_OUNCE;

        // Set to max
        assertThat(imperialVolume.packMeasurementOneIsSet(
                Double.parseDouble(decimalInputNumberFormat.format(
                        maxFluidOunces))), is(true));


        // Check value set
        assertThat(imperialVolume.getPackMeasurementOne(), is(11.9));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(17));
        assertThat(imperialVolume.getProductMeasurementOne(), is(11.9));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(17));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        double maxFluidOunces = MAXIMUM_VOLUME / UNIT_FLUID_OUNCE;

        // Set to max + 1
        assertThat(imperialVolume.packMeasurementOneIsSet(
                Double.parseDouble(decimalInputNumberFormat.format(
                        maxFluidOunces + .1))), is(false));

        // Check values no changes
        assertThat(imperialVolume.getPackMeasurementOne(), is(0.));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementOne(), is(0.));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
        assertThat(imperialVolume.getBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(imperialVolume.packMeasurementOneIsSet(.1), is(true));

        // Check set
        assertThat(imperialVolume.getPackMeasurementOne(), is(.1));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementOne(), is(.1));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMin() { // OUT OF RANGE MIN

        // Set to .01 below min
        assertThat(imperialVolume.packMeasurementOneIsSet(0.09), is(false));

        // Check values unchanged
        assertThat(imperialVolume.getPackMeasurementOne(), is(0.));
        assertThat(imperialVolume.getPackMeasurementTwo(), is(0));
        assertThat(imperialVolume.getProductMeasurementOne(), is(0.));
        assertThat(imperialVolume.getProductMeasurementTwo(), is(0));
        assertThat(imperialVolume.getBaseUnits(), is(0.));
    }
}