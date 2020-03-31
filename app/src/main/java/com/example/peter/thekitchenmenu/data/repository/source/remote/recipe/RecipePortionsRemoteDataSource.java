package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipePortions;

import javax.annotation.Nonnull;

public class RecipePortionsRemoteDataSource implements DataSourceRecipePortions {

    private static RecipePortionsRemoteDataSource INSTANCE;

    public static RecipePortionsRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipePortionsRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipePortionsEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipePortionsEntity entity) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@Nonnull String id) {

    }
}
