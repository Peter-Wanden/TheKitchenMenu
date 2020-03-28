package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeMetadataEntity;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipePersistenceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

/**
 * Represented in primitive form by {@link RecipeMetadataEntity}
 */
public final class RecipeMetadataPersistenceModel extends RecipePersistenceModel {

    private String parentId;
    private HashMap<ComponentName, ComponentState> componentStates;
    private List<FailReasons> failReasons;
    private String createdBy;
    private long createDate;
    private long lastUpdate;

    private RecipeMetadataPersistenceModel(){}

    public String getParentId() {
        return parentId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeMetadataPersistenceModel that = (RecipeMetadataPersistenceModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(recipeId, that.recipeId) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(componentStates, that.componentStates) &&
                Objects.equals(failReasons, that.failReasons) &&
                Objects.equals(createdBy, that.createdBy) &&
                createDate == that.createDate &&
                lastUpdate == that.lastUpdate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, recipeId, parentId, componentStates, failReasons,
                createdBy, createDate, lastUpdate);
    }

    public static class Builder extends DomainModelBuilder<
                    Builder,
                    RecipeMetadataPersistenceModel> {

        public Builder setId(String id) {
            model.id = id;
            return self();
        }

        public Builder setRecipeId(String recipeId) {
            model.recipeId = recipeId;
            return self();
        }

        public Builder setParentId(String parentId) {
            model.parentId = parentId;
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
