package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.FailReasons;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipemediator.Recipe;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState.*;
import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidation.*;

public class RecipeIdentityEditorViewModel
        extends
        ObservableViewModel
        implements
        RecipeModelObserver.RecipeModelActions {

    private static final String TAG = "tkm-" + RecipeIdentityEditorViewModel.class.getSimpleName() +
            ": ";

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private Recipe recipe;
    @Nonnull
    private Resources resources;

    private RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableBoolean dataLoadingError = new ObservableBoolean();

    private RecipeIdentityResponse response;

    private boolean updatingUi;
    private boolean dataLoading;

    public RecipeIdentityEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull Recipe recipe,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipe = recipe;
        this.resources = resources;
        response = RecipeIdentityResponse.Builder.getDefault().build();
    }

    void setModelValidationSubmitter(RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {

            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setRecipeId(recipeId).
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setModel(RecipeIdentityRequest.Model.Builder.getDefault().build()).
                    build();
            handler.execute(recipe, request, getCallback());
        }
    }

    @Override
    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {

            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setRecipeId(cloneFromRecipeId).
                    setCloneToRecipeId(cloneToRecipeId).
                    setModel(RecipeIdentityRequest.Model.Builder.getDefault().build()).
                    build();

            handler.execute(recipe, request, getCallback());
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return response.getModel().getId().isEmpty() ||
                !response.getModel().getId().equals(recipeId);
    }

    private UseCaseCommand.Callback<RecipeIdentityResponse> getCallback() {
        return new UseCaseCommand.Callback<RecipeIdentityResponse>() {
            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                RecipeIdentityEditorViewModel.this.response = response;
                updateRecipeComponentStatus();
                onUseCaseSuccess();
            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                RecipeIdentityEditorViewModel.this.response = response;
                updateRecipeComponentStatus();
                onUseCaseError();
            }
        };
    }

    @Bindable
    public String getTitle() {
        return response.getModel().getTitle();
    }

    public void setTitle(String title) {
        if (isTitleChanged(title)) {

            RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                    setTitle(title).
                    setDescription(response.getModel().getDescription()).
                    build();
            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setRecipeId(response.getModel().getId()).
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setModel(model).
                    build();
            handler.execute(recipe, request, getCallback());
        }
    }

    private boolean isTitleChanged(String title) {
        return !updatingUi && !response.getModel().getTitle().equals(title.trim());
    }

    @Bindable
    public String getDescription() {
        return response.getModel().getDescription();
    }

    public void setDescription(String description) {
        if (isDescriptionChanged(description)) {
            RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                    setTitle(response.getModel().getTitle()).
                    setDescription(description).
                    build();
            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setRecipeId(response.getModel().getId()).
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setModel(model).
                    build();
            handler.execute(recipe, request, getCallback());
        }
    }

    private boolean isDescriptionChanged(String description) {
        return !updatingUi && !response.getModel().getDescription().equals(description.trim());
    }

    private void onUseCaseSuccess() {
        clearErrors();
        updateObservables();
    }

    private void clearErrors() {
        titleErrorMessage.set(null);
        descriptionErrorMessage.set(null);
    }

    private void onUseCaseError() {
        List<FailReasons> failReasons = response.getFailReasons();
        if (failReasons.contains(RecipeIdentity.FailReason.DATA_UNAVAILABLE)) {
            dataLoadingError.set(true);
        }
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT)) {
            titleErrorMessage.set(
                    resources.getString(R.string.input_error_text_too_short,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_max_length))));
        }
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG)) {
            titleErrorMessage.set(
                    resources.getString(R.string.input_error_text_too_long,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_max_length))));
        }
        if (failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG)) {
            descriptionErrorMessage.set(
                    resources.getString(R.string.input_error_text_too_long,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_long_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_long_text_max_length))));
        }
    }

    private void updateRecipeComponentStatus() {
        if (!updatingUi) {
            modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStateModel(
                    ComponentName.IDENTITY,
                    response.getState())
            );
        }
    }

    private void updateObservables() {
        updatingUi = true;
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.description);
        updatingUi = false;
    }
}