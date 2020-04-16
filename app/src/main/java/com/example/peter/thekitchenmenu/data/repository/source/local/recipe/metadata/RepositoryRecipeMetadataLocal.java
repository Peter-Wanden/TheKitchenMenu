package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetActiveByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetAllActiveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Uses data adapters and filters to enable the flow of domain models objects to and from the
 * persistence local persistence framework.
 */
public class RepositoryRecipeMetadataLocal
        implements DomainDataAccess<RecipeMetadataPersistenceModel> {

    private static volatile RepositoryRecipeMetadataLocal INSTANCE;

    @Nonnull
    private final RecipeMetadataLocalGetByDataIdAdapter localGetByDataIdAdapter;
    @Nonnull
    private final RecipeMetadataLocalGetActiveByDomainIdAdapter getActiveByDomainIdAdapter;
    @Nonnull
    private final RecipeMetadataLocalGetAllActiveAdapter getAllActiveAdapter;
    @Nonnull
    private final RecipeMetadataLocalSaveAdapter saveAdapter;
    @Nonnull
    private final RecipeMetadataLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipeMetadataLocal(
            @Nonnull RecipeMetadataLocalGetByDataIdAdapter localGetByDataIdAdapter,
            @Nonnull RecipeMetadataLocalGetActiveByDomainIdAdapter getActiveByDomainIdAdapter,
            @Nonnull RecipeMetadataLocalGetAllActiveAdapter getAllActiveAdapter,
            @Nonnull RecipeMetadataLocalSaveAdapter saveAdapter,
            @Nonnull RecipeMetadataLocalDeleteAdapter deleteAdapter) {

        this.localGetByDataIdAdapter = localGetByDataIdAdapter;
        this.getActiveByDomainIdAdapter = getActiveByDomainIdAdapter;
        this.getAllActiveAdapter = getAllActiveAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipeMetadataLocal getInstance(
            @Nonnull RecipeMetadataLocalGetByDataIdAdapter localGetByDataIdAdapter,
            @Nonnull RecipeMetadataLocalGetActiveByDomainIdAdapter getActiveByDomainIdAdapter,
            @Nonnull RecipeMetadataLocalGetAllActiveAdapter getAllActiveAdapter,
            @Nonnull RecipeMetadataLocalSaveAdapter saveAdapter,
            @Nonnull RecipeMetadataLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RepositoryRecipeMetadataLocal.class) {
                if (INSTANCE == null)
                    INSTANCE = new RepositoryRecipeMetadataLocal(
                            localGetByDataIdAdapter,
                            getActiveByDomainIdAdapter,
                            getAllActiveAdapter,
                            saveAdapter,
                            deleteAdapter
                    );
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        getAllActiveAdapter.adaptLatestToDomainObjects(
                new RecipeMetadataLocalGetAllActiveAdapter.Callback() {
                    @Override
                    public void onAllLoaded(@Nonnull List<RecipeMetadataPersistenceModel> models) {
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        localGetByDataIdAdapter.adaptToDomainModel(
                dataId,
                new RecipeMetadataLocalGetByDataIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model) {
                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        getActiveByDomainIdAdapter.getActiveModelForDomainId(
                domainId,
                new RecipeMetadataLocalGetActiveByDomainIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model) {
                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void save(@Nonnull RecipeMetadataPersistenceModel persistenceModel) {
        saveAdapter.save(persistenceModel);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteByDataId(String dataId) {
        deleteAdapter.deleteDataId(dataId);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull final String domainId) {
        deleteAdapter.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAdapter.deleteAll();
    }
}
