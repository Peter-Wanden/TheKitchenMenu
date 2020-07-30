package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public enum RecipeIdentityUseCaseFailReason
        implements
        FailReasons {
    TITLE_NULL(300),
    TITLE_TOO_SHORT(301),
    TITLE_TOO_LONG(302),
    DESCRIPTION_NULL(303),
    DESCRIPTION_TOO_SHORT(304),
    DESCRIPTION_TOO_LONG(305);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipeIdentityUseCaseFailReason> options = new HashMap<>();

    RecipeIdentityUseCaseFailReason(int id) {
        this.id = id;
    }

    static {
        for (RecipeIdentityUseCaseFailReason s : RecipeIdentityUseCaseFailReason.values())
            options.put(s.id, s);
    }

    public static RecipeIdentityUseCaseFailReason getById(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
