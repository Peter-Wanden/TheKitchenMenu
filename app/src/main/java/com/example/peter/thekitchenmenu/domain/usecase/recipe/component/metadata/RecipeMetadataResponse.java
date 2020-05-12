package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModelMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseMetadata;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public final class RecipeMetadataResponse
        extends BaseDomainMessageModelMetadata<RecipeMetadataResponse.Model>
        implements UseCase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", metadata=" + metadata +
                ", model=" + model +
                "'}'";
    }

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
        private String parentDomainId;
        private HashMap<ComponentName, ComponentState> componentStates;

        private Model() {}

        @Nonnull
        public String getParentDomainId() {
            return parentDomainId;
        }

        public HashMap<ComponentName, ComponentState> getComponentStates() {
            return componentStates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return Objects.equals(parentDomainId, model.parentDomainId) &&
                    Objects.equals(componentStates, model.componentStates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentDomainId, componentStates);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "parentDomainId='" + parentDomainId + '\'' +
                    ", componentStates=" + componentStates +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                model.parentDomainId = "";
                model.componentStates = new HashMap<>();
                return self();
            }

            public Builder setParentDomainId(String parentId) {
                model.parentDomainId = parentId;
                return self();
            }

            public Builder setComponentStates(
                    HashMap<ComponentName, ComponentState> componentStates) {
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
