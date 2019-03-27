package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;

import java.util.Arrays;

import androidx.databinding.BindingAdapter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.SINGLE_ITEM;

public class UnitOfMeasureBindingAdapter {

    private static final String TAG = "UnitOfMeasureBindingAda";

    @BindingAdapter(value = {"onUnitOfMeasureSelected", "numberOfItems"})
    public static void onUnitOfMeasureSelected(View view,
                                               MeasurementSubType measurementSubType,
                                               int numberOfItems) {

        Log.d(TAG, "onUnitOfMeasureSelected: View passed in is: " +
                view.getContext().getResources().getResourceEntryName(view.getId()));

        if (isVisible(view, measurementSubType)) {
            setupViews(view, measurementSubType, isMultiPack(numberOfItems));
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

    private static boolean isMultiPack(int numberOfItems) {
        return numberOfItems > SINGLE_ITEM;
    }

    private static void setupViews(View view,
                                   MeasurementSubType measurementSubType,
                                   boolean isMultiPack) {

        Context viewContext = view.getContext();

        UnitOfMeasure unitOfMeasure = UnitOfMeasureClassSelector.
                getClassWithSubType(viewContext, measurementSubType);


        Log.d(TAG, "setupViews: new unit of measure is: " +
                unitOfMeasure.getMeasurementSubType());

        int viewId = view.getId();


        if (viewId != View.NO_ID && view.getVisibility() == View.VISIBLE) {

            switch (viewId) {

                case R.id.pack_size_label:

                    setPackSizeLabel(view, unitOfMeasure);
                    break;

                case R.id.item_size_label:

                    setItemSizeLabel(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_editable_measurement_one:

                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_measurement_label_one:

                    setMeasurementUnitLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_editable_measurement_two:

                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.pack_measurement_label_two:

                    setMeasurementUnitLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_editable_measurement_two:

                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_measurement_label_two:

                    setMeasurementUnitLabels(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_editable_measurement_one:

                    setupPackEditableMeasurements(view, unitOfMeasure, isMultiPack);
                    break;

                case R.id.item_measurement_label_one:

                    setMeasurementUnitLabels(view, unitOfMeasure, isMultiPack);
                    break;
            }
        }
    }

    private static void setPackSizeLabel(View view, UnitOfMeasure unitOfMeasure) {

        TextView packSize = (TextView) view;
        packSize.setText(view.getContext().getString(
                R.string.pack_size_total, unitOfMeasure.getTypeAsString()));
    }

    private static void setItemSizeLabel(View view,
                                         UnitOfMeasure unitOfMeasure,
                                         boolean isMultiPack) {
        if (isMultiPack) {

            TextView itemSize = (TextView) view;
            itemSize.setText(view.getContext().getString(
                    R.string.item_size_label, unitOfMeasure.getTypeAsString()));

        } else {

            view.setVisibility(View.INVISIBLE);
        }
    }

    private static void setupPackEditableMeasurements(View view,
                                                      UnitOfMeasure newUnitOfMeasure,
                                                      boolean isMultiPack) {

        EditText editText = (EditText) view;
        int viewId = editText.getId();

        if (!isMultiPack && (

                viewId == R.id.item_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_two ||
                viewId == R.id.item_editable_measurement_three)) {

            editText.setVisibility(View.GONE);

            return;
        }

        setInputForSoftAndHardKeyboard(editText, newUnitOfMeasure);
    }

    private static void setInputForSoftAndHardKeyboard(EditText editText,
                                                       UnitOfMeasure unitOfMeasure) {
        setInputType(editText);
        setInputFilters(editText, unitOfMeasure);
    }

    private static void setInputType(EditText editText) {

        editText.setInputType(TYPE_CLASS_NUMBER);
    }

    private static void setInputFilters(EditText editText, UnitOfMeasure unitOfMeasure) {

        int viewId = editText.getId();
        int[] inputFilterFormat = unitOfMeasure.getInputFilterFormat();

        Log.d(TAG, "setInputFilters: " + Arrays.toString(inputFilterFormat));

        if (
                viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.item_editable_measurement_one) {

            editText.setFilters(

                    new InputFilter[] {
                    new DecimalDigitsInputFilter(inputFilterFormat[1], 0)});
        }

        if (
                viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.item_editable_measurement_two) {

            editText.setFilters(
                    new InputFilter[] {
                    new DecimalDigitsInputFilter(inputFilterFormat[2], 0)});
        }

        if (
                viewId == R.id.pack_editable_measurement_three ||
                viewId == R.id.item_editable_measurement_three) {

            editText.setFilters(
                    new InputFilter[] {
                    new DecimalDigitsInputFilter(inputFilterFormat[3], 0)});

        }
    }

    private static void setMeasurementUnitLabels(View view,
                                                 UnitOfMeasure newUnitOfMeasure,
                                                 boolean isMultiPack) {

        int viewId = view.getId();
        TextView textView = (TextView) view;

        if (!isMultiPack && (

                viewId == R.id.item_measurement_label_one ||
                viewId == R.id.item_measurement_label_two ||
                viewId == R.id.item_measurement_label_three )) {

            textView.setVisibility(View.GONE);

            return;
        }

        if (

                viewId == R.id.pack_measurement_label_one ||
                viewId == R.id.item_measurement_label_one) {

            textView.setText(newUnitOfMeasure.getMeasurementUnitOne());

        } else if (

                viewId == R.id.pack_measurement_label_two ||
                viewId == R.id.item_measurement_label_two) {

            textView.setText(newUnitOfMeasure.getMeasurementUnitTwo());

        } else if (

                viewId == R.id.pack_measurement_label_three ||
                viewId == R.id.item_measurement_label_three) {

            textView.setText(newUnitOfMeasure.getMeasurementUnitThree());
        }
    }
}
