package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

public class Count implements UnitOfMeasure {

    public Count(Context context) {

    }

    @Override
    public String getTypeAsString() {
        return null;
    }

    @Override
    public MeasurementType getType() {
        return null;
    }

    @Override
    public String getSubTypeAsString() {
        return null;
    }

    @Override
    public MeasurementSubType getSubType() {
        return null;
    }

    @Override
    public List<MeasurementUnits> getUnits() {
        return null;
    }

    @Override
    public String[] getUnitsAsString() {
        return null;
    }

    @Override
    public Measurement getMinAndMax() {
        return null;
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
    public boolean setNumberOfItems(int numberOfItems) {
        return false;
    }

    @Override
    public int getNumberOfItems() {
        return 0;
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
}
