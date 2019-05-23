package com.example.peter.thekitchenmenu.ui.detail.product.editor;

import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubType;

import androidx.core.util.Pair;

public class ProductMeasurementHandler {

    private static final String TAG = "tkm-MeasurementHandler";

    private static final int MEASUREMENT_ERROR = -1;
    private ProductMeasurementViewModel viewModel;

    public ProductMeasurementHandler(ProductMeasurementViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void newUnitOfMeasureSelected(Spinner spinnerWithSubType) {
        viewModel.setSubType(MeasurementSubType.values()
                        [spinnerWithSubType.getSelectedItemPosition()]);
    }

    public void newMeasurementReceived(EditText editableMeasurement) {
        int viewId = editableMeasurement.getId();
        double decimalMeasurement;
        int integerMeasurement;
        int numberOfUnitsAfterDecimal;

        if (
                viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one) {

            Pair[] inputDigitsFilters = viewModel.getUnitOfMeasure().getInputDigitsFilter();
            numberOfUnitsAfterDecimal = (int) inputDigitsFilters[0].second;

        } else numberOfUnitsAfterDecimal = 0;

        if (numberOfUnitsAfterDecimal > 0) {
            decimalMeasurement = parseDecimalFromString(editableMeasurement);

            if (decimalMeasurement == MEASUREMENT_ERROR) return;

            viewModel.newMeasurementReceived(
                    viewId,
                    0,
                    decimalMeasurement,
                    numberOfUnitsAfterDecimal);

        } else {
            integerMeasurement = parseIntegerFromString(editableMeasurement);

            if (integerMeasurement == MEASUREMENT_ERROR) return;

            viewModel.newMeasurementReceived(
                    viewId,
                    integerMeasurement,
                    0.,
                    numberOfUnitsAfterDecimal);
        }
    }

    private double parseDecimalFromString(EditText editableMeasurement) {
        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty() || rawMeasurement.equals(".")) return 0.;

        try {
            return Double.parseDouble(rawMeasurement);
        } catch (NumberFormatException e) {
            setNumberFormatExceptionError(editableMeasurement);
            return MEASUREMENT_ERROR;
        }
    }

    private int parseIntegerFromString(TextView editableMeasurement) {
        String rawMeasurement = editableMeasurement.getText().toString();

        if (rawMeasurement.isEmpty()) return 0;

        try {
            return Integer.parseInt(rawMeasurement);
        } catch (NumberFormatException e) {
            setNumberFormatExceptionError(editableMeasurement);
            return MEASUREMENT_ERROR;
        }
    }

    private void setNumberFormatExceptionError(TextView editable) {
        editable.setError(
                editable.getContext().
                getResources().getString(R.string.number_format_exception));
    }
}