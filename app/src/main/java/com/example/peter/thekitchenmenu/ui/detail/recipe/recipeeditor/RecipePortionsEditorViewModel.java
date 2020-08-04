package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.core.util.Pair;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCaseFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipePortionsEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-" + RecipePortionsEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private Resources resources;
    @Nonnull
    private UseCaseHandler handler;
    @Nonnull
    private Recipe recipeMacro;
    private RecipePortionsResponse response;

//    public final ObservableField<String> servingsErrorMessage = new ObservableField<>();
//    public final ObservableField<String> sittingsErrorMessage = new ObservableField<>();
//
//    private final ObservableBoolean isDataLoading = new ObservableBoolean();
//    private final ObservableBoolean dataLoadingError = new ObservableBoolean();
    private boolean isUpdatingUi;
    private PortionsCallbackListener callback;

    public RecipePortionsEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull Recipe recipeMacro,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;
        this.resources = resources;

        response = new RecipePortionsResponse.Builder().getDefault().build();

        callback = new PortionsCallbackListener();
        recipeMacro.registerComponentListener(new Pair<>(
                RecipeComponentName.PORTIONS,
                callback)
        );
    }

    /**
     * Registered recipe component callback listening for updates pushed from
     * {@link Recipe}
     */
    private class PortionsCallbackListener implements UseCaseBase.Callback<RecipePortionsResponse> {
        @Override
        public void onUseCaseSuccess(RecipePortionsResponse response) {
//            isDataLoading.set(false);
            if (isStateChanged(response)) {
                System.out.println(TAG + "onSuccess:" + response);
                RecipePortionsEditorViewModel.this.response = response;
                onUseCaseSuccess(response);
            }
        }

        @Override
        public void onUseCaseError(RecipePortionsResponse response) {
//            isDataLoading.set(false);
            if (isStateChanged(response)) {
                System.out.println(TAG + "onError:" + response);
                RecipePortionsEditorViewModel.this.response = response;
                this.onUseCaseError(response);
            }
        }
    }

    private boolean isStateChanged(RecipePortionsResponse response) {
        return !response.equals(this.response);
    }

    private void onUseCaseSuccess() {
        clearErrors();
        updateObservables();
    }

    private void clearErrors() {
//        sittingsErrorMessage.set(null);
//        servingsErrorMessage.set(null);
//        dataLoadingError.set(false);
    }

    private void onUseCaseError(RecipePortionsResponse response) {
        List<FailReasons> failReasons = response.getMetadata().getFailReasons();

        if (failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
//            dataLoadingError.set(true);
            return;
        }
        if (failReasons.contains(RecipePortionsUseCaseFailReason.SERVINGS_TOO_LOW) ||
                failReasons.contains(RecipePortionsUseCaseFailReason.SERVINGS_TOO_HIGH)) {
            showServingsErrorMessage();
        }
        if (failReasons.contains(RecipePortionsUseCaseFailReason.SITTINGS_TOO_LOW) ||
                failReasons.contains(RecipePortionsUseCaseFailReason.SITTINGS_TOO_HIGH)) {
            showSittingsErrorMessage();
        }
        updateObservables();
    }

    private void updateObservables() {
        isUpdatingUi = true;
//        notifyPropertyChanged(BR.servingsInView);
//        notifyPropertyChanged(BR.sittingsInView);
//        notifyPropertyChanged(BR.portionsInView);
        isUpdatingUi = false;
    }

//    @Bindable
    public String getServingsInView() {
        return response == null ? "" : String.valueOf(response.getDomainModel().getServings());
    }

    public void setServingsInView(String servingsInView) {
        if (isServingsInViewChanged(servingsInView)) {
            if (!servingsInView.isEmpty()) {
                int servingsParsed = parseIntegerFromString(servingsInView);

//                if (servingsParsed == MEASUREMENT_ERROR) {
//                    servingsErrorMessage.set(numberFormatExceptionErrorMessage());
//                } else {
//                    RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
//                            basedResponseModel(response.getDomainModel()).
//                            setServings(servingsParsed).
//                            build();
//                    RecipePortionsRequest request = new RecipePortionsRequest.Builder().
//                            setDataId(response.getDataId()).
//                            setDomainId(response.getDomainId()).
//                            setDomainModel(model).
//                            build();
//                    handler.executeAsync(recipeMacro, request, callback);
//                }
            }
        }
    }

    private boolean isServingsInViewChanged(String servingsInView) {
        return !isUpdatingUi && !String.valueOf(response.getDomainModel().getServings()).
                equals(servingsInView);
    }

    private void showServingsErrorMessage() {
        int minServings = resources.getInteger(R.integer.recipe_min_servings);
        int maxServings = resources.getInteger(R.integer.recipe_max_servings);
        String errorMessage = resources.getString(R.string.input_error_recipe_servings,
                minServings, maxServings);
//        servingsErrorMessage.set(errorMessage);
    }

    private void showSittingsErrorMessage() {
        int minSittings = resources.getInteger(R.integer.recipe_min_sittings);
        int maxSittings = resources.getInteger(R.integer.recipe_max_sittings);
        String errorMessage = resources.getString(R.string.input_error_recipe_sittings,
                minSittings, maxSittings);
//        sittingsErrorMessage.set(errorMessage);
    }

//    @Bindable
    public String getSittingsInView() {
        return String.valueOf(response.getDomainModel().getSittings());
    }

    public void setSittingsInView(String sittingsInView) {
        if (isSittingsInViewChanged(sittingsInView)) {
            if (!sittingsInView.isEmpty()) {
                int sittingsParsed = parseIntegerFromString(sittingsInView);

//                if (sittingsParsed == MEASUREMENT_ERROR)
//                    sittingsErrorMessage.set(numberFormatExceptionErrorMessage());
//                else {
//                    RecipePortionsRequest.DomainModel model = new RecipePortionsRequest.DomainModel.Builder().
//                            basedResponseModel(response.getDomainModel()).
//                            setSittings(sittingsParsed).
//                            build();
//                    RecipePortionsRequest request = new RecipePortionsRequest.Builder().
//                            setDataId(response.getDataId()).
//                            setDomainId(response.getDomainId()).
//                            setDomainModel(model).
//                            build();
//                    handler.executeAsync(recipeMacro, request, callback);
//                }
            }
        }
    }

    private boolean isSittingsInViewChanged(String sittingsInView) {
        return !isUpdatingUi && !String.valueOf(response.getDomainModel().getSittings()).
                equals(sittingsInView);
    }

//    @Bindable
    public String getPortionsInView() {
        return String.valueOf(response.getDomainModel().getPortions());
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
