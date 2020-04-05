package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters.DeleteAllAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters.DeleteByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters.DeleteByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters.GetAllLatestAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters.GetByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters.GetLatestByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapters.SaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Responsible for the flow of domain models objects to primitive data structures to and from the
 * local persistence framework.
 */
public class RecipeMetadataLocalDataAccess
        implements DataAccess<RecipeMetadataPersistenceModel> {

    private static volatile RecipeMetadataLocalDataAccess INSTANCE;

    @Nonnull
    private final GetByDataIdAdapter getByDataIdAdapter;
    @Nonnull
    private final GetLatestByDomainIdAdapter getLatestByDomainIdAdapter;
    @Nonnull
    private final GetAllLatestAdapter getAllLatestAdapter;
    @Nonnull
    private final SaveAdapter saveAdapter;
    @Nonnull
    private final DeleteByDataIdAdapter deleteByDataIdAdapter;
    @Nonnull
    private final DeleteByDomainIdAdapter deleteByDomainIdAdapter;
    @Nonnull
    private final DeleteAllAdapter deleteAllAdapter;

    private RecipeMetadataLocalDataAccess(
            @Nonnull GetByDataIdAdapter getByDataIdAdapter,
            @Nonnull GetLatestByDomainIdAdapter getLatestByDomainIdAdapter,
            @Nonnull GetAllLatestAdapter getAllLatestAdapter,
            @Nonnull SaveAdapter saveAdapter,
            @Nonnull DeleteByDataIdAdapter deleteByDataIdAdapter,
            @Nonnull DeleteByDomainIdAdapter deleteByDomainIdAdapter,
            @Nonnull DeleteAllAdapter deleteAllAdapter) {

        this.getByDataIdAdapter = getByDataIdAdapter;
        this.getLatestByDomainIdAdapter = getLatestByDomainIdAdapter;
        this.getAllLatestAdapter = getAllLatestAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteByDataIdAdapter = deleteByDataIdAdapter;
        this.deleteByDomainIdAdapter = deleteByDomainIdAdapter;
        this.deleteAllAdapter = deleteAllAdapter;
    }

    public static RecipeMetadataLocalDataAccess getInstance(
            @Nonnull GetByDataIdAdapter getByDataIdAdapter,
            @Nonnull GetLatestByDomainIdAdapter getLatestByDomainIdAdapter,
            @Nonnull GetAllLatestAdapter getAllLatestAdapter,
            @Nonnull SaveAdapter saveAdapter,
            @Nonnull DeleteByDataIdAdapter deleteByDataIdAdapter,
            @Nonnull DeleteByDomainIdAdapter deleteByDomainIdAdapter,
            @Nonnull DeleteAllAdapter deleteAllAdapter) {

        if (INSTANCE == null) {
            synchronized (RecipeMetadataLocalDataAccess.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeMetadataLocalDataAccess(
                            getByDataIdAdapter,
                            getLatestByDomainIdAdapter,
                            getAllLatestAdapter,
                            saveAdapter,
                            deleteByDataIdAdapter,
                            deleteByDomainIdAdapter,
                            deleteAllAdapter
                    );
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        getAllLatestAdapter.adaptLatestToDomainObjects(
                new GetAllLatestAdapter.Callback() {
                    @Override
                    public void onAllLoaded(@Nonnull List<RecipeMetadataPersistenceModel> models) {
                        callback.onAllLoaded(models);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                });
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        getByDataIdAdapter.adaptToDomainModel(
                dataId,
                new GetByDataIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model) {
                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                });
    }

    @Override
    public void getLatestByDomainId(
            @Nonnull String recipeId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        getLatestByDomainIdAdapter.adaptToDomainModel(
                recipeId, new GetLatestByDomainIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model) {
                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                });
    }

    @Override
    public void save(@Nonnull RecipeMetadataPersistenceModel persistenceModel) {
        saveAdapter.adaptAndSave(persistenceModel);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteByDataId(String dataId) {
        deleteByDataIdAdapter.deleteDataId(dataId);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull final String domainId) {
        deleteByDomainIdAdapter.deleteAllForDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAllAdapter.deleteAll();
    }
}
