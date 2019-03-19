package com.example.peter.thekitchenmenu.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.ObservableProductModel;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.ObservableMeasurement;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureClassSelector;
import com.example.peter.thekitchenmenu.viewmodels.ProductEditorViewModel;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_PACKS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_PACKS;

public class ProductNumericValidationHandler {

    private static final String TAG = "ProductNumericValidatio";

    private Context applicationContext;
    private ProductEditorViewModel viewModel;
    private Resources resources;

    private UnitOfMeasure unitOfMeasure;
    private ObservableMeasurement measurement;
    private static final int MEASUREMENT_ERROR = -1;

    private int numberOfPacksInPack;

    public ProductNumericValidationHandler(Application applicationContext,
                                           ProductEditorViewModel viewModel,
                                           ObservableMeasurement measurement) {

        this.applicationContext = applicationContext;
        this.viewModel = viewModel;
        resources = applicationContext.getResources();
        this.measurement = measurement;
    }

    public void newUnitOfMeasureSelected(Spinner spinnerWithSubType) {

        UnitOfMeasure newUnitOfMeasure = UnitOfMeasureClassSelector.getClassWithSubType(
                applicationContext, MeasurementSubType.values()[spinnerWithSubType.getSelectedItemPosition()]);

        Log.d(TAG, "newUnitOfMeasureSelected: " +
                newUnitOfMeasure.getMeasurementSubType().getMeasurementType());

        Log.d(TAG, "newUnitOfMeasureSelected: packs in old pack are: " +
                unitOfMeasure.getNumberOfPacksInPack());

        // If the view model has a unit of measure that has some base units in it there may be a
        // measurement to convert
        if (unitOfMeasure.getBaseSiUnits() > 0. ||
                unitOfMeasure.getNumberOfPacksInPack() > MULTI_PACK_MINIMUM_NO_OF_PACKS) {

            Log.d(TAG, "newUnitOfMeasureSelected: old base units are: " +
                    unitOfMeasure.getBaseSiUnits());

            Log.d(TAG, "newUnitOfMeasureSelected: old number of packs are: " +
                    unitOfMeasure.getNumberOfPacksInPack());

            if (unitOfMeasure.getNumberOfPacksInPack() > MULTI_PACK_MINIMUM_NO_OF_PACKS) {

                newUnitOfMeasure.setNumberOfPacksInPack(unitOfMeasure.getNumberOfPacksInPack());
                Log.d(TAG, "newUnitOfMeasureSelected: setting number of packs to: " +
                        unitOfMeasure.getNumberOfPacksInPack());
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

                        boolean numberOfPacksIsSet =
                                newUnitOfMeasure.setNumberOfPacksInPack(
                                        unitOfMeasure.getNumberOfPacksInPack());
                        Log.d(TAG, "newUnitOfMeasureSelected: No of packs are set: " +
                                numberOfPacksIsSet);

                        if (!numberOfPacksIsSet) {
                            // An error, setting number of packs could be because it reduces the
                            // packs size below the minimum measurement. Set error and reset values
                            // within the unit of measure and the ObservableMeasurement (shouldn't have to do
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
                    // happen if the numberOfPacks has been set first and in setting the base units
                    // has made the pack size smaller than the smallest unit for this measure class.

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
                    "and number of packs is below the minimum.");

            updateUnitOfMeasure(newUnitOfMeasure);
            updateMeasurementValues();
        }
    }

    private void updateUnitOfMeasure(UnitOfMeasure newUnitOfMeasure) {

        unitOfMeasure = newUnitOfMeasure;
    }

    private void updateMeasurementValues() {

        measurement.resetMeasurementSubType();
        measurement.setMeasurementSubType(unitOfMeasure.getMeasurementSubType());
    }

    private void updateMeasurementNumericValues() {

        if (measurement.getNumberOfPacksInPack() != unitOfMeasure.getNumberOfPacksInPack()) {
            measurement.setNumberOfPacksInPack(unitOfMeasure.getNumberOfPacksInPack());
            Log.d(TAG, "updateMeasurementNumericValues: updating packs in pack to: " +
                    unitOfMeasure.getNumberOfPacksInPack());
        }

        if (measurement.getPackMeasurementOne() != unitOfMeasure.getPackMeasurementOne()) {
            measurement.setPackMeasurementOne(unitOfMeasure.getPackMeasurementOne());
            Log.d(TAG, "updateMeasurementNumericValues: updating pack One to: " +
                    unitOfMeasure.getPackMeasurementOne());
        }

        if (measurement.getPackMeasurementTwo() != unitOfMeasure.getPackMeasurementTwo()) {
            measurement.setPackMeasurementTwo(unitOfMeasure.getPackMeasurementTwo());
            Log.d(TAG, "updateMeasurementNumericValues: updating pack Two to: " +
                    unitOfMeasure.getPackMeasurementTwo());
        }

        if (measurement.getSinglePackMeasurementOne() != unitOfMeasure.getSinglePackMeasurementOne()) {
            measurement.setSinglePackMeasurementOne(unitOfMeasure.getSinglePackMeasurementOne());
            Log.d(TAG, "updateMeasurementNumericValues: updating single pack One to: " +
                    unitOfMeasure.getSinglePackMeasurementOne());
        }

        if (measurement.getSinglePackMeasurementTwo() != unitOfMeasure.getSinglePackMeasurementTwo()) {
            measurement.setSinglePackMeasurementTwo(unitOfMeasure.getSinglePackMeasurementTwo());
            Log.d(TAG, "updateMeasurementNumericValues: updating single pack Two to: " +
                    unitOfMeasure.getSinglePackMeasurementTwo());
        }
    }

    public void validatePacksInPack(EditText editablePacksInPack) {

        String rawPacksInPack = editablePacksInPack.getText().toString();

        if (!rawPacksInPack.isEmpty()) try {

            int numberPacksInPack = Integer.parseInt(rawPacksInPack);
            Log.d(TAG, "validatePacksInPack: Received: " + numberPacksInPack);
            setPacksInPack(editablePacksInPack, numberPacksInPack);

        } catch (NumberFormatException e) {

            setNumberFormatExceptionError(editablePacksInPack);
            Log.d(TAG, "validatePacksInPack: Setting error");
        }
    }

    private void setPacksInPack(EditText editablePacksInPack, int numberOfPacksInPack) {

        Log.d(TAG, "setNumberOfPacks: EditablePacksInPack is: " +
                editablePacksInPack.getText().toString() +
                " number of packs in pack: " + numberOfPacksInPack);

        boolean packsInPackAreSet = unitOfMeasure.setNumberOfPacksInPack(numberOfPacksInPack);

        Log.d(TAG, "setNumberOfPacks: Set number of packs to unit of measure: " +
                packsInPackAreSet);


        Log.d(TAG, "setNumberOfPacks: baseUnitsAreAboveMinValue: " +
                baseUnitsAreAboveMinimumValue() + " base unit are: " + unitOfMeasure.getBaseSiUnits());

        if (packsInPackAreSet) {

            viewModel.getProductEntity().getValue().setNumberOfPacks(numberOfPacksInPack);

            if (baseUnitsAreAboveMinimumValue()) viewModel.setNumberOfPacksInPackValidated(true);
            updateMeasurementNumericValues();

        } else {

            Log.d(TAG, "setNumberOfPacks: setting packs out of bounds error for some reason");
            setPacksInPackOutOfBoundsError(editablePacksInPack);
        }
    }

    private boolean baseUnitsAreAboveMinimumValue() {

        return unitOfMeasure.getBaseSiUnits() >
                unitOfMeasure.getMinAndMax().getMinimumMeasurementOne();
    }

    public void modifyPacksInPackByOne(EditText editablePacksInPack, Button button) {

        numberOfPacksInPack = measurement.getNumberOfPacksInPack();
        int buttonId = button.getId();

        if (buttonId == R.id.multi_pack_plus &&
                numberOfPacksInPack < MULTI_PACK_MAXIMUM_NO_OF_PACKS)
            editablePacksInPack.setText(String.valueOf(numberOfPacksInPack + 1));

        if (buttonId == R.id.multi_pack_minus &&
                numberOfPacksInPack > MULTI_PACK_MINIMUM_NO_OF_PACKS)
            editablePacksInPack.setText(String.valueOf(numberOfPacksInPack -1));
    }

    private void setPacksInPackOutOfBoundsError(EditText editablePacksInPack) {

        String packsInPackError =
                resources.getString(
                        R.string.input_error_packs_in_pack,
                        MULTI_PACK_MINIMUM_NO_OF_PACKS,
                        MULTI_PACK_MAXIMUM_NO_OF_PACKS);

        editablePacksInPack.setError(packsInPackError);
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

            Log.d(TAG, "validatePackSize: ObservableMeasurement has changed, sending measurement " +
                    "for processing");
            processMeasurements(editableMeasurement, measurement);
        }

        Log.d(TAG, "validatePackSize: ObservableMeasurement has not changed, aborting.");
    }

