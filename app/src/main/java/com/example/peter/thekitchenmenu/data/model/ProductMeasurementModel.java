package com.example.peter.thekitchenmenu.data.model;

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

    private int packMeasurementOneAsInt = 0;
    private double packMeasurementOneAsDecimal = 0.;

    private String itemMeasurementOne;
    private int itemMeasurementOneAsInt = 0;
    private double itemMeasurementOneAsDecimal = 0.;

    private int packMeasurementTwo = 0;
    private int itemMeasurementTwo = 0;


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
}
