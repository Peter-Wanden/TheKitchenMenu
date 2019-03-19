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

    private static final double UNIT_COUNT = BASE_UNIT_COUNT;

    private String type;
    private String subType;
    private String unitCount;

    private int numberOfPacksInPack = 0;
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
    public String getMeasurementUnitOne() {
        return null;
    }

    @Override
    public String getMeasurementUnitTwo() {
        return null;
    }

    @Override
    public ObservableMeasurement getMinAndMax() {

        ObservableMeasurement observableMeasurement = new ObservableMeasurement();
        observableMeasurement.setMinimumMeasurementOne(MINIMUM_COUNT);
        observableMeasurement.setMaximumMeasurementOne(MAXIMUM_COUNT);

        return observableMeasurement;
    }

    @Override
    public void setNewMeasurementValuesTo(ObservableMeasurement observableMeasurement) {
    }

    @Override
    public boolean getValuesFromObservableProductModel(ObservableProductModel productModel) {
        return false;
    }

    @Override
    public double getBaseSiUnits() {
        return baseSiUnits;
    }

    @Override
    public boolean setBaseSiUnits(double baseSiUnits) {

        if (numberOfPacksInPack > 1) {

            if (baseSiUnits <= MAXIMUM_COUNT && baseSiUnits >= UNIT_COUNT * numberOfPacksInPack) {
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
    public boolean setNumberOfPacksInPack(int numberOfItems) {

        // TODO - When setting number of items, check the size / measurements (if available) do not
        // TODO - exceed MAX
        if (numberOfItems >= MULTI_PACK_MINIMUM_NO_OF_PACKS &&
                numberOfItems <= MULTI_PACK_MAXIMUM_NO_OF_PACKS) {

            this.numberOfPacksInPack = numberOfItems;
            notifyPropertyChanged(BR.numberOfPacksInPack);
            return true;
        }
        return false;
    }

    @Override @Bindable
    public int getNumberOfPacksInPack() {
        return numberOfPacksInPack;
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

    @Override @Bindable
    public int getSinglePackMeasurementOne() {
        return 0;
    }

    @Override
    public boolean setSinglePackMeasurementOne(int itemMeasurementOne) {
        return false;
    }

    @Override @Bindable
    public int getSinglePackMeasurementTwo() {
        return 0;
    }

    @Override
    public boolean setSinglePackMeasurementTwo(int itemMeasurementTwo) {
        return false;
    }

    @Override
    public String toString() {
        return "Count{" +
                "\ntype='" + type + '\'' +
                "\n, subType='" + subType + '\'' +
                "\n, unitCount='" + unitCount + '\'' +
                "\n, numberOfPacksInPack=" + numberOfPacksInPack +
                "\n, baseSiUnits=" + baseSiUnits +
                '}';
    }
}
