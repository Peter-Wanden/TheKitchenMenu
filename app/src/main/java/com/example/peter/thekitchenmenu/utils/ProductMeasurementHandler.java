package com.example.peter.thekitchenmenu.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureSubtypeSelector;
import com.example.peter.thekitchenmenu.viewmodels.ProductMeasurementViewModel;

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;

public class ProductMeasurementHandler {

    private static final String TAG = "ProductMeasurementHandl";

    private Context applicationContext;
    private ProductMeasurementViewModel viewModel;
    private Resources resources;

    private UnitOfMeasure unitOfMeasure;

    private static final int MEASUREMENT_ERROR = -1;

    public ProductMeasurementHandler(Application applicationContext,
                                     ProductMeasurementViewModel viewModel) {

        this.applicationContext = applicationContext;
        this.viewModel = viewModel;
        resources = applicationContext.getResources();

        // Default unit of measure
        unitOfMeasure = UnitOfMeasureSubtypeSelector.getClassWithSubType(applicationContext,
                MeasurementSubType.TYPE_METRIC_MASS);

        updateMeasurementModel();
    }

    public void newUnitOfMeasureSelected(Spinner spinnerWithSubType) {

        changeUnitOfMeasure(spinnerWithSubType.getSelectedItemPosition());
    }

    public void changeUnitOfMeasure(int unitOfMeasureSubTypeAsInt) {

        if (unitOfMeasure.getMeasurementSubType().ordinal() != unitOfMeasureSubTypeAsInt) {

            Log.d(TAG, "changeUnitOfMeasure: unit of measure is different from " +
                    "the one i have. Changing it now.");

            unitOfMeasure = UnitOfMeasureSubtypeSelector.getClassWithSubType(
                    applicationContext, MeasurementSubType.values()
                            [unitOfMeasureSubTypeAsInt]);

            updateMeasurementModel();

        } else Log.d(TAG, "changeUnitOfMeasure: Unit of measure has not changed!");
    }

    public void numberOfItems(EditText editableItemsInPack) {

        int newNumberOfItems = parseIntegerFromEditText(editableItemsInPack);

        if (newNumberOfItems == 0 || newNumberOfItems == MEASUREMENT_ERROR) return;

        numberOfItemsUpdated(newNumberOfItems);
    }

    public boolean numberOfItemsUpdated(int newNumberOfItems) {

        if (numberOfItemsHasChanged(newNumberOfItems)) {

            Log.d(TAG, "numberOfItemsUpdated: Value has changed to: " +
                    newNumberOfItems + " Setting new value");

            if (numberOfItemsAreSet(newNumberOfItems)) {

                Log.d(TAG, "numberOfItemsUpdated: New value set");
                return true;

            } else Log.d(TAG, "numberOfItemsUpdated: Unit of measure refused new number of items");

        } else Log.d(TAG, "numberOfItemsUpdated: Number of items has not changed");

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

    private void setNumberOfItemsOutOfBoundsError(EditText editableItemsInPack) {

        String numberOfItemsError =
                resources.getString(
                        R.string.input_error_items_in_pack,
                        MULTI_PACK_MINIMUM_NO_OF_ITEMS,
                        MULTI_PACK_MAXIMUM_NO_OF_ITEMS);

        editableItemsInPack.setError(numberOfItemsError);
    }

    public void modifyNumberOfItemsByOne(EditText editableNoOfItems, Button button) {

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

        Context context = editableMeasurement.getContext();
        UnitOfMeasure unitOfMeasure = UnitOfMeasureSubtypeSelector.
                getClassWithSubType(context, subType);

        Pair[] inputDigitsFilters = unitOfMeasure.getInputDigitsFilter();

        int viewId = editableMeasurement.getId();
        double doubleMeasurement;
        int integerMeasurement;

        Log.d(TAG, "validatePackSize: Measurement received from: " +
                resources.getResourceEntryName(viewId) +
                " Raw value received is: " + editableMeasurement.getText().toString());


        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one) {

            // TODO - Check the input filters to see if the number input should be a double or an integer
            int numberOfUnitsAfterDecimal = (int) inputDigitsFilters[0].second;

            doubleMeasurement = parseDoubleFromEditText(editableMeasurement);

            if (doubleMeasurement == MEASUREMENT_ERROR) return;

            if (measurementHasChangedDouble(viewId, doubleMeasurement))
                processDoubleMeasurements(editableMeasurement, doubleMeasurement);

        } else {

            integerMeasurement = parseIntegerFromEditText(editableMeasurement);

            if (integerMeasurement == MEASUREMENT_ERROR) return;

            if (measurementHasChangedInteger(viewId, integerMeasurement))
                processIntegerMeasurements(editableMeasurement, integerMeasurement);
        }
    }

    private double parseDoubleFromEditText(EditText editableMeasurement) {

        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty()) return 0.;

