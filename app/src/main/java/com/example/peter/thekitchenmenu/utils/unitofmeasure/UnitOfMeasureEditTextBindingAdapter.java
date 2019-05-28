package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.text.InputFilter;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;

import androidx.core.util.Pair;
import androidx.databinding.BindingAdapter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class UnitOfMeasureEditTextBindingAdapter {

    private static final String TAG = "tkm-InputFilters";

    @BindingAdapter(value = {"setUpEditTextForSubtypeSelected"})
    public static void setUpEditTextForSubtypeSelected(EditText editText,
                                                       MeasurementSubtype subtype) {
        UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
        int viewId = editText.getId();
        int units = unitOfMeasure.getNumberOfMeasurementUnits();
        int digitsAfterDecimal = (int) unitOfMeasure.getMeasurementUnitNumberTypeArray()[0].second;

        if (viewId == R.id.pack_editable_measurement_one && digitsAfterDecimal > 0 ||
                viewId == R.id.item_editable_measurement_one && digitsAfterDecimal > 0)
            editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);

        else editText.setInputType(TYPE_CLASS_NUMBER);

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one ||
                viewId == R.id.pack_editable_measurement_two && units > 1 ||
                viewId == R.id.item_editable_measurement_two && units > 1)
            setInputFilters(viewId, editText, unitOfMeasure);
    }

    private static void setInputFilters(int viewId,
                                        EditText editText,
                                        UnitOfMeasure unitOfMeasure) {
        Pair[] inputDigitsFilters = unitOfMeasure.getMeasurementUnitNumberTypeArray();

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one)
            editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                            (int) inputDigitsFilters[0].first,
                            (int) inputDigitsFilters[0].second)});

        if (viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.item_editable_measurement_two) {
            editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                            (int) inputDigitsFilters[1].first,
                            (int) inputDigitsFilters[1].second)});
        }
    }
}