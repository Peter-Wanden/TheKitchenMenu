package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeComponentStateModel;

public class TestDataRecipeValidator {

    public static RecipeComponentStateModel getIdentityModelStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.IDENTITY,
                RecipeStateCalculator.ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.IDENTITY,
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.IDENTITY,
                RecipeStateCalculator.ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.IDENTITY,
                RecipeStateCalculator.ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.IDENTITY,
                RecipeStateCalculator.ComponentState.VALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.COURSE,
                RecipeStateCalculator.ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.COURSE,
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.COURSE,
                RecipeStateCalculator.ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.COURSE,
                RecipeStateCalculator.ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.COURSE,
                RecipeStateCalculator.ComponentState.VALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getDurationStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.DURATION,
                RecipeStateCalculator.ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.DURATION,
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.DURATION,
                RecipeStateCalculator.ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.DURATION,
                RecipeStateCalculator.ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.DURATION,
                RecipeStateCalculator.ComponentState.VALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.PORTIONS,
                RecipeStateCalculator.ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.PORTIONS,
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.PORTIONS,
                RecipeStateCalculator.ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.PORTIONS,
                RecipeStateCalculator.ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                RecipeStateCalculator.ComponentName.PORTIONS,
                RecipeStateCalculator.ComponentState.VALID_CHANGED
        );
    }
}
