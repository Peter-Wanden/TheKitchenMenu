package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_MASS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;

public class ImperialVolume implements UnitOfMeasure {

    private static final int IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_FLUID_OUNCE = BASE_SI_UNIT_VOLUME * 28.4130625;
    private static final double UNIT_PINT = UNIT_FLUID_OUNCE * 20;

    private int numberOfItemsInPack = 0;

    ImperialVolume(Context context) {

    }

    @Override
    public String getTypeAsString() {
        return null;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return null;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_IMPERIAL_VOLUME;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return 0;
    }

    @Override
    public String getMeasurementUnitOne() {
        return null;
    }

    @Override
    public String getMeasurementUnitTwo() {
        return null;
    }

    @Override
    public ObservableMeasurementModel getMinAndMax() {
        return null;
    }

    @Override
    public double getBaseSiUnits() {
        return 0;
    }

    @Override
    public boolean baseSiUnitsAreSet(double baseSiUnits) {
        return false;
    }

    @Override
    public boolean setNumberOfItems(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems >= MULTI_PACK_MINIMUM_NO_OF_ITEMS &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS) {

            this.numberOfItemsInPack = numberOfItems;
            return true;
        }
        return false;
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItemsInPack;
    }

    @Override
    public int[] getInputFilterFormat() {
        return null;
    }

    @Override
    public int getPackMeasurementOne() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {
        return false;
    }

    @Override
    public int getPackMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {
        return false;
    }

    @Override
    public int getPackMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementThree(int packMeasurementThree) {
        return false;
    }

    @Override
    public int getItemMeasurementOne() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementOne(int itemMeasurementOne) {
        return false;
    }

    @Override
    public int getItemMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {
        return false;
    }

    @Override
    public int getItemMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementThree(int itemMeasurementThree) {
        return false;
    }

    @Override
    public void resetNumericValues() {

    }
}
