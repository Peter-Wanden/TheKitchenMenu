package com.example.peter.thekitchenmenu.ui.detail.product.editor;

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

import androidx.core.util.Pair;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MINIMUM_NO_OF_ITEMS;

public class ProductMeasurementHandler {

    private static final String TAG = "tkm-MeasurementHandler";

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
    }

    public void newUnitOfMeasureSelected(Spinner spinnerWithSubType) {
        viewModel.newUnitOfMeasureSelected(spinnerWithSubType.getSelectedItemPosition());
    }

    public void numberOfItemsChanged(TextView editableItemsInPack) {
        int newNumberOfItems = parseIntegerFromEditText(editableItemsInPack);

        if (newNumberOfItems == 0 || newNumberOfItems == MEASUREMENT_ERROR) return;
        viewModel.numberOfItemsChanged(newNumberOfItems);
    }

    public void modifyNumberOfItemsByOne(Button button) {
        viewModel.modifyNumberOfItemsByOne(button.getId());
    }

    public void validatePackSize(EditText editableMeasurement) {
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
            viewModel.validatePackSize(viewId, 0, doubleMeasurement);

        } else {
            integerMeasurement = parseIntegerFromEditText(editableMeasurement);
            if (integerMeasurement == MEASUREMENT_ERROR) return;
            viewModel.validatePackSize(viewId, integerMeasurement, 0.);
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

    private void setNumberFormatExceptionError(TextView editable) {
        editable.setError(resources.getString(R.string.number_format_exception));
    }
}