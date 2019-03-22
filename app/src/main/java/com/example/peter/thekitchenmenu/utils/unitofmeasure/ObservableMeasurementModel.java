package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ObservableMeasurementModel extends BaseObservable {

    private static final String TAG = "ObservableMeasurementMo";

    // TODO move this block to ProductModel
    private MeasurementSubType measurementSubType = MeasurementSubType.NOTHING_SELECTED;

    private int minimumMeasurementOne = 0;
    private int minimumMeasurementTwo = 0;
    private int maximumMeasurementOne = 0;
    private int maximumMeasurementTwo = 0;

    private double baseSiUnits = 0;
    private int numberOfItems = 0;
    private int packMeasurementOne = 0;
    private int packMeasurementTwo = 0;
    private int itemMeasurementOne = 0;
    private int itemMeasurementTwo = 0;

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

    public int getMinimumMeasurementOne() {
        return minimumMeasurementOne;
    }

    void setMinimumMeasurementOne(int minimumMeasurementOne) {
        this.minimumMeasurementOne = minimumMeasurementOne;
    }

    void setMinimumMeasurementTwo(int minimumMeasurementTwo) {
        this.minimumMeasurementTwo = minimumMeasurementTwo;
    }

    void setMaximumMeasurementOne(int maximumMeasurementOne) {
        this.maximumMeasurementOne = maximumMeasurementOne;
    }

    public int getMaximumMeasurementTwo() {
        return maximumMeasurementTwo;
    }

    void setMaximumMeasurementTwo(int maximumMeasurementTwo) {
        this.maximumMeasurementTwo = maximumMeasurementTwo;
    }

    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    public void setBaseSiUnits(double baseSiUnits) {
        this.baseSiUnits = baseSiUnits;
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
    public String toString() {
        return "ObservableMeasurementModel{" +
                ", \nmeasurementSubType=" + measurementSubType +
                ", \nnumberOfItems=" + numberOfItems +
                ", \nminimumMeasurementOne=" + minimumMeasurementOne +
                ", \nmaximumMeasurementOne=" + maximumMeasurementOne +
                ", \npackMeasurementOne=" + packMeasurementOne +
                ", \npackMeasurementTwo=" + packMeasurementTwo +
                ", \nitemMeasurementOne=" + itemMeasurementOne +
                ", \nitemMeasurementTwo=" + itemMeasurementTwo +
                '}';
    }

    public void getNumericValuesFromProductModel(ObservableProductModel productModel) {

        Log.d(TAG, "getNumericValuesFromProductModel: Getting values");

        setBaseSiUnits(productModel.getBaseSiUnits());
        setMeasurementSubType(MeasurementSubType.values()[productModel.getUnitOfMeasureSubType()]);
        setNumberOfItems(productModel.getNumberOfItems());
    }

    public void resetNumericValues() {

        numberOfItems = 0;
        minimumMeasurementOne = 0;
        maximumMeasurementOne = 0;

        packMeasurementOne = 0;
        packMeasurementTwo = 0;
        itemMeasurementOne = 0;
        itemMeasurementTwo = 0;
    }
}
