package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata.ComponentState;

public final class RecipeMetadataResponse
        extends
        UseCaseMessageModelDataIdMetadata<RecipeMetadataResponse.DomainModel>
        implements
        UseCaseBase.Response {

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
            extends
            MessageModelDataIdMetadataBuilder<
                    Builder,
                    RecipeMetadataResponse,
                    DomainModel> {

        public Builder() {
            message = new RecipeMetadataResponse();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
            message.model = new DomainModel.Builder().getDefault().build();
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

        private DomainModel() {}

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
            return "DomainModel{" +
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

            public Builder setParentDomainId(String parentId) {
                domainModel.parentDomainId = parentId;
                return self();
            }

            public Builder setComponentStates(
                    HashMap<ComponentName, ComponentState> componentStates) {
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
