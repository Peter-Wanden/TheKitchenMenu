package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipePortionsEditorViewModel
        extends ObservableViewModel
        implements UseCase.Callback<RecipePortionsResponse> {

    private static final String TAG = "tkm-" + RecipePortionsEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private Resources resources;
    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    Recipe recipe;
    private RecipePortionsResponse response;

    public final ObservableField<String> servingsErrorMessage = new ObservableField<>();
    public final ObservableField<String> sittingsErrorMessage = new ObservableField<>();

    private final ObservableBoolean isDataLoading = new ObservableBoolean();
    private boolean dataLoadingError;
    private boolean updatingUi;
    private String recipeId;

    public RecipePortionsEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull Recipe recipe,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipe = recipe;
        this.resources = resources;

        response = RecipePortionsResponse.Builder.getDefault().build();
    }


    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;
            isDataLoading.set(true);

            RecipePortionsRequest request = RecipePortionsRequest.Builder.
                    getDefault().
                    setRecipeId(recipeId).
                    build();
            handler.execute(recipe, request, this);
        }
    }


    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            recipeId = cloneToRecipeId;

            RecipePortionsRequest request = RecipePortionsRequest.Builder.
                    getDefault().
                    setRecipeId(cloneFromRecipeId).
                    setCloneToRecipeId(cloneToRecipeId).
                    build();
            handler.execute(recipe, request, this);
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return !this.recipeId.equals(recipeId);
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
        isDataLoading.set(false);
        this.response = response;

        ComponentState state = response.getState();
        setPortionsErrorMessage(false);

        if (state == ComponentState.DATA_UNAVAILABLE) {
            dataLoadingError = true;
            return;

        } else if (state == ComponentState.INVALID_UNCHANGED) {
            setPortionsErrorMessage(true);

        } else if (state == ComponentState.INVALID_CHANGED) {
            setPortionsErrorMessage(true);
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
        return response == null ? "" : String.valueOf(response.getModel().getServings());
    }

    public void setServingsInView(String servingsInView) {
        if (isServingsInViewChanged(servingsInView)) {
            if (!servingsInView.isEmpty()) {
                int servingsParsed = parseIntegerFromString(servingsInView);

                if (servingsParsed == MEASUREMENT_ERROR)
                    servingsErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    RecipePortionsRequest.Model model = RecipePortionsRequest.Model.Builder.
                            basedOnPortionsResponseModel(response.getModel()).
                            setServings(servingsParsed).
                            build();
                    RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                            setRecipeId(recipeId).
                            setModel(model).
                            build();
                    handler.execute(recipe, request, this);
                }
            }
        }
    }

    private boolean isServingsInViewChanged(String servingsInView) {
        return !String.valueOf(response.getModel().getServings()).equals(servingsInView);
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
        return String.valueOf(response.getModel().getSittings());
    }

    public void setSittingsInView(String sittingsInView) {
        if (isSittingsInViewChanged(sittingsInView)) {
            if (!sittingsInView.isEmpty()) {
                int sittingsParsed = parseIntegerFromString(sittingsInView);

                if (sittingsParsed == MEASUREMENT_ERROR)
                    sittingsErrorMessage.set(numberFormatExceptionErrorMessage());
                else {
                    RecipePortionsRequest.Model model = RecipePortionsRequest.Model.Builder.
                            basedOnPortionsResponseModel(response.getModel()).
                            setSittings(sittingsParsed).
                            build();
                    RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                            setRecipeId(recipeId).
                            setModel(model).
                            build();
                    handler.execute(recipe, request, this);
                }
            }
        }
    }

    private boolean isSittingsInViewChanged(String sittingsInView) {
        return !String.valueOf(response.getModel().getSittings()).equals(sittingsInView);
    }

    @Bindable
    public String getPortionsInView() {
        return String.valueOf(response.getModel().getPortions());
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
