package com.example.peter.thekitchenmenu.utils;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.InverseMethod;

import com.example.peter.thekitchenmenu.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class IntegerStringFormatConverter {

    private static final String TAG = "IntegerStringFormatConv";

    @InverseMethod("stringToInteger")
    public static String integerToString(EditText editText, int oldValue, int measurementModelValue) {

        NumberFormat numberFormat = getNumberFormat(editText);

        try {

            // Don't return a different value if the parsed value doesn't change
            String numberInView = editText.getText().toString();

            int parsed = numberFormat.parse(numberInView).intValue();

            if (parsed == measurementModelValue) {

                if (parsed == 0) return "";
                else return editText.getText().toString();
            }

        } catch (ParseException e){

            // Number set from Product is broken
        }

        if (measurementModelValue == 0) return "";
        else return numberFormat.format(measurementModelValue);
    }

    public static int stringToInteger(EditText editText, int oldValue, String newValue) {

        NumberFormat numberFormat = getNumberFormat(editText);

        try {

            return numberFormat.parse(newValue).intValue();

        } catch (ParseException e) {

            Resources resources = editText.getResources();
            String errorString = resources.getString(R.string.number_format_exception);

            editText.setError(errorString);

            return oldValue;
        }
    }

    private static NumberFormat getNumberFormat(View view) {

        Resources resources = view.getResources();

        Locale locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            locale = resources.getConfiguration().getLocales().get(0);


        } else {

            locale = resources.getConfiguration().locale;
        }

        NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);

        return format;
    }
}
