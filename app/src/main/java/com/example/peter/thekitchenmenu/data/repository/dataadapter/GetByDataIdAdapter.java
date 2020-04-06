package com.example.peter.thekitchenmenu.data.repository.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class GetByDataIdAdapter {

    public interface Callback {
        void onModelCreated(@Nonnull RecipeMetadataPersistenceModel model);
        void onDataUnavailable();
    }

    @Nonnull
    private final RecipeMetadataParentEntityLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;

    private final RecipeMetadataPersistenceModel.Builder modelBuilder;
    private Callback callback;
    private boolean isParentAdded;
    private boolean isComponentStatesAdded;
    private boolean isFailReasonsAdded;

    public GetByDataIdAdapter(
            @Nonnull RecipeMetadataParentEntityLocalDataSource parentDataSource,
            @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
            @Nonnull RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;

        modelBuilder = new RecipeMetadataPersistenceModel.Builder();
    }

    public void adaptToDomainModel(String dataId, Callback callback) {
        this.callback = callback;
        getParentEntityFromDataId(dataId);
        getComponentStatesFromParentDataId(dataId);
        getFailReasonsFromParentDataId(dataId);
    }

    private void getParentEntityFromDataId(String dataId) {
        parentDataSource.getByDataId(
                dataId,
                new PrimitiveDataSource.GetEntityCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeMetadataParentEntity e) {
                        addParentEntityToModelBuilder(e);
                        createModel();
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                });
    }

    private void addParentEntityToModelBuilder(RecipeMetadataParentEntity e) {
        modelBuilder.
                setDataId(e.getDataId()).
                setRecipeId(e.getDomainId()).
                setRecipeParentId(e.getRecipeParentId()).
                setRecipeState(RecipeState.getById(e.getRecipeStateId())).
                setCreatedBy(e.getCreatedBy()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLastUpdate());
        isParentAdded = true;
    }

    private void getComponentStatesFromParentDataId(String parentId) {
        componentStateDataSource.getAllByParentDataId(
                parentId,
                new PrimitiveDataSource.GetAllCallback<RecipeComponentStateEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeComponentStateEntity> entities) {
                        if (!entities.isEmpty()) {
                            addComponentStatesToModelBuilder(entities);
                        } else {
                            addComponentStatesToModelBuilder(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        addComponentStatesToModelBuilder(new ArrayList<>());
                    }
                }
        );
    }

    private void addComponentStatesToModelBuilder(List<RecipeComponentStateEntity> entities) {
        HashMap<ComponentName, ComponentState> componentStates =
                new HashMap<>();
        for (RecipeComponentStateEntity e : entities) {
            componentStates.put(
                    ComponentName.getFromId(e.getComponentNameId()),
                    ComponentState.getFromId(e.getComponentStateId()));
        }
        modelBuilder.setComponentStates(componentStates);
        isComponentStatesAdded = true;
        createModel();
    }

    private void getFailReasonsFromParentDataId(String parentId) {
        recipeFailReasonsDataSource.getAllByParentDataId(
                parentId,
                new PrimitiveDataSource.GetAllCallback<RecipeFailReasonEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeFailReasonEntity> entities) {
                        if (!entities.isEmpty()) {
                            addFailReasonsToModelBuilder(entities);
                        } else {
                            addFailReasonsToModelBuilder(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        addFailReasonsToModelBuilder(new ArrayList<>());
                    }
                }
        );
    }

    private void addFailReasonsToModelBuilder(List<RecipeFailReasonEntity> entities) {
        List<FailReasons> failReasons = new ArrayList<>();
        for (RecipeFailReasonEntity e : entities) {
            failReasons.add(FailReason.getById(e.getFailReasonId()));
        }
        modelBuilder.setFailReasons(failReasons);
        isFailReasonsAdded = true;
        createModel();
    }

    private void createModel() {
        if (isParentAdded && isComponentStatesAdded && isFailReasonsAdded) {
            callback.onModelCreated(modelBuilder.build());
        }
    }
}
