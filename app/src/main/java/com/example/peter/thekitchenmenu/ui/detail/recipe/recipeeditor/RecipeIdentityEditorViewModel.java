package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.core.util.Pair;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;

public class RecipeIdentityEditorViewModel extends ObservableViewModel {

    private static final String TAG = "tkm-" + RecipeIdentityEditorViewModel.class.getSimpleName() +
            ": ";

    @Nonnull
    private final Resources resources;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private RecipeMacro recipeMacro;

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableBoolean dataLoadingError = new ObservableBoolean();
    private final ObservableBoolean dataLoading = new ObservableBoolean();

    private RecipeIdentityResponse response;

    private boolean updatingUi;

    public RecipeIdentityEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull RecipeMacro recipeMacro,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipeMacro = recipeMacro;
        this.resources = resources;

        response = RecipeIdentityResponse.Builder.getDefault().build();

        recipeMacro.registerComponentCallback(new Pair<>(ComponentName.IDENTITY,
                new IdentityCallbackListener())
        );
    }

    /**
     * Registered recipe component callback listening for updates pushed from
     * {@link RecipeMacro}
     */
    private class IdentityCallbackListener implements UseCase.Callback<RecipeIdentityResponse> {
        @Override
        public void onSuccess(RecipeIdentityResponse response) {
            System.out.println(TAG + "onSuccess:" + response);
            RecipeIdentityEditorViewModel.this.response = response;
            onUseCaseSuccess();
        }

        @Override
        public void onError(RecipeIdentityResponse response) {
            System.out.println(TAG + "onError:" + response);
            RecipeIdentityEditorViewModel.this.response = response;
            onUseCaseError();
        }
    }

    @Bindable
    public String getTitle() {
        return response.getModel().getTitle();
    }

    public void setTitle(String title) {
        if (isTitleChanged(title)) {
            RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                    basedOnIdentityResponseModel(response.getModel()).
                    setTitle(title).
                    build();
            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setId(response.getId()).
                    setCloneToId(DO_NOT_CLONE).
                    setModel(model).
                    build();
            handler.execute(recipeMacro, request, getRecipeIdentityCallback());
        }
    }

    private boolean isTitleChanged(String title) {
        return !updatingUi && !response.getModel().getTitle().equals(title.trim());
    }

    @Bindable
    public String getDescription() {
        return response.getModel().getDescription();
    }

    public void setDescription(String description) {
        if (isDescriptionChanged(description)) {
            RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                    basedOnIdentityResponseModel(response.getModel()).
                    setDescription(description).
                    build();
            RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                    setId(response.getId()).
                    setCloneToId(DO_NOT_CLONE).
                    setModel(model).
                    build();

            System.out.println(TAG + response);

            handler.execute(recipeMacro, request, getRecipeIdentityCallback());
        }
    }

    private boolean isDescriptionChanged(String description) {
        return !updatingUi && !response.getModel().getDescription().equals(description.trim());
    }

    /**
     * @return callback passed in when sending requests directly to {@link RecipeMacro}
     */
    private UseCase.Callback<RecipeIdentityResponse> getRecipeIdentityCallback() {
        return new UseCase.Callback<RecipeIdentityResponse>() {
            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                System.out.println(TAG + "onSuccess:");
                if (isStateChanged(response)) {
                    RecipeIdentityEditorViewModel.this.response = response;
                    onUseCaseSuccess();
                }
            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                System.out.println(TAG + "onError:" + response);
                if (isStateChanged(response)) {
                    RecipeIdentityEditorViewModel.this.response = response;
                    onUseCaseError();
                }
            }
        };
    }

    private boolean isStateChanged(RecipeIdentityResponse response) {
        return !this.response.equals(response);
    }

    private void onUseCaseSuccess() {
        clearErrors();
        updateObservables();
    }

    private void clearErrors() {
        titleErrorMessage.set(null);
        descriptionErrorMessage.set(null);
    }

    private void onUseCaseError() {
        System.out.println(TAG + "onUseCaseError: failReasons=" + response.getFailReasons());
        List<FailReasons> failReasons = response.getFailReasons();
        if (failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
            dataLoadingError.set(true);
        }
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT)) {
            System.out.println(TAG + "Setting title too short error.");
            titleErrorMessage.set(
                    resources.getString(R.string.input_error_text_too_short,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_max_length))));
        }
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG)) {
            titleErrorMessage.set(
                    resources.getString(R.string.input_error_text_too_long,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_short_text_max_length))));
        }
        if (failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG)) {
            descriptionErrorMessage.set(
                    resources.getString(R.string.input_error_text_too_long,
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_long_text_min_length)),
                            String.valueOf(resources.getInteger(
                                    R.integer.input_validation_long_text_max_length))));
        }
    }

    private void updateObservables() {
        updatingUi = true;
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.description);
        updatingUi = false;
    }
}