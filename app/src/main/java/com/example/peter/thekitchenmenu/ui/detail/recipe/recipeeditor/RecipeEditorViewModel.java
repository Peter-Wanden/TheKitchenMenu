package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.RecipeValidationStatus.*;

public class RecipeEditorViewModel
        extends ViewModel
        implements
        DataSource.GetEntityCallback<RecipeEntity>, RecipeValidation.RecipeEditor {

    private static final String TAG = "tkm-RecipeEditorVM";

    private DataSource<RecipeEntity> recipeEntityDataSource;
    private AddEditRecipeNavigator navigator;
    private UniqueIdProvider idProvider;
    private Resources resources;
    private TimeProvider timeProvider;
    private RecipeValidationStatus recipeValidationStatus = INVALID_MISSING_MODELS;
    private RecipeValidator validator;
    private RecipeModelComposite recipeModels;

    private final SingleLiveEvent<Void> showUnsavedChangesDialogEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> setActivityTitleEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> enableReviewButtonEvent = new SingleLiveEvent<>();

    public final ObservableBoolean showIngredientsButtonObservable = new ObservableBoolean();
    public final ObservableField<String> ingredientsButtonTextObservable = new ObservableField<>();
    public final ObservableBoolean dataIsLoadingObservable = new ObservableBoolean();

    private RecipeEntity recipeEntity;

    private boolean isDraft;
    private boolean isNewRecipe;
    private boolean showReviewButton;

    public RecipeEditorViewModel(TimeProvider timeProvider,
                                 DataSource<RecipeEntity> recipeEntityDataSource,
                                 UniqueIdProvider idProvider,
                                 Resources resources,
                                 RecipeValidator validator) {
        this.timeProvider = timeProvider;
        this.recipeEntityDataSource = recipeEntityDataSource;
        this.idProvider = idProvider;
        this.resources = resources;
        this.validator = validator;
        validator.setRecipeEditor(this);
    }

    void setNavigator(AddEditRecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void setRecipeModelComposite(RecipeModelComposite recipeModels) {
        this.recipeModels = recipeModels;
    }

    void start() {
        setupForNewRecipe();
    }

    private void setupForNewRecipe() {
        isNewRecipe = true;

        setActivityTitleEvent.setValue(R.string.activity_title_add_new_recipe);

        recipeEntity = getNewRecipe();
//        Log.d(TAG, "setupForNewRecipe: Save called");
        saveRecipe();
        startModels();
        setIngredientsButton();
    }

    void start(String recipeId) {
        loadExistingRecipe(recipeId);
    }

    private void loadExistingRecipe(String recipeId) {
        dataIsLoadingObservable.set(true);
        recipeEntityDataSource.getById(recipeId, this);
    }

    @Override
    public void onEntityLoaded(RecipeEntity recipeEntity) {
        dataIsLoadingObservable.set(false);
        this.recipeEntity = recipeEntity;

        if (creatorIsEditingOwnRecipe())
            setupForExistingRecipe();
        else {
            setupForClonedRecipe();
        }
    }

    private void setupForExistingRecipe() {
        isNewRecipe = false;
        setActivityTitleEvent.setValue(R.string.activity_title_edit_recipe);

        startModels();
        setIngredientsButton();
    }

    private void setupForClonedRecipe() {
        isNewRecipe = false;
        setActivityTitleEvent.setValue(R.string.activity_title_copy_recipe);

        recipeEntity = getClonedRecipeEntity();
//        Log.d(TAG, "setupForClonedRecipe: save called");
        saveRecipe();
        startModelsWithClone();
        setIngredientsButton();
    }

    private void startModels() {
        recipeModels.start(recipeEntity.getId());
    }

    private void startModelsWithClone() {
        String oldRecipeId = recipeEntity.getParentId();
        String newRecipeId = recipeEntity.getId();
        recipeModels.startWithClonedModel(oldRecipeId, newRecipeId);
    }

    @Override
    public void onDataNotAvailable() {
        dataIsLoadingObservable.set(false);
        setupForNewRecipe();
    }

    SingleLiveEvent<Integer> getSetActivityTitleEvent() {
        return setActivityTitleEvent;
    }

    private void setIngredientsButton() {
        if (isNewRecipe) {
            // Todo - isNewRecipe, change to ifNoIngredients when ingredient component added
            ingredientsButtonTextObservable.set(resources.getString(R.string.add_ingredients));

        } else if (creatorIsEditingOwnRecipe()) {
            ingredientsButtonTextObservable.set(resources.getString(R.string.edit_ingredients));

        } else if (recipeIsCloned()) {
            ingredientsButtonTextObservable.set(resources.getString(R.string.review_ingredients));

        } else {
            throwUnknownEditingModeException();
        }
    }

    SingleLiveEvent<Void> getEnableReviewButtonEvent() {
        return enableReviewButtonEvent;
    }

    RecipeValidator getValidator() {
        return validator;
    }

    @Override
    public void setValidationStatus(RecipeValidationStatus recipeValidationStatus) {
        this.recipeValidationStatus = recipeValidationStatus;

//        Log.d(TAG, "setValidationStatus=" + recipeValidationStatus);

        isDraft = recipeValidationStatus != VALID_HAS_CHANGES &&
                recipeValidationStatus != VALID_NO_CHANGES;

        if (recipeValidationStatus != INVALID_NO_CHANGES &&
                recipeValidationStatus != VALID_NO_CHANGES) {

            this.recipeEntity = createNewEntity();
            saveRecipe();
        }
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        if (recipeValidationStatus == VALID_HAS_CHANGES) {
            showIngredientsButtonObservable.set(true);
            showReviewButton = true;
            enableReviewButtonEvent.call();

        } else if (recipeValidationStatus == VALID_NO_CHANGES) {
            showIngredientsButtonObservable.set(true);
            showReviewButton = false;
            enableReviewButtonEvent.call();

        } else {
            hideButtons();
        }
    }

    private void hideButtons() {
        showIngredientsButtonObservable.set(false);
        showReviewButton = false;
        enableReviewButtonEvent.call();
    }

    boolean isShowReviewButton() {
        return showReviewButton;
    }

    void upOrBackPressed() {
        if (recipeValidationStatus == INVALID_HAS_CHANGES) {
            showUnsavedChangesDialogEvent.call();
        } else {
            navigator.cancelEditing();
        }
    }

    SingleLiveEvent<Void> getShowUnsavedChangesDialogEvent() {
        return showUnsavedChangesDialogEvent;
    }

    void reviewButtonPressed() {
        String recipeId = recipeEntity.getId();

        if (isNewRecipe) {
            navigator.reviewNewRecipe(recipeId);

        } else if (creatorIsEditingOwnRecipe()) {
            navigator.reviewEditedRecipe(recipeId);

        } else if (recipeIsCloned()) {
            navigator.reviewClonedRecipe(recipeId);

        } else {
            throwUnknownEditingModeException();
        }
    }

    void ingredientsButtonPressed() {
        String recipeId = recipeEntity.getId();

        if (isNewRecipe) {
            navigator.addIngredients(recipeId);

        } else if (creatorIsEditingOwnRecipe()) {
            navigator.editIngredients(recipeId);

        } else if (recipeIsCloned()) {
            navigator.reviewIngredients(recipeId);

        } else {
            throwUnknownEditingModeException();
        }
    }

    private String getRecipeId() {
        RecipeEntity recipeEntity = createNewEntity();
        return recipeEntity.getId();
    }

    private RecipeEntity createNewEntity() {
        if (creatorIsEditingOwnRecipe()) {
            return getEditedRecipe();

        } else if (recipeIsCloned()) {
            return getClonedRecipeEntity();

        } else {
            throw new RuntimeException("Unknown editing mode type");
        }
    }

    private RecipeEntity getNewRecipe() {
        String id = idProvider.getUId();
        long timeStamp = timeProvider.getCurrentTimestamp();
        return new RecipeEntity(
                id,
                id,
                Constants.getUserId().getValue(),
                timeStamp,
                timeStamp,
                true
        );
    }

    private RecipeEntity getEditedRecipe() {
        return new RecipeEntity(
                recipeEntity.getId(),
                recipeEntity.getId(),
                recipeEntity.getCreatedBy(),
                recipeEntity.getCreateDate(),
                timeProvider.getCurrentTimestamp(),
                isDraft
        );
    }

    private RecipeEntity getClonedRecipeEntity() {
        long timeStamp = timeProvider.getCurrentTimestamp();
        return new RecipeEntity(
                idProvider.getUId(),
                recipeEntity.getId(),
                recipeEntity.getCreatedBy(),
                timeStamp,
                timeStamp,
                isDraft
        );
    }

    private boolean creatorIsEditingOwnRecipe() {
        return recipeEntity.getCreatedBy().equals(Constants.getUserId().getValue());
    }

    private boolean recipeIsCloned() {
        return !creatorIsEditingOwnRecipe();
    }

    private void saveRecipe() {
//        Log.d(TAG, "saveRecipe: " + recipeEntity);
        recipeEntityDataSource.save(recipeEntity);
    }

    private void throwUnknownEditingModeException() {
        throw new RuntimeException("Unknown editing mode type");
    }
}