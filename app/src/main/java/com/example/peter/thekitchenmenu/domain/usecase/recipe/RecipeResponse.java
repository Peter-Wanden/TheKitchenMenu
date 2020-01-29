package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeResponse implements UseCaseCommand.Response {
    @Nonnull
    private final RecipeStateCalculator.RecipeState recipeState;
    @Nonnull
    private final List<FailReasons> failReasons;
    @Nonnull
    private final RecipeIdentityResponse identityResponse;
    @Nonnull
    private final RecipeCourseResponse courseResponse;
    @Nonnull
    private final RecipeDurationResponse durationResponse;

    public RecipeResponse(@Nonnull RecipeStateCalculator.RecipeState recipeState,
                          @Nonnull List<FailReasons> failReasons,
                          @Nonnull RecipeIdentityResponse identityResponse,
                          @Nonnull RecipeCourseResponse courseResponse,
                          @Nonnull RecipeDurationResponse durationResponse) {
        this.recipeState = recipeState;
        this.failReasons = failReasons;
        this.identityResponse = identityResponse;
        this.courseResponse = courseResponse;
        this.durationResponse = durationResponse;
    }

    @Nonnull
    public RecipeStateCalculator.RecipeState getRecipeState() {
        return recipeState;
    }

    @Nonnull
    public List<FailReasons> getFailReasons() {
        return failReasons;
    }

    @Nonnull
    public RecipeIdentityResponse getIdentityResponse() {
        return identityResponse;
    }

    @Nonnull
    public RecipeCourseResponse getCourseResponse() {
        return courseResponse;
    }

    @Nonnull
    public RecipeDurationResponse getDurationResponse() {
        return durationResponse;
    }

    @Nonnull
    @Override
    public String toString() {
        return "RecipeResponse{" +
                "recipeState=" + recipeState +
                ", failReasons=" + failReasons +
                ", identityResponse=" + identityResponse +
                ", courseResponse=" + courseResponse +
                ", durationResponse=" + durationResponse +
                '}';
    }
}
