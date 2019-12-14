package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipeIdentity.UseCaseRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidation.*;

public class RecipeIdentityEditorViewModel
        extends ObservableViewModel
        implements RecipeModelComposite.RecipeModelActions {

    private static final String TAG =
            "tkm-" + RecipeIdentityEditorViewModel.class.getSimpleName() + " ";

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private UseCaseRecipeIdentity useCaseRecipeIdentity;
    @Nonnull
    private UseCaseTextValidator useCaseTextValidator;
    @Nonnull
    private Resources resources;

    private RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableBoolean dataLoadingError = new ObservableBoolean();

    private UseCaseRecipeIdentity.Response response = new UseCaseRecipeIdentity.Response.Builder().
            getDefault().build();

    private boolean updatingUi;
    private boolean dataLoading;

    public RecipeIdentityEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull UseCaseRecipeIdentity useCaseRecipeIdentity,
                                         @Nonnull UseCaseTextValidator useCaseTextValidator,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.useCaseRecipeIdentity = useCaseRecipeIdentity;
        this.useCaseTextValidator = useCaseTextValidator;
        this.resources = resources;
    }

    void setModelValidationSubmitter(RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            executeUseCaseRecipeIdentity(
                    recipeId,
                    UseCaseRecipeIdentity.DO_NOT_CLONE,
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
                                              UseCaseRecipeIdentity.Model model) {

        UseCaseRecipeIdentity.Request request = new UseCaseRecipeIdentity.Request.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();

        dataLoading = true;
        handler.execute(useCaseRecipeIdentity, request, getUseCaseRecipeIdentityCallback());
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

        UseCaseTextValidator.Request request = getTextValidatorRequest(
                UseCaseTextValidator.RequestType.SHORT_TEXT,
                title
        );
        handler.execute(
                useCaseTextValidator,
                request,
                getShortTextValidatorCallback());
    }

    private UseCaseTextValidator.Callback<UseCaseTextValidator.Response>
    getShortTextValidatorCallback() {
        return new UseCaseTextValidator.Callback<UseCaseTextValidator.Response>() {

            @Override
            public void onSuccess(UseCaseTextValidator.Response response) {
                processShortTextValidationResponse(response);
            }

            @Override
            public void onError(UseCaseTextValidator.Response response) {
                processShortTextValidationResponse(response);
            }
        };
    }

    private void processShortTextValidationResponse(UseCaseTextValidator.Response response) {
        if (response.getResult() == UseCaseTextValidator.Result.VALID) {

            executeUseCaseRecipeIdentity(UseCaseRecipeIdentity.Model.Builder.
                    basedOn(this.response.getModel()).
                    setTitle(response.getModel().getText()).
                    build()
            );
        } else {
            setError(titleErrorMessage, response);
            updateRecipeComponentStatus(true, false);
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

        UseCaseTextValidator.Request request = getTextValidatorRequest(
                UseCaseTextValidator.RequestType.LONG_TEXT,
                description
        );
        handler.execute(
                useCaseTextValidator,
                request,
                getLongTextValidatorCallback());
    }

    private UseCaseTextValidator.Callback<UseCaseTextValidator.Response>
    getLongTextValidatorCallback() {
        return new UseCaseTextValidator.Callback<UseCaseTextValidator.Response>() {

            @Override
            public void onSuccess(UseCaseTextValidator.Response response) {
                processLongTextValidationResponse(response);
            }

            @Override
            public void onError(UseCaseTextValidator.Response response) {
                processLongTextValidationResponse(response);
            }
        };
    }

    private void processLongTextValidationResponse(UseCaseTextValidator.Response response) {
        if (response.getResult() == UseCaseTextValidator.Result.VALID) {

            executeUseCaseRecipeIdentity(UseCaseRecipeIdentity.Model.Builder.
                    basedOn(this.response.getModel()).
                    setDescription(response.getModel().getText()).
                    build()
            );
        } else {
            setError(descriptionErrorMessage, response);
            updateRecipeComponentStatus(true, false);
        }
    }

    private UseCaseTextValidator.Request getTextValidatorRequest(
            UseCaseTextValidator.RequestType type, String textToValidate) {
        return new UseCaseTextValidator.Request(
                type,
                new UseCaseTextValidator.Model(textToValidate));
    }

    private void setError(ObservableField<String> errorObservable,
                          UseCaseTextValidator.Response response) {
        if (response.getResult() == UseCaseTextValidator.Result.TOO_SHORT) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_short,
                    response.getMinLength(),
                    response.getMaxLength()));

        } else if (response.getResult() == UseCaseTextValidator.Result.TOO_LONG) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_long,
                    response.getMinLength(),
                    response.getMaxLength()
            ));
        }
    }

    private void executeUseCaseRecipeIdentity(UseCaseRecipeIdentity.Model model) {
        dataLoading = true;

        UseCaseRecipeIdentity.Request request = new UseCaseRecipeIdentity.Request.Builder().
                setRecipeId(response.getRecipeId()).
                setCloneToRecipeId("").
                setModel(model).
                build();

        handler.execute(
                useCaseRecipeIdentity,
                request,
                getUseCaseRecipeIdentityCallback());
    }

    private UseCaseInteractor.Callback<UseCaseRecipeIdentity.Response>
    getUseCaseRecipeIdentityCallback() {
        return new UseCaseInteractor.Callback<UseCaseRecipeIdentity.Response>() {

            @Override
            public void onSuccess(UseCaseRecipeIdentity.Response response) {
                processUseCaseResponse(response);
            }

            @Override
            public void onError(UseCaseRecipeIdentity.Response response) {
                processUseCaseResponse(response);
            }
        };
    }

    private void processUseCaseResponse(UseCaseRecipeIdentity.Response response) {
        this.response = response;
        dataLoading = false;
        if (response.getResult() == UseCaseRecipeIdentity.Result.DATA_UNAVAILABLE) {
            dataLoadingError.set(true);
            updateRecipeComponentStatus(false, false);
            return;
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.INVALID_UNCHANGED) {
            updateRecipeComponentStatus(false, false);
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.VALID_UNCHANGED) {
            updateRecipeComponentStatus(false, true);
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.INVALID_CHANGED) {
            updateRecipeComponentStatus(true, false);
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.VALID_CHANGED) {
            updateRecipeComponentStatus(true, true);
        }
        updateObservables();
    }

    private void updateRecipeComponentStatus(boolean isChanged, boolean isValid) {
        if (!updatingUi) {
            modelSubmitter.submitModelStatus(new RecipeModelStatus(
                    RecipeValidator.ModelName.IDENTITY_MODEL,
                    isChanged,
                    isValid
            ));
        }
    }

    private void updateObservables() {
        updatingUi = true;
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.description);
        updatingUi = false;
    }
}