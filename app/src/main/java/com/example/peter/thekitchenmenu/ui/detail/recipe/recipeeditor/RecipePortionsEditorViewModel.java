package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.UseCaseCommand;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.UseCaseRecipePortions;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeportions.UseCaseRecipePortions.DO_NOT_CLONE;

public class RecipePortionsEditorViewModel
        extends ObservableViewModel
        implements RecipeModelObserver.RecipeModelActions {

    private static final String TAG = "tkm-" + RecipePortionsEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private UseCaseRecipePortions useCase;
    @Nonnull
    private Resources resources;
    private UseCaseRecipePortions.Response useCaseResponse;

    private boolean dataLoadingError;

    private RecipeValidation.RecipeValidatorModelSubmission modelSubmitter;

    public final ObservableField<String> servingsErrorMessage = new ObservableField<>();
    public final ObservableField<String> sittingsErrorMessage = new ObservableField<>();

    private boolean dataLoading;
    private boolean updatingUi;

    public RecipePortionsEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull UseCaseRecipePortions useCase,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.useCase = useCase;
        this.resources = resources;
        useCaseResponse = UseCaseRecipePortions.Response.Builder.
                getDefault().
                build();
    }

    void setModelValidationSubmitter(RecipeValidation.RecipeValidatorModelSubmission
                                             modelSubmitter) {
        this.modelSubmitter = modelSubmitter;
    }

    @Override
    public void start(String recipeId) {
        executeUseCase(
                recipeId,
                DO_NOT_CLONE,
                UseCaseRecipePortions.Model.Builder.
                        getDefault().
                        build());
    }

    @Override
    public void startByCloningModel(String oldRecipeId, String cloneToRecipeId) {
        executeUseCase(
                oldRecipeId,
                cloneToRecipeId,
                UseCaseRecipePortions.Model.Builder.
                        getDefault().
                        build());
    }

    private void executeUseCase(String recipeId,
                                String cloneToRecipeId,
                                UseCaseRecipePortions.Model model) {
        UseCaseRecipePortions.Request request = new UseCaseRecipePortions.Request.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();

        dataLoading = true;
        handler.execute(useCase, request, getCallback());
    }

    private UseCaseInteractor.Callback<UseCaseRecipePortions.Response> getCallback() {
        return new UseCaseCommand.Callback<UseCaseRecipePortions.Response>() {
            @Override
            public void onSuccess(UseCaseRecipePortions.Response response) {
                processUseCaseResponse(response);
            }

            @Override
            public void onError(UseCaseRecipePortions.Response response) {
                processUseCaseResponse(response);
            }
        };
    }

    private void processUseCaseResponse(UseCaseRecipePortions.Response response) {
        useCaseResponse = response;
        dataLoading = false;
        setPortionsErrorMessage(false);
        if (response.getResult() == UseCaseRecipePortions.Result.DATA_UNAVAILABLE) {
            dataLoadingError = true;
            updateRecipeComponentStatus(false, false);
            return;
        } else if (response.getResult() == UseCaseRecipePortions.Result.INVALID_UNCHANGED) {
            setPortionsErrorMessage(true);
            updateRecipeComponentStatus(false, false);
        } else if (response.getResult() == UseCaseRecipePortions.Result.VALID_UNCHANGED) {
            updateRecipeComponentStatus(true, false);
        } else if (response.getResult() == UseCaseRecipePortions.Result.INVALID_CHANGED) {
            setPortionsErrorMessage(true);
            updateRecipeComponentStatus(false, true);
        } else if (response.getResult() == UseCaseRecipePortions.Result.VALID_CHANGED) {
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
                    UseCaseRecipePortions.Model model = UseCaseRecipePortions.Model.Builder.basedOn(
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
                    UseCaseRecipePortions.Model model = UseCaseRecipePortions.Model.Builder.basedOn(
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
            modelSubmitter.submitRecipeComponentStatus(new RecipeComponentStatus(
                    RecipeValidator.ModelName.PORTIONS_MODEL,
                    isChanged,
                    isValid
            ));
        }
    }
}
