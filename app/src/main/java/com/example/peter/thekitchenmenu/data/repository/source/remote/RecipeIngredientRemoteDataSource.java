package com.example.peter.thekitchenmenu.data.repository.source.remote;


import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSourceRecipeIngredient;

import javax.annotation.Nonnull;

public class RecipeIngredientRemoteDataSource implements DataSourceRecipeIngredient {

    private static RecipeIngredientRemoteDataSource INSTANCE;

    public static RecipeIngredientRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeIngredientRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getByProductId(@Nonnull String productId,
                               @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getByIngredientId(@Nonnull String ingredientId,
                                  @Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getAll(@Nonnull GetAllCallback<RecipeIngredientEntity> callback) {
        callback.onDataNotAvailable();
    }

    @Override
    public void getById(@Nonnull String id,
                        @Nonnull GetEntityCallback<RecipeIngredientEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIngredientEntity entity) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // data from all the available sources.
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(@Nonnull String id) {

    }
}
