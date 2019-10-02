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
        double decimalMeasurement;
        int integerMeasurement;

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_one) {
            decimalMeasurement = parseDecimalFromString(editableMeasurement);
            if (decimalMeasurement == MEASUREMENT_ERROR)
                return;
            viewModel.newDecimalMeasurementReceived(viewId, decimalMeasurement);

        } else {
            integerMeasurement = parseIntegerFromString(editableMeasurement);
            if (integerMeasurement == MEASUREMENT_ERROR)
                return;
            viewModel.newIntegerMeasurementReceived(viewId, integerMeasurement);
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