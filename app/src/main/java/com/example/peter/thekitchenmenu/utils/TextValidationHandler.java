package com.example.peter.thekitchenmenu.utils;

import android.content.res.Resources;
import android.text.Html;

import com.example.peter.thekitchenmenu.R;

public class TextValidationHandler {

    public static final String VALIDATED = "validated";

    public String validateShortText(Resources resources, String textToValidate) {
        return textToValidate == null ? "" : validateTextLength(resources, textToValidate);
    }

    private String validateTextLength(Resources resources, String textToValidate) {
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

    public String validateLongText(Resources resources, String textToValidate) {
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

    public String stripOutHtml(String inputText) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(inputText, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.escapeHtml(inputText);
        }
    }
}
