package com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure;

import org.junit.Test;

import java.math.RoundingMode;
import java.text.NumberFormat;

import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.MIN_VOLUME;
import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureConstants.MAX_VOLUME;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ImperialVolumeTest {

    private ImperialVolume imperialVolume = new ImperialVolume();

    private static final double UNIT_PINT = MIN_VOLUME * 568.26125;
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

        assertThat(imperialVolume.getNumberOfUnits(), is(2));
    }

    //////////////////////////// SETTING AND GETTING BASE SI TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void testBaseUnitsInRangeMin() { // IN RANGE MIN

        assertThat(imperialVolume.isTotalBaseUnitsSet(UNIT_FLUID_OUNCE_DECIMAL), is(true));

        assertThat(imperialVolume.getTotalUnitOne(), is(.1));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitOne(), is(.1));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
        assertThat(imperialVolume.getTotalBaseUnits(), is(UNIT_FLUID_OUNCE_DECIMAL));

        System.out.println();
    }

    @Test
    public void testBaseUnitsOutOfRangeMin() { // OUT OF RANGE MIN

        assertThat(imperialVolume.isTotalBaseUnitsSet(UNIT_FLUID_OUNCE_DECIMAL - .00000001),
                is(false));

        assertThat(imperialVolume.getTotalUnitOne(), is(0.));
        assertThat(imperialVolume.getItemUnitOne(), is(0.));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
        assertThat(imperialVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsInRangeMax() { // IN RANGE MAX

        assertThat(imperialVolume.isTotalBaseUnitsSet((MAX_VOLUME)), is(true));

        assertThat(imperialVolume.getTotalUnitOne(), is(12.));
        assertThat(imperialVolume.getTotalUnitTwo(), is(17));
        assertThat(imperialVolume.getItemUnitOne(), is(12.));
        assertThat(imperialVolume.getItemUnitTwo(), is(17));
        assertThat(imperialVolume.getTotalBaseUnits(), is(MAX_VOLUME));

        System.out.println();
    }

    @Test
    public void testBaseUnitsOutOfRangeMax() { // OUT OF RANGE MAX

        assertThat(imperialVolume.isTotalBaseUnitsSet(MAX_VOLUME + .00000001), is(false));

        assertThat(imperialVolume.getTotalUnitOne(), is(0.));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitOne(), is(0.));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
        assertThat(imperialVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsViolateMinimumItemSize() { // CONDITION: BASE SI SMALLER THAN SMALLEST ITEM

        assertThat(imperialVolume.isNumberOfItemsSet(2), is(true));

        assertThat(imperialVolume.isTotalBaseUnitsSet(UNIT_FLUID_OUNCE_DECIMAL - .00000001),
                is(false));

        assertThat(imperialVolume.getTotalUnitOne(), is(0.));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitOne(), is(0.));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
        assertThat(imperialVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testBaseUnitsAtMinimumItemSize() { // CONDITION: BASE SI SAME AS SMALLEST ITEM

        assertThat(imperialVolume.isNumberOfItemsSet(2), is(true));

        assertThat(imperialVolume.isTotalBaseUnitsSet(UNIT_FLUID_OUNCE_DECIMAL * 2), is(true));

        assertThat(imperialVolume.getTotalUnitOne(), is(.2));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitOne(), is(.1));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
        assertThat(imperialVolume.getTotalBaseUnits(), is(UNIT_FLUID_OUNCE_DECIMAL * 2));

        System.out.println();
    }

    @Test
    public void testBaseUnitsRetrieveFromPackAndItem() {// CONDITION: BASE SI SET, CHECK PACK AND ITEM UPDATED

        // Set base SI
        assertThat(imperialVolume.isTotalBaseUnitsSet((UNIT_PINT * 12) + (UNIT_FLUID_OUNCE * 2)),
                is(true));

        // Check pack and item values have updated correctly
        assertThat(imperialVolume.getTotalUnitOne(), is(2.));
        assertThat(imperialVolume.getTotalUnitTwo(), is(12));
        assertThat(imperialVolume.getItemUnitOne(), is(2.));
        assertThat(imperialVolume.getItemUnitTwo(), is(12));
        assertThat(imperialVolume.getTotalBaseUnits(),
                is((UNIT_PINT * 12) + (UNIT_FLUID_OUNCE * 2)));

        System.out.println();
    }

    //////////////////////////// PACK MEASUREMENT ONE TESTS \\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Test
    public void testMeasurementUnitOneInRangeMax() { // IN RANGE MAX

        double maxFluidOunces = MAX_VOLUME / UNIT_FLUID_OUNCE;

        // Set to max
        assertThat(imperialVolume.isTotalUnitOneSet(
                Double.parseDouble(decimalInputNumberFormat.format(
                        maxFluidOunces))), is(true));


        // Check value set
        assertThat(imperialVolume.getTotalUnitOne(), is(11.9));
        assertThat(imperialVolume.getTotalUnitTwo(), is(17));
        assertThat(imperialVolume.getItemUnitOne(), is(11.9));
        assertThat(imperialVolume.getItemUnitTwo(), is(17));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMax() { // OUT OF RANGE MAX

        double maxFluidOunces = MAX_VOLUME / UNIT_FLUID_OUNCE;

        // Set to max + 1
        assertThat(imperialVolume.isTotalUnitOneSet(
                Double.parseDouble(decimalInputNumberFormat.format(
                        maxFluidOunces + .1))), is(false));

        // Check values no changes
        assertThat(imperialVolume.getTotalUnitOne(), is(0.));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitOne(), is(0.));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
        assertThat(imperialVolume.getTotalBaseUnits(), is(0.));

        System.out.println();
    }

    @Test
    public void testMeasurementUnitOneInRangeMin() { // IN RANGE MIN

        // Set to minimum
        assertThat(imperialVolume.isTotalUnitOneSet(.1), is(true));

        // Check set
        assertThat(imperialVolume.getTotalUnitOne(), is(.1));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitOne(), is(.1));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
    }

    @Test
    public void testMeasurementUnitOneOutOfRangeMin() { // OUT OF RANGE MIN

        // Set to .01 below min
        assertFalse(imperialVolume.isTotalUnitOneSet(0.09));

        // Check values unchanged
        assertThat(imperialVolume.getTotalUnitOne(), is(0.));
        assertThat(imperialVolume.getTotalUnitTwo(), is(0));
        assertThat(imperialVolume.getItemUnitOne(), is(0.));
        assertThat(imperialVolume.getItemUnitTwo(), is(0));
        assertThat(imperialVolume.getTotalBaseUnits(), is(0.));
    }
}