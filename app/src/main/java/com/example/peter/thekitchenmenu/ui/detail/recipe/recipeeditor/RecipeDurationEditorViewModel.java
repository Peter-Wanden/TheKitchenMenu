package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration.DO_NOT_CLONE;

public class RecipeDurationEditorViewModel
        extends
        ObservableViewModel
        implements
        RecipeModelObserver.RecipeModelActions, UseCaseCommand.Callback<RecipeDurationResponse> {

    private static final String TAG = "tkm-" + RecipeDurationEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private RecipeDuration useCase;
    private RecipeDurationResponse useCaseResponse;

    private Resources resources;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private String recipeId;

    private boolean dataLoading;
    private boolean dataLoadingError;
    private boolean updatingUi;

    public RecipeDurationEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull RecipeDuration useCase,
                                         Resources resources) {
        this.handler = handler;
        this.useCase = useCase;
        this.resources = resources;
        useCaseResponse = RecipeDurationResponse.Builder.
                getDefault().
                build();
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission
                                             modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;

            executeUseCase(
                    recipeId,
                    DO_NOT_CLONE,
                    RecipeDurationModel.Builder.
                            getDefault().
                            build());
        }
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String cloneToRecipeId) {
        if (isRecipeIdChanged(cloneToRecipeId)) {
            this.recipeId = cloneToRecipeId;

            executeUseCase(
                    oldRecipeId,
                    cloneToRecipeId,
                    RecipeDurationModel.Builder.
                            getDefault().
                            build());
        }
    }

    private boolean isRecipeIdChanged(String recipeId) {
        return this.recipeId == null || !this.recipeId.equals(recipeId);
    }

    @Override
    public void onSuccess(RecipeDurationResponse response) {
        processUseCaseResponse(response);
    }

    @Override
    public void onError(RecipeDurationResponse response) {
        processUseCaseResponse(response);
    }

    private void processUseCaseResponse(RecipeDurationResponse response) {
        useCaseResponse = response;
        dataLoading = false;
        RecipeState.ComponentState state = response.getState();

        if (state == RecipeState.ComponentState.DATA_UNAVAILABLE) {
            dataLoadingError = true;
            updateRecipeComponentStatus(false, false);
            return;
        } else if (state == RecipeState.ComponentState.INVALID_UNCHANGED) {
            updateRecipeComponentStatus(false, false);
        } else if (state == RecipeState.ComponentState.VALID_UNCHANGED) {
            updateRecipeComponentStatus(true, false);
        } else if (state == RecipeState.ComponentState.INVALID_CHANGED) {
            updateRecipeComponentStatus(false, true);
        } else if (state == RecipeState.ComponentState.VALID_CHANGED) {
            updateRecipeComponentStatus(true, true);
        }
        updateObservables();
    }

    private void updateRecipeComponentStatus(boolean isValid, boolean isChanged) {
        if (!updatingUi) {
            RecipeComponentStateModel model = new RecipeComponentStateModel(
                    RecipeState.ComponentName.DURATION,
                    getStatus(isChanged, isValid)
            );
            modelSubmitter.submitRecipeComponentStatus(model);
        }
    }

    private RecipeState.ComponentState getStatus(boolean isChanged, boolean isValid) {
        if (!isValid && !isChanged) {
            return RecipeState.ComponentState.INVALID_UNCHANGED;

        } else if (isValid && !isChanged) {
            return RecipeState.ComponentState.VALID_UNCHANGED;

        } else if (!isValid && isChanged) {
            return RecipeState.ComponentState.INVALID_CHANGED;

        } else {
            return RecipeState.ComponentState.VALID_CHANGED;
        }
    }

    private void updateObservables() {
        updatingUi = true;
        notifyPropertyChanged(BR.prepHoursInView);
        notifyPropertyChanged(BR.prepMinutesInView);
        notifyPropertyChanged(BR.cookHoursInView);
        notifyPropertyChanged(BR.cookMinutesInView);
        updatingUi = false;

        if (hasFailReasons()) {
            displayErrors();
        }
    }

    private boolean hasFailReasons() {
        return !useCaseResponse.getFailReasons().contains(FailReason.NONE);
    }

    private void displayErrors() {
        prepTimeErrorMessage.set(isInvalidPrepTime() ? getPrepTimeErrorMessage() : null);
        cookTimeErrorMessage.set(isInvalidCookTime() ? getCookTimeErrorMessage() : null);
    }

    private boolean isInvalidPrepTime() {
        return useCaseResponse.getFailReasons().contains(FailReason.INVALID_PREP_TIME);
    }

    private String getPrepTimeErrorMessage() {
        return resources.getString(R.string.input_error_recipe_prep_time_too_long);
    }

    private boolean isInvalidCookTime() {
        return useCaseResponse.getFailReasons().contains(FailReason.INVALID_COOK_TIME);
    }

    private String getCookTimeErrorMessage() {
        return resources.getString(R.string.input_error_recipe_cook_time_too_long);
    }

    @Bindable
    public String getPrepHoursInView() {
        return String.valueOf(useCaseResponse.getModel().getPrepHours());
    }

    public void setPrepHoursInView(String prepHoursInView) {
        if (!updatingUi) {
            if (isPrepHoursInViewChanged(prepHoursInView)) {
                if (!prepHoursInView.isEmpty()) {
                    int prepHoursParsed = parseIntegerFromString(prepHoursInView);

                    if (prepHoursParsed == MEASUREMENT_ERROR)
                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationModel model = RecipeDurationModel.Builder.
                                basedOn(useCaseResponse.getModel()).
                                setPrepHours(prepHoursParsed).
                                build();
                        executeUseCase(recipeId, DO_NOT_CLONE, model);
                    }
                }
            }
        }
    }

    private boolean isPrepHoursInViewChanged(String prepHoursInView) {
        return !String.valueOf(useCaseResponse.getModel().getPrepHours()).equals(prepHoursInView);
    }

    @Bindable
    public String getPrepMinutesInView() {
        return String.valueOf(useCaseResponse.getModel().getPrepMinutes());
    }

    public void setPrepMinutesInView(String prepMinutesInView) {
        if (!updatingUi) {
            if (isPrepMinutesInViewChanged(prepMinutesInView)) {
                if (!prepMinutesInView.isEmpty()) {
                    int prepMinutesParsed = parseIntegerFromString(prepMinutesInView);

                    if (prepMinutesParsed == MEASUREMENT_ERROR)
                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationModel model = RecipeDurationModel.Builder.
                                basedOn(useCaseResponse.getModel()).
                                setPrepMinutes(prepMinutesParsed).
                                build();
                        executeUseCase(recipeId, DO_NOT_CLONE, model);
                    }
                }
            }
        }
    }

    private boolean isPrepMinutesInViewChanged(String prepMinutesInView) {
        return !String.valueOf(useCaseResponse.getModel().getPrepMinutes()).equals(prepMinutesInView);
    }

    @Bindable
    public String getCookHoursInView() {
        return String.valueOf(useCaseResponse.getModel().getCookHours());
    }

    public void setCookHoursInView(String cookHoursInView) {
        if (!updatingUi) {
            if (isCookHoursInViewChanged(cookHoursInView)) {
                if (!cookHoursInView.isEmpty()) {
                    int cookHoursParsed = parseIntegerFromString(cookHoursInView);

                    if (cookHoursParsed == MEASUREMENT_ERROR)
                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationModel model = RecipeDurationModel.Builder.
                                basedOn(useCaseResponse.getModel()).
                                setCookHours(cookHoursParsed).
                                build();
                        executeUseCase(recipeId, DO_NOT_CLONE, model);
                    }
                }
            }
        }
    }

    private boolean isCookHoursInViewChanged(String cookHoursInView) {
        return !String.valueOf(useCaseResponse.getModel().getCookHours()).equals(cookHoursInView);
    }

    @Bindable
    public String getCookMinutesInView() {
        return String.valueOf(useCaseResponse.getModel().getCookMinutes());
    }

    public void setCookMinutesInView(String cookMinutesInView) {
        if (!updatingUi) {
            if (isCookMinutesInViewChanged(cookMinutesInView)) {
                if (!cookMinutesInView.isEmpty()) {
                    int cookMinutesParsed = parseIntegerFromString(cookMinutesInView);

                    if (cookMinutesParsed == MEASUREMENT_ERROR)
                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationModel model = RecipeDurationModel.Builder.
                                basedOn(useCaseResponse.getModel()).
                                setCookMinutes(cookMinutesParsed).
                                build();
                        executeUseCase(recipeId, DO_NOT_CLONE, model);
                    }
                }
            }
        }
    }

    private boolean isCookMinutesInViewChanged(String cookMinutesInView) {
        return !String.valueOf(useCaseResponse.getModel().getCookMinutes()).equals(cookMinutesInView);
    }

    private void executeUseCase(String recipeId,
                                String cloneToRecipeId,
                                RecipeDurationModel model) {
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();
        dataLoading = true;
        handler.execute(useCase, request, this);
    }

    private int parseIntegerFromString(String integerToParse) {
        try {
            return Integer.parseInt(integerToParse);
        } catch (NumberFormatException e) {
            return MEASUREMENT_ERROR;
        }
    }

    private String numberFormatExceptionErrorMessage() {
        return resources.getString(R.string.number_format_exception);
    }
}
