package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.R;

public class UnitOfMeasureListItemBindingAdapter {

    private static final String TAG = "tkm-UOMListItemAdapter";

    @BindingAdapter(value = {"setLabelForSubtypeInt", "formatBaseUnits"})
    public static void setLabelForSubtypeInt(TextView view, int subtypeInt, double baseUnits) {

        UnitOfMeasure unitOfMeasure = MeasurementSubtype.values()[subtypeInt].getMeasurementClass();
        boolean baseUnitsAreSet = unitOfMeasure.baseUnitsAreSet(baseUnits);
        Resources resources = view.getResources();
        String measurement = "";

        if (baseUnitsAreSet) {
            if (unitOfMeasure instanceof MetricMass || unitOfMeasure instanceof MetricVolume) {
                measurement = getMetricMeasurement(unitOfMeasure, resources);
            }
            if (unitOfMeasure instanceof ImperialMass || unitOfMeasure instanceof ImperialVolume) {
                measurement = getImperialMeasurement(unitOfMeasure, resources);
            }
        }
        setMeasurementToView(view, measurement);
    }

    private static String getMetricMeasurement(UnitOfMeasure unitOfMeasure, Resources resources) {
        StringBuilder measurement = new StringBuilder();
        double baseUnits = unitOfMeasure.getBaseUnits();

        if (baseUnits > 999) {
            measurement.
                    append(baseUnits / 1000).
                    append("").
                    append(resources.getString(
                            unitOfMeasure.getUnitTwoLabelStringResourceId()));
        } else {
            measurement.
                    append((int) baseUnits).
                    append("").
                    append(resources.getString(
                            unitOfMeasure.getUnitOneLabelStringResourceId()));
        }
        return measurement.toString();
    }

    private static String getImperialMeasurement(UnitOfMeasure unitOfMeasure, Resources resources) {
        StringBuilder measurement = new StringBuilder();

        if (unitOfMeasure.getPackMeasurementTwo() > 0) {
            measurement.
                    append(unitOfMeasure.getPackMeasurementTwo()).
                    append(resources.getString(unitOfMeasure.getUnitTwoLabelStringResourceId())).
                    append(" ");
        }

        if (unitOfMeasure.getPackMeasurementOne() > 0) {
            measurement.
                    append(unitOfMeasure.getPackMeasurementOne()).
                    append(resources.getString(unitOfMeasure.getUnitOneLabelStringResourceId()));
        }
        return measurement.toString();
    }

    private static void setMeasurementToView(TextView view, String measurement) {
        int viewId = view.getId();

        if (viewId == R.id.product_list_item_measurement ||
                viewId == R.id.product_viewer_size) {
            view.setText(measurement);
        }
    }
}