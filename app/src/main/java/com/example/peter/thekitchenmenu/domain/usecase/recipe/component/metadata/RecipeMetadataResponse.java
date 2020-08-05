package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

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

    private RecipeMetadataResponse() {
    }

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

        private HashMap<RecipeComponentName, ComponentState> componentStates;

        private DomainModel() {
        }

        public HashMap<RecipeComponentName, ComponentState> getComponentStates() {
            return componentStates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DomainModel domainModel = (DomainModel) o;
            return Objects.equals(componentStates, domainModel.componentStates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(componentStates);
        }

        @Nonnull
        @Override
        public String toString() {
            return "DomainModel{" +
                    ", componentStates=" + componentStates +
                    '}';
        }

        public static class Builder
                extends
                BaseDomainModelBuilder<Builder, DomainModel> {

            public Builder() {
                super(new DomainModel());
            }

            @Override
            public Builder basedOnModel(DomainModel model) {
                return null;
            }

            @Override
            public Builder getDefault() {
                domainModel.componentStates = new HashMap<>();
                return self();
            }

            public Builder setComponentStates(
                    HashMap<RecipeComponentName, ComponentState> componentStates) {
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
