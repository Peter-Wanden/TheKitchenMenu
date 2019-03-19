package com.example.peter.thekitchenmenu.utils;

import android.content.Context;

import com.example.peter.thekitchenmenu.R;

public class InputValidation {

    public static String validateTextField(Context context, String textToValidate) {

        if (textToValidate == null || textToValidate.length() < 5) {
            return context.getResources().getString(R.string.validation_text_too_short);

        } else if (textToValidate.length() > 120){
            return context.getResources().getString(R.string.validation_text_too_long);
        }
        return "";
    }

    public static boolean validatePackSize(int packSize) {
        return packSize >= 1 && packSize <= 10000;
    }
}
