package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeComponentStatus;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator;

public class TestDataRecipeValidator {

    public static RecipeComponentStatus getIdentityModelStatusUnchangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                false,
                false);
    }

    public static RecipeComponentStatus getIdentityModelStatusUnchangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                false,
                true);
    }

    public static RecipeComponentStatus getIdentityModelStatusChangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                true,
                false);
    }

    public static RecipeComponentStatus getIdentityModelStatusChangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                true,
                true);
    }

    public static RecipeComponentStatus getCoursesModelStatusUnchangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                false,
                false);
    }

    public static RecipeComponentStatus getCoursesModelStatusUnchangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                false,
                true
        );
    }

    public static RecipeComponentStatus getCoursesModelStatusChangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                true,
                false
        );
    }

    public static RecipeComponentStatus getCoursesModelStatusChangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                true,
                true
        );
    }

    public static RecipeComponentStatus getDurationModelStatusUnchangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                false,
                false
        );
    }

    public static RecipeComponentStatus getDurationModelStatusUnchangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                false,
                true
        );
    }

    public static RecipeComponentStatus getDurationModelStatusChangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                true,
                false
        );
    }

    public static RecipeComponentStatus getDurationModelStatusChangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                true,
                true
        );
    }

    public static RecipeComponentStatus getPortionsModelStatusUnchangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                false,
                false
        );
    }

    public static RecipeComponentStatus getPortionsModelStatusUnchangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                false,
                true
        );
    }

    public static RecipeComponentStatus getPortionsModelStatusChangedInvalid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                true,
                false
        );
    }

    public static RecipeComponentStatus getPortionsModelStatusChangedValid() {
        return new RecipeComponentStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                true,
                true
        );
    }
}
