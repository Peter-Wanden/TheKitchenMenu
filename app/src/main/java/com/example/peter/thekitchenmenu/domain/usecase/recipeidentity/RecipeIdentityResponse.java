package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import javax.annotation.Nonnull;

public final class RecipeIdentityResponse implements UseCaseInteractor.Response {

    @Nonnull
    private final String recipeId;
    @Nonnull
    private final RecipeIdentityModel model;
    private final RecipeIdentity.Result result;

    private RecipeIdentityResponse(@Nonnull String recipeId,
                     @Nonnull RecipeIdentityModel model,
                     @Nonnull RecipeIdentity.Result result) {
        this.recipeId = recipeId;
        this.model = model;
        this.result = result;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public RecipeIdentityModel getModel() {
        return model;
    }

    public RecipeIdentity.Result getResult() {
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeIdentityResponse{" +
                "recipeId='" + recipeId + '\'' +
                ", response=" + result +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private String recipeId;
        private RecipeIdentityModel model;
        private RecipeIdentity.Result result;

        public Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setModel(new RecipeIdentityModel.Builder().
                            getDefault().
                            build());
        }

        public Builder setRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setModel(RecipeIdentityModel model) {
            this.model = model;
            return this;
        }

        public Builder setResult(RecipeIdentity.Result result) {
            this.result = result;
            return this;
        }

        public RecipeIdentityResponse build() {
            return new RecipeIdentityResponse(
                    recipeId,
                    model,
                    result
            );
        }
    }
}