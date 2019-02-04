package com.example.peter.thekitchenmenu.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class ShowHideSoftInput {

    public static void showKeyboard(View view, boolean showKeyboard) {
        InputMethodManager imm = getInputMethodManager(view.getContext());

        if (showKeyboard) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
    }
}
