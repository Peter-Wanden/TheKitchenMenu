package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentityRemote
        implements DomainDataAccess<RecipeIdentityPersistenceDomainModel> {

    private static RepositoryRecipeIdentityRemote INSTANCE;

    public static RepositoryRecipeIdentityRemote getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentityRemote();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceDomainModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIdentityPersistenceDomainModel model) {

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
