package com.example.peter.thekitchenmenu.utils;

import android.content.res.Resources;
import android.os.Build;

import androidx.databinding.ObservableField;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ParseIntegerFromObservableHandler {

    private Resources resources;

    public int parseInt(Resources resources, ObservableField<String> observable, int oldValue) {
        this.resources = resources;
        NumberFormat numberformat = getNumberFormat();

        try {
            String intInObservable = observable.get();
            return numberformat.parse(intInObservable).intValue();

        } catch (ParseException e) {
            return oldValue;
        }
    }

    private NumberFormat getNumberFormat() {
        Locale locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = resources.getConfiguration().getLocales().get(0);
        } else {
            locale = resources.getConfiguration().locale;
        }

        NumberFormat format = NumberFormat.getNumberInstance(locale);

        if (format instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) format;
            decimalFormat.setGroupingUsed(false);
        }
        return format;
    }
}
