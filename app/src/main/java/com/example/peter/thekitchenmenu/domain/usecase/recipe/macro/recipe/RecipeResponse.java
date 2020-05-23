package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.MessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.Nonnull;


public final class RecipeResponse
        extends MessageModelDataId<RecipeResponse.Model>
        implements UseCaseBase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    private RecipeResponse() {
    }

    public static class Builder
            extends MessageModelDataIdBuilder<Builder, RecipeResponse, Model> {

        public Builder() {
            message = new RecipeResponse();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new Model.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends BaseDomainModel {

        private HashMap<RecipeMetadata.ComponentName, UseCaseBase.Response> componentResponses;

        public Model() {
        }

        public HashMap<RecipeMetadata.ComponentName, UseCaseBase.Response> getComponentResponses() {
            return componentResponses;
        }

        @Override
        public String toString() {
            return "Model{" +
                    "componentResponses=" + componentResponses +
                    '}';
        }

        public static class Builder
                extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                domainModel = new Model();
            }

            public Builder getDefault() {
                return new Model.Builder().setComponentResponses(getDefaultComponentResponses());
            }

            public Builder setComponentResponses(
                    HashMap<RecipeMetadata.ComponentName, UseCaseBase.Response> componentResponses) {
                domainModel.componentResponses = componentResponses;
                return self();
            }

            private static HashMap<RecipeMetadata.ComponentName, UseCaseBase.Response>
            getDefaultComponentResponses() {
                return new LinkedHashMap<>();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
