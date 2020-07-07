package com.example.peter.thekitchenmenu.data.repository.source.remote;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryIngredientRemote
        implements DomainDataAccess<IngredientPersistenceDomainModel> {

    private static RepositoryIngredientRemote INSTANCE;

    public static RepositoryIngredientRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryIngredientRemote();
        return INSTANCE;
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<IngredientPersistenceDomainModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void save(@Nonnull IngredientPersistenceDomainModel model) {

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
