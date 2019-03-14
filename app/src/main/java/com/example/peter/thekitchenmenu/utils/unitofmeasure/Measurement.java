package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Measurement extends BaseObservable {

    private String typeAsString = "";
    private MeasurementSubType measurementSubType = MeasurementSubType.NOTHING_SELECTED;

    private String measurementUnitOne = "";
    private String measurementUnitTwo = "";

    private int minimumMeasurementOne = 0;
    private int minimumMeasurementTwo = 0;
    private int maximumMeasurementOne = 0;
    private int maximumMeasurementTwo = 0;

    private int baseSiUnits = 0;
    private int numberOfItemsInPack = 0;
    private int packMeasurementOne = 0;
    private int packMeasurementTwo = 0;
    private int itemMeasurementOne = 0;
    private int itemMeasurementTwo = 0;

    @Bindable
    public String getTypeAsString() {
        return typeAsString;
    }

    public void setTypeAsString(String typeAsString) {
        this.typeAsString = typeAsString;
        notifyPropertyChanged(BR.typeAsString);
    }

    @Bindable
    public MeasurementSubType getMeasurementSubType() {
        return measurementSubType;
    }

    public void setMeasurementSubType(MeasurementSubType measurementSubType) {
        this.measurementSubType = measurementSubType;
        notifyPropertyChanged(BR.measurementSubType);
    }

    @Bindable
    public int getNumberOfItemsInPack() {
        return numberOfItemsInPack;
    }

    public void setNumberOfItemsInPack(int numberOfItemsInPack) {
        this.numberOfItemsInPack = numberOfItemsInPack;
        notifyPropertyChanged(BR.numberOfItemsInPack);
    }

    @Bindable
    public String getMeasurementUnitOne() {
        return measurementUnitOne;
    }

    public void setMeasurementUnitOne(String measurementUnitOne) {
        this.measurementUnitOne = measurementUnitOne;
        notifyPropertyChanged(BR.measurementUnitOne);
    }

    @Bindable
    public String getMeasurementUnitTwo() {
        return measurementUnitTwo;
    }

    public void setMeasurementUnitTwo(String measurementUnitTwo) {
        this.measurementUnitTwo = measurementUnitTwo;
        notifyPropertyChanged(BR.measurementUnitTwo);
    }

    public int getMinimumMeasurementOne() {
        return minimumMeasurementOne;
    }

    public void setMinimumMeasurementOne(int minimumMeasurementOne) {
        this.minimumMeasurementOne = minimumMeasurementOne;
    }

    public int getMinimumMeasurementTwo() {
        return minimumMeasurementTwo;
    }

    public void setMinimumMeasurementTwo(int minimumMeasurementTwo) {
        this.minimumMeasurementTwo = minimumMeasurementTwo;
    }

    public int getMaximumMeasurementOne() {
        return maximumMeasurementOne;
    }

    public void setMaximumMeasurementOne(int maximumMeasurementOne) {
        this.maximumMeasurementOne = maximumMeasurementOne;
    }

    public int getMaximumMeasurementTwo() {
        return maximumMeasurementTwo;
    }

    public void setMaximumMeasurementTwo(int maximumMeasurementTwo) {
        this.maximumMeasurementTwo = maximumMeasurementTwo;
    }

    @Bindable
    public int getBaseSiUnits() {
        return baseSiUnits;
    }

    public void setBaseSiUnits(int baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
        notifyPropertyChanged(BR.baseSiUnits);
    }

    @Bindable
    public int getPackMeasurementOne() {
        return packMeasurementOne;
    }

    public void setPackMeasurementOne(int packMeasurementOne) {
        this.packMeasurementOne = packMeasurementOne;
        notifyPropertyChanged(BR.packMeasurementOne);
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
    public int getItemMeasurementOne() {
        return itemMeasurementOne;
    }

    public void setItemMeasurementOne(int itemMeasurementOne) {
        this.itemMeasurementOne = itemMeasurementOne;
        notifyPropertyChanged(BR.itemMeasurementOne);
    }

    @Bindable
    public int getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    public void setItemMeasurementTwo(int itemMeasurementTwo) {
        this.itemMeasurementTwo = itemMeasurementTwo;
        notifyPropertyChanged(BR.itemMeasurementTwo);
    }

    @Override
    public void notifyChange() {
        super.notifyChange();
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "\ntypeAsString='" + typeAsString + '\'' +
                ", \nmeasurementSubType=" + measurementSubType +
                ", \nnumberOfItemsInPack=" + numberOfItemsInPack +
                ", \nmeasurementUnitOne='" + measurementUnitOne + '\'' +
                ", \nmeasurementUnitTwo='" + measurementUnitTwo + '\'' +
                ", \nminimumMeasurementOne=" + minimumMeasurementOne +
                ", \nmaximumMeasurementOne=" + maximumMeasurementOne +
                ", \npackMeasurementOne=" + packMeasurementOne +
                ", \npackMeasurementTwo=" + packMeasurementTwo +
                ", \nitemMeasurementOne=" + itemMeasurementOne +
                ", \nitemMeasurementTwo=" + itemMeasurementTwo +
                '}';
    }

    public void resetNonNumericValues() {

        typeAsString = "";
        measurementSubType = MeasurementSubType.NOTHING_SELECTED;

        measurementUnitOne = "";
        measurementUnitTwo = "";
    }

    public void resetNumericValues() {

        numberOfItemsInPack = 0;
        minimumMeasurementOne = 0;
        maximumMeasurementOne = 0;

        packMeasurementOne = 0;
        packMeasurementTwo = 0;
        itemMeasurementOne = 0;
        itemMeasurementTwo = 0;
    }
}
