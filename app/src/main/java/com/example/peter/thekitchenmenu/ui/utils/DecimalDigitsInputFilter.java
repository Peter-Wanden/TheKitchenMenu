package com.example.peter.thekitchenmenu.ui.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

    private Pattern pattern;

    public DecimalDigitsInputFilter(int digitsBeforeDecimal, int digitsAfterDecimal) {

        pattern = Pattern.compile(("(([1-9]{1}[0-9]{0," + (
                digitsBeforeDecimal - 1) + "})?||[0]{1})((\\.[0-9]{0," +
                digitsAfterDecimal + "})?)||(\\.)?"));
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned destination, int destinationStart, int destinationEnd) {

        String newString =

                destination.toString().substring(0, destinationStart) +
                destination.toString().substring(destinationEnd);

        newString =

                newString.substring(0, destinationStart) + source.toString() +
                newString.substring(destinationStart);

        Matcher matcher = pattern.matcher(newString);

        if (matcher.matches()) {
            // Returning null indicates that the input is valid.
            return null;
        }
        return "";
    }
}
