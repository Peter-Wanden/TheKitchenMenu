package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.DataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Responsible for the flow of domain models objects to primitive data structures to and from the
 * local data source.
 */
public class RecipeMetadataLocalDataAccess
        implements DataAccess<RecipeMetadataPersistenceModel> {

    private static volatile RecipeMetadataLocalDataAccess INSTANCE;

    @Nonnull
    private final GetAllLatestAdapter getAllLatestAdapter;
    @Nonnull
    private final GetByDataIdAdapter getByDataIdAdapter;
    @Nonnull
    private final GetLatestByDomainIdAdapter getLatestByDomainIdAdapter;

    private RecipeMetadataLocalDataAccess() {


    }

    public static RecipeMetadataLocalDataAccess getInstance() {

        if (INSTANCE == null) {
            synchronized (RecipeMetadataLocalDataAccess.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeMetadataLocalDataAccess(

                    );
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        getAllLatestAdapter.getLatest(
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
        getByDataIdAdapter.adaptToModel(
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
    public void getByDomainId(
            @Nonnull String recipeId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        getLatestByDomainIdAdapter.adaptToModel(
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
        toPrimitiveModelAdapter.saveDomainModel(persistenceModel);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    /**
     * Deletes a specific instance of recipe metadata parent data and associated children
     * - If it is the only instance, also delete the corresponding component states and fail reasons
     * - If more than one instance, only delete metadata parent
     *
     * @param domainId the data id
     */
    @Override
    public void deleteByDomainId(@Nonnull final String domainId) {
        // 1. Get the metadata parent data ID
        // 2. Delete its children by parent ID
        // 3. Delete the metadata parent
        toPrimitiveModelAdapter.deleteByDataId(dataId);
    }

    /**
     * Deletes all history of a recipes metadata
     *
     * @param recipeId the recipes domain id
     */
    @Override
    public void deleteByRecipeId(@Nonnull String recipeId) {
        // 1. Find all recipe metadata parent entities
        // 2. For each entity, get its data Id.
        // 3. For each child data source delete all by parent Id

        // 1. Delete MetadataParent
        metadataSource.deleteAllByParentId(recipeId);
        // 2. Delete ComponentStates
        componentStateDataSource.deleteAllByParentId();
        // 3. Delete FailReasons
        recipeFailReasonsDataSource.deleteAllByParentId();
    }

    /**
     * Deletes all local recipe metadata
     */
    @Override
    public void deleteAll() {
        metadataSource.deleteAll();
        componentStateDataSource.deleteAll();
        recipeFailReasonsDataSource.deleteAll();
    }
}
