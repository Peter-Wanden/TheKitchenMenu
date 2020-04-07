package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.RecipeMetadataLocalDeleteAllAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.DeleteByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.DeleteByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.GetAllActiveAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.RecipeMetadataLocalGetByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.GetActiveByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.SaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataModelPersistence;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Responsible for the flow of domain models objects to primitive data structures to and from the
 * local persistence framework.
 */
public class RecipeMetadataLocalDomainDataAccess
        implements DataAccess<RecipeMetadataModelPersistence> {

    private static volatile RecipeMetadataLocalDomainDataAccess INSTANCE;

    @Nonnull
    private final RecipeMetadataLocalGetByDataIdAdapter localGetByDataIdAdapter;
    @Nonnull
    private final GetActiveByDomainIdAdapter getActiveByDomainIdAdapter;
    @Nonnull
    private final GetAllActiveAdapter getAllActiveAdapter;
    @Nonnull
    private final SaveAdapter saveAdapter;
    @Nonnull
    private final DeleteByDataIdAdapter deleteByDataIdAdapter;
    @Nonnull
    private final DeleteByDomainIdAdapter deleteByDomainIdAdapter;
    @Nonnull
    private final RecipeMetadataLocalDeleteAllAdapter deleteAllAdapter;

    private RecipeMetadataLocalDomainDataAccess(
            @Nonnull RecipeMetadataLocalGetByDataIdAdapter localGetByDataIdAdapter,
            @Nonnull GetActiveByDomainIdAdapter getActiveByDomainIdAdapter,
            @Nonnull GetAllActiveAdapter getAllActiveAdapter,
            @Nonnull SaveAdapter saveAdapter,
            @Nonnull DeleteByDataIdAdapter deleteByDataIdAdapter,
            @Nonnull DeleteByDomainIdAdapter deleteByDomainIdAdapter,
            @Nonnull RecipeMetadataLocalDeleteAllAdapter deleteAllAdapter) {

        this.localGetByDataIdAdapter = localGetByDataIdAdapter;
        this.getActiveByDomainIdAdapter = getActiveByDomainIdAdapter;
        this.getAllActiveAdapter = getAllActiveAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteByDataIdAdapter = deleteByDataIdAdapter;
        this.deleteByDomainIdAdapter = deleteByDomainIdAdapter;
        this.deleteAllAdapter = deleteAllAdapter;
    }

    public static RecipeMetadataLocalDomainDataAccess getInstance(
            @Nonnull RecipeMetadataLocalGetByDataIdAdapter localGetByDataIdAdapter,
            @Nonnull GetActiveByDomainIdAdapter getActiveByDomainIdAdapter,
            @Nonnull GetAllActiveAdapter getAllActiveAdapter,
            @Nonnull SaveAdapter saveAdapter,
            @Nonnull DeleteByDataIdAdapter deleteByDataIdAdapter,
            @Nonnull DeleteByDomainIdAdapter deleteByDomainIdAdapter,
            @Nonnull RecipeMetadataLocalDeleteAllAdapter deleteAllAdapter) {

        if (INSTANCE == null) {
            synchronized (RecipeMetadataLocalDomainDataAccess.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeMetadataLocalDomainDataAccess(
                            localGetByDataIdAdapter,
                            getActiveByDomainIdAdapter,
                            getAllActiveAdapter,
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
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataModelPersistence> callback) {
        getAllActiveAdapter.adaptLatestToDomainObjects(
                new GetAllActiveAdapter.Callback() {
                    @Override
                    public void onAllLoaded(@Nonnull List<RecipeMetadataModelPersistence> models) {
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
            @Nonnull GetDomainModelCallback<RecipeMetadataModelPersistence> callback) {
        localGetByDataIdAdapter.adaptToDomainModel(
                dataId,
                new RecipeMetadataLocalGetByDataIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataModelPersistence model) {
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
            @Nonnull String recipeId,
            @Nonnull GetDomainModelCallback<RecipeMetadataModelPersistence> callback) {
        getActiveByDomainIdAdapter.adaptToDomainModel(
                recipeId, new GetActiveByDomainIdAdapter.Callback() {
                    @Override
                    public void onModelCreated(@Nonnull RecipeMetadataModelPersistence model) {
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
    public void save(@Nonnull RecipeMetadataModelPersistence persistenceModel) {
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
