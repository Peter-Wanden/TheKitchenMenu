package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import javax.annotation.Nonnull;

public class RecipeMetadataRemoteDataAccess implements DataAccess<RecipeMetadataPersistenceModel> {

    private static RecipeMetadataRemoteDataAccess INSTANCE;

    public static RecipeMetadataRemoteDataAccess getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RecipeMetadataRemoteDataAccess();
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        callback.onModelsUnavailable();

    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        callback.onModelUnavailable();
    }

    @Override
    public void getLatestByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        callback.onModelUnavailable();
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
