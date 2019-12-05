package com.example.peter.thekitchenmenu.ui.utils;

import android.content.res.Resources;
import android.os.Build;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    private Resources resources;
    private NumberFormat numberFormat;

    public NumberFormatter(Resources resources) {
        this.resources = resources;
        numberFormat = getNumberFormat();
    }

    public String formatDecimalForDisplay(double numberToFormat) {
        if (numberToFormat == 0) {
            return "";
        }
        return numberFormat.format(numberToFormat);
    }

    public String formatIntegerForDisplay(int numberToFormat) {
        if (numberToFormat == 0) {
            return "";
        }
        return numberFormat.format(numberToFormat);
    }

    @SuppressWarnings("deprecation")
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
