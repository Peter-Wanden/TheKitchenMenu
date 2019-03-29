package com.example.peter.thekitchenmenu.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureClassSelector;
import com.example.peter.thekitchenmenu.viewmodels.ProductSizeViewModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.SINGLE_ITEM;

public class ProductSizeValidationHandler {

    private static final String TAG = "ProductSizeValidationHa";

    private Context applicationContext;
    private ProductSizeViewModel viewModel;
    private Resources resources;

    private UnitOfMeasure unitOfMeasure;
    private UnitOfMeasure newUnitOfMeasure;

    private static final int MEASUREMENT_ERROR = -1;

    public ProductSizeValidationHandler(Application applicationContext,
                                        ProductSizeViewModel viewModel) {

        this.applicationContext = applicationContext;
        this.viewModel = viewModel;
        resources = applicationContext.getResources();

        unitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(applicationContext,
                MeasurementSubType.TYPE_METRIC_MASS);
    }

    public void newUnitOfMeasureSelected(Spinner spinnerWithSubType) {

        // Check for change
        if (unitOfMeasure.getMeasurementSubType().ordinal() ==
                spinnerWithSubType.getSelectedItemPosition()) {
            Log.d(TAG, "newUnitOfMeasureSelected: Unit of measure is the same, aborting");

            return;
        }

        boolean newUnitOfMeasureIsSet = setNewUnitOfMeasure(
                MeasurementSubType.values()[spinnerWithSubType.getSelectedItemPosition()]
        );

        if (newUnitOfMeasureIsSet) {

            Log.d(TAG, "newUnitOfMeasureSelected: old unit of measure is: " +
                    unitOfMeasure.getMeasurementSubType() + " new unit of measure is: " +
                    newUnitOfMeasure.getMeasurementSubType().getMeasurementType());

            Log.d(TAG, "newUnitOfMeasureSelected: updating Measurement with new number of units");
            viewModel.getMeasurement().setNumberOfMeasurementUnits(
                    newUnitOfMeasure.getNumberOfMeasurementUnits());

            // Transfer old number of items to new unit of measure
            if (unitOfMeasure.getNumberOfItems() > MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

                newUnitOfMeasure.setNumberOfItems(unitOfMeasure.getNumberOfItems());
                Log.d(TAG, "newUnitOfMeasureSelected: transferring number of items");
            }

            // If the view model has a unit of measure that has some base units in it there may be a
            // measurement to convert
            if (viewModel.getMeasurement().getBaseSiUnits() > 0.) {

                Log.d(TAG, "newUnitOfMeasureSelected: old base units are: " +
                        viewModel.getMeasurement().getBaseSiUnits());

                Log.d(TAG, "newUnitOfMeasureSelected: old number of items are: " +
                        viewModel.getMeasurement().getNumberOfItems());

                newUnitOfMeasure.setNumberOfItems(viewModel.getMeasurement().getNumberOfItems());
                Log.d(TAG, "newUnitOfMeasureSelected: setting number of items to: " +
                        newUnitOfMeasure.getNumberOfItems());

                Log.d(TAG, "newUnitOfMeasureSelected: old and new subtypes are different.");

                // If the types are the same, there may be a// measurement to convert...
                if (unitOfMeasure.getMeasurementType() == newUnitOfMeasure.getMeasurementType()) {

                    Log.d(TAG, "newUnitOfMeasureSelected: old and new types are the same.");

                    // Set the base units to the new unit of measure
                    boolean baseSiUnitsAreSet =
                            newUnitOfMeasure.baseSiUnitsAreSet(unitOfMeasure.getBaseSiUnits());

                    Log.d(TAG, "newUnitOfMeasureSelected: base units are set: " +
                            baseSiUnitsAreSet);

                    // If the base units set ok...
                    if (baseSiUnitsAreSet) {

                        Log.d(TAG, "newUnitOfMeasureSelected: base units are set");

                        // Measurements have been successfully converted
                        updateUnitOfMeasure(newUnitOfMeasure);
                        updateMeasurementValues();
                        updateNumericValues();
                    }
                    // The types are the same, but the base units would not set. This can only
                    // happen if the numberOfItems has been set first and in setting the base units
                    // has made the pack size smaller than the smallest unit for this measure class.

                    // Reset values and maybe pass an error or toast reflecting the above?
                    updateUnitOfMeasure(newUnitOfMeasure);
                    updateMeasurementValues();
                    updateNumericValues();

                } else {

                    // The types are not the same, cannot convert
                    // Reset the measurement in the view model, update the unit of measure in the view
                    // model and update the measurement
                    Log.d(TAG, "newUnitOfMeasureSelected: old and new types are different, " +
                            "cannot convert measurement.");
                    updateUnitOfMeasure(newUnitOfMeasure);
                    updateMeasurementValues();
                    updateNumericValues();
                }

            } else {
                // TODO - Check to se if this auto updates (from an empty unit of measure to a new empty
                // unit of measure. Measurements are inconvertible between units of measure, or the
                // original unit of measure is empty. Reset the measurement in the view model, update
                // the unit of measure in the view model and update the measurement
                Log.d(TAG, "newUnitOfMeasureSelected: Old unit of measure has no base units");

                updateUnitOfMeasure(newUnitOfMeasure);
                updateMeasurementValues();
                updateNumericValues();
            }
        }
    }

