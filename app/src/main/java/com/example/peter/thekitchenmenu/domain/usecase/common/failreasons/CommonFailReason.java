package com.example.peter.thekitchenmenu.domain.usecase.common.failreasons;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum CommonFailReason implements FailReasons {
    DATA_UNAVAILABLE(50),
    NONE(51);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, CommonFailReason> options = new HashMap<>();

    CommonFailReason(int id) {
        this.id = id;
    }

    static {
        for (CommonFailReason r : CommonFailReason.values())
            options.put(r.id, r);
    }

    public static CommonFailReason getFromId(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
