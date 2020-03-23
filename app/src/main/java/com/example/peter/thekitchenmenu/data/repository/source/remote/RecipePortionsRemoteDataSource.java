package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipePortions;

import javax.annotation.Nonnull;

public class RecipePortionsRemoteDataSource implements DataSourceRecipePortions {

    private static RecipePortionsRemoteDataSource INSTANCE;

    public static RecipePortionsRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipePortionsRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getPortionsForRecipe(@Nonnull String recipeId,
                                     @Nonnull GetEntityCallback<RecipePortionsEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipePortionsEntity> callback) {
        callback.onDataNotAvailable();
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
