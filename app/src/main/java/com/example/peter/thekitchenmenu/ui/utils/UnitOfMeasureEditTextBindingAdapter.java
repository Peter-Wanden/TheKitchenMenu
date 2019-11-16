package com.example.peter.thekitchenmenu.ui.utils;

import android.text.InputFilter;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.UnitOfMeasure;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;

import androidx.core.util.Pair;
import androidx.databinding.BindingAdapter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class UnitOfMeasureEditTextBindingAdapter {

    private static final String TAG = "tkm-EditTextAdapter";

    @BindingAdapter(value = {"setUpEditTextForSubtypeSelected"})
    public static void setUpEditTextForSubtypeSelected(EditText editText,
                                                       MeasurementSubtype subtype) {
        UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
        int viewId = editText.getId();
        int units = unitOfMeasure.getNumberOfUnits();
        int digitsAfterDecimal = (int) unitOfMeasure.getMaxUnitDigitWidths()[0].second;

        if (viewId == R.id.pack_editable_measurement_one && digitsAfterDecimal > 0 ||
                viewId == R.id.product_editable_measurement_one && digitsAfterDecimal > 0 ||
                viewId == R.id.recipe_ingredient_editable_measurement_one && digitsAfterDecimal > 0)

            editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
        else
            editText.setInputType(TYPE_CLASS_NUMBER);

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_one ||
                viewId == R.id.pack_editable_measurement_two && units > 1 ||
                viewId == R.id.product_editable_measurement_two && units > 1 ||
                viewId == R.id.recipe_ingredient_editable_measurement_one && units > 1 ||
                viewId == R.id.recipe_ingredient_editable_measurement_two)

            setInputFilters(viewId, editText, unitOfMeasure);
    }

    private static void setInputFilters(int viewId,
                                        EditText editText,
                                        UnitOfMeasure unitOfMeasure) {
        Pair[] inputDigitsFilters = unitOfMeasure.getMaxUnitDigitWidths();

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_one ||
                viewId == R.id.recipe_ingredient_editable_measurement_one)

            editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                    (int) inputDigitsFilters[0].first,
                    (int) inputDigitsFilters[0].second)});

        if (viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.product_editable_measurement_two ||
                viewId == R.id.recipe_ingredient_editable_measurement_two)

            editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                    (int) inputDigitsFilters[1].first,
                    (int) inputDigitsFilters[1].second)});
    }

    @BindingAdapter(value = {"setUpEditTextForConversionFactor"})
    public static void setUpEditTextForConversionFactor(EditText editText,
                                                        MeasurementSubtype subtype) {
                UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
        if (unitOfMeasure.isConversionFactorEnabled()) {
            int viewId = editText.getId();
            int digitsBeforeDecimal = (int) unitOfMeasure.getMaxUnitDigitWidths()[2].first;
            int digitsAfterDecimal = (int) unitOfMeasure.getMaxUnitDigitWidths()[2].second;

            if (viewId == R.id.recipe_ingredient_editable_conversion_factor) {

                editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
                editText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(
                        digitsBeforeDecimal, digitsAfterDecimal)});
            }
        }
    }
}