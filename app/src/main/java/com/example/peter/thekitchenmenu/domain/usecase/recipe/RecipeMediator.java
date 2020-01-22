package com.example.peter.thekitchenmenu.domain.usecase.recipe;

public interface RecipeMediator {

    void componentStatusChanged(RecipeMediatorColleague colleague);

    void createComponents();

    void startColleaguesAndNotify(String recipeId);
}
