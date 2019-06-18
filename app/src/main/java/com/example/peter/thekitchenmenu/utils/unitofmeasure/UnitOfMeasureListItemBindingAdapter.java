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
        Log.d(TAG, "setLabelForSubtypeInt: subtype=" + subtypeInt + " baseUnits=" + baseUnits);

        String measurement = getMeasurement(subtypeInt, baseUnits, view.getResources());
        setMeasurementToView(view, measurement);
    }

    private static String getMeasurement(int subtypeInt, double baseUnits, Resources resources) {
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.values()[subtypeInt].getMeasurementClass();
        boolean baseUnitsAreSet = unitOfMeasure.baseUnitsAreSet(baseUnits);
        int numberOfMeasurementUnits = unitOfMeasure.getNumberOfMeasurementUnits();

        if (baseUnitsAreSet) {
            if (numberOfMeasurementUnits == 1 || unitOfMeasure.getPackMeasurementTwo() == 0) {
                return measurementBasedOnUnitOne(unitOfMeasure, resources);

            } else if (numberOfMeasurementUnits == 2) {
                return measurementBasedOnTwoUnits(unitOfMeasure, resources);
            }
        }
        return "";
    }

    private static String measurementBasedOnUnitOne(UnitOfMeasure unitOfMeasure,
                                                    Resources resources) {
        return (int)unitOfMeasure.getBaseUnits() + " " +
                resources.getString(unitOfMeasure.getUnitOneLabelStringResourceId());
    }

    private static String measurementBasedOnTwoUnits(UnitOfMeasure unitOfMeasure,
                                                     Resources resources) {
        // TODO - Does this work for units of measure with a decimal? Also need to strip off the
        //  zeros!!! Or switch through the units of measure and have a case for each?
        return unitOfMeasure.getPackMeasurementTwo() + "." +
                (int)unitOfMeasure.getPackMeasurementOne() + " " +
                resources.getString(unitOfMeasure.getUnitTwoLabelStringResourceId());
    }

    private static void setMeasurementToView(TextView view, String measurement) {
        int viewId = view.getId();

        if (viewId == R.id.product_list_item_label_unit_of_measure) {
            view.setText(measurement);
        }
    }
}
