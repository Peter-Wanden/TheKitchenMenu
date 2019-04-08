package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.view.View;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureFieldVisibility {

    @BindingAdapter(value = {"setFieldVisibility"})
    public static void setFieldVisibility(View view,
                                          MeasurementSubType subType,
                                          int numberOfItems) {

        setupViews(view, subType, isMultiPack(numberOfItems));
    }

    private static boolean isMultiPack(int numberOfItems) {

        return numberOfItems > 1;
    }

    private static void setupViews(View view,
                                   MeasurementSubType subType,
                                   boolean multiPack) {

        Context context = view.getContext();

        UnitOfMeasure unitOfMeasure = UnitOfMeasureSubtypeSelector.
                getClassWithSubType(context, subType);

        int viewId = view.getId();
        int units = unitOfMeasure.getNumberOfMeasurementUnits();

        if (units == 1 && view.getVisibility() == View.VISIBLE) {

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
