package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public class RecipeMetadataLocalGetAdapter {

    private static final String TAG = "tkm-" + RecipeMetadataLocalGetAdapter.class.
            getSimpleName() + ": ";

    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;

    private RecipeMetadataPersistenceModel.Builder modelBuilder;
    private GetDomainModelCallback<RecipeMetadataPersistenceModel> callback;
    private String activeDataId = "";
    private boolean isParentAdded;
    private boolean isComponentStatesAdded;
    private boolean isFailReasonsAdded;

    public RecipeMetadataLocalGetAdapter(
            @Nonnull RecipeMetadataParentLocalDataSource parentDataSource,
            @Nonnull RecipeComponentStateLocalDataSource componentStateDataSource,
            @Nonnull RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource) {
        this.parentDataSource = parentDataSource;
        this.componentStateDataSource = componentStateDataSource;
        this.recipeFailReasonsDataSource = recipeFailReasonsDataSource;
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        setupAdapter();
        this.callback = callback;

        getParentEntityFromDataId(dataId);
        getFailReasonsFromParentDataId(dataId);
        getComponentStatesFromParentDataId(dataId);
    }

    private void getParentEntityFromDataId(String dataId) {
        parentDataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeMetadataParentEntity e) {
                        addParentEntityToModelBuilder(e);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    private void addParentEntityToModelBuilder(RecipeMetadataParentEntity e) {
        modelBuilder.
                setDataId(e.getDataId()).
                setDomainId(e.getDomainId()).
                setParentDomainId(e.getParentDomainId()).
                setRecipeState(RecipeState.getById(e.getRecipeStateId())).
                setCreatedBy(e.getCreatedBy()).
                setCreateDate(e.getCreateDate()).
                setLastUpdate(e.getLastUpdate()
                );
        isParentAdded = true;
        createModel();
    }

    private void getFailReasonsFromParentDataId(String parentDataId) {
        recipeFailReasonsDataSource.getAllByParentDataId(
                parentDataId,
                new GetAllPrimitiveCallback<RecipeFailReasonEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeFailReasonEntity> entities) {
                        if (entities.isEmpty()) {
                            addFailReasonsToModelBuilder(new ArrayList<>());
                        } else {
                            addFailReasonsToModelBuilder(entities);
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
            CommonFailReason commonFailReason = CommonFailReason.getFromId(e.getFailReasonId());
            RecipeMetadata.FailReason metadataFailReason = FailReason.getById(e.getFailReasonId());

            failReasons.add(commonFailReason == null ? metadataFailReason : commonFailReason);
        }
        modelBuilder.setFailReasons(failReasons);
        isFailReasonsAdded = true;
        createModel();
    }

    private void getComponentStatesFromParentDataId(String parentDataId) {
        componentStateDataSource.getAllByParentDataId(
                parentDataId, new GetAllPrimitiveCallback<RecipeComponentStateEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeComponentStateEntity> entities) {
                        if (entities.isEmpty()) {
                            addComponentStatesToModelBuilder(new ArrayList<>());
                        } else {
                            addComponentStatesToModelBuilder(entities);
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
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        for (RecipeComponentStateEntity e : entities) {
            s.put(
                    ComponentName.getFromId(e.getComponentNameId()),
                    ComponentState.getFromId(e.getComponentStateId())
            );
        }
        modelBuilder.setComponentStates(s);
        isComponentStatesAdded = true;
        createModel();
    }

    private void createModel() {
        if (isParentAdded && isFailReasonsAdded && isComponentStatesAdded)
            callback.onModelLoaded(modelBuilder.build());
    }

    public void getActiveModelFromDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        setupAdapter();
        this.callback = callback;
        getLatestParentEntityIdFromDomainId(domainId);
    }

    private void getLatestParentEntityIdFromDomainId(String domainId) {
        parentDataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        filterForActive(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    private void filterForActive(List<RecipeMetadataParentEntity> entities) {
        long lastUpdated = 0;
        activeDataId = "";

        for (RecipeMetadataParentEntity e : entities) {
            if (e.getLastUpdate() > lastUpdated) {
                lastUpdated = e.getLastUpdate();
                activeDataId = e.getDataId();
            }
        }
        if (activeDataId.isEmpty()) {
            callback.onModelUnavailable();
        } else {
            getByDataId(activeDataId, callback);
        }
    }

    private void setupAdapter() {
        callback = null;
        activeDataId = "";
        isParentAdded = false;
        isComponentStatesAdded = false;
        isFailReasonsAdded = false;
        modelBuilder = new RecipeMetadataPersistenceModel.Builder().getDefault();
    }
}
