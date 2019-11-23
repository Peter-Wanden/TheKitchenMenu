package com.example.peter.thekitchenmenu.ui.bindingadapters.unitofmeasure;

import android.view.View;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureFieldVisibilityBindingAdapter {

    private static final String TAG = "tkm-FieldVisibility";

    @BindingAdapter(value = {
            "fieldVisibilityNumberOfProducts",
            "fieldVisibilityNumberOfMeasurementUnits"},
            requireAll = false)
    public static void fieldVisibility(View view,
                                       int numberOfItems,
                                       int numberOfMeasurementUnits) {
        setViewVisibility(view, numberOfItems, numberOfMeasurementUnits);
    }

    private static void setViewVisibility(View view,
                                          int numberOfItems,
                                          int numberOfMeasurementUnits) {
        int viewId = view.getId();

        if (viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.pack_measurement_label_two) {
            view.setVisibility(numberOfMeasurementUnits > 1 && numberOfItems > 1 ?
                    View.VISIBLE : View.GONE);
        }

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.pack_measurement_label_one ||
                viewId == R.id.pack_size_label) {
            view.setVisibility(numberOfItems > 1 ? View.VISIBLE : View.GONE);
        }

        if (viewId == R.id.product_editable_measurement_two ||
                viewId == R.id.product_measurement_label_two ||
                viewId == R.id.recipe_ingredient_editable_measurement_one) {
            view.setVisibility(numberOfMeasurementUnits > 1 ? View.VISIBLE : View.GONE);
        }
    }
}