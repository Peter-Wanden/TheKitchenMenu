package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public final class RecipeIdentityResponse implements UseCaseCommand.Response {

    @Nonnull
    private final RecipeState.ComponentState state;
    @Nonnull
    private final List<RecipeIdentity.FailReason> failReasons;
    @Nonnull
    private final RecipeIdentityModel model;

    private RecipeIdentityResponse(@Nonnull RecipeState.ComponentState state,
                                   @Nonnull List<RecipeIdentity.FailReason> failReasons,
                                   @Nonnull RecipeIdentityModel model) {
        this.state = state;
        this.failReasons = failReasons;
        this.model = model;
    }

    @Nonnull
    public RecipeIdentityModel getModel() {
        return model;
    }

    @Nonnull
    public List<RecipeIdentity.FailReason> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public RecipeState.ComponentState getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIdentityResponse that = (RecipeIdentityResponse) o;
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
        return "RecipeIdentityResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", model=" + model +
                '}';
    }

    public static class Builder {
        private RecipeIdentityModel model;
        private List<RecipeIdentity.FailReason> failReasons;
        private RecipeState.ComponentState state;

        public Builder getDefault() {
            return new Builder().
                    setModel(new RecipeIdentityModel.Builder().
                            getDefault().
                            build());
        }

        public Builder setState(RecipeState.ComponentState state) {
            this.state = state;
            return this;
        }

        public Builder setFailReasons(List<RecipeIdentity.FailReason> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setModel(RecipeIdentityModel model) {
            this.model = model;
            return this;
        }

        public RecipeIdentityResponse build() {
            return new RecipeIdentityResponse(
                    state,
                    failReasons,
                    model
            );
        }
    }
}