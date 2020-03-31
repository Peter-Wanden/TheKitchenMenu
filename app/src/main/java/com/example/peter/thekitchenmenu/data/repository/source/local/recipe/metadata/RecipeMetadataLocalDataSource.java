package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeMetaData;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeMetadataLocalDataSource
        implements
        DataSourceRecipeMetaData {

    private static volatile RecipeMetadataLocalDataSource INSTANCE;

    @Nonnull
    private final UniqueIdProvider idProvider;
    @Nonnull
    private final PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource;
    @Nonnull
    private final RecipeMetadataToPersistenceAdapter toPersistenceAdapter;
    @Nonnull
    private final RecipeMetadataListToPersistenceAdapter listToPersistenceAdapter;

    private RecipeMetadataLocalDataSource(
            @Nonnull UniqueIdProvider idProvider,
            @Nonnull PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource,
            @Nonnull RecipeMetadataToPersistenceAdapter toPersistenceAdapter,
            @Nonnull RecipeMetadataListToPersistenceAdapter listToPersistenceAdapter) {

        this.idProvider = idProvider;
        this.metadataSource = metadataSource;
        this.toPersistenceAdapter = toPersistenceAdapter;
        this.listToPersistenceAdapter = listToPersistenceAdapter;
    }

    public static RecipeMetadataLocalDataSource getInstance(
            @Nonnull UniqueIdProvider idProvider,
            @Nonnull PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource,
            @Nonnull RecipeMetadataToPersistenceAdapter toPersistenceAdapter,
            @Nonnull RecipeMetadataListToPersistenceAdapter listToPersistenceAdapter) {

        if (INSTANCE == null) {
            synchronized (RecipeMetadataLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeMetadataLocalDataSource(
                            idProvider,
                            metadataSource,
                            toPersistenceAdapter,
                            listToPersistenceAdapter);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {

        listToPersistenceAdapter.getAllAndNotify(
                new RecipeMetadataListToPersistenceAdapter.Callback() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataPersistenceModel> models) {
                        if (models.isEmpty()) {
                            callback.onDataUnavailable();
                        } else {
                            callback.onAllLoaded(models);
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                });
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        toPersistenceAdapter.createModelByDataIdAndNotify(
                dataId, new RecipeMetadataToPersistenceAdapter.Callback() {
                    @Override
                    public void onModelCreated(RecipeMetadataPersistenceModel model) {

                    }

                    @Override
                    public void onModelUnavailable() {

                    }
                });
    }

    @Override
    public void getByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        // 1. GetAllByRecipeId
        // 2. Only return the one with the most recent date

        // Create a database adapter for each CRUD situation
    }

    @Override
    public void save(@Nonnull RecipeMetadataPersistenceModel persistenceModel) {
        // 1. Break persistence model into primitive models
        RecipeMetadataToPrimitiveAdapter adapter = new RecipeMetadataToPrimitiveAdapter(
                new UniqueIdProvider(), persistenceModel);
        RecipeMetadataToPrimitiveAdapter.Model models = adapter.convertToPrimitives();
        // 2. Save metadata parent
        metadataSource.save(models.getParentEntity());
        // 3. Save ComponentStates list

        for (RecipeComponentStateEntity e : models.getStateEntities()) {
            componentStateDataSource.save(e);
        }
        // 4. Save FailReasons list
        for (RecipeFailReasonEntity e : models.getFailReasonEntities()) {
            recipeFailReasonsDataSource.save(e);
        }
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
     * @param id the data id
     */
    @Override
    public void deleteById(@Nonnull final String id) {
        // 1. Get the metadata parent data ID
        // 2. Delete its children by parent ID
        // 3. Delete the metadata parent
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
