package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;


import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeIngredient;

import javax.annotation.Nonnull;

public class RecipeIngredientRemoteDataSource implements DataSourceRecipeIngredient {

    private static RecipeIngredientRemoteDataSource INSTANCE;

    public static RecipeIngredientRemoteDataSource getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeIngredientRemoteDataSource();
        return INSTANCE;
    }

    @Override
    public void getAllByRecipeId(@Nonnull String recipeId,
                                 @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAllByProductId(@Nonnull String productId,
                                  @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAllByIngredientId(@Nonnull String ingredientId,
                                     @Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        callback.onDataUnavailable();
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeIngredientEntity> callback) {
        callback.onDataUnavailable();
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
