package com.example.peter.thekitchenmenu.commonmocks;

import java.util.HashSet;
import java.util.Set;

import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

public class RecipeComponents {
    public static final Set<RecipeComponentName> requiredComponents = new HashSet<>();

    static {
        requiredComponents.add(RecipeComponentName.IDENTITY);
        requiredComponents.add(RecipeComponentName.COURSE);
        requiredComponents.add(RecipeComponentName.DURATION);
        requiredComponents.add(RecipeComponentName.PORTIONS);
    }
}