        try {

            return Double.parseDouble(rawMeasurement);

        } catch (NumberFormatException e) {

            setNumberFormatExceptionError(editableMeasurement);
            return MEASUREMENT_ERROR;
        }
    }

    private int parseIntegerFromEditText(EditText editableMeasurement) {

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

            case R.id.pack_editable_measurement_three:

                oldMeasurement = unitOfMeasure.getPackMeasurementThree();

                if (oldMeasurement != newMeasurement)

                    Log.d(TAG, "measurementHasChangedInteger: Pack Three:" +
                            " Old measurement: " + oldMeasurement +
                            " New measurement: " + newMeasurement);

                return newMeasurement != oldMeasurement;


            case R.id.item_editable_measurement_three:

                oldMeasurement = unitOfMeasure.getItemMeasurementThree();

                if (oldMeasurement != newMeasurement)

                    Log.d(TAG, "measurementHasChangedInteger: Item Three: " +
                            " Old Measurement: " + oldMeasurement +
                            " New Measurement: " + newMeasurement);

                return newMeasurement != oldMeasurement;
        }
        Log.d(TAG, "measurementHasChangedInteger: View not recognised, aborting.");
        return false;
    }

    private void processDoubleMeasurements(EditText editableMeasurement, double newMeasurement) {

        int viewId = editableMeasurement.getId();
        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one) {

            Log.d(TAG, "processDoubleMeasurements: Processing change to Pack One");
            measurementIsSet = unitOfMeasure.packMeasurementOneDecimalIsSet(newMeasurement);
        }

        if (viewId == R.id.item_editable_measurement_one) {

            Log.d(TAG, "processDoubleMeasurements: Processing change to Item One");
            measurementIsSet = unitOfMeasure.itemMeasurementOneDecimalIsSet(newMeasurement);
        }

        if (measurementIsSet) {

            Log.d(TAG, "processDoubleMeasurements: Measurement is set!");
            updateMeasurementModel();

        } else {
            Log.d(TAG, "processDoubleMeasurements: measurement is out of bounds");
            setMeasurementOutOfBoundsError(editableMeasurement);
        }
    }

    private void processIntegerMeasurements(EditText editableMeasurement, int newMeasurement) {

        // TODO - check for changes before processing measurements!!

        int viewId = editableMeasurement.getId();

        Log.d(TAG, "processIntegerMeasurements: New measurement received from: " +
                resources.getResourceEntryName(editableMeasurement.getId()) + " measurement is: "
                + newMeasurement);

        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_two) {

            Log.d(TAG, "processIntegerMeasurements: processing change to Pack Two");
            measurementIsSet = unitOfMeasure.packMeasurementTwoIsSet(newMeasurement);

        } else if (viewId == R.id.item_editable_measurement_two) {

            Log.d(TAG, "processIntegerMeasurements: processing change to Item Two");
            measurementIsSet = unitOfMeasure.itemMeasurementTwoIsSet(newMeasurement);

        } else if (viewId == R.id.pack_editable_measurement_three) {

            Log.d(TAG, "processIntegerMeasurements: processing change to Pack three");
            measurementIsSet = unitOfMeasure.packMeasurementThreeIsSet(newMeasurement);

        } else if (viewId == R.id.item_editable_measurement_three) {

            Log.d(TAG, "processIntegerMeasurements: ");
            measurementIsSet = unitOfMeasure.itemMeasurementThreeIsSet(newMeasurement);
        }

        if (measurementIsSet) {
            Log.d(TAG, "processIntegerMeasurements: Measurement is set!");
            updateMeasurementModel();

        } else {

            Log.d(TAG, "processIntegerMeasurements: measurement is out of bounds");
            setMeasurementOutOfBoundsError(editableMeasurement);
        }

    }

    private void setMeasurementOutOfBoundsError(EditText editableMeasurement) {

        Log.d(TAG, "setMeasurementOutOfBoundsError: Pack size out of bounds error");

        setErrorTo(editableMeasurement);
    }

    private void setErrorTo(EditText editableMeasurement) {

        // TODO - Add quantity strings (plurals)
        // https://developer.android.com/guide/topics/resources/string-resource.html#Plurals
        // TODO - Use plurals for this error
        // TODO - If measurement units contain zero values do not show them
        // TODO - Have different errors for 1 to 3 measurement values

        String[] measurementError = unitOfMeasure.getMeasurementError();

        editableMeasurement.setError(

                resources.getString(

                        R.string.input_error_pack_size,
                        measurementError[0],
                        measurementError[1],
                        measurementError[2],
                        measurementError[3],
                        measurementError[4]
                ));


        Log.d(TAG, "setErrorTo: Error should be set.");
    }

    private void setNumberFormatExceptionError(EditText editable) {

        editable.setError(resources.getString(R.string.number_format_exception));
    }

    public boolean setBaseSiUnits(double baseSi) {

        Log.d(TAG, "setBaseSiUnits: New base units received: " + baseSi);

        if (baseSiUnitsAreSet(baseSi)) {

            Log.d(TAG, "setBaseSiUnits: New base units have been set!");

            updateMeasurementModel();
            return true;
        } else Log.d(TAG, "setBaseSiUnits: Base units have been refused");

        return false;
    }

    private boolean baseSiUnitsAreSet(double baseSi) {

        return unitOfMeasure.baseSiUnitsAreSet(baseSi);
    }

    // Synchronises the measurement model with the unit of measure
    private void updateMeasurementModel() {

        Log.d(TAG, "updateMeasurementModel: Updating measurement model");

        if (viewModel.getMeasurement().getMeasurementSubType() !=
                unitOfMeasure.getMeasurementSubType()) {

            Log.d(TAG, "updateMeasurementModel: Updating measurement Subtype to: " +
                    unitOfMeasure.getMeasurementSubType());

            viewModel.getMeasurement().setMeasurementSubType(
                    unitOfMeasure.getMeasurementSubType());
        }

        if (viewModel.getMeasurement().getNumberOfMeasurementUnits() !=
                unitOfMeasure.getNumberOfMeasurementUnits()) {

            Log.d(TAG, "updateMeasurementModel: Updating number of measurement units to: " +
                    unitOfMeasure.getNumberOfMeasurementUnits());

            viewModel.getMeasurement().setNumberOfMeasurementUnits(
                    unitOfMeasure.getNumberOfMeasurementUnits());
        }

        if (viewModel.getMeasurement().getNumberOfItems() !=
                unitOfMeasure.getNumberOfItems()) {

            Log.d(TAG, "updateMeasurementModel: Updating number of Items to: " +
                    unitOfMeasure.getNumberOfItems());

            viewModel.getMeasurement().setNumberOfItems(
                    unitOfMeasure.getNumberOfItems());
        }


        if (unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_MASS ||
                unitOfMeasure.getMeasurementSubType() == MeasurementSubType.TYPE_IMPERIAL_VOLUME) {

            if (viewModel.getMeasurement().getPackMeasurementOneAsDecimal() !=
                    unitOfMeasure.getPackMeasurementOne()) {

                Log.d(TAG, "updateMeasurementModel: Updating pack One DECIMAL to: " +
                        unitOfMeasure.getPackMeasurementOne());

                viewModel.getMeasurement().setPackMeasurementOneAsDecimal(
                        unitOfMeasure.getPackMeasurementOne());
            }

            if (viewModel.getMeasurement().getItemMeasurementOneAsDecimal() !=
                    unitOfMeasure.getItemMeasurementOne()) {

                viewModel.getMeasurement().setItemMeasurementOneAsDecimal(
                        unitOfMeasure.getItemMeasurementOne());

                Log.d(TAG, "updateMeasurementModel: Updating Item One DECIMAL to: " +
                        unitOfMeasure.getItemMeasurementOne());
            }

        } else {

            if (viewModel.getMeasurement().getPackMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getPackMeasurementOne()) {

                Log.d(TAG, "updateMeasurementModel: Updating Pack One as INTEGER to: " +
                        (int) unitOfMeasure.getPackMeasurementOne());

                viewModel.getMeasurement().setPackMeasurementOneAsInt(
                        (int) unitOfMeasure.getPackMeasurementOne());
            }


            if (viewModel.getMeasurement().getItemMeasurementOneAsInt() !=
                    (int) unitOfMeasure.getItemMeasurementOne()) {

                Log.d(TAG, "updateMeasurementModel: Updating Item One as INTEGER to: " +
                        (int) unitOfMeasure.getItemMeasurementOne());

                viewModel.getMeasurement().setItemMeasurementOneAsInt(
                        (int) unitOfMeasure.getItemMeasurementOne());
            }

        }

        if (viewModel.getMeasurement().getPackMeasurementTwo() !=
                unitOfMeasure.getPackMeasurementTwo()) {

            viewModel.getMeasurement().setPackMeasurementTwo(
                    unitOfMeasure.getPackMeasurementTwo());

            Log.d(TAG, "updateMeasurementModel: Updating pack Two to: " +
                    unitOfMeasure.getPackMeasurementTwo());
        }

        if (viewModel.getMeasurement().getItemMeasurementTwo() !=
                unitOfMeasure.getItemMeasurementTwo()) {

            viewModel.getMeasurement().setItemMeasurementTwo(
                    unitOfMeasure.getItemMeasurementTwo());

            Log.d(TAG, "updateMeasurementModel: Updating Item Two to: " +
                    unitOfMeasure.getItemMeasurementTwo());
        }

        if (viewModel.getMeasurement().getPackMeasurementThree() !=
                unitOfMeasure.getPackMeasurementThree()) {

            viewModel.getMeasurement().setPackMeasurementThree(
                    unitOfMeasure.getPackMeasurementThree());

            Log.d(TAG, "updateMeasurementModel: Updating Pack Three to: " +
                    unitOfMeasure.getPackMeasurementThree());
        }

        if (viewModel.getMeasurement().getItemMeasurementThree() !=
                unitOfMeasure.getItemMeasurementThree()) {

            viewModel.getMeasurement().setItemMeasurementThree(
                    unitOfMeasure.getItemMeasurementThree());

            Log.d(TAG, "updateMeasurementModel: Updating item Three to: " +
                    unitOfMeasure.getItemMeasurementThree());
        }

        Log.d(TAG, "updateMeasurementModel: Measurement model updating complete");
    }
}