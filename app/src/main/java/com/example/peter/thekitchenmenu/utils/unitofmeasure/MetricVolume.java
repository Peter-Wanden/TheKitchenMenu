package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.BASE_SI_UNIT_VOLUME;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.SINGLE_ITEM;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;

public class MetricVolume implements UnitOfMeasure {

    private static final String TAG = "MetricVolume";

    // Unit values as they relate to the International System of Units, or SI
    private static final int METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS = 3;
    private static final double UNIT_MILLILITRE = BASE_SI_UNIT_VOLUME;
    private static final double UNIT_CENTILITRE = UNIT_MILLILITRE * 100;
    private static final double UNIT_LITRE = UNIT_CENTILITRE * 10;

    private int numberOfItemsInPack = SINGLE_ITEM;

    MetricVolume(Context context) {

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
        return MeasurementSubType.TYPE_METRIC_VOLUME;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
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
    public String getMeasurementUnitThree() {
        return null;
    }

    @Override
    public MeasurementModel getMinAndMax() {
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
        if (numberOfItems >= SINGLE_ITEM &&
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
