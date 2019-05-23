package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.BR;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-MeasurementVM";

    private MutableLiveData<ProductMeasurementModel> modelOut;
    private ProductMeasurementHandler measurementHandler;

    private MeasurementSubType subType;
    private UnitOfMeasure unitOfMeasure;
    private int numberOfMeasurementUnits;
    private int numberOfItems;

    // TODO - these need to be integers and doubles!
    private double packMeasurementOne;
    private double itemMeasurementOne;
    private int packMeasurementTwo;
    private int itemMeasurementTwo;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        modelOut = new MutableLiveData<>();
        measurementHandler = new ProductMeasurementHandler(this);
        unitOfMeasure = MeasurementSubType.TYPE_METRIC_MASS.getMeasurementClass(); // default
        subType = unitOfMeasure.getMeasurementSubType();
        updateMeasurementModel();
    }

    MutableLiveData<ProductMeasurementModel> getModelOut() {
        if (modelOut == null) modelOut = new MutableLiveData<>();
        return modelOut;
    }

    ProductMeasurementHandler getMeasurementHandler() {
        return measurementHandler;
    }

    // TODO - Bind number of measurement units to the display and remove it from Measurement
    //  model. Then re-jig the references to it in xml
    //
    void setMeasurementModelIn(ProductMeasurementModel measurementModelIn) {
        setSubType(measurementModelIn.getMeasurementSubType());
        newNumberOfItems(measurementModelIn.getNumberOfItems());
//        setBaseSiUnits(measurementModelIn.getBaseSiUnits());
    }

    @Bindable
    public MeasurementSubType getSubType() {
        return subType;
    }

    public void setSubType(MeasurementSubType subType) {
        this.subType = subType;
        newUnitOfMeasureSelected(subType);
        notifyPropertyChanged(BR.subType);
    }

    UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    void newUnitOfMeasureSelected(MeasurementSubType subType) {
        if (unitOfMeasure.getMeasurementSubType() != subType) {
            unitOfMeasure = subType.getMeasurementClass();
            setNumberOfMeasurementUnits(unitOfMeasure.getNumberOfMeasurementUnits());
            updateMeasurementModel();
        }
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    private void setNumberOfMeasurementUnits(int numberOfMeasurementUnits) {
        this.numberOfMeasurementUnits = numberOfMeasurementUnits;
        notifyPropertyChanged(BR.numberOfMeasurementUnits);
    }

    @Bindable
    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
        notifyPropertyChanged(BR.numberOfItems);
    }

    public void addOneToNumberOfItems() {
        newNumberOfItems((unitOfMeasure.getNumberOfItems() + 1));
    }

    public void minusOneFromNumberOfItems() {
        newNumberOfItems((unitOfMeasure.getNumberOfItems() - 1));
    }

    private void newNumberOfItems(int newNumberOfItems) {
        if (numberOfItemsHasChanged(newNumberOfItems))
            if (numberOfItemsAreSet(newNumberOfItems)) {
                setNumberOfItems(newNumberOfItems);
                updateMeasurements();
                updateMeasurementModel();
            }
    }

    private boolean numberOfItemsHasChanged(int newNumberOfItems) {
        return unitOfMeasure.getNumberOfItems() != newNumberOfItems;
    }

    private boolean numberOfItemsAreSet(int numberOfItems) {
        return unitOfMeasure.numberOfItemsAreSet(numberOfItems);
    }

    private void setBaseSiUnits(double newBaseSiUnits) {
        if (unitOfMeasure.getBaseSiUnits() != newBaseSiUnits) {
            if (unitOfMeasure.baseSiUnitsAreSet(newBaseSiUnits)) {
                updateMeasurements();
                updateMeasurementModel();
            }
        }
    }

    void validatePackSize(int viewId, int integerMeasurement, double doubleMeasurement) {
        if (numberOfUnitsAfterDecimalForPackAndItemOne() > 0 &&
                viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one) {

            if (measurementHasChangedDouble(viewId, doubleMeasurement))
                processDoubleMeasurements(viewId, doubleMeasurement);

        } else {
            if (measurementHasChangedInteger(viewId, integerMeasurement))
                processIntegerMeasurements(viewId, integerMeasurement);
        }
    }

    private int numberOfUnitsAfterDecimalForPackAndItemOne() {
        Pair[] inputDigitFilters = unitOfMeasure.getInputDigitsFilter();
        return (int) inputDigitFilters[0].second;
    }

    private boolean measurementHasChangedDouble(int viewId, double newMeasurement) {
        double oldMeasurement;

        switch (viewId) {
            case R.id.pack_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getPackMeasurementOne();
                return oldMeasurement != newMeasurement;

            case R.id.item_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getItemMeasurementOne();
                return oldMeasurement != newMeasurement;
        }
        return false;
    }

    private boolean measurementHasChangedInteger(int viewId, int newMeasurement) {
        int oldMeasurement;

        switch (viewId) {
            case R.id.pack_editable_measurement_one:
                oldMeasurement = (int) unitOfMeasure.getPackMeasurementOne();
                return oldMeasurement != newMeasurement;

            case R.id.item_editable_measurement_one:
                oldMeasurement = (int) unitOfMeasure.getItemMeasurementOne();
                return oldMeasurement != newMeasurement;

            case R.id.pack_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getPackMeasurementTwo();
                return newMeasurement != oldMeasurement;

            case R.id.item_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getItemMeasurementTwo();
                return newMeasurement != oldMeasurement;
        }
        return false;
    }

    private void processDoubleMeasurements(int viewId, double newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one)
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);

        if (viewId == R.id.item_editable_measurement_one)
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);

        if (measurementIsSet) {updateMeasurements(); updateMeasurementModel();}
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one){

            Log.d(TAG, "processIntegerMeasurements: Pack one found with value: " + newMeasurement);
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);
        }

        if (viewId == R.id.item_editable_measurement_one)
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);

        if (viewId == R.id.pack_editable_measurement_two)
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        else if (viewId == R.id.item_editable_measurement_two)
            measurementIsSet = unitOfMeasure.itemMeasurementTwoIsSet(newMeasurement);

        if (measurementIsSet) updateMeasurements();
    }

    private void updateMeasurements() {
        setPackMeasurementOne(unitOfMeasure.getPackMeasurementOne());
        setItemMeasurementOne(unitOfMeasure.getItemMeasurementOne());
        setPackMeasurementTwo(unitOfMeasure.getPackMeasurementTwo());
        setItemMeasurementTwo(unitOfMeasure.getItemMeasurementTwo());

        updateMeasurementModel();
    }

    @Bindable
    public double getPackMeasurementOne() {
        return packMeasurementOne;
    }

    public void setPackMeasurementOne(double packMeasurementOne) {
        this.packMeasurementOne = packMeasurementOne;
        notifyPropertyChanged(BR.packMeasurementOne);
    }

    @Bindable
    public double getItemMeasurementOne() {
        return itemMeasurementOne;
    }

    public void setItemMeasurementOne(double itemMeasurementOne) {
        this.itemMeasurementOne = itemMeasurementOne;
        notifyPropertyChanged(BR.itemMeasurementOne);
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
    public int getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    public void setItemMeasurementTwo(int itemMeasurementTwo) {
        this.itemMeasurementTwo = itemMeasurementTwo;
        notifyPropertyChanged(BR.itemMeasurementTwo);
    }

    private void updateMeasurementModel() {
            ProductMeasurementModel modelOut = new ProductMeasurementModel();
            modelOut.setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
            modelOut.setNumberOfItems(unitOfMeasure.getNumberOfItems());
            modelOut.setBaseSiUnits(unitOfMeasure.getBaseSiUnits());

            if(unitOfMeasure.isValidMeasurement()) getModelOut().setValue(modelOut);
            Log.d(TAG, "updateMeasurementModel: model created:" + modelOut.toString());
    }
}