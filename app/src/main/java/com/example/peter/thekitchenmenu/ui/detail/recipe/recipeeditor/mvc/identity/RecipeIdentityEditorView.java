package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;

public interface RecipeIdentityEditorView
        extends
        ObservableViewMvc<RecipeIdentityEditorView.Listener> {

    interface Listener {

        void identityViewOnTitleChanged(String title);

        void identityViewOnDescriptionChanged(String description);
    }

    void updateViewWithOnSuccessResponse(RecipeIdentityResponse response);

    void updateViewWithOnErrorResponse(RecipeIdentityResponse response);

    void showDataLoadingError();

    void hideDataLoadingError();
}
