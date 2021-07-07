package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

/**
 * Fail reasons are metadata that provide a description as to why a domain data element of
 * a use case is invalid. As such, if a use case has one or more domain data elements that
 * can be invalid a fail reason must be added to reflect its invalid state.
 * For example: TEXT_TOO_LONG or TEXT_TOO_SHORT, VALUE_TOO_HIGH etc.
 */
public enum TestUseCaseFailReasons
        implements
        FailReasons {
    TEXT_NULL(1000),
    TEXT_TOO_SHORT(1003),
    TEXT_TOO_LONG(1002);

    /* Enums must provide a numerical representation of themselves for persistence reasons */
    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, TestUseCaseFailReasons> options = new HashMap<>();

    TestUseCaseFailReasons(int id) {
        this.id = id;
    }

    static {
        for (TestUseCaseFailReasons f : TestUseCaseFailReasons.values()) {
            options.put(f.getId(), f);
        }
    }

    public static TestUseCaseFailReasons getById(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
