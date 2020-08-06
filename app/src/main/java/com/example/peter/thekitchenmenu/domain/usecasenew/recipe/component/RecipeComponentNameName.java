package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.componentname.UseCaseInvokerComponentName;

import java.util.HashMap;
import java.util.Map;

/**
 * The group of components that make up a recipe
 */
public enum RecipeComponentNameName implements UseCaseInvokerComponentName {
    COURSE(1),
    DURATION(2),
    IDENTITY(3),
    PORTIONS(4),
    TEXT_VALIDATOR(5),
    RECIPE_METADATA(6),
    RECIPE(7);

    private final int id;

    @SuppressLint("UseSparseArrays")
    private static Map<Integer, RecipeComponentNameName> options = new HashMap<>();

    RecipeComponentNameName(int id) {
        this.id = id;
    }

    static {
        for (RecipeComponentNameName n : RecipeComponentNameName.values())
            options.put(n.id, n);
    }

    public static RecipeComponentNameName getFromId(int id) {
        return options.get(id);
    }

    @Override
    public int getId() {
        return id;
    }

}
