package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.BasePersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

/**
 * Represented in primitive form by {@link RecipeMetadataParentEntity},
 * {@link RecipeComponentStateEntity} & {@link RecipeFailReasonEntity}
 */
public final class RecipeMetadataPersistenceModel extends BasePersistence {

    private String parentDomainId;
    private RecipeState recipeState;
    private HashMap<ComponentName, ComponentState> componentStates;
    private List<FailReasons> failReasons;
    private String createdBy;
    private long createDate;
    private long lastUpdate;

    private RecipeMetadataPersistenceModel(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeMetadataPersistenceModel that = (RecipeMetadataPersistenceModel) o;
        return dataId.equals(that.dataId) &&
                domainId.equals(that.domainId) &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate &&
                Objects.equals(parentDomainId, that.parentDomainId) &&
                recipeState == that.recipeState &&
                Objects.equals(componentStates, that.componentStates) &&
                Objects.equals(failReasons, that.failReasons) &&
                Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, domainId, parentDomainId, recipeState, componentStates,
                failReasons, createdBy, createDate, lastUpdate);
    }

    public String getParentDomainId() {
        return parentDomainId;
    }

    public RecipeState getRecipeState() {
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

    public long getCreateDate() {
        return createDate;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }


    public static class Builder extends DomainModelBuilder<
            Builder,
            RecipeMetadataPersistenceModel> {

        public Builder() {
            model = new RecipeMetadataPersistenceModel();
        }

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.domainId = "";
            model.parentDomainId = "";
            model.recipeState = RecipeState.DATA_UNAVAILABLE;
            model.componentStates = new HashMap<>();
            model.failReasons = new ArrayList<>();
            model.createdBy = Constants.getUserId();
            model.createDate = 0L;
            model.lastUpdate = 0L;
            return self();
        }

        public Builder setDataId(String dataId) {
            model.dataId = dataId;
            return self();
        }

        public Builder setDomainId(String domainId) {
            model.domainId = domainId;
            return self();
        }

        public Builder setParentDomainId(String parentDomainId) {
            model.parentDomainId = parentDomainId;
            return self();
        }

        public Builder setRecipeState(RecipeState recipeState) {
            model.recipeState = recipeState;
            return self();
        }

        public Builder setComponentStates(HashMap<ComponentName, ComponentState> componentStates) {
            model.componentStates = componentStates;
            return self();
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            model.failReasons = failReasons;
            return self();
        }

        public Builder setCreatedBy(String createdBy) {
            model.createdBy = createdBy;
            return self();
        }

        public Builder setCreateDate(long createDate) {
            model.createDate = createDate;
            return self();
        }

        public Builder setLastUpdate(long lastUpdate) {
            model.lastUpdate = lastUpdate;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
