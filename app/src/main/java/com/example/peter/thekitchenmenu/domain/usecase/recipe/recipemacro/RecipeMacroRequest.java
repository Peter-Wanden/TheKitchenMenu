package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestModel;

import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeMacroRequest extends RecipeRequest<RecipeMacroRequest.Model> {

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMacroRequest{" +
                "id='" + id + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder extends RecipeRequestBuilder<Builder, RecipeMacroRequest, Model> {

        public Builder() {
            request = new RecipeMacroRequest();
        }

        public Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    public static final class Model extends RecipeRequestModel {
        private String recipeId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return recipeId.equals(model.recipeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(recipeId);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "recipeId='" + recipeId + '\'' +
                    '}';
        }

        public static class Builder extends RecipeRequestModelBuilder<Builder, Model> {

            public Builder() {
                model = new Model();
            }

            public static Builder getDefault() {
                return new Builder().setRecipeId("");
            }

            public Builder setRecipeId(String recipeId) {
                model.recipeId = recipeId;
                return self();
            }

            @Override
            protected Builder self() {
                return this;
            }
        }
    }
}
