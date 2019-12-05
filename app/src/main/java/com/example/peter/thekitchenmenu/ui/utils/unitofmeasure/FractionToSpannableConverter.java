package com.example.peter.thekitchenmenu.ui.utils.unitofmeasure;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;

public class FractionToSpannableConverter {

    public Spannable getFractionSpannable(String fractionString) {
        SpannableStringBuilder fraction = new SpannableStringBuilder(fractionString);

        if (fractionString.contains("/")) {
            int numeratorStart = 0;
            int numeratorEnd = fractionString.indexOf("/");
            int denominatorStart = fractionString.indexOf("/") + 1;
            int denominatorEnd = fractionString.length();
            int exclusive = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
            float proportion = 0.75f;

            fraction.setSpan(new SuperscriptSpan(), numeratorStart, numeratorEnd, exclusive);
            fraction.setSpan(new RelativeSizeSpan(proportion), numeratorStart, numeratorEnd, exclusive);
            fraction.setSpan(new SubscriptSpan(), denominatorStart, denominatorEnd, exclusive);
            fraction.setSpan(new RelativeSizeSpan(proportion), denominatorStart, denominatorEnd, exclusive);
            fraction.setSpan(new ScaleXSpan(proportion), numeratorStart, denominatorEnd, exclusive);
            fraction.append(" ");

        }
        return fraction;
    }
}