    private void updateUnitOfMeasure(UnitOfMeasure newUnitOfMeasure) {

        unitOfMeasure = newUnitOfMeasure;
    }

    private void updateMeasurementValues() {

        viewModel.getMeasurement().setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
    }

    // Blocks infinite loops and updates the measurement and product model
    private void updateNumericValues() {

        if (viewModel.getMeasurement().getBaseSiUnits() != (int) unitOfMeasure.getBaseSiUnits()) {
            viewModel.getMeasurement().setBaseSiUnits((int) unitOfMeasure.getBaseSiUnits());
            Log.d(TAG, "updateNumericValues: updating basSiUnits to: " +
                    unitOfMeasure.getBaseSiUnits());
        }

        if (viewModel.getMeasurement().getNumberOfItems() !=
                unitOfMeasure.getNumberOfItems()) {

            viewModel.getMeasurement().setNumberOfItems(unitOfMeasure.getNumberOfItems());
            Log.d(TAG, "updateNumericValues: updating items in pack to: " +
                    unitOfMeasure.getNumberOfItems());
        }

        if (viewModel.getMeasurement().getPackMeasurementOne() !=
                unitOfMeasure.getPackMeasurementOne()) {

            viewModel.getMeasurement().setPackMeasurementOne(unitOfMeasure.getPackMeasurementOne());
            Log.d(TAG, "updateNumericValues: updating pack One to: " +
                    unitOfMeasure.getPackMeasurementOne());
        }

        if (viewModel.getMeasurement().getPackMeasurementTwo() !=
                unitOfMeasure.getPackMeasurementTwo()) {

            viewModel.getMeasurement().setPackMeasurementTwo(unitOfMeasure.getPackMeasurementTwo());
            Log.d(TAG, "updateNumericValues: updating pack Two to: " +
                    unitOfMeasure.getPackMeasurementTwo());
        }

        if (viewModel.getMeasurement().getItemMeasurementOne() !=
                unitOfMeasure.getItemMeasurementOne()) {

            viewModel.getMeasurement().setItemMeasurementOne(unitOfMeasure.getItemMeasurementOne());
            Log.d(TAG, "updateNumericValues: updating single pack One to: " +
                    unitOfMeasure.getItemMeasurementOne());
        }

        if (viewModel.getMeasurement().getItemMeasurementTwo() !=
                unitOfMeasure.getItemMeasurementTwo()) {

            viewModel.getMeasurement().setItemMeasurementTwo(unitOfMeasure.getItemMeasurementTwo());
            Log.d(TAG, "updateNumericValues: updating single pack Two to: " +
                    unitOfMeasure.getItemMeasurementTwo());
        }
    }

    public void validateItemsInPack(EditText editableItemsInPack) {

        int measurementViewId = editableItemsInPack.getId();
        int itemsInPack = parseIntegerFromEditText(editableItemsInPack);

        if (measurementHasChanged(measurementViewId, itemsInPack)) {

            Log.d(TAG, "validateItemsInPack: Value has changed");

            boolean itemsInPackAreSet = unitOfMeasure.setNumberOfItems(itemsInPack);

            if (itemsInPackAreSet) {

                updateNumericValues();

                Log.d(TAG, "validateItemsInPack: New items in pack has been set!");

            } else {

                Log.d(TAG, "setNumberOfItems: setting packs out of bounds error for some reason");
                setItemsInPackOutOfBoundsError(editableItemsInPack);
            }
        }
    }

    public void modifyItemsInPackByOne(EditText editableItemsInPack, Button button) {

        int itemsInPack = viewModel.getMeasurement().getNumberOfItems();
        int buttonId = button.getId();

        if (buttonId == R.id.multi_pack_plus &&
                itemsInPack < MULTI_PACK_MAXIMUM_NO_OF_ITEMS)
            editableItemsInPack.setText(String.valueOf(itemsInPack + 1));

        if (buttonId == R.id.multi_pack_minus &&
                itemsInPack > MULTI_PACK_MINIMUM_NO_OF_ITEMS)
            editableItemsInPack.setText(String.valueOf(itemsInPack - 1));
    }

