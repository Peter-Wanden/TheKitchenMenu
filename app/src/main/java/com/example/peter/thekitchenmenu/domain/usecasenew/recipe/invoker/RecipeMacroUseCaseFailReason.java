package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public enum RecipeMacroUseCaseFailReason
        implements
        FailReasons {
    MISSING_REQUIRED_COMPONENTS(400),
    INVALID_COMPONENTS(401);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipeMacroUseCaseFailReason> options = new HashMap<>();

    RecipeMacroUseCaseFailReason(int id) {
        this.id = id;
    }

    static {
        for (RecipeMacroUseCaseFailReason s : RecipeMacroUseCaseFailReason.values())
            options.put(s.id, s);
    }

    public static RecipeMacroUseCaseFailReason getById(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
