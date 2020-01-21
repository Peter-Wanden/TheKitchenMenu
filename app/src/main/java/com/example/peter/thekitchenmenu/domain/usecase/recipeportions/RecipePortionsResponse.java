package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState.*;

public final class RecipePortionsResponse implements UseCaseCommand.Response {
    @Nonnull
    private final ComponentState state;
    @Nonnull
    private final List<RecipePortions.FailReason> failReasons;
    @Nonnull
    private final RecipePortionsModel model;

    private RecipePortionsResponse(@Nonnull ComponentState state,
                                   @Nonnull List<RecipePortions.FailReason> failReasons,
                                   @Nonnull RecipePortionsModel model) {
        this.state = state;
        this.failReasons = failReasons;
        this.model = model;
    }

    @Nonnull
    public ComponentState getState() {
        return state;
    }

    @Nonnull
    public List<RecipePortions.FailReason> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public RecipePortionsModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipePortionsResponse that = (RecipePortionsResponse) o;
        return state == that.state &&
                failReasons.equals(that.failReasons) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, model);
    }

    @Override
    public String toString() {
        return "RecipePortionsResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private ComponentState state;
        private List<RecipePortions.FailReason> failReasons;
        private RecipePortionsModel model;

        public static Builder getDefault() {
            return new Builder().
                    setState(ComponentState.INVALID_UNCHANGED).
                    setModel(new RecipePortionsModel.Builder().
                            getDefault().
                            build());
        }

        public Builder setState(ComponentState state) {
            this.state = state;
            return this;
        }

        public Builder setFailReasons(List<RecipePortions.FailReason> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setModel(RecipePortionsModel model) {
            this.model = model;
            return this;
        }

        public RecipePortionsResponse build() {
            return new RecipePortionsResponse(
                    state,
                    failReasons,
                    model
            );
        }
    }
}