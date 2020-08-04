package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;

import java.util.HashMap;
import java.util.Map;

public enum RecipeMetadataUseCaseFailReason implements FailReasons {
    MISSING_REQUIRED_COMPONENTS(400),
    INVALID_COMPONENTS(401);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipeMetadataUseCaseFailReason> options = new HashMap<>();

    RecipeMetadataUseCaseFailReason(int id) {
        this.id = id;
    }

    static {
        for (RecipeMetadataUseCaseFailReason s : RecipeMetadataUseCaseFailReason.values())
            options.put(s.id, s);
    }

    public static RecipeMetadataUseCaseFailReason getById(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }
}
