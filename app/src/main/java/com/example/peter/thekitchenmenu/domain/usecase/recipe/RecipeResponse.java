package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeResponse implements UseCaseCommand.Response {
    @Nonnull
    private final RecipeStateCalculator.RecipeState recipeState;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final RecipeIdentityResponse identityResponse;

    public RecipeResponse(@Nonnull RecipeStateCalculator.RecipeState recipeState,
                          @Nonnull List<FailReasons> failReasons,
                          @Nonnull RecipeIdentityResponse identityResponse) {
        this.recipeState = recipeState;
        this.failReasons = failReasons;
        this.identityResponse = identityResponse;
    }

    @Nonnull
    public RecipeStateCalculator.RecipeState getRecipeState() {
        return recipeState;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    public RecipeIdentityResponse getIdentityResponse() {
        return identityResponse;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipeState=" + recipeState +
                ", failReasons=" + failReasons +
                ", identityResponse=" + identityResponse +
                '}';
    }

}
