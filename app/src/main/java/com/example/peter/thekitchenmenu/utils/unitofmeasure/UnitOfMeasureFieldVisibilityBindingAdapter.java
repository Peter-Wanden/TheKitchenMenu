package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.view.View;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureFieldVisibilityBindingAdapter {

    private static final String TAG = "UnitOfMeasureFieldVisib";

    @BindingAdapter(
            value = {"fieldVisibilityAdapterNumberOfUnits", "fieldVisibilityAdapterNumberOfItems"},
            requireAll = false)
    public static void fieldVisibility(View view, int numberOfUnits, int numberOfItems) {
        setViewVisibility(view, numberOfUnits, numberOfItems);
    }

    private static void setViewVisibility(View view, int units, int numberOfItems) {

        int viewId = view.getId();

        if (
                viewId == R.id.item_editable_measurement_one ||
                viewId == R.id.item_measurement_label_one ||
                viewId == R.id.item_editable_measurement_two ||
                viewId == R.id.item_measurement_label_two) {

            view.setVisibility(numberOfItems >= 2 ? View.VISIBLE : View.INVISIBLE);
        }

        if (
                viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.pack_measurement_label_two ||
                viewId == R.id.item_editable_measurement_two ||
                viewId == R.id.item_measurement_label_two)

            view.setVisibility(units == 2 ? View.VISIBLE : View.INVISIBLE);
    }
}
