package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipePortions;

import javax.annotation.Nonnull;

public class RecipePortionsRemoteDataAccess implements DataAccessRecipePortions {

    private static RecipePortionsRemoteDataAccess INSTANCE;

    public static RecipePortionsRemoteDataAccess getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipePortionsRemoteDataAccess();
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipePortionsEntity> callback) {
        callback.onModelsUnavailable();
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
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }
}
