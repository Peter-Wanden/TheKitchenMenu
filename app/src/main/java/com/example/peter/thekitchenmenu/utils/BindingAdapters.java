package com.example.peter.thekitchenmenu.utils;

import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.MULTI_PACK_MAXIMUM_NO_OF_ITEMS;
import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.NO_INPUT;

public class BindingAdapters {

    private static final String TAG = "BindingAdapters";

    @BindingAdapter("app:showWhenChecked")
    public static void showWhenChecked(View view, Boolean checked) {

        view.setVisibility(checked ? View.VISIBLE : View.GONE);

        if (checked) {
            int viewId = view.getId();

            if (viewId != View.NO_ID) {

                if (viewId == R.id.editable_items_in_pack) {
                    EditText noOfItemsInPack = (EditText) view;
                    setupItemsInPackEditText(noOfItemsInPack, checked);
                }
            }
        }
    }

    private static void setupItemsInPackEditText(EditText itemsInPack, Boolean checked) {

        setInputFilter(itemsInPack);
        itemsInPack.requestFocus();
        ShowHideSoftInput.showKeyboard(itemsInPack, checked);

//        String rawInput = itemsInPack.getText().toString();
//        int numberOfItemsInPack;
//
//        if (!rawInput.isEmpty()) {
//
//            try {
//
//                numberOfItemsInPack = Integer.parseInt(rawInput);
//
//                if (numberOfItemsInPack == NO_INPUT) showHintIfNoInput(itemsInPack);
//
//            } catch (NumberFormatException e) {
//
//                showHintIfNoInput(itemsInPack);
//            }
//        }
    }

    private static void setInputFilter(EditText noOfItemsInPack) {

        int numberOfDigits = 0;
        int digits = MULTI_PACK_MAXIMUM_NO_OF_ITEMS;

        while(digits > 0) {
            numberOfDigits ++;
            digits = digits / 10;
        }

        noOfItemsInPack.setFilters(new InputFilter[] {
                new DecimalDigitsInputFilter(numberOfDigits, 0)});

        noOfItemsInPack.setEms(numberOfDigits);
    }

    private static void showHintIfNoInput(EditText noOfItemsInPack) {
        noOfItemsInPack.setText("");
    }

    @BindingAdapter("app:showIfFood")
    public static void showIfFood(View view, int category) {
        final int FOOD = 1;

        if (category == FOOD) {
            view.setVisibility(View.VISIBLE);
            int viewId = view.getId();

            if (viewId != View.NO_ID) {

                if (viewId == R.id.spinner_shelf_life) {

                    view.requestFocus();
                    // Avoids WindowManager$BadTokenException by waiting for the screen to redraw.
                    new Handler().postDelayed(view::performClick, 100);
                }
            }
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("app:nonFoodGetFocus")
    public static void nonFoodGetFocus(View view, int category) {
        final int NON_FOOD = 2;

        if (category == NON_FOOD) {

            int viewId = view.getId();

            if (viewId != View.NO_ID) {

                if (viewId == R.id.multi_pack_check_box) {
                    view.requestFocusFromTouch();
                }
            }

        }
    }

    @BindingAdapter("app:shelfLifeValueChange")
    public static void shelfLifeValueChange(View view, int shelfLife) {
        if (shelfLife != 0) {
            int viewId = view.getId();

            if (viewId != View.NO_ID) {

                if(viewId == R.id.multi_pack_check_box) {
                    view.requestFocusFromTouch();
                }
            }
        }
    }

    @BindingAdapter("app:formatCardinalOrDecimal")
    public static void formatCardinalOrDecimal(View v, int unitOfMeasure) {
        EditText editText = (EditText) v;
        Log.d(TAG, "formatCardinalOrDecimal: view text is: " + editText.getText().toString());
        Log.d(TAG, "formatCardinalOrDecimal: unit of measure is: " + unitOfMeasure);
    }
}
