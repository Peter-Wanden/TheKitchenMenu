package com.example.peter.thekitchenmenu.utils;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableInt;

public class BindingAdapters {

    private static final String TAG = "BindingAdapters";

    @BindingAdapter("app:showWhenChecked")
    public static void showWhenChecked(View view, Boolean checked) {
        Log.d(TAG, "showWhenChecked: checked is:" + checked);
        view.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("app:showIfFood")
    public static void showIfFood(View view, int category) {
        final int FOOD = 1;

        if (category == FOOD) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
