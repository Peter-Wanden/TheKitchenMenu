package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.core.util.Pair;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro.CREATE_NEW_RECIPE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;


public class RecipeEditorViewModel extends ViewModel {

    private static final String TAG = "tkm-" + RecipeEditorViewModel.class.getSimpleName() + ":";

    private AddEditRecipeNavigator navigator;

    @Nonnull
    private Resources resources;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private RecipeMacro recipeMacro;
    @Nonnull
    private UniqueIdProvider idProvider;

    public final ObservableBoolean showIngredientsButtonObservable = new ObservableBoolean();
    public final ObservableField<String> ingredientsButtonTextObservable = new ObservableField<>();
    public final ObservableBoolean dataIsLoadingObservable = new ObservableBoolean();

    private boolean isNewRecipe;
    private boolean isCloned;
    private boolean showReviewButton;

    private RecipeResponse recipeResponse;
    private RecipeResponseListener recipeResponseListener;
    private RecipeStateResponse recipeStateResponse;

    public RecipeEditorViewModel(@Nonnull UseCaseHandler handler,
                                 @Nonnull RecipeMacro recipeMacro,
                                 @Nonnull UniqueIdProvider idProvider,
                                 @Nonnull Resources resources) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;
        this.idProvider = idProvider;
        this.resources = resources;

        recipeResponse = RecipeResponse.Builder.getDefault().build();
        recipeResponseListener = new RecipeResponseListener();
        recipeMacro.registerComponentCallback(new Pair<>(ComponentName.RECIPE, recipeResponseListener));

        recipeStateResponse = RecipeStateResponse.Builder.getDefault().build();
        recipeMacro.registerStateListener(new RecipeStateListener());
    }

    void setNavigator(AddEditRecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String recipeId) {
        RecipeRequest.Builder request = new RecipeRequest.Builder();

        if (recipeId.equals(CREATE_NEW_RECIPE)) {
            isNewRecipe = true;
            request.setId(idProvider.getUId());
        } else {
            isNewRecipe = false;
            request.setId(recipeId);
        }
        request.setCloneToId("");
        handler.execute(recipeMacro, request.build(), recipeResponseListener);
    }

    /**
     * Registered recipe component callback listening for updates pushed from
     * {@link RecipeMacro}
     */
    private class RecipeResponseListener implements UseCase.Callback<RecipeResponse> {
        private final String TAG = "tkm-" + RecipeResponseListener.class.
                getSimpleName() + ": ";
        @Override
        public void onSuccess(RecipeResponse recipeResponse) {
            if (isRecipeResponseChanged(recipeResponse)) {
                System.out.println(RecipeEditorViewModel.TAG + TAG + "onSuccess:" + recipeResponse);
                RecipeEditorViewModel.this.recipeResponse = recipeResponse;
                onUseCaseSuccess();
            }
        }

        @Override
        public void onError(RecipeResponse recipeResponse) {
            if (isRecipeResponseChanged(recipeResponse)) {
                System.out.println(RecipeEditorViewModel.TAG + TAG + "onError:" + recipeResponse);
                RecipeEditorViewModel.this.recipeResponse = recipeResponse;
                onUseCaseError();
            }
        }
    }

    private class RecipeStateListener implements RecipeMacro.RecipeStateListener {
        private final String TAG = "tkm-" + RecipeStateListener.class.getSimpleName() + ": ";

        @Override
        public void recipeStateChanged(RecipeStateResponse recipeStateResponse) {
            if (isRecipeStateChanged(recipeStateResponse)) {
                System.out.println(RecipeEditorViewModel.TAG + TAG + recipeStateResponse);
                RecipeEditorViewModel.this.recipeStateResponse = recipeStateResponse;
                updateButtonVisibility();
            }
        }
    }

    private boolean isRecipeStateChanged(RecipeStateResponse recipeStateResponse) {
        return !this.recipeStateResponse.equals(recipeStateResponse);
    }

    private boolean isRecipeResponseChanged(RecipeResponse recipeResponse) {
        return !this.recipeResponse.equals(recipeResponse);
    }

    private void onUseCaseSuccess() {
        if (isEditorCreator()) {
            if (isCloned) {
                isCloned = false;
                setupForClonedRecipe();
            } else {
                setupForExistingRecipe();
            }
        } else {
            cloneRecipe();
        }
    }

    private void onUseCaseError() {
        if (isNewRecipe) {
            setupForNewRecipe();
        }
    }

    private boolean isEditorCreator() {
        return Constants.getUserId().equals(recipeResponse.getCreatedBy());
    }

    private void cloneRecipe() {
        RecipeRequest request = new RecipeRequest.Builder().
                setId(recipeResponse.getId()).
                setCloneToId(idProvider.getUId()).build();
        handler.execute(recipeMacro, request, recipeResponseListener);
    }

    private void setupForNewRecipe() {
        navigator.setActivityTitle(R.string.activity_title_add_new_recipe);
        setIngredientsButton();
    }

    private void setupForExistingRecipe() {
        isNewRecipe = false;
        navigator.setActivityTitle(R.string.activity_title_edit_recipe);
        setIngredientsButton();
    }

    private void setupForClonedRecipe() {
        isNewRecipe = false;
        navigator.setActivityTitle(R.string.activity_title_copy_recipe);
        setIngredientsButton();
    }

    private void setIngredientsButton() {
        if (isNewRecipe) {
            // Todo - isNewRecipe, change to ifNoIngredients when ingredient component added
            ingredientsButtonTextObservable.set(resources.getString(R.string.add_ingredients));

        } else {
            ingredientsButtonTextObservable.set(resources.getString(R.string.edit_ingredients));
        }
    }

    private void updateButtonVisibility() {

        if (recipeStateResponse.getState() == RecipeState.VALID_CHANGED) {
            showIngredientsButtonObservable.set(true);
            showReviewButton = true;
            navigator.refreshOptionsMenu();

        } else if (recipeStateResponse.getState() == RecipeState.VALID_UNCHANGED) {
            showIngredientsButtonObservable.set(true);
            showReviewButton = false;
            navigator.refreshOptionsMenu();

        } else {
            hideButtons();
        }
    }

    private void hideButtons() {
        showIngredientsButtonObservable.set(false);
        showReviewButton = false;
        navigator.refreshOptionsMenu();
    }

    boolean isShowReviewButton() {
        return showReviewButton;
    }

    void upOrBackPressed() {
        if (recipeStateResponse.getState() == RecipeState.INVALID_CHANGED) {
            navigator.showUnsavedChangedDialog();
        } else {
            navigator.cancelEditing();
        }
    }

    void reviewButtonPressed() {
        String recipeId = recipeResponse.getId();
        if (isNewRecipe) {
            navigator.reviewNewRecipe(recipeResponse.getId());

        } else if (isEditorCreator()) {
            navigator.reviewEditedRecipe(recipeId);

        } else if (isCloned) {
            navigator.reviewClonedRecipe(recipeId);
        }
    }

    public void ingredientsButtonPressed() {
        String recipeId = recipeResponse.getId();

        if (isNewRecipe) {
            navigator.addIngredients(recipeId);

        } else if (isEditorCreator()) {
            navigator.editIngredients(recipeId);

        } else if (isCloned) {
            navigator.reviewIngredients(recipeId);
        }
    }
}