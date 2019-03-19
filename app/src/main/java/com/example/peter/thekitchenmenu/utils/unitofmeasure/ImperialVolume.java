package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_PACKS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_PACKS;

public class ImperialVolume implements UnitOfMeasure {

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
    public String getMeasurementUnitOne() {
        return null;
    }

    @Override
    public String getMeasurementUnitTwo() {
        return null;
    }

    @Override
    public ObservableMeasurement getMinAndMax() {
        return null;
    }

    @Override
    public void setNewMeasurementValuesTo(ObservableMeasurement observableMeasurement) {
    }

    @Override
    public boolean getValuesFromObservableProductModel(ObservableProductModel productModel) {
        return false;
    }

    @Override
    public double getBaseSiUnits() {
        return 0;
    }

    @Override
    public boolean setBaseSiUnits(double baseSiUnits) {
        return false;
    }

    @Override
    public boolean setNumberOfPacksInPack(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems >= MULTI_PACK_MINIMUM_NO_OF_PACKS &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_PACKS) {

            this.numberOfItemsInPack = numberOfItems;
            return true;
        }
        return false;
    }

    @Override
    public int getNumberOfPacksInPack() {
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
    public int getSinglePackMeasurementOne() {
        return 0;
    }

    @Override
    public boolean setSinglePackMeasurementOne(int itemMeasurementOne) {
        return false;
    }

    @Override
    public int getSinglePackMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean setSinglePackMeasurementTwo(int itemMeasurementTwo) {
        return false;
    }

    @Override
    public void resetNumericValues() {

    }
}
