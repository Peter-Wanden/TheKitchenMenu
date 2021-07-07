package com.example.peter.thekitchenmenu.ui.detail.product.producteditor;

import android.widget.EditText;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;

public class ProductMeasurementHandler {

    private static final int MEASUREMENT_ERROR = -1;
    private ProductMeasurementViewModel viewModel;

    ProductMeasurementHandler(ProductMeasurementViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void newMeasurementReceived(EditText editableMeasurement) {
        int viewId = editableMeasurement.getId();
        double decimalUnit;
        int integerUnit;

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_one) {
            decimalUnit = parseDecimalFromString(editableMeasurement);
            if (decimalUnit == MEASUREMENT_ERROR)
                return;
            viewModel.processDecimalUnit(viewId, decimalUnit);

        } else {
            integerUnit = parseIntegerFromString(editableMeasurement);
            if (integerUnit == MEASUREMENT_ERROR)
                return;
            viewModel.processIntegerUnit(viewId, integerUnit);
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
        editable.setError(editable.getResources().getString(R.string.number_format_exception));
    }
}