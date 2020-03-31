package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent;

import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipeMetadataParent
        extends PrimitiveDataSource<RecipeMetadataParentEntity> {

    void getAllByRecipeId(@Nonnull String recipeId,
                          @Nonnull GetAllCallback<RecipeMetadataParentEntity> callback);

    void deleteAllByRecipeId(@Nonnull String recipeId);
}
