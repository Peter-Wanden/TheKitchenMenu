package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;

public final class RecipeMetadataRequest
        extends
        UseCaseMessageModelDataId<RecipeMetadataRequest.DomainModel>
        implements
        UseCaseBase.Request {

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
            extends
            UseCaseMessageModelDataIdBuilder<
                    Builder,
                    RecipeMetadataRequest,
                    DomainModel> {

        public Builder() {
            message = new RecipeMetadataRequest();
            message.model = new DomainModel();
        }

        @Override
        public Builder getDefault() {
            message.dataId = NO_ID;
            message.domainId = NO_ID;
            message.model = new DomainModel.Builder().getDefault().build();
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

    public static final class DomainModel
            extends
            BaseDomainModel {

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
            DomainModel domainModel = (DomainModel) o;
            return Objects.equals(parentDomainId, domainModel.parentDomainId) &&
                    Objects.equals(componentStates, domainModel.componentStates);
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

        public static class Builder
                extends
                DomainModelBuilder<
                                        Builder,
                                        DomainModel> {

            public Builder() {
                domainModel = new DomainModel();
            }

            @Override
            public Builder getDefault() {
                domainModel.parentDomainId = "";
                domainModel.componentStates = new HashMap<>();
                return self();
            }


            public Builder basedOnResponseModel(RecipeMetadataResponse.DomainModel m) {
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