    private boolean measurementHasChanged(int measurementViewId, int newMeasurement) {

        int oldMeasurement;

        switch (measurementViewId) {

            case R.id.pack_editable_measurement_one:

                Log.d(TAG, "measurementHasChanged: Pack measurement one has changed");
                oldMeasurement = unitOfMeasure.getPackMeasurementOne();
                return newMeasurement != oldMeasurement;

            case R.id.pack_editable_measurement_two:

                Log.d(TAG, "measurementHasChanged: Pack measurement Two has changed");
                oldMeasurement = unitOfMeasure.getPackMeasurementTwo();
                return newMeasurement != oldMeasurement;

            case R.id.single_pack_editable_measurement_one:

                Log.d(TAG, "measurementHasChanged: Single pack measurement One has changed");
                oldMeasurement = unitOfMeasure.getSinglePackMeasurementOne();
                return newMeasurement != oldMeasurement;

            case R.id.single_pack_editable_measurement_two:

                Log.d(TAG, "measurementHasChanged: Single pack measurement Two has changed");
                oldMeasurement = unitOfMeasure.getSinglePackMeasurementTwo();
                return newMeasurement != oldMeasurement;
        }
        Log.d(TAG, "measurementHasChanged: View not recognised, aborting.");
        return false;
    }

    private void processMeasurements(EditText editableMeasurement, int newMeasurement) {

        int viewId = editableMeasurement.getId();

        Log.d(TAG, "processMeasurements: New measurement received from: " +
                resources.getResourceEntryName(editableMeasurement.getId()));

        boolean isMultiPack = viewModel.getMultiPack().get();
        boolean multiPackSelectedWithNoPacks =
                measurement.getNumberOfPacksInPack() < MULTI_PACK_MINIMUM_NO_OF_PACKS;

        Log.d(TAG, "processMeasurements: isMultiPack: " + isMultiPack +
                " selectedWithNoPacks: " + multiPackSelectedWithNoPacks);

        Log.d(TAG, "processMeasurements: number of packs: " +
                viewModel.getMeasurement().getNumberOfPacksInPack());

        boolean measurementIsSet = false;

        if (viewId == R.id.pack_editable_measurement_one) {

            Log.d(TAG, "processMeasurements: processing change to pack measurement One");
            measurementIsSet = unitOfMeasure.setPackMeasurementOne(newMeasurement);

        } else if (viewId == R.id.pack_editable_measurement_two) {

            Log.d(TAG, "processMeasurements: processing change to pack measurement Two");
            measurementIsSet = unitOfMeasure.setPackMeasurementTwo(newMeasurement);

        } else if (viewId == R.id.single_pack_editable_measurement_one) {

            Log.d(TAG, "processMeasurements: processing change to single pack measurement One");
            measurementIsSet = unitOfMeasure.setSinglePackMeasurementOne(newMeasurement);

        } else if (viewId == R.id.single_pack_editable_measurement_two) {

            Log.d(TAG, "processMeasurements: processing change to single pack measurement Two");
            measurementIsSet = unitOfMeasure.setSinglePackMeasurementTwo(newMeasurement);
        }

        Log.d(TAG, "processMeasurements: MeasurementOneIsSet: " + measurementIsSet);

        if (measurementIsSet) {
            Log.d(TAG, "processMeasurements: ObservableMeasurement one IS SET!");
            updateMeasurementNumericValues();

        } else {

            Log.d(TAG, "processMeasurements: ObservableMeasurement is out of bounds");
            unitOfMeasure.setBaseSiUnits(measurement.getBaseSiUnits());
            setMeasurementOutOfBoundsError(editableMeasurement);
        }
        // Input from one of the measurement fields is zero. Or multiPack has been selected and has
        // no valid number. Reset all numeric fields apart from number of packs
        Log.d(TAG, "processMeasurements: measurements have been zeroed out.");

    }

