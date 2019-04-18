package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;

import androidx.core.util.Pair;
import androidx.databinding.BindingAdapter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class UnitOfMeasureEditTextBindingAdapter {

    private static final String TAG = "UnitOfMeasureEditTextBi";

    @BindingAdapter(value = {"setUpEditTextForSubTypeSelected"})
    public static void setUpEditTextForSubTypeSelected(EditText editText,
                                                       MeasurementSubType subType) {
        setupViews(editText, subType);
    }

    private static void setupViews(EditText editText, MeasurementSubType subType) {

        UnitOfMeasure unitOfMeasure = subType.getMeasurementClass();

        int viewId = editText.getId();
        int units = unitOfMeasure.getNumberOfMeasurementUnits();
        int digitsAfterDecimal = (int) unitOfMeasure.getInputDigitsFilter()[0].second;

        if (
                viewId == R.id.pack_editable_measurement_one && digitsAfterDecimal > 0 ||
                viewId == R.id.item_editable_measurement_one && digitsAfterDecimal > 0)

            setInputTypeNumberDecimal(editText);

        else setInputTypeNumber(editText);

        if (viewId != View.NO_ID)

            if (
                viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one ||
                viewId == R.id.pack_editable_measurement_two && units > 1 ||
                viewId == R.id.item_editable_measurement_two && units > 1 ||
                viewId == R.id.pack_editable_measurement_three && units > 2 ||
                viewId == R.id.item_editable_measurement_three && units > 2)

                setInputForSoftAndHardKeyboard(editText, unitOfMeasure);
    }

    private static void setInputForSoftAndHardKeyboard(EditText editText,
                                                       UnitOfMeasure unitOfMeasure) {
        setInputFilters(editText, unitOfMeasure);
    }

    private static void setInputTypeNumber(EditText editText) {

        editText.setInputType(TYPE_CLASS_NUMBER);
    }

    private static void setInputTypeNumberDecimal(EditText editText) {

        editText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
    }

    private static void setInputFilters(EditText editText,
                                        UnitOfMeasure unitOfMeasure) {

        int viewId = editText.getId();
        Pair[] inputDigitsFilters = unitOfMeasure.getInputDigitsFilter();


        if (
                viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one)

            editText.setFilters(new InputFilter[]{

                new DecimalDigitsInputFilter(
                        (int) inputDigitsFilters[0].first,
                        (int) inputDigitsFilters[0].second)});

        if (
                viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.item_editable_measurement_two) {

            editText.setFilters(new InputFilter[]{

                new DecimalDigitsInputFilter(
                        (int)inputDigitsFilters[1].first,
                        (int)inputDigitsFilters[1].second)});
        }

        if (
                viewId == R.id.pack_editable_measurement_three ||
                viewId == R.id.item_editable_measurement_three) {

            editText.setFilters(new InputFilter[]{

                new DecimalDigitsInputFilter(
                        (int)inputDigitsFilters[2].first,
                        (int)inputDigitsFilters[2].second)});

        }
    }
}