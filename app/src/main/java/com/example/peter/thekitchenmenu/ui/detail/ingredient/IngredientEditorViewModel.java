package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.UseCaseIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;

public class IngredientEditorViewModel extends ViewModel {

    private static final String TAG = "tkm-" + IngredientEditorViewModel.class.getSimpleName() + ":";

    private Resources resources;
    private UseCaseHandler handler;
    private UseCaseTextValidator useCaseTextValidator;
    private UseCaseIngredient useCaseIngredient;
    private AddEditIngredientNavigator navigator;

    public final ObservableField<String> showNameError = new ObservableField<>();
    public final ObservableField<String> showDescriptionError = new ObservableField<>();
    public final ObservableField<String> showDuplicateError = new ObservableField<>();
    final MutableLiveData<Boolean> showUseButton = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> dataLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> dataLoadingError = new MutableLiveData<>(false);

    private String ingredientId = "";
    private UseCaseIngredient.Response ingredientResponse;
    private boolean updatingUi;
    private boolean isNameValid;
    private boolean isDescriptionValid;
    private boolean isChanged;
    private boolean isValid;

    public IngredientEditorViewModel(Resources resources,
                                     UseCaseHandler handler,
                                     UseCaseTextValidator useCaseTextValidator,
                                     UseCaseIngredient useCaseIngredient) {
        this.resources = resources;
        this.handler = handler;
        this.useCaseTextValidator = useCaseTextValidator;
        this.useCaseIngredient = useCaseIngredient;
    }

    void setNavigator(AddEditIngredientNavigator navigator) {
        this.navigator = navigator;
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    void start() {
        if (isNewInstantiation()) {
            navigator.setActivityTitle(R.string.activity_title_add_new_ingredient);
            dataLoading.setValue(true);

            UseCaseIngredient.Model model = new UseCaseIngredient.Model.Builder().
                    getDefault().
                    build();

            executeUseCaseIngredient(model);
        }
    }

    void start(String ingredientId) {
        if (isNewInstantiation() || isIngredientIdChanged(ingredientId)) {
            navigator.setActivityTitle(R.string.activity_title_edit_ingredient);
            dataLoading.setValue(true);

            UseCaseIngredient.Model model = new UseCaseIngredient.Model.Builder().
                    getDefault().
                    setIngredientId(ingredientId).
                    build();

            executeUseCaseIngredient(model);
        }
    }

    private boolean isNewInstantiation() {
        return ingredientId.isEmpty();
    }

    private boolean isIngredientIdChanged(String ingredientId) {
        return !this.ingredientId.equals(ingredientId);
    }

    private void executeUseCaseIngredient(UseCaseIngredient.Model model) {
        dataLoading.setValue(true);
        UseCaseIngredient.Request request = new UseCaseIngredient.Request(model);

        handler.execute(
                useCaseIngredient,
                request,
                getUseCaseIngredientCallback());
    }

    private UseCaseInteractor.Callback<UseCaseIngredient.Response> getUseCaseIngredientCallback() {
        return new UseCaseInteractor.Callback<UseCaseIngredient.Response>() {

            @Override
            public void onSuccess(UseCaseIngredient.Response response) {
                processUseCaseIngredientResponse(response);
            }

            @Override
            public void onError(UseCaseIngredient.Response response) {
                processUseCaseIngredientResponse(response);
            }
        };
    }

    private void processUseCaseIngredientResponse(UseCaseIngredient.Response response) {
        dataLoading.setValue(false);
        ingredientResponse = response;

        System.out.println(TAG + response);

        UseCaseIngredient.Result result = response.getResult();

        if (result == UseCaseIngredient.Result.DATA_UNAVAILABLE) {
            dataLoadingError.setValue(true);
            showUseButton.setValue(false);
        } else if (result == UseCaseIngredient.Result.UNEDITABLE) {
            navigator.finishActivity("");
        } else if (result == UseCaseIngredient.Result.IS_DUPLICATE) {
            showUseButton.setValue(false);
            isValid = false;
            showDuplicateError.set("Duplicate name");
        } else if (result == UseCaseIngredient.Result.UNCHANGED_INVALID) {
            showUseButton.setValue(false);
            isChanged = false;
            isValid = false;
            updateObservables();
        } else if (result == UseCaseIngredient.Result.UNCHANGED_VALID) {
            isChanged = true;
            isValid = true;
            showUseButton.setValue(true);
        } else if (result == UseCaseIngredient.Result.CHANGED_INVALID) {
            isChanged = true;
            isValid = false;
            showUseButton.setValue(false);
            updateObservables();
        } else if (result == UseCaseIngredient.Result.CHANGED_VALID) {
            isChanged = true;
            isValid = true;
            showUseButton.setValue(true);
            updateObservables();
        }
    }

    private void updateObservables() {
        updatingUi = true;
        setName(ingredientResponse.getModel().getName());
        setDescription(ingredientResponse.getModel().getName());
        updatingUi = false;
        updateUseButtonVisibility();
    }

    public String getName() {
        return ingredientResponse.getModel().getName();
    }

    public void setName(String name) {
        if (!updatingUi) {
            validateName(name);
        }
    }

    private void validateName(String name) {
        showNameError.set(null);

        UseCaseTextValidator.Request request = getTextValidatorRequest(
                UseCaseTextValidator.RequestType.SHORT_TEXT,
                name
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
            isNameValid = true;

            UseCaseIngredient.Model model = UseCaseIngredient.Model.Builder.
                    basedOn(ingredientResponse.getModel()).
                    setName(response.getModel().getText()).
                    build();
            executeUseCaseIngredient(model);

        } else {
            isNameValid = false;
            setError(showNameError, response);
            updateUseButtonVisibility();
        }
    }

    public String getDescription() {
        return ingredientResponse.getModel().getDescription();
    }

    public void setDescription(String description) {
        if (!updatingUi) {
            validateDescription(description);
        }
    }

    private void validateDescription(String description) {
        showDescriptionError.set(null);

        UseCaseTextValidator.Request request = getTextValidatorRequest(
                UseCaseTextValidator.RequestType.LONG_TEXT,
                description);

        handler.execute(
                useCaseTextValidator,
                request,
                getLongTextValidatorCallback());

    }

    private UseCaseTextValidator.Request getTextValidatorRequest(
            UseCaseTextValidator.RequestType type, String textToValidate) {
        return new UseCaseTextValidator.Request(
                type,
                new UseCaseTextValidator.Model(textToValidate));
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

    private void processLongTextValidationResponse(UseCaseTextValidator.Response longTextResponse) {
        if (longTextResponse.getResult() == UseCaseTextValidator.Result.VALID) {
            isDescriptionValid = true;

            UseCaseIngredient.Model model = UseCaseIngredient.Model.Builder.
                    basedOn(ingredientResponse.getModel()).
                    setDescription(longTextResponse.getModel().getText()).
                    build();

            executeUseCaseIngredient(model);
        } else {
            isDescriptionValid = false;
            updateUseButtonVisibility();
            setError(showDescriptionError, longTextResponse);
        }
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

    private void updateUseButtonVisibility() {
        if (!updatingUi) {
            if (isValid() && isChanged) {
                showUseButton.setValue(true);
            } else
                showUseButton.setValue(false);
        }
    }

    private boolean isValid() {
        return isNameValid && isDescriptionValid;
    }

    void useButtonPressed() {
        navigator.finishActivity(ingredientResponse.getModel().getIngredientId());
    }
}
