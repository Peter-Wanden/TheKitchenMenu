package com.example.peter.thekitchenmenu.data.model;

import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ProductMeasurementModel extends BaseObservable {

    private static final String TAG = "ProductMeasurementModel";

    // Default
    private MeasurementSubType measurementSubType = MeasurementSubType.TYPE_METRIC_MASS;
    private int numberOfMeasurementUnits = 2;

    private int numberOfItems = 1;

    private String packMeasurementOne;
    private int packMeasurementOneAsInt = 0;
    private double packMeasurementOneAsDecimal = 0.;

    private String itemMeasurementOne;
    private int itemMeasurementOneAsInt = 0;
    private double itemMeasurementOneAsDecimal = 0.;

    private int packMeasurementTwo = 0;
    private int itemMeasurementTwo = 0;

    private int packMeasurementThree = 0;
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

        Log.d(TAG, "tkm - getPackMeasurementOne: " + packMeasurementOne);
        return packMeasurementOne;
    }

    private void setPackMeasurementOne(String packMeasurementOne) {

        Log.d(TAG, "tkm - setPackMeasurementOne: Value passed in to method   : " +
                packMeasurementOne);
        Log.d(TAG, "tkm - setPackMeasurementOne: Instance value before change: " +
                this.packMeasurementOne);

        this.packMeasurementOne = packMeasurementOne;

        Log.d(TAG, "tkm - setPackMeasurementOne: New instance value          : " +
                this.packMeasurementOne);

        notifyPropertyChanged(BR.packMeasurementOne);
    }

    public double getPackMeasurementOneAsDecimal() {

        return packMeasurementOneAsDecimal;
    }

    public void setPackMeasurementOneAsDecimal(double packMeasurementOneAsDecimal) {

        Log.d(TAG, "tkm - getPackMeasurementOneAsDecimal: " + packMeasurementOneAsDecimal);

        this.packMeasurementOneAsDecimal = packMeasurementOneAsDecimal;
        setPackMeasurementOne(Double.toString(this.packMeasurementOneAsDecimal));
    }

    public int getPackMeasurementOneAsInt() {

        return packMeasurementOneAsInt;
    }

    public void setPackMeasurementOneAsInt(int packMeasurementOneAsInt) {

        Log.d(TAG, "tkm - setPackMeasurementOneAsInt: new measurement is: " +
                packMeasurementOneAsInt);

        this.packMeasurementOneAsInt = packMeasurementOneAsInt;
        setPackMeasurementOne(Integer.toString(this.packMeasurementOneAsInt));
    }

    @Bindable
    public int getPackMeasurementTwo() {

        Log.d(TAG, "tkm - getPackMeasurementTwo: " + packMeasurementTwo);
        return packMeasurementTwo;
    }

    public void setPackMeasurementTwo(int packMeasurementTwo) {

        this.packMeasurementTwo = packMeasurementTwo;
        Log.d(TAG, "tkm - setPackMeasurementTwo: " + this.packMeasurementTwo);
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
    public String getItemMeasurementOne() {

        return itemMeasurementOne;
    }

    public void setItemMeasurementOne(String itemMeasurementOne) {

        this.itemMeasurementOne = itemMeasurementOne;
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

        Log.d(TAG, "tkm - getItemMeasurementTwo: " + itemMeasurementTwo);
        return itemMeasurementTwo;
    }

    public void setItemMeasurementTwo(int itemMeasurementTwo) {

        this.itemMeasurementTwo = itemMeasurementTwo;
        Log.d(TAG, "tkm - setItemMeasurementTwo: " + this.itemMeasurementTwo);
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

        return "tkm - ProductMeasurementModel{" +
                ", \nmeasurementSubType=" + measurementSubType +
                ", \nnumberOfItems=" + numberOfItems +
                ", \npackMeasurementOneAsDecimal=" + packMeasurementOneAsDecimal +
                ", \nitemMeasurementOne=" + itemMeasurementOne +
                ", \npackMeasurementTwo=" + packMeasurementTwo +
                ", \nitemMeasurementTwo=" + itemMeasurementTwo +
                '}';
    }
}
