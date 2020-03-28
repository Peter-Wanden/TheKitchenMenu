package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public final class RecipeMetadataResponse extends UseCaseResponse<RecipeMetadataResponse.Model> {

    @Override
    public String toString() {
        return "RecipeMetadataResponse{" +
                "id='" + id + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                '}';
    }

    private RecipeMetadataResponse() {
    }

    public static class Builder extends UseCaseResponseBuilder<
            Builder,
            RecipeMetadataResponse,
            Model> {

        public Builder() {
            response = new RecipeMetadataResponse();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setMetadata(new UseCaseMetadata.Builder().
                            getDefault().
                            build()).
                    setModel(new Model.Builder().
                            getDefault().
                            build());
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends UseCaseDomainModel {
        private String parentId;
        private RecipeState state;
        private List<FailReasons> failReasons;
        private HashMap<ComponentName, ComponentState> componentStates;
        private long createDate;
        private long lastUpdate;

        private Model() {
        }

        @Nonnull
        public String getParentId() {
            return parentId;
        }

        public RecipeState getState() {
            return state;
        }

        public List<FailReasons> getFailReasons() {
            return failReasons;
        }

        public HashMap<ComponentName, ComponentState> getComponentStates() {
            return componentStates;
        }

        public long getCreateDate() {
            return createDate;
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        @Override
        public String toString() {
            return "Model{" +
                    "parentId='" + parentId + '\'' +
                    ", state=" + state +
                    ", failReasons=" + failReasons +
                    ", componentStates=" + componentStates +
                    ", createDate=" + createDate +
                    ", lastUpdate=" + lastUpdate +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<
                Builder,
                Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.parentId = "";
                model.state = RecipeState.INVALID_UNCHANGED;
                model.failReasons = getDefaultFailReasons();
                model.componentStates = new HashMap<>();
                return self();
            }

            public Builder setParentId(String parentId) {
                model.parentId = parentId;
                return self();
            }

            public Builder setRecipeState(RecipeState state) {
                model.state = state;
                return self();
            }

            public Builder setFailReasons(List<FailReasons> failReasons) {
                model.failReasons = failReasons;
                return self();
            }

            public Builder setComponentStates(
                    HashMap<ComponentName, ComponentState> componentStates) {
                model.componentStates = componentStates;
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

            private List<FailReasons> getDefaultFailReasons() {
                List<FailReasons> defaultFailReasons = new ArrayList<>();
                defaultFailReasons.add(FailReason.MISSING_COMPONENTS);
                return defaultFailReasons;
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
