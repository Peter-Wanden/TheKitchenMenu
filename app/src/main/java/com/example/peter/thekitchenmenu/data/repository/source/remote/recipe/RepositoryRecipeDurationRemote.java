package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeDurationRemote
        implements DomainDataAccess<RecipeDurationPersistenceDomainModel> {

    private static RepositoryRecipeDurationRemote INSTANCE;

    public static RepositoryRecipeDurationRemote getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryRecipeDurationRemote();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceDomainModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeDurationPersistenceDomainModel model) {

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
