package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public enum RecipeDurationUseCaseFailReason
        implements
        FailReasons {

    INVALID_PREP_TIME(250),
    INVALID_COOK_TIME(251);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipeDurationUseCaseFailReason> options = new HashMap<>();

    RecipeDurationUseCaseFailReason(int id) {
        this.id = id;
    }

    static {
        for (RecipeDurationUseCaseFailReason s : RecipeDurationUseCaseFailReason.values())
            options.put(s.id, s);
    }

    public static RecipeDurationUseCaseFailReason getById(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
