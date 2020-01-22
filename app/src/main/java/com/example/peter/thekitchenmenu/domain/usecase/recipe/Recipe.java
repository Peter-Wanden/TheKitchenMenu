package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState.*;

public class Recipe implements RecipeMediator {

    private static final String TAG = "tkm-" + Recipe.class.getSimpleName() + ": ";

    public interface Listener {
        void recipeStateChanged(RecipeStateModel stateModel);
    }

    private final RecipeIdentity identity;
    private String recipeId;
    private RecipeStateModel model;

    private final List<Listener> recipeClientListeners = new ArrayList<>();

    private final HashMap<ComponentName, ComponentState> componentStates = new LinkedHashMap<>();

    public Recipe(RecipeIdentity identity) {
        this.identity = identity;
        createComponents();
    }

    @Override
    public void createComponents() {
        identity.setMediator(this);
    }

    @Override
    public void startColleaguesAndNotify(String recipeId) {
        this.recipeId = recipeId;
        identity.startColleague(recipeId);
    }

    @Override
    public void componentStatusChanged(RecipeMediatorColleague colleague) {
        if (isRecipeIdChanged(colleague.getRecipeId())) {
            startColleaguesAndNotify(colleague.getRecipeId());
            return;
        }
        if (colleague == identity) {
            componentStates.put(ComponentName.IDENTITY, identity.getState());
            System.out.println(TAG + "componentStates=" + componentStates);
        }
        updateRecipeState();
    }

    private boolean isRecipeIdChanged(String recipeId) {
        return !this.recipeId.equals(recipeId);
    }

    private void updateRecipeState() {
        model = new RecipeStateModel(
                State.VALID_UNCHANGED,
                componentStates
        );
        notifyClientListeners();
    }

    private void notifyClientListeners() {
        for (Listener listener : recipeClientListeners) {
            listener.recipeStateChanged(model);
        }
    }

    public void registerClientListener(Listener listener) {
        recipeClientListeners.add(listener);
    }

    public void unRegisterClientListener(Listener listener) {
        recipeClientListeners.remove(listener);
    }

    public RecipeIdentity getIdentity() {
        return identity;
    }
}
