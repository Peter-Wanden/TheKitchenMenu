package com.example.peter.thekitchenmenu.domain.usecase.recipeduration;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeDurationResponse implements UseCaseCommand.Response {
    @Nonnull
    private final RecipeStateCalculator.ComponentState state;
    @Nonnull
    private final List<RecipeDuration.FailReason> failReasons;
    @Nonnull
    private final RecipeDurationModel model;

    public RecipeDurationResponse(@Nonnull RecipeStateCalculator.ComponentState state,
                                  @Nonnull List<RecipeDuration.FailReason> failReasons,
                                  @Nonnull RecipeDurationModel model) {
        this.state = state;
        this.failReasons = failReasons;
        this.model = model;
    }

    @Nonnull
    public RecipeStateCalculator.ComponentState getState() {
        return state;
    }

    @Nonnull
    public List<RecipeDuration.FailReason> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public RecipeDurationModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeDurationResponse response = (RecipeDurationResponse) o;
        return state == response.state &&
                failReasons.equals(response.failReasons) &&
                model.equals(response.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeDurationResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private RecipeStateCalculator.ComponentState state;
        private List<RecipeDuration.FailReason> failReasons;
        private RecipeDurationModel model;

        public static Builder getDefault() {
            return new Builder().
                    setState(RecipeStateCalculator.ComponentState.INVALID_UNCHANGED).
                    setFailReasons(getDefaultFailReasons()).
                    setModel(RecipeDurationModel.Builder.
                            getDefault().
                            build());
        }

        public Builder setState(RecipeStateCalculator.ComponentState state) {
            this.state = state;
            return this;
        }

        public Builder setFailReasons(List<RecipeDuration.FailReason> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setModel(RecipeDurationModel model) {
            this.model = model;
            return this;
        }

        public RecipeDurationResponse build() {
            return new RecipeDurationResponse(
                    state,
                    failReasons,
                    model
            );
        }

        private static List<RecipeDuration.FailReason> getDefaultFailReasons() {
            List<RecipeDuration.FailReason> defaultFailReasons = new LinkedList<>();
            defaultFailReasons.add(RecipeDuration.FailReason.NONE);
            return defaultFailReasons;
        }
    }
}