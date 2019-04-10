package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.util.Log;
import android.view.View;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureFieldVisibilityBindingAdapter {

    private static final String TAG = "UnitOfMeasureFieldVisib";

    @BindingAdapter(value = {"setFieldVisibility"})
    public static void setFieldVisibility(View view, int numberOfMeasurementUnits) {

        Log.d(TAG, "zyx - setFieldVisibility: number of units: " + numberOfMeasurementUnits);

        setupViews(view, numberOfMeasurementUnits);
    }

    private static void setupViews(View view, int units) {

        int viewId = view.getId();

        if (units == 1 && view.getVisibility() == View.VISIBLE) {

            Log.d(TAG, "zyx - setupViews: ");

            if (
                    viewId == R.id.pack_measurement_label_two ||
                    viewId == R.id.pack_editable_measurement_two ||
                    viewId == R.id.item_measurement_label_two ||
                    viewId == R.id.item_editable_measurement_two ||
                    viewId == R.id.pack_measurement_label_three ||
                    viewId == R.id.pack_editable_measurement_three ||
                    viewId == R.id.item_measurement_label_three ||
                    viewId == R.id.item_editable_measurement_three) {

                view.setVisibility(View.INVISIBLE);
            }
        }

        if (units == 2 && view.getVisibility() == View.VISIBLE) {

            if (
                    viewId == R.id.pack_measurement_label_three ||
                    viewId == R.id.pack_editable_measurement_three ||
                    viewId == R.id.item_measurement_label_three ||
                    viewId == R.id.item_editable_measurement_three) {

                view.setVisibility(View.INVISIBLE);
            }
        }
    }
}
