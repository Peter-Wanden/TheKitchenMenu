package com.example.peter.thekitchenmenu.utils;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.InverseMethod;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberStringConverter {
    private static final String TAG = "tkm-NumberStringConverter";

    @InverseMethod("toDouble")
    public static String toString(TextView view, double oldValue, double value) {
        NumberFormat numberFormat = getNumberFormat(view);
        double parsed = 0;
        try {
            Log.d(TAG, "toString: oldValue=" + oldValue + " value=" + value);
            // Don't return a different value if the parsed value doesn't change
            String inView = view.getText().toString();
            parsed = numberFormat.parse(inView).doubleValue();
            if (parsed == value) {
                if (parsed == 0) {
                    Log.d(TAG, "toString: returning empty string from tyrCatch");
                    return "";
                } else {
                    Log.d(TAG, "toString: returning from tryCatch=" + view.getText().toString());
                    return view.getText().toString();
                }
            }
        } catch (ParseException e) {
            // Old number was broken
        }
        String returnString = numberFormat.format(value);
        if (parsed == 0) {
            Log.d(TAG, "toString: parsed is 0, return string is:" + returnString + " returning empty string");
            return "";
        } else {
            Log.d(TAG, "toString: outsideTryCatch:" + returnString);
            return numberFormat.format(value);
        }
    }

    public static double toDouble(TextView view, double oldValue, String value) {
        NumberFormat numberFormat = getNumberFormat(view);
        Log.d(TAG, "toDouble: " + view.getResources().getResourceEntryName(view.getId()));
        Log.d(TAG, "toDouble: oldValue=" + oldValue + " newValue=" + value);
        if (value.isEmpty()) return 0;
        try {
            Log.d(TAG, "toDouble: fromTryCatch:" + numberFormat.parse(value).doubleValue());
            return numberFormat.parse(value).doubleValue();
        } catch (ParseException e) {
//            Resources resources = view.getResources();
            String errStr = "Please only enter numbers";
            view.setError(errStr);
            Log.d(TAG, "toDouble: errorInTryCatch, returning old value:" + oldValue);
            return oldValue;
        }
    }

    @SuppressWarnings("deprecation")
    private static NumberFormat getNumberFormat(View view) {
        Resources resources = view.getResources();
        Locale locale = resources.getConfiguration().locale;
        NumberFormat format =
                NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) format;
            decimalFormat.setGroupingUsed(false);
        }
        return format;
    }
}
