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
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;
import com.example.peter.thekitchenmenu.ui.utils.TextValidator;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidation.*;

public class RecipeIdentityEditorViewModel
        extends ObservableViewModel
        implements RecipeModelComposite.RecipeModelActions {

    private static final String TAG = "tkm-" + RecipeIdentityEditorViewModel.class.getSimpleName() + " ";

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private UseCaseRecipeIdentity useCase;
    @Nonnull
    private Resources resources;
    @Nonnull
    private TextValidator textValidator;

    private RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableBoolean dataLoadingError = new ObservableBoolean();

    private UseCaseRecipeIdentity.Response response = new UseCaseRecipeIdentity.Response.Builder().
            getDefault().build();

    private boolean updatingUi;
    private boolean dataLoading;

    public RecipeIdentityEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull UseCaseRecipeIdentity useCase,
                                         @Nonnull Resources resources,
                                         @Nonnull TextValidator textValidator) {
        this.handler = handler;
        this.useCase = useCase;
        this.resources = resources;
        this.textValidator = textValidator;
    }

    void setModelValidationSubmitter(RecipeValidatorModelSubmission modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            executeUseCase(recipeId, UseCaseRecipeIdentity.DO_NOT_CLONE, response.getModel());
        }
    }

    @Override
    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            executeUseCase(cloneFromRecipeId, cloneToRecipeId, response.getModel());
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return response.getRecipeId().isEmpty() || !response.getRecipeId().equals(recipeId);
    }

    private void executeUseCase(String recipeId,
                                String cloneToRecipeId,
                                UseCaseRecipeIdentity.Model model) {

        UseCaseRecipeIdentity.Request request = new UseCaseRecipeIdentity.Request.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();

        dataLoading = true;
        handler.execute(useCase, request, getCallback());
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

        TextValidator.Response response = validateText(TextValidator.RequestType.SHORT_TEXT, title);
        if (response.getResult() == TextValidator.Result.VALID) {
            executeUseCase(UseCaseRecipeIdentity.Model.Builder.
                    basedOn(this.response.getModel()).
                    setTitle(title).
                    build()
            );
        } else {
            setError(titleErrorMessage, response);
            submitModelStatus(true, false);
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

        TextValidator.Response response = validateText(TextValidator.RequestType.LONG_TEXT,
                description);
        if (response.getResult() == TextValidator.Result.VALID) {
            executeUseCase(UseCaseRecipeIdentity.Model.Builder.
                    basedOn(this.response.getModel()).
                    setDescription(description).
                    build()
            );
        } else {
            setError(descriptionErrorMessage, response);
            submitModelStatus(true, false);
        }
    }

    private TextValidator.Response validateText(TextValidator.RequestType type,
                                                String textToValidate) {
        TextValidator.Request request = new TextValidator.Request(type, textToValidate);
        return textValidator.validateText(request);
    }

    private void setError(ObservableField<String> errorObservable,
                          TextValidator.Response response) {
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

    private void executeUseCase(UseCaseRecipeIdentity.Model model) {
        dataLoading = true;

        UseCaseRecipeIdentity.Request request = new UseCaseRecipeIdentity.Request.Builder().
                setRecipeId(response.getRecipeId()).
                setCloneToRecipeId("").
                setModel(model).
                build();

        handler.execute(useCase, request, getCallback());
    }

    private UseCaseInteractor.Callback<UseCaseRecipeIdentity.Response> getCallback() {
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
            submitModelStatus(false, false);
            return;
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.INVALID_UNCHANGED) {
            submitModelStatus(false, false);
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.VALID_UNCHANGED) {
            submitModelStatus(false, true);
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.INVALID_CHANGED) {
            submitModelStatus(true, false);
        } else if (response.getResult() == UseCaseRecipeIdentity.Result.VALID_CHANGED) {
            submitModelStatus(true, true);
        }
        updateObservables();
    }

    private void submitModelStatus(boolean isChanged, boolean isValid) {
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