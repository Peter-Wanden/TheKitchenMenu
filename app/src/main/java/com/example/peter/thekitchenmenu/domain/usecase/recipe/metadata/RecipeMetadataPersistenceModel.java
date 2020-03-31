package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipePersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

/**
 * Represented in primitive form by {@link RecipeMetadataParentEntity}
 */
public final class RecipeMetadataPersistenceModel extends RecipePersistenceModel {

    private String recipeParentId;
    private RecipeState recipeState;
    private HashMap<ComponentName, ComponentState> componentStates;
    private List<FailReasons> failReasons;
    private String createdBy;
    private long createDate;
    private long lastUpdate;

    private RecipeMetadataPersistenceModel(){}

    public String getRecipeParentId() {
        return recipeParentId;
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

        @Override
        public Builder getDefault() {
            model.dataId = "";
            model.recipeId = "";
            model.recipeParentId = "";
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

        public Builder setRecipeId(String recipeId) {
            model.recipeId = recipeId;
            return self();
        }

        public Builder setRecipeParentId(String recipeParentId) {
            model.recipeParentId = recipeParentId;
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
