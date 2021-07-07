package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public enum RecipePortionsUseCaseFailReason
        implements
        FailReasons {

    SERVINGS_TOO_LOW(350),
    SERVINGS_TOO_HIGH(351),
    SITTINGS_TOO_LOW(352),
    SITTINGS_TOO_HIGH(353);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipePortionsUseCaseFailReason> options = new HashMap<>();

    RecipePortionsUseCaseFailReason(int id) {
        this.id = id;
    }

    static {
        for (RecipePortionsUseCaseFailReason s : RecipePortionsUseCaseFailReason.values())
            options.put(s.id, s);
    }

    public static RecipePortionsUseCaseFailReason getById(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
