package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataSourceRecipeMetaData;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntityDao;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntityDao;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityDao;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class RecipeMetadataLocalDataSource implements DataSourceRecipeMetaData {

    private static volatile RecipeMetadataLocalDataSource INSTANCE;

    @Nonnull
    private final PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource;
    @Nonnull
    private final RecipeMetadataParentEntityDao dao;
    @Nonnull
    private final PrimitiveDataSource<RecipeComponentStateEntity> componentStateDataSource;
    @Nonnull
    private final RecipeComponentStateEntityDao componentStateDao;
    @Nonnull
    private final PrimitiveDataSource<RecipeFailReasonEntity> recipeFailReasonsDataSource;
    @Nonnull
    private final RecipeFailReasonEntityDao failReasonsDao;
    @Nonnull
    private final AppExecutors executors;
    @Nonnull
    private final UniqueIdProvider idProvider;

    private final List<RecipeMetadataPersistenceModel> persistenceModels;
    private int listSize;
    private int totalProcessed;

    // todo - Implement the adapter pattern from persistence to primitive and back :)
    private RecipeMetadataLocalDataSource(
            @Nonnull AppExecutors executors,
            @Nonnull UniqueIdProvider idProvider,
            @Nonnull PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource,
            @Nonnull RecipeMetadataParentEntityDao dao,
            @Nonnull PrimitiveDataSource<RecipeComponentStateEntity> componentStateDataSource,
            @Nonnull RecipeComponentStateEntityDao componentStateDao,
            @Nonnull PrimitiveDataSource<RecipeFailReasonEntity> failReasonDataSource,
            @Nonnull RecipeFailReasonEntityDao failReasonsDao) {

        this.executors = executors;
        this.idProvider = idProvider;
        this.metadataSource = metadataSource;
        this.dao = dao;
        this.componentStateDataSource = componentStateDataSource;
        this.componentStateDao = componentStateDao;
        this.recipeFailReasonsDataSource = failReasonDataSource;
        this.failReasonsDao = failReasonsDao;
        persistenceModels = new ArrayList<>();
    }

    public static RecipeMetadataLocalDataSource getInstance(
            @Nonnull AppExecutors appExecutors,
            @Nonnull UniqueIdProvider idProvider,
            @Nonnull PrimitiveDataSource<RecipeMetadataParentEntity> metadataSource,
            @Nonnull RecipeMetadataParentEntityDao dao,
            @Nonnull PrimitiveDataSource<RecipeComponentStateEntity> componentStateDataSource,
            @Nonnull RecipeComponentStateEntityDao componentStateDao,
            @Nonnull PrimitiveDataSource<RecipeFailReasonEntity> failReasonsDataSource,
            @Nonnull RecipeFailReasonEntityDao failReasonsDao) {

        if (INSTANCE == null) {
            synchronized (RecipeMetadataLocalDataSource.class) {
                if (INSTANCE == null)
                    INSTANCE = new RecipeMetadataLocalDataSource(
                            appExecutors,
                            idProvider,
                            metadataSource,
                            dao,
                            componentStateDataSource,
                            componentStateDao,
                            failReasonsDataSource,
                            failReasonsDao);
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        persistenceModels.clear();

        metadataSource.getAll(new PrimitiveDataSource.GetAllCallback<RecipeMetadataParentEntity>() {
            @Override
            public void onAllLoaded(List<RecipeMetadataParentEntity> parentEntities) {

                if (parentEntities.isEmpty())
                    callback.onDataUnavailable();
                else {
                    listSize = parentEntities.size();

                    for (RecipeMetadataParentEntity p : parentEntities) {

                        RecipeMetadataPersistenceModel.Builder modelBuilder =
                                new RecipeMetadataPersistenceModel.Builder().getDefault();

                        modelBuilder.
                                setDataId(p.getDataId()).
                                setRecipeId(p.getRecipeId()).
                                setRecipeParentId(p.getRecipeParentId()).
                                setRecipeState(RecipeState.getById(p.getRecipeStateId())).
                                setCreatedBy(p.getCreatedBy()).
                                setCreateDate(p.getCreateDate()).
                                setLastUpdate(p.getLastUpdate());

                        addComponentStates(p.getDataId(), modelBuilder);
                    }
                    returnAll(callback); // todo - should this be somewhere else?
                }
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void addComponentStates(
            String parentDataId,
            RecipeMetadataPersistenceModel.Builder modelBuilder) {

        HashMap<ComponentName, ComponentState> componentStates = new HashMap<>();

        componentStateDataSource.getAllByParentDataId(
                parentDataId,
                new PrimitiveDataSource.GetAllCallback<RecipeComponentStateEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeComponentStateEntity> componentStateList) {

                        for (RecipeComponentStateEntity c : componentStateList) {
                            componentStates.put(
                                    ComponentName.getFromId(c.getComponentId()),
                                    ComponentState.getFromId(c.getComponentStateId()));
                        }
                        modelBuilder.setComponentStates(componentStates);

                        addComponentFailReasons(parentDataId, modelBuilder);
                    }

                    @Override
                    public void onDataUnavailable() {
                        addComponentFailReasons(parentDataId, modelBuilder);
                    }
                }
        );
    }

    private void addComponentFailReasons(
            String parentDataId,
            RecipeMetadataPersistenceModel.Builder modelBuilder) {

        recipeFailReasonsDataSource.getAllByParentDataId(
                parentDataId,
                new PrimitiveDataSource.GetAllCallback<RecipeFailReasonEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeFailReasonEntity> failReasons) {
                        List<FailReasons> fr = new ArrayList<>();

                        for (RecipeFailReasonEntity f : failReasons) {
                            fr.add(RecipeMetadata.FailReason.getById(f.getFailReasonId()));
                        }
                        modelBuilder.setFailReasons(fr);

                        persistenceModels.add(modelBuilder.build());
                        totalProcessed++;
                    }

                    @Override
                    public void onDataUnavailable() {
                        persistenceModels.add(modelBuilder.build());
                        totalProcessed ++;
                    }
                }
        );
    }

    private void returnAll(GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        if (totalProcessed == listSize) {
            totalProcessed = 0;
            listSize = 0;

            callback.onAllLoaded(persistenceModels);
        }
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        listSize = 1;
        RecipeMetadataPersistenceModel.Builder modelBuilder =
                new RecipeMetadataPersistenceModel.Builder();

        metadataSource.getByDataId(
                dataId,
                new PrimitiveDataSource.GetEntityCallback<RecipeMetadataParentEntity>() {
            @Override
            public void onEntityLoaded(RecipeMetadataParentEntity e) {
                modelBuilder.
                        setDataId(e.getDataId()).
                        setRecipeId(e.getRecipeId()).
                        setRecipeParentId(e.getRecipeParentId()).
                        setRecipeState(RecipeState.getById(e.getRecipeStateId())).
                        setCreatedBy(e.getCreatedBy()).
                        setCreateDate(e.getCreateDate()).
                        setLastUpdate(e.getLastUpdate());

                addComponentStates(e.getDataId(), modelBuilder);

            }

            @Override
            public void onDataUnavailable() {
                callback.onModelUnavailable();
            }
        });
    }

    @Override
    public void getByRecipeId(@Nonnull String recipeId,
                              @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {

    }

    @Override
    public void save(@Nonnull RecipeMetadataPersistenceModel pm) {
        // 1. Break persistence model into primitive models
        RecipeMetadataParentEntity pe = new RecipeMetadataParentEntity.Builder().
                setDataId(pm.getDataId()).
                setRecipeId(pm.getDomainId()).
                setRecipeParentId(pm.getRecipeParentId()).
                setRecipeStateId(pm.getRecipeState().getId()).
                setCreatedBy(pm.getCreatedBy()).
                setCreateDate(pm.getCreateDate()).
                setLastUpdate(pm.getLastUpdate()).
                build();

        for (ComponentName componentName : pm.getComponentStates().keySet()) {
            RecipeComponentStateEntity stateEntity = new RecipeComponentStateEntity(
                    idProvider.getUId(),
                    pe.getDataId(),
                    componentName.getId(),
                    pm.getComponentStates().get(componentName).getId()
            );
        }
        // 2. Save metadata parent
        // 3. Save ComponentStates list
        // 4. Save FailReasons list
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
