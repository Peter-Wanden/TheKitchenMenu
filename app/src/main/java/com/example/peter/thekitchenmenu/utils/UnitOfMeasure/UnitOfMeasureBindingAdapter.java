package com.example.peter.thekitchenmenu.utils.UnitOfMeasure;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.utils.DecimalDigitsInputFilter;
import com.example.peter.thekitchenmenu.utils.ShowHideSoftInput;

import androidx.core.util.Pair;
import androidx.databinding.BindingAdapter;

import static android.text.InputType.*;
import static com.example.peter.thekitchenmenu.utils.UnitOfMeasure.UnitOfMeasureConstants.*;

public class UnitOfMeasureBindingAdapter {

    private static final String TAG = "UnitOfMeasureBindingAda";

    private static final int SPINNER_NOTHING_SELECTED = 0;


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

                case R.id.total_or_unit:
                    setTotalOrUnitSeparator(view, isMultiPack);
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

    private static void setInputForSoftAndHardKeyboard(EditText packSize,
                                                       UnitOfMeasure newUnitOfMeasure) {

        setInputTypes(packSize, newUnitOfMeasure);
        setInputFilters(packSize, newUnitOfMeasure);
        packSize.requestFocusFromTouch();
        ShowHideSoftInput.showKeyboard(packSize, true);
    }

    private static void setInputTypes(EditText packSize, UnitOfMeasure newUnitOfMeasure) {

        if (newUnitOfMeasure.getUnitAsInt() == UNIT_GRAMS) {
            packSize.setInputType(TYPE_CLASS_NUMBER);

        } else {
            packSize.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL);
        }
    }

    private static void setInputFilters(EditText packSize, UnitOfMeasure newUnitOfMeasure) {
        Pair<Integer, Integer> inputFilterFormat = newUnitOfMeasure.getInputFilterFormat();

        if (inputFilterFormat.first != null && inputFilterFormat.second != null)
            packSize.setFilters(new InputFilter[]
                    {new DecimalDigitsInputFilter(inputFilterFormat.first, inputFilterFormat.second)});
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
        setInputForSoftAndHardKeyboard(packSize, newUnitOfMeasure);

        String rawInputMeasurement = packSize.getText().toString();
        double inputMeasurement;

        if (!rawInputMeasurement.isEmpty()) {

            try {
                inputMeasurement = Double.parseDouble(rawInputMeasurement);

            } catch (NumberFormatException e) {
                inputMeasurement = NO_INPUT;

                packSize.setText(String.valueOf(inputMeasurement));
                packSize.setError(packSize.getContext().getString(R.string.invalid_pack_size));
            }

            if (inputMeasurement > NO_INPUT) {

                oldUnitOfMeasure.setMeasurement(inputMeasurement);
                boolean canConvert = convertToNewUnitOfMeasurement(
                        oldUnitOfMeasure,
                        newUnitOfMeasure);

                if (canConvert) {

                    if (newUnitOfMeasure.getUnitAsInt() == UNIT_GRAMS) {
                        packSize.setText(String.valueOf((int) newUnitOfMeasure.getMeasurement()));
                        return;

                    } else {
                        packSize.setText(String.valueOf(newUnitOfMeasure.getMeasurement()));
                        return;
                    }
                }
            }
        }
        resetIfInconvertibleOrNoInput(packSize, newUnitOfMeasure);
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
