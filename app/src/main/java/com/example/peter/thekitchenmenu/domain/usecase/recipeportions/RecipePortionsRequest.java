package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsRequest implements UseCaseCommand.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nonnull
    private final RecipePortionsModel model;

    private RecipePortionsRequest(@Nonnull String recipeId,
                    @Nonnull String cloneToRecipeId,
                    @Nonnull RecipePortionsModel model) {
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
    public RecipePortionsModel getModel() {
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
        private RecipePortionsModel model;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setCloneToRecipeId("").
                    setModel(new RecipePortionsModel.Builder().
                            getDefault().
                            build());
        }

        public static Builder basedOnRequest(@Nonnull RecipePortionsRequest request) {
            return new Builder().
                    setRecipeId(request.getRecipeId()).
                    setCloneToRecipeId(request.getCloneToRecipeId()).
                    setModel(request.getModel());
        }

        public Builder setRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setCloneToRecipeId(String cloneToRecipeId) {
            this.cloneToRecipeId = cloneToRecipeId;
            return this;
        }

        public Builder setModel(RecipePortionsModel model) {
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
}
