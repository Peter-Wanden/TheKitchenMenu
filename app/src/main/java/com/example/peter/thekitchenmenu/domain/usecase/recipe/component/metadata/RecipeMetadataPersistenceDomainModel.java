package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.model.BasePersistenceDomainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

/**
 * Represented in primitive form by {@link RecipeMetadataParentEntity},
 * {@link RecipeComponentStateEntity} & {@link RecipeFailReasonEntity}
 */
public final class RecipeMetadataPersistenceDomainModel
        extends
        BasePersistenceDomainModel {

    private String parentDomainId;
    private ComponentState componentState;
    private HashMap<ComponentName, ComponentState> componentStates;
    private List<FailReasons> failReasons;
    private String createdBy;

    private RecipeMetadataPersistenceDomainModel(){}

    public String getParentDomainId() {
        return parentDomainId;
    }

    public ComponentState getComponentState() {
        return componentState;
    }

    public HashMap<ComponentName, ComponentState> getComponentStates() {
        return componentStates;
    }

    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataPersistenceModel{" +
                "parentDomainId='" + parentDomainId + '\'' +
                ", componentState=" + componentState +
                ", componentStates=" + componentStates +
                ", failReasons=" + failReasons +
                ", createdBy='" + createdBy + '\'' +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, RecipeMetadataPersistenceDomainModel> {

        public Builder() {
            persistenceModel = new RecipeMetadataPersistenceDomainModel();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.parentDomainId = "";
            persistenceModel.componentState = ComponentState.INVALID_UNCHANGED;
            persistenceModel.componentStates = new HashMap<>();
            persistenceModel.failReasons = new ArrayList<>();
            persistenceModel.createdBy = Constants.getUserId();
            persistenceModel.createDate = 0L;
            persistenceModel.lastUpdate = 0L;
            return self();
        }

        @Override
        public Builder basedOnModel(RecipeMetadataPersistenceDomainModel model) {
            persistenceModel.dataId = model.getDataId();
            persistenceModel.domainId = model.getDomainId();
            persistenceModel.parentDomainId = model.getParentDomainId();
            persistenceModel.componentState = model.getComponentState();
            persistenceModel.componentStates = model.getComponentStates();
            persistenceModel.failReasons = model.getFailReasons();
            persistenceModel.createdBy = model.getCreatedBy();
            persistenceModel.createDate = model.getCreateDate();
            persistenceModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        public Builder setParentDomainId(String parentDomainId) {
            persistenceModel.parentDomainId = parentDomainId;
            return self();
        }

        public Builder setRecipeState(ComponentState state) {
            persistenceModel.componentState = state;
            return self();
        }

        public Builder setComponentStates(HashMap<ComponentName, ComponentState> componentStates) {
            persistenceModel.componentStates = componentStates;
            return self();
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            persistenceModel.failReasons = failReasons;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            persistenceModel.createdBy = createdBy;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
