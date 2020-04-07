package com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalDeleteAllAdapter {
    @Nonnull
    private final RecipeMetadataParentEntityLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;

    public RecipeMetadataLocalDeleteAllAdapter(@Nonnull RecipeMetadataParentEntityLocalDataSource parentDataSource,
                                               @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
                                               @Nonnull RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;
    }

    public void deleteAll() {
        parentDataSource.deleteAll();
        componentStateDataSource.deleteAll();
        recipeFailReasonsDataSource.deleteAll();
    }
}
