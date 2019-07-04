package com.example.peter.thekitchenmenu.utils;

import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;

public class TextValidationHandler {

    private static final String TAG = "TextValidationHandler";

    public static final String VALIDATED = "validated";

    public static String validateShortText(Resources resources, String textToValidate) {
        return validateTextLength(resources, textToValidate);
    }

    private static String validateTextLength(Resources resources, String textToValidate) {

        int minimumLength = resources.getInteger(R.integer.input_validation_short_text_shortest_length);
        int maximumLength = resources.getInteger(R.integer.input_validation_short_text_longest_length);
        String lengthTooShort = resources.getString(R.string.input_error_short_text_too_short);
        String lengthTooLong = resources.getString(R.string.input_error_short_text_too_long);

        if (textToValidate.length() < minimumLength)
            return lengthTooShort;
        else if (textToValidate.length() > maximumLength)
            return lengthTooLong;
        else return VALIDATED;
    }

    public static String validateLongText(Resources resources, String textToValidate) {
        int maximumLength = resources.getInteger(R.integer.input_validation_long_text_longest_length);
        int minimumLength = resources.getInteger(R.integer.input_validation_long_text_shortest_length);
        String lengthTooShort = resources.getString(R.string.input_error_long_text_too_short);
        String lengthTooLong = resources.getString(R.string.input_error_long_text_too_long);

        if (textToValidate.length() < minimumLength)
            return lengthTooShort;
        else if (textToValidate.length() > maximumLength)
            return lengthTooLong;
        else return VALIDATED;

    }
}
