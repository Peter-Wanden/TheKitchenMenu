package com.example.peter.thekitchenmenu.commonmocks;

import java.util.HashSet;
import java.util.Set;

import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;

public class RecipeComponents {
    public static final Set<RecipeComponentNameName> requiredComponents = new HashSet<>();

    static {
        requiredComponents.add(RecipeComponentNameName.IDENTITY);
        requiredComponents.add(RecipeComponentNameName.COURSE);
        requiredComponents.add(RecipeComponentNameName.DURATION);
        requiredComponents.add(RecipeComponentNameName.PORTIONS);
    }
}