    private int parseIntegerFromEditText(EditText editableMeasurement) {

        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty()) return 0;

        try {

            return Integer.parseInt(rawMeasurement);

        } catch (NumberFormatException e) {

            return -1;
        }
    }

    private void setMeasurementOutOfBoundsError(EditText editableMeasurement) {

        Log.d(TAG, "setMeasurementOutOfBoundsError: Pack size out of bounds error");

        setErrorTo(editableMeasurement);

        resetMeasurementValues();
    }

    private void setErrorTo(EditText editableMeasurement) {

        ObservableMeasurement observableMeasurement = unitOfMeasure.getMinAndMax();

        // TODO - Use plurals for this error
        // TODO - If measurement units contain zero values do not show them
        // TODO - Have different errors for 1 to 3 measurement values

        editableMeasurement.setError(

                resources.getString(
                        R.string.input_error_pack_size,
                        unitOfMeasure.getTypeAsString(),
                        observableMeasurement.getMaximumMeasurementTwo(),
                        unitOfMeasure.getMeasurementUnitTwo(),
                        observableMeasurement.getMinimumMeasurementOne(),
                        unitOfMeasure.getMeasurementUnitOne(),
                        unitOfMeasure.getTypeAsString(),
                        getMinimumPerSinglePackMeasurement(observableMeasurement),
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

    // TODO - Add quantity strings (plurals)
    // https://developer.android.com/guide/topics/resources/string-resource.html#Plurals

    private int getMinimumPerSinglePackMeasurement(ObservableMeasurement observableMeasurement) {

        if (observableMeasurement.getNumberOfPacksInPack() >= MULTI_PACK_MINIMUM_NO_OF_PACKS)
            return observableMeasurement.getMinimumMeasurementOne() / observableMeasurement.getNumberOfPacksInPack();

        return observableMeasurement.getMinimumMeasurementOne();
    }
}
