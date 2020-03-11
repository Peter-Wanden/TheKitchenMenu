package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestBase;

import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeMacroRequest extends RecipeRequestBase<RecipeMacroRequest.Model> {

    private RecipeMacroRequest(@Nonnull String id, @Nonnull RecipeMacroRequest.Model model) {
        super(id, model);
    }

    @Override
    public Model getModel() {
        return model;
    }

    public static class Builder {
        private String id;
        private Model model;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setModel(Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setModel(Model model) {
            this.model = model;
            return this;
        }

        public RecipeMacroRequest build() {
            return new RecipeMacroRequest(
                    id,
                    model
            );
        }
    }

    public static final class Model implements RecipeRequestBase.RecipeRequestModel {
        @Nonnull
        private final String recipeId;

        private Model(@Nonnull String recipeId) {
            this.recipeId = recipeId;
        }

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

        public static class Builder {
            private String recipeId;

            public static Builder getDefault() {
                return new Builder().setRecipeId("");
            }

            public Builder setRecipeId(String recipeId) {
                this.recipeId = recipeId;
                return this;
            }

            public Model build() {
                return new Model(
                        recipeId
                );
            }

        }
    }
}
