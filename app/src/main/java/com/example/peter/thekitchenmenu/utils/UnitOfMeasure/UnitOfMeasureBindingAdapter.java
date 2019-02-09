package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;

import androidx.databinding.BindingAdapter;

import static android.text.InputType.*;
import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class UnitOfMeasureBindingAdapter {

    private static final String TAG = "UnitOfMeasureBindingAda";

    private static final int SPINNER_NOTHING_SELECTED = 0;
    private static final int NO_INPUT_TYPE = 0;
    private static final int NO_INPUT_TYPE_FLAG = 0;


    @BindingAdapter(value = {"app:onUnitOfMeasureSelected", "app:multiPack"}, requireAll = false)
    public static void onUnitOfMeasureSelected(View view,
                                               int oldUnitOfMeasureSelected,
                                               boolean oldIsMultiPack,
                                               int newUnitOfMeasureSelected,
                                               boolean newIsMultiPack) {

        boolean isVisible = setViewVisibility(view, newUnitOfMeasureSelected);

        if (isVisible) {
            setupViews(view, oldUnitOfMeasureSelected, newUnitOfMeasureSelected, newIsMultiPack);
        }
    }

    private static boolean setViewVisibility(View view, int newUnitOfMeasureSelected) {

        if (newUnitOfMeasureSelected == SPINNER_NOTHING_SELECTED) {
            view.setVisibility(View.GONE);
            return false;

        } else {
            view.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private static void setupViews(View view,
                                   int oldUnitOfMeasureSelected,
                                   int newUnitOfMeasureSelected,
                                   boolean isMultiPack) {

        Context viewContext = view.getContext();

        UnitOfMeasure oldUnitOfMeasure = UnitOfMeasureClassSelector.
                getUnitOfMeasureClass(viewContext, oldUnitOfMeasureSelected);

        UnitOfMeasure newUnitOfMeasure = UnitOfMeasureClassSelector.
                getUnitOfMeasureClass(viewContext, newUnitOfMeasureSelected);

        int viewId = view.getId();

        if (viewId != View.NO_ID) {

            switch (viewId) {

                case R.id.pack_size_label:
                    setPackSizeLabel(view, newUnitOfMeasure);
                    break;

                case R.id.editable_pack_size:
                    setPackSizeEditable(view, oldUnitOfMeasure, newUnitOfMeasure);
                    break;

                case R.id.item_size_label:
                    setItemSizeLabel(view, newUnitOfMeasure, isMultiPack);
                    break;

                case R.id.editable_item_size:
                    setItemSizeEditable(view, newUnitOfMeasure, isMultiPack);
                    break;
            }
        }
    }

    private static void setPackSizeLabel(View view, UnitOfMeasure unitOfMeasure) {
        TextView packSize = (TextView) view;
        packSize.setText(view.getContext().getString(
                R.string.pack_size_total, unitOfMeasure.getTypeAsString()));
    }

    private static void setPackSizeEditable(View view,
                                            UnitOfMeasure oldUnitOfMeasure,
                                            UnitOfMeasure newUnitOfMeasure) {

        EditText packSize = (EditText) view;
        String rawInputMeasurement = packSize.getText().toString();

        setInputForSoftKeyboard(packSize, newUnitOfMeasure);

        double inputMeasurement;

        if (rawInputMeasurement.isEmpty()) {
            inputMeasurement = NO_INPUT;

        } else {
            inputMeasurement = Double.parseDouble(rawInputMeasurement);
        }

        if (oldUnitOfMeasure.getTypeAsInt() == newUnitOfMeasure.getTypeAsInt()) {

            if (inputMeasurement == NO_INPUT) {
                resetIfInconvertibleOrNoInput(packSize, newUnitOfMeasure);

            } else {

                oldUnitOfMeasure.setMeasurement(inputMeasurement);
                newUnitOfMeasure.setBaseSiUnits(oldUnitOfMeasure.getBaseSiUnits());

                double measurement = newUnitOfMeasure.getMeasurement();

                if (getInputTypeFlag(newUnitOfMeasure) == NO_INPUT_TYPE_FLAG) {

                    int measurementWithoutRemainder = (int) measurement;
                    packSize.setText(String.valueOf(measurementWithoutRemainder));

                } else {
                    packSize.setText(String.valueOf(newUnitOfMeasure.getMeasurement()));
                }
            }
        } else {
            resetIfInconvertibleOrNoInput(packSize, newUnitOfMeasure);
        }
    }

    private static void setInputForSoftKeyboard(EditText packSize, UnitOfMeasure newUnitOfMeasure) {
        packSize.requestFocusFromTouch();

        int inputType = getInputType(newUnitOfMeasure);
        int inputTypeFlag = getInputTypeFlag(newUnitOfMeasure);

        ShowHideSoftInput.showKeyboard(packSize, true);

        // For cardinal input
        if (inputTypeFlag == NO_INPUT_TYPE_FLAG) {

            packSize.setInputType(inputType);
            packSize.setFilters(new InputFilter[]
                    {new DecimalDigitsInputFilter(7, 0)});

            // For decimal input
        } else if (inputTypeFlag == TYPE_NUMBER_FLAG_DECIMAL) {

            packSize.setInputType(inputType | inputTypeFlag);
            packSize.setFilters(new InputFilter[]
                    {new DecimalDigitsInputFilter(4, 3)});
        }
    }

    private static int getInputType(UnitOfMeasure unitOfMeasure) {
        int unit = unitOfMeasure.getUnitAsInt();

        switch (unit) {
            case UNIT_GRAMS:
                return TYPE_CLASS_NUMBER;

            case UNIT_KILOGRAMS:
                return TYPE_CLASS_NUMBER;

            case UNIT_OUNCES:
                return TYPE_CLASS_NUMBER;

            default:
                return NO_INPUT_TYPE;
        }
    }

    private static int getInputTypeFlag(UnitOfMeasure unitOfMeasure) {
        int unit = unitOfMeasure.getUnitAsInt();

        switch (unit) {
            case UNIT_KILOGRAMS:
                return TYPE_NUMBER_FLAG_DECIMAL;

            case UNIT_OUNCES:
                return TYPE_NUMBER_FLAG_DECIMAL;

            default:
                return NO_INPUT_TYPE_FLAG;
        }
    }

    private static void resetIfInconvertibleOrNoInput(EditText packSize, UnitOfMeasure unitOfMeasure) {
        packSize.setText("");
        packSize.setHint(packSize.getContext().getString(
                R.string.pack_size_total_hint, unitOfMeasure.getUnitAsString()));
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
                    R.string.item_size_hint, unitOfMeasure.getUnitAsString()));

        } else {
            view.setVisibility(View.GONE);
        }
    }
}
