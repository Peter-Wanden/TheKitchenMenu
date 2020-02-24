package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration.*;

public class RecipeDurationEditorViewModel
        extends ObservableViewModel
        implements UseCase.Callback<RecipeMacroResponse> {

    private static final String TAG = "tkm-" + RecipeDurationEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private final Resources resources;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private RecipeMacro recipeMacro;

    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

    private RecipeDurationResponse response;
    private String recipeId = "";

    private boolean isUpdatingUi;
    private final ObservableBoolean isDataLoading = new ObservableBoolean();
    private boolean isDataLoadingError;

    public RecipeDurationEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull RecipeMacro recipeMacro,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;
        this.resources = resources;

        response = RecipeDurationResponse.Builder.getDefault().build();
    }

    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;
            isDataLoading.set(true);

            RecipeDurationRequest request = RecipeDurationRequest.Builder.
                    getDefault().
                    setId(recipeId).
                    build();
            handler.execute(recipeMacro, request, this);
        }
    }

    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            recipeId = cloneToRecipeId;

            RecipeDurationRequest request = RecipeDurationRequest.Builder.
                    getDefault().
                    setId(cloneFromRecipeId).
                    setCloneToId(cloneToRecipeId).
                    build();
            handler.execute(recipeMacro, request, this);
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return !this.recipeId.equals(recipeId);
    }

    @Override
    public void onSuccess(RecipeMacroResponse response) {
        System.out.println(TAG + "onSuccess:" + response);
        RecipeDurationResponse durationResponse = getResponse(response);
        if (isStateChanged(durationResponse)) {
            this.response = durationResponse;
            updateObservables();
        }
    }

    @Override
    public void onError(RecipeMacroResponse response) {
        System.out.println(TAG + "onError:" + response);
        RecipeDurationResponse durationResponse = getResponse(response);
        if (isStateChanged(durationResponse)) {
            this.response = durationResponse;
            displayErrors();
        }
    }

    private boolean isStateChanged(RecipeDurationResponse response) {
        return !this.response.equals(response);
    }

    private RecipeDurationResponse getResponse(RecipeMacroResponse response) {
        return (RecipeDurationResponse) response.
                getComponentResponses().
                get(RecipeStateCalculator.ComponentName.DURATION);
    }

    private void updateObservables() {
        isUpdatingUi = true;
        notifyPropertyChanged(BR.prepHoursInView);
        notifyPropertyChanged(BR.prepMinutesInView);
        notifyPropertyChanged(BR.cookHoursInView);
        notifyPropertyChanged(BR.cookMinutesInView);
        isUpdatingUi = false;

        if (hasFailReasons()) {
            displayErrors();
        }
    }

    private boolean hasFailReasons() {
        return !response.getFailReasons().contains(CommonFailReason.NONE);
    }

    private void displayErrors() {
        prepTimeErrorMessage.set(isInvalidPrepTime() ? getPrepTimeErrorMessage() : null);
        cookTimeErrorMessage.set(isInvalidCookTime() ? getCookTimeErrorMessage() : null);
    }

    private boolean isInvalidPrepTime() {
        return response.getFailReasons().contains(FailReason.INVALID_PREP_TIME);
    }

    private String getPrepTimeErrorMessage() {
        return resources.getString(R.string.input_error_recipe_prep_time_too_long);
    }

    private boolean isInvalidCookTime() {
        return response.getFailReasons().contains(FailReason.INVALID_COOK_TIME);
    }

    private String getCookTimeErrorMessage() {
        return resources.getString(R.string.input_error_recipe_cook_time_too_long);
    }

    @Bindable
    public String getPrepHoursInView() {
        return String.valueOf(response.getModel().getPrepHours());
    }

    public void setPrepHoursInView(String prepHoursInView) {
        if (!isUpdatingUi) {
            if (isPrepHoursInViewChanged(prepHoursInView)) {
                if (!prepHoursInView.isEmpty()) {
                    int prepHoursParsed = parseIntegerFromString(prepHoursInView);

                    if (prepHoursParsed == MEASUREMENT_ERROR)
                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                                basedOnDurationResponseModel(response.getModel()).
                                setPrepHours(prepHoursParsed).
                                build();
                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                                setId(recipeId).
                                setModel(model).
                                build();
                        handler.execute(recipeMacro, request, this);
                    }
                }
            }
        }
    }

    private boolean isPrepHoursInViewChanged(String prepHoursInView) {
        return !String.valueOf(response.getModel().getPrepHours()).equals(prepHoursInView);
    }

    @Bindable
    public String getPrepMinutesInView() {
        return String.valueOf(response.getModel().getPrepMinutes());
    }

    public void setPrepMinutesInView(String prepMinutesInView) {
        if (!isUpdatingUi) {
            if (isPrepMinutesInViewChanged(prepMinutesInView)) {
                if (!prepMinutesInView.isEmpty()) {
                    int prepMinutesParsed = parseIntegerFromString(prepMinutesInView);

                    if (prepMinutesParsed == MEASUREMENT_ERROR)
                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                                basedOnDurationResponseModel(response.getModel()).
                                setPrepMinutes(prepMinutesParsed).
                                build();
                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                                setId(recipeId).
                                setModel(model).
                                build();
                        handler.execute(recipeMacro, request, this);
                    }
                }
            }
        }
    }

    private boolean isPrepMinutesInViewChanged(String prepMinutesInView) {
        return !String.valueOf(response.getModel().getPrepMinutes()).equals(prepMinutesInView);
    }

    @Bindable
    public String getCookHoursInView() {
        return String.valueOf(response.getModel().getCookHours());
    }

    public void setCookHoursInView(String cookHoursInView) {
        if (!isUpdatingUi) {
            if (isCookHoursInViewChanged(cookHoursInView)) {
                if (!cookHoursInView.isEmpty()) {
                    int cookHoursParsed = parseIntegerFromString(cookHoursInView);

                    if (cookHoursParsed == MEASUREMENT_ERROR)
                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                                basedOnDurationResponseModel(response.getModel()).
                                setCookHours(cookHoursParsed).
                                build();
                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                                setId(recipeId).
                                setModel(model).
                                build();
                        handler.execute(recipeMacro, request, this);
                    }
                }
            }
        }
    }

    private boolean isCookHoursInViewChanged(String cookHoursInView) {
        return !String.valueOf(response.getModel().getCookHours()).equals(cookHoursInView);
    }

    @Bindable
    public String getCookMinutesInView() {
        return String.valueOf(response.getModel().getCookMinutes());
    }

    public void setCookMinutesInView(String cookMinutesInView) {
        if (!isUpdatingUi) {
            if (isCookMinutesInViewChanged(cookMinutesInView)) {
                if (!cookMinutesInView.isEmpty()) {
                    int cookMinutesParsed = parseIntegerFromString(cookMinutesInView);

                    if (cookMinutesParsed == MEASUREMENT_ERROR)
                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
                    else {
                        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
                                basedOnDurationResponseModel(response.getModel()).
                                setCookMinutes(cookMinutesParsed).
                                build();
                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                                setId(recipeId).
                                setModel(model).
                                build();
                        handler.execute(recipeMacro, request, this);
                    }
                }
            }
        }
    }

    private boolean isCookMinutesInViewChanged(String cookMinutesInView) {
        return !String.valueOf(response.getModel().getCookMinutes()).equals(cookMinutesInView);
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
