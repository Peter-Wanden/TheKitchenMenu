package com.example.peter.thekitchenmenu.ui.bindingadapters.unitofmeasure;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.entity.unitofmeasure.UnitOfMeasure;

import androidx.databinding.BindingAdapter;

public class UnitOfMeasureLabelBindingAdapter {

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

            if (viewId == R.id.recipe_ingredient_size_label)
                setIngredientSizeLabel(textView, unitOfMeasure);

            if (viewId == R.id.product_size_label)
                setItemSizeLabel(textView, unitOfMeasure);

            if (viewId == R.id.pack_measurement_label_one ||
                    viewId == R.id.product_measurement_label_one ||
                    viewId == R.id.pack_measurement_label_two ||
                    viewId == R.id.product_measurement_label_two ||
                    viewId == R.id.recipe_ingredient_measurement_label_one ||
                    viewId == R.id.recipe_ingredient_measurement_label_two)
                setMeasurementUnitLabels(textView, unitOfMeasure);
        }
    }

    private static void setIngredientSizeLabel(TextView textView, UnitOfMeasure unitOfMeasure) {
        Resources resources = textView.getResources();
        textView.setText(
                resources.getString(R.string.ingredient_size_label,
                resources.getString(unitOfMeasure.getTypeStringResourceId())));
    }

    private static void setPackSizeLabel(TextView textView, UnitOfMeasure unitOfMeasure) {
        Resources resources = textView.getResources();
        textView.setText(
                resources.getString(R.string.pack_size_label,
                resources.getString(unitOfMeasure.getTypeStringResourceId())));
    }

    private static void setItemSizeLabel(TextView textView, UnitOfMeasure unitOfMeasure) {
        Resources resources = textView.getResources();
        textView.setText(
                resources.getString(R.string.product_size_label,
                resources.getString(unitOfMeasure.getTypeStringResourceId())));
    }

    private static void setMeasurementUnitLabels(TextView textView, UnitOfMeasure unitOfMeasure) {
        int viewId = textView.getId();
        if (viewId == R.id.pack_measurement_label_one ||
                viewId == R.id.product_measurement_label_one ||
                viewId == R.id.recipe_ingredient_measurement_label_one)
            textView.setText(unitOfMeasure.getUnitOneLabelResourceId());

        else if (viewId == R.id.pack_measurement_label_two ||
                viewId == R.id.product_measurement_label_two ||
                viewId == R.id.recipe_ingredient_measurement_label_two)
            textView.setText(unitOfMeasure.getUnitTwoLabelResourceId());
    }
}
