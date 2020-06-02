package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public final class RecipeMetadataRequest
        extends UseCaseMessageModelDataId<RecipeMetadataRequest.Model>
        implements UseCaseBase.Request {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMetadataRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipeMetadataRequest() {
    }

    public static class Builder
            extends UseCaseMessageModelDataIdBuilder<Builder, RecipeMetadataRequest, Model> {

        public Builder() {
            message = new RecipeMetadataRequest();
            message.model = new Model();
        }

        public Builder getDefault() {
            message.dataId = NO_ID;
            message.domainId = NO_ID;
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        public Builder basedOnResponse(RecipeMetadataResponse r) {
            message.dataId = r.getDataId();
            message.domainId = r.getDomainId();
            message.model.parentDomainId = r.getDomainModel().getParentDomainId();
            message.model.componentStates = r.getDomainModel().getComponentStates();
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

        @Nonnull
        public String getParentDomainId() {
            return parentDomainId;
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
            return Objects.equals(parentDomainId, model.parentDomainId) &&
                    Objects.equals(componentStates, model.componentStates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentDomainId, componentStates);
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "parentDomainId='" + parentDomainId + '\'' +
                    ", componentStates=" + componentStates +
                    '}';
        }

        public static class Builder extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                domainModel = new Model();
            }

            public Builder getDefault() {
                domainModel.parentDomainId = "";
                domainModel.componentStates = new HashMap<>();
                return self();
            }

            public Builder basedOnResponseModel(RecipeMetadataResponse.Model m) {
                domainModel.parentDomainId = m.getParentDomainId();
                domainModel.componentStates = m.getComponentStates();
                return self();
            }

            public Builder setParentId(String parentId) {
                domainModel.parentDomainId = parentId;
                return self();
            }

            public Builder setComponentStates(HashMap<ComponentName,
                    ComponentState> componentStates) {
                domainModel.componentStates = componentStates;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
