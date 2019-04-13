package com.example.peter.thekitchenmenu.data.model;

import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;

import java.math.BigDecimal;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ProductMeasurementModel extends BaseObservable {

    private static final String TAG = "ProductMeasurementModel";

    // Default
    private MeasurementSubType measurementSubType = MeasurementSubType.TYPE_METRIC_MASS;
    private int numberOfMeasurementUnits = 2;

    private int numberOfItems = 1;
    private String packMeasurementOne;
    private double packMeasurementOneAsDecimal = 0;
    private int packMeasurementTwo = 0;
    private int packMeasurementThree = 0;
    private double itemMeasurementOne = 0;
    private int itemMeasurementTwo = 0;
    private int itemMeasurementThree = 0;

    @Bindable
    public MeasurementSubType getMeasurementSubType() {

        return measurementSubType;
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    public void setNumberOfMeasurementUnits(int numberOfMeasurementUnits) {

        this.numberOfMeasurementUnits = numberOfMeasurementUnits;
        notifyPropertyChanged(BR.numberOfMeasurementUnits);
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

    @Bindable
    public String getPackMeasurementOne() {

        return packMeasurementOne;
    }

    private void setPackMeasurementOne(String packMeasurementOne) {
        Log.d(TAG, "zyx- setPackMeasurementOne: " + packMeasurementOne);
        this.packMeasurementOne = packMeasurementOne;
        notifyPropertyChanged(BR.packMeasurementOne);
    }

    private void setPackMeasurementOneFromDecimal(double packMeasurementOneAsDecimal){

        Log.d(TAG, "setPackMeasurementOneFromDecimal: " + packMeasurementOneAsDecimal);
        packMeasurementOne = Double.toString(packMeasurementOneAsDecimal);
        notifyPropertyChanged(BR.packMeasurementOne);
    }

    @Bindable
    public double getPackMeasurementOneAsDecimal() {

        return packMeasurementOneAsDecimal;
    }

    public void setPackMeasurementOneAsDecimal(double packMeasurementOneAsDecimal) {

        Log.d(TAG, "setPackMeasurementOneAsDecimal: " + packMeasurementOneAsDecimal);
        this.packMeasurementOneAsDecimal = packMeasurementOneAsDecimal;
        notifyPropertyChanged(BR.packMeasurementOneAsDecimal);
        setPackMeasurementOneFromDecimal(this.packMeasurementOneAsDecimal);
    }

    public int getPackMeasurementOneAsInt() {

        return (int) packMeasurementOneAsDecimal;
    }

    public void setPackMeasurementOneAsInt(int packMeasurementOne) {

        this.packMeasurementOneAsDecimal = (double) packMeasurementOne;
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
    public int getPackMeasurementThree() {

        return packMeasurementThree;
    }

    public void setPackMeasurementThree(int packMeasurementThree) {

        this.packMeasurementThree = packMeasurementThree;
        notifyPropertyChanged(BR.packMeasurementThree);
    }

    @Bindable
    public double getItemMeasurementOneAsDecimal() {

        return itemMeasurementOne;
    }

    public void setItemMeasurementOneAsDecimal(double itemMeasurementOne) {

        this.itemMeasurementOne = itemMeasurementOne;
        notifyPropertyChanged(BR.itemMeasurementOneAsDecimal);
    }

    @Bindable
    public int getItemMeasurementOneAsInt() {

        return (int) itemMeasurementOne;
    }

    public void setItemMeasurementOneAsInt(int itemMeasurementOne) {

        this.itemMeasurementOne = (double) itemMeasurementOne;
        notifyPropertyChanged(BR.itemMeasurementOneAsInt);
    }

    @Bindable
    public int getItemMeasurementTwo() {

        return itemMeasurementTwo;
    }

    public void setItemMeasurementTwo(int itemMeasurementTwo) {

        this.itemMeasurementTwo = itemMeasurementTwo;
        notifyPropertyChanged(BR.itemMeasurementTwo);
    }

    @Bindable
    public int getItemMeasurementThree() {

        return itemMeasurementThree;
    }

    public void setItemMeasurementThree(int itemMeasurementThree) {

        this.itemMeasurementThree = itemMeasurementThree;
        notifyPropertyChanged(BR.itemMeasurementThree);
    }

    @Override
    public String toString() {

        return "ProductMeasurementModel{" +
                ", \nmeasurementSubType=" + measurementSubType +
                ", \nnumberOfItems=" + numberOfItems +
                ", \npackMeasurementOneAsDecimal=" + packMeasurementOneAsDecimal +
                ", \nitemMeasurementOne=" + itemMeasurementOne +
                ", \npackMeasurementTwo=" + packMeasurementTwo +
                ", \nitemMeasurementTwo=" + itemMeasurementTwo +
                '}';
    }
}
