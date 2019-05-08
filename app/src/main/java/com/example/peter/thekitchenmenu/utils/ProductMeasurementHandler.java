package com.example.peter.thekitchenmenu.utils;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.viewmodels.ProductMeasurementViewModel;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;

public class ProductMeasurementHandler {

    private static final String TAG = "ProductMeasurementHandl";

    private ProductMeasurementViewModel viewModel;
    private Resources resources;

    private UnitOfMeasure unitOfMeasure;

    private static final int MEASUREMENT_ERROR = -1;

    public ProductMeasurementHandler(Application applicationContext,
                                     ProductMeasurementViewModel viewModel) {

        this.viewModel = viewModel;
        resources = applicationContext.getResources();

        // Default unit of measure
        unitOfMeasure = MeasurementSubType.TYPE_METRIC_MASS.getMeasurementClass();

        updateMeasurementModel();
    }

    public void newUnitOfMeasureSelected(Spinner spinnerWithSubType) {

        changeUnitOfMeasure(spinnerWithSubType.getSelectedItemPosition());
    }

    public void changeUnitOfMeasure(int subTypeAsInt) {

        if (unitOfMeasure.getMeasurementSubType().ordinal() != subTypeAsInt) {

            unitOfMeasure = MeasurementSubType.values()[subTypeAsInt].getMeasurementClass();
            updateMeasurementModel();
        }
    }

    public void numberOfItems(TextView editableItemsInPack) {

        int newNumberOfItems = parseIntegerFromEditText(editableItemsInPack);

        if (newNumberOfItems == 0 || newNumberOfItems == MEASUREMENT_ERROR) return;

        numberOfItemsUpdated(newNumberOfItems);
    }

    public boolean numberOfItemsUpdated(int newNumberOfItems) {

        if (numberOfItemsHasChanged(newNumberOfItems)) {

            Log.d(TAG, "tkm - numberOfItemsUpdated: Value has changed to: " +
                    newNumberOfItems + " Setting new value");

            if (numberOfItemsAreSet(newNumberOfItems)) {

                Log.d(TAG, "tkm - numberOfItemsUpdated: New value set");
                return true;

            } else Log.d(TAG, "tkm - numberOfItemsUpdated: Unit of measure refused new number of items");

        } else Log.d(TAG, "tkm - numberOfItemsUpdated: Number of items has not changed");

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

    private void setNumberOfItemsOutOfBoundsError(TextView editableItemsInPack) {

        String numberOfItemsError =
                resources.getString(
                        R.string.input_error_items_in_pack,
                        MULTI_PACK_MINIMUM_NO_OF_ITEMS,
                        MULTI_PACK_MAXIMUM_NO_OF_ITEMS);

//        editableItemsInPack.setError(numberOfItemsError);
    }

    public void modifyNumberOfItemsByOne(TextView editableNoOfItems, Button button) {

        int itemsInPack = unitOfMeasure.getNumberOfItems();
        int buttonId = button.getId();

        if (buttonId == R.id.multi_pack_plus)
            if (!numberOfItemsUpdated((itemsInPack + 1)))
                setNumberOfItemsOutOfBoundsError(editableNoOfItems);


        if (buttonId == R.id.multi_pack_minus)
            if (!numberOfItemsUpdated((itemsInPack - 1)))
                setNumberOfItemsOutOfBoundsError(editableNoOfItems);
    }

    public void validatePackSize(EditText editableMeasurement, MeasurementSubType subType) {

        int viewId = editableMeasurement.getId();
        double doubleMeasurement;
        int integerMeasurement;
        int numberOfUnitsAfterDecimal;

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one) {

            Pair[] inputDigitsFilters = unitOfMeasure.getInputDigitsFilter();
            numberOfUnitsAfterDecimal = (int) inputDigitsFilters[0].second;

        } else numberOfUnitsAfterDecimal = 0;

        if (numberOfUnitsAfterDecimal > 0) {

            doubleMeasurement = parseDoubleFromEditText(editableMeasurement);

            if (doubleMeasurement == MEASUREMENT_ERROR) return;

            if (measurementHasChangedDouble(viewId, doubleMeasurement)) {

                if (viewId == R.id.pack_editable_measurement_one)
                    viewModel.getNewMeasurement().setPackMeasurementOneAsDecimal(doubleMeasurement);

                if (viewId == R.id.item_editable_measurement_one)
                    viewModel.getNewMeasurement().setItemMeasurementOneAsDecimal(doubleMeasurement);

                processDoubleMeasurements(editableMeasurement, doubleMeasurement);
            }

        } else {

            integerMeasurement = parseIntegerFromEditText(editableMeasurement);

            if (integerMeasurement == MEASUREMENT_ERROR) return;

            if (measurementHasChangedInteger(viewId, integerMeasurement)) {

                if (viewId == R.id.pack_editable_measurement_one)
                    viewModel.getNewMeasurement().setPackMeasurementOneAsInt(integerMeasurement);

                if (viewId == R.id.item_editable_measurement_one)
                    viewModel.getNewMeasurement().setItemMeasurementOneAsInt(integerMeasurement);

                processIntegerMeasurements(editableMeasurement, integerMeasurement);
            }
        }
    }

