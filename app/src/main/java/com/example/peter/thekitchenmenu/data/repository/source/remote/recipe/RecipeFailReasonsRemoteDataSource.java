package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeFailReasonPrimitive;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeFailReasons;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RecipeFailReasonEntity;

import javax.annotation.Nonnull;

public class RecipeFailReasonsRemoteDataSource implements DataSourceRecipeFailReasons {

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
    public void getById(@Nonnull String id,
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
    public void deleteById(@Nonnull String id) {

    }
}
