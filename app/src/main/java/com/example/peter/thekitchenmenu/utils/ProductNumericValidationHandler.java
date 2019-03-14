package com.example.peter.thekitchenmenu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.databinding.ProductEditorBinding;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.Measurement;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureClassSelector;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NO_INPUT;

public class ProductNumericValidationHandler {

    private static final String TAG = "ProductNumericValidatio";

    private Context context;
    private ProductEditorViewModel viewModel;
    private ProductEditorBinding productEditor;
    private Resources resources;

    private UnitOfMeasure unitOfMeasure;
    private Measurement measurement;

    public void setBinding(Context context, ProductEditorBinding productEditor) {

        this.context = context;
        viewModel = productEditor.getProductEditorViewModel();
        this.productEditor = productEditor;

        resources = context.getResources();
        unitOfMeasure = viewModel.getUnitOfMeasure();
        measurement = viewModel.getMeasurement();
    }

    public void newUnitOfMeasureSelected(Spinner spinnerWithSubType) {

        UnitOfMeasure newUnitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                context, MeasurementSubType.values()[spinnerWithSubType.getSelectedItemPosition()]);

        Log.d(TAG, "newUnitOfMeasureSelected: " +
                newUnitOfMeasure.getMeasurementSubType().getMeasurementType());

        Log.d(TAG, "newUnitOfMeasureSelected: items in old pack are: " +
                unitOfMeasure.getNumberOfItemsInPack());

