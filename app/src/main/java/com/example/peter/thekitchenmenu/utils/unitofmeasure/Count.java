package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.content.res.Resources;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.*;

public class Count extends BaseObservable implements UnitOfMeasure {

    private static final String TAG = "Count";

    private static final int COUNT_NUMBER_OF_MEASUREMENT_UNITS = 1;
    private static final double UNIT_COUNT = BASE_UNIT_COUNT;

    private String type;
    private String subType;
    private String unitCount;

    private int numberOfItems = 0;
    private double baseSiUnits = 0;

    public Count(Context context) {

        Resources resources = context.getResources();
        type = resources.getString(R.string.count);
        subType = resources.getString(R.string.count);
        unitCount = resources.getString(R.string.each);
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public MeasurementType getMeasurementType() {
        return MeasurementType.TYPE_COUNT;
    }

    @Override @Bindable
    public MeasurementSubType getMeasurementSubType() {
        return MeasurementSubType.TYPE_COUNT;
    }

    @Override
    public int getNumberOfMeasurementUnits() {
        return COUNT_NUMBER_OF_MEASUREMENT_UNITS;
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
    public ObservableMeasurementModel getMinAndMax() {

        ObservableMeasurementModel observableMeasurementModel = new ObservableMeasurementModel();
        observableMeasurementModel.setMinimumMeasurementOne(MINIMUM_COUNT);
        observableMeasurementModel.setMaximumMeasurementOne(MAXIMUM_COUNT);

        return observableMeasurementModel;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean baseSiUnitsAreSet(double baseSiUnits) {

        if (numberOfItems > 1) {

            if (baseSiUnits <= MAXIMUM_COUNT && baseSiUnits >= UNIT_COUNT * numberOfItems) {
                this.baseSiUnits = baseSiUnits;
                notifyPropertyChanged(BR.packMeasurementOne);
                return true;
            }
        }

        if (baseSiUnits > MINIMUM_COUNT && baseSiUnits < MAXIMUM_COUNT) {
            this.baseSiUnits = baseSiUnits;
            return true;
        }
        return false;
    }

    @Override
    public boolean setNumberOfItems(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems >= MULTI_PACK_MINIMUM_NO_OF_ITEMS &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_ITEMS) {

            this.numberOfItems = numberOfItems;
            notifyPropertyChanged(BR.numberOfItems);
            return true;
        }
        return false;
    }

    @Override @Bindable
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public int[] getInputFilterFormat() {
        int[] inputFilterFormat = new int[5];
        inputFilterFormat[1] = 4;
        inputFilterFormat[2] = 5;
        return inputFilterFormat;
    }

    @Override
    public void resetNumericValues() {

    }

    @Override @Bindable
    public int getPackMeasurementOne() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementOne(int packMeasurementOne) {
        return false;
    }

    @Override @Bindable
    public int getPackMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementTwo(int packMeasurementTwo) {
        return false;
    }

    @Override
    public int getPackMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setPackMeasurementThree(int packMeasurementThree) {
        return false;
    }

    @Override @Bindable
    public int getItemMeasurementOne() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementOne(int itemMeasurementOne) {
        return false;
    }

    @Override @Bindable
    public int getItemMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementTwo(int itemMeasurementTwo) {
        return false;
    }

    @Override
    public int getItemMeasurementThree() {
        return 0;
    }

    @Override
    public boolean setItemMeasurementThree(int itemMeasurementThree) {
        return false;
    }

    @Override
    public String toString() {
        return "Count{" +
                "\ntype='" + type + '\'' +
                "\n, subType='" + subType + '\'' +
                "\n, unitCount='" + unitCount + '\'' +
                "\n, numberOfItems=" + numberOfItems +
                "\n, baseSiUnits=" + baseSiUnits +
                '}';
    }
}
