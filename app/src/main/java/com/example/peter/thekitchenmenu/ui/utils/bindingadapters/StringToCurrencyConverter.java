package com.example.peter.thekitchenmenu.ui.utils.bindingadapters;

import android.widget.TextView;

import com.google.android.gms.common.util.Strings;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class StringToCurrencyConverter {

//    @BindingAdapter(value = "stringToCurrency")
    public static void toString(TextView view, String price) {

        if (Strings.isEmptyOrWhitespace(price)) {
            view.setText("");
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
