package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;

import java.util.Arrays;

import androidx.databinding.BindingAdapter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NO_INPUT;

public class UnitOfMeasureBindingAdapter {

    private static final String TAG = "UnitOfMeasureBindingAda";

    @BindingAdapter(value = {"onUnitOfMeasureSelected", "multiPack"}, requireAll = false)
    public static void onUnitOfMeasureSelected(View view,
                                               MeasurementSubType measurementSubType,
                                               boolean isMultiPack) {

        Log.d(TAG, "onUnitOfMeasureSelected: measurement sub type: " + measurementSubType.name());

        if (isVisible(view, measurementSubType)) {
            setupViews(view, measurementSubType, isMultiPack);
        }
    }

    private static boolean isVisible(View view, MeasurementSubType measurementSubType) {

        if (measurementSubType == MeasurementSubType.NOTHING_SELECTED) {
            view.setVisibility(View.GONE);
            return false;

        } else {
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private static void setupViews(View view,
                                   MeasurementSubType measurementSubType,
                                   boolean isMultiPack) {

        Context viewContext = view.getContext();

        UnitOfMeasure unitOfMeasure = UnitOfMeasureClassSelector.
                getClassWithSubType(viewContext, measurementSubType);

        int viewId = view.getId();

        if (viewId != View.NO_ID && view.getVisibility() == View.VISIBLE) {

            switch (viewId) {

                case R.id.pack_size_label:
                    setPackSizeLabel(view, unitOfMeasure);
                    break;

                case R.id.pack_editable_measurement_one:
                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_measurement_label_one:
                    setPackMeasurementLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_editable_measurement_two:
                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_measurement_label_two:
                    setPackMeasurementLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_size_label:
                    setItemSizeLabel(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_editable_measurement_two:
                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_measurement_label_two:
                    setPackMeasurementLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_editable_measurement_one:
                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_measurement_label_one:
                    setPackMeasurementLabels(view, unitOfMeasure, isMultiPack);
                    break;
            }
        }
    }

    private static void setPackSizeLabel(View view, UnitOfMeasure unitOfMeasure) {
        TextView packSize = (TextView) view;
        packSize.setText(view.getContext().getString(
                R.string.pack_size_total, unitOfMeasure.getTypeAsString()));
    }

    private static void setupPackEditableMeasurements(View view,
                                                      UnitOfMeasure newUnitOfMeasure,
                                                      boolean isMultiPack) {

        EditText editText = (EditText) view;
        int viewId = editText.getId();

        if (!isMultiPack && (viewId == R.id.item_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_two)) {

            editText.setVisibility(View.GONE);
            return;
        }

        setInputForSoftAndHardKeyboard(editText, newUnitOfMeasure);

        String rawInputMeasurement = editText.getText().toString();
        int inputMeasurement = NO_INPUT;

        if (!rawInputMeasurement.isEmpty()) {

            try {

                inputMeasurement = Integer.parseInt(rawInputMeasurement);

                if (inputMeasurement == NO_INPUT) editText.setText("");


            } catch (NumberFormatException e) {

                editText.setText(String.valueOf(inputMeasurement));
            }
        }
    }

    private static void setInputForSoftAndHardKeyboard(EditText editText,
                                                       UnitOfMeasure unitOfMeasure) {
        setInputType(editText);
        setInputFilters(editText, unitOfMeasure);
        // ToDo - Which view should request focus when there are no values and where there are values?
        editText.requestFocusFromTouch();
        ShowHideSoftInput.showKeyboard(editText, true);
    }

    private static void setInputType(EditText editText) {
        editText.setInputType(TYPE_CLASS_NUMBER);
    }

    private static void setInputFilters(EditText editText, UnitOfMeasure unitOfMeasure) {

        int viewId = editText.getId();
        int[] inputFilterFormat = unitOfMeasure.getInputFilterFormat();

        Log.d(TAG, "setInputFilters: " + Arrays.toString(inputFilterFormat));

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one) {

            editText.setFilters(new InputFilter[] {
                    new DecimalDigitsInputFilter(inputFilterFormat[1], 0)});
        }

        if (viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.item_editable_measurement_two) {

            editText.setFilters(new InputFilter[] {
                    new DecimalDigitsInputFilter(inputFilterFormat[2], 0)});
        }
    }

    private static void setPackMeasurementLabels(View view,
                                                 UnitOfMeasure newUnitOfMeasure,
                                                 boolean isMultiPack) {

        int viewId = view.getId();
        TextView textView = (TextView) view;

        if (!isMultiPack && (viewId == R.id.item_measurement_label_one ||
                viewId == R.id.item_measurement_label_two)) {

            textView.setVisibility(View.GONE);
            return;
        }

        if (viewId == R.id.pack_measurement_label_one ||
                viewId == R.id.item_measurement_label_one) {

            textView.setText(newUnitOfMeasure.getMeasurementUnitOne());

        } else if (viewId == R.id.pack_measurement_label_two ||
                viewId == R.id.item_measurement_label_two) {

            textView.setText(newUnitOfMeasure.getMeasurementUnitTwo());
        }
    }

    private static void setItemSizeLabel(View view,
                                         UnitOfMeasure unitOfMeasure,
                                         boolean isMultiPack) {
        if (isMultiPack) {
            TextView itemSize = (TextView) view;
            itemSize.setText(view.getContext().getString(
                    R.string.single_pack_size, unitOfMeasure.getTypeAsString()));

        } else {
            view.setVisibility(View.GONE);
        }
    }
}