    private void setItemsInPackOutOfBoundsError(EditText editableItemsInPack) {

        String itemsInPackError =
                resources.getString(
                        R.string.input_error_items_in_pack,
                        MULTI_PACK_MINIMUM_NO_OF_ITEMS,
                        MULTI_PACK_MAXIMUM_NO_OF_ITEMS);

        editableItemsInPack.setError(itemsInPackError);
    }

    // TODO - Validate for MAX_MASS in UnitOfMeasureConstants

    public void validatePackSize(EditText editableMeasurement) {

        int measurementViewId = editableMeasurement.getId();
        int measurement = parseIntegerFromEditText(editableMeasurement);

        if (measurement == MEASUREMENT_ERROR) {

            Log.d(TAG, "validatePackSize: NumberFormatException error, resetting values.");
            setNumberFormatExceptionError(editableMeasurement);
            resetMeasurementValues();

        } else if (measurementHasChanged(measurementViewId, measurement)) {

            Log.d(TAG, "validatePackSize: Measurement has changed, sending measurement " +
                    "for processing");
            processMeasurements(editableMeasurement, measurement);
        }

        Log.d(TAG, "validatePackSize: Measurement has not changed, aborting.");
    }

    private boolean measurementHasChanged(int measurementViewId, int newMeasurement) {

        // Should this be checking against the unit of measure value and not the measurement?

        int oldMeasurement;

        switch (measurementViewId) {

            case R.id.editable_items_in_pack:

                oldMeasurement = viewModel.getMeasurement().getNumberOfItems();

                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChanged: numberOfItems has changed from: " +
                            oldMeasurement + " to: " + newMeasurement);

                return newMeasurement != oldMeasurement;

            case R.id.pack_editable_measurement_one:

                oldMeasurement = viewModel.getMeasurement().getPackMeasurementOne();

                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChanged: packOne has changed from: " +
                            oldMeasurement + " to: " + newMeasurement);

                return newMeasurement != oldMeasurement;

            case R.id.pack_editable_measurement_two:

