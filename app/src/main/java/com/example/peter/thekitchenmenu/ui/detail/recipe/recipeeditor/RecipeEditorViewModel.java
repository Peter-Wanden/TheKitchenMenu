package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator.*;

public class RecipeEditorViewModel
        extends ViewModel
        implements DataSource.GetEntityCallback<RecipeEntity> {

    private DataSource<RecipeEntity> recipeEntityDataSource;
    private AddEditRecipeNavigator navigator;
    private UniqueIdProvider idProvider;
    private Resources resources;
    private TimeProvider timeProvider;
    private RecipeValidationStatus recipeValidationStatus = RecipeValidationStatus.INVALID_MISSING_MODELS;
    private RecipeValidator validator;

    private final SingleLiveEvent<Void> showUnsavedChangesDialogEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> setActivityTitleEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> enableReviewButtonEvent = new SingleLiveEvent<>();

    public final ObservableBoolean showIngredientsButtonObservable = new ObservableBoolean();
    public final ObservableField<String> ingredientsButtonTextObservable = new ObservableField<>();
    public final ObservableBoolean dataIsLoadingObservable = new ObservableBoolean();

    // the listener attached to this live data starts the other recipe models
    private final MutableLiveData<String> recipeIdLiveData = new MutableLiveData<>();
    private final MutableLiveData<RecipeEntity> recipeEntity = new MutableLiveData<>();

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
    }

    void setNavigator(AddEditRecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start() {
        setupForNewRecipe();
    }

    private void setupForNewRecipe() {
        isNewRecipe = true;
        recipeEntity.setValue(getEmptyRecipe());
        recipeIdLiveData.setValue(recipeEntity.getValue().getId());
        setActivityTitleEvent.setValue(R.string.activity_title_add_new_recipe);
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
        setupForExistingRecipe(recipeEntity);
    }

    @Override
    public void onDataNotAvailable() {
        dataIsLoadingObservable.set(false);
        setupForNewRecipe();
    }

    private void setupForExistingRecipe(RecipeEntity recipeEntity) {
        isNewRecipe = false;
        this.recipeEntity.setValue(recipeEntity);
        recipeIdLiveData.setValue(recipeEntity.getId());
        setActivityTitleEvent.setValue(R.string.activity_title_edit_recipe);
        setIngredientsButton();
    }

    private void setIngredientsButton() {
        if (isNewRecipe) {
            ingredientsButtonTextObservable.set(resources.getString(R.string.add_ingredients));

        } else if (creatorIsEditingOwnRecipe()) {
            ingredientsButtonTextObservable.set(resources.getString(R.string.edit_ingredients));

        } else if (recipeIsCloned()) {
            ingredientsButtonTextObservable.set(resources.getString(R.string.review_ingredients));

        } else {
            throwUnknownEditingModeException();
        }
    }

    MutableLiveData<String> getRecipeIdLiveData() {
        return recipeIdLiveData;
    }

    SingleLiveEvent<Integer> getSetActivityTitleEvent() {
        return setActivityTitleEvent;
    }

    SingleLiveEvent<Void> getEnableReviewButtonEvent() {
        return enableReviewButtonEvent;
    }

    void reportRecipeModelStatus(RecipeModelStatus modelStatus) {
        recipeValidationStatus = validator.getRecipeValidationStatus(modelStatus);

        if (recipeValidationStatus == RecipeValidationStatus.VALID_HAS_CHANGES)
            showButtons();
        else
            hideButtons();
    }

    private void showButtons() {
        showIngredientsButtonObservable.set(true);
        showReviewButton = true;
        enableReviewButtonEvent.call();
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
        if (recipeValidationStatus == RecipeValidationStatus.INVALID_HAS_CHANGES) {
            showUnsavedChangesDialogEvent.call();
        } else {
            navigator.cancelEditing();
        }
    }

    SingleLiveEvent<Void> getShowUnsavedChangesDialogEvent() {
        return showUnsavedChangesDialogEvent;
    }

    void reviewButtonPressed() {
        String recipeId = getRecipeId();

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
        String recipeId = getRecipeId();

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
        saveRecipe(recipeEntity);
        return recipeEntity.getId();
    }

    private RecipeEntity createNewEntity() {
        if (isNewRecipe) {
            return getNewRecipe();

        } else if (creatorIsEditingOwnRecipe()) {
            return getEditedRecipe();

        } else if (recipeIsCloned()) {
            return getClonedRecipe();

        } else {
            throw new RuntimeException("Unknown editing mode type");
        }
    }

    private RecipeEntity getEmptyRecipe() {
        String id = idProvider.getUId();
        long timeStamp = timeProvider.getCurrentTimestamp();
        return new RecipeEntity(
                id,
                id,
                Constants.getUserId().getValue(),
                timeStamp,
                timeStamp
        );
    }

    private RecipeEntity getNewRecipe() {
        String id = idProvider.getUId();
        long timeStamp = timeProvider.getCurrentTimestamp();
        return new RecipeEntity(
                id,
                id,
                Constants.getUserId().getValue(),
                timeStamp,
                timeStamp
        );
    }

    private RecipeEntity getEditedRecipe() {
        return new RecipeEntity(
                recipeEntity.getValue().getId(),
                recipeEntity.getValue().getId(),
                recipeEntity.getValue().getCreatedBy(),
                recipeEntity.getValue().getCreateDate(),
                timeProvider.getCurrentTimestamp()
        );
    }

    private RecipeEntity getClonedRecipe() {
        long timeStamp = timeProvider.getCurrentTimestamp();
        return new RecipeEntity(
                idProvider.getUId(),
                recipeEntity.getValue().getId(),
                recipeEntity.getValue().getCreatedBy(),
                timeStamp,
                timeStamp
        );
    }

    private boolean creatorIsEditingOwnRecipe() {
        return recipeEntity.getValue().getCreatedBy().equals(Constants.getUserId().getValue());
    }

    private boolean recipeIsCloned() {
        return !creatorIsEditingOwnRecipe();
    }

    private void saveRecipe(RecipeEntity recipeEntity) {
        recipeEntityDataSource.save(recipeEntity);
    }

    private void throwUnknownEditingModeException() {
        throw new RuntimeException("Unknown editing mode type");
    }
}