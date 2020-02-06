package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;

public class RecipeIdentityEditorViewModel
        extends ObservableViewModel
        implements UseCase.Callback<RecipeResponse> {

    private static final String TAG = "tkm-" + RecipeIdentityEditorViewModel.class.getSimpleName() +
            ": ";

    @Nonnull
    private final Resources resources;
    @Nonnull
    private final UseCaseHandler handler;
    @Nonnull
    private Recipe recipe;

    public final ObservableField<String> titleErrorMessage = new ObservableField<>();
    public final ObservableField<String> descriptionErrorMessage = new ObservableField<>();
    public final ObservableBoolean dataLoadingError = new ObservableBoolean();

    private RecipeIdentityResponse response;
    private String recipeId = "";

    private boolean updatingUi;
    private final ObservableBoolean dataLoading = new ObservableBoolean();

    public RecipeIdentityEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull Recipe recipe,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.recipe = recipe;
        this.resources = resources;

        response = RecipeIdentityResponse.Builder.getDefault().build();
    }

    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;
            dataLoading.set(true);

            RecipeIdentityRequest request = RecipeIdentityRequest.Builder.
                    getDefault().
                    setRecipeId(recipeId).
                    build();
            handler.execute(recipe, request, this);
        }
    }

    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            this.recipeId = cloneToRecipeId;
            dataLoading.set(true);

            RecipeIdentityRequest request = RecipeIdentityRequest.Builder.
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
    public void onSuccess(RecipeResponse response) {
        System.out.println(TAG + "onSuccess");
        extractResponse(response);
        onUseCaseSuccess();
    }

    @Override
    public void onError(RecipeResponse response) {
        System.out.println(TAG + "onError");
        extractResponse(response);
        onUseCaseError();
    }

    private void extractResponse(RecipeResponse recipeResponse) {
        response = (RecipeIdentityResponse) recipeResponse.
                getComponentResponses().
                get(RecipeStateCalculator.ComponentName.IDENTITY);
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
                    setRecipeId(recipeId).
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setModel(model).
                    build();
            handler.execute(recipe, request, this);
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
                    setRecipeId(recipeId).
                    setCloneToRecipeId(DO_NOT_CLONE).
                    setModel(model).
                    build();
            handler.execute(recipe, request, this);
        }
    }

    private boolean isDescriptionChanged(String description) {
        return !updatingUi && !response.getModel().getDescription().equals(description.trim());
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
        System.out.println(TAG + "onUseCaseErrorCalled");
        List<FailReasons> failReasons = response.getFailReasons();
        if (failReasons.contains(RecipeIdentity.FailReason.DATA_UNAVAILABLE)) {
            dataLoadingError.set(true);
        }
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT)) {
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