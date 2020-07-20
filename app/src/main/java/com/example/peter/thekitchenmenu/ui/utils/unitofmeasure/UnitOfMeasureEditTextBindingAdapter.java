package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import android.text.InputFilter;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasure;
import com.example.peter.thekitchenmenu.ui.utils.DecimalDigitsInputFilter;

import androidx.core.util.Pair;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureAbstract.CONVERSION_FACTOR_WIDTH_INDEX;
import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureAbstract.UNIT_ONE_WIDTH_INDEX;
import static com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.UnitOfMeasureAbstract.UNIT_TWO_WIDTH_INDEX;

public class UnitOfMeasureEditTextBindingAdapter {

//    @BindingAdapter(value = {"setUpEditTextForSubtypeSelected"})
    public static void setUpEditTextForSubtypeSelected(EditText editText,
                                                       MeasurementSubtype subtype) {
        UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
        int viewId = editText.getId();
        int units = unitOfMeasure.getNumberOfUnits();
        int digitsAfterDecimal = (int) unitOfMeasure.
                getMaxUnitDigitWidths()[UNIT_ONE_WIDTH_INDEX].second;

        if (viewId == R.id.pack_editable_measurement_one && digitsAfterDecimal > 0 ||
                viewId == R.id.product_editable_measurement_one && digitsAfterDecimal > 0 ||
                viewId == R.id.recipe_ingredient_editable_measurement_one && digitsAfterDecimal > 0) {

            editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);

        } else {
            editText.setInputType(TYPE_CLASS_NUMBER);
        }

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_one ||
                viewId == R.id.pack_editable_measurement_two && units > 1 ||
                viewId == R.id.product_editable_measurement_two && units > 1 ||
                viewId == R.id.recipe_ingredient_editable_measurement_one && units > 1 ||
                viewId == R.id.recipe_ingredient_editable_measurement_two) {

            setInputFilters(viewId, editText, unitOfMeasure);
        }
    }

    private static void setInputFilters(int viewId,
                                        EditText editText,
                                        UnitOfMeasure unitOfMeasure) {
        Pair[] inputDigitsFilters = unitOfMeasure.getMaxUnitDigitWidths();

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_one ||
                viewId == R.id.recipe_ingredient_editable_measurement_one) {

            editText.setFilters(
                    new InputFilter[]{
                            new DecimalDigitsInputFilter(
                                    (int) inputDigitsFilters[UNIT_ONE_WIDTH_INDEX].first,
                                    (int) inputDigitsFilters[UNIT_ONE_WIDTH_INDEX].second)
                    }
            );
        }

        if (viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.product_editable_measurement_two ||
                viewId == R.id.recipe_ingredient_editable_measurement_two) {

            editText.setFilters(
                    new InputFilter[]{
                            new DecimalDigitsInputFilter(
                                    (int) inputDigitsFilters[UNIT_TWO_WIDTH_INDEX].first,
                                    (int) inputDigitsFilters[UNIT_TWO_WIDTH_INDEX].second)
                    }
            );
        }
    }

//    @BindingAdapter(value = {"setUpEditTextForConversionFactor"})
    public static void setUpEditTextForConversionFactor(EditText editText,
                                                        MeasurementSubtype subtype) {
        UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();

        if (unitOfMeasure.isConversionFactorEnabled()) {
            int viewId = editText.getId();
            int digitsBeforeDecimal = (int) unitOfMeasure.
                    getMaxUnitDigitWidths()[CONVERSION_FACTOR_WIDTH_INDEX].first;
            int digitsAfterDecimal = (int) unitOfMeasure.
                    getMaxUnitDigitWidths()[CONVERSION_FACTOR_WIDTH_INDEX].second;

            if (viewId == R.id.recipe_ingredient_editable_conversion_factor) {

                editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
                editText.setFilters(
                        new InputFilter[]{
                                new DecimalDigitsInputFilter(
                                        digitsBeforeDecimal,
                                        digitsAfterDecimal)
                        }
                );
            }
        }
    }
}