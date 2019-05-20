package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.app.Application;
import android.util.Log;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ProductMeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.viewmodels.ObservableViewModel;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

public class ProductMeasurementViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-MeasurementVM";

    private MutableLiveData<ProductMeasurementModel> existingMeasurementModel =
            new MutableLiveData<>();

    // Whenever a editedMeasurementModel is valid, set it to measurement model
    private ProductMeasurementModel editedMeasurementModel;
    private ProductMeasurementHandler measurementHandler;
    private UnitOfMeasure unitOfMeasure;

    public ProductMeasurementViewModel(@NonNull Application application) {
        super(application);

        editedMeasurementModel = new ProductMeasurementModel();

        measurementHandler = new ProductMeasurementHandler(
                application,
                this);

        unitOfMeasure = MeasurementSubType.TYPE_METRIC_MASS.getMeasurementClass();
        updateMeasurementModel();
    }

    MutableLiveData<ProductMeasurementModel> getExistingMeasurementModel() {
        return existingMeasurementModel;
    }

    ProductMeasurementHandler getMeasurementHandler() {
        return measurementHandler;
    }

    ProductMeasurementModel getEditedMeasurementModel() {
        return editedMeasurementModel;
    }

    void newUnitOfMeasureSelected(int subTypeAsInt) {
        if (unitOfMeasure.getMeasurementSubType().ordinal() != subTypeAsInt) {
            unitOfMeasure = MeasurementSubType.values()[subTypeAsInt].getMeasurementClass();
            updateMeasurementModel();
        }
    }

    boolean numberOfItemsChanged(int newNumberOfItems) {
        if (numberOfItemsHasChanged(newNumberOfItems)) {
            Log.d(TAG, "numberOfItemsChanged: Value has changed to: " +
                    newNumberOfItems + " Setting new value");

            if (numberOfItemsAreSet(newNumberOfItems)) {
                Log.d(TAG, "numberOfItemsChanged: New value set");
                return true;
            } else Log.d(TAG, "numberOfItemsChanged: Unit of measure refused new number of items");
        } else Log.d(TAG, "numberOfItemsChanged: Number of items has not changed");
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
                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChangedDouble: Pack One:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);
                return oldMeasurement != newMeasurement;

            case R.id.item_editable_measurement_one:
                oldMeasurement = unitOfMeasure.getItemMeasurementOne();
                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChangedDouble: Item One:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);
                return oldMeasurement != newMeasurement;
        }
        return false;
    }

    private boolean measurementHasChangedInteger(int viewId, int newMeasurement) {
        int oldMeasurement;

        switch (viewId) {
            case R.id.pack_editable_measurement_one:
                oldMeasurement = (int) unitOfMeasure.getPackMeasurementOne();
                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChangedInteger: Pack One:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);
                return oldMeasurement != newMeasurement;

            case R.id.item_editable_measurement_one:
                oldMeasurement = (int) unitOfMeasure.getItemMeasurementOne();
                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChangedInteger: Item One:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);
                return oldMeasurement != newMeasurement;

            case R.id.pack_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getPackMeasurementTwo();
                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChangedInteger: Pack Two:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);
                return newMeasurement != oldMeasurement;

            case R.id.item_editable_measurement_two:
                oldMeasurement = unitOfMeasure.getItemMeasurementTwo();
                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChangedInteger: Item Two:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);
                return newMeasurement != oldMeasurement;
        }
        Log.d(TAG, "measurementHasChangedInteger: View not recognised, aborting.");
        return false;
    }

    private void processDoubleMeasurements(int viewId, double newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one) {
            Log.d(TAG, "processDoubleMeasurements: Processing change to Pack One");
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);
        }

        if (viewId == R.id.item_editable_measurement_one) {
            Log.d(TAG, "processDoubleMeasurements: Processing change to Item One");
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);
        }

        if (measurementIsSet) {
            Log.d(TAG, "processDoubleMeasurements: Measurement is set!");
            updateMeasurementModel();
        } else {
            Log.d(TAG, "processDoubleMeasurements: measurement is out of bounds");
            updateMeasurementModel();
        }
    }

    private void processIntegerMeasurements(int viewId, int newMeasurement) {
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one){
            Log.d(TAG, "processIntegerMeasurements: Processing change to Pack One");
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);
        }

        if (viewId == R.id.item_editable_measurement_one) {
            Log.d(TAG, "processIntegerMeasurements: Processing change to Item One");
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);
        }

        if (viewId == R.id.pack_editable_measurement_two) {
            Log.d(TAG, "processIntegerMeasurements: processing change to Pack Two");
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        } else if (viewId == R.id.item_editable_measurement_two) {
            Log.d(TAG, "processIntegerMeasurements: processing change to Item Two");
            measurementIsSet = unitOfMeasure.itemMeasurementTwoIsSet(newMeasurement);
        }

        if (measurementIsSet) {
            Log.d(TAG, "processIntegerMeasurements: Measurement is set!");
            updateMeasurementModel();
        } else {
            Log.d(TAG, "processIntegerMeasurements: measurement is out of bounds");
            updateMeasurementModel();
        }
    }

    // Synchronises the measurement model with the unit of measure
    private void updateMeasurementModel() {

        Log.d(TAG, "updateMeasurementModel: Updating measurement model");
        Log.d(TAG, "updateMeasurementModel: Base units are: " + unitOfMeasure.getBaseSiUnits());

        if (editedMeasurementModel.getMeasurementSubType() != unitOfMeasure.getMeasurementSubType()) {
            Log.d(TAG, "updateMeasurementModel: Updating measurement Subtype to: " +
                    unitOfMeasure.getMeasurementSubType());

            editedMeasurementModel.setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
        }

        if (editedMeasurementModel.getNumberOfMeasurementUnits() !=
                unitOfMeasure.getNumberOfMeasurementUnits()) {
            Log.d(TAG, "updateMeasurementModel: Updating number of measurement units to: " +
                    unitOfMeasure.getNumberOfMeasurementUnits());

            editedMeasurementModel.setNumberOfMeasurementUnits(
                    unitOfMeasure.getNumberOfMeasurementUnits());
        }

        if (editedMeasurementModel.getNumberOfItems() != unitOfMeasure.getNumberOfItems()) {
            Log.d(TAG, "updateMeasurementModel: Updating number of Items to: " +
                    unitOfMeasure.getNumberOfItems());

            editedMeasurementModel.setNumberOfItems(unitOfMeasure.getNumberOfItems());
        }

        if (unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_MASS ||
                unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_VOLUME) {

            if (editedMeasurementModel.getPackMeasurementOneAsDecimal() !=
                    unitOfMeasure.getPackMeasurementOne()) {
                Log.d(TAG, "updateMeasurementModel: Updating pack One DECIMAL to: " +
                        unitOfMeasure.getPackMeasurementOne());

                editedMeasurementModel.setPackMeasurementOneAsDecimal(
                        unitOfMeasure.getPackMeasurementOne());
            }

            if (editedMeasurementModel.getItemMeasurementOneAsDecimal() !=
                    unitOfMeasure.getItemMeasurementOne()) {
                editedMeasurementModel.setItemMeasurementOneAsDecimal(
                        unitOfMeasure.getItemMeasurementOne());

                Log.d(TAG, "updateMeasurementModel: Updating Item One DECIMAL to: " +
                        unitOfMeasure.getItemMeasurementOne());
            }

        } else {

            if (editedMeasurementModel.getPackMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getPackMeasurementOne()) {
                Log.d(TAG, "updateMeasurementModel: Updating Pack One as INTEGER to: " +
                        (int) unitOfMeasure.getPackMeasurementOne());

                editedMeasurementModel.setPackMeasurementOneAsInt(
                        (int) unitOfMeasure.getPackMeasurementOne());
            }

            if (editedMeasurementModel.getItemMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getItemMeasurementOne()) {
                Log.d(TAG, "updateMeasurementModel: Updating Item One as INTEGER to: " +
                        (int) unitOfMeasure.getItemMeasurementOne());

                editedMeasurementModel.setItemMeasurementOneAsInt(
                        (int) unitOfMeasure.getItemMeasurementOne());
            }
        }

        if (editedMeasurementModel.getPackMeasurementTwo() !=
                unitOfMeasure.getPackMeasurementTwo()) {
            editedMeasurementModel.setPackMeasurementTwo(unitOfMeasure.getPackMeasurementTwo());

            Log.d(TAG, "updateMeasurementModel: Updating pack Two to: " +
                    unitOfMeasure.getPackMeasurementTwo());
        }
        Log.d(TAG, "updateMeasurementModel: Measurement - Item two is: " +
                editedMeasurementModel.getItemMeasurementTwo() +
                " Unit of measure Item Two is: " + unitOfMeasure.getItemMeasurementTwo());

        if (editedMeasurementModel.getItemMeasurementTwo() !=
                unitOfMeasure.getItemMeasurementTwo()) {

            editedMeasurementModel.setItemMeasurementTwo(
                    unitOfMeasure.getItemMeasurementTwo());

            Log.d(TAG, "updateMeasurementModel: Updating Item Two to: " +
                    unitOfMeasure.getItemMeasurementTwo());
        }

        existingMeasurementModel.setValue(editedMeasurementModel);
        Log.d(TAG, "updateMeasurementModel: Measurement model updating complete");
    }
}