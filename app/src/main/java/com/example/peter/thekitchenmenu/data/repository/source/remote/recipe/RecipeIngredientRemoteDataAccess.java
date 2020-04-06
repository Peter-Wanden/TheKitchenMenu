package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;


import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

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
    public void getLatestByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceModel> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAllByProductId(
            @Nonnull String productId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceModel> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIngredientPersistenceModel model) {

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
