package com.example.peter.thekitchenmenu.ui.bindingadapters;

import android.widget.EditText;

import androidx.databinding.BindingAdapter;


public class EditTextErrorBindingAdapter {
    @BindingAdapter(value = "editTextErrorSetter")
    public static void setError(EditText editText, String error) {
        if (!editText.getText().toString().equals(error))
            editText.setError(error);
    }
}
