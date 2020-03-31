package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.DataSourceRecipeFailReason;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity;

import javax.annotation.Nonnull;

public class RecipeFailReasonsRemoteDataSource implements DataSourceRecipeFailReason {

    public static RecipeFailReasonsRemoteDataSource INSTANCE;

    public static RecipeFailReasonsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeFailReasonsRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeFailReasonEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void deleteAllByRecipeId(@Nonnull String recipeId) {

    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeFailReasonEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetEntityCallback<RecipeFailReasonEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeFailReasonEntity entity) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {

    }
}
