package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.DataSourceRecipeComponentState;

import javax.annotation.Nonnull;

public class RecipeComponentStateRemoteDataSource implements DataSourceRecipeComponentState {

    private static RecipeComponentStateRemoteDataSource INSTANCE;

    public static RecipeComponentStateRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeComponentStateRemoteDataSource();
        }
        return INSTANCE;
    }


    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllCallback<RecipeComponentStateEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void deleteAllByRecipeId(@Nonnull String recipeId) {

    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeComponentStateEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getByDataId(@Nonnull String dataId, @Nonnull GetEntityCallback<RecipeComponentStateEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeComponentStateEntity entity) {

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