                oldMeasurement = viewModel.getMeasurement().getPackMeasurementTwo();

                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChanged: packTwo has changed from: " +
                            oldMeasurement + " to: " + newMeasurement);

                return newMeasurement != oldMeasurement;

            case R.id.pack_editable_measurement_three:

                oldMeasurement = viewModel.getMeasurement().getPackMeasurementThree();

                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChanged: packThree has changed from: " +
                            oldMeasurement + " to: " + newMeasurement);

                return newMeasurement != oldMeasurement;

            case R.id.item_editable_measurement_one:

                oldMeasurement = viewModel.getMeasurement().getItemMeasurementOne();

                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChanged: itemOne has changed from: " +
                            oldMeasurement + " to: " + newMeasurement);

                return newMeasurement != oldMeasurement;

            case R.id.item_editable_measurement_two:

                oldMeasurement = viewModel.getMeasurement().getItemMeasurementTwo();

                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChanged: itemTwo has changed from: " +
                            oldMeasurement + " to: " + newMeasurement);

                return newMeasurement != oldMeasurement;

            case R.id.item_editable_measurement_three:

                oldMeasurement = viewModel.getMeasurement().getItemMeasurementThree();

                if (oldMeasurement != newMeasurement)
                    Log.d(TAG, "measurementHasChanged: itemThree has changed from: " +
                            oldMeasurement + " to: " + newMeasurement);

                return newMeasurement != oldMeasurement;
        }
        Log.d(TAG, "measurementHasChanged: View not recognised, aborting.");
        return false;
    }

    private void processMeasurements(EditText editableMeasurement, int newMeasurement) {

        // TODO - check for changes before processing measurements!!

        int viewId = editableMeasurement.getId();

        Log.d(TAG, "processMeasurements: New measurement received from: " +
                resources.getResourceEntryName(editableMeasurement.getId()) + " measurement is: "
                + newMeasurement);

        Log.d(TAG, "processMeasurements: number of packs: " +
                viewModel.getMeasurement().getNumberOfItems());

        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one) {


            Log.d(TAG, "processMeasurements: processing change to pack measurement One");
            measurementIsSet = unitOfMeasure.setPackMeasurementOne(newMeasurement);

        } else if (viewId == R.id.pack_editable_measurement_two) {

            Log.d(TAG, "processMeasurements: processing change to pack measurement Two");
            measurementIsSet = unitOfMeasure.setPackMeasurementTwo(newMeasurement);

        } else if (viewId == R.id.pack_editable_measurement_three) {

            Log.d(TAG, "processMeasurements: processing change to pack measurement three");
            measurementIsSet = unitOfMeasure.setPackMeasurementThree(newMeasurement);

        } else if (viewId == R.id.item_editable_measurement_one) {

            Log.d(TAG, "processMeasurements: processing change to item measurement One");
            measurementIsSet = unitOfMeasure.setItemMeasurementOne(newMeasurement);

        } else if (viewId == R.id.item_editable_measurement_two) {

            Log.d(TAG, "processMeasurements: processing change to item measurement Two");
            measurementIsSet = unitOfMeasure.setItemMeasurementTwo(newMeasurement);

        } else if (viewId == R.id.item_editable_measurement_three) {

            Log.d(TAG, "processMeasurements: ");
            measurementIsSet = unitOfMeasure.setItemMeasurementThree(newMeasurement);

        } else {

            Log.d(TAG, "processMeasurements: View not recognised!. View passed in: " +
                    resources.getResourceEntryName(viewId) + " Cannot process further, aborting.");

            return;
        }

        Log.d(TAG, "processMeasurements: MeasurementOneIsSet: " + measurementIsSet);

        if (measurementIsSet) {
            Log.d(TAG, "processMeasurements: MeasurementModel one IS SET!");
            updateNumericValues();

        } else {

            Log.d(TAG, "processMeasurements: MeasurementModel is out of bounds");
            unitOfMeasure.baseSiUnitsAreSet(viewModel.getMeasurement().getBaseSiUnits());
            setMeasurementOutOfBoundsError(editableMeasurement);
        }
        // Input from one of the measurement fields is zero. Or multiPack has been selected and has
        // no valid number. Reset all numeric fields apart from number of items
        Log.d(TAG, "processMeasurements: measurements have been zeroed out.");

    }

    private int parseIntegerFromEditText(EditText editableMeasurement) {

        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty()) return 0;

        try {

            return Integer.parseInt(rawMeasurement);

        } catch (NumberFormatException e) {

            setNumberFormatExceptionError(editableMeasurement);
            return -1;
        }
    }

    private void setMeasurementOutOfBoundsError(EditText editableMeasurement) {

        Log.d(TAG, "setMeasurementOutOfBoundsError: Pack size out of bounds error");

        setErrorTo(editableMeasurement);

        resetMeasurementValues();
    }

    private void setErrorTo(EditText editableMeasurement) {

        MeasurementModel measurementModel = unitOfMeasure.getMinAndMax();

        // TODO - Add quantity strings (plurals)
        // https://developer.android.com/guide/topics/resources/string-resource.html#Plurals
        // TODO - Use plurals for this error
        // TODO - If measurement units contain zero values do not show them
        // TODO - Have different errors for 1 to 3 measurement values

        editableMeasurement.setError(

                resources.getString(
                        R.string.input_error_pack_size,
                        unitOfMeasure.getTypeAsString(),
                        measurementModel.getMaximumMeasurementTwo(),
                        unitOfMeasure.getMeasurementUnitTwo(),
                        measurementModel.getMinimumMeasurementOne(),
                        unitOfMeasure.getMeasurementUnitOne(),
                        unitOfMeasure.getTypeAsString(),
                        getMinimumPerItemMeasurement(measurementModel),
                        unitOfMeasure.getMeasurementUnitOne()));


        Log.d(TAG, "setErrorTo: Error should be set.");
    }

    private void resetMeasurementValues() {

        unitOfMeasure.resetNumericValues();
        viewModel.getMeasurement().resetNumericValues();
        Log.d(TAG, "resetNumericValues: " + unitOfMeasure.toString());
    }

    private void setNumberFormatExceptionError(EditText editable) {

        editable.setError(resources.getString(R.string.number_format_exception));
    }


    private int getMinimumPerItemMeasurement(MeasurementModel measurementModel) {

        if (measurementModel.getNumberOfItems() > SINGLE_ITEM)
            return measurementModel.getMinimumMeasurementOne() / measurementModel.getNumberOfItems();

        return measurementModel.getMinimumMeasurementOne();
    }

    public void setNewBaseSiUnits(double baseSiUnits) {

        Log.d(TAG, "setNewBaseSiUnits: New base units received: " + baseSiUnits);
    }

    public void setNumberOfItems(int numberOfItems) {

    }

    public void setBaseSiUnits(double baseSiUnits) {

    }

    public boolean setNewUnitOfMeasure(MeasurementSubType subType) {

        newUnitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                applicationContext, subType);

        return true;
    }
}