package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeComponentStateModel;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;

public class TestDataRecipeValidator {

    public static RecipeComponentStateModel getIdentityModelStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                ComponentName.IDENTITY,
                ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.IDENTITY,
                ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.IDENTITY,
                ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.IDENTITY,
                ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getIdentityModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.IDENTITY,
                ComponentState.VALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                ComponentName.COURSES,
                ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.COURSES,
                ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.COURSES,
                ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.COURSES,
                ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getCoursesModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.COURSES,
                ComponentState.VALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getDurationStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                ComponentName.DURATION,
                ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.DURATION,
                ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.DURATION,
                ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.DURATION,
                ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getDurationModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.DURATION,
                ComponentState.VALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusDATA_UNAVAILABLE() {
        return new RecipeComponentStateModel(
                ComponentName.PORTIONS,
                ComponentState.DATA_UNAVAILABLE
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusINVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.PORTIONS,
                ComponentState.INVALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusVALID_UNCHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.PORTIONS,
                ComponentState.VALID_UNCHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusINVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.PORTIONS,
                ComponentState.INVALID_CHANGED
        );
    }

    public static RecipeComponentStateModel getPortionsModelStatusVALID_CHANGED() {
        return new RecipeComponentStateModel(
                ComponentName.PORTIONS,
                ComponentState.VALID_CHANGED
        );
    }
}
