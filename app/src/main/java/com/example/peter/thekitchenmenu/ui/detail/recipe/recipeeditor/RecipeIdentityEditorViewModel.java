package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

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
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacroResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.ui.ObservableViewModel;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe.DO_NOT_CLONE;

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

    private RecipeIdentityResponse response;
    private String recipeId = "";

    private boolean updatingUi;
    private final ObservableBoolean dataLoading = new ObservableBoolean();

    public RecipeIdentityEditorViewModel(@Nonnull UseCaseHandler handler,
                                         @Nonnull RecipeMacro recipeMacro,
                                         @Nonnull Resources resources) {
        this.handler = handler;
        this.resources = resources;

        response = RecipeIdentityResponse.Builder.getDefault().build();

        this.recipeMacro = recipeMacro;
//        recipeMacro.registerComponentCallback(ComponentName.IDENTITY, getRecipeIdentityCallback());
//        recipeMacro.registerMacroCallback(getRecipeResponseCallback());
    }

    private UseCase.Callback<RecipeIdentityResponse> getRecipeIdentityCallback() {
        return new UseCase.Callback<RecipeIdentityResponse>() {
            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                System.out.println(TAG + "onSuccess");
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

    private UseCase.Callback<RecipeMacroResponse> getRecipeResponseCallback() {
        return new UseCase.Callback<RecipeMacroResponse>() {
            @Override
            public void onSuccess(RecipeMacroResponse response) {
                // ToDo implement clone to etc
            }

            @Override
            public void onError(RecipeMacroResponse response) {

            }
        };
    }

    public void start(String recipeId) {
        if (isNewInstantiationOrRecipeIdChanged(recipeId)) {
            this.recipeId = recipeId;
            dataLoading.set(true);

            RecipeIdentityRequest request = RecipeIdentityRequest.Builder.
                    getDefault().
                    setId(recipeId).
                    build();
            handler.execute(recipeMacro, request, getRecipeIdentityCallback());
        }
    }

    public void startByCloningModel(String cloneFromRecipeId, String cloneToRecipeId) {
        if (isNewInstantiationOrRecipeIdChanged(cloneToRecipeId)) {
            this.recipeId = cloneToRecipeId;
            dataLoading.set(true);

            RecipeIdentityRequest request = RecipeIdentityRequest.Builder.
                    getDefault().
                    setId(cloneFromRecipeId).
                    setCloneToId(cloneToRecipeId).
                    build();
            handler.execute(recipeMacro, request, getRecipeIdentityCallback());
        }
    }

    private boolean isNewInstantiationOrRecipeIdChanged(String recipeId) {
        return !this.recipeId.equals(recipeId);
    }

    private boolean isStateChanged(RecipeIdentityResponse response) {
        return !this.response.equals(response);
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
                    setId(recipeId).
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
                    setId(recipeId).
                    setCloneToId(DO_NOT_CLONE).
                    setModel(model).
                    build();
            handler.execute(recipeMacro, request, getRecipeIdentityCallback());
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
        System.out.println("OnUseCaseErrorCalled:" + response.getFailReasons());
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