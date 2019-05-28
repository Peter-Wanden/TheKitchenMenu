package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureLabelBindingAdapter {

    private static final String TAG = "tkm-UomLabelBinding";

    @BindingAdapter(value = {"setLabelForSubtypeSelected"})
    public static void setLabelForSubtypeSelected(TextView textView, MeasurementSubtype subtype) {
        setUpViewLabels(textView, subtype);
    }

    private static void setUpViewLabels(TextView textView, MeasurementSubtype subtype) {
        UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
        int viewId = textView.getId();

        if (viewId != View.NO_ID) {
            if (viewId == R.id.pack_size_label)
                setPackSizeLabel(textView, unitOfMeasure);

            if (viewId == R.id.item_size_label)
                setItemSizeLabel(textView, unitOfMeasure);

            if (viewId == R.id.pack_measurement_label_one ||
                    viewId == R.id.item_measurement_label_one ||
                    viewId == R.id.pack_measurement_label_two ||
                    viewId == R.id.item_measurement_label_two)
                setMeasurementUnitLabels(textView, unitOfMeasure);
        }
    }

    private static void setPackSizeLabel(TextView textView, UnitOfMeasure unitOfMeasure) {
        Resources resources = textView.getResources();
        textView.setText(textView.getContext().getString(
                R.string.pack_size_label,
                resources.getString(unitOfMeasure.getTypeStringResourceId())));
    }

    private static void setItemSizeLabel(TextView textView, UnitOfMeasure unitOfMeasure) {
        Resources resources = textView.getResources();
        textView.setText(textView.getContext().getString(
                R.string.item_size_label,
                resources.getString(unitOfMeasure.getTypeStringResourceId())));
    }

    private static void setMeasurementUnitLabels(TextView textView, UnitOfMeasure unitOfMeasure) {
        int viewId = textView.getId();
        if (viewId == R.id.pack_measurement_label_one ||
                viewId == R.id.item_measurement_label_one)
            textView.setText(unitOfMeasure.getUnitOneLabelStringResourceId());

        else if (viewId == R.id.pack_measurement_label_two ||
                viewId == R.id.item_measurement_label_two)
            textView.setText(unitOfMeasure.getUnitTwoLabelStringResourceId());
    }
}
