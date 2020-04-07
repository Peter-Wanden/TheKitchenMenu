package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;


import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientModelPersistence;

import javax.annotation.Nonnull;

public class RecipeIngredientRemoteDataAccess implements DataAccessRecipeIngredient {

    private static RecipeIngredientRemoteDataAccess INSTANCE;

    public static RecipeIngredientRemoteDataAccess getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeIngredientRemoteDataAccess();
        }
        return INSTANCE;
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIngredientModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAllByProductId(
            @Nonnull String productId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIngredientModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIngredientModelPersistence model) {

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
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }
}
