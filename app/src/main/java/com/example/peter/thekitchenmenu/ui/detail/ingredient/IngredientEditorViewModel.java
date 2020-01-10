package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.res.Resources;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientModel;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientRequest;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientResponse;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorRequest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorResponse;

public class IngredientEditorViewModel
        extends ViewModel
        implements UseCaseCommand.Callback<IngredientResponse> {

    private static final String TAG = "tkm-" + IngredientEditorViewModel.class.getSimpleName() + ":";

    private Resources resources;
    private UseCaseHandler handler;
    private TextValidator textValidator;
    private Ingredient ingredient;
    private AddEditIngredientNavigator navigator;

    public final ObservableField<String> showNameError = new ObservableField<>();
    public final ObservableField<String> showDescriptionError = new ObservableField<>();
    final MutableLiveData<Boolean> showUseButton = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> dataLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> dataLoadingError = new MutableLiveData<>(false);

    private String ingredientId = "";
    private IngredientResponse ingredientResponse;
    private boolean updatingUi;

    private boolean isChanged;
    private boolean isValid;

    public IngredientEditorViewModel(Resources resources,
                                     UseCaseHandler handler,
                                     TextValidator textValidator,
                                     Ingredient ingredient) {
        this.resources = resources;
        this.handler = handler;
        this.textValidator = textValidator;
        this.ingredient = ingredient;
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

            IngredientModel model = new IngredientModel.Builder().
                    getDefault().
                    build();

            executeUseCaseIngredient(model);
        }
    }

    void start(String ingredientId) {
        if (isNewInstantiation() || isIngredientIdChanged(ingredientId)) {
            navigator.setActivityTitle(R.string.activity_title_edit_ingredient);

            IngredientModel model = new IngredientModel.Builder().
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

    public String getName() {
        return ingredientResponse == null ? "" : ingredientResponse.getModel().getName();
    }

    public void setName(String name) {
        if (!updatingUi) {
            validateName(name);
        }
    }

    private void validateName(String name) {
        showNameError.set(null);
        // todo - strip out html
        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.SHORT_TEXT,
                new TextValidatorModel(name)
        );
        handler.execute(
                textValidator,
                request,
                new UseCaseCommand.Callback<TextValidatorResponse>() {
                    @Override
                    public void onSuccess(TextValidatorResponse response) {
                        processNameTextValidationResponse(response);
                    }

                    @Override
                    public void onError(TextValidatorResponse response) {
                        processNameTextValidationResponse(response);
                    }
                });
    }

    private void processNameTextValidationResponse(TextValidatorResponse response) {
        if (response.getFailReason() == TextValidator.FailReason.NONE) {

            IngredientModel model = IngredientModel.Builder.
                    basedOn(ingredientResponse.getModel()).
                    setName(response.getModel().getText()).
                    build();
            executeUseCaseIngredient(model);

        } else {
            setError(showNameError, response);
            updateUseButtonVisibility();
        }
    }

    public String getDescription() {
        return ingredientResponse == null ? "" : ingredientResponse.getModel().getDescription();
    }

    public void setDescription(String description) {
        if (!updatingUi) {
            validateDescription(description);
        }
    }

    private void validateDescription(String description) {
        showDescriptionError.set(null);

        TextValidatorRequest request = new TextValidatorRequest(
                TextValidator.TextType.LONG_TEXT,
                new TextValidatorModel(description)
        );

        handler.execute(
                textValidator,
                request,
                new UseCaseCommand.Callback<TextValidatorResponse>() {
                    @Override
                    public void onSuccess(TextValidatorResponse response) {
                        processDescriptionTextValidationResponse(response);
                    }

                    @Override
                    public void onError(TextValidatorResponse response) {
                        processDescriptionTextValidationResponse(response);
                    }
                });
    }

    private void processDescriptionTextValidationResponse(TextValidatorResponse
                                                                  longTextResponse) {
        if (longTextResponse.getFailReason() == TextValidator.FailReason.NONE) {

            IngredientModel model = IngredientModel.Builder.
                    basedOn(ingredientResponse.getModel()).
                    setDescription(longTextResponse.getModel().getText()).
                    build();

            executeUseCaseIngredient(model);
        } else {
            setError(showDescriptionError, longTextResponse);
            updateUseButtonVisibility();
        }
    }

    private void executeUseCaseIngredient(IngredientModel model) {
        dataLoading.setValue(true);
        IngredientRequest request = new IngredientRequest(model);

        handler.execute(ingredient, request, this);
    }

    @Override
    public void onSuccess(IngredientResponse response) {
        processUseCaseIngredientResponse(response);
    }

    @Override
    public void onError(IngredientResponse response) {
        processUseCaseIngredientResponse(response);
    }

    private void processUseCaseIngredientResponse(IngredientResponse response) {
        dataLoading.setValue(false);
        ingredientResponse = response;

        Ingredient.Result result = response.getResult();

        if (result == Ingredient.Result.DATA_UNAVAILABLE) {
            dataLoadingError.setValue(true);
            isValid = false;
            isChanged = false;
            return;
        } else {
            dataLoadingError.setValue(false);
        }

        if (result == Ingredient.Result.UNEDITABLE) {
            navigator.finishActivity("");

        } else if (result == Ingredient.Result.IS_DUPLICATE) {
            isValid = false;
            isChanged = false;
            showNameError.set(resources.getString(R.string.ingredient_name_duplicate_error_message));

        } else if (result == Ingredient.Result.INVALID_UNCHANGED) {
            isChanged = false;
            isValid = false;

        } else if (result == Ingredient.Result.VALID_UNCHANGED) {
            isChanged = false;
            isValid = true;

        } else if (result == Ingredient.Result.INVALID_CHANGED) {
            isChanged = true;
            isValid = false;

        } else if (result == Ingredient.Result.VALID_CHANGED) {
            isChanged = true;
            isValid = true;
        }
        updateObservables();
    }

    private void updateObservables() {
        if (isChanged) {
            updatingUi = true;
            setName(ingredientResponse.getModel().getName());
            setDescription(ingredientResponse.getModel().getName());
            updatingUi = false;
        }
        updateUseButtonVisibility();
    }

    private void setError(ObservableField<String> errorObservable,
                          TextValidatorResponse response) {

        if (response.getFailReason() == TextValidator.FailReason.TOO_SHORT) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_short,
                    response.getMinLength(),
                    response.getMaxLength()));

        } else if (response.getFailReason() == TextValidator.FailReason.TOO_LONG) {
            errorObservable.set(resources.getString(
                    R.string.input_error_text_too_long,
                    response.getMinLength(),
                    response.getMaxLength()
            ));
        }
    }

    private void updateUseButtonVisibility() {
        if (!updatingUi) {
            if (isValid && isChanged) {
                showUseButton.setValue(true);
            } else
                showUseButton.setValue(false);
        }
    }

    void useButtonPressed() {
        navigator.finishActivity(ingredientResponse.getModel().getIngredientId());
    }
}