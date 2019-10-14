package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.view.View;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureFieldVisibilityBindingAdapter {

//    private static final String TAG = "tkm=FieldVisibility";

    @BindingAdapter(value = {"fieldVisibilityNumberOfProducts",
                    "fieldVisibilityNumberOfMeasurementUnits"}, requireAll = false)
    public static void fieldVisibility(View view,
                                       int numberOfProducts,
                                       int numberOfMeasurementUnits) {
        setViewVisibility(view, numberOfProducts, numberOfMeasurementUnits);
    }

    private static void setViewVisibility(View view,
                                          int numberOfProducts,
                                          int numberOfMeasurementUnits) {
        int viewId = view.getId();

        if (viewId == R.id.pack_editable_measurement_two ||
                viewId == R.id.pack_measurement_label_two) {
            view.setVisibility(numberOfMeasurementUnits > 1 && numberOfProducts > 1 ?
                    View.VISIBLE : View.GONE);
        }

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.pack_measurement_label_one ||
                viewId == R.id.pack_size_label) {
            view.setVisibility(numberOfProducts > 1 ? View.VISIBLE : View.GONE);
        }

        if (viewId == R.id.product_editable_measurement_two ||
                viewId == R.id.product_measurement_label_two ||
                viewId == R.id.recipe_ingredient_editable_measurement_two) {
            view.setVisibility(numberOfMeasurementUnits > 1 ? View.VISIBLE : View.GONE);
        }
    }
}