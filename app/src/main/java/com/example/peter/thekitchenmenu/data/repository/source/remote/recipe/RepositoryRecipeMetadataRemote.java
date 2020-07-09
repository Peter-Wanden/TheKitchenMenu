package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeMetadataRemote
        implements DomainDataAccess<RecipeMetadataPersistenceModel> {

    private static RepositoryRecipeMetadataRemote INSTANCE;

    public static RepositoryRecipeMetadataRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeMetadataRemote();
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        callback.onDomainModelsUnavailable();

    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeMetadataPersistenceModel model) {

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
