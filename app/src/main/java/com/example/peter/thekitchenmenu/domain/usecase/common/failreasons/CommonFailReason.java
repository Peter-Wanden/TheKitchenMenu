package com.example.peter.thekitchenmenu.domain.usecase.common.failreasons;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum CommonFailReason implements FailReasons {

    // Either the persistence model is unavailable or the data for the data requested or/and the
    // data for this domain model has never been in a valid state to save.
    DATA_UNAVAILABLE(50),
    // There is valid saved domain data for the domain model in question
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
