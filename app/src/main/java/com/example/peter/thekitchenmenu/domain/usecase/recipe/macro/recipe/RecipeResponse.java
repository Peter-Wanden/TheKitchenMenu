package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.Nonnull;


public final class RecipeResponse
        extends
        UseCaseMessageModelDataId<RecipeResponse.Model>
        implements
        UseCaseBase.Response {

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
            extends
            UseCaseMessageModelDataIdBuilder<
                    Builder,
                    RecipeResponse,
                    Model> {

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

    public static final class Model
            extends
            BaseDomainModel {

        private HashMap<RecipeComponentName, UseCaseBase.Response> componentResponses;

        public Model() {
        }

        public HashMap<RecipeComponentName, UseCaseBase.Response> getComponentResponses() {
            return componentResponses;
        }

        @Nonnull
        @Override
        public String toString() {
            return "Model{" +
                    "componentResponses=" + componentResponses +
                    '}';
        }

        public static class Builder
                extends
                BaseDomainModelBuilder<
                        Builder,
                        Model> {

            public Builder() {
                domainModel = new Model();
            }

            @Override
            public Builder basedOnModel(Model model) {
                domainModel.componentResponses = model.getComponentResponses();
                return self();
            }

            @Override
            public Builder getDefault() {
                return new Model.Builder().setComponentResponses(getDefaultComponentResponses());
            }

            public Builder setComponentResponses(
                    HashMap<RecipeComponentName, UseCaseBase.Response> componentResponses) {
                domainModel.componentResponses = componentResponses;
                return self();
            }

            private static HashMap<RecipeComponentName, UseCaseBase.Response>
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
