package com.example.peter.thekitchenmenu.ui.utils.bindingadapters;

import android.widget.EditText;


public class EditTextErrorBindingAdapter {
//    @BindingAdapter(value = "editTextErrorSetter")
    public static void setError(EditText editText, String error) {
        if (!editText.getText().toString().equals(error))
            editText.setError(error);
    }
}
