package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.data.entity.Product;

import java.util.HashMap;
import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;

public class MetricVolume implements UnitOfMeasure {

    private int numberOfItemsInPack = 0;

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
    public String getMeasurementUnitOne() {
        return null;
    }

    @Override
    public String getMeasurementUnitTwo() {
        return null;
    }

    @Override
    public Measurement getMinAndMax() {
        return null;
    }

    @Override
    public Measurement setNewMeasurementValuesTo(Measurement measurement) {
        return null;
    }

    @Override
    public boolean setValuesFromProduct(Product product) {
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
    public boolean setNumberOfItemsInPack(int numberOfItems) {

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
    public int getNumberOfItemsInPack() {
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
    public void resetNumericValues() {

    }
}
