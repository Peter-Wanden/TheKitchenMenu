package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity.RecipeIdentityUseCaseRequestModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIdentityEditorViewModel
        extends
        ObservableViewModel {

    private static final String TAG = "tkm-" + RecipeIdentityEditorViewModel.class.getSimpleName() +
            ": ";

    @Nonnull
    private final Resources resources;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private Recipe recipe;

//    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
//    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
//    public final ObservableBoolean isDataLoadingError = new ObservableBoolean();
//    private final ObservableBoolean isDataLoading = new ObservableBoolean();

    private RecipeIdentityResponse response;

    private boolean isUpdatingUi;

    public RecipeIdentityEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull Recipe recipe,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipe = recipe;
        this.resources = resources;

        response = new RecipeIdentityResponse.Builder().getDefault().build();

        recipe.registerComponentListener(new Pair<>(
                RecipeMetadata.ComponentName.IDENTITY,
                new IdentityCallbackListener())
        );
    }

    /**
     * Registered recipe component callback listening for updates pushed from
     * {@link Recipe}
     */
    private class IdentityCallbackListener implements UseCaseBase.Callback<RecipeIdentityResponse> {
        @Override
        public void onUseCaseSuccess(RecipeIdentityResponse response) {
//            isDataLoading.set(false);
            if (isStateChanged(response)) {
                System.out.println(TAG + "onSuccess:" + response);
                RecipeIdentityEditorViewModel.this.response = response;
                processUseCaseSuccessResponse();
            }
        }

        @Override
        public void onUseCaseError(RecipeIdentityResponse response) {
//            isDataLoading.set(false);
            if (isStateChanged(response)) {
                RecipeIdentityEditorViewModel.this.response = response;
                processUseCaseErrorResponse();
            }
        }
    }

//    @Bindable
    public String getTitle() {
        return response == null ? "" : response.getDomainModel().getTitle();
    }

    public void setTitle(String title) {
        if (isTitleChanged(title)) {
            RecipeIdentityUseCaseRequestModel model = new RecipeIdentityUseCaseRequestModel.Builder().
                    basedOnResponseModel(response.getDomainModel()).
                    setTitle(title).
                    build();
            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setDataId(response.getDataId()).
                    setDomainId(response.getDomainId()).
                    setDomainModel(model).
                    build();
            handler.executeAsync(recipe, request, new IdentityCallbackListener());
        }
    }

    private boolean isTitleChanged(String title) {
        return !isUpdatingUi && !response.getDomainModel().getTitle().equals(title.trim());
    }

//    @Bindable
    public String getDescription() {
        return response == null ? "" : response.getDomainModel().getDescription();
    }

    public void setDescription(String description) {
        if (isDescriptionChanged(description)) {
            RecipeIdentityUseCaseRequestModel model = new RecipeIdentityUseCaseRequestModel.Builder().
                    basedOnResponseModel(response.getDomainModel()).
                    setDescription(description).
                    build();
            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setDataId(response.getDataId()).
                    setDomainId(response.getDomainId()).
                    setDomainModel(model).
                    build();

            System.out.println(TAG + response);

            handler.executeAsync(recipe, request, new IdentityCallbackListener());
        }
    }

    private boolean isDescriptionChanged(String description) {
        return !isUpdatingUi && !response.getDomainModel().getDescription().equals(description.trim());
    }

    private boolean isStateChanged(RecipeIdentityResponse response) {
        return !this.response.equals(response);
    }

    private void processUseCaseSuccessResponse() {
        clearErrors();
        updateObservables();
    }

    private void clearErrors() {
//        titleErrorMessage.set(null);
//        descriptionErrorMessage.set(null);
    }

    private void updateObservables() {
        isUpdatingUi = true;
//        notifyPropertyChanged(BR.title);
//        notifyPropertyChanged(BR.description);
        isUpdatingUi = false;
    }

    private void processUseCaseErrorResponse() {
        System.out.println(TAG + "onUseCaseError: failReasons=" +
                response.getMetadata().getFailReasons());

        List<FailReasons> failReasons = response.getMetadata().getFailReasons();
//        if (failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
//            isDataLoadingError.set(true);
//        }
//        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT)) {
//            System.out.println(TAG + "Setting title too short error.");
//            titleErrorMessage.set(
//                    resources.getString(R.string.input_error_text_too_short,
//                            String.valueOf(resources.getInteger(
//                                    R.integer.input_validation_short_text_min_length)),
//                            String.valueOf(resources.getInteger(
//                                    R.integer.input_validation_short_text_max_length))));
//        }
//        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG)) {
//            titleErrorMessage.set(
//                    resources.getString(R.string.input_error_text_too_long,
//                            String.valueOf(resources.getInteger(
//                                    R.integer.input_validation_short_text_min_length)),
//                            String.valueOf(resources.getInteger(
//                                    R.integer.input_validation_short_text_max_length))));
//        }
//        if (failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG)) {
//            descriptionErrorMessage.set(
//                    resources.getString(R.string.input_error_text_too_long,
//                            String.valueOf(resources.getInteger(
//                                    R.integer.input_validation_long_text_min_length)),
//                            String.valueOf(resources.getInteger(
//                                    R.integer.input_validation_long_text_max_length))));
//        }
    }
}