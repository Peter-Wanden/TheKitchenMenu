package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.core.util.Pair;
import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCaseFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import javax.annotation.Nonnull;

public class RecipeDurationEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-" + RecipeDurationEditorViewModel.class.getSimpleName()
            + ":";

    private static final int MEASUREMENT_ERROR = -1;

    @Nonnull
    private final Resources resources;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private Recipe recipeMacro;

//    public final ObservableField<String> prepTimeErrorMessage = new ObservableField<>();
//    public final ObservableField<String> cookTimeErrorMessage = new ObservableField<>();

//    private final ObservableBoolean isDataLoading = new ObservableBoolean();
//    private final ObservableBoolean isDataLoadingError = new ObservableBoolean();

    private RecipeDurationResponse response;

    private boolean isUpdatingUi;

    public RecipeDurationEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull Recipe recipeMacro,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;
        this.resources = resources;

        response = new RecipeDurationResponse.Builder().getDefault().build();

        recipeMacro.registerComponentListener(new Pair<>(
                RecipeComponentNameName.DURATION,
                new DurationCallbackListener()));
    }

    private class DurationCallbackListener implements UseCaseBase.Callback<RecipeDurationResponse> {
        @Override
        public void onUseCaseSuccess(RecipeDurationResponse response) {
//            isDataLoading.set(false);
            if (isStateChanged(response)) {
                System.out.println(TAG + "onSuccess:" + response);
                RecipeDurationEditorViewModel.this.response = response;
                updateObservables();
            }
        }

        @Override
        public void onUseCaseError(RecipeDurationResponse response) {
//            isDataLoading.set(false);
            if (isStateChanged(response)) {
                RecipeDurationEditorViewModel.this.response = response;
                displayErrors();
            }
        }
    }

    private boolean isStateChanged(RecipeDurationResponse response) {
        return !this.response.equals(response);
    }

    private void updateObservables() {
        isUpdatingUi = true;
//        notifyPropertyChanged(BR.prepHoursInView);
//        notifyPropertyChanged(BR.prepMinutesInView);
//        notifyPropertyChanged(BR.cookHoursInView);
//        notifyPropertyChanged(BR.cookMinutesInView);
        isUpdatingUi = false;
    }

    private void displayErrors() {
//        prepTimeErrorMessage.set(isInvalidPrepTime() ? getPrepTimeErrorMessage() : null);
//        cookTimeErrorMessage.set(isInvalidCookTime() ? getCookTimeErrorMessage() : null);
    }

    private boolean isInvalidPrepTime() {
        return response.getMetadata().
                getFailReasons().
                contains(RecipeDurationUseCaseFailReason.INVALID_PREP_TIME);
    }

    private String getPrepTimeErrorMessage() {
        return resources.getString(R.string.input_error_recipe_prep_time_too_long);
    }

    private boolean isInvalidCookTime() {
        return response.getMetadata().
                getFailReasons().
                contains(RecipeDurationUseCaseFailReason.INVALID_COOK_TIME);
    }

    private String getCookTimeErrorMessage() {
        return resources.getString(R.string.input_error_recipe_cook_time_too_long);
    }

//    @Bindable
    public String getPrepHoursInView() {
        return response == null ? "" : String.valueOf(response.getDomainModel().getPrepHours());
    }

    public void setPrepHoursInView(String prepHoursInView) {
        if (!isUpdatingUi) {
            if (isPrepHoursInViewChanged(prepHoursInView)) {
                if (!prepHoursInView.isEmpty()) {
                    int prepHoursParsed = parseIntegerFromString(prepHoursInView);

//                    if (prepHoursParsed == MEASUREMENT_ERROR)
////                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
//                    else {
//                        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.DomainModel.
//                                Builder().
//                                basedOnResponseModel(response.getDomainModel()).
//                                setPrepHours(prepHoursParsed).
//                                build();
//                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
//                                setDataId(response.getDataId()).
//                                setDomainId(response.getDomainId()).
//                                setDomainModel(domainModel).
//                                build();
//                        handler.executeAsync(recipeMacro, request, new DurationCallbackListener());
//                    }
                }
            }
        }
    }

    private boolean isPrepHoursInViewChanged(String prepHoursInView) {
        return !String.valueOf(response.getDomainModel().getPrepHours()).equals(prepHoursInView);
    }

