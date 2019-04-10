package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;

import androidx.databinding.BindingAdapter;

import static com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasureConstants.SINGLE_ITEM;

public class UnitOfMeasureLabelBindingAdapter {

    private static final String TAG = "UnitOfMeasureLabelBindi";

    @BindingAdapter(value = {"setLabelForSubTypeSelected", "numberOfItems"})
    public static void setLabelForSubTypeSelected(TextView textView,
                                                  MeasurementSubType subType,
                                                  int numberOfItems) {

        setUpViewLabels(textView, subType, isMultiPack(numberOfItems));
    }

    private static boolean isMultiPack(int numberOfItems) {

        return numberOfItems > SINGLE_ITEM;
    }

    private static void setUpViewLabels(TextView textView,
                                        MeasurementSubType subType,
                                        boolean isMultiPack) {

        Context context = textView.getContext();

        UnitOfMeasure unitOfMeasure = UnitOfMeasureSubtypeSelector.
                getClassWithSubType(context, subType);

        int units = unitOfMeasure.getNumberOfMeasurementUnits();
        int viewId = textView.getId();

        if (viewId != View.NO_ID && textView.getVisibility() == View.VISIBLE) {

            if (viewId == R.id.pack_size_label)
                setPackSizeLabel(textView, unitOfMeasure);

            if (viewId == R.id.item_size_label && isMultiPack)
                setItemSizeLabel(textView, unitOfMeasure);

            if (
                    viewId == R.id.pack_measurement_label_one ||
                    viewId == R.id.item_measurement_label_one && isMultiPack ||
                    viewId == R.id.pack_measurement_label_two && units > 1 ||
                    viewId == R.id.item_measurement_label_two && isMultiPack && units > 1 ||
                    viewId == R.id.pack_measurement_label_three && units > 2||
                    viewId == R.id.item_measurement_label_three && isMultiPack && units > 2)

            setMeasurementUnitLabels(textView, unitOfMeasure);
        }
    }

    private static void setPackSizeLabel(TextView textView, UnitOfMeasure unitOfMeasure) {

        textView.setText(textView.getContext().getString(
                R.string.pack_size_label, unitOfMeasure.getMeasurementTypeAsString()));
    }

    private static void setItemSizeLabel(TextView textView, UnitOfMeasure unitOfMeasure) {

        textView.setText(textView.getContext().getString(
                R.string.item_size_label, unitOfMeasure.getMeasurementTypeAsString()));
    }

    private static void setMeasurementUnitLabels(TextView textView, UnitOfMeasure unitOfMeasure) {

        int viewId = textView.getId();

        if (
                viewId == R.id.pack_measurement_label_one ||
                viewId == R.id.item_measurement_label_one)

            textView.setText(unitOfMeasure.getMeasurementUnitOneLabel());

        else if (

                viewId == R.id.pack_measurement_label_two ||
                viewId == R.id.item_measurement_label_two)

            textView.setText(unitOfMeasure.getMeasurementUnitTwoLabel());

        else if (

                viewId == R.id.pack_measurement_label_three ||
                viewId == R.id.item_measurement_label_three)

            textView.setText(unitOfMeasure.getMeasurementUnitThreeLabel());
    }
}
