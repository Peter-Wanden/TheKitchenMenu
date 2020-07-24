package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseResult.ComponentState;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;

public class RecipeMetadataLocalGetAdapter {

    private static final String TAG = "tkm-" + RecipeMetadataLocalGetAdapter.class.
            getSimpleName() + ": ";

    @Nonnull
    private final RecipeMetadataParentLocalDataSource parentDataSource;
    @Nonnull
    private final RecipeComponentStateLocalDataSource componentStateDataSource;
    @Nonnull
    private final RecipeFailReasonsLocalDataSource recipeFailReasonsDataSource;
    @Nonnull
    private final Set<String> uniqueDomainIdList;
    @Nonnull
    private List<RecipeMetadataPersistenceModel> domainModels;

    private GetDomainModelCallback<RecipeMetadataPersistenceModel> getModelCallback;
    private GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> getAllCallback;

    private String activeDataId = "";
    private RecipeMetadataPersistenceModel.Builder modelBuilder;
    private int uniqueDomainIdListSize;
    private int domainModelsProcessed;

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

        uniqueDomainIdList = new HashSet<>();
        domainModels = new ArrayList<>();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {
        this.getModelCallback = callback;

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
                        getModelCallback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMetadataPersistenceModel> callback) {

        this.getModelCallback = callback;
        getParentEntitiesFromDomainId(domainId);
    }

    private void getParentEntitiesFromDomainId(String domainId) {
        parentDataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        filterForActiveParent(entities);
                    }

                    @Override
                    public void onDataUnavailable() {
                        getModelCallback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    private void filterForActiveParent(List<RecipeMetadataParentEntity> entities) {
        long lastUpdated = 0;
        activeDataId = "";

        RecipeMetadataParentEntity parentEntity = null;

        for (RecipeMetadataParentEntity e : entities) {
            if (e.getLastUpdate() > lastUpdated) {
                lastUpdated = e.getLastUpdate();
                activeDataId = e.getDataId();
                parentEntity = e;
            }
        }

        if (activeDataId.isEmpty()) {
            getModelCallback.onPersistenceModelUnavailable();
        } else {
            addParentEntityToModelBuilder(parentEntity);
            getFailReasonsFromParentDataId(activeDataId);
            getComponentStatesFromParentDataId(activeDataId);
        }
    }

    private void addParentEntityToModelBuilder(RecipeMetadataParentEntity e) {
        modelBuilder = new RecipeMetadataPersistenceModel.Builder();

        modelBuilder.
                setDataId(e.getDataId()).
                setDomainId(e.getDomainId()).
                setParentDomainId(e.getParentDomainId()).
                setRecipeState(ComponentState.fromInt(e.getRecipeStateId())).
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
            FailReason metadataRecipeMetadataFailReason = FailReason.getById(e.getFailReasonId());

            failReasons.add(commonFailReason == null ? metadataRecipeMetadataFailReason : commonFailReason);
        }
        modelBuilder.setFailReasons(failReasons);
        isFailReasonsAdded = true;
        createModel();
    }

    private void getComponentStatesFromParentDataId(String parentDataId) {
        componentStateDataSource.getAllByParentDataId(
                parentDataId,
                new GetAllPrimitiveCallback<RecipeComponentStateEntity>() {
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
                    ComponentState.fromInt(e.getComponentStateId())
            );
        }
        modelBuilder.setComponentStates(s);
        isComponentStatesAdded = true;
        createModel();
    }

    private void createModel() {
        if (isModelComplete()) {
            getModelCallback.onPersistenceModelLoaded(modelBuilder.build());
        }
    }

    private boolean isModelComplete() {
        return isParentAdded && isFailReasonsAdded && isComponentStatesAdded;
    }

    public void getAllActive(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataPersistenceModel> callback) {
        getAllCallback = callback;
        getAllParents();
    }

    private void getAllParents() {
        parentDataSource.getAll(
                new GetAllPrimitiveCallback<RecipeMetadataParentEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeMetadataParentEntity> entities) {
                        if (entities == null || entities.isEmpty()) {
                            getAllCallback.onDomainModelsUnavailable();
                        } else {
                            createUniqueDomainIdList(entities);
                        }
                    }

                    @Override
                    public void onDataUnavailable() {
                        getAllCallback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    private void createUniqueDomainIdList(List<RecipeMetadataParentEntity> es) {
        uniqueDomainIdList.clear();
        uniqueDomainIdListSize = 0;
        domainModelsProcessed = 0;
        domainModels.clear();

        for (RecipeMetadataParentEntity e : es) {
            uniqueDomainIdList.add(e.getDomainId());
        }
        uniqueDomainIdListSize = uniqueDomainIdList.size();
        getActiveModels();
    }

    private void getActiveModels() {
        for (String domainId : uniqueDomainIdList) {
            getActiveByDomainId(
                    domainId,
                    new GetDomainModelCallback<RecipeMetadataPersistenceModel>() {
                        @Override
                        public void onPersistenceModelLoaded(RecipeMetadataPersistenceModel model) {
                            domainModels.add(model);
                            domainModelsProcessed++;
                            returnAllDomainModels();
                        }

                        @Override
                        public void onPersistenceModelUnavailable() {
                            returnAllDomainModels();
                        }
                    }
            );
        }
    }
    
    private void returnAllDomainModels() {
        if (domainModelsProcessed == uniqueDomainIdListSize) {
            getAllCallback.onAllDomainModelsLoaded(domainModels);
        }
    }
}
