package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.model.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.provider.TimeProvider;
import com.example.peter.thekitchenmenu.utils.SingleLiveEvent;

import java.util.UUID;

public class RecipeEditorViewModel extends AndroidViewModel {

    private AddEditRecipeNavigator navigator;

    private final SingleLiveEvent<Void> showUnsavedChangesDialogEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> setActivityTitleEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> enableReviewButtonEvent = new SingleLiveEvent<>();

    // Models listen to this recipeEntityLiveData for their initial values
    private final MutableLiveData<RecipeEntity> recipeEntity = new MutableLiveData<>();
    public static final RecipeEntity EMPTY_RECIPE = new RecipeEntity(
            "",
            "",
            "",
            0,
            0,
            "",
            0L,
            0L);

    private RecipeIdentityModel identityModel;
    private boolean
            isNewRecipe,
            validIdentityModel,
            identityModelChanged,
            showReviewButton;
    private TimeProvider timeProvider;

    public RecipeEditorViewModel(@NonNull Application application, TimeProvider timeProvider) {
        super(application);
        this.timeProvider = timeProvider;
    }

    void setNavigator(AddEditRecipeNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start() {
        isNewRecipe = true;
        recipeEntity.setValue(EMPTY_RECIPE);
        setActivityTitleEvent.setValue(R.string.activity_title_add_new_recipe);
    }

    void start(RecipeEntity recipeEntity) {
        isNewRecipe = false;
        this.recipeEntity.setValue(recipeEntity);
        setActivityTitleEvent.setValue(R.string.activity_title_edit_recipe);
    }

    MutableLiveData<RecipeEntity> getRecipeEntity() {
        return recipeEntity;
    }

    SingleLiveEvent<Integer> getSetActivityTitleEvent() {
        return setActivityTitleEvent;
    }

    SingleLiveEvent<Void> getEnableReviewButtonEvent() {
        return enableReviewButtonEvent;
    }

    void setRecipeIdentityModel(RecipeIdentityModelMetaData identityModelMetaData) {
        identityModel = identityModelMetaData.getIdentityModel();
        validIdentityModel = identityModelMetaData.isValidModel();
        identityModelChanged = identityModelMetaData.isModelChanged();
        enableReviewIfIsValidRecipeEntity();
    }

    private void enableReviewIfIsValidRecipeEntity() {
        if (modelsAreValid() && identityModelChanged)
            showReviewButton();
        else
            hideReviewButton();
    }

    private boolean modelsAreValid() {
        return validIdentityModel;
    }

    private void showReviewButton() {
        showReviewButton = true;
        enableReviewButtonEvent.call();
    }

    private void hideReviewButton() {
        showReviewButton = false;
        enableReviewButtonEvent.call();
    }

    boolean isShowReviewButton() {
        return showReviewButton;
    }

    void upOrBackPressed() {
        if (modelsHaveChanged()) {
            showUnsavedChangesDialogEvent.call();
        } else {
            navigator.cancelEditing();
        }
    }

    private boolean modelsHaveChanged() {
        return identityModelChanged;
    }

    SingleLiveEvent<Void> getShowUnsavedChangesDialogEvent() {
        return showUnsavedChangesDialogEvent;
    }

    void createOrUpdateRecipe() {

        if (isNewRecipe) {
            navigator.reviewNewRecipe(new RecipeEntity(
                    String.valueOf(UUID.randomUUID()),
                    identityModel.getTitle(),
                    identityModel.getDescription(),
                    identityModel.getPrepTime(),
                    identityModel.getCookTime(),
                    Constants.getUserId().getValue(),
                    timeProvider.getCurrentTimestamp(),
                    timeProvider.getCurrentTimestamp()
            ));
        } else {
            navigator.updateExistingRecipe(new RecipeEntity(
                    recipeEntity.getValue().getId(),
                    identityModel.getTitle(),
                    identityModel.getDescription(),
                    identityModel.getPrepTime(),
                    identityModel.getCookTime(),
                    recipeEntity.getValue().getCreatedBy(),
                    recipeEntity.getValue().getCreateDate(),
                    timeProvider.getCurrentTimestamp()
            ));
        }
    }
}
