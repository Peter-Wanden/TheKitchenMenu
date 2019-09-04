package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeModelStatus;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator;

public class RecipeValidatorTestData {

    public static RecipeModelStatus getIdentityModelStatusUnChangedInValid() {
        return new RecipeModelStatus(
                RecipeValidator.ModelName.IDENTITY_MODEL,
                false,
                false);
    }

    public static RecipeModelStatus getIdentityModelStatusUnChangedValid() {
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
}
