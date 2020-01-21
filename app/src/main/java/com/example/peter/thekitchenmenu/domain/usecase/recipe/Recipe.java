package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState.*;

public class Recipe implements RecipeMediator {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    private RecipeIdentity identity;

    private String recipeId;
    private RecipeState recipeState;

    private HashMap<ComponentName, ComponentState> componentStates = new LinkedHashMap<>();

    public Recipe(RecipeIdentity identity) {
        this.identity = identity;
        createComponents();
    }

    @Override
    public void createComponents() {
        identity.setMediator(this);
        System.out.println(TAG + "createComponents called, factory returned=" + identity);
    }

    @Override
    public void componentStatusChanged(RecipeMediatorColleague colleague) {
        if (colleague == identity) {
            componentStates.put(ComponentName.IDENTITY, identity.getState());
            System.out.println(componentStates);
        }
        updateRecipeState();
    }

    @Override
    public void startColleagues(String recipeId) {
        this.recipeId = recipeId;
        identity.startColleague(recipeId);
    }

    private void updateRecipeState() {

        notifyListeners();
    }

    private void notifyListeners() {

    }
}
