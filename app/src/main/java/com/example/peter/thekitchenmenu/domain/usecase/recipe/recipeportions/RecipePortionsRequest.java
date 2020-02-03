package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsRequest implements UseCaseCommand.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nonnull
    private final RecipePortionsRequest.Model model;

    private RecipePortionsRequest(@Nonnull String recipeId,
                                  @Nonnull String cloneToRecipeId,
                                  @Nonnull RecipePortionsRequest.Model model) {
        this.recipeId = recipeId;
        this.cloneToRecipeId = cloneToRecipeId;
        this.model = model;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public String getCloneToRecipeId() {
        return cloneToRecipeId;
    }

    @Nonnull
    public RecipePortionsRequest.Model getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePortionsRequest request = (RecipePortionsRequest) o;
        return recipeId.equals(request.recipeId) &&
                cloneToRecipeId.equals(request.cloneToRecipeId) &&
                model.equals(request.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, cloneToRecipeId, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsRequest{" +
                "recipeId='" + recipeId + '\'' +
                ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String recipeId;
        private String cloneToRecipeId;
        private RecipePortionsRequest.Model model;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setCloneToRecipeId("").
                    setModel(RecipePortionsRequest.Model.Builder.
                            getDefault().
                            build());
        }

        public Builder setRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setCloneToRecipeId(String cloneToRecipeId) {
            this.cloneToRecipeId = cloneToRecipeId;
            return this;
        }

        public Builder setModel(RecipePortionsRequest.Model model) {
            this.model = model;
            return this;
        }

        public RecipePortionsRequest build() {
            return new RecipePortionsRequest(
                    recipeId,
                    cloneToRecipeId,
                    model
            );
        }
    }

    public static final class Model {
        private final int servings;
        private final int sittings;

        private Model(int servings, int sittings) {
            this.servings = servings;
            this.sittings = sittings;
        }

        public int getServings() {
            return servings;
        }

        public int getSittings() {
            return sittings;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Model model = (Model) o;
            return servings == model.servings &&
                    sittings == model.sittings;
        }

        @Override
        public int hashCode() {
            return Objects.hash(servings, sittings);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "servings=" + servings +
                    ", sittings=" + sittings +
                    '}';
        }

        public static class Builder {
            int servings;
            int sittings;

            public static Builder getDefault() {
                return new Builder().
                        setServings(1).
                        setSittings(1);
            }

            public static Builder basedOnResponseModel(RecipePortionsResponse.Model model) {
                return new Builder().
                        setServings(model.getServings()).
                        setSittings(model.getSittings());
            }

            public Builder setServings(int servings) {
                this.servings = servings;
                return this;
            }

            public Builder setSittings(int sittings) {
                this.sittings = sittings;
                return this;
            }

            public Model build() {
                return new Model(
                        servings,
                        sittings
                );
            }
        }
    }
}
