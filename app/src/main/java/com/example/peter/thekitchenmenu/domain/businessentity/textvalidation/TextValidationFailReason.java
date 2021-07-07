package com.example.peter.thekitchenmenu.domain.businessentity.textvalidation;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public enum TextValidationFailReason implements FailReasons {
    TEXT_TOO_SHORT(500),
    TEXT_TOO_LONG(501),
    TEXT_NULL(502);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, TextValidationFailReason> options = new HashMap<>();

    TextValidationFailReason(int id) {
        this.id = id;
    }

    static {
        for (TextValidationFailReason fr : TextValidationFailReason.values()) {
            options.put(fr.id, fr);
        }
    }

    public static TextValidationFailReason getById(int id) {
        return options.get(id);
    }


    @Override
    public int getId() {
        return id;
    }
}
