package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.ui.common.ScreensNavigator;
import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsEventBus;
import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsManager;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.AddEditRecipeNavigator;

import java.util.ArrayList;
import java.util.List;

public class RecipeEditorController
        implements
        AddEditRecipeNavigator,
        UseCaseBase.Callback<RecipeResponse> {

    public interface RecipeEditorControllerListener {
        void refreshOptionsMenu();
    }

    private final ScreensNavigator screensNavigator;
    private final DialogsManager dialogsManager;
    private final DialogsEventBus dialogsEventBus;
    private List<RecipeEditorControllerListener> controllerListeners = new ArrayList<>();

    private RecipeEditorView view;

    private final UseCaseHandler handler;
    private String recipeDomainId;
    private boolean isNewRecipe;

    private final Recipe recipe;
    private RecipeResponse recipeResponse;
    private RecipeMetadataResponse metadataResponse;

    public RecipeEditorController(ScreensNavigator screensNavigator,
                                  DialogsManager dialogsManager,
                                  DialogsEventBus dialogsEventBus,
                                  UseCaseHandler handler,
                                  Recipe recipe) {
        this.screensNavigator = screensNavigator;
        this.dialogsManager = dialogsManager;
        this.dialogsEventBus = dialogsEventBus;
        this.handler = handler;

        this.recipe = recipe;
        recipeResponse = new RecipeResponse.Builder().getDefault().build();
        metadataResponse = new RecipeMetadataResponse.Builder().getDefault().build();
    }

    public void onStart() {
        view.registerListener(this);
        // TODO -
        //  if there is no recipe id create a new recipe
        //  if there is a recipe id, load the recipe:
        //  - if the recipe creator is not the user, copy the recipe
        //  - if the recipe creator is the user, edit the recipe
        //  - if the recipe is being used by others, make a copy and allow uses to update their
        //     copy if they want to
        //  See {@link RecipeEditorViewModel}
    }

    public void onStop() {
        view.unregisterListener(this);
    }

    public void bindView(RecipeEditorView view) {
        this.view = view;
    }

    public void setRecipeDomainId(String recipeDomainId) {
        this.recipeDomainId = recipeDomainId;

        if (Recipe.CREATE_NEW_RECIPE.equals(recipeDomainId)) {
            isNewRecipe = true;
            view.setTitle(R.string.activity_title_add_new_recipe);
            setIngredientsButton();
        }
    }

    private void setIngredientsButton() {
        if (isNewRecipe) {
            view.setIngredientsButtonText(R.string.add_ingredients);
        } else {
            view.setIngredientsButtonText(R.string.edit_ingredients);
        }
    }

    public void start() {
        RecipeRequest request = new RecipeRequest.Builder().
                getDefault().
                setDomainId(recipeDomainId).
                build();
        handler.executeAsync(recipe, request, this);
    }

    @Override
    public void onUseCaseSuccess(RecipeResponse response) {
        isNewRecipe = false;

        recipeResponse = response;
        metadataResponse = (RecipeMetadataResponse) response.getModel().getComponentResponses().
                get(RecipeMetadata.ComponentName.RECIPE_METADATA);

        setActivityTitle(R.string.activity_title_edit_recipe);
        setIngredientsButton();
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        RecipeMetadata.ComponentState recipeState = metadataResponse.getMetadata().getState();
        if (RecipeMetadata.ComponentState.VALID_CHANGED == recipeState ||
                RecipeMetadata.ComponentState.VALID_UNCHANGED == recipeState) {
            view.showIngredientButton();
            view.showReviewButton();
            view.refreshOptionsMenu();

        } else {
            hideButtons();
        }
    }

    @Override
    public void onUseCaseError(RecipeResponse response) {
        this.recipeResponse = response;
        if (isNewRecipe) {
            setupForNewRecipe();
        }
    }

    private void setupForNewRecipe() {
        setActivityTitle(R.string.activity_title_add_new_recipe);
        setIngredientsButton();
    }

    public void componentChanged(String domainId) {
        // todo, refresh data
    }

    @Override
    public void reviewNewRecipe(String recipeId) {

    }

    @Override
    public void reviewEditedRecipe(String recipeId) {

    }

    @Override
    public void reviewClonedRecipe(String recipeId) {

    }

    @Override
    public void addIngredients(String recipeId) {

    }

    @Override
    public void editIngredients(String recipeId) {

    }

    @Override
    public void reviewIngredients(String recipeId) {

    }

    @Override
    public void cancelEditing() {

    }

    @Override
    public void refreshOptionsMenu() {
        controllerListeners.forEach(RecipeEditorControllerListener::refreshOptionsMenu);
    }

    @Override
    public void setActivityTitle(int activityTitleResourceId) {
        view.setTitle(activityTitleResourceId);
    }

    @Override
    public void showUnsavedChangedDialog() {

    }

    public void registerControllerListener(RecipeEditorControllerListener listener) {
        controllerListeners.add(listener);
    }

    public void unregisterControllerListener(RecipeEditorControllerListener listener) {
        controllerListeners.remove(listener);
    }
}
