package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class ObservableMeasurement extends BaseObservable {

    // TODO move this block to ProductModel
    private MeasurementSubType measurementSubType = MeasurementSubType.NOTHING_SELECTED;

    private int minimumMeasurementOne = 0;
    private int minimumMeasurementTwo = 0;
    private int maximumMeasurementOne = 0;
    private int maximumMeasurementTwo = 0;

    private int baseSiUnits = 0;
    private int numberOfPacksInPack = 0;
    private int packMeasurementOne = 0;
    private int packMeasurementTwo = 0;
    private int singlePackMeasurementOne = 0;
    private int singlePackMeasurementTwo = 0;

    @Bindable
    public MeasurementSubType getMeasurementSubType() {
        return measurementSubType;
    }

    public void setMeasurementSubType(MeasurementSubType measurementSubType) {
        this.measurementSubType = measurementSubType;
        notifyPropertyChanged(BR.measurementSubType);
    }

    @Bindable
    public int getNumberOfPacksInPack() {
        return numberOfPacksInPack;
    }

    public void setNumberOfPacksInPack(int numberOfPacksInPack) {
        this.numberOfPacksInPack = numberOfPacksInPack;
        notifyPropertyChanged(BR.numberOfPacksInPack);
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
    public int getSinglePackMeasurementOne() {
        return singlePackMeasurementOne;
    }

    public void setSinglePackMeasurementOne(int singlePackMeasurementOne) {
        this.singlePackMeasurementOne = singlePackMeasurementOne;
        notifyPropertyChanged(BR.singlePackMeasurementOne);
    }

    @Bindable
    public int getSinglePackMeasurementTwo() {
        return singlePackMeasurementTwo;
    }

    public void setSinglePackMeasurementTwo(int singlePackMeasurementTwo) {
        this.singlePackMeasurementTwo = singlePackMeasurementTwo;
        notifyPropertyChanged(BR.singlePackMeasurementTwo);
    }

    @Override
    public void notifyChange() {
        super.notifyChange();
    }

    @Override
    public String toString() {
        return "ObservableMeasurement{" +
                ", \nmeasurementSubType=" + measurementSubType +
                ", \nnumberOfPacksInPack=" + numberOfPacksInPack +
                ", \nminimumMeasurementOne=" + minimumMeasurementOne +
                ", \nmaximumMeasurementOne=" + maximumMeasurementOne +
                ", \npackMeasurementOne=" + packMeasurementOne +
                ", \npackMeasurementTwo=" + packMeasurementTwo +
                ", \nsinglePackMeasurementOne=" + singlePackMeasurementOne +
                ", \nsinglePackMeasurementTwo=" + singlePackMeasurementTwo +
                '}';
    }

    public void resetMeasurementSubType() {

        measurementSubType = MeasurementSubType.NOTHING_SELECTED;
    }

    public void resetNumericValues() {

        numberOfPacksInPack = 0;
        minimumMeasurementOne = 0;
        maximumMeasurementOne = 0;

        packMeasurementOne = 0;
        packMeasurementTwo = 0;
        singlePackMeasurementOne = 0;
        singlePackMeasurementTwo = 0;
    }
}
