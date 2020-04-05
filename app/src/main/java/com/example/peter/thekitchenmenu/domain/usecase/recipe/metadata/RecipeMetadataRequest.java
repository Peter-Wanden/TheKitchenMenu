package com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainMessageBasePlusModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

public final class RecipeMetadataRequest
        extends UseCaseDomainMessageBasePlusModel<RecipeMetadataRequest.Model> {

    @Override
    public String toString() {
        return "RecipeMetadataRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder
            extends UseCaseMessageBuilderWithModel<Builder, RecipeMetadataRequest, Model> {

        public Builder() {
            request = new RecipeMetadataRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setDataId("").
                    setModel(new Model.Builder().
                            getDefault().
                            build()
                    );
        }

        public Builder basedOnResponse(RecipeMetadataResponse response) {
            request.dataId = response.getId();
            request.model.parentId = response.getModel().getParentId();
            request.model.componentStates = response.getModel().getComponentStates();
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
                return new Builder().
                        setParentId("").
                        setComponentStates(new HashMap<>());
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
