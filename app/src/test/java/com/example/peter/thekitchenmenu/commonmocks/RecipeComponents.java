package com.example.peter.thekitchenmenu.commonmocks;

import java.util.HashSet;
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;

public class RecipeComponents {
    public static final Set<ComponentName> requiredComponents = new HashSet<>();

    static {
        requiredComponents.add(ComponentName.IDENTITY);
        requiredComponents.add(ComponentName.COURSE);
        requiredComponents.add(ComponentName.DURATION);
        requiredComponents.add(ComponentName.PORTIONS);
    }
}
