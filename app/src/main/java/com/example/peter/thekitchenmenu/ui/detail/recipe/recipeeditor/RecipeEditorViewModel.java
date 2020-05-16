package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe.CREATE_NEW_RECIPE;


public class RecipeEditorViewModel extends ViewModel {

    private static final String TAG = "tkm-" + RecipeEditorViewModel.class.getSimpleName() + ":";

    private AddEditRecipeNavigator navigator;

    @Nonnull
    private Resources resources;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private Recipe recipeMacro;
    @Nonnull
    private UniqueIdProvider idProvider;

    public final ObservableBoolean showIngredientsButtonObservable = new ObservableBoolean();
    public final ObservableField<String> ingredientsButtonTextObservable = new ObservableField<>();
    public final ObservableBoolean dataIsLoadingObservable = new ObservableBoolean();

    private boolean isNewRecipe;
    private boolean showReviewButton;

    private RecipeResponseListener recipeResponseListener;
    private RecipeResponse recipeResponse;
    private RecipeMetadataResponse metadataResponse;

    public RecipeEditorViewModel(@Nonnull UseCaseHandler handler,
                                 @Nonnull Recipe recipeMacro,
                                 @Nonnull UniqueIdProvider idProvider,
                                 @Nonnull Resources resources) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;
        this.idProvider = idProvider;
        this.resources = resources;

        metadataResponse = new RecipeMetadataResponse.Builder().getDefault().build();
        recipeResponseListener = new RecipeResponseListener();

        recipeMacro.registerRecipeCallback(recipeResponseListener);
    }

    void setNavigator(AddEditRecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String recipeId) {
        RecipeMetadataRequest.Builder request = new RecipeMetadataRequest.Builder();

        if (recipeId.equals(CREATE_NEW_RECIPE)) {
            isNewRecipe = true;
            request.setDataId(idProvider.getUId());
        } else {
            isNewRecipe = false;
            request.setDataId(recipeId);
        }
        handler.execute(recipeMacro, request.build(), recipeResponseListener);
    }

    /**
     * Registered recipe component callback listening for updates pushed from
     * {@link Recipe}
     */
    private class RecipeResponseListener implements UseCase.Callback<RecipeResponse> {
        private final String TAG = "tkm-" + RecipeResponseListener.class.
                getSimpleName() + ": ";
        @Override
        public void onSuccess(RecipeResponse response) {
            if (isRecipeResponseChanged(response)) {
                System.out.println(RecipeEditorViewModel.TAG + TAG + "onSuccess:" + response);
                recipeResponse = response;
                onUseCaseSuccess();
            }
        }

        @Override
        public void onError(RecipeResponse response) {
            if (isRecipeResponseChanged(response)) {
                System.out.println(RecipeEditorViewModel.TAG + TAG + "onError:" + response);
                recipeResponse = response;
                onUseCaseError();
            }
        }
    }

    private boolean isRecipeResponseChanged(RecipeResponse recipeResponse) {
        return !this.recipeResponse.equals(recipeResponse);
    }

    private boolean isRecipeStateChanged(RecipeMetadataResponse metadataResponse) {
        return !this.metadataResponse.equals(metadataResponse);
    }

    private void onUseCaseSuccess() {
//        if (isEditorCreator()) {
//            if (isCloned) {
//                isCloned = false;
//                setupForClonedRecipe();
//            } else {
//                setupForExistingRecipe();
//            }
//        } else {
//            cloneRecipe();
//        }
    }

    private void onUseCaseError() {
        if (isNewRecipe) {
            setupForNewRecipe();
        }
    }

    private boolean isEditorCreator() {
//        return Constants.getUserId().equals(recipeResponse.getCreatedBy());
        return false;
    }

    private void cloneRecipe() {
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(recipeResponse.getId()).
//                setCloneToId(idProvider.getUId()).build();
//        handler.execute(recipeMacro, request, recipeResponseListener);
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

        if (metadataResponse.getMetadata().getState() == ComponentState.VALID_CHANGED) {
            showIngredientsButtonObservable.set(true);
            showReviewButton = true;
            navigator.refreshOptionsMenu();

        } else if (metadataResponse.getMetadata().getState() == ComponentState.VALID_UNCHANGED) {
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
        if (metadataResponse.getMetadata().getState() == ComponentState.INVALID_CHANGED) {
            navigator.showUnsavedChangedDialog();
        } else {
            navigator.cancelEditing();
        }
    }

    void reviewButtonPressed() {
        String recipeId = metadataResponse.getDomainId();
        if (isNewRecipe) {
            navigator.reviewNewRecipe(metadataResponse.getDomainId());

        } else if (isEditorCreator()) {
            navigator.reviewEditedRecipe(recipeId);

        }
//        else if (isCloned) {
//            navigator.reviewClonedRecipe(recipeId);
//        }
    }

    public void ingredientsButtonPressed() {
        String recipeId = metadataResponse.getDomainId();

        if (isNewRecipe) {
            navigator.addIngredients(recipeId);

        } else if (isEditorCreator()) {
            navigator.editIngredients(recipeId);

        }
//        else if (isCloned) {
//            navigator.reviewIngredients(recipeId);
//        }
    }
}