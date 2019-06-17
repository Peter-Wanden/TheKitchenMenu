package com.example.peter.thekitchenmenu.utils;

import android.util.Log;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.google.android.gms.common.util.Strings;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class StringToCurrencyConverter {

    private static final String TAG = "tkm-StringToCurrency";

    @BindingAdapter(value = "stringToCurrency")
    public static void toString(TextView view, String price) {

        Log.d(TAG, "toString: price passed in=" + price);

        if (Strings.isEmptyOrWhitespace(price)) {
            Log.d(TAG, "toString: String is empty!");
            return;
        }

        NumberFormat money = NumberFormat.getCurrencyInstance();
        BigDecimal unformattedPriceAsDecimal;

        try {
            unformattedPriceAsDecimal = new BigDecimal(price);
        } catch (NumberFormatException e) {
            unformattedPriceAsDecimal = new BigDecimal("0");
        }

        view.setText(money.format(unformattedPriceAsDecimal));
    }
}
