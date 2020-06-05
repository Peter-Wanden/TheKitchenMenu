package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentityRemote
        implements DomainDataAccess<RecipeIdentityPersistenceModel> {

    private static RepositoryRecipeIdentityRemote INSTANCE;

    public static RepositoryRecipeIdentityRemote getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentityRemote();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceModel> callback) {
        callback.onDomainModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceModel> callback) {
        callback.onDomainModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIdentityPersistenceModel model) {

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
