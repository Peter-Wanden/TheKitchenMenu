package com.example.peter.thekitchenmenu.commonmocks;

import android.app.Application;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeMediator;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;


public class UseCaseFactoryMock {

    private RecipeMediator recipeMediator;
    private TextValidator textValidator;
    private RecipeIdentity recipeIdentity;


    public void setRecipeMediator(RecipeMediator recipeMediator) {
        this.recipeMediator = recipeMediator;
    }

    public void setTextValidator(TextValidator textValidator) {
        this.textValidator = textValidator;
    }

    public void setRecipeIdentity(RecipeIdentity recipeIdentity) {
        this.recipeIdentity = recipeIdentity;
    }

    public RecipeIdentity provideRecipeIdentity(RecipeMediator mediator) {
        return recipeIdentity;
    }

    public TextValidator provideTextValidator() {
        return textValidator;
    }

    private class ApplicationMock extends Application {

    }
}