    private double parseDoubleFromEditText(EditText editableMeasurement) {

        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty() || rawMeasurement.equals(".")) return 0.;

        try {

            return Double.parseDouble(rawMeasurement);

        } catch (NumberFormatException e) {

            setNumberFormatExceptionError(editableMeasurement);
            return MEASUREMENT_ERROR;
        }
    }

    private int parseIntegerFromEditText(TextView editableMeasurement) {

        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty()) return 0;

        try {

            return Integer.parseInt(rawMeasurement);

        } catch (NumberFormatException e) {

            setNumberFormatExceptionError(editableMeasurement);
            return MEASUREMENT_ERROR;
        }
    }

    private boolean measurementHasChangedDouble(int viewId, double newMeasurement) {

        double oldMeasurement;

        switch (viewId) {

            case R.id.pack_editable_measurement_one:

                oldMeasurement = unitOfMeasure.getPackMeasurementOne();

                if (oldMeasurement != newMeasurement)

                    Log.d(TAG, "tkm - measurementHasChangedDouble: Pack One:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);


                return oldMeasurement != newMeasurement;

            case R.id.item_editable_measurement_one:

                oldMeasurement = unitOfMeasure.getItemMeasurementOne();

                if (oldMeasurement != newMeasurement)

                    Log.d(TAG, "tkm - measurementHasChangedDouble: Item One:" +
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

                    Log.d(TAG, "tkm - measurementHasChangedInteger: Pack Two:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);

                return newMeasurement != oldMeasurement;

            case R.id.item_editable_measurement_two:

                oldMeasurement = unitOfMeasure.getItemMeasurementTwo();

                if (oldMeasurement != newMeasurement)

                    Log.d(TAG, "tkm - measurementHasChangedInteger: Item Two:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);

                return newMeasurement != oldMeasurement;
        }
        Log.d(TAG, "tkm - measurementHasChangedInteger: View not recognised, aborting.");
        return false;
    }

    private void processDoubleMeasurements(EditText editableMeasurement, double newMeasurement) {

        int viewId = editableMeasurement.getId();
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one) {

            Log.d(TAG, "tkm - processDoubleMeasurements: Processing change to Pack One");
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);
        }

        if (viewId == R.id.item_editable_measurement_one) {

            Log.d(TAG, "tkm - processDoubleMeasurements: Processing change to Item One");
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);
        }

        if (measurementIsSet) {

            Log.d(TAG, "tkm - processDoubleMeasurements: Measurement is set!");
            updateMeasurementModel();

        } else {
            Log.d(TAG, "tkm - processDoubleMeasurements: measurement is out of bounds");
            setMeasurementOutOfBoundsError(editableMeasurement);
            updateMeasurementModel();
        }
    }

    private void processIntegerMeasurements(EditText editableMeasurement, int newMeasurement) {

        int viewId = editableMeasurement.getId();

        Log.d(TAG, "tkm - processIntegerMeasurements: New measurement received from: " +
                resources.getResourceEntryName(editableMeasurement.getId()) + " measurement is: "
                + newMeasurement);

        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one){

            Log.d(TAG, "tkm - processIntegerMeasurements: Processing change to Pack One");
            measurementIsSet = unitOfMeasure.packMeasurementOneIsSet(newMeasurement);
        }

        if (viewId == R.id.item_editable_measurement_one) {

            Log.d(TAG, "tkm -processIntegerMeasurements: Processing change to Item One");
            measurementIsSet = unitOfMeasure.itemMeasurementOneIsSet(newMeasurement);
        }

        if (viewId == R.id.pack_editable_measurement_two) {

            Log.d(TAG, "tkm - processIntegerMeasurements: processing change to Pack Two");
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        } else if (viewId == R.id.item_editable_measurement_two) {

            Log.d(TAG, "tkm - processIntegerMeasurements: processing change to Item Two");
            measurementIsSet = unitOfMeasure.itemMeasurementTwoIsSet(newMeasurement);
        }

        if (measurementIsSet) {
            Log.d(TAG, "tkm - processIntegerMeasurements: Measurement is set!");
            updateMeasurementModel();

        } else {

            Log.d(TAG, "tkm - processIntegerMeasurements: measurement is out of bounds");
            setMeasurementOutOfBoundsError(editableMeasurement);
            updateMeasurementModel();
        }

    }

    private void setMeasurementOutOfBoundsError(EditText editableMeasurement) {

        Log.d(TAG, "tkm - setMeasurementOutOfBoundsError: Pack size out of bounds error");

//        setErrorTo(editableMeasurement);
    }

    private void setErrorTo(EditText editableMeasurement) {

        // TODO - Add quantity strings (plurals)
        // https://developer.android.com/guide/topics/resources/string-resource.html#Plurals
        // TODO - If measurement units contain zero values do not show them
        // TODO - Have different errors for 1 to 3 measurement values

        int[] measurementError = unitOfMeasure.getMeasurementError();

        editableMeasurement.setError(

                resources.getString(

                        R.string.input_error_pack_size,
                        resources.getString(measurementError[0]),
                        String.valueOf(measurementError[1]),
                        resources.getString(measurementError[2]),
                        String.valueOf(measurementError[3]),
                        resources.getString(measurementError[4])
                ));

        Log.d(TAG, "tkm - setErrorTo: Error should be set.");
    }

    private void setNumberFormatExceptionError(TextView editable) {

        editable.setError(resources.getString(R.string.number_format_exception));
    }

    public boolean setBaseSiUnits(double baseSi) {

        Log.d(TAG, "tkm - setBaseSiUnits: New base units received: " + baseSi);

        if (baseSiUnitsAreSet(baseSi)) {

            Log.d(TAG, "tkm - setBaseSiUnits: New base units have been set!");

            updateMeasurementModel();
            return true;
        } else Log.d(TAG, "tkm - setBaseSiUnits: Base units have been refused");

        return false;
    }

    private boolean baseSiUnitsAreSet(double baseSi) {

        return unitOfMeasure.baseSiUnitsAreSet(baseSi);
    }

    // Synchronises the measurement model with the unit of measure
    private void updateMeasurementModel() {

        Log.d(TAG, "tkm - updateMeasurementModel: Updating measurement model");
        Log.d(TAG, "tkm - updateMeasurementModel: Base units are: " + unitOfMeasure.getBaseSiUnits());

        if (viewModel.getNewMeasurement().getMeasurementSubType() !=
                unitOfMeasure.getMeasurementSubType()) {

            Log.d(TAG, "tkm - updateMeasurementModel: Updating measurement Subtype to: " +
                    unitOfMeasure.getMeasurementSubType());

            viewModel.getNewMeasurement().setMeasurementSubType(
                    unitOfMeasure.getMeasurementSubType());
        }

        if (viewModel.getNewMeasurement().getNumberOfMeasurementUnits() !=
                unitOfMeasure.getNumberOfMeasurementUnits()) {

            Log.d(TAG, "tkm - updateMeasurementModel: Updating number of measurement units to: " +
                    unitOfMeasure.getNumberOfMeasurementUnits());

            viewModel.getNewMeasurement().setNumberOfMeasurementUnits(
                    unitOfMeasure.getNumberOfMeasurementUnits());
        }

        if (viewModel.getNewMeasurement().getNumberOfItems() !=
                unitOfMeasure.getNumberOfItems()) {

            Log.d(TAG, "tkm - updateMeasurementModel: Updating number of Items to: " +
                    unitOfMeasure.getNumberOfItems());

            viewModel.getNewMeasurement().setNumberOfItems(
                    unitOfMeasure.getNumberOfItems());
        }

        if (unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_MASS ||
                unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_VOLUME) {

            if (viewModel.getNewMeasurement().getPackMeasurementOneAsDecimal() !=
                    unitOfMeasure.getPackMeasurementOne()) {

                Log.d(TAG, "tkm - updateMeasurementModel: Updating pack One DECIMAL to: " +
                        unitOfMeasure.getPackMeasurementOne());

                viewModel.getNewMeasurement().setPackMeasurementOneAsDecimal(
                        unitOfMeasure.getPackMeasurementOne());
            }

            if (viewModel.getNewMeasurement().getItemMeasurementOneAsDecimal() !=
                    unitOfMeasure.getItemMeasurementOne()) {

                viewModel.getNewMeasurement().setItemMeasurementOneAsDecimal(
                        unitOfMeasure.getItemMeasurementOne());

                Log.d(TAG, "tkm - updateMeasurementModel: Updating Item One DECIMAL to: " +
                        unitOfMeasure.getItemMeasurementOne());
            }

        } else {

            if (viewModel.getNewMeasurement().getPackMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getPackMeasurementOne()) {

                Log.d(TAG, "tkm - updateMeasurementModel: Updating Pack One as INTEGER to: " +
                        (int) unitOfMeasure.getPackMeasurementOne());

                viewModel.getNewMeasurement().setPackMeasurementOneAsInt(
                        (int) unitOfMeasure.getPackMeasurementOne());
            }


            if (viewModel.getNewMeasurement().getItemMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getItemMeasurementOne()) {

                Log.d(TAG, "tkm - updateMeasurementModel: Updating Item One as INTEGER to: " +
                        (int) unitOfMeasure.getItemMeasurementOne());

                viewModel.getNewMeasurement().setItemMeasurementOneAsInt(
                        (int) unitOfMeasure.getItemMeasurementOne());
            }
        }

        if (viewModel.getNewMeasurement().getPackMeasurementTwo() !=
                unitOfMeasure.getPackMeasurementTwo()) {

            viewModel.getNewMeasurement().setPackMeasurementTwo(
                    unitOfMeasure.getPackMeasurementTwo());

            Log.d(TAG, "tkm - updateMeasurementModel: Updating pack Two to: " +
                    unitOfMeasure.getPackMeasurementTwo());
        }


        Log.d(TAG, "tkm - updateMeasurementModel: Measurement - Item two is: " +
                viewModel.getNewMeasurement().getItemMeasurementTwo() +
                " Unit of measure Item Two is: " + unitOfMeasure.getItemMeasurementTwo());

        if (viewModel.getNewMeasurement().getItemMeasurementTwo() !=
                unitOfMeasure.getItemMeasurementTwo()) {

            viewModel.getNewMeasurement().setItemMeasurementTwo(
                    unitOfMeasure.getItemMeasurementTwo());

            Log.d(TAG, "tkm - updateMeasurementModel: Updating Item Two to: " +
                    unitOfMeasure.getItemMeasurementTwo());
        }

        Log.d(TAG, "tkm - updateMeasurementModel: Measurement model updating complete");
    }
}