package com.example.peter.thekitchenmenu.utils;

import android.app.Application;
import android.content.res.Resources;
import android.text.Editable;

import com.example.peter.thekitchenmenu.R;

public class TextValidationHandler {

    private static final String TAG = "TextValidationHandler";

    public static final String VALIDATED = "validated";

    public static String validateText(Application application, String textToValidate) {
        return validateTextLength(application, textToValidate);
    }

    private static String validateTextLength(Application application, String testToValidate) {
        Resources resources = application.getResources();

        int minimumLength = resources.getInteger(R.integer.user_input_text_shortest_length);
        int maximumLength = resources.getInteger(R.integer.user_input_text_longest_length);
        String lengthTooShort = resources.getString(R.string.validation_text_too_short);
        String lengthTooLong = resources.getString(R.string.validation_text_too_long);

        if (testToValidate.length() < minimumLength) return lengthTooShort;
        else if (testToValidate.length() > maximumLength) return lengthTooLong;
        else return VALIDATED;
    }
}
