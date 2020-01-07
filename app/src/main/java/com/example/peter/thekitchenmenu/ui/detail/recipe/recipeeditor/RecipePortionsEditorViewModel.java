package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortions.DO_NOT_CLONE;

public class RecipePortionsEditorViewModel
        extends
        ObservableViewModel
        implements
        RecipeModelObserver.RecipeModelActions,
        UseCaseCommand.Callback<RecipePortionsResponse> {

    private static final String TAG = "tkm-" + RecipePortionsEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private RecipePortions useCase;
    @Nonnull
    private Resources resources;
    private RecipePortionsResponse useCaseResponse;

    private boolean dataLoadingError;

    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> servingsErrorMessage = new ObservableField<>();
    public final ObservableField<String> sittingsErrorMessage = new ObservableField<>();

    private boolean dataLoading;
    private boolean updatingUi;

    public RecipePortionsEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull RecipePortions useCase,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.useCase = useCase;
        this.resources = resources;
        useCaseResponse = RecipePortionsResponse.Builder.
                getDefault().
                build();
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission
                                             modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            executeUseCase(
                    recipeId,
                    DO_NOT_CLONE,
                    new RecipePortionsModel.Builder().
                            getDefault().
                            build());
        }
    }

    @Override
    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            executeUseCase(
                    cloneFromRecipeId,
                    cloneToRecipeId,
                    new RecipePortionsModel.Builder().
                            getDefault().
                            build());
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return !useCaseResponse.getModel().getId().equals(recipeId);
    }

    private void executeUseCase(String recipeId,
                                String cloneToRecipeId,
                                RecipePortionsModel model) {
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();

        dataLoading = true;
        handler.execute(useCase, request, this);
    }

    @Override
    public void onSuccess(RecipePortionsResponse response) {
        processUseCaseResponse(response);
    }

    @Override
    public void onError(RecipePortionsResponse response) {
        processUseCaseResponse(response);
    }

    private void processUseCaseResponse(RecipePortionsResponse response) {
        useCaseResponse = response;
        dataLoading = false;
        setPortionsErrorMessage(false);
        if (response.getResult() == RecipePortions.Result.DATA_UNAVAILABLE) {
            dataLoadingError = true;
            updateRecipeComponentStatus(false, false);
            return;
        } else if (response.getResult() == RecipePortions.Result.INVALID_UNCHANGED) {
            setPortionsErrorMessage(true);
            updateRecipeComponentStatus(false, false);
        } else if (response.getResult() == RecipePortions.Result.VALID_UNCHANGED) {
            updateRecipeComponentStatus(true, false);
        } else if (response.getResult() == RecipePortions.Result.INVALID_CHANGED) {
            setPortionsErrorMessage(true);
            updateRecipeComponentStatus(false, true);
        } else if (response.getResult() == RecipePortions.Result.VALID_CHANGED) {
            updateRecipeComponentStatus(true, true);
        }
        updateObservables();
    }

    private void updateObservables() {
        updatingUi = true;
        notifyPropertyChanged(BR.servingsInView);
        notifyPropertyChanged(BR.sittingsInView);
        notifyPropertyChanged(BR.portionsInView);
        updatingUi = false;
    }

    @Bindable
    public String getServingsInView() {
        return useCaseResponse == null ? "" : String.valueOf(useCaseResponse.getModel().
                getServings());
    }

    public void setServingsInView(String servingsInView) {
        if (isServingsInViewChanged(servingsInView)) {
            if (!servingsInView.isEmpty()) {
                int servingsParsed = parseIntegerFromString(servingsInView);

                if (servingsParsed == MEASUREMENT_ERROR)
                    servingsErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    RecipePortionsModel model = RecipePortionsModel.Builder.basedOn(
                            useCaseResponse.getModel()).
                            setServings(servingsParsed).
                            build();
                    executeUseCase(useCaseResponse.getModel().getRecipeId(), DO_NOT_CLONE, model);
                }
            }
        }
    }

    private boolean isServingsInViewChanged(String servingsInView) {
        return !String.valueOf(useCaseResponse.getModel().getServings()).equals(servingsInView);
    }

    private void setPortionsErrorMessage(boolean showErrorMessage) {
        if (showErrorMessage) {
            int minServings = resources.getInteger(R.integer.recipe_min_servings);
            int maxServings = resources.getInteger(R.integer.recipe_max_servings);
            String errorMessage = resources.getString(R.string.input_error_recipe_servings,
                    minServings, maxServings);
            servingsErrorMessage.set(errorMessage);
        } else {
            servingsErrorMessage.set(null);
        }
    }

    @Bindable
    public String getSittingsInView() {
        if (useCaseResponse != null) {
            return String.valueOf(useCaseResponse.getModel().getSittings());
        } else {
            return "";
        }
    }

    public void setSittingsInView(String sittingsInView) {
        if (isSittingsInViewChanged(sittingsInView)) {
            if (!sittingsInView.isEmpty()) {
                int sittingsParsed = parseIntegerFromString(sittingsInView);

                if (sittingsParsed == MEASUREMENT_ERROR)
                    sittingsErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    RecipePortionsModel model = RecipePortionsModel.Builder.basedOn(
                            useCaseResponse.getModel()).
                            setSittings(sittingsParsed).
                            build();
                    executeUseCase(useCaseResponse.getModel().getRecipeId(), DO_NOT_CLONE, model);
                }
            }
        }
    }

    private boolean isSittingsInViewChanged(String sittingsInView) {
        return !String.valueOf(useCaseResponse.getModel().getSittings()).equals(sittingsInView);
    }

    @Bindable
    public String getPortionsInView() {
        return String.valueOf(
                useCaseResponse.getModel().getServings() *
                        useCaseResponse.getModel().getSittings());
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

    private void updateRecipeComponentStatus(boolean isValid, boolean isChanged) {
        if (!updatingUi) {
            modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStateModel(
                    RecipeValidator.ComponentName.PORTIONS,
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
}