        // If the view model has a unit of measure that has some base units in it there may be a
        // measurement to convert
        if (unitOfMeasure.getBaseSiUnits() > 0. ||
                unitOfMeasure.getNumberOfItemsInPack() > MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

            Log.d(TAG, "newUnitOfMeasureSelected: old base units are: " +
                    unitOfMeasure.getBaseSiUnits());

            Log.d(TAG, "newUnitOfMeasureSelected: old number of items are: " +
                    unitOfMeasure.getNumberOfItemsInPack());

            if (unitOfMeasure.getNumberOfItemsInPack() > MULTI_PACK_MINIMUM_NO_OF_ITEMS) {

                newUnitOfMeasure.setNumberOfItemsInPack(unitOfMeasure.getNumberOfItemsInPack());
                Log.d(TAG, "newUnitOfMeasureSelected: setting number of items to: " +
                        unitOfMeasure.getNumberOfItemsInPack());
            }

            // If the subtype in the view models unit of measure and the validations new unit of
            // measure are different it may be possible to convert if the types are the same
            if (unitOfMeasure.getMeasurementSubType() != newUnitOfMeasure.getMeasurementSubType()) {

                Log.d(TAG, "newUnitOfMeasureSelected: old and new subtypes are different.");

                // If the types are the same and the base measurement is above 0, there may be a
                // measurement to convert...
                if (unitOfMeasure.getMeasurementType() == newUnitOfMeasure.getMeasurementType()) {

                    Log.d(TAG, "newUnitOfMeasureSelected: old and new types are the same.");

                    // Set the base units to the new unit of measure
                    boolean baseSiUnitsAreSet =
                            newUnitOfMeasure.setBaseSiUnits(unitOfMeasure.getBaseSiUnits());

                    Log.d(TAG, "newUnitOfMeasureSelected: base units are set: " +
                            baseSiUnitsAreSet);

                    // If the base units set ok...
                    if (baseSiUnitsAreSet) {

                        boolean numberOfItemsAreSet =
                                newUnitOfMeasure.setNumberOfItemsInPack(
                                        unitOfMeasure.getNumberOfItemsInPack());
                        Log.d(TAG, "newUnitOfMeasureSelected: No of items are set: " +
                                numberOfItemsAreSet);

                        if (!numberOfItemsAreSet) {
                            // An error, setting number of items could be because it reduces the
                            // item size below the minimum measurement. Set error and reset values
                            // within the unit of measure and the Measurement (shouldn't have to do
                            // both)
                            updateUnitOfMeasure(newUnitOfMeasure);
                            updateMeasurementValues();
                        }

                        // Measurements have been successfully converted
                        updateUnitOfMeasure(newUnitOfMeasure);
                        updateMeasurementValues();
                        updateMeasurementNumericValues();
                    }
                    // The types are the same, but the base units would not set. This can only
                    // happen if the numberOfItems has been set first and in setting the base units
                    // has made the item size smaller than the smallest item for this unit of
                    // measure class.

                    // Reset values and maybe pass an error or toast reflecting the above?
                    updateUnitOfMeasure(newUnitOfMeasure);
                    updateMeasurementValues();

                } else {

                    // The types are not the same, cannot convert
                    // Reset the measurement in the view model, update the unit of measure in the view
                    // model and update the measurement
                    Log.d(TAG, "newUnitOfMeasureSelected: old and new types are different, " +
                            "cannot convert measurement.");
                    updateUnitOfMeasure(newUnitOfMeasure);
                    updateMeasurementValues();
                }

            } else {
                // If the subtypes are the same nothing has changed, therefor there is nothing to do.
                Log.d(TAG, "newUnitOfMeasureSelected: old and new subtypes are the same. " +
                        "nothing has changed, aborting");
            }

        } else {
            // TODO - Check to se if this auto updates (from an empty unit of measure to a new empty
            // unit of measure. Measurements are inconvertible between units of measure, or the
            // original unit of measure is empty. Reset the measurement in the view model, update
            // the unit of measure in the view model and update the measurement
            Log.d(TAG, "newUnitOfMeasureSelected: Old unit of measure has no base units " +
                    "and number of items is below the minimum.");

            updateUnitOfMeasure(newUnitOfMeasure);
            updateMeasurementValues();
        }
    }

    private void updateUnitOfMeasure(UnitOfMeasure newUnitOfMeasure) {

        unitOfMeasure = newUnitOfMeasure;
    }

    private void updateMeasurementValues() {

        measurement.resetNonNumericValues();
        measurement.setTypeAsString(unitOfMeasure.getTypeAsString());
        measurement.setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
        measurement.setMeasurementUnitOne(unitOfMeasure.getMeasurementUnitOne());
        measurement.setMeasurementUnitTwo(unitOfMeasure.getMeasurementUnitTwo());
    }

    private void updateMeasurementNumericValues() {

        if (viewModel.getMeasurement().getNumberOfItemsInPack()
                != unitOfMeasure.getNumberOfItemsInPack()) {

            measurement.setNumberOfItemsInPack(
                    unitOfMeasure.getNumberOfItemsInPack());
            Log.d(TAG, "updateMeasurementNumericValues: updating items in pack to: " +
                    unitOfMeasure.getNumberOfItemsInPack());
        }

        if (viewModel.getMeasurement().getPackMeasurementOne() !=
                unitOfMeasure.getPackMeasurementOne()) {

            measurement.setPackMeasurementOne(unitOfMeasure.getPackMeasurementOne());
            Log.d(TAG, "updateMeasurementNumericValues: updating pack One to: " +
                    unitOfMeasure.getPackMeasurementOne());
        }

        if (viewModel.getMeasurement().getPackMeasurementTwo() !=
                unitOfMeasure.getPackMeasurementTwo()) {

            measurement.setPackMeasurementTwo(unitOfMeasure.getPackMeasurementTwo());
            Log.d(TAG, "updateMeasurementNumericValues: updating pack Two to: " +
                    unitOfMeasure.getPackMeasurementTwo());
        }

        if (viewModel.getMeasurement().getItemMeasurementOne() !=
                unitOfMeasure.getItemMeasurementOne()) {

            measurement.setItemMeasurementOne(unitOfMeasure.getItemMeasurementOne());
            Log.d(TAG, "updateMeasurementNumericValues: updating item One to: " +
                    unitOfMeasure.getItemMeasurementOne());
        }

        if (viewModel.getMeasurement().getItemMeasurementTwo() !=
                unitOfMeasure.getItemMeasurementTwo()) {

            measurement.setItemMeasurementTwo(unitOfMeasure.getItemMeasurementTwo());
            Log.d(TAG, "updateMeasurementNumericValues: updating item Two to: " +
                    unitOfMeasure.getItemMeasurementTwo());
        }
    }

    public void validateItemsInPack(EditText editableItemsInPack) {

        String rawItemsInPack = editableItemsInPack.getText().toString();

        if (!rawItemsInPack.isEmpty()) try {

            int numberOfItemsInPack = Integer.parseInt(rawItemsInPack);
            setItemsInPack(editableItemsInPack, numberOfItemsInPack);

        } catch (NumberFormatException e) {

            setNumberFormatExceptionError(editableItemsInPack);
        }

        Log.d(TAG, "validateItemsInPack: items in pack are blank. Resetting values in UoM and " +
                "Measurement");
        unitOfMeasure.setBaseSiUnits(0);
        measurement.setPackMeasurementOne(0);
        measurement.setPackMeasurementTwo(0);
        measurement.setItemMeasurementOne(0);
        measurement.setItemMeasurementTwo(0);
    }

    private void setItemsInPack(EditText editableItemsInPack, int numberOfItemsInPack) {

        Log.d(TAG, "setItemsInPack: EditableItemsInPack is: " +
                editableItemsInPack.getText().toString() +
                " number of items in pack: " + numberOfItemsInPack);

        boolean itemsInPackAreSet = unitOfMeasure.setNumberOfItemsInPack(numberOfItemsInPack);

        boolean baseUnitsAreAboveMinimumValue =
                unitOfMeasure.getBaseSiUnits() >
                        unitOfMeasure.getMinAndMax().getMinimumMeasurementOne();

        Log.d(TAG, "setItemsInPack: baseUnitsAreAboveMinValue: " +
                baseUnitsAreAboveMinimumValue + " base unit are: " + unitOfMeasure.getBaseSiUnits());

        if (itemsInPackAreSet) {

            viewModel.getProduct().getValue().setNumberOfItemsInPack(numberOfItemsInPack);
            if (baseUnitsAreAboveMinimumValue) viewModel.setNumberOfItemsInPackValidated(true);
            updateMeasurementNumericValues();

        } else {

            setItemsInPackOutOfBoundsError(editableItemsInPack);
        }
    }

    private void modifyItemsInPack(AppCompatButton button) {

//        int buttonId = button.getId();
//        int numberOfItemsInPack = parseNumberFromEditText(editableItemsInPack);
//
//        if (buttonId == R.id.multi_pack_plus) editableItemsInPack.setText(numberOfItemsInPack +1);
//        if (buttonId == R.id.multi_pack_minus) editableItemsInPack.setText(numberOfItemsInPack -1);
    }

    private int parseNumberFromEditText(EditText editTextToParse) {

        String rawNumber = editTextToParse.getText().toString();
        int number = 0;
        if (rawNumber.isEmpty()) return number;

        try {

            number = Integer.parseInt(rawNumber);
            return number;

        } catch (NumberFormatException e) {

            return -1;
        }
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

    public void validatePackSize(EditText editableMeasurementOne, EditText editableMeasurementTwo) {

        processMeasurements(editableMeasurementOne, editableMeasurementTwo);
    }

    private void processMeasurements(EditText editableMeasurementOne,
                                     EditText editableMeasurementTwo) {

        int measurementOneViewId = editableMeasurementOne.getId();
        int measurementTwoViewId = editableMeasurementTwo.getId();

        int measurementOne = getMeasurement(editableMeasurementOne);

        if (measurementOne == -1) {

            setNumberFormatExceptionError(editableMeasurementOne);
            resetMeasurementValues();
        }

        int measurementTwo = getMeasurement(editableMeasurementTwo);

        if (measurementTwo == -1) {

            setNumberFormatExceptionError(editableMeasurementTwo);
            resetMeasurementValues();
        }

        Log.d(TAG, "processMeasurements: New Two: " + measurementTwo +
                " New One: " + measurementOne);

//        if ((measurementOne > NO_INPUT || measurementTwo > NO_INPUT)) {

        boolean isMultiPack = viewModel.getMultiPack().get();
        boolean multiPackSelectedWithNoItems = viewModel.
                getMeasurement().
                getNumberOfItemsInPack() < MULTI_PACK_MINIMUM_NO_OF_ITEMS;

        Log.d(TAG, "processMeasurements: isMultiPack: " + isMultiPack +
                " selectedWithNoItems: " + multiPackSelectedWithNoItems);

        Log.d(TAG, "processMeasurements: number of items: " +
                viewModel.getMeasurement().getNumberOfItemsInPack());

        boolean measurementOneIsSet = false;
        boolean measurementTwoIsSet = false;

        if (isMultiPack && multiPackSelectedWithNoItems) {

            setItemsInPackOutOfBoundsError(productEditor.editableItemsInPack);
            measurement.setPackMeasurementOne(NO_INPUT);
            measurement.setPackMeasurementTwo(NO_INPUT);
            measurement.setItemMeasurementOne(NO_INPUT);
            measurement.setItemMeasurementTwo(NO_INPUT);
        }


        if (measurementOneViewId == R.id.pack_editable_measurement_one &&
                measurementTwoViewId == R.id.pack_editable_measurement_two) {

            Log.d(TAG, "processMeasurements: processing pack measurements");

            if (measurementOne != unitOfMeasure.getPackMeasurementOne())
                measurementOneIsSet = unitOfMeasure.setPackMeasurementOne(measurementOne);

            else if (measurementTwo != unitOfMeasure.getPackMeasurementTwo())
                measurementTwoIsSet = unitOfMeasure.setPackMeasurementTwo(measurementTwo);

            else {
                Log.d(TAG, "processMeasurements: Measurements have not changed, aborting");
                return;
            }

            Log.d(TAG, "processMeasurements: PackTwoIsSet: " + measurementTwoIsSet +
                    " PackOneIsSet: " + measurementOneIsSet);

        } else if (measurementOneViewId == R.id.item_editable_measurement_one &&
                measurementTwoViewId == R.id.item_editable_measurement_two) {

            Log.d(TAG, "processMeasurements: processing item measurements");

            if (measurementOne != unitOfMeasure.getItemMeasurementOne())
                measurementOneIsSet = unitOfMeasure.setItemMeasurementOne(measurementOne);

            else if (measurementTwo != unitOfMeasure.getItemMeasurementTwo())
                measurementTwoIsSet = unitOfMeasure.setItemMeasurementTwo(measurementTwo);

            else {
                Log.d(TAG, "processMeasurements: Measurements have not changed, aborting");
                return;
            }

            Log.d(TAG, "processMeasurements: ItemTwoIsSet: " + measurementTwoIsSet +
                    " ItemOneIsSet: " + measurementOneIsSet);

        } else {

            Log.e(TAG, "processMeasurements: Wrong combination of views sent for " +
                    "validation. Views sent are: " +
                    resources.getResourceEntryName(measurementOneViewId) + " and " +
                    resources.getResourceEntryName(measurementTwoViewId));
        }

        Log.d(TAG, "processMeasurements: MeasurementOneIsSet: " + measurementOneIsSet +
                " MeasurementTwoIsSet: " + measurementTwoIsSet);

        if (measurementOneIsSet) {
            Log.d(TAG, "processMeasurements: Measurement one IS SET!");
            updateMeasurementNumericValues();

        } else if (measurementTwoIsSet) {

            Log.d(TAG, "processMeasurements: Measurement two IS SET!");
            updateMeasurementNumericValues();

        } else {
            // One of the measurements did not set, so reset back to last known good value
            // And show an error
            Log.d(TAG, "processMeasurements: Measurement is out of bounds");
            unitOfMeasure.setBaseSiUnits(measurement.getBaseSiUnits());
            setMeasurementOutOfBoundsError();
        }
//        }
        // Input from one of the measurement fields is zero. Or multiPack has been selected and has
        // no valid number. Reset all numeric fields apart from
        // Number of items
        Log.d(TAG, "processMeasurements: measurements have been zeroed out.");

    }

    private int getMeasurement(EditText editableMeasurement) {

        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty()) return 0;

        try {

            return Integer.parseInt(rawMeasurement);

        } catch (NumberFormatException e) {

            return -1;
        }
    }

    private void setMeasurementOutOfBoundsError() {

        Log.d(TAG, "setMeasurementOutOfBoundsError: Pack size out of bounds error");

        setErrorTo(productEditor.packEditableMeasurementOne);
        setErrorTo(productEditor.packEditableMeasurementTwo);

        if (productEditor.itemEditableMeasurementOne.getVisibility() == View.VISIBLE)
            setErrorTo(productEditor.itemEditableMeasurementOne);

        if (productEditor.itemEditableMeasurementTwo.getVisibility() == View.VISIBLE)
            setErrorTo(productEditor.itemEditableMeasurementTwo);

        resetMeasurementValues();
    }

    private void setErrorTo(EditText editableMeasurement) {

        Measurement measurement = unitOfMeasure.getMinAndMax();

        // TODO - Use plurals for this error
        // TODO - If measurement units contain zero values do not show them
        // TODO - Have different errors for 1 to 3 measurement values

        editableMeasurement.setError(

                resources.getString(
                        R.string.input_error_pack_size,
                        measurement.getTypeAsString(),
                        measurement.getMaximumMeasurementTwo(),
                        measurement.getMeasurementUnitTwo(),
                        measurement.getMinimumMeasurementOne(),
                        measurement.getMeasurementUnitOne(),
                        measurement.getTypeAsString(),
                        getMinimumPerItemMeasurement(measurement),
                        measurement.getMeasurementUnitOne()));


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

    // TODO - Add quantity strings (plurals)
    // https://developer.android.com/guide/topics/resources/string-resource.html#Plurals

    private int getMinimumPerItemMeasurement(Measurement measurement) {

        if (measurement.getNumberOfItemsInPack() >= MULTI_PACK_MINIMUM_NO_OF_ITEMS)
            return measurement.getMinimumMeasurementOne() / measurement.getNumberOfItemsInPack();

        return measurement.getMinimumMeasurementOne();
    }
}
