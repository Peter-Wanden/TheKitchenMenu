package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;

import androidx.core.util.Pair;

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
        return MeasurementSubType.TYPE_METRIC_VOLUME;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return METRIC_VOLUME_NUMBER_OF_MEASUREMENT_UNITS;
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
    public Pair[] getInputDigitsFilter() {
        return null;
    }

    @Override
    public double getPackMeasurementOne() {
        return 0.;
    }

    @Override
    public boolean setPackMeasurementOne(double packMeasurementOne) {
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
    public double getItemMeasurementOne() {
        return 0.;
    }

    @Override
    public boolean setItemMeasurementOne(double itemMeasurementOne) {
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
    public String[] getMeasurementError() {
        return new String[0];
    }

    @Override
    public void resetNumericValues() {

    }
}
