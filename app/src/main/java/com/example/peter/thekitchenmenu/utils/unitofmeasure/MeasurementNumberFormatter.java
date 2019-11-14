package com.example.peter.thekitchenmenu.utils.unitofmeasure;

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

public class MeasurementNumberFormatter {

    @InverseMethod("toReturn")
    public static String toFormat(EditText editText,
                                  MeasurementSubtype subtype,
                                  String oldValue,
                                  String measurementModelValue) {

        UnitOfMeasure unitOfMeasure = subtype.getMeasurementClass();
        int viewId = editText.getId();
        int digitsAfterDecimal = 0;

        if (viewId == R.id.pack_editable_measurement_one ||
                viewId == R.id.product_editable_measurement_one ||
                viewId == R.id.recipe_ingredient_editable_measurement_one)
            digitsAfterDecimal = (int) unitOfMeasure.getMaxUnitDigitWidths()[0].second;

        else if (viewId == R.id.recipe_ingredient_editable_conversion_factor) {
            digitsAfterDecimal = (int) unitOfMeasure.getMaxUnitDigitWidths()[2].second;
        }

        NumberFormat numberFormat = getNumberFormat(editText);

        if (digitsAfterDecimal == 0) {
            int parsedModelValue = 0;

            try {
                parsedModelValue = numberFormat.parse(measurementModelValue).intValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                // Don't return a different value if the parsed value doesn't change
                String numberInView = editText.getText().toString();
                int parsedInView = numberFormat.parse(numberInView).intValue();

                if (parsedInView == parsedModelValue) {
                    if (parsedInView == 0)
                        return "";
                    else
                        return editText.getText().toString();
                }
            } catch (ParseException e) {
                // Number is broken
            }

            if (parsedModelValue == 0)
                return "";
            else
                return numberFormat.format(parsedModelValue);

        } else {
            double parsedModelValue = 0.;

            try {
                parsedModelValue = numberFormat.parse(oldValue).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                // Don't return a different value if the parsed value doesn't change
                String numberInView = editText.getText().toString();
                double parsedInView = numberFormat.parse(numberInView).doubleValue();

                if (parsedInView == parsedModelValue) {
                    if (parsedInView == 0.0)
                        return "";
                    else
                        return editText.getText().toString();
                }
            } catch (ParseException e) {
                // Number is broken
            }

            if (parsedModelValue == 0.0)
                return "";
            else
                return numberFormat.format(parsedModelValue);
        }
    }

    public static String toReturn(EditText editText,
                                  MeasurementSubtype subtype,
                                  String oldString,
                                  String newString) {
        return editText.getText().toString();
    }

    @SuppressWarnings("deprecation")
    private static NumberFormat getNumberFormat(View view) {
        Resources resources = view.getResources();
        Locale locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            locale = resources.getConfiguration().getLocales().get(0);
        else
            locale = resources.getConfiguration().locale;

        NumberFormat format = NumberFormat.getNumberInstance(locale);

        if (format instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) format;
            decimalFormat.setGroupingUsed(false);
        }
        return format;
    }
}
