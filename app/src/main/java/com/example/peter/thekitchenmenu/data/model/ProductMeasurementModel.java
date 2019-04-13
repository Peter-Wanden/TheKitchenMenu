package com.example.peter.thekitchenmenu.data.model;

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

        // What if pack one contains zero?
        if (packMeasurementOneAsDecimal == 0.) {

            return "";
        }

        int whole = (int) packMeasurementOneAsDecimal;
        double decimal = whole - packMeasurementOneAsDecimal;

        if (decimal > 0.09) {

            // If packOneAsDecimal does have a fractional part, only display the tenths
            // (remove this part from the Unit of measure classes and put it here?)
            packMeasurementOne = String.format("%.1f", packMeasurementOneAsDecimal);

        } else {

            // If packOneAsDecimal does not have a fractional element after the decimal point, just show the integer
            packMeasurementOne = String.format("%.0f", packMeasurementOneAsDecimal);
        }

        return packMeasurementOne;
    }

    private void setPackMeasurementOne(String packMeasurementOne) {

        // What if the measurement is empty or 0.0
        if (packMeasurementOne.isEmpty()) packMeasurementOneAsDecimal = 0.;

        this.packMeasurementOneAsDecimal = Double.parseDouble(packMeasurementOne);
    }

    public double getPackMeasurementOneAsDecimal() {

        return packMeasurementOneAsDecimal;
    }

    public void setPackMeasurementOneAsDecimal(double packMeasurementOne) {

        this.packMeasurementOneAsDecimal = packMeasurementOne;
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
