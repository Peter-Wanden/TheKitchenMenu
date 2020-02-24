package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;


public class RecipeEditorViewModel
        extends ViewModel
        implements DataSource.GetEntityCallback<RecipeEntity> {

    private static final String TAG = "tkm-" + RecipeEditorViewModel.class.getSimpleName() + ":";

    private RepositoryRecipe repositoryRecipe;
    private AddEditRecipeNavigator navigator;
    private UniqueIdProvider idProvider;
    private Resources resources;
    private TimeProvider timeProvider;
    private final UseCaseHandler handler;

    public final ObservableBoolean showIngredientsButtonObservable = new ObservableBoolean();
    public final ObservableField<String> ingredientsButtonTextObservable = new ObservableField<>();
    public final ObservableBoolean dataIsLoadingObservable = new ObservableBoolean();

    private RecipeEntity recipeEntity;
    private String recipeId;
    private RecipeMacro recipeMacro;

    private boolean isDraft;
    private boolean isNewRecipe;
    private boolean showReviewButton;

    public RecipeEditorViewModel(TimeProvider timeProvider,
                                 RepositoryRecipe repositoryRecipe,
                                 UniqueIdProvider idProvider,
                                 Resources resources,
                                 UseCaseHandler handler,
                                 UseCaseFactory factory) {
        this.timeProvider = timeProvider;
        this.repositoryRecipe = repositoryRecipe;
        this.idProvider = idProvider;
        this.resources = resources;
        this.handler = handler;

        recipeMacro = factory.provideRecipeMacro();
    }

    void setNavigator(AddEditRecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public RecipeMacro getRecipeMacro() {
        return recipeMacro;
    }

    void start() {
        if (recipeEntity == null) {
            setupForNewRecipe();
        }
    }

    private void setupForNewRecipe() {
        isNewRecipe = true;
        navigator.setActivityTitle(R.string.activity_title_add_new_recipe);
        recipeId = idProvider.getUId();

        recipeEntity = getNewRecipe();
        saveRecipe();
        setIngredientsButton();
    }

    void start(String recipeId) {
        loadExistingRecipe(recipeId);
    }

    void start(String cloneFromRecipeId, String cloneToRecipeId) {

    }

    private void loadExistingRecipe(String recipeId) {
        dataIsLoadingObservable.set(true);
        repositoryRecipe.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeEntity recipeEntity) {
        dataIsLoadingObservable.set(false);
        this.recipeEntity = recipeEntity;

        if (editorIsCreatorOfRecipe()) {
            setupForExistingRecipe();
        }
        else {
            setupForClonedRecipe();
        }
    }

    private void setupForExistingRecipe() {
        isNewRecipe = false;
        navigator.setActivityTitle(R.string.activity_title_edit_recipe);

        setIngredientsButton();
    }

    private void setupForClonedRecipe() {
        isNewRecipe = false;
        navigator.setActivityTitle(R.string.activity_title_copy_recipe);

        recipeEntity = getClonedRecipeEntity();
        saveRecipe();
        startModelsWithClone();
        setIngredientsButton();
    }

    private void startModelsWithClone() {
        String oldRecipeId = recipeEntity.getParentId();
        String newRecipeId = recipeEntity.getId();
    }

    @Override
    public void onDataNotAvailable() {
        dataIsLoadingObservable.set(false);
        setupForNewRecipe();
    }

    private void setIngredientsButton() {
        if (isNewRecipe) {
            // Todo - isNewRecipe, change to ifNoIngredients when ingredient component added
            ingredientsButtonTextObservable.set(resources.getString(R.string.add_ingredients));

        } else if (editorIsCreatorOfRecipe()) {
            ingredientsButtonTextObservable.set(resources.getString(R.string.edit_ingredients));

        } else if (recipeIsCloned()) {
            ingredientsButtonTextObservable.set(resources.getString(R.string.review_ingredients));

        } else {
            throwUnknownEditingModeException();
        }
    }

    private void updateButtonVisibility() {
//        if (recipeStatus == VALID_CHANGED) {
//            showIngredientsButtonObservable.set(true);
//            showReviewButton = true;
//            navigator.refreshOptionsMenu();
//
//        } else if (recipeStatus == VALID_UNCHANGED) {
//            showIngredientsButtonObservable.set(true);
//            showReviewButton = false;
//            navigator.refreshOptionsMenu();
//
//        } else {
//            hideButtons();
//        }
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
//        if (recipeStatus == INVALID_CHANGED) {
//            navigator.showUnsavedChangedDialog();
//        } else {
//            navigator.cancelEditing();
//        }
    }

    void reviewButtonPressed() {
        String recipeId = recipeEntity.getId();

        if (isNewRecipe) {
            navigator.reviewNewRecipe(recipeId);

        } else if (editorIsCreatorOfRecipe()) {
            navigator.reviewEditedRecipe(recipeId);

        } else if (recipeIsCloned()) {
            navigator.reviewClonedRecipe(recipeId);

        } else {
            throwUnknownEditingModeException();
        }
    }

    public void ingredientsButtonPressed() {
        String recipeId = recipeEntity.getId();

        if (isNewRecipe) {
            navigator.addIngredients(recipeId);

        } else if (editorIsCreatorOfRecipe()) {
            navigator.editIngredients(recipeId);

        } else if (recipeIsCloned()) {
            navigator.reviewIngredients(recipeId);

        } else {
            throwUnknownEditingModeException();
        }
    }

    private RecipeEntity createNewEntity() {
        if (editorIsCreatorOfRecipe()) {
            return getEditedRecipe();

        } else if (recipeIsCloned()) {
            return getClonedRecipeEntity();

        } else {
            throw new RuntimeException("Unknown editing mode type");
        }
    }

    private RecipeEntity getNewRecipe() {
        String id = idProvider.getUId();
        long timeStamp = timeProvider.getCurrentTimeInMills();
        return new RecipeEntity(
                id,
                id,
                Constants.getUserId(),
                timeStamp,
                timeStamp
        );
    }

    private RecipeEntity getEditedRecipe() {
        return new RecipeEntity(
                recipeEntity.getId(),
                recipeEntity.getId(),
                recipeEntity.getCreatedBy(),
                recipeEntity.getCreateDate(),
                timeProvider.getCurrentTimeInMills()
        );
    }

    private RecipeEntity getClonedRecipeEntity() {
        long timeStamp = timeProvider.getCurrentTimeInMills();
        return new RecipeEntity(
                idProvider.getUId(),
                recipeEntity.getId(),
                recipeEntity.getCreatedBy(),
                timeStamp,
                timeStamp
        );
    }

    private boolean editorIsCreatorOfRecipe() {
        return recipeEntity.getCreatedBy().equals(Constants.getUserId());
    }

    private boolean recipeIsCloned() {
        return !editorIsCreatorOfRecipe();
    }

    private void saveRecipe() {
        repositoryRecipe.save(recipeEntity);
    }

    private void throwUnknownEditingModeException() {
        throw new RuntimeException("Unknown editing mode");
    }
}