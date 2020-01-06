package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeComponentStatusModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator;

public class TestDataRecipeValidator {

    public static RecipeComponentStatusModel getIdentityModelStatusUnchangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.IDENTITY,
                false,
                false);
    }

    public static RecipeComponentStatusModel getIdentityModelStatusUnchangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.IDENTITY,
                false,
                true);
    }

    public static RecipeComponentStatusModel getIdentityModelStatusChangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.IDENTITY,
                true,
                false);
    }

    public static RecipeComponentStatusModel getIdentityModelStatusChangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.IDENTITY,
                true,
                true);
    }

    public static RecipeComponentStatusModel getCoursesModelStatusUnchangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.COURSES,
                false,
                false);
    }

    public static RecipeComponentStatusModel getCoursesModelStatusUnchangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.COURSES,
                false,
                true
        );
    }

    public static RecipeComponentStatusModel getCoursesModelStatusChangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.COURSES,
                true,
                false
        );
    }

    public static RecipeComponentStatusModel getCoursesModelStatusChangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.COURSES,
                true,
                true
        );
    }

    public static RecipeComponentStatusModel getDurationModelStatusUnchangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.DURATION,
                false,
                false
        );
    }

    public static RecipeComponentStatusModel getDurationModelStatusUnchangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.DURATION,
                false,
                true
        );
    }

    public static RecipeComponentStatusModel getDurationModelStatusChangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.DURATION,
                true,
                false
        );
    }

    public static RecipeComponentStatusModel getDurationModelStatusChangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.DURATION,
                true,
                true
        );
    }

    public static RecipeComponentStatusModel getPortionsModelStatusUnchangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.PORTIONS,
                false,
                false
        );
    }

    public static RecipeComponentStatusModel getPortionsModelStatusUnchangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.PORTIONS,
                false,
                true
        );
    }

    public static RecipeComponentStatusModel getPortionsModelStatusChangedInvalid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.PORTIONS,
                true,
                false
        );
    }

    public static RecipeComponentStatusModel getPortionsModelStatusChangedValid() {
        return new RecipeComponentStatusModel(
                RecipeValidator.ComponentName.PORTIONS,
                true,
                true
        );
    }
}
