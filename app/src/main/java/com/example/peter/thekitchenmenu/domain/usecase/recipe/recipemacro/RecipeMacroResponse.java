package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeMacroResponse extends RecipeResponse {

    @Nonnull
    private final RecipeStateResponse recipeStateResponse;
    @Nonnull
    private final HashMap<ComponentName, Response> componentResponses;

    public RecipeMacroResponse(@Nonnull String id,
                               @Nonnull RecipeStateResponse recipeStateResponse,
                               @Nonnull HashMap<ComponentName, Response> componentResponses) {
        this.id = id;
        this.recipeStateResponse = recipeStateResponse;
        this.componentResponses = componentResponses;
    }

    @Nonnull
    public String getId() {
        return id;
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
        RecipeMacroResponse that = (RecipeMacroResponse) o;
        return id.equals(that.id) &&
                recipeStateResponse.equals(that.recipeStateResponse) &&
                componentResponses.equals(that.componentResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recipeStateResponse, componentResponses);
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeMacroResponse{" +
                "id='" + id + '\'' +
                ", recipeStateResponse=" + recipeStateResponse +
                ", componentResponses=" + componentResponses +
                '}';
    }

    public static class Builder {
        private String id;
        private RecipeStateResponse recipeStateResponse;
        private HashMap<ComponentName, Response> componentResponses;

        public static Builder getDefault() {
            return new Builder().
                    setId("").
                    setRecipeStateResponse(RecipeStateResponse.Builder.getDefault().build()).
                    setComponentResponses(getDefaultComponentResponses());
        }

        public Builder setId(String id) {
            this.id = id;
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

        public RecipeMacroResponse build() {
            return new RecipeMacroResponse(
                    id,
                    recipeStateResponse,
                    componentResponses
            );
        }

        private static HashMap<ComponentName, Response> getDefaultComponentResponses() {
            return new LinkedHashMap<>();
        }
    }
}
