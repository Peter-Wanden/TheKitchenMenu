package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeMetaData;

import javax.annotation.Nonnull;

public class RecipeMetadataRemoteDataSource implements DataSourceRecipeMetaData {

    private static RecipeMetadataRemoteDataSource INSTANCE;

    public static RecipeMetadataRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeMetadataRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getById(@Nonnull String recipeId,
                        @Nonnull GetEntityCallback<RecipeMetadataParentEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipeMetadataParentEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeMetadataParentEntity entity) {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // recipes from all the available data sources.
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteById(@Nonnull String id) {

    }

    @Override
    public void deleteByRecipeId(String recipeId) {

    }

    @Override
    public void deleteAll() {

    }
}
