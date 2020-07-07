package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceDomainModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeMetadataRemote
        implements DomainDataAccess<RecipeMetadataPersistenceDomainModel> {

    private static RepositoryRecipeMetadataRemote INSTANCE;

    public static RepositoryRecipeMetadataRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeMetadataRemote();
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceDomainModel> callback) {
        callback.onDomainModelsUnavailable();

    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceDomainModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeMetadataPersistenceDomainModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAll() {

    }
}
