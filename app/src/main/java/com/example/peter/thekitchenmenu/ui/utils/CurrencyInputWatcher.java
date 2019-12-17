package com.example.peter.thekitchenmenu.ui.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class CurrencyInputWatcher implements TextWatcher {

    private static final String TAG = "tkm-" + CurrencyInputWatcher.class.getSimpleName() + ":";

    private final WeakReference<EditText> editTextWeakReference;

    public CurrencyInputWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<>(editText);
    }

    @Override
    public synchronized void afterTextChanged(Editable editable) {

        EditText editText = editTextWeakReference.get();
        if (editText == null) return;

        String s = editable.toString();
        if (s.isEmpty()) return;

        editText.removeTextChangedListener(this);

        String replaceable = String.format(
                "[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
        String cleanString = s.replaceAll(replaceable, "");

        BigDecimal parsed = new BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR).divide(
                        new BigDecimal(100), BigDecimal.ROUND_FLOOR);

        String formatted = NumberFormat.getCurrencyInstance().format(parsed);

        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
}






























