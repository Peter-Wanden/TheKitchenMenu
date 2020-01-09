package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecipeIdentityRequest implements UseCaseCommand.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nullable
    private final RecipeIdentityModel model;

    private RecipeIdentityRequest(@Nonnull String recipeId,
                    @Nonnull String cloneToRecipeId,
                    @Nullable RecipeIdentityModel model) {
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

    @Nullable
    public RecipeIdentityModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityRequest request = (RecipeIdentityRequest) o;
        return recipeId.equals(request.recipeId) &&
                cloneToRecipeId.equals(request.cloneToRecipeId) &&
                Objects.equals(model, request.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, cloneToRecipeId, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityRequest{" +
                "recipeId='" + recipeId + '\'' +
                ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String recipeId;
        private String cloneToRecipeId;
        private RecipeIdentityModel model;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setCloneToRecipeId("").
                    setModel(new RecipeIdentityModel.Builder().
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

        public Builder setModel(RecipeIdentityModel model) {
            this.model = model;
            return this;
        }

        public RecipeIdentityRequest build() {
            return new RecipeIdentityRequest(
                    recipeId,
                    cloneToRecipeId,
                    model
            );
        }
    }
}