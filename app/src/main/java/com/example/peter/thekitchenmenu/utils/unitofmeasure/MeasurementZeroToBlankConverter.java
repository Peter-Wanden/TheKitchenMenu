package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import android.widget.EditText;

public class MeasurementZeroToBlankConverter {

    private static final String TAG = "MeasurementZeroToBlankC";

    // TODO - Add inverse method to see if this solves the measurement model updating problem!

    public static String doNotShowZero(EditText editText, int value) {

        try {

            String numberInView = editText.getText().toString();
            int parsed = Integer.parseInt(numberInView);

            if (parsed == 0) return "";
            if (value == 0) return "";

            return String.valueOf(value);

        } catch (NumberFormatException e) {

            if (value > 0) return String.valueOf(value);
            return "";
        }
    }

    public static String doNotShowZero(EditText editText, double value) {

        try {

            String numberInView = editText.getText().toString();
            double parsed = Double.parseDouble(numberInView);

            if (parsed == 0.) return "";
            if (value == 0.) return "";

            return String.valueOf(value);

        } catch (NumberFormatException e) {

            if (value > 0) return String.valueOf(value);
            return "";
        }
    }
}
