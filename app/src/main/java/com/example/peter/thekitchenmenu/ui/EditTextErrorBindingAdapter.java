package com.example.peter.thekitchenmenu.ui;

import android.widget.EditText;

import androidx.databinding.BindingAdapter;


public class EditTextErrorBindingAdapter {

    @BindingAdapter(value = "editTextErrorSetter")
    public static void setError(EditText editText, String error) {
        editText.setError(error);
    }
}
