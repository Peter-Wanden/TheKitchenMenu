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
                                               int unitOfMeasureSelected,
                                               boolean isMultiPack) {
        Context viewContext = view.getContext();

        if (unitOfMeasureSelected == SPINNER_NOTHING_SELECTED) {
            view.setVisibility(View.GONE);

        } else {
            view.setVisibility(View.VISIBLE);
            int viewId = view.getId();

            if (viewId != View.NO_ID) {
                UnitOfMeasure unitOfMeasure = UnitOfMeasureClassSelector.
                        getUnitOfMeasureClass(viewContext, unitOfMeasureSelected);

                switch (viewId) {

                    case R.id.pack_size_label:
                        setPackSizeLabel(view, unitOfMeasure);
                        break;

                    case R.id.editable_pack_size:
                        setPackSizeEditable(view, unitOfMeasure);
                        break;

                    case R.id.item_size_label:
                        setItemSizeLabel(view, unitOfMeasure, isMultiPack);
                        break;

                    case R.id.editable_item_size:
                        setItemSizeEditable(view, unitOfMeasure, isMultiPack);
                        break;
                }
            }
        }
    }

    private static void setPackSizeEditable(View view, UnitOfMeasure unitOfMeasure) {
        EditText packSize = (EditText) view;

        if (packSize.getText().toString().isEmpty()) {
            showHintIfZero(packSize, unitOfMeasure);
        }

        packSize.requestFocusFromTouch();
        ShowHideSoftInput.showKeyboard(view, true);

        int inputType = getInputType(unitOfMeasure);
        int inputTypeFlag = getInputTypeFlag(unitOfMeasure);

        // For cardinal input
        if (inputTypeFlag == NO_INPUT_TYPE_FLAG) {
            packSize.setInputType(inputType);
            packSize.setFilters(new InputFilter[]{});

            String packSizeText = packSize.getText().toString();
            String decimalRemoved;

            // When user changes from decimal to cardinal
            if (packSizeText.contains(".")) {
                decimalRemoved = (convertToCardinal(packSizeText, unitOfMeasure));
                packSizeText = decimalRemoved;
                packSize.setText(packSizeText);
            }

            if (Integer.parseInt(packSizeText) == 0) {
                showHintIfZero(packSize, unitOfMeasure);
            }

        // For decimal input
        } else if (inputTypeFlag == TYPE_NUMBER_FLAG_DECIMAL) {
            packSize.setInputType(inputType | inputTypeFlag);
            packSize.setFilters(new InputFilter[]{new
                    DecimalDigitsInputFilter(4, 3)});
        }
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

    private static void showHintIfZero(EditText packSize, UnitOfMeasure unitOfMeasure) {
        packSize.setText("");
        packSize.setHint(packSize.getContext().getString(
                R.string.pack_size_total_hint, unitOfMeasure.getUnitAsString()));
    }

    private static String convertToCardinal(String packSizeText, UnitOfMeasure unitOfMeasure) {
        return String.valueOf(unitOfMeasure.convertValueToBaseSiUnit(packSizeText));
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
