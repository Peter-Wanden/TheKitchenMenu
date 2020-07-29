package com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum ComponentState {
    INVALID_DEFAULT(1),
    INVALID_UNCHANGED(2),
    INVALID_CHANGED(3),
    VALID_DEFAULT(4),
    VALID_UNCHANGED(5),
    VALID_CHANGED(6);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, ComponentState> options = new HashMap<>();

    ComponentState(int id) {
        this.id = id;
    }

    static {
        for (ComponentState state : ComponentState.values())
            options.put(state.id, state);
    }

    public static ComponentState fromInt(int id) {
        return options.get(id);
    }

    public int id() {
        return id;
    }
}
