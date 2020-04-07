package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainMessageModel;
import com.example.peter.thekitchenmenu.domain.usecase.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;

import java.util.HashMap;
import java.util.LinkedHashMap;


public final class RecipeResponse
        extends BaseDomainMessageModel<RecipeResponse.Model>
        implements UseCase.Response {

    private RecipeResponse() {
    }

    public static class Builder
            extends UseCaseMessageBuilderModel<Builder, RecipeResponse, Model> {

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
        private HashMap<RecipeMetadata.ComponentName, UseCase.Response> componentResponses;

        public Model() {
        }

        public HashMap<RecipeMetadata.ComponentName, UseCase.Response> getComponentResponses() {
            return componentResponses;
        }

        public static class Builder
                extends DomainModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Model.Builder().setComponentResponses(getDefaultComponentResponses());
            }

            public Builder setComponentResponses(
                    HashMap<RecipeMetadata.ComponentName, UseCase.Response> componentResponses) {
                model.componentResponses = componentResponses;
                return self();
            }

            private static HashMap<RecipeMetadata.ComponentName, UseCase.Response>
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
