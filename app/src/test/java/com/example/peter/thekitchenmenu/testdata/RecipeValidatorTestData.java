package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeModelStatus;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator;

public class RecipeValidatorTestData {

    public static RecipeModelStatus getIdentityModelStatusUnchangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                false,
                false);
    }

    public static RecipeModelStatus getIdentityModelStatusUnchangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                false,
                true);
    }

    public static RecipeModelStatus getIdentityModelStatusChangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                true,
                false);
    }

    public static RecipeModelStatus getIdentityModelStatusChangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                true,
                true);
    }

    public static RecipeModelStatus getCoursesModelStatusUnchangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                false,
                false);
    }

    public static RecipeModelStatus getCoursesModelStatusUnchangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                false,
                true
        );
    }

    public static RecipeModelStatus getCoursesModelStatusChangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                true,
                false
        );
    }

    public static RecipeModelStatus getCoursesModelStatusChangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.COURSES_MODEL,
                true,
                true
        );
    }

    public static RecipeModelStatus getDurationModelStatusUnchangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                false,
                false
        );
    }

    public static RecipeModelStatus getDurationModelStatusUnchangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                false,
                true
        );
    }

    public static RecipeModelStatus getDurationModelStatusChangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                true,
                false
        );
    }

    public static RecipeModelStatus getDurationModelStatusChangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.DURATION_MODEL,
                true,
                true
        );
    }

    public static RecipeModelStatus getPortionsModelStatusUnchangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                false,
                false
        );
    }

    public static RecipeModelStatus getPortionsModelStatusUnchangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                false,
                true
        );
    }

    public static RecipeModelStatus getPortionsModelStatusChangedInvalid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                true,
                false
        );
    }

    public static RecipeModelStatus getPortionsModelStatusChangedValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.PORTIONS_MODEL,
                true,
                true
        );
    }
}
