package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipePortionsResponse implements UseCaseInteractor.Response {
    @Nonnull
    private final RecipePortions.Result result;
    @Nonnull
    private final RecipePortionsModel model;

    private RecipePortionsResponse(@Nonnull RecipePortions.Result result,
                                   @Nonnull RecipePortionsModel model) {
        this.result = result;
        this.model = model;
    }

    @Nonnull
    public RecipePortions.Result getResult() {
        return result;
    }

    @Nonnull
    public RecipePortionsModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePortionsResponse response = (RecipePortionsResponse) o;
        return result == response.result &&
                model.equals(response.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipePortionsResponse{" +
                "result=" + result +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private RecipePortions.Result result;
        private RecipePortionsModel model;

        public static Builder getDefault() {
            return new Builder().
                    setResult(RecipePortions.Result.INVALID_UNCHANGED).
                    setModel(new RecipePortionsModel.Builder().
                            getDefault().
                            build());
        }

        public Builder setResult(RecipePortions.Result result) {
            this.result = result;
            return this;
        }

        public Builder setModel(RecipePortionsModel model) {
            this.model = model;
            return this;
        }

        public RecipePortionsResponse build() {
            return new RecipePortionsResponse(
                    result,
                    model
            );
        }
    }
}