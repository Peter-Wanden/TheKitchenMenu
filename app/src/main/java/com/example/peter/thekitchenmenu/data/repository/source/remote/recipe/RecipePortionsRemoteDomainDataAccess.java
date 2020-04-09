package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsModelPersistence;

import javax.annotation.Nonnull;

public class RecipePortionsRemoteDomainDataAccess implements DomainDataAccessRecipePortions {

    private static RecipePortionsRemoteDomainDataAccess INSTANCE;

    public static RecipePortionsRemoteDomainDataAccess getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipePortionsRemoteDomainDataAccess();
        return INSTANCE;
    }

    @Override
    public void getByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetDomainModelCallback<RecipePortionsModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipePortionsModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipePortionsModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipePortionsModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipePortionsModelPersistence model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }
}