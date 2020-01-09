package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration.DO_NOT_CLONE;

public final class RecipeDurationRequest implements UseCaseCommand.Request {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final String cloneToRecipeId;
    @Nonnull
    private final RecipeDurationModel model;

    public RecipeDurationRequest(@Nonnull String recipeId,
                   @Nonnull String cloneToRecipeId,
                   @Nonnull RecipeDurationModel model) {
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
    public RecipeDurationModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDurationRequest request = (RecipeDurationRequest) o;
        return recipeId.equals(request.recipeId) &&
                cloneToRecipeId.equals(request.cloneToRecipeId) &&
                model.equals(request.model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationRequest{" +
                "recipeId='" + recipeId + '\'' +
                ", cloneToRecipeId='" + cloneToRecipeId + '\'' +
                ", model=" + model +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, cloneToRecipeId, model);
    }

    public static class Builder {
        private String recipeId;
        private String cloneToRecipeId;
        private RecipeDurationModel model;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setModel(RecipeDurationModel.Builder.
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

        public Builder setModel(RecipeDurationModel model) {
            this.model = model;
            return this;
        }

        public RecipeDurationRequest build() {
            return new RecipeDurationRequest(
                    recipeId,
                    cloneToRecipeId,
                    model
            );
        }
    }
}