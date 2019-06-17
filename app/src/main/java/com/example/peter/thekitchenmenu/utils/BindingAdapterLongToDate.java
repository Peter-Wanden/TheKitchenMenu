package com.example.peter.thekitchenmenu.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

import java.text.DateFormat;

public class BindingAdapterLongToDate {
    @BindingAdapter("longToDate")
    public static void longToDate(TextView dateView, long date) {
        dateView.setText(DateFormat.getDateInstance().format(date));
    }

    @BindingAdapter(value = {"getMeasurementUnit", "getMeasurement"})
    public static void getMeasurementUnit(TextView view, int unitOfMeasureSubtype, double size) {
        UnitOfMeasure unitOfMeasure = MeasurementSubtype.values()[unitOfMeasureSubtype].getMeasurementClass();

    }
}
