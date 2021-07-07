package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryIngredientRemote
        implements DomainDataAccess<IngredientPersistenceModel> {

    private static RepositoryIngredientRemote INSTANCE;

    public static RepositoryIngredientRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredientRemote();
        return INSTANCE;
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<IngredientPersistenceModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void save(@Nonnull IngredientPersistenceModel model) {

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
