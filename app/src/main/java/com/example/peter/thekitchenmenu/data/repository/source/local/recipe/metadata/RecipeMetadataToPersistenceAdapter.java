package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class RecipeMetadataToPersistenceAdapter {

    public interface Callback {
        void onModelCreated(RecipeMetadataPersistenceModel model);
        void onModelUnavailable();
    }

    @Nonnull
    private final PrimitiveDataSource<RecipeComponentStateEntity> componentStateDataSource;
    @Nonnull
    private final PrimitiveDataSource<RecipeFailReasonEntity> recipeFailReasonsDataSource;
    @Nonnull
    private final PrimitiveDataSource<RecipeMetadataParentEntity> parentDataSource;

    private final HashMap<ComponentName, ComponentState> componentStates;
    private final List<FailReasons> failReasons;

    private RecipeMetadataPersistenceModel.Builder modelBuilder;
    private RecipeMetadataParentEntity parentEntity;
    private boolean statesAdded;
    private boolean failReasonsAdded;
    private Callback callback;

    public RecipeMetadataToPersistenceAdapter(
            @Nonnull PrimitiveDataSource<RecipeMetadataParentEntity> parentDataSource,
            @Nonnull PrimitiveDataSource<RecipeComponentStateEntity> componentStateDataSource,
            @Nonnull PrimitiveDataSource<RecipeFailReasonEntity> recipeFailReasonsDataSource) {

        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;

        componentStates = new HashMap<>();
        failReasons = new ArrayList<>();
    }

    void createModelAndNotify(@Nonnull RecipeMetadataParentEntity parentEntity,
                              @Nonnull Callback callback) {
        resetState();
        this.parentEntity = parentEntity;
        this.callback = callback;

        getComponentStates();
        getFailReasons();
    }

    void createModelByDataIdAndNotify(@Nonnull String dataId, Callback callback) {
        resetState();
        this.callback = callback;
        getParentEntity(dataId);
    }

    private void getParentEntity(String dataId) {
        parentDataSource.getByDataId(
                dataId,
                new PrimitiveDataSource.GetEntityCallback<RecipeMetadataParentEntity>() {
            @Override
            public void onEntityLoaded(RecipeMetadataParentEntity e) {
                parentEntity = e;
                parentEntityLoaded = true;
            }

            @Override
            public void onDataUnavailable() {
                callback.onModelUnavailable();
            }
        });
    }

    private void getComponentStates() {
        componentStateDataSource.getAllByParentDataId(
                parentEntity.getDataId(),
                new PrimitiveDataSource.GetAllCallback<RecipeComponentStateEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeComponentStateEntity> entities) {
                        if (!entities.isEmpty()) {
                            addStates(entities);
                        } else {
                            statesAdded = true;
                            createModel();
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        statesAdded = true;
                        createModel();
                    }
                }
        );
    }

    private void addStates(List<RecipeComponentStateEntity> entities) {
        for (RecipeComponentStateEntity e : entities) {
            componentStates.put(
                    ComponentName.getFromId(e.getComponentNameId()),
                    ComponentState.getFromId(e.getComponentStateId()));
        }
        statesAdded = true;
        createModel();
    }

    private void getFailReasons() {
        recipeFailReasonsDataSource.getAllByParentDataId(
                parentEntity.getDataId(),
                new PrimitiveDataSource.GetAllCallback<RecipeFailReasonEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeFailReasonEntity> entities) {
                        if (!entities.isEmpty()) {
                            addFailReasons(entities);
                        } else {
                            failReasonsAdded = true;
                            createModel();
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        failReasonsAdded = true;
                        createModel();
                    }
                }
        );
    }

    private void addFailReasons(List<RecipeFailReasonEntity> failReasonEntities) {
        for (RecipeFailReasonEntity e : failReasonEntities) {
            failReasons.add(RecipeMetadata.FailReason.getById(e.getFailReasonId()));
        }
        failReasonsAdded = true;
        createModel();
    }

    private void createModel() {
        if (statesAdded && failReasonsAdded) {
            RecipeMetadataPersistenceModel persistenceModel =
                    new RecipeMetadataPersistenceModel.Builder().
                            setDataId(parentEntity.getDataId()).
                            setRecipeId(parentEntity.getRecipeId()).
                            setRecipeParentId(parentEntity.getRecipeParentId()).
                            setRecipeState(RecipeState.getById(parentEntity.getRecipeStateId())).
                            setComponentStates(componentStates).
                            setFailReasons(failReasons).
                            setCreatedBy(parentEntity.getCreatedBy()).
                            setCreateDate(parentEntity.getCreateDate()).
                            setLastUpdate(parentEntity.getLastUpdate()).
                            build();

            callback.onModelCreated(persistenceModel);
        }
    }

    private void resetState() {
        componentStates.clear();
        failReasons.clear();
        statesAdded = false;
        failReasonsAdded = false;
    }
}
