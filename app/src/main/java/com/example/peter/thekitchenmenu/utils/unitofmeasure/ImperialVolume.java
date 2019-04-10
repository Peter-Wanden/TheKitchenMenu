package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.SINGLE_ITEM;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;

public class ImperialVolume implements UnitOfMeasure {

    private static final int IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 2;
    private static final double UNIT_FLUID_OUNCE = BASE_SI_UNIT_VOLUME * 28.4130625;
    private static final double UNIT_PINT = UNIT_FLUID_OUNCE * 20;

    private int numberOfItems = SINGLE_ITEM;

    ImperialVolume(Context context) {

    }

    @Override
    public String getMeasurementTypeAsString() {
        return null;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return null;
    }

    @Override
    public String getMeasurementSubTypeAsString() {
        return null;
    }

    @Override
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_IMPERIAL_VOLUME;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return IMPERIAL_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
    }

    @Override
    public String getMeasurementUnitOneLabel() {
        return null;
    }

    @Override
    public String getMeasurementUnitTwoLabel() {
        return null;
    }

    @Override
    public String getMeasurementUnitThreeLabel() {
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
    public boolean numberOfItemsAreSet(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems > SINGLE_ITEM &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS) {

            this.numberOfItems = numberOfItems;
            return true;
        }
        return false;
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public Pair[] getInputDigitsFilter() {
        return null;
    }

    @Override
    public double getPackMeasurementOne() {
        return 0.;
    }

    @Override
    public boolean packMeasurementOneDecimalIsSet(double packMeasurementOne) {
        return false;
    }

    @Override
    public int getPackMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean packMeasurementTwoIsSet(int packMeasurementTwo) {
        return false;
    }

    @Override
    public int getPackMeasurementThree() {
        return 0;
    }

    @Override
    public boolean packMeasurementThreeIsSet(int packMeasurementThree) {
        return false;
    }

    @Override
    public double getItemMeasurementOne() {
        return 0;
    }

    @Override
    public boolean itemMeasurementOneDecimalIsSet(double itemMeasurementOne) {
        return false;
    }

    @Override
    public int getItemMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean itemMeasurementTwoIsSet(int itemMeasurementTwo) {
        return false;
    }

    @Override
    public int getItemMeasurementThree() {
        return 0;
    }

    @Override
    public boolean itemMeasurementThreeIsSet(int itemMeasurementThree) {
        return false;
    }

    @Override
    public String[] getMeasurementError() {
        return new String[0];
    }

    @Override
    public void resetNumericValues() {

    }
}
