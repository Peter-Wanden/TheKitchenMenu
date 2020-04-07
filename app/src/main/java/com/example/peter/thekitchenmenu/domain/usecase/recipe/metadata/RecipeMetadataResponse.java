package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModelMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public final class RecipeMetadataResponse
        extends BaseDomainMessageModelMetadata<RecipeMetadataResponse.Model>
        implements UseCase.Response {

    private RecipeMetadataResponse() {}

    public static class Builder
            extends UseCaseMessageBuilderMetadata<Builder, RecipeMetadataResponse, Model> {

        public Builder() {
            message = new RecipeMetadataResponse();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadata.Builder().getDefault().build();
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {
        private String parentId;
        private RecipeState state;
        private List<FailReasons> failReasons;
        private HashMap<ComponentName, ComponentState> componentStates;
        private long createDate;
        private long lastUpdate;

        private Model() {}

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
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return createDate == model.createDate &&
                    lastUpdate == model.lastUpdate &&
                    Objects.equals(parentId, model.parentId) &&
                    state == model.state &&
                    Objects.equals(failReasons, model.failReasons) &&
                    Objects.equals(componentStates, model.componentStates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentId, state, failReasons, componentStates, createDate,
                    lastUpdate);
        }

        @Nonnull
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

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.parentId = "";
                model.state = RecipeState.INVALID_UNCHANGED;
                model.failReasons = getDefaultFailReasons();
                model.componentStates = new HashMap<>();
                model.createDate = 0L;
                model.lastUpdate = 0L;
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
