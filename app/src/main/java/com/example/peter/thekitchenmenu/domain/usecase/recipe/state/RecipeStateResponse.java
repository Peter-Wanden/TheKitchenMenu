package com.example.peter.thekitchenmenu.domain.usecase.recipe.state;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

public class RecipeStateResponse implements UseCase.Response {
    @Nonnull
    private final RecipeMetadata.RecipeState state;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final HashMap<RecipeMetadata.ComponentName, RecipeMetadata.ComponentState> componentStates;

    public RecipeStateResponse(@Nonnull RecipeMetadata.RecipeState state,
                               @Nonnull List<FailReasons> failReasons,
                               @Nonnull HashMap<RecipeMetadata.ComponentName, RecipeMetadata.ComponentState> componentStates) {
        this.state = state;
        this.failReasons = failReasons;
        this.componentStates = componentStates;
    }

    @Nonnull
    public RecipeMetadata.RecipeState getState() {
        return state;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public HashMap<RecipeMetadata.ComponentName, RecipeMetadata.ComponentState> getComponentStates() {
        return componentStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeStateResponse that = (RecipeStateResponse) o;
        return state == that.state &&
                failReasons.equals(that.failReasons) &&
                componentStates.equals(that.componentStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, failReasons, componentStates);
    }

    @Override
    public String toString() {
        return "RecipeStateResponse{" +
                "state=" + state +
                ", failReasons=" + failReasons +
                ", componentStates=" + componentStates +
                '}';
    }

    public static class Builder {
        private RecipeMetadata.RecipeState recipeState;
        private List<FailReasons> failReasons;
        private HashMap<RecipeMetadata.ComponentName, RecipeMetadata.ComponentState> componentStates;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeState(RecipeMetadata.RecipeState.INVALID_UNCHANGED).
                    setFailReasons(new ArrayList<>()).
                    setComponentStates(new HashMap<>());
        }

        public Builder setRecipeState(RecipeMetadata.RecipeState recipeState) {
            this.recipeState = recipeState;
            return this;
        }

        public Builder setFailReasons(List<FailReasons> failReasons) {
            this.failReasons = failReasons;
            return this;
        }

        public Builder setComponentStates(HashMap<RecipeMetadata.ComponentName, RecipeMetadata.ComponentState> componentStates) {
            this.componentStates = componentStates;
            return this;
        }

        public RecipeStateResponse build() {
            return new RecipeStateResponse(
                    recipeState,
                    failReasons,
                    componentStates
            );
        }
    }
}
