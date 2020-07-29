package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BasePersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;

/**
 * Represented in primitive form by {@link RecipeMetadataParentEntity},
 * {@link RecipeComponentStateEntity} & {@link RecipeFailReasonEntity}
 */
public final class RecipeMetadataPersistenceModel
        extends
        BasePersistenceModel {

    private String parentDomainId;
    private ComponentState componentState;
    private HashMap<ComponentName, ComponentState> componentStates;
    private List<FailReasons> failReasons;
    private String createdBy;

    private RecipeMetadataPersistenceModel(){}

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
            PersistenceModelBuilder<Builder, RecipeMetadataPersistenceModel> {

        public Builder() {
            domainModel = new RecipeMetadataPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.parentDomainId = "";
            domainModel.componentState = ComponentState.INVALID_UNCHANGED;
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
            domainModel.componentState = model.getComponentState();
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
            domainModel.componentState = state;
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
