package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.componentname.UseCaseComponentName;

import java.util.HashMap;
import java.util.Map;

public enum RecipeComponentName
        implements
        UseCaseComponentName {

    RECIPE_INVOKER(0),
    COURSE(1),
    DURATION(2),
    IDENTITY(3),
    PORTIONS(4);

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

    @Override
    public int getId() {
        return id;
    }
}
