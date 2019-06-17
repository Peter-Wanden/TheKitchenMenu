package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.util.Log;
import android.view.View;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureFieldVisibilityBindingAdapter {

    private static final String TAG = "tkm-FieldVisibility";

    @BindingAdapter(value = {"fieldVisibilityAdapterNumberOfProducts"})
    public static void fieldVisibility(View view, int numberOfProducts) {
        Log.d(TAG, "fieldVisibility: view=" + view.getResources().getResourceEntryName(view.getId()));
        Log.d(TAG, "fieldVisibility: noOfProducts=" + numberOfProducts);

        setViewVisibility(view, numberOfProducts);
    }

    private static void setViewVisibility(View view, int numberOfProducts) {
        int viewId = view.getId();

        if (    viewId == R.id.product_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_two ||
                viewId == R.id.product_measurement_label_one ||
                viewId == R.id.product_measurement_label_two) {

            view.setVisibility(numberOfProducts > 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
