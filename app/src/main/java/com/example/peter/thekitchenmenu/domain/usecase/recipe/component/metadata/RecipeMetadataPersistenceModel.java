package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainPersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

/**
 * Represented in primitive form by {@link RecipeMetadataParentEntity},
 * {@link RecipeComponentStateEntity} & {@link RecipeFailReasonEntity}
 */
public final class RecipeMetadataPersistenceModel extends BaseDomainPersistenceModel {

    private String parentDomainId;
    private ComponentState recipeState;
    private HashMap<ComponentName, ComponentState> componentStates;
    private List<FailReasons> failReasons;
    private String createdBy;

    private RecipeMetadataPersistenceModel(){}

    public String getParentDomainId() {
        return parentDomainId;
    }

    public ComponentState getRecipeState() {
        return recipeState;
    }

    public HashMap<ComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeMetadataPersistenceModel> {

        public Builder() {
            domainModel = new RecipeMetadataPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.parentDomainId = "";
            domainModel.recipeState = ComponentState.INVALID_UNCHANGED;
            domainModel.componentStates = new HashMap<>();
            domainModel.failReasons = new ArrayList<>();
            domainModel.createdBy = Constants.getUserId();
            domainModel.createDate = 0L;
            domainModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeMetadataPersistenceModel model) {
            domainModel.dataId = model.getDataId();
            domainModel.domainId = model.getDomainId();
            domainModel.parentDomainId = model.getParentDomainId();
            domainModel.recipeState = model.getRecipeState();
            domainModel.componentStates = model.getComponentStates();
            domainModel.failReasons = model.getFailReasons();
            domainModel.createdBy = model.getCreatedBy();
            domainModel.createDate = model.getCreateDate();
            domainModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setParentDomainId(String parentDomainId) {
            domainModel.parentDomainId = parentDomainId;
            return self();
        }

        public Builder setRecipeState(ComponentState state) {
            domainModel.recipeState = state;
            return self();
        }

        public Builder setComponentStates(HashMap<ComponentName, ComponentState> componentStates) {
            domainModel.componentStates = componentStates;
            return self();
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            domainModel.failReasons = failReasons;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            domainModel.createdBy = createdBy;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
