package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationModelPersistence;

import javax.annotation.Nonnull;

public class RecipeDurationRemoteDataAccess implements DataAccess<RecipeDurationModelPersistence> {

    private static RecipeDurationRemoteDataAccess INSTANCE;

    public static RecipeDurationRemoteDataAccess getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipeDurationRemoteDataAccess();
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationModelPersistence> callback) {
        callback.onModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeDurationModelPersistence> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeDurationModelPersistence model) {

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
