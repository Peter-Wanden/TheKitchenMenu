package com.example.peter.thekitchenmenu.data.repository.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;

import javax.annotation.Nonnull;

public class DeleteByDataIdAdapter {
    @Nonnull
    private final RecipeMetadataParentEntityLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;

    public DeleteByDataIdAdapter(
            @Nonnull RecipeMetadataParentEntityLocalDataSource parentDataSource,
            @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
            @Nonnull RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;
    }

    public void deleteDataId(String dataId) {
        recipeFailReasonsDataSource.deleteAllByParentId(dataId);
        componentStateDataSource.deleteAllByParentId(dataId);
        parentDataSource.deleteByDataId(dataId);
    }
}
