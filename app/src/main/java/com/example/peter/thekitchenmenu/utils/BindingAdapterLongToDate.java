package com.example.peter.thekitchenmenu.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class BindingAdapterLongToDate {
    @BindingAdapter("longToDate")
    public static void longToDate(TextView dateView, long date) {
        Date dateToFormat = new Date(date);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        dateView.setText(dateFormat.format(dateToFormat));
    }
}
