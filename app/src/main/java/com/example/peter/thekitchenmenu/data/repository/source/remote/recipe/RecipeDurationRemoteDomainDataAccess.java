package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import javax.annotation.Nonnull;

public class RecipeDurationRemoteDomainDataAccess implements DomainDataAccess<RecipeDurationPersistenceModel> {

    private static RecipeDurationRemoteDomainDataAccess INSTANCE;

    public static RecipeDurationRemoteDomainDataAccess getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeDurationRemoteDomainDataAccess();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceModel> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeDurationPersistenceModel model) {

    }

    @Override
    public void refreshData() {
        // Not required because the {@link Repository} handles the logic of refreshing data
        // from all the available data sources.
    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
