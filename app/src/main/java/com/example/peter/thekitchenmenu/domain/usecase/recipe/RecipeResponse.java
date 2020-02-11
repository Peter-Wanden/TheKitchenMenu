package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeResponse implements UseCaseCommand.Response {
    @Nonnull
    private final String recipeId;
    @Nonnull
    private final RecipeStateResponse recipeStateResponse;
    @Nonnull
    private final HashMap<ComponentName, Response> componentResponses;

    public RecipeResponse(@Nonnull String recipeId,
                          @Nonnull RecipeStateResponse recipeStateResponse,
                          @Nonnull HashMap<ComponentName, Response> componentResponses) {
        this.recipeId = recipeId;
        this.recipeStateResponse = recipeStateResponse;
        this.componentResponses = componentResponses;
    }

    @Nonnull
    public String getRecipeId() {
        return recipeId;
    }

    @Nonnull
    public RecipeStateResponse getRecipeStateResponse() {
        return recipeStateResponse;
    }

    @Nonnull
    public HashMap<ComponentName, Response> getComponentResponses() {
        return componentResponses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponse that = (RecipeResponse) o;
        return recipeId.equals(that.recipeId) &&
                recipeStateResponse.equals(that.recipeStateResponse) &&
                componentResponses.equals(that.componentResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, recipeStateResponse, componentResponses);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipeId='" + recipeId + '\'' +
                ", recipeStateResponse=" + recipeStateResponse +
                ", componentResponses=" + componentResponses +
                '}';
    }

    public static class Builder {
        private String recipeId;
        private RecipeStateResponse recipeStateResponse;
        private HashMap<ComponentName, Response> componentResponses;

        public static Builder getDefault() {
            return new Builder().
                    setRecipeId("").
                    setRecipeStateResponse(RecipeStateResponse.Builder.getDefault().build()).
                    setComponentResponses(getDefaultComponentResponses());
        }

        public Builder setRecipeId(String recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public Builder setRecipeStateResponse(RecipeStateResponse recipeStateResponse) {
            this.recipeStateResponse = recipeStateResponse;
            return this;
        }

        public Builder setComponentResponses(HashMap<ComponentName, Response> componentResponses) {
            this.componentResponses = componentResponses;
            return this;
        }

        public RecipeResponse build() {
            return new RecipeResponse(
                    recipeId,
                    recipeStateResponse,
                    componentResponses
            );
        }

        private static HashMap<ComponentName, Response> getDefaultComponentResponses() {
            return new LinkedHashMap<>();
        }
    }
}
