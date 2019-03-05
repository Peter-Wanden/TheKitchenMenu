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

public class UnitOfMeasureBindingAdapter {

    private static final String TAG = "UnitOfMeasureBindingAda";

    @BindingAdapter(value = {"app:onUnitOfMeasureSelected", "app:multiPack", "app:baseSiUnits"},
            requireAll = false)
    public static void onUnitOfMeasureSelected(View view,
                                               MeasurementSubType measurementSubType,
                                               boolean isMultiPack,
                                               double packSize) {

        Log.d(TAG, "onUnitOfMeasureSelected: measurement sub type: " + measurementSubType.name());
        Log.d(TAG, "onUnitOfMeasureSelected: pack size is: " + packSize);

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

                case R.id.pack_label_measurement_one:
                    setPackMeasurementLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_editable_measurement_two:
                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_label_measurement_two:
                    setPackMeasurementLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.total_or_unit:
                    setTotalOrUnitSeparator(view, isMultiPack);
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
        int inputMeasurement;

//        if (!rawInputMeasurement.isEmpty()) {
//
//            try {
//                inputMeasurement = Integer.parseInt(rawInputMeasurement);
//
//            } catch (NumberFormatException e) {
//                inputMeasurement = NO_INPUT;
//
//                editText.setText(String.valueOf(inputMeasurement));
//                editText.setError(editText.getContext().getString(R.string.invalid_pack_size));
//            }
//
//            if (inputMeasurement > NO_INPUT) {
//
//                List<MeasurementUnits> oldUnitOfMeasureUnits = oldUnitOfMeasure.getUnits();
//
//                HashMap<MeasurementUnits, Integer> measurement = new HashMap<>();
//                measurement.put(oldUnitOfMeasureUnits.get(1), inputMeasurement);
//                measurement.put(oldUnitOfMeasureUnits.get(2), 0);
//                boolean isSet = oldUnitOfMeasure.setPackMeasurement(measurement);
//
//                if (isSet) Log.d(TAG, "setupPackEditableMeasurements: old measurement set successfully");
//                else Log.d(TAG, "setupPackEditableMeasurements: old measurement failed to set");
//
//                boolean canConvert = convertToNewUnitOfMeasurement(
//                        oldUnitOfMeasure,
//                        newUnitOfMeasure);
//
//                if (canConvert) {
//
////                    if (newUnitOfMeasure.getUnitAsInt() == UNIT_GRAMS) {
////                        editText.setText(String.valueOf((int) newUnitOfMeasure.getMeasurement()));
////                        return;
////
////                    } else {
////                        editText.setText(String.valueOf(newUnitOfMeasure.getMeasurement()));
////                        return;
////                    }
//                }
//            }
//        }
//        resetIfInconvertibleOrNoInput(editText, newUnitOfMeasure);
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

    private static boolean convertToNewUnitOfMeasurement(
            UnitOfMeasure oldUnitOfMeasure,
            UnitOfMeasure newUnitOfMeasure) {

        if (oldUnitOfMeasure.getType() != newUnitOfMeasure.getType()) return false;
        newUnitOfMeasure.setBaseSiUnits(oldUnitOfMeasure.getBaseSiUnits());

        return true;
    }

    private static void resetIfInconvertibleOrNoInput(EditText packSize,
                                                      UnitOfMeasure unitOfMeasure) {
        packSize.setText("");
        packSize.setHint(packSize.getContext().getString(
                R.string.pack_size_total_hint, unitOfMeasure.getUnitsAsString()));
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

        String[] measurementUnits = newUnitOfMeasure.getUnitsAsString();

        if (viewId == R.id.pack_label_measurement_one ||
                viewId == R.id.item_measurement_label_one) {

            textView.setText(measurementUnits[1]);

        } else if (viewId == R.id.pack_label_measurement_two ||
                viewId == R.id.item_measurement_label_two) {

            textView.setText(measurementUnits[2]);
        }
    }

    private static void setTotalOrUnitSeparator(View view, boolean isMultiPack) {
        if (!isMultiPack) view.setVisibility(View.GONE);
    }

    private static void setItemSizeLabel(View view,
                                         UnitOfMeasure unitOfMeasure,
                                         boolean isMultiPack) {
        if (isMultiPack) {
            TextView itemSize = (TextView) view;
            itemSize.setText(view.getContext().getString(
                    R.string.item_size, unitOfMeasure.getTypeAsString()));

        } else {
            view.setVisibility(View.GONE);
        }
    }

    private static void setItemSizeEditable(View view,
                                            UnitOfMeasure unitOfMeasure,
                                            boolean isMultiPack) {
        if (isMultiPack) {
            EditText itemSize = (EditText) view;
            itemSize.setHint(view.getContext().getString(
                    R.string.item_size_hint, unitOfMeasure.getUnitsAsString()));

        } else {
            view.setVisibility(View.GONE);
        }
    }
}
