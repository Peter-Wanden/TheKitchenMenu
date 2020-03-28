package com.example.peter.thekitchenmenu.data.repository.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;

public interface DataSourceRecipeMetaData extends DataSource<RecipeMetadataPersistenceModel> {

    void getByRecipeId(@Nonnull String recipeId,
                       @Nonnull GetModelCallback<RecipeMetadataPersistenceModel> callback);

    void deleteByRecipeId(@Nonnull String recipeId);
}
