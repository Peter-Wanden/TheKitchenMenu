package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc.identity;

import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;

public interface RecipeIdentityEditorView
        extends
        ObservableViewMvc<RecipeIdentityEditorView.Listener> {

    interface Listener {

        void identityViewOnTitleChanged(String title);

        void identityViewOnDescriptionChanged(String description);
    }

    void setTitleLabelResourceId(int titleLabelResourceId);

    void setTitle(String title);

    void displayTitleTooShortErrorMessage();

    void displayTitleTooLongErrorMessage();

    void setDescriptionLabel(int descriptionLabelResourceId);

    void setDescription(String description);

    void displayDescriptionTooLongErrorMessage();

    void showDataLoadingError();

    void hideDataLoadingError();
}
