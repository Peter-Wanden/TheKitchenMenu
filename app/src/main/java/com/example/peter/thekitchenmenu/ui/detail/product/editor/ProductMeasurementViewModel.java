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

    private MutableLiveData<ProductMeasurementModel> existingMeasurementModel =
            new MutableLiveData<>();
    // Whenever a editedMeasurementModel is valid, set it to measurement model
    private ProductMeasurementModel editedMeasurementModel;
    private ProductMeasurementHandler measurementHandler;

    private UnitOfMeasure unitOfMeasure;
    private int numberOfMeasurementUnits;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        editedMeasurementModel = new ProductMeasurementModel();
        measurementHandler = new ProductMeasurementHandler(this);
        unitOfMeasure = MeasurementSubType.TYPE_METRIC_MASS.getMeasurementClass(); // default
        updateMeasurementModel();
    }

    // TODO - Bind number of measurement units to the display and remove it from Measurement
    //  model. Then re-jig the references to it in xml
    //
    MutableLiveData<ProductMeasurementModel> getExistingMeasurementModel() {
        return existingMeasurementModel;
    }

    ProductMeasurementHandler getMeasurementHandler() {
        return measurementHandler;
    }

    ProductMeasurementModel getEditedMeasurementModel() {
        return editedMeasurementModel;
    }

    UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    @Bindable
    public int getNumberOfMeasurementUnits() {
        return numberOfMeasurementUnits;
    }

    private void setNumberOfMeasurementUnits(int numberOfMeasurementUnits) {
        this.numberOfMeasurementUnits = numberOfMeasurementUnits;
        notifyPropertyChanged(BR.numberOfMeasurementUnits);
    }

    void newUnitOfMeasureSelected(int subTypeAsInt) {
        if (unitOfMeasure.getMeasurementSubType().ordinal() != subTypeAsInt) {
            unitOfMeasure = MeasurementSubType.values()[subTypeAsInt].getMeasurementClass();
            setNumberOfMeasurementUnits(unitOfMeasure.getNumberOfMeasurementUnits());
            updateMeasurementModel();
        }
    }

    boolean numberOfItemsChanged(int newNumberOfItems) {
        if (numberOfItemsHasChanged(newNumberOfItems)) {
            if (numberOfItemsAreSet(newNumberOfItems)) {
                return true;
            }
        }
        return false;
    }

    private boolean numberOfItemsHasChanged(int newNumberOfItems) {
        return unitOfMeasure.getNumberOfItems() != newNumberOfItems;
    }

    private boolean numberOfItemsAreSet(int numberOfItems) {
        if (unitOfMeasure.numberOfItemsAreSet(numberOfItems)) {
            updateMeasurementModel();
            return true;
        }
        return false;
    }

    void modifyNumberOfItemsByOne(int buttonId) {
        int itemsInPack = unitOfMeasure.getNumberOfItems();

        if (buttonId == R.id.multi_pack_plus)
            numberOfItemsChanged((itemsInPack + 1));

        if (buttonId == R.id.multi_pack_minus)
            numberOfItemsChanged((itemsInPack - 1));
    }

    void setBaseSiUnits(double newBaseSiUnits) {
        if (unitOfMeasure.getBaseSiUnits() != newBaseSiUnits) {
            unitOfMeasure.baseSiUnitsAreSet(newBaseSiUnits);
            updateMeasurementModel();
        }
    }

    void validatePackSize(int viewId, int integerMeasurement, double doubleMeasurement) {
        int numberOfUnitsAfterDecimal;

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one) {
            Pair[] inputDigitFilters = unitOfMeasure.getInputDigitsFilter();
            numberOfUnitsAfterDecimal = (int) inputDigitFilters[0].second;

        } else numberOfUnitsAfterDecimal = 0;

        if (numberOfUnitsAfterDecimal > 0) {

            if (measurementHasChangedDouble(viewId, doubleMeasurement)) {
                if (viewId == R.id.pack_editable_measurement_one)
                    editedMeasurementModel.setPackMeasurementOneAsDecimal(doubleMeasurement);

                if (viewId == R.id.item_editable_measurement_one)
                    editedMeasurementModel.setItemMeasurementOneAsDecimal(doubleMeasurement);

                processDoubleMeasurements(viewId, doubleMeasurement);
            }
        } else {
            if (measurementHasChangedInteger(viewId, integerMeasurement)) {
                if (viewId == R.id.pack_editable_measurement_one)
                    editedMeasurementModel.setPackMeasurementOneAsInt(integerMeasurement);

                if (viewId == R.id.item_editable_measurement_one)
                    editedMeasurementModel.setItemMeasurementOneAsInt(integerMeasurement);

                processIntegerMeasurements(viewId, integerMeasurement);
            }
        }
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

        if (measurementIsSet) updateMeasurementModel();
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one)
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);

        if (viewId == R.id.item_editable_measurement_one)
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);

        if (viewId == R.id.pack_editable_measurement_two)
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        else if (viewId == R.id.item_editable_measurement_two)
            measurementIsSet = unitOfMeasure.itemMeasurementTwoIsSet(newMeasurement);

        if (measurementIsSet) updateMeasurementModel();
    }

    // Synchronises the measurement model with the unit of measure
    private void updateMeasurementModel() {
        if (editedMeasurementModel.getMeasurementSubType() !=
                unitOfMeasure.getMeasurementSubType())
            editedMeasurementModel.setMeasurementSubType(
                    unitOfMeasure.getMeasurementSubType());

        if (editedMeasurementModel.getNumberOfItems() !=
                unitOfMeasure.getNumberOfItems())
            editedMeasurementModel.setNumberOfItems(
                    unitOfMeasure.getNumberOfItems());

        if (unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_MASS ||
                unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_VOLUME) {

            if (editedMeasurementModel.getPackMeasurementOneAsDecimal() !=
                    unitOfMeasure.getPackMeasurementOne())
                editedMeasurementModel.setPackMeasurementOneAsDecimal(
                        unitOfMeasure.getPackMeasurementOne());

            if (editedMeasurementModel.getItemMeasurementOneAsDecimal() !=
                    unitOfMeasure.getItemMeasurementOne())
                editedMeasurementModel.setItemMeasurementOneAsDecimal(
                        unitOfMeasure.getItemMeasurementOne());

        } else {
            if (editedMeasurementModel.getPackMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getPackMeasurementOne())
                editedMeasurementModel.setPackMeasurementOneAsInt(
                        (int) unitOfMeasure.getPackMeasurementOne());

            if (editedMeasurementModel.getItemMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getItemMeasurementOne())
                editedMeasurementModel.setItemMeasurementOneAsInt(
                        (int) unitOfMeasure.getItemMeasurementOne());
        }

        if (numberOfMeasurementUnits > 1) {
            if (editedMeasurementModel.getPackMeasurementTwo() !=
                    unitOfMeasure.getPackMeasurementTwo())
                editedMeasurementModel.setPackMeasurementTwo(
                        unitOfMeasure.getPackMeasurementTwo());

            if (editedMeasurementModel.getItemMeasurementTwo() !=
                    unitOfMeasure.getItemMeasurementTwo())
                editedMeasurementModel.setItemMeasurementTwo(
                        unitOfMeasure.getItemMeasurementTwo());
        }
        Log.d(TAG, "updateMeasurementModel: Measurement model updating complete: " +
                editedMeasurementModel.toString());
        saveToExistingModelIfValid();
    }

    private void saveToExistingModelIfValid() {
        Log.d(TAG, "saveToExistingModelIfValid: " + unitOfMeasure.isValidMeasurement());
//        if (unitOfMeasure.isValidMeasurement())
//            existingMeasurementModel.setValue(editedMeasurementModel);
    }
}