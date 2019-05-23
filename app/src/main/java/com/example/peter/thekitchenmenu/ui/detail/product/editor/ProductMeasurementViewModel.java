package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.content.res.Resources;
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

    private Resources resources;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        modelOut = new MutableLiveData<>();
        measurementHandler = new ProductMeasurementHandler(this);
        setSubType(MeasurementSubType.TYPE_METRIC_MASS); // default
        resources = application.getResources();
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
        setNumberOfItems(measurementModelIn.getNumberOfItems());
        setBaseSiUnits(measurementModelIn.getBaseSiUnits());
    }

    @Bindable
    public MeasurementSubType getSubType() {
        return subType;
    }

    public void setSubType(MeasurementSubType subType) {
        if (this.subType != subType) {
            newUnitOfMeasureSelected(subType);
        }
    }

    UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    private void newUnitOfMeasureSelected(MeasurementSubType subType) {
        unitOfMeasure = subType.getMeasurementClass();
        updateUi();
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    @Bindable
    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void addOneToNumberOfItems() {
        setNumberOfItems((unitOfMeasure.getNumberOfItems() + 1));
    }

    public void minusOneFromNumberOfItems() {
        setNumberOfItems((unitOfMeasure.getNumberOfItems() - 1));
    }

    public void setNumberOfItems(int numberOfItems) {
        if (newNumberOfItemsReceived(numberOfItems))
            if (canChangeToNewNumberOfItems(numberOfItems)) updateUi(); // with new value
                // Room to add error message if required
            else updateUi(); // with old value
    }

    private boolean newNumberOfItemsReceived(int newNumberOfItems) {
        return unitOfMeasure.getNumberOfItems() != newNumberOfItems;
    }

    private boolean canChangeToNewNumberOfItems(int numberOfItems) {
        return unitOfMeasure.numberOfItemsAreSet(numberOfItems);
    }

    private void setBaseSiUnits(double newBaseSiUnits) {
        if (newBaseUnitsReceived(newBaseSiUnits)) {
            if (canChangeToNewNumberOfBaseSiUnits(newBaseSiUnits)) updateUi(); // with new value
                // Room to add error message if required
            else updateUi(); // with old value
        }
    }

    private boolean newBaseUnitsReceived(double newBaseSiUnits) {
        return unitOfMeasure.getBaseSiUnits() != newBaseSiUnits;
    }

    private boolean canChangeToNewNumberOfBaseSiUnits(double newBaseSiUnits) {
        return unitOfMeasure.baseSiUnitsAreSet(newBaseSiUnits);
    }

    void newMeasurementReceived(int viewId,
                                int integerMeasurement,
                                double decimalMeasurement,
                                int numberOfUnitsAfterDecimal) {

        if (numberOfUnitsAfterDecimal > 0) {
            Log.d(TAG, "newMeasurementReceived from " +
                    resources.getResourceEntryName(viewId) +
                    " with decimal value: " + decimalMeasurement);

            if (decimalMeasurementHasChanged(viewId, decimalMeasurement))
                processDecimalMeasurements(viewId, decimalMeasurement);

        } else {
            Log.d(TAG, "newMeasurementReceived from " +
                    resources.getResourceEntryName(viewId) +
                    " with decimal value: " + integerMeasurement);

            if (measurementHasChangedInteger(viewId, integerMeasurement))
                processIntegerMeasurements(viewId, integerMeasurement);
        }
    }

    private boolean decimalMeasurementHasChanged(int viewId, double newMeasurement) {
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

    private void processDecimalMeasurements(int viewId, double decimalMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one) {
            Log.d(TAG, "processDecimalMeasurements - packMeasurementOne:" +
                    " old=" + unitOfMeasure.getPackMeasurementOne() +
                    " new=" + decimalMeasurement);
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(decimalMeasurement);

        }
        if (viewId == R.id.item_editable_measurement_one) {
            Log.d(TAG, "processDecimalMeasurements - itemMeasurementOne:" +
                    " old=" + unitOfMeasure.getItemMeasurementOne() +
                    " new=" + decimalMeasurement);
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(decimalMeasurement);
        }

        Log.d(TAG, "processDecimalMeasurements: isSet=" + measurementIsSet);

        if (measurementIsSet) updateUi();
        else if (unitOfMeasure.getBaseSiUnits() == 0.0) updateUi();
        Log.d(TAG, "processDecimalMeasurements: baseSi= " + unitOfMeasure.getBaseSiUnits());
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one){
            Log.d(TAG, "processIntegerMeasurements - packMeasurementOne:" +
                    " old=" + unitOfMeasure.getPackMeasurementOne() +
                    " new=" + newMeasurement);
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);
        }
        if (viewId == R.id.item_editable_measurement_one) {
            Log.d(TAG, "processIntegerMeasurements - itemMeasurementOne:" +
                    " old=" + unitOfMeasure.getItemMeasurementOne() +
                    " new=" + newMeasurement);
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);
        }
        if (viewId == R.id.pack_editable_measurement_two) {
            Log.d(TAG, "processIntegerMeasurements - packMeasurementTwo:" +
                    " old=" + unitOfMeasure.getPackMeasurementTwo() +
                    " new=" + newMeasurement);
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);
        }

        if (viewId == R.id.item_editable_measurement_two) {
            Log.d(TAG, "processIntegerMeasurements - itemMeasurementTwo:" +
                    " old=" + unitOfMeasure.getItemMeasurementTwo() +
                    " new=" + newMeasurement);
            measurementIsSet = unitOfMeasure.itemMeasurementTwoIsSet(newMeasurement);
        }

        Log.d(TAG, "processIntegerMeasurements: isSet=" + measurementIsSet);
        Log.d(TAG, "processIntegerMeasurements: baseSi= " + unitOfMeasure.getBaseSiUnits());

        if (measurementIsSet) updateUi();
        else if (unitOfMeasure.getBaseSiUnits() == 0.0) updateUi();
    }

    private void updateUi() {
        if (subType != unitOfMeasure.getMeasurementSubType()) {
            subType = unitOfMeasure.getMeasurementSubType();
            notifyPropertyChanged(BR.subType);
        }
        if (numberOfMeasurementUnits != unitOfMeasure.getNumberOfMeasurementUnits()) {
            numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();
            notifyPropertyChanged(BR.numberOfMeasurementUnits);
        }
        if (numberOfItems != unitOfMeasure.getNumberOfItems()) {
            numberOfItems = unitOfMeasure.getNumberOfItems();
            notifyPropertyChanged(BR.numberOfItems);
        }
        if (packMeasurementOne != unitOfMeasure.getPackMeasurementOne()) {
            Log.d(TAG, "updateUi: packOne: " +
                    "ui=" + packMeasurementOne + " uom=" + unitOfMeasure.getPackMeasurementOne());
            packMeasurementOne = unitOfMeasure.getPackMeasurementOne();
            notifyPropertyChanged(BR.packMeasurementOne);
        }
        if (itemMeasurementOne != unitOfMeasure.getItemMeasurementOne()) {
            Log.d(TAG, "updateUi: itemOne " +
                    "ui=" + itemMeasurementOne + " uom=" + unitOfMeasure.getItemMeasurementOne());
            itemMeasurementOne = unitOfMeasure.getItemMeasurementOne();
            notifyPropertyChanged(BR.itemMeasurementOne);
        }
        if (numberOfMeasurementUnits > 1) {
            if (packMeasurementTwo != unitOfMeasure.getPackMeasurementTwo()) {
                packMeasurementTwo = unitOfMeasure.getPackMeasurementTwo();
                notifyPropertyChanged(BR.packMeasurementTwo);
            }
            if (itemMeasurementTwo != unitOfMeasure.getItemMeasurementTwo()) {
                itemMeasurementTwo = unitOfMeasure.getItemMeasurementTwo();
                notifyPropertyChanged(BR.itemMeasurementTwo);
            }
        }
        updateMeasurementModel();
    }

    private void updateMeasurementModel() {
        ProductMeasurementModel modelOut = new ProductMeasurementModel();
        modelOut.setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
        modelOut.setNumberOfItems(unitOfMeasure.getNumberOfItems());
        modelOut.setBaseSiUnits(unitOfMeasure.getBaseSiUnits());

        if (unitOfMeasure.isValidMeasurement()) {
            getModelOut().setValue(modelOut);
            Log.d(TAG, "updateMeasurementModel: model isValid:" + modelOut.toString());
        } else Log.d(TAG, "updateMeasurementModel: model isNotValid");
    }

    @Bindable
    public double getPackMeasurementOne() {
        return packMeasurementOne;
    }

    public void setPackMeasurementOne(double packMeasurementOne) {
//        this.packMeasurementOne = packMeasurementOne;
//        notifyPropertyChanged(BR.packMeasurementOne);
    }

    @Bindable
    public double getItemMeasurementOne() {
        return itemMeasurementOne;
    }

    public void setItemMeasurementOne(double itemMeasurementOne) {
//        this.itemMeasurementOne = itemMeasurementOne;
//        notifyPropertyChanged(BR.itemMeasurementOne);
    }

    @Bindable
    public int getPackMeasurementTwo() {
        return packMeasurementTwo;
    }

    public void setPackMeasurementTwo(int packMeasurementTwo) {
//        this.packMeasurementTwo = packMeasurementTwo;
//        notifyPropertyChanged(BR.packMeasurementTwo);
    }

    @Bindable
    public int getItemMeasurementTwo() {
        return itemMeasurementTwo;
    }

    public void setItemMeasurementTwo(int itemMeasurementTwo) {
//        this.itemMeasurementTwo = itemMeasurementTwo;
//        notifyPropertyChanged(BR.itemMeasurementTwo);
    }
}