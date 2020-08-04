package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public enum RecipeComponentName {
    COURSE(1),
    DURATION(2),
    IDENTITY(3),
    PORTIONS(4),
    TEXT_VALIDATOR(5),
    RECIPE_METADATA(6),
    RECIPE(7);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipeComponentName> options = new HashMap<>();

    RecipeComponentName(int id) {
        this.id = id;
    }

    static {
        for (RecipeComponentName n : RecipeComponentName.values())
            options.put(n.id, n);
    }

    public static RecipeComponentName getFromId(int id) {
        return options.get(id);
    }

    public int getId() {
        return id;
    }

}
