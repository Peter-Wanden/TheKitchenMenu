package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.RecipeComponentMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeDataModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;

public final class RecipeResponse implements UseCase.Response {

    protected String id;
    protected Model model;

    public String getId() {
        return id;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponse that = (RecipeResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    @Override
    public String toString() {
        return "RecipeResponse{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private RecipeResponse response;

        public Builder() {
            response = new RecipeResponse();
        }

        public Builder setId(String id) {
            response.id = id;
            return this;
        }

        public Builder setModel(Model model) {
            response.model = model;
            return this;
        }

        public RecipeResponse build() {
            return response;
        }
    }

    public static final class Model extends RecipeDataModel {
        private RecipeStateResponse recipeStateResponse;
        private HashMap<ComponentName, Response> componentResponses;

        public Model() {}

        public RecipeStateResponse getRecipeStateResponse() {
            return recipeStateResponse;
        }

        public HashMap<ComponentName, Response> getComponentResponses() {
            return componentResponses;
        }

        public static class Builder extends RecipeDataModelBuilder<
                Builder,
                Model> {

            public Builder() {
                model = new Model();
            }

            public Builder getDefault() {
                return new Model.Builder().
                        setRecipeStateResponse(RecipeStateResponse.Builder.
                                getDefault().
                                build()).
                        setComponentResponses(getDefaultComponentResponses());
            }

            public Builder setRecipeStateResponse(RecipeStateResponse response) {
                model.recipeStateResponse = response;
                return self();
            }

            public Builder setComponentResponses(HashMap<ComponentName, Response> componentResponses) {
                model.componentResponses = componentResponses;
                return self();
            }

            private static HashMap<ComponentName, Response> getDefaultComponentResponses() {
                return new LinkedHashMap<>();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
