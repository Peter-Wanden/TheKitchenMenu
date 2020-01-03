package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.UseCaseRecipeDuration;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.UseCaseInteractor.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.UseCaseRecipeDuration.*;
import static com.example.peter.thekitchenmenu.domain.usecase.recipeduration.UseCaseRecipeDuration.DO_NOT_CLONE;

public class RecipeDurationEditorViewModel
        extends ObservableViewModel
        implements RecipeModelObserver.RecipeModelActions {

    private static final String TAG = "tkm-" + RecipeDurationEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private UseCaseRecipeDuration useCase;
    private UseCaseRecipeDuration.Response useCaseResponse;

    private Resources resources;
    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private String recipeId;

    private boolean dataLoading;
    private boolean dataLoadingError;
    private boolean updatingUi;

    public RecipeDurationEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull UseCaseRecipeDuration useCase,
                                         Resources resources) {
        this.handler = handler;
        this.useCase = useCase;
        this.resources = resources;
        useCaseResponse = UseCaseRecipeDuration.Response.Builder.
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
                    Model.Builder.
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
                    Model.Builder.
                            getDefault().
                            build());
        }
    }

    private boolean isRecipeIdChanged(String recipeId) {
        return this.recipeId == null || !this.recipeId.equals(recipeId);
    }

    private UseCaseInteractor.Callback<UseCaseRecipeDuration.Response> getUseCaseCallback() {
        return new UseCaseCommand.Callback<UseCaseRecipeDuration.Response>() {
            @Override
            public void onSuccess(UseCaseRecipeDuration.Response response) {
                processUseCaseResponse(response);
            }

            @Override
            public void onError(UseCaseRecipeDuration.Response response) {
                processUseCaseResponse(response);
            }
        };
    }

    private void processUseCaseResponse(UseCaseRecipeDuration.Response response) {
        useCaseResponse = response;
        dataLoading = false;
        Result result = response.getResult();

        if (result == Result.DATA_UNAVAILABLE) {
            dataLoadingError = true;
            updateRecipeComponentStatus(false, false);
            return;
        } else if (result == Result.INVALID_UNCHANGED) {
            updateRecipeComponentStatus(false, false);
        } else if (result == Result.VALID_UNCHANGED) {
            updateRecipeComponentStatus(true, false);
        } else if (result == Result.INVALID_CHANGED) {
            updateRecipeComponentStatus(false, true);
        } else if (result == Result.VALID_CHANGED) {
            updateRecipeComponentStatus(true, true);
        }
        updateObservables();
    }

    private void updateRecipeComponentStatus(boolean isValid, boolean isChanged) {
        if (!updatingUi) {
            modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStatus(
                    RecipeValidator.ModelName.DURATION_MODEL,
                    isChanged,
                    isValid
            ));
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
                        Model model = Model.Builder.
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
                        Model model = Model.Builder.
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
                        Model model = Model.Builder.
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
                        Model model = Model.Builder.
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
                                Model model) {
        UseCaseRecipeDuration.Request request = new UseCaseRecipeDuration.Request.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();
        dataLoading = true;
        handler.execute(useCase, request, getUseCaseCallback());
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