//    @Bindable
    public String getPrepMinutesInView() {
        return response == null ? "" : String.valueOf(response.getDomainModel().getPrepMinutes());
    }

    public void setPrepMinutesInView(String prepMinutesInView) {
        if (!isUpdatingUi) {
            if (isPrepMinutesInViewChanged(prepMinutesInView)) {
                if (!prepMinutesInView.isEmpty()) {
                    int prepMinutesParsed = parseIntegerFromString(prepMinutesInView);

//                    if (prepMinutesParsed == MEASUREMENT_ERROR)
//                        prepTimeErrorMessage.set(numberFormatExceptionErrorMessage());
//                    else {
//                        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.DomainModel.
//                                Builder().
//                                basedOnResponseModel(response.getDomainModel()).
//                                setPrepMinutes(prepMinutesParsed).
//                                build();
//                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
//                                setDataId(response.getDataId()).
//                                setDomainId(response.getDomainId()).
//                                setDomainModel(domainModel).
//                                build();
//                        handler.executeAsync(recipeMacro, request, new DurationCallbackListener());
//                    }
                }
            }
        }
    }

    private boolean isPrepMinutesInViewChanged(String prepMinutesInView) {
        return !String.valueOf(response.getDomainModel().getPrepMinutes()).equals(prepMinutesInView);
    }

//    @Bindable
    public String getCookHoursInView() {
        return response == null ? "" : String.valueOf(response.getDomainModel().getCookHours());
    }

    public void setCookHoursInView(String cookHoursInView) {
        if (!isUpdatingUi) {
            if (isCookHoursInViewChanged(cookHoursInView)) {
                if (!cookHoursInView.isEmpty()) {
                    int cookHoursParsed = parseIntegerFromString(cookHoursInView);

//                    if (cookHoursParsed == MEASUREMENT_ERROR)
//                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
//                    else {
//                        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.DomainModel.
//                                Builder().
//                                basedOnResponseModel(response.getDomainModel()).
//                                setCookHours(cookHoursParsed).
//                                build();
//                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
//                                setDataId(response.getDataId()).
//                                setDomainId(response.getDomainId()).
//                                setDomainModel(domainModel).
//                                build();
//                        handler.executeAsync(recipeMacro, request, new DurationCallbackListener());
//                    }
                }
            }
        }
    }

    private boolean isCookHoursInViewChanged(String cookHoursInView) {
        return !String.valueOf(response.getDomainModel().getCookHours()).equals(cookHoursInView);
    }

//    @Bindable
    public String getCookMinutesInView() {
        return response == null ? "" : String.valueOf(response.getDomainModel().getCookMinutes());
    }

    public void setCookMinutesInView(String cookMinutesInView) {
        if (!isUpdatingUi) {
            if (isCookMinutesInViewChanged(cookMinutesInView)) {
                if (!cookMinutesInView.isEmpty()) {
                    int cookMinutesParsed = parseIntegerFromString(cookMinutesInView);

//                    if (cookMinutesParsed == MEASUREMENT_ERROR)
//                        cookTimeErrorMessage.set(numberFormatExceptionErrorMessage());
//                    else {
//                        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.DomainModel.
//                                Builder().
//                                basedOnResponseModel(response.getDomainModel()).
//                                setCookMinutes(cookMinutesParsed).
//                                build();
//                        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
//                                setDataId(response.getDataId()).
//                                setDomainId(response.getDomainId()).
//                                setDomainModel(domainModel).
//                                build();
//                        handler.executeAsync(recipeMacro, request, new DurationCallbackListener());
//                    }
                }
            }
        }
    }

    private boolean isCookMinutesInViewChanged(String cookMinutesInView) {
        return !String.valueOf(response.getDomainModel().getCookMinutes()).equals(cookMinutesInView);
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
