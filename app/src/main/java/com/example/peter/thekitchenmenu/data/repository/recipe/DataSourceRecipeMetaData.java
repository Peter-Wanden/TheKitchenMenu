package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;

import javax.annotation.Nonnull;

public interface DataSourceRecipeMetaData extends PrimitiveDataSource<RecipeMetadataEntity> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetEntityCallback<RecipeMetadataEntity> callback);

    void deleteByRecipeId(@Nonnull String recipeId);
}
