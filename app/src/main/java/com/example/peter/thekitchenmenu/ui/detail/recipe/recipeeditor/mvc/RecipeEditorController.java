package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.ui.common.ScreensNavigator;
import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsEventBus;
import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsManager;

public class RecipeEditorController
        implements
        UseCaseBase.Callback<RecipeResponse>,
        RecipeEditorView.ListenerViewInput {

    private final ScreensNavigator screensNavigator;
    private final DialogsManager dialogsManager;
    private final DialogsEventBus dialogsEventBus;

    private RecipeEditorView view;

    private final UseCaseHandler handler;
    private String recipeDomainId;
    private boolean isNewRecipe;

    private final Recipe recipe;
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
            view.isNewRecipe(isNewRecipe = true);
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
        metadataResponse = (RecipeMetadataResponse) response.getDomainModel().getComponentResponses().
                get(ComponentName.RECIPE_METADATA);
        ComponentState recipeComponentState = metadataResponse.getMetadata().getComponentState();

        view.isNewRecipe(isNewRecipe = false);
        view.setRecipeState(recipeComponentState);
    }

    @Override
    public void onUseCaseError(RecipeResponse response) {
        metadataResponse = (RecipeMetadataResponse) response.getDomainModel().getComponentResponses().
                get(ComponentName.RECIPE_METADATA);
        ComponentState recipeComponentState = metadataResponse.getMetadata().getComponentState();

        view.isNewRecipe(isNewRecipe);
        view.setRecipeState(recipeComponentState);
    }

    public void componentChanged(String domainId) {
        RecipeRequest request = new RecipeRequest.Builder().getDefault().build();
        handler.executeAsync(recipe, request, this);
    }

    @Override
    public void onReviewButtonClicked() {

    }

    @Override
    public void onNavigateUp() {

    }
}
