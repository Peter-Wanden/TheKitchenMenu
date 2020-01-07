package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidation.*;

public class RecipeIdentityEditorViewModel
        extends
        ObservableViewModel
        implements
        RecipeModelObserver.RecipeModelActions,
        UseCaseCommand.Callback<RecipeIdentityResponse> {

    private static final String TAG =
            "tkm-" + RecipeIdentityEditorViewModel.class.getSimpleName() + ":";

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private RecipeIdentity recipeIdentity;
    @Nonnull
    private TextValidator textValidator;
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
                                         @Nonnull RecipeIdentity recipeIdentity,
                                         @Nonnull TextValidator textValidator,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipeIdentity = recipeIdentity;
        this.textValidator = textValidator;
        this.resources = resources;
        response = new RecipeIdentityResponse.Builder().getDefault().build();
    }

    void setModelValidationSubmitter(RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            executeUseCaseRecipeIdentity(
                    recipeId,
                    RecipeIdentity.DO_NOT_CLONE,
                    response.getModel());
        }
    }

    @Override
    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            executeUseCaseRecipeIdentity(
                    cloneFromRecipeId,
                    cloneToRecipeId,
                    response.getModel());
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return response.getRecipeId().isEmpty() || !response.getRecipeId().equals(recipeId);
    }

    private void executeUseCaseRecipeIdentity(String recipeId,
                                              String cloneToRecipeId,
                                              RecipeIdentityModel model) {

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();

        dataLoading = true;
        handler.execute(recipeIdentity, request, this);
    }

    @Bindable
    public String getTitle() {
        return response.getModel().getTitle();
    }

    public void setTitle(String title) {
        if (!updatingUi) {
            validateTitle(title);
        }
    }

    private void validateTitle(String title) {
        titleErrorMessage.set(null);

        TextValidatorRequest request = getTextValidatorRequest(
                TextValidator.RequestType.SHORT_TEXT,
                title
        );
        handler.execute(
                textValidator,
                request, new UseCaseCommand.Callback<TextValidatorResponse>() {
                    @Override
                    public void onSuccess(TextValidatorResponse response) {
                        processShortTextValidationResponse(response);
                    }

                    @Override
                    public void onError(TextValidatorResponse response) {
                        processShortTextValidationResponse(response);
                    }
                });
    }

    private void processShortTextValidationResponse(TextValidatorResponse response) {
        if (response.getResult() == TextValidator.Result.VALID) {

            executeUseCaseRecipeIdentity(RecipeIdentityModel.Builder.
                    basedOn(this.response.getModel()).
                    setTitle(response.getModel().getText()).
                    build()
            );
        } else {
            setError(titleErrorMessage, response);
            updateRecipeComponentStatus(false, true);
        }
    }

    @Bindable
    public String getDescription() {
        return response.getModel().getDescription();
    }

    public void setDescription(String description) {
        if (!updatingUi) {
            validateDescription(description);
        }
    }

    private void validateDescription(String description) {
        descriptionErrorMessage.set(null);

        TextValidatorRequest request = getTextValidatorRequest(
                TextValidator.RequestType.LONG_TEXT,
                description
        );

        handler.execute(
                textValidator,
                request,
                new UseCaseCommand.Callback<TextValidatorResponse>() {
                    @Override
                    public void onSuccess(TextValidatorResponse response) {
                        processLongTextValidationResponse(response);
                    }

                    @Override
                    public void onError(TextValidatorResponse response) {
                        processLongTextValidationResponse(response);
                    }
                });
    }

    private void processLongTextValidationResponse(TextValidatorResponse response) {
        if (response.getResult() == TextValidator.Result.VALID) {

            executeUseCaseRecipeIdentity(RecipeIdentityModel.Builder.
                    basedOn(this.response.getModel()).
                    setDescription(response.getModel().getText()).
                    build()
            );
        } else {
            setError(descriptionErrorMessage, response);
            updateRecipeComponentStatus(false, true);
        }
    }

    private TextValidatorRequest getTextValidatorRequest(
            TextValidator.RequestType type, String textToValidate) {
        return new TextValidatorRequest(
                type,
                new TextValidatorModel(textToValidate));
    }

    private void setError(ObservableField<String> errorObservable,
                          TextValidatorResponse response) {
        if (response.getResult() == TextValidator.Result.TOO_SHORT) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_short,
                    response.getMinLength(),
                    response.getMaxLength()));

        } else if (response.getResult() == TextValidator.Result.TOO_LONG) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_long,
                    response.getMinLength(),
                    response.getMaxLength()
            ));
        }
    }

    private void executeUseCaseRecipeIdentity(RecipeIdentityModel model) {
        dataLoading = true;

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setRecipeId(response.getRecipeId()).
                setCloneToRecipeId("").
                setModel(model).
                build();

        handler.execute(recipeIdentity, request, this);
    }

    @Override
    public void onSuccess(RecipeIdentityResponse response) {
        processUseCaseResponse(response);
    }

    @Override
    public void onError(RecipeIdentityResponse response) {
        processUseCaseResponse(response);
    }

    private void processUseCaseResponse(RecipeIdentityResponse response) {
        this.response = response;
        dataLoading = false;
        if (response.getResult() == RecipeIdentity.Result.DATA_UNAVAILABLE) {
            dataLoadingError.set(true);
            updateRecipeComponentStatus(false, false);
            return;
        } else if (response.getResult() == RecipeIdentity.Result.INVALID_UNCHANGED) {
            updateRecipeComponentStatus(false, false);
        } else if (response.getResult() == RecipeIdentity.Result.VALID_UNCHANGED) {
            updateRecipeComponentStatus(true, false);
        } else if (response.getResult() == RecipeIdentity.Result.INVALID_CHANGED) {
            updateRecipeComponentStatus(false, true);
        } else if (response.getResult() == RecipeIdentity.Result.VALID_CHANGED) {
            updateRecipeComponentStatus(true, true);
        }
        updateObservables();
    }

    private void updateRecipeComponentStatus(boolean isValid, boolean isChanged) {
        if (!updatingUi) {
            modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStateModel(
                    RecipeValidator.ComponentName.IDENTITY,
                    getStatus(isChanged, isValid))
            );
        }
    }

    private RecipeValidator.ComponentState getStatus(boolean isChanged, boolean isValid) {
        if (!isValid && !isChanged) {
            return RecipeValidator.ComponentState.INVALID_UNCHANGED;

        } else if (isValid && !isChanged) {
            return RecipeValidator.ComponentState.VALID_UNCHANGED;

        } else if (!isValid && isChanged) {
            return RecipeValidator.ComponentState.INVALID_CHANGED;

        } else {
            return RecipeValidator.ComponentState.VALID_CHANGED;
        }
    }

    private void updateObservables() {
        updatingUi = true;
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.description);
        updatingUi = false;
    }
}