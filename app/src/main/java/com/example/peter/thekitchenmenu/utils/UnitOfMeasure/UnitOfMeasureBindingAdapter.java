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
//        Log.d(TAG, "onUnitOfMeasureSelected: old UoM: " + oldUnitOfMeasureSelected + " new UoM: " + newUnitOfMeasureSelected);
//        Log.d(TAG, "onUnitOfMeasureSelected: old multi pack: " + oldIsMultiPack + " new multi pack: " + newIsMultiPack);

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
        int viewId = view.getId();

        if (viewId != View.NO_ID) {
            UnitOfMeasure newUnitOfMeasure = UnitOfMeasureClassSelector.
                    getUnitOfMeasureClass(viewContext, newUnitOfMeasureSelected);

            UnitOfMeasure oldUnitOfMeasure = UnitOfMeasureClassSelector.
                    getUnitOfMeasureClass(viewContext, oldUnitOfMeasureSelected);

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

        Log.d(TAG, "setPackSizeEditable: new uom is: " + newUnitOfMeasure.getUnitAsInt());
        Log.d(TAG, "setPackSizeEditable: old uom is: " + oldUnitOfMeasure.getUnitAsInt());

        int inputType = getInputType(newUnitOfMeasure);
        int inputTypeFlag = getInputTypeFlag(newUnitOfMeasure);

        packSize.requestFocusFromTouch();
        ShowHideSoftInput.showKeyboard(view, true);
        String packSizeText = packSize.getText().toString();

        // For cardinal input
        if (inputTypeFlag == NO_INPUT_TYPE_FLAG) {
            packSize.setInputType(inputType);
            packSize.setFilters(new InputFilter[]
                    {new DecimalDigitsInputFilter(7, 0)});

            if (packSizeText.isEmpty()) {
                resetValueIfZeroOrInconvertible(packSize, newUnitOfMeasure);

            } else {
                // If old and new have inconvertible types
                if (oldUnitOfMeasure.getTypeAsInt() != newUnitOfMeasure.getTypeAsInt()) {
                    resetValueIfZeroOrInconvertible(packSize, newUnitOfMeasure);
                    return;
                }

                int convertedValue;

                if (oldUnitOfMeasure.getUnitAsInt() != newUnitOfMeasure.getUnitAsInt()) {
                    convertedValue = oldUnitOfMeasure.convertToInt(newUnitOfMeasure, packSizeText);

                } else {
                    convertedValue = Integer.parseInt(packSizeText);
                }

                packSizeText = String.valueOf(convertedValue);
                packSize.setText(packSizeText);

                if (Integer.parseInt(packSizeText) == NO_INPUT) {
                    Log.d(TAG, "setPackSizeEditable: is this reached");
                    resetValueIfZeroOrInconvertible(packSize, newUnitOfMeasure);
                }
            }
            // For decimal input
        } else if (inputTypeFlag == TYPE_NUMBER_FLAG_DECIMAL) {
            Log.d(TAG, "setPackSizeEditable: is decimal input");
            packSize.setInputType(inputType | inputTypeFlag);
            packSize.setFilters(new InputFilter[]
                    {new DecimalDigitsInputFilter(4, 3)});

            if (packSizeText.isEmpty()) {
                resetValueIfZeroOrInconvertible(packSize, newUnitOfMeasure);

            } else {

                if (oldUnitOfMeasure.getTypeAsInt() != newUnitOfMeasure.getTypeAsInt()) {
                    resetValueIfZeroOrInconvertible(packSize, newUnitOfMeasure);
                    return;
                }

                double convertedValue;

                if (oldUnitOfMeasure.getUnitAsInt() != newUnitOfMeasure.getUnitAsInt()) {
                    convertedValue = oldUnitOfMeasure.convertToDouble(newUnitOfMeasure, packSizeText);

                } else {
                    convertedValue = Double.parseDouble(packSizeText);
                }

                packSizeText = String.valueOf(convertedValue);
                packSize.setText(packSizeText);

                if (Double.parseDouble(packSizeText) == NO_INPUT) {
                    resetValueIfZeroOrInconvertible(packSize, newUnitOfMeasure);
                }
            }
        }
    }

    private static void resetValueIfZeroOrInconvertible(EditText packSize, UnitOfMeasure unitOfMeasure) {
        packSize.setText("");
        packSize.setHint(packSize.getContext().getString(
                R.string.pack_size_total_hint, unitOfMeasure.getUnitAsString()));
    }

    private static int getInputType(UnitOfMeasure unitOfMeasure) {
        int unit = unitOfMeasure.getUnitAsInt();

        switch (unit) {
            case UNIT_GRAMS:
                return TYPE_CLASS_NUMBER;

            case UNIT_KILOGRAMS:
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

            default:
                return NO_INPUT_TYPE_FLAG;
        }
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
