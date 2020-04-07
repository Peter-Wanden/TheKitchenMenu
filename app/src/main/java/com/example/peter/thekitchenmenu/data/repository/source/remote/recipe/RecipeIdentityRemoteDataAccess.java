package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityModelPersistence;

import javax.annotation.Nonnull;

public class RecipeIdentityRemoteDataAccess implements DataAccess<RecipeIdentityModelPersistence> {

    private static RecipeIdentityRemoteDataAccess INSTANCE;

    public static RecipeIdentityRemoteDataAccess getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeIdentityRemoteDataAccess();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIdentityModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIdentityModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIdentityModelPersistence model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
