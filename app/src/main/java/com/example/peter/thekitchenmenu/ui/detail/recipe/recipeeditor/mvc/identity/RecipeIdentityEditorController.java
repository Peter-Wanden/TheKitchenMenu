package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity;

import androidx.core.util.Pair;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeIdentityEditorController
        implements
        RecipeIdentityEditorView.Listener,
        UseCaseBase.Callback<RecipeIdentityResponse> {

    // Allows for different instances of the same use case macro, with the same domain data,
    // to communicate that a component of the use macro case has changed so the listener can update
    // its data.
    public interface UseCaseComponentChangedListener {
        void componentChanged(String domainId);
    }

    private static final String TAG = "tkm-" + RecipeIdentityEditorController.class.
            getSimpleName() + ": ";

    private RecipeIdentityEditorView view;
    private boolean isDataLoading;

    private UseCaseHandler handler;
    private String recipeDomainId = "";
    private Recipe recipe;
    private RecipeIdentityResponse response;

    private List<UseCaseComponentChangedListener> componentChangedListeners = new ArrayList<>();

    public RecipeIdentityEditorController(UseCaseHandler handler, Recipe recipe) {
        this.handler = handler;
        this.recipe = recipe;
        response = new RecipeIdentityResponse.Builder().getDefault().build();
    }

    void bindView(RecipeIdentityEditorView view) {
        this.view = view;
    }

    public void setRecipeDomainId(String recipeDomainId) {
        this.recipeDomainId = recipeDomainId;
    }

    public void registerUseCaseComponentChangedListener(UseCaseComponentChangedListener listener) {
        componentChangedListeners.add(listener);
    }

    public void unregisterUseCaseComponentChangedListener(UseCaseComponentChangedListener listener){
        componentChangedListeners.remove(listener);
    }

    void onStart() {
        view.registerListener(this);
        recipe.registerComponentListener(new Pair<>(ComponentName.IDENTITY, this));
        loadData();
    }

    private void loadData() {
        isDataLoading = true;

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(recipeDomainId).
                build();
        handler.executeAsync(recipe, request, this);
    }

    void onStop() {
        view.unregisterListener(this);
        recipe.unregisterComponentListener(new Pair<>(ComponentName.IDENTITY, this));
    }

    @Override
    public void identityViewOnTitleChanged(String title) {
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(response).
                setDomainModel(
                        new RecipeIdentityRequest.DomainModel.
                                Builder().
                                basedOnResponseModel(response.getDomainModel()).
                                setTitle(title).
                                build()).
                build();
        handler.executeAsync(recipe, request, this);
    }

    @Override
    public void identityViewOnDescriptionChanged(String description) {
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(response).
                setDomainModel(
                        new RecipeIdentityRequest.DomainModel.
                                Builder().
                                basedOnResponseModel(response.getDomainModel()).
                                setDescription(description).
                                build()).
                build();
        handler.executeAsync(recipe, request, this);
    }

    @Override
    public void onUseCaseSuccess(RecipeIdentityResponse response) {
        if (isStateChanged(response)) {
            RecipeIdentityEditorController.this.response = response;
            view.updateViewWithOnSuccessResponse(response);
            notifyUseCaseComponentChangedListeners();
        }
    }

    private void notifyUseCaseComponentChangedListeners() {
        componentChangedListeners.forEach(listener -> listener.componentChanged(recipeDomainId));
    }

    @Override
    public void onUseCaseError(RecipeIdentityResponse response) {
        isDataLoading = false;
        if (isStateChanged(response)) {
            RecipeIdentityEditorController.this.response = response;
            view.updateViewWithOnErrorResponse(response);
            notifyUseCaseComponentChangedListeners();
        }
    }

    private boolean isStateChanged(RecipeIdentityResponse response) {
        return !this.response.equals(response);
    }
}
