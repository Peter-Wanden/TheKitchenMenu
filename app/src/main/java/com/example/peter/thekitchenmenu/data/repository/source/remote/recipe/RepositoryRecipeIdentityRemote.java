package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentityRemote
        implements DomainDataAccess<RecipeIdentityUseCasePersistenceModel> {

    private static RepositoryRecipeIdentityRemote INSTANCE;

    public static RepositoryRecipeIdentityRemote getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeIdentityRemote();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityUseCasePersistenceModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeIdentityUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIdentityUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeIdentityUseCasePersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
