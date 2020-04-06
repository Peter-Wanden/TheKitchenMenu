package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBaseModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public final class RecipeMetadataRequest
        extends UseCaseDomainMessageBaseModel<RecipeMetadataRequest.Model>
        implements UseCase.Request {

    private RecipeMetadataRequest() {}

    public static class Builder
            extends UseCaseMessageBuilderModel<Builder, RecipeMetadataRequest, Model> {

        public Builder() {
            message = new RecipeMetadataRequest();
        }

        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeMetadataResponse r) {
            message.dataId = r.getDataId();
            message.domainId = r.getDomainId();
            message.model.parentId = r.getModel().getParentId();
            message.model.componentStates = r.getModel().getComponentStates();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends UseCaseDomainModel {
        private String parentId;
        private HashMap<ComponentName, ComponentState> componentStates;

        @Nonnull
        public String getParentId() {
            return parentId;
        }

        @Nonnull
        public HashMap<ComponentName, ComponentState> getComponentStates() {
            return componentStates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Objects.equals(parentId, model.parentId) &&
                    Objects.equals(componentStates, model.componentStates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentId, componentStates);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "parentId='" + parentId + '\'' +
                    ", componentStates=" + componentStates +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.parentId = "";
                model.componentStates = new HashMap<>();
                return self();
            }

            public Builder setParentId(String parentId) {
                model.parentId = parentId;
                return self();
            }

            public Builder setComponentStates(HashMap<ComponentName,
                    ComponentState> componentStates) {
                model.componentStates = componentStates;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
