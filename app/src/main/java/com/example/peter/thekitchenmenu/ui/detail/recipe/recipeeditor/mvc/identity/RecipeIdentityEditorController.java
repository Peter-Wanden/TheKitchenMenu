package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;

import java.util.List;

public class RecipeIdentityEditorController
        implements
        RecipeIdentityEditorView.Listener,
        UseCaseBase.Callback<RecipeIdentityResponse> {

    private static final String TAG = "tkm-" + RecipeIdentityEditorController.class.
            getSimpleName() + ": ";

    private RecipeIdentityEditorView view;
    private boolean isUpdatingUi;
    private boolean isDataLoading;

    private UseCaseHandler handler;
    private Recipe recipe;
    private RecipeIdentityResponse response;

    public RecipeIdentityEditorController(UseCaseHandler handler) {
        this.handler = handler;
        response = new RecipeIdentityResponse.Builder().getDefault().build();
    }

    public void bindView(RecipeIdentityEditorView view) {
        this.view = view;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    void onStart() {
        view.registerListener(this);
        recipe.registerComponentListener(new Pair<>(ComponentName.IDENTITY, this));
        loadData();
    }

    private void loadData() {
        isDataLoading = true;
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().getDefault().build();
        handler.executeAsync(recipe, request, this);
    }

    void onStop() {
        view.unregisterListener(this);
        recipe.unregisterComponentListener(new Pair<>(ComponentName.IDENTITY, this));
    }

    @Override
    public void identityViewOnTitleChanged(String title) {

    }

    @Override
    public void identityViewOnDescriptionChanged(String description) {

    }

    @Override
    public void onUseCaseSuccess(RecipeIdentityResponse response) {
        if (isStateChanged(response)) {
            RecipeIdentityEditorController.this.response = response;
            processUseCaseSuccessResponse();
        }
    }

    private void processUseCaseSuccessResponse() {
        clearErrors();
        updateView();
    }

    private void clearErrors() {
        view.displayTitleTooShortErrorMessage();
        view.displayDescriptionTooLongErrorMessage();
    }

    private void updateView() {
        RecipeIdentityResponse.DomainModel domainModel = response.getModel();
        isUpdatingUi = true;
        view.setTitle(domainModel.getTitle());
        view.setDescription(domainModel.getDescription());
        isUpdatingUi = false;
    }

    @Override
    public void onUseCaseError(RecipeIdentityResponse response) {
        isDataLoading = false;
        if (isStateChanged(response)) {
            RecipeIdentityEditorController.this.response = response;
            processUseCaseErrorResponse(response);
        }
    }

    private void processUseCaseErrorResponse(RecipeIdentityResponse response) {
        List<FailReasons> failReasons = response.getMetadata().getFailReasons();

        if (failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
            view.showDataLoadingError();
        }
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT)) {
            view.displayTitleTooShortErrorMessage();
        }
        if (failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG)) {
            view.displayTitleTooLongErrorMessage();
        }
        if (failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG)) {
            view.displayDescriptionTooLongErrorMessage();
        }
    }

    private boolean isStateChanged(RecipeIdentityResponse response) {
        return !this.response.equals(response);
    }
}
