package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

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

        public Builder basedOnResponse(RecipeMetadataResponse response) {
            message.dataId = response.getDataId();
            message.domainId = response.getDomainId();
            message.model.componentStates = response.getDomainModel().getComponentStates();
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

        @Nonnull
        public HashMap<RecipeComponentName, ComponentState> getComponentStates() {
            return componentStates;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DomainModel)) return false;

            DomainModel that = (DomainModel) o;

            return Objects.equals(componentStates, that.componentStates);
        }

        @Override
        public int hashCode() {
            return componentStates != null ? componentStates.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "DomainModel{" +
                    "componentStates=" + componentStates +
                    '}';
        }

        public static class Builder
                extends
                BaseDomainModelBuilder<
                        Builder,
                        DomainModel> {

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


            public Builder basedOnResponseModel(RecipeMetadataResponse.DomainModel m) {
                domainModel.componentStates = m.getComponentStates();
                return self();
            }

            public Builder setComponentStates(HashMap<RecipeComponentName,
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