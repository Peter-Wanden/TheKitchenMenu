package com.example.peter.thekitchenmenu.data.model;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.google.firebase.database.annotations.NotNull;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ProductMeasurementModel extends BaseObservable {

    private static final String TAG = "tkm - MeasurementModel";

    // Default
    private MeasurementSubType measurementSubType;

    private int numberOfItems = 1;

    private int packMeasurementOneAsInt = 0;
    private double packMeasurementOneAsDecimal = 0.;

    private String itemMeasurementOne;
    private int itemMeasurementOneAsInt = 0;
    private double itemMeasurementOneAsDecimal = 0.;

    private int packMeasurementTwo = 0;
    private int itemMeasurementTwo = 0;

    private double baseSiUnits;

    @Bindable
    public MeasurementSubType getMeasurementSubType() {
        return measurementSubType;
    }

    public void setMeasurementSubType(MeasurementSubType measurementSubType) {
        this.measurementSubType = measurementSubType;
        notifyPropertyChanged(BR.measurementSubType);
    }

    @Bindable
    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        notifyPropertyChanged(BR.numberOfItems);
    }

    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    public void setBaseSiUnits(double baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
    }

    @Bindable
    public double getPackMeasurementOneAsDecimal() {
        return packMeasurementOneAsDecimal;
    }

    public void setPackMeasurementOneAsDecimal(double packMeasurementOneAsDecimal) {
        this.packMeasurementOneAsDecimal = packMeasurementOneAsDecimal;
        notifyPropertyChanged(BR.packMeasurementOneAsDecimal);
    }

    @Bindable
    public int getPackMeasurementOneAsInt() {
        return packMeasurementOneAsInt;
    }

    public void setPackMeasurementOneAsInt(int packMeasurementOneAsInt) {
        this.packMeasurementOneAsInt = packMeasurementOneAsInt;
        notifyPropertyChanged(BR.packMeasurementOneAsInt);
    }

    @Bindable
    public int getPackMeasurementTwo() {
        return packMeasurementTwo;
    }

    public void setPackMeasurementTwo(int packMeasurementTwo) {
        this.packMeasurementTwo = packMeasurementTwo;
        notifyPropertyChanged(BR.packMeasurementTwo);
    }

    @Bindable
    public String getItemMeasurementOne() {
        return itemMeasurementOne;
    }

    public void setItemMeasurementOne(String itemMeasurementOne) {
        if (itemMeasurementOne.equals("0")) this.itemMeasurementOne = "";
        else this.itemMeasurementOne = itemMeasurementOne;
        notifyPropertyChanged(BR.itemMeasurementOne);
    }

    @Bindable
    public double getItemMeasurementOneAsDecimal() {
        return itemMeasurementOneAsDecimal;
    }

    public void setItemMeasurementOneAsDecimal(double itemMeasurementOneAsDecimal) {
        this.itemMeasurementOneAsDecimal = itemMeasurementOneAsDecimal;
        notifyPropertyChanged(BR.itemMeasurementOneAsDecimal);
        setItemMeasurementOne(Double.toString(itemMeasurementOneAsDecimal));
    }

    @Bindable
    public int getItemMeasurementOneAsInt() {
        return itemMeasurementOneAsInt;
    }

    public void setItemMeasurementOneAsInt(int itemMeasurementOneAsInt) {
        this.itemMeasurementOneAsInt = itemMeasurementOneAsInt;
        notifyPropertyChanged(BR.itemMeasurementOneAsInt);
        setItemMeasurementOne(Integer.toString(this.itemMeasurementOneAsInt));
    }

    @Bindable
    public int getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    public void setItemMeasurementTwo(int itemMeasurementTwo) {
        this.itemMeasurementTwo = itemMeasurementTwo;
        notifyPropertyChanged(BR.itemMeasurementTwo);
    }

    @NotNull
    @Override
    public String toString() {
        return "\ntkm-ProductMeasurementModel{" +
                "\nmeasurementSubType="         + measurementSubType +
                "\nnumberOfItems="              + numberOfItems +
                "\npackMeasurementOneAsInt="    + packMeasurementOneAsInt +
                "\npackMeasurementOneAsDecimal="+ packMeasurementOneAsDecimal +
                "\nitemMeasurementOne='"        + itemMeasurementOne + '\'' +
                "\nitemMeasurementOneAsInt="    + itemMeasurementOneAsInt +
                "\nitemMeasurementOneAsDecimal="+ itemMeasurementOneAsDecimal +
                "\npackMeasurementTwo="         + packMeasurementTwo +
                "\nitemMeasurementTwo="         + itemMeasurementTwo +
                "\nbaseSiUnits="                + baseSiUnits +
                '}';
    }
}
